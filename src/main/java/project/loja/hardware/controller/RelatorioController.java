package project.loja.hardware.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.loja.hardware.service.RelatorioService;

@RestController
@RequestMapping("/relatorio")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;
    @GetMapping("/1")
    public ResponseEntity<List<Map<String, Object>>> getTopProdutos() {
        List<Map<String, Object>> relatorio = relatorioService.gerarRelatorioTopProdutos();
        return new ResponseEntity<>(relatorio, HttpStatus.OK);
    }

    @GetMapping("/2")
    public ResponseEntity<List<Map<String, Object>>> getTopFabricantes() {
        List<Map<String, Object>> relatorio = relatorioService.gerarRelatorioTopFabricantes();
        return new ResponseEntity<>(relatorio, HttpStatus.OK);
    }

    @GetMapping("/3")
    public ResponseEntity<List<Map<String, Object>>> getTopCategorias() {
        List<Map<String, Object>> relatorio = relatorioService.gerarRelatorioTopCategorias();
        return new ResponseEntity<>(relatorio, HttpStatus.OK);
    }

    @GetMapping("/4")
    public ResponseEntity<List<Map<String, Object>>> getVendasUltimoAno() {
        List<Map<String, Object>> relatorio = relatorioService.gerarHistoricoVendasUltimoAno();
        return new ResponseEntity<>(relatorio, HttpStatus.OK);
    }

    @GetMapping("/5")
    public ResponseEntity<List<Map<String, Object>>> getBestProductCombination() {
        List<Map<String, Object>> relatorio = relatorioService.gerarRelatorioMelhoresCombinacoesDeProdutos();
        return new ResponseEntity<>(relatorio, HttpStatus.OK);
    }

    @GetMapping("/6")
    public ResponseEntity<List<Map<String, Object>>> getProdutosComBaixoEstoque() {
        List<Map<String, Object>> relatorio = relatorioService.gerarRelatorioProdutosComBaixoEstoque();
        return new ResponseEntity<>(relatorio, HttpStatus.OK);
    }
}
