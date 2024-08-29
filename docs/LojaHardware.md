Cliente(<u>id</u>, <u>cpf</u>, <u>email</u>, senha, nome)

Categoria(<u>id</u>, <u>nome</u>)

Subcategoria(<u>id</u>, <u>nome</u>, <span style="text-decoration: overline;">id_categoria</span><span style="position: relative; top: -10px; font-size: 0.8em;">Categoria</span>)

Compras(<u>id</u>, dt_compras, valor, <u><span style="text-decoration: overline;">id_compras</span></u><span style="position: relative; top: -10px; font-size: 0.8em;">Compras</span>)

Carrinho(quantidade, <u><span style="text-decoration: overline;">id_produto</span></u><span style="position: relative; top: -10px; font-size: 0.8em;">Produto</span>, <u><span style="text-decoration: overline;">id_carrinho</span></u><span style="position: relative; top: -10px; font-size: 0.8em;">Carrinho</span>)

ItensCompra(quantidade, preco_unitario, <u><span style="text-decoration: overline;">id_compras</span></u><span style="position: relative; top: -10px; font-size: 0.8em;">Compras</span>, <u><span style="text-decoration: overline;">id_produtos</span></u><span style="position: relative; top: -10px; font-size: 0.8em;">Produtos</span>, <u><span style="text-decoration: overline;">id_cliente</span></u><span style="position: relative; top: -10px; font-size: 0.8em;">Cliente</span>)

Produto(<u>id</u>, <u>nome</u>, quantidade, descrição, preço, <u><span style="text-decoration: overline;">id_fabricante</span></u><span style="position: relative; top: -10px; font-size: 0.8em;">Fabricante</span>)

Fabricante(<u>id</u>, <u>nome</u>)