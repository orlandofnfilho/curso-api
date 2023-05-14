package com.ficr.edu.bancoapi.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Conta {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String numero;
    private String agencia;
    private String cliente;
    private Double saldo = 0.0;

    public Conta() {
    }

    public Conta(String numero, String agencia, String cliente) {
        this.numero = numero;
        this.agencia = agencia;
        this.cliente = cliente;
    }

    public Conta(Long id, String numero, String agencia, String cliente, Double saldo) {
        this.id = id;
        this.numero = numero;
        this.agencia = agencia;
        this.cliente = cliente;
        this.saldo = saldo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conta conta = (Conta) o;
        return getId() == conta.getId() && Objects.equals(getNumero(), conta.getNumero());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNumero());
    }

    @Override
    public String toString() {
        return "Conta{" +
                "id=" + id +
                ", numero='" + numero + '\'' +
                ", agencia='" + agencia + '\'' +
                ", cliente='" + cliente + '\'' +
                ", saldo=" + saldo +
                '}';
    }
}
