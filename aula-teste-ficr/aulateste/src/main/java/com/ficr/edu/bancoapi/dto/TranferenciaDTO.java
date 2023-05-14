package com.ficr.edu.bancoapi.dto;

public class TranferenciaDTO {
    private Long idContaOrigin;
    private Long idContaDestino;
    private Double valor;

    public TranferenciaDTO() {
    }

    public TranferenciaDTO(Long idContaOrigin, Long idContaDestino, Double valor) {
        this.idContaOrigin = idContaOrigin;
        this.idContaDestino = idContaDestino;
        this.valor = valor;
    }

    public Long getIdContaOrigin() {
        return idContaOrigin;
    }

    public void setIdContaOrigin(Long idContaOrigin) {
        this.idContaOrigin = idContaOrigin;
    }

    public Long getIdContaDestino() {
        return idContaDestino;
    }

    public void setIdContaDestino(Long idContaDestino) {
        this.idContaDestino = idContaDestino;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

}
