package com.ficr.edu.bancoapi.entities;

import com.ficr.edu.bancoapi.exceptions.BusinessRuleException;
import com.ficr.edu.bancoapi.templates.ContaTemplate;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ContaTest {

    @Test
    void deveriaVerificarOSaldoDaConta(){

        Conta conta = ContaTemplate.entidade();
        conta.setSaldo(500.00);
        assertDoesNotThrow(() -> conta.verificaSaldo(100.0));
    }

    @Test
    void deveriaVerificarOSaldoDaContaERetornarUmaException(){

        Conta conta = ContaTemplate.entidade();
        conta.setSaldo(500.00);
        assertThrows(BusinessRuleException.class, () -> conta.verificaSaldo(1000.00));

    }
}
