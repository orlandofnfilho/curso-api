package com.ficr.edu.bancoapi.services;

import com.ficr.edu.bancoapi.dto.ContaEntradaDTO;
import com.ficr.edu.bancoapi.dto.TransferenciaDTO;
import com.ficr.edu.bancoapi.dto.TransacaoDTO;
import com.ficr.edu.bancoapi.entities.Conta;
import com.ficr.edu.bancoapi.exceptions.BusinessRuleException;
import com.ficr.edu.bancoapi.exceptions.ResourceNotFoundException;
import com.ficr.edu.bancoapi.repositories.ContaRepository;
import com.ficr.edu.bancoapi.templates.ContaTemplate;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ContaServiceTest {


    @Mock
    private ContaRepository contaRepository;

    @InjectMocks
    private ContaService contaService;

    private Conta conta;

    private Conta contaOrigin;
    private Conta contaDestino;
    private ContaEntradaDTO entradaDTO;
    private ContaEntradaDTO updateDTO;

    private TransferenciaDTO transferenciaDTO;

    private TransacaoDTO transacaoDTO;



    @BeforeEach
    void setup(){
        conta = ContaTemplate.entidade();
        contaOrigin = ContaTemplate.contaOrigin();
        contaDestino = ContaTemplate.contaDestino();
        entradaDTO = ContaTemplate.entradaDTO();
        updateDTO = ContaTemplate.updateDTO();
        transferenciaDTO = ContaTemplate.transferenciaDTO();
        transacaoDTO = ContaTemplate.transacaoDTO();
    }

    @Test
    void deveriaSalvarUmaNovaConta(){
        when(contaRepository.save(any())).thenReturn(conta);
        Conta response = contaService.saveConta(entradaDTO);

        assertAll(ContaTemplate.CHECK_IF_NOT_NULL_OBJECT_CLASS_AND_ATTRIBUTES_VALUES
                , () -> assertNotNull(response)
                , () -> assertEquals(Conta.class, response.getClass())
                , () -> assertEquals(conta.getId(), response.getId())
                , () -> assertEquals(conta.getAgencia(), response.getAgencia())
                , () -> assertEquals(conta.getNumero(), response.getNumero())
                , () -> assertEquals(conta.getCliente(), response.getCliente())
                , () -> assertEquals(conta.getSaldo(), response.getSaldo()));
    }

    @Test
    void deveriaListarTodasAsContas(){
        when(contaRepository.findAll()).thenReturn(List.of(conta));

        List<Conta> response = contaService.getAllContas();
        assertAll(ContaTemplate.CHECK_IF_LIST_NOT_NULL_AND_CONTENT
                , () -> assertNotNull(response)
                , () -> assertEquals(Conta.class, response.get(0).getClass())
                , () -> assertEquals(1, response.size())
                , () -> assertEquals(conta.getId(), response.get(0).getId())
                , () -> assertEquals(conta.getNumero(), response.get(0).getNumero()));

    }

    @Test
    void deveriaBuscarUmaContaPorId(){
        when(contaRepository.findById(anyLong())).thenReturn(Optional.of(conta));

        Conta response = contaService.getContaById(conta.getId());
        assertAll(ContaTemplate.CHECK_IF_NOT_NULL_OBJECT_CLASS_AND_ATTRIBUTES_VALUES
                , () -> assertNotNull(response)
                , () -> assertEquals(Conta.class, response.getClass())
                , () -> assertEquals(conta.getId(), response.getId())
                , () -> assertEquals(conta.getAgencia(), response.getAgencia())
                , () -> assertEquals(conta.getNumero(), response.getNumero())
                , () -> assertEquals(conta.getCliente(), response.getCliente())
                , () -> assertEquals(conta.getSaldo(), response.getSaldo()));

    }

    @Test
    void deveriaBuscarUmaContaPorIdERetornarUmaException(){
        when(contaRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> contaService.getContaById(ContaTemplate.ID));
        assertEquals(String.format(ContaTemplate.CONTA_NAO_ENCONTRADA, ContaTemplate.ID), exception.getMessage());
    }

    @Test
    void deveriaDeletarUmaContaPeloId(){
        when(contaRepository.findById(anyLong())).thenReturn(Optional.of(conta));
        doNothing().when(contaRepository).deleteById(anyLong());
        contaService.deleteById(ContaTemplate.ID);
        verify(contaRepository, times(1)).deleteById(ContaTemplate.ID);
    }

    @Test
    void deveriaDeletarUmaContaPeloIdELancarUmaException(){
        when(contaRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> contaService.deleteById(ContaTemplate.ID));
        assertEquals(String.format(ContaTemplate.CONTA_NAO_ENCONTRADA, ContaTemplate.ID), exception.getMessage());
    }

    @Test
    void deveriaAtualizarUmaConta(){
        when(contaRepository.findById(anyLong())).thenReturn(Optional.of(conta));
        when(contaRepository.save(any())).thenReturn(conta);

        Conta response = contaService.updateConta(updateDTO, ContaTemplate.ID);
        assertAll(ContaTemplate.CHECK_IF_NOT_NULL_OBJECT_CLASS_AND_ATTRIBUTES_VALUES
                , () -> assertNotNull(response)
                , () -> assertEquals(Conta.class, response.getClass())
                , () -> assertEquals(1L, response.getId())
                , () -> assertEquals("7856", response.getAgencia())
                , () -> assertEquals("0580518", response.getNumero())
                , () -> assertEquals("Maria da Silva", response.getCliente())
                , () -> assertEquals(0.0, response.getSaldo()));
    }

    @Test
    void deveriaLancarUmaExceptionQuandoAtualizaUmaConta(){
        when(contaRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
               ()-> contaService.updateConta(updateDTO, ContaTemplate.ID));
        assertEquals(String.format(ContaTemplate.CONTA_NAO_ENCONTRADA, ContaTemplate.ID), exception.getMessage());
    }

    @Test
    void deveriaRealizarUmaTransferenciaEntreContas(){
        when(contaRepository.findById(contaOrigin.getId())).thenReturn(Optional.of(contaOrigin));
        when(contaRepository.findById(contaDestino.getId())).thenReturn(Optional.of(contaDestino));
        when(contaRepository.saveAll(anyCollection())).thenReturn(List.of(contaOrigin, contaDestino));

        String response = contaService.realizarTranferencia(transferenciaDTO);
        assertAll(ContaTemplate.CHECK_IF_NOT_NULL_OBJECT_CLASS_AND_ATTRIBUTES_VALUES
                , () -> assertNotNull(response)
                , () -> assertEquals(String.class, response.getClass())
                , () -> assertEquals(600.0, contaOrigin.getSaldo())
                , () -> assertEquals(500.0, contaDestino.getSaldo())
                , () -> assertEquals("Transferência realizada com sucesso!", response));
    }

    @Test
    void deveriaRealizarUmaTransferenciaERetornarUmaResourceNotFoundException(){
        when(contaRepository.findById(contaOrigin.getId())).thenReturn(Optional.of(contaOrigin));
        when(contaRepository.findById(contaDestino.getId())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> contaService.realizarTranferencia(transferenciaDTO));
        assertEquals(String.format(ContaTemplate.CONTA_NAO_ENCONTRADA
                , transferenciaDTO.getIdContaDestino()), exception.getMessage());
    }

    @Test
    void deveriaRealizarUmaTransferenciaComSaldoInvalidoERetornarUmaException(){
        when(contaRepository.findById(contaOrigin.getId())).thenReturn(Optional.of(contaOrigin));
        when(contaRepository.findById(contaDestino.getId())).thenReturn(Optional.of(contaDestino));

        contaOrigin.setSaldo(0.0);
        BusinessRuleException exception = assertThrows(BusinessRuleException.class,
                () -> contaService.realizarTranferencia(transferenciaDTO));
        assertEquals(ContaTemplate.SALDO_INVALIDO, exception.getMessage());
    }

    @Test
    void deveriaRealizarUmaTransferenciaComValorInvalidoERetornarUmaException(){
        when(contaRepository.findById(contaOrigin.getId())).thenReturn(Optional.of(contaOrigin));
        when(contaRepository.findById(contaDestino.getId())).thenReturn(Optional.of(contaDestino));

        transferenciaDTO.setValor(0.0);
        BusinessRuleException exception = assertThrows(BusinessRuleException.class,
                () -> contaService.realizarTranferencia(transferenciaDTO));
        assertEquals(ContaTemplate.VALOR_INVALIDO, exception.getMessage());
    }

    @Test
    void deveriaRealizarUmDeposito(){
        when(contaRepository.findById(anyLong())).thenReturn(Optional.of(conta));
        when(contaRepository.save(any())).thenReturn(conta);

        String response = contaService.depositar(transacaoDTO);
        assertAll(ContaTemplate.CHECK_IF_NOT_NULL_OBJECT_CLASS_AND_ATTRIBUTES_VALUES
                , () -> assertNotNull(response)
                , () -> assertEquals(String.class, response.getClass())
                , () -> assertEquals("Deposito realizado com sucesso!", response)
                , () -> assertEquals(100.0, conta.getSaldo()));
    }

    @Test
    void deveriaRealizarUmDepositoERetornarUmaResourceNotFoundException(){
        when(contaRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> contaService.depositar(transacaoDTO));
        assertEquals(String.format(ContaTemplate.CONTA_NAO_ENCONTRADA
                ,transacaoDTO.getIdConta()), exception.getMessage());
    }
    @Test
    void deveriaRealizarUmDepositoComValorInvalidoERetornarUmaException(){
        transacaoDTO.setValor(0.0);
        BusinessRuleException exception = assertThrows(BusinessRuleException.class,
                () -> contaService.depositar(transacaoDTO));
        assertEquals(ContaTemplate.VALOR_INVALIDO, exception.getMessage());
    }

    @Test
    void deveriaRealizarUmSaque(){
        when(contaRepository.findById(anyLong())).thenReturn(Optional.of(conta));
        when(contaRepository.save(any())).thenReturn(conta);

        conta.setSaldo(200.0);
        String response = contaService.sacar(transacaoDTO);
        assertAll(ContaTemplate.CHECK_IF_NOT_NULL_OBJECT_CLASS_AND_ATTRIBUTES_VALUES
                , () -> assertNotNull(response)
                , () -> assertEquals(String.class, response.getClass())
                , () -> assertEquals("Saque realizado com sucesso!", response)
                , () -> assertEquals(100.0, conta.getSaldo()));
    }

    @Test
    void deveriaRealizarUmSaqueERetornarUmaResourceNotFoundException(){
        when(contaRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> contaService.sacar(transacaoDTO));
        assertEquals(String.format(ContaTemplate.CONTA_NAO_ENCONTRADA
                ,transacaoDTO.getIdConta()), exception.getMessage());
    }

    @Test
    void deveriaRealizarUmSaqueComSaldoInvalidoERetornarUmaException(){
        when(contaRepository.findById(anyLong())).thenReturn(Optional.of(conta));
        BusinessRuleException exception = assertThrows(BusinessRuleException.class,
                () -> contaService.sacar(transacaoDTO));
        assertEquals(ContaTemplate.SALDO_INVALIDO, exception.getMessage());
    }

    @Test
    void deveriaRealizarUmSaqueComValorInvalidoERetornarUmaException(){
        transacaoDTO.setValor(0.0);
        BusinessRuleException exception = assertThrows(BusinessRuleException.class,
                () -> contaService.sacar(transacaoDTO));
        assertEquals(ContaTemplate.VALOR_INVALIDO, exception.getMessage());
    }

    @Test
    void deveriaVerificarUmValorValido(){
        assertDoesNotThrow(() -> ContaService.verificaValor(100.00));
    }

    @Test
    void deveriaVerificarUmValorERetornarUmaException(){
        BusinessRuleException exception = assertThrows(BusinessRuleException.class,
                () -> ContaService.verificaValor(0.00));
        assertEquals(ContaTemplate.VALOR_INVALIDO, exception.getMessage());
    }
}
