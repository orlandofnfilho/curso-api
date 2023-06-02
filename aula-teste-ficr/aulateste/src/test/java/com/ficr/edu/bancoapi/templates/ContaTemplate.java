package com.ficr.edu.bancoapi.templates;

import com.ficr.edu.bancoapi.dto.ContaEntradaDTO;
import com.ficr.edu.bancoapi.dto.TransferenciaDTO;
import com.ficr.edu.bancoapi.dto.TransacaoDTO;
import com.ficr.edu.bancoapi.entities.Conta;

public class ContaTemplate {

    public static final Long ID = 1L;
    public static final String AGENCIA = "1234";
    public static final String NUMERO = "1234567";
    public static final String CLIENTE = "João da Silva";

    public static final String CHECK_IF_NOT_NULL_OBJECT_CLASS_AND_ATTRIBUTES_VALUES =
            "Check if not null, object class and attributes values";

    public static final String CHECK_IF_LIST_NOT_NULL_AND_CONTENT = "Check if list not null and content";

    public static final String SALDO_INVALIDO = "Você nao tem saldo suficiente para realizar a operaçao!";

    public static final String VALOR_INVALIDO = "O valor precisa ser maior que zero";

    public static final String CONTA_NAO_ENCONTRADA = "Conta nao encontrada id: %d";

    public static final String URI = "/contas";


    public static Conta entidade(){
        return new Conta.Builder()
                .id(ID)
                .agencia(AGENCIA)
                .numero(NUMERO)
                .cliente(CLIENTE)
                .build();
    }

    public static Conta contaOrigin(){
        return new Conta.Builder()
                .id(1L)
                .agencia("3325")
                .numero("0045619")
                .cliente("Luiz Felipe")
                .saldo(800.0)
                .build();
    }


    public static Conta contaDestino(){
        return new Conta.Builder()
                .id(2L)
                .agencia("3325")
                .numero("0065784")
                .cliente("José Souza")
                .saldo(300.0)
                .build();
    }

    public static TransferenciaDTO transferenciaDTO(){
        return new TransferenciaDTO.Builder()
                .idContaOrigin(1L)
                .idContaDestino(2L)
                .valor(200.0)
                .build();
    }

    public static TransacaoDTO transacaoDTO(){
        return new TransacaoDTO.Builder()
                .idConta(1L)
                .valor(100.0)
                .build();
    }

    public static ContaEntradaDTO entradaDTO(){
        return new ContaEntradaDTO.Builder()
                .agencia(AGENCIA)
                .numero(NUMERO)
                .cliente(CLIENTE)
                .build();
    }

    public static ContaEntradaDTO updateDTO(){
        return new ContaEntradaDTO.Builder()
                .agencia("7856")
                .numero("0580518")
                .cliente("Maria da Silva")
                .build();
    }
}
