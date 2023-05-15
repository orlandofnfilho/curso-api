package com.ficr.edu.bancoapi.dto;

public class ContaEntradaDTO {

    private String numero;
    private String agencia;
    private String cliente;

    public ContaEntradaDTO() {
    }

    public ContaEntradaDTO(String numero, String agencia, String cliente) {
        this.numero = numero;
        this.agencia = agencia;
        this.cliente = cliente;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }
}
