import os
import random

import psycopg2
from dotenv import load_dotenv
from faker import Faker

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


def populate_cliente(cursor, num_records):
    for _ in range(num_records):
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


def populate_categoria(cursor, categorias):
    for nome in categorias:
        cursor.execute(
            """
            INSERT INTO bd_hardware.categoria (nome)
            VALUES (%s)
            """,
            (nome,),
        )


def populate_subcategoria(cursor, subcategorias):
    cursor.execute("SELECT id_categoria FROM bd_hardware.categoria")
    categorias_ids = [row[0] for row in cursor.fetchall()]

    for nome in subcategorias:
        id_categoria = random.choice(categorias_ids)
        cursor.execute(
            """
            INSERT INTO bd_hardware.subcategoria (nome, id_categoria)
            VALUES (%s, %s)
            """,
            (nome, id_categoria),
        )


def populate_produtos(cursor, num_records):
    cursor.execute("SELECT id_subcategoria FROM bd_hardware.subcategoria")
    subcategorias_ids = [row[0] for row in cursor.fetchall()]

    for _ in range(num_records):
        nome = fake.word()
        preco = round(random.uniform(10.0, 1000.0), 2)
        quantidade_estoque = random.randint(1, 100)
        fabricante = fake.company()
        modelo = fake.word()
        descricao = fake.text()
        id_subcategoria = random.choice(subcategorias_ids)

        cursor.execute(
            """
            INSERT INTO bd_hardware.produtos (nome, preco, quantidade_estoque, fabricante, modelo, descricao, id_subcategoria)
            VALUES (%s, %s, %s, %s, %s, %s, %s)
            """,
            (
                nome,
                preco,
                quantidade_estoque,
                fabricante,
                modelo,
                descricao,
                id_subcategoria,
            ),
        )


def populate_carrinhos(cursor):
    cursor.execute("SELECT id_cliente FROM bd_hardware.cliente")
    clientes_ids = [row[0] for row in cursor.fetchall()]

    for id_cliente in clientes_ids:
        data_criacao = fake.date_time_this_year()
        cursor.execute(
            """
            INSERT INTO bd_hardware.carrinhos (id_cliente, data_criacao)
            VALUES (%s, %s)
            """,
            (id_cliente, data_criacao),
        )


def populate_itens_carrinho(cursor, num_records):
    cursor.execute("SELECT id_produtos FROM bd_hardware.produtos")
    produtos_ids = [row[0] for row in cursor.fetchall()]
    cursor.execute("SELECT id_cliente FROM bd_hardware.cliente")
    clientes_ids = [row[0] for row in cursor.fetchall()]

    existing_pairs = set()

    for _ in range(num_records):
        while True:
            id_produtos = random.choice(produtos_ids)
            id_cliente = random.choice(clientes_ids)
            pair = (id_produtos, id_cliente)

            # Verifica se o par já existe
            if pair not in existing_pairs:
                existing_pairs.add(pair)
                break

        quantidade = random.randint(1, 5)

        cursor.execute(
            """
            INSERT INTO bd_hardware.itens_carrinho (id_produtos, id_cliente, quantidade)
            VALUES (%s, %s, %s)
            """,
            (id_produtos, id_cliente, quantidade),
        )


def populate_compras(cursor, num_records):
    cursor.execute("SELECT id_cliente FROM bd_hardware.cliente")
    clientes_ids = [row[0] for row in cursor.fetchall()]

    for _ in range(num_records):
        id_cliente = random.choice(clientes_ids)
        data_compra = fake.date_time_this_year()
        valor_total = round(random.uniform(50.0, 2000.0), 2)

        cursor.execute(
            """
            INSERT INTO bd_hardware.compras (id_cliente, data_compra, valor_total)
            VALUES (%s, %s, %s)
            RETURNING id_compras
            """,
            (id_cliente, data_compra, valor_total),
        )

        id_compras = cursor.fetchone()[0]
        populate_itens_compra(cursor, id_compras, id_cliente)


def populate_itens_compra(cursor, id_compras, id_cliente):
    cursor.execute("SELECT id_produtos FROM bd_hardware.produtos")
    produtos_ids = [row[0] for row in cursor.fetchall()]

    num_items = random.randint(1, 5)
    for _ in range(num_items):
        id_produtos = random.choice(produtos_ids)
        quantidade = random.randint(1, 3)
        preco_unitario = round(random.uniform(10.0, 1000.0), 2)

        cursor.execute(
            """
            INSERT INTO bd_hardware.itens_compra (id_compras, id_produtos, id_cliente, quantidade, preco_unitario)
            VALUES (%s, %s, %s, %s, %s)
            """,
            (id_compras, id_produtos, id_cliente, quantidade, preco_unitario),
        )


def main():
    with conn.cursor() as cursor:
        conn.autocommit = True
        categorias = ["Periféricos", "Componentes", "Armazenamento", "Redes"]
        subcategorias = [
            "Teclados",
            "Monitores",
            "Processadores",
            "Memórias",
            "HDs",
            "SSDs",
            "Roteadores",
        ]
        escolha = input("Qual tabela deseja popular? ").lower()
        # Populando as tabelas
        if escolha == "cliente":
            qty = int(input("Quantidade: "))
            populate_cliente(cursor, qty)
        elif escolha == "categoria":
            populate_categoria(cursor, categorias)
        elif escolha == "subcategoria":
            populate_subcategoria(cursor, subcategorias)
        elif escolha == "produtos":
            qty = int(input("Quantidade: "))
            populate_produtos(cursor, qty)
        elif escolha == "carrinhos":
            populate_carrinhos(cursor)
        elif escolha == "itens carrinho":
            qty = int(input("Quantidade: "))
            populate_itens_carrinho(cursor, qty)
        elif escolha == "compras":
            qty = int(input("Quantidade: "))
            populate_compras(cursor, qty)
        elif escolha == "all":
            populate_cliente(cursor, 200)
            populate_categoria(cursor, categorias)
            populate_subcategoria(cursor, subcategorias)
            populate_produtos(cursor, 200)
            populate_carrinhos(cursor)
            populate_itens_carrinho(cursor, 200)
            populate_compras(cursor, 200)
        else:
            print("Tabela não encontrada")
    conn.close()


if __name__ == "__main__":
    main()
