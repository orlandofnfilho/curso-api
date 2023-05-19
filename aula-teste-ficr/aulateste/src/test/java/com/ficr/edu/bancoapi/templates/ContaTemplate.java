package com.ficr.edu.bancoapi.templates;

import com.ficr.edu.bancoapi.dto.ContaEntradaDTO;
import com.ficr.edu.bancoapi.dto.TranferenciaDTO;
import com.ficr.edu.bancoapi.dto.TransacaoDTO;
import com.ficr.edu.bancoapi.entities.Conta;

public class ContaTemplate {

    public static final Long ID = 1L;
    public static final String AGENCIA = "1234";
    public static final String NUMERO = "123456";

    public static final String CLIENTE = "Cliente";

    public static final String NOME = "João da Silva";

    public static final String CHECK_IF_NOT_NULL_OBJECT_CLASS_AND_ATTRIBUTES_VALUES =
            "Check if not null, object class and attributes values";

    public static final String CHECK_IF_LIST_NOT_NULL_AND_CONTENT = "Check if list not null and content";


    public static Conta entidade(){
        return new Conta.Builder()
                .id(ID)
                .agencia(AGENCIA)
                .numero(NUMERO)
                .cliente(CLIENTE)
                .saldo(150.0)
                .build();
    }

    public static Conta entidadeDestino(){
        return new Conta.Builder()
                .id(2L)
                .agencia("3325")
                .numero("004561")
                .cliente("Luiz Felipe")
                .saldo(300.0)
                .build();
    }

    public static TranferenciaDTO tranferenciaDTO(){
        return new TranferenciaDTO(1L, 2L, 200.0);
    }

    public static TransacaoDTO transacaoDTO(){
        return new TransacaoDTO(1L, 50.0);
    }

    public static ContaEntradaDTO contaDTO(){
        return new ContaEntradaDTO.Builder()
                .agencia(AGENCIA)
                .numero(NUMERO)
                .cliente(CLIENTE)
                .build();
    }

    public static Conta entidadeAtualizada(){
        return new Conta.Builder()
                .id(ID)
                .agencia(AGENCIA)
                .numero(NUMERO)
                .cliente(NOME)
                .saldo(150.0)
                .build();
    }

    public static ContaEntradaDTO contaDTOAtualizada(){
        return new ContaEntradaDTO.Builder()
                .agencia(AGENCIA)
                .numero(NUMERO)
                .cliente(NOME)
                .build();
    }
}
