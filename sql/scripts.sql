-- Definir o search_path
SET
  search_path TO bd_hardware;

-- -----------------------------------------------------
-- Table bd_hardware.cliente
-- -----------------------------------------------------
-- CREATE --
INSERT INTO
  cliente (cpf, nome, email, senha)
VALUES
  (
    '12345678921',
    'Murilo',
    'murilo@uel2.br',
    'supersenha123'
  );

-- READ --
SELECT
  *
FROM
  cliente;

-- UPDATE --
UPDATE cliente
SET
  senha = 'senha123'
WHERE
  nome = 'Murilo';

-- DELETE --
DELETE FROM cliente
WHERE
  cpf = '12345678911';

-- -----------------------------------------------------
-- Table bd_hardware.categoria
-- -----------------------------------------------------
-- CREATE --
INSERT INTO
  categoria (nome)
VALUES
  ('Processador');

-- READ --
SELECT
  *
FROM
  categoria;

-- UPDATE --
UPDATE categoria
SET
  nome = 'Placa de Vídeo'
WHERE
  nome = 'Processador';

-- DELETE --
DELETE FROM categoria
WHERE
  nome = 'Processador';

-- -----------------------------------------------------
-- Table bd_hardware.subcategoria
-- -----------------------------------------------------
-- CREATE --
INSERT INTO
  subcategoria (nome, id_categoria)
VALUES
  (
    'Família i3',
    (
      SELECT
        id_categoria
      FROM
        categoria
      WHERE
        categoria.nome = 'Processadores'
    )
  );

-- READ --
SELECT
  categoria.nome,
  subcategoria.nome
FROM
  categoria
  INNER JOIN subcategoria ON categoria.id_categoria = subcategoria.id_categoria;

-- UPDATE --
UPDATE subcategoria
SET
  nome = 'Família i9'
WHERE
  nome = 'Família i3';

-- DELETE --
DELETE FROM subcategoria
WHERE
  nome = 'Família i9';

-- -----------------------------------------------------
-- Table bd_hardware.fabricante
-- -----------------------------------------------------
-- CREATE --
INSERT INTO
  fabricante (nome)
VALUES
  ('AMD1');

-- READ --
SELECT
  *
FROM
  fabricante;

-- UPDATE --
UPDATE fabricante
SET
  nome = 'AMD'
WHERE
  nome = 'AMD1';

-- DELETE --
DELETE FROM fabricante
WHERE
  nome = 'AMD';

-- -----------------------------------------------------
-- Table bd_hardware.produtos
-- -----------------------------------------------------
-- CREATE --
INSERT INTO
  produtos (
    nome,
    preco,
    quantidade_estoque,
    descricao,
    id_subcategoria,
    id_fabricante
  )
VALUES
  (
    'GTX 1080',
    1999.99,
    20,
    '200W, 2GB Memória',
    (
      SELECT
        id_subcategoria
      FROM
        subcategoria
      WHERE
        nome = 'Família i3'
    ),
    (
      SELECT
        id_fabricante
      FROM
        fabricante
      WHERE
        nome = 'AMD'
    )
  );

-- READ --
SELECT
  nome,
  preco,
  quantidade_estoque
FROM
  produtos;

-- UPDATE --
UPDATE produtos
SET
  quantidade_estoque = 10 + (
    SELECT
      quantidade_estoque
    FROM
      produtos
    WHERE
      nome = 'GTX 1080'
  )
WHERE
  nome = 'GTX 1080';

-- DELETE --
DELETE FROM produtos
WHERE
  quantidade_estoque = 0;

-- -----------------------------------------------------
-- Table bd_hardware.itens_carrinho
-- -----------------------------------------------------
-- CREATE --
INSERT INTO
  itens_carrinho (id_cliente, id_produtos, quantidade)
