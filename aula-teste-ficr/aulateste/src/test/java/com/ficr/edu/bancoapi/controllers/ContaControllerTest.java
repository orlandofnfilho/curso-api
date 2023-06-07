package com.ficr.edu.bancoapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ficr.edu.bancoapi.dto.ContaEntradaDTO;
import com.ficr.edu.bancoapi.dto.TransacaoDTO;
import com.ficr.edu.bancoapi.dto.TransferenciaDTO;
import com.ficr.edu.bancoapi.entities.Conta;
import com.ficr.edu.bancoapi.exceptions.BusinessRuleException;
import com.ficr.edu.bancoapi.exceptions.ControllerExceptionHandler;
import com.ficr.edu.bancoapi.exceptions.ResourceNotFoundException;
import com.ficr.edu.bancoapi.services.ContaService;
import com.ficr.edu.bancoapi.templates.ContaTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

;

@AutoConfigureMockMvc
@ContextConfiguration
@SpringBootTest
public class ContaControllerTest {

    @Mock
    private ContaService contaService;

    @InjectMocks
    private ContaController contaController;

    @Autowired
    private MockMvc mockMvc;
    private Conta conta;
    private ContaEntradaDTO entradaDTO;
    private TransferenciaDTO transferenciaDTO;
    private TransacaoDTO transacaoDTO;
    private ObjectMapper mapper;


    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(contaController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
        conta = ContaTemplate.entidade();
        entradaDTO = ContaTemplate.entradaDTO();
        transacaoDTO = ContaTemplate.transacaoDTO();
        transferenciaDTO = ContaTemplate.transferenciaDTO();
        mapper = new ObjectMapper();
    }

