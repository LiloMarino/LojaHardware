package project.loja.hardware.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import project.loja.hardware.model.Compra;
import project.loja.hardware.model.ItemCompra;
import project.loja.hardware.repository.CompraRepository;

@Service
public class CompraService {

    @Autowired
    private CompraRepository compraRepository;

    @Transactional
    public int realizarCompra(int idCliente) {
        Compra compra = new Compra();
        compra.setIdCliente(idCliente);
        compra.setDataCompra(Date.from(Instant.now()));
        return compraRepository.save(compra);
    }

    public Compra getCompraById(int idCompra) {
        return compraRepository.findByIdCompra(idCompra);
    }

    public List<Compra> listarComprasPorCliente(int idCliente) {
        return compraRepository.findByIdCliente(idCliente);
    }

    public List<ItemCompra> listarItensCompra(int idCompra) {
        Compra compra = compraRepository.findByIdCompra(idCompra);
        return compraRepository.findItemsByCompra(compra);
    }

    public void atualizarItensCompra(int idCompra, int idProduto, ItemCompra itemCompraAtualizado) {
        compraRepository.updateItensCompra(idCompra, idProduto, itemCompraAtualizado);
    }

    public void deletarCompra(int idCompra) {
        compraRepository.deleteByIdCompra(idCompra);
    }

    public void deletarItemCompra(int idCompra, int idProduto) {
        compraRepository.deleteByIdCompraAndIdProduto(idCompra, idProduto);
    }

    public String gerarNotaFiscal(int idCompra) {
        Compra compra = compraRepository.findByIdCompra(idCompra);
        List<ItemCompra> itensCompra = listarItensCompra(idCompra);

        StringBuilder notaFiscal = new StringBuilder();
        notaFiscal.append("----- NOTA FISCAL -----\n");
        notaFiscal.append("Compra Nº: ").append(compra.getIdCompra()).append("\n");

        // Convert java.util.Date to LocalDate
        Date dataCompra = compra.getDataCompra();
        LocalDate dataCompraLocalDate = Instant.ofEpochMilli(dataCompra.getTime()).atZone(ZoneId.systemDefault())
                .toLocalDate();

        notaFiscal.append("Data da Compra: ")
                .append(dataCompraLocalDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("\n");
        notaFiscal.append("Cliente ID: ").append(compra.getIdCliente()).append("\n");
        notaFiscal.append("-----------------------\n");
        notaFiscal.append("Itens Comprados:\n");

        BigDecimal valorTotal = BigDecimal.ZERO;
        for (ItemCompra item : itensCompra) {
            notaFiscal.append("Produto ID: ").append(item.getIdProduto()).append(" | Quantidade: ")
                    .append(item.getQuantidade()).append(" | Preço Unitário: ").append(item.getPrecoUnitario())
                    .append("\n");
            valorTotal = valorTotal.add(item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())));
        }

        notaFiscal.append("-----------------------\n");
        notaFiscal.append("Valor Total: R$ ").append(valorTotal).append("\n");
        notaFiscal.append("-----------------------\n");
        notaFiscal.append("Obrigado pela compra!\n");

        return notaFiscal.toString();
    }
}