VALUES
  (
    (
      SELECT
        id_cliente
      FROM
        cliente
      WHERE
        nome = 'Murilo'
    ),
    (
      SELECT
        id_produtos
      FROM
        produtos
      WHERE
        nome = 'GTX 1080'
    ),
    1
  );

-- READ --
SELECT
  c.nome AS nome_cliente,
  p.nome,
  ic.quantidade,
  p.preco
FROM
  itens_carrinho AS ic
  INNER JOIN cliente AS c ON ic.id_cliente = c.id_cliente
  INNER JOIN produtos AS p ON p.id_produtos = ic.id_produtos;

-- UPDATE --
UPDATE itens_carrinho
SET
  quantidade = (
    SELECT
      quantidade
    FROM
      itens_carrinho AS ic
      INNER JOIN cliente AS c ON ic.id_cliente = c.id_cliente
      INNER JOIN produtos AS p ON p.id_produtos = ic.id_produtos
    WHERE
      c.nome = 'Murilo'
      AND p.nome = 'GTX 1080'
  ) + 1;

-- DELETE --
DELETE FROM itens_carrinho
WHERE
  id_cliente = (
    SELECT
      id_cliente
    FROM
      cliente
    WHERE
      nome = 'Murilo'
  );

-- ------------------------------------------------------------
-- Table bd_hardware.compras e Table bd_hardware.itens_compra
-- ------------------------------------------------------------
-- CREATE --
-- Dá para fazer com transação no Java --
DO $$
DECLARE
  compras_id INTEGER;
BEGIN
  -- Insere uma nova compra
  INSERT INTO compras (id_cliente, data_compra, valor_total)
  VALUES (
    (SELECT id_cliente FROM cliente WHERE nome = 'Murilo'),
    CURRENT_DATE,
    (
      SELECT SUM(ic.quantidade * p.preco)
      FROM itens_carrinho AS ic
      INNER JOIN cliente AS c ON ic.id_cliente = c.id_cliente
      INNER JOIN produtos AS p ON p.id_produtos = ic.id_produtos
      WHERE c.nome = 'Murilo'
    )
  )
  RETURNING id_compras INTO compras_id;

  -- Insere os itens na tabela itens_compra
  INSERT INTO itens_compra (id_compras, id_cliente, id_produtos, quantidade, preco_unitario)
  SELECT 
    compras_id,
    ic.id_cliente, 
    ic.id_produtos, 
    ic.quantidade, 
    p.preco
  FROM itens_carrinho AS ic
  INNER JOIN produtos AS p ON ic.id_produtos = p.id_produtos
  WHERE ic.id_cliente = (SELECT id_cliente FROM cliente WHERE nome = 'Murilo');

  -- Atualiza a quantidade de estoque dos produtos
  UPDATE produtos
  SET quantidade_estoque = quantidade_estoque - ic.quantidade
  FROM itens_carrinho AS ic
  WHERE produtos.id_produtos = ic.id_produtos
  AND ic.id_cliente = (SELECT id_cliente FROM cliente WHERE nome = 'Murilo');

  -- Remove os itens do carrinho
  DELETE FROM itens_carrinho
  WHERE id_cliente = (SELECT id_cliente FROM cliente WHERE nome = 'Murilo');
END $$;

-- READ --
SELECT
  nome,
  data_compra,
  valor_total
FROM
  compras AS c
  INNER JOIN cliente AS cl ON cl.id_cliente = c.id_cliente;

-- READ ITENS --
SELECT
  cl.nome AS nome_cliente,
  c.data_compra,
  c.valor_total,
  p.nome AS nome_produto,
  ic.quantidade,
  ic.preco_unitario,
  (ic.quantidade * ic.preco_unitario) AS valor_item
FROM
  compras AS c
  INNER JOIN itens_compra ic ON c.id_compras = ic.id_compras
  INNER JOIN cliente cl ON c.id_cliente = cl.id_cliente
  INNER JOIN produtos p ON ic.id_produtos = p.id_produtos
WHERE
  cl.nome = 'Murilo';