    @Test
    void quandoSalvarUmaContaDeveRetornarStatusOk() throws Exception {
        when(contaService.saveConta(any())).thenReturn(conta);

        String request = mapper.writeValueAsString(entradaDTO);
        this.mockMvc.perform(post(ContaTemplate.URI)
                        .contentType("application/json;charset=UTF-8")
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cliente").value(ContaTemplate.CLIENTE))
                .andExpect(jsonPath("$.numero").value(ContaTemplate.NUMERO))
                .andExpect(jsonPath("$.agencia").value(ContaTemplate.AGENCIA))
                .andDo(print());

    }

    @Test
    void quandoListarContasDeveRetornarStatusOk() throws Exception {
        when(contaService.getAllContas()).thenReturn(List.of(conta));
        this.mockMvc.perform(get(ContaTemplate.URI))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void quandoRecuperarUmaContaValidaPeloIdDeveRetornarStatusOk() throws Exception {
        when(contaService.getContaById(anyLong())).thenReturn(conta);
        this.mockMvc.perform(get(ContaTemplate.URI + "/{idConta}", 1L))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void quandoRecuperarUmaContaInvalidaPeloIdDeveRetornarStatusNotFound() throws Exception {
        when(contaService.getContaById(anyLong())).thenThrow(ResourceNotFoundException.class);
        this.mockMvc.perform(get(ContaTemplate.URI + "/{idConta}", 1L))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void quandoDeletaUmaContaValidaDeveRetornarStatusNoContent() throws Exception {
        doNothing().when(contaService).deleteById(anyLong());
        this.mockMvc.perform(delete(ContaTemplate.URI + "/{idConta}", 1L))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void quandoDeletarUmaContaInvalidaDeveRetornarStatusNotFound() throws Exception {
        doThrow(ResourceNotFoundException.class).when(contaService).deleteById(anyLong());
        this.mockMvc.perform(delete(ContaTemplate.URI + "/{idConta}", 1L))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void quandoAtualizarUmaContaValidaDeveRetornarStatusOk() throws Exception {
        when(contaService.updateConta(any(), anyLong())).thenReturn(conta);
        conta.setCliente("Fernando");

        String request = mapper.writeValueAsString(conta);
        this.mockMvc.perform(put(ContaTemplate.URI + "/{idConta}", 1L)
                        .contentType("application/json;charset=UTF-8")
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cliente").value("Fernando"))
                .andDo(print());
    }

    @Test
    void quandoAtualizarUmaContaValidaDeveRetornarStatusNotFound() throws Exception {
        when(contaService.updateConta(any(), anyLong())).thenThrow(ResourceNotFoundException.class);
        conta.setCliente("Fernando");

        String request = mapper.writeValueAsString(conta);
        this.mockMvc.perform(put(ContaTemplate.URI + "/{idConta}", 1L)
                        .contentType("application/json;charset=UTF-8")
                        .content(request))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void quandoDepositarUmValorEmUmaContaValidaDeveRetornarStatusOk() throws Exception {
        when(contaService.depositar(any())).thenReturn("Deposito realizado com sucesso!");

        String request = mapper.writeValueAsString(transacaoDTO);
        this.mockMvc.perform(post(ContaTemplate.URI + "/depositar")
                        .contentType("application/json;charset=UTF-8")
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(content().string("Deposito realizado com sucesso!"))
                .andDo(print());
    }

    @Test
    void quandoDepositarUmValorEmUmaContaInvalidaDeveRetornarStatusNotFound() throws Exception {
        when(contaService.depositar(any())).thenThrow(ResourceNotFoundException.class);

        String request = mapper.writeValueAsString(transacaoDTO);
        this.mockMvc.perform(post(ContaTemplate.URI + "/depositar")
                        .contentType("application/json;charset=UTF-8")
                        .content(request))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void quandoDepositarUmValorInvalidoEmUmaContaInvalidaDeveRetornarStatusBadRequest() throws Exception {
        when(contaService.depositar(any())).thenThrow(BusinessRuleException.class);

        transacaoDTO.setValor(-100.0);
        String request = mapper.writeValueAsString(transacaoDTO);
        this.mockMvc.perform(post(ContaTemplate.URI + "/depositar")
                        .contentType("application/json;charset=UTF-8")
                        .content(request))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void quandoSacarUmaValorValidoDeUmaContaValidaDeveRetornarStatusOk() throws Exception {
        when(contaService.sacar(any())).thenReturn("Saque realizado com sucesso!");

        conta.setSaldo(500.0);
        String request = mapper.writeValueAsString(transacaoDTO);
        this.mockMvc.perform(post(ContaTemplate.URI + "/sacar")
                        .contentType("application/json;charset=UTF-8")
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(content().string("Saque realizado com sucesso!"))
                .andDo(print());
    }

    @Test
    void quandoSacarUmValorInvalidoDeUmaContaValidaDeveRetornarStatusBadRequest() throws Exception {
        when(contaService.sacar(any())).thenThrow(BusinessRuleException.class);

        String request = mapper.writeValueAsString(transacaoDTO);
        this.mockMvc.perform(post(ContaTemplate.URI + "/sacar")
                        .contentType("application/json;charset=UTF-8")
                        .content(request))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void quandoSacarUmValorValidoDeUmaContaInvalidaDeveRetornarStatusNotFound() throws Exception {
        when(contaService.sacar(any())).thenThrow(ResourceNotFoundException.class);

        String request = mapper.writeValueAsString(transacaoDTO);
        this.mockMvc.perform(post(ContaTemplate.URI + "/sacar")
                        .contentType("application/json;charset=UTF-8")
                        .content(request))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void quandoRealizarUmaTranferenciaEntreContasValidasComSaldoValidoDeveRetornarStatusOk() throws Exception {
        when(contaService.realizarTranferencia(any())).thenReturn("Transferência realizada com sucesso!");

        String request = mapper.writeValueAsString(transferenciaDTO);
        this.mockMvc.perform(post(ContaTemplate.URI + "/transferir")
                        .contentType("application/json;charset=UTF-8")
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(content().string("Transferência realizada com sucesso!"))
                .andDo(print());
    }

    @Test
    void quandoRealizarUmaTransferenciaComContaDestinoInvalidaDeveRetornarStatusNotFound() throws Exception {
        when(contaService.realizarTranferencia(any())).thenThrow(ResourceNotFoundException.class);

        String request = mapper.writeValueAsString(transferenciaDTO);
        this.mockMvc.perform(post(ContaTemplate.URI + "/transferir")
                        .contentType("application/json;charset=UTF-8")
                        .content(request))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void quandoRealizarUmaTransferenciaEntreContasValidasESaldoInvalidoDeveRetornarStatusBadRequest() throws Exception {
        when(contaService.realizarTranferencia(any())).thenThrow(BusinessRuleException.class);

        transferenciaDTO.setValor(1000.0);
        String request = mapper.writeValueAsString(transferenciaDTO);
        this.mockMvc.perform(post(ContaTemplate.URI + "/transferir")
                        .contentType("application/json;charset=UTF-8")
                        .content(request))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}