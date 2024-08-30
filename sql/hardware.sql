-- -----------------------------------------------------
-- Schema bd_hardware
-- ---------------------------------------------------
DROP SCHEMA IF EXISTS bd_hardware CASCADE;

CREATE SCHEMA IF NOT EXISTS bd_hardware;

-- -----------------------------------------------------
-- Table bd_hardware.cliente
-- -----------------------------------------------------
CREATE TABLE
  IF NOT EXISTS bd_hardware.cliente (
    id_cliente SERIAL PRIMARY KEY,
    cpf CHAR(11) NOT NULL,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    CONSTRAINT uk_email UNIQUE (email),
    CONSTRAINT uk_cpf UNIQUE (cpf)
  );

-- -----------------------------------------------------
-- Table bd_hardware.categoria
-- -----------------------------------------------------
CREATE TABLE
  IF NOT EXISTS bd_hardware.categoria (
    id_categoria SERIAL PRIMARY KEY,
    nome VARCHAR(45) NOT NULL,
    CONSTRAINT uk_categoria UNIQUE (nome)
  );

-- -----------------------------------------------------
-- Table bd_hardware.subcategoria
-- -----------------------------------------------------
CREATE TABLE
  IF NOT EXISTS bd_hardware.subcategoria (
    id_subcategoria SERIAL PRIMARY KEY,
    nome VARCHAR(45) NOT NULL,
    id_categoria INT NOT NULL,
    CONSTRAINT uk_subcategoria UNIQUE (nome),
    CONSTRAINT fk_subcategoria_categoria FOREIGN KEY (id_categoria) REFERENCES bd_hardware.categoria (id_categoria) ON DELETE NO ACTION ON UPDATE NO ACTION
  );

-- -----------------------------------------------------
-- Table bd_hardware.fabricante
-- -----------------------------------------------------
CREATE TABLE
  IF NOT EXISTS bd_hardware.fabricante (
    id_fabricante SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    CONSTRAINT uk_fabricante UNIQUE (nome)
  );

-- -----------------------------------------------------
-- Table bd_hardware.produtos
-- -----------------------------------------------------
CREATE TABLE
  IF NOT EXISTS bd_hardware.produtos (
    id_produtos SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    preco NUMERIC(10, 2) NOT NULL CHECK (preco >= 0),
    quantidade_estoque INT NOT NULL CHECK (quantidade_estoque >= 0),
    descricao TEXT,
    id_subcategoria INT NOT NULL,
    id_fabricante INT NOT NULL,
    CONSTRAINT fk_produtos_subcategoria FOREIGN KEY (id_subcategoria) REFERENCES bd_hardware.subcategoria (id_subcategoria) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT fk_produtos_fabricante FOREIGN KEY (id_fabricante) REFERENCES bd_hardware.fabricante (id_fabricante) ON DELETE NO ACTION ON UPDATE NO ACTION
  );

-- -----------------------------------------------------
-- Table bd_hardware.itens_carrinho
-- -----------------------------------------------------
CREATE TABLE
  IF NOT EXISTS bd_hardware.itens_carrinho (
    id_cliente INT NOT NULL,
    id_produtos INT NOT NULL,
    quantidade INT NOT NULL CHECK (quantidade >= 0),
    PRIMARY KEY (id_cliente, id_produtos),
    CONSTRAINT fk_itens_carrinho_cliente FOREIGN KEY (id_cliente) REFERENCES bd_hardware.cliente (id_cliente) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT fk_itens_carrinho_produtos FOREIGN KEY (id_produtos) REFERENCES bd_hardware.produtos (id_produtos) ON DELETE NO ACTION ON UPDATE NO ACTION
  );

-- -----------------------------------------------------
-- Table bd_hardware.compras
-- -----------------------------------------------------
CREATE TABLE
  IF NOT EXISTS bd_hardware.compras (
    id_compras SERIAL,
    id_cliente INT NOT NULL,
    data_compra DATE NOT NULL,
    valor_total NUMERIC(10, 2) NOT NULL CHECK (valor_total >= 0),
    PRIMARY KEY (id_compras, id_cliente),
    CONSTRAINT fk_compras_cliente FOREIGN KEY (id_cliente) REFERENCES bd_hardware.cliente (id_cliente) ON DELETE NO ACTION ON UPDATE NO ACTION
  );

-- -----------------------------------------------------
-- Table bd_hardware.itens_compra
-- -----------------------------------------------------
CREATE TABLE
  IF NOT EXISTS bd_hardware.itens_compra (
    id_compras INT NOT NULL,
    id_cliente INT NOT NULL,
    id_produtos INT NOT NULL,
    quantidade INT NOT NULL CHECK (quantidade >= 0),
    preco_unitario NUMERIC(10, 2) NOT NULL CHECK (preco_unitario >= 0),
    PRIMARY KEY (id_compras, id_produtos, id_cliente),
    CONSTRAINT fk_itens_compra_compras FOREIGN KEY (id_compras, id_cliente) REFERENCES bd_hardware.compras (id_compras, id_cliente) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT fk_itens_compra_produtos FOREIGN KEY (id_produtos) REFERENCES bd_hardware.produtos (id_produtos) ON DELETE NO ACTION ON UPDATE NO ACTION
  );

-- --------------------------------------------------------------------
-- Função para verificar se o valor total corresponde à soma dos itens
-- --------------------------------------------------------------------
CREATE OR REPLACE FUNCTION verificar_valor_total()
RETURNS TRIGGER AS $$
DECLARE
    soma_itens NUMERIC(10, 2);
BEGIN
    -- Calcula a soma dos itens da compra
    SELECT SUM(preco_unitario * quantidade)
    INTO soma_itens
    FROM bd_hardware.itens_compra
    WHERE id_compras = NEW.id_compras AND id_cliente = NEW.id_cliente;

    -- Verifica se a soma dos itens corresponde ao valor total
    IF soma_itens IS DISTINCT FROM NEW.valor_total THEN
        RAISE EXCEPTION 'O valor total informado (%.2f) não corresponde à soma dos itens (%.2f)', NEW.valor_total, soma_itens;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- -----------------------------------------------------------------------------
-- Trigger para verificar o valor total antes de inserir ou atualizar uma compra
-- -----------------------------------------------------------------------------
CREATE TRIGGER trigger_verificar_valor_total
BEFORE INSERT OR UPDATE ON bd_hardware.compras
FOR EACH ROW
EXECUTE FUNCTION verificar_valor_total();