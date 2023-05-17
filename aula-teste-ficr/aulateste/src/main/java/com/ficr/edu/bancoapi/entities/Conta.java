package com.ficr.edu.bancoapi.entities;

import com.ficr.edu.bancoapi.exceptions.BusinessRuleException;
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
    public void verificaSaldo(Double saldo, Double valor) {
        if (saldo < valor || saldo <= 0)
            throw new BusinessRuleException("Você nao tem saldo suficiente para realizar a operaçao!");
    }

    public static class Builder {
        private Long id;
        private String numero;
        private String agencia;
        private String cliente;
        private Double saldo;

        public Builder() {
            // Inicialize os campos opcionais com valores padrão
            this.saldo = 0.0;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
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

        public Builder saldo(Double saldo) {
            this.saldo = saldo;
            return this;
        }

        public Conta build() {
            Conta conta = new Conta();
            conta.id = this.id;
            conta.numero = this.numero;
            conta.agencia = this.agencia;
            conta.cliente = this.cliente;
            conta.saldo = this.saldo;
            return conta;
        }
    }
}
