package com.ficr.edu.bancoapi.dto;

public class TransacaoDTO {

    private Long idConta;
    private Double valor;

    public TransacaoDTO() {
    }

    public TransacaoDTO(Long idConta, Double valor) {
        this.idConta = idConta;
        this.valor = valor;
    }

    public Long getIdConta() {
        return idConta;
    }

    public void setIdConta(Long idConta) {
        this.idConta = idConta;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}
