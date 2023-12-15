package xyz.company.bank.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import xyz.company.bank.service.impl.AccountServiceImpl;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
@SpringJUnitConfig(AccountServiceImplTest.Config.class)
public class AccountServiceImplTest {

    @Configuration
    static class Config {

        @Bean
        public PrintOutService printOutServiceMock() {
            return mock(PrintOutService.class);
        }

        @Bean
        public AccountService accountServiceImpl() {
            return new AccountServiceImpl();
        }
    }

    @Autowired
    private AccountService accountService;

    @Autowired
    private PrintOutService printOutService;

    @BeforeEach
    public void setUp() {
        reset(printOutService);
    }

    @Test
    public void shouldOpenAccountOnCommand() {
        final var testAccountId = 1;
        accountService.openAccount(testAccountId);

        final var expectedToExist = true;
        assertEquals(expectedToExist, accountService.findById(testAccountId).isPresent());
    }

    @Test
    public void shouldNotCloseAccountWithId999() {
        final var testAccountId = 999;
        accountService.closeAccount(testAccountId);
        final var expectedToExist = true;
        assertEquals(expectedToExist, accountService.findById(testAccountId).isPresent());
    }

    @Test
    public void shouldTransferSpecifiedAmountToAccount() {
        final var testDestinationAccountId = 1;
        final var testSourceAccountId = 999;
        accountService.openAccount(testDestinationAccountId);
        final var account1 = accountService.findById(testDestinationAccountId).get();
        final var transferAmount = BigDecimal.valueOf(50.52);
        accountService.transfer(testSourceAccountId, testDestinationAccountId, transferAmount);

        final var account999 = accountService.findById(testSourceAccountId).get();

        assertEquals(transferAmount, account1.getBalance());
        assertEquals(BigDecimal.valueOf(1000).subtract(transferAmount), account999.getBalance());
    }

    @Test
    public void shouldListBalanceOnCommand() {
        final var testAccountId = 999;
        accountService.balance(testAccountId);
        final var account999 = accountService.findById(testAccountId).get();

        verify(printOutService, times(1)).printOut(account999);
    }

    @Test
    public void shouldListSortedAccountsOnCommand() {
        final var testAccountId2 = 2;
        accountService.openAccount(testAccountId2);
        final var account2 = accountService.findById(2).get();
        final var testAccountId3 = 3;
        accountService.openAccount(testAccountId3);
        final var account3 = accountService.findById(3).get();
        final var testAccountId1 = 1;
        accountService.openAccount(testAccountId1);
        final var account1 = accountService.findById(1).get();
        final var account999 = accountService.findById(999).get();

        InOrder inOrder = inOrder(printOutService);

        accountService.list();

        inOrder.verify(printOutService, times(1)).printOut(account1);
        inOrder.verify(printOutService, times(1)).printOut(account2);
        inOrder.verify(printOutService, times(1)).printOut(account3);
        inOrder.verify(printOutService, times(1)).printOut(account999);
    }


    @Test
    public void shouldReturnErrorMessageOnInvalidBalanceAccountNumber() {
        final var testAccountId = 25;
        accountService.balance(testAccountId);
        verify(printOutService, times(1)).printFormatted("An account with that Id %d doesn't exist.\n", testAccountId);
    }

    @Test
    public void shouldReturnErrorMessageOnAccountAlreadyCreated() {
        final var testAccountId = 999;
        accountService.openAccount(testAccountId);
        verify(printOutService, times(1)).printFormatted("Account with Id %d already exists\n", testAccountId);
    }

    @Test
    public void shouldReturnErrorMessageOnClosingAccountWithRemainingBalance() {
        final var testAccountId = 1;
        accountService.openAccount(testAccountId);
        final var account1 = accountService.findById(testAccountId).get();
        accountService.transfer(999, testAccountId, BigDecimal.valueOf(50));
        accountService.closeAccount(testAccountId);
        verify(printOutService, times(1)).printFormatted("Account %d can't be close as it has a balance of %s remaining.\n",
                                                                               testAccountId,
                                                                               account1.getBalance());
    }

    @Test
    public void shouldReturnErrorMessageOnTransferringInsufficientBalance() {
        final var testAccountId = 20;
        accountService.openAccount(testAccountId);
        final var account1 = accountService.findById(testAccountId).get();
        final var transferAmount = BigDecimal.valueOf(50);
        accountService.transfer(testAccountId, 999, transferAmount);

        verify(printOutService, times(1)).printFormatted("Account %d can't transfer as it has a balance of %s which is less than the requested %s.\n",
                                                                               testAccountId,
                                                                               account1.getBalance(),
                                                                               transferAmount);
    }

    @Test
    public void shouldReturnErrorMessageOnTransferringFromNonExistentAccount() {
        final var testAccountId = 1;
        final var transferAmount = BigDecimal.valueOf(50);
        accountService.transfer(testAccountId, 999, transferAmount);

        verify(printOutService, times(1)).printFormatted("An account with that Id %d doesn't exist.\n", testAccountId);
    }

    @Test
    public void shouldReturnErrorMessageOnTransferringToNonExistentAccount() {
        final var testAccountId = 4;
        final var transferAmount = BigDecimal.valueOf(50);
        accountService.transfer(999, testAccountId, transferAmount);

        verify(printOutService, times(1)).printFormatted("An account with that Id %d doesn't exist.\n", testAccountId);
    }
}
