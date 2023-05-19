package com.ficr.edu.bancoapi.dto;

public class ContaEntradaDTO {
    private String numero;
    private String agencia;
    private String cliente;

    private ContaEntradaDTO() {
    }

    public String getNumero() {
        return numero;
    }

    public String getAgencia() {
        return agencia;
    }

    public String getCliente() {
        return cliente;
    }

    public static class Builder {
        private String numero;
        private String agencia;
        private String cliente;

        public Builder() {
        }

        public Builder numero(String numero) {
            this.numero = numero;
            return this;
        }

        public Builder agencia(String agencia) {
            this.agencia = agencia;
            return this;
        }

        public Builder cliente(String cliente) {
            this.cliente = cliente;
            return this;
        }

        public ContaEntradaDTO build() {
            ContaEntradaDTO contaEntradaDTO = new ContaEntradaDTO();
            contaEntradaDTO.numero = this.numero;
            contaEntradaDTO.agencia = this.agencia;
            contaEntradaDTO.cliente = this.cliente;
            return contaEntradaDTO;
        }
    }
}
