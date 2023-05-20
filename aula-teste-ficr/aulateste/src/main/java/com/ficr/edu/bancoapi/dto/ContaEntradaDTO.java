package com.ficr.edu.bancoapi.dto;

public class ContaEntradaDTO {

    private String agencia;
    private String numero;
    private String cliente;

    private ContaEntradaDTO() {
    }

    public String getAgencia() {
        return agencia;
    }

    public String getNumero() {
        return numero;
    }

    public String getCliente() {
        return cliente;
    }

    public static class Builder {

        private String agencia;
        private String numero;
        private String cliente;

        public Builder() {
        }

        public Builder agencia(String agencia) {
            this.agencia = agencia;
            return this;
        }

        public Builder numero(String numero) {
            this.numero = numero;
            return this;
        }

        public Builder cliente(String cliente) {
            this.cliente = cliente;
            return this;
        }

        public ContaEntradaDTO build() {
            ContaEntradaDTO contaEntradaDTO = new ContaEntradaDTO();
            contaEntradaDTO.agencia = this.agencia;
            contaEntradaDTO.numero = this.numero;
            contaEntradaDTO.cliente = this.cliente;
            return contaEntradaDTO;
        }
    }
}
