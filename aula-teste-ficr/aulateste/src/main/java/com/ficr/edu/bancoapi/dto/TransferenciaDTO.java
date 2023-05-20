package com.ficr.edu.bancoapi.dto;

public class TransferenciaDTO {
    private Long idContaOrigin;
    private Long idContaDestino;
    private Double valor;

    public TransferenciaDTO() {
    }

    public TransferenciaDTO(Long idContaOrigin, Long idContaDestino, Double valor) {
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

    public static class Builder {
        private Long idContaOrigin;
        private Long idContaDestino;
        private Double valor;

        public Builder idContaOrigin(Long idContaOrigin) {
            this.idContaOrigin = idContaOrigin;
            return this;
        }

        public Builder idContaDestino(Long idContaDestino) {
            this.idContaDestino = idContaDestino;
            return this;
        }

        public Builder valor(Double valor) {
            this.valor = valor;
            return this;
        }

        public TransferenciaDTO build() {
            TransferenciaDTO transferenciaDTO = new TransferenciaDTO();
            transferenciaDTO.idContaOrigin = this.idContaOrigin;
            transferenciaDTO.idContaDestino = this.idContaDestino;
            transferenciaDTO.valor = this.valor;
            return transferenciaDTO;
        }
    }

}
