package xyz.company.bank.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import xyz.company.bank.service.impl.CommandServiceImpl;
import xyz.company.bank.service.impl.ExecutionServiceImpl;
import xyz.company.bank.service.impl.PrintOutServiceImpl;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringJUnitConfig(ExecutionServiceImplTest.Config.class)
public class ExecutionServiceImplTest {

    @Configuration
    static class Config {

        @Bean
        public CommandService commandServiceImpl() {
            return new CommandServiceImpl();
        }

        @Bean
        public ExecutionService executionServiceImpl() {
            return new ExecutionServiceImpl();
        }

        @Bean
        public UserInputService userInputServiceMock() {
            return Mockito.mock(UserInputService.class);
        }

        @Bean
        public AccountService accountServiceMock() {
            return Mockito.mock(AccountService.class);
        }
        
        @Bean
        public PrintOutService printOutServiceImpl() {
            return new PrintOutServiceImpl();
        }
    }

    @Autowired
    private UserInputService userInputServiceMock;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ExecutionService executionService;

    @BeforeEach
    public void setUp() {
        reset(accountService, userInputServiceMock);
    }

    @Test
    public void shouldCorrectlyExecuteOpenCommand() {
        when(userInputServiceMock.read()).thenReturn("O 1");
        executionService.initAndRead(false);
        verify(accountService, times(1)).openAccount(1);
    }

    @Test
    public void shouldCorrectlyExecuteCloseCommand() {
        when(userInputServiceMock.read()).thenReturn("C 1");
        executionService.initAndRead(false);
        verify(accountService, times(1)).closeAccount(1);
    }

    @Test
    public void shouldCorrectlyExecuteTransferCommand() {
        when(userInputServiceMock.read()).thenReturn("T 1 2 50");
        executionService.initAndRead(false);
        verify(accountService, times(1)).transfer(1, 2, BigDecimal.valueOf(50));
    }

    @Test
    public void shouldCorrectlyExecuteBalanceCommand() {
        when(userInputServiceMock.read()).thenReturn("B 1");
        executionService.initAndRead(false);
        verify(accountService, times(1)).balance(1);
    }

    @Test
    public void shouldCorrectlyExecuteListCommand() {
        when(userInputServiceMock.read()).thenReturn("L");
        executionService.initAndRead(false);
        verify(accountService, times(1)).list();
    }

    @Test
    public void shouldnotExecuteAnyOnInvalidCommand() {
        when(userInputServiceMock.read()).thenReturn("UTR");
        executionService.initAndRead(false);
        verify(accountService, times(0)).openAccount(anyInt());
        verify(accountService, times(0)).closeAccount(anyInt());
        verify(accountService, times(0)).transfer(anyInt(), anyInt(), any());
        verify(accountService, times(0)).balance(anyInt());
        verify(accountService, times(0)).list();
    }
}
