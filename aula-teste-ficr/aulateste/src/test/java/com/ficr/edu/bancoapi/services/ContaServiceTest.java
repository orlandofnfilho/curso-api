package com.ficr.edu.bancoapi.services;

import com.ficr.edu.bancoapi.entities.Conta;
import com.ficr.edu.bancoapi.exceptions.ResourceNotFoundException;
import com.ficr.edu.bancoapi.repositories.ContaRepository;
import com.ficr.edu.bancoapi.templates.ContaTemplate;
import static org.junit.jupiter.api.Assertions.*;
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

    @Test
    void deveriaSalvarUmaNovaConta(){
        when(contaRepository.save(any())).thenReturn(ContaTemplate.entidade());
        Conta response = contaService.saveConta(ContaTemplate.contaDTO());

        assertAll(ContaTemplate.CHECK_IF_NOT_NULL_OBJECT_CLASS_AND_ATTRIBUTES_VALUES
                , () -> assertNotNull(response)
                , () -> assertEquals(Conta.class, response.getClass())
                , () -> assertEquals(ContaTemplate.ID, response.getId())
                , () -> assertEquals(ContaTemplate.AGENCIA, response.getAgencia())
                , () -> assertEquals(ContaTemplate.NUMERO, response.getNumero())
                , () -> assertEquals(ContaTemplate.CLIENTE, response.getCliente())
                , () -> assertEquals(150.0, response.getSaldo()));
    }

    @Test
    void deveriaListarTodasAsContas(){
        when(contaRepository.findAll()).thenReturn(List.of(ContaTemplate.entidade()));

        List<Conta> response = contaService.getAllContas();
        assertAll(ContaTemplate.CHECK_IF_LIST_NOT_NULL_AND_CONTENT
                , () -> assertNotNull(response)
                , () -> assertEquals(Conta.class, response.get(0).getClass())
                , () -> assertEquals(1, response.size())
                , () -> assertEquals(ContaTemplate.ID, response.get(0).getId()));
    }

    @Test
    void deveriaBuscarUmaContaPorId(){
        when(contaRepository.findById(anyLong())).thenReturn(Optional.of(ContaTemplate.entidade()));

        Conta response = contaService.getContaById(ContaTemplate.ID);
        assertAll(ContaTemplate.CHECK_IF_NOT_NULL_OBJECT_CLASS_AND_ATTRIBUTES_VALUES
                , () -> assertNotNull(response)
                , () -> assertEquals(Conta.class, response.getClass())
                , () -> assertEquals(ContaTemplate.ID, response.getId())
                , () -> assertEquals(ContaTemplate.AGENCIA, response.getAgencia())
                , () -> assertEquals(ContaTemplate.NUMERO, response.getNumero())
                , () -> assertEquals(ContaTemplate.CLIENTE, response.getCliente())
                , () -> assertEquals(150.0, response.getSaldo()));

    }

    @Test
    void deveriaBuscarUmaContaPorIdERetornarUmaException(){
        when(contaRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> contaService.getContaById(ContaTemplate.ID));
    }

    @Test
    void deveriaDeletarUmaContaPeloId(){
        when(contaRepository.findById(anyLong())).thenReturn(Optional.of(ContaTemplate.entidade()));
        doNothing().when(contaRepository).deleteById(anyLong());
        contaService.deleteById(ContaTemplate.ID);
        verify(contaRepository, times(1)).deleteById(ContaTemplate.ID);
    }

    @Test
    void deveriaDeletarUmaContaPeloIdELancarUmaException(){
        when(contaRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> contaService.deleteById(ContaTemplate.ID));
    }

    @Test
    void deveriaAtualizarUmaConta(){
        when(contaRepository.findById(anyLong())).thenReturn(Optional.of(ContaTemplate.entidade()));
        when(contaRepository.save(any())).thenReturn(ContaTemplate.entidadeAtualizada());

        Conta response = contaService.updateConta(ContaTemplate.contaDTOAtualizada(), ContaTemplate.ID);
        assertAll(ContaTemplate.CHECK_IF_NOT_NULL_OBJECT_CLASS_AND_ATTRIBUTES_VALUES
                , () -> assertNotNull(response)
                , () -> assertEquals(Conta.class, response.getClass())
                , () -> assertEquals(ContaTemplate.ID, response.getId())
                , () -> assertEquals(ContaTemplate.AGENCIA, response.getAgencia())
                , () -> assertEquals(ContaTemplate.NUMERO, response.getNumero())
                , () -> assertEquals(ContaTemplate.NOME, response.getCliente())
                , () -> assertEquals(150.0, response.getSaldo()));
    }

    @Test
    void deveriaLancarUmaExceptionQuandoAtualizaUmaConta(){
        when(contaRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                ()-> contaService.updateConta(ContaTemplate.contaDTOAtualizada(), ContaTemplate.ID));
    }

    @Test
    void deveriaRealizarUmaTransferenciaEntreContas(){
        when(contaRepository.findById(anyLong())).thenReturn(Optional.of(ContaTemplate.entidade()));
        when(contaRepository.findById(anyLong())).thenReturn(Optional.of(ContaTemplate.entidadeDestino()));
        when(contaRepository.saveAll(anyCollection())).thenReturn(List.of(ContaTemplate.entidade(), ContaTemplate.entidadeDestino()));

        String response = contaService.realizarTranferencia(ContaTemplate.tranferenciaDTO());
        assertAll(ContaTemplate.CHECK_IF_NOT_NULL_OBJECT_CLASS_AND_ATTRIBUTES_VALUES
                , () -> assertNotNull(response)
                , () -> assertEquals(String.class, response.getClass())
                , () -> assertEquals("Transferência realizada com sucesso!", response));
    }
}
