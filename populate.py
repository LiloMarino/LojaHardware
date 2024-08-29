import os
import random

import psycopg2
from dotenv import load_dotenv
from faker import Faker
from functools import wraps

import psycopg2.sql

# Carregar variáveis de ambiente do arquivo .env
load_dotenv()

# Configurações de conexão com o banco de dados
conn = psycopg2.connect(
    dbname=os.getenv("DB_NAME"),
    user=os.getenv("DB_USER"),
    password=os.getenv("DB_PASSWORD"),
    host=os.getenv("DB_HOST"),
    port=os.getenv("DB_PORT"),
)

fake = Faker("pt_BR")


# Decorator para fazer o loop de inserção
def loop_insertion(func):
    @wraps(func)
    def wrapper(*args, **kwargs):
        qty = int(input("Quantidade: "))
        for _ in range(qty):
            func(*args, **kwargs)

    return wrapper


# Decorator para capturar exceções e continuar a execução
def handle_db_exceptions(func):
    @wraps(func)
    def wrapper(*args, **kwargs):
        try:
            func(*args, **kwargs)
        except psycopg2.IntegrityError as e:
            # Lidar com erros de integridade, como violação de unicidade
            print(f"Erro de integridade: {e}")
            args[0].connection.rollback()  # Desfaz a transação atual
        except psycopg2.DatabaseError as e:
            # Lidar com outros erros de banco de dados
            print(f"Erro de banco de dados: {e}")
            args[0].connection.rollback()  # Desfaz a transação atual
        except Exception as e:
            # Lidar com quaisquer outras exceções
            print(f"Erro inesperado: {e}")
            args[0].connection.rollback()  # Desfaz a transação atual

    return wrapper


# Funções para a inserção
@loop_insertion
@handle_db_exceptions
def populate_cliente(cursor):
    cpf = fake.cpf().replace(".", "").replace("-", "")
    nome = fake.name()
    email = fake.email()
    senha = fake.password()
    cursor.execute(
        """
        INSERT INTO bd_hardware.cliente (cpf, nome, email, senha)
        VALUES (%s, %s, %s, %s)
        """,
        (cpf, nome, email, senha),
    )


@loop_insertion
@handle_db_exceptions
def populate_categoria(cursor):
    nome = input("Categoria: ")
    cursor.execute(
        """
        INSERT INTO bd_hardware.categoria (nome)
        VALUES (%s)
        """,
        (nome,),
    )


@loop_insertion
@handle_db_exceptions
def populate_subcategoria(cursor):
    cursor.execute("SELECT id_categoria FROM bd_hardware.categoria")
    categorias_ids = [row[0] for row in cursor.fetchall()]
    id_categoria = random.choice(categorias_ids)
    nome = input("Subcategoria: ")
    cursor.execute(
        """
        INSERT INTO bd_hardware.subcategoria (nome, id_categoria)
        VALUES (%s, %s)
        """,
        (nome, id_categoria),
    )


@loop_insertion
@handle_db_exceptions
def populate_fabricante(cursor):
    fabricante = fake.company()
    cursor.execute(
        """
        INSERT INTO bd_hardware.fabricante (nome)
        VALUES (%s)
        """,
        (fabricante,),
    )


@loop_insertion
@handle_db_exceptions
def populate_produtos(cursor):
    nome = fake.word()
    preco = round(random.uniform(100.0, 5000.0), 2)
    quantidade_estoque = random.randint(1, 100)
    descricao = fake.text()
    cursor.execute(
        """
        INSERT INTO bd_hardware.produtos (nome, preco, quantidade_estoque, descricao, id_subcategoria, id_fabricante)
        VALUES (%s, %s, %s, %s, 
        (SELECT id_subcategoria FROM bd_hardware.subcategoria ORDER BY RANDOM() LIMIT 1), 
        (SELECT id_fabricante FROM bd_hardware.fabricante ORDER BY RANDOM() LIMIT 1))
        """,
        (
            nome,
            preco,
            quantidade_estoque,
            descricao,
        ),
    )


@loop_insertion
@handle_db_exceptions
def populate_itens_carrinho(cursor):
    quantidade = random.randint(1, 5)
    cursor.execute(
        """
            INSERT INTO bd_hardware.itens_carrinho (id_cliente, id_produtos, quantidade)
            VALUES (
            (SELECT id_cliente FROM bd_hardware.cliente ORDER BY RANDOM() LIMIT 1),
            (SELECT id_produtos FROM bd_hardware.produtos ORDER BY RANDOM() LIMIT 1), %s)
            """,
        (quantidade,),
    )


@loop_insertion
@handle_db_exceptions
def populate_compras(cursor):
    cursor.execute(
        "SELECT id_cliente FROM bd_hardware.itens_carrinho ORDER BY RANDOM() LIMIT 1"
    )
    id_cliente = cursor.fetchone()[0]
    data_compra = fake.date_time_this_year()
    cursor.execute(
        """INSERT INTO compras (id_cliente, data_compra, valor_total)
            VALUES (%s, %s,
            (SELECT SUM(ic.quantidade * p.preco) FROM itens_carrinho AS ic 
                INNER JOIN produtos AS p ON p.id_produtos = ic.id_produtos 
                WHERE ic.id_cliente = %s))
            RETURNING id_compras
            """,
        (id_cliente, data_compra, id_cliente),
    )
    id_compras = cursor.fetchone()[0]
    cursor.execute(
        """INSERT INTO itens_compra (id_compras, id_cliente, id_produtos, quantidade, preco_unitario)
                SELECT 
                    %s,
                    %s, 
                    ic.id_produtos, 
                    ic.quantidade, 
                    p.preco
                FROM itens_carrinho AS ic
                INNER JOIN produtos AS p ON ic.id_produtos = p.id_produtos
                WHERE ic.id_cliente = %s
            """,
        (id_compras, id_cliente, id_cliente),
    )
    cursor.execute(
        """DELETE FROM itens_carrinho 
        WHERE id_cliente = %s 
        """,
        (id_cliente,),
    )
    conn.commit()


def main():
    with conn.cursor() as cursor:
        conn.autocommit = True
        while True:
            escolha = input("Qual tabela deseja popular? ").lower()
            # Populando as tabelas
            if escolha == "cliente":
                populate_cliente(cursor)
            elif escolha == "categoria":
                populate_categoria(cursor)
            elif escolha == "subcategoria":
                populate_subcategoria(cursor)
            elif escolha == "fabricante":
                populate_fabricante(cursor)
            elif escolha == "produtos":
                populate_produtos(cursor)
            elif escolha == "itens carrinho":
                populate_itens_carrinho(cursor)
            elif escolha == "compras":
                conn.autocommit = False
                populate_compras(cursor)
                conn.autocommit = True
            elif escolha == "all":
                populate_cliente(cursor)
                populate_categoria(cursor)
                populate_subcategoria(cursor)
                populate_fabricante(cursor)
                populate_produtos(cursor)
                populate_itens_carrinho(cursor)
                conn.autocommit = False
                populate_compras(cursor)
                conn.autocommit = True
            elif escolha == "exit":
                break
            else:
                print("Tabela não encontrada")
    conn.close()


if __name__ == "__main__":
    main()
