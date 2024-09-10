package project.loja.hardware.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.loja.hardware.repository.RelatorioRepository;

import java.util.List;
import java.util.Map;

@Service
public class RelatorioService {

    @Autowired
    private RelatorioRepository relatorioRepository;

    /**
     * Retorna as 10 principais categorias de produtos mais vendidos.
     * @return Lista de categorias e suas quantidades vendidas.
     */
    public List<Map<String, Object>> gerarRelatorioTopCategorias() {
        return relatorioRepository.getTopCategorias();
    }

    /**
     * Retorna os 10 principais produtos mais vendidos.
     * @return Lista de produtos e suas quantidades vendidas.
     */
    public List<Map<String, Object>> gerarRelatorioTopProdutos() {
        return relatorioRepository.getTopProdutos();
    }

    /**
     * Retorna os 10 principais fabricantes com mais vendas.
     * @return Lista de fabricantes e suas quantidades de produtos vendidos.
     */
    public List<Map<String, Object>> gerarRelatorioTopFabricantes() {
        return relatorioRepository.getTopFabricantes();
    }

    /**
     * Retorna o histórico de vendas mensais dos últimos 12 meses.
     * @return Lista de meses e seus totais de vendas.
     */
    public List<Map<String, Object>> gerarHistoricoVendasUltimoAno() {
        return relatorioRepository.getSellHistoryLastYear();
    }

    /**
     * Retorna a lista de produtos com estoque baixo.
     * @return Lista de produtos com estoque abaixo de 10 unidades.
     */
    public List<Map<String, Object>> gerarRelatorioProdutosComBaixoEstoque() {
        return relatorioRepository.getLowStockProdutos();
    }

    /**
     * Retorna as melhores combinações de produtos frequentemente comprados juntos.
     * @return Lista de produtos e suas combinações mais compradas juntas.
     */
    public List<Map<String, Object>> gerarRelatorioMelhoresCombinacoesDeProdutos() {
        return relatorioRepository.getBestProdutoCombination();
    }
}

