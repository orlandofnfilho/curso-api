package com.ficr.edu.bancoapi.services;

import com.ficr.edu.bancoapi.dto.ContaEntradaDTO;
import com.ficr.edu.bancoapi.dto.TransacaoDTO;
import com.ficr.edu.bancoapi.dto.TranferenciaDTO;
import com.ficr.edu.bancoapi.entities.Conta;
import com.ficr.edu.bancoapi.exceptions.BusinessRuleException;
import com.ficr.edu.bancoapi.exceptions.ResourceNotFoundException;
import com.ficr.edu.bancoapi.repositories.ContaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContaService {

    private final ContaRepository contaRepository;

    public ContaService(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    public Conta saveConta(ContaEntradaDTO contaEntradaDTO) {
        Conta conta = new Conta(contaEntradaDTO.getNumero(),
                contaEntradaDTO.getAgencia(), contaEntradaDTO.getCliente());
        return contaRepository.save(conta);
    }

    public List<Conta> getAllContas() {
        return contaRepository.findAll();
    }

    public Conta getContaById(Long idConta) {
        return contaRepository.findById(idConta).orElseThrow(
                () -> new ResourceNotFoundException("Conta nao encontrada id: "+idConta));
    }

    public void deleteById(Long idConta) {
        this.getContaById(idConta);
        contaRepository.deleteById(idConta);
    }

    public Conta updateConta(ContaEntradaDTO contaEntradaDTO, Long idConta) {
        Conta contaExistente = this.getContaById(idConta);
        contaExistente.setNumero(contaEntradaDTO.getNumero());
        contaExistente.setAgencia(contaEntradaDTO.getNumero());
        contaExistente.setCliente(contaEntradaDTO.getCliente());
        return contaRepository.save(contaExistente);
    }

    public String realizarTranferencia(TranferenciaDTO tranferenciaDTO) {
        Conta contaOrigin = this.getContaById(tranferenciaDTO.getIdContaOrigin());
        Conta contaDestino =  this.getContaById(tranferenciaDTO.getIdContaDestino());

        verificaSaldo(contaOrigin, tranferenciaDTO.getValor());

        contaOrigin.setSaldo(contaOrigin.getSaldo() - tranferenciaDTO.getValor());
        contaDestino.setSaldo(contaDestino.getSaldo() + tranferenciaDTO.getValor());
        contaRepository.saveAll(List.of(contaOrigin, contaDestino));
        return "Transferência realizada com sucesso!";
    }

    public String depositar(TransacaoDTO transacaoDTO) {
        verificaValor(transacaoDTO.getValor());
        Conta contaExistente = this.getContaById(transacaoDTO.getIdConta());
        contaExistente.setSaldo(contaExistente.getSaldo() + transacaoDTO.getValor());
        contaRepository.save(contaExistente);
        return "Deposito realizado com sucesso!";
    }

    public String sacar(TransacaoDTO transacaoDTO) {
        verificaValor(transacaoDTO.getValor());
        Conta contaExistente = this.getContaById(transacaoDTO.getIdConta());
        verificaSaldo(contaExistente, transacaoDTO.getValor());
        contaExistente.setSaldo(contaExistente.getSaldo() - transacaoDTO.getValor());
        contaRepository.save(contaExistente);
        return "Saque realizado com sucesso!";
    }

    private static void verificaValor(Double valor) {
        if(valor <= 0)
            throw new BusinessRuleException("O valor precisa ser maior que zero");
    }

    private static void verificaSaldo(Conta contaOrigin, Double valor) {
        if (contaOrigin.getSaldo() < valor || contaOrigin.getSaldo() <= 0)
            throw new BusinessRuleException("Você nao tem saldo suficiente para realizar a operaçao!");
    }



}
