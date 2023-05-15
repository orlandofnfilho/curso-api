package com.ficr.edu.bancoapi.controllers;

import com.ficr.edu.bancoapi.dto.ContaEntradaDTO;
import com.ficr.edu.bancoapi.dto.TransacaoDTO;
import com.ficr.edu.bancoapi.dto.TranferenciaDTO;
import com.ficr.edu.bancoapi.entities.Conta;
import com.ficr.edu.bancoapi.services.ContaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contas")
public class ContaController {

    private final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @PostMapping
    public ResponseEntity<Conta> cadastrarConta(@RequestBody ContaEntradaDTO contaEntradaDTO) {
        Conta response = contaService.saveConta(contaEntradaDTO);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping
    public ResponseEntity<List<Conta>> listarContas() {
        List<Conta> response = contaService.getAllContas();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{idConta}")
    public ResponseEntity<Conta> recuperarConta(@PathVariable Long idConta) {
        Conta response = contaService.getContaById(idConta);
        return ResponseEntity.ok().body(response);

    }

    @DeleteMapping("/{idConta}")
    public ResponseEntity<Object> apagarConta(@PathVariable Long idConta) {
        contaService.deleteById(idConta);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idConta}")
    public ResponseEntity<Conta> alterarConta(@RequestBody Conta conta, @PathVariable Long idConta) {
        Conta response = contaService.updateConta(conta, idConta);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/transferir")
    public ResponseEntity<String> tranferirValor(@RequestBody TranferenciaDTO tranferenciaDTO) {
        String response = contaService.realizarTranferencia(tranferenciaDTO);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/depositar")
    public ResponseEntity<String> depositarValor(@RequestBody TransacaoDTO transacaoDto) {
        String response = contaService.depositar(transacaoDto);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/sacar")
    public ResponseEntity<Object> sacar(@RequestBody TransacaoDTO transacaoDTO) {
        String response = contaService.sacar(transacaoDTO);
        return ResponseEntity.ok().body(response);
    }
}

