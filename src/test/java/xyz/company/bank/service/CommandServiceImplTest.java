package xyz.company.bank.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import xyz.company.bank.Command;
import xyz.company.bank.service.impl.CommandServiceImpl;
import xyz.company.bank.service.impl.PrintOutServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringJUnitConfig(CommandServiceImplTest.Config.class)
public class CommandServiceImplTest {

    @Configuration
    static class Config {

        @Bean
        public CommandService commandServiceImpl() {
            return new CommandServiceImpl();
        }

        @Bean
        public UserInputService userInputServiceMock() {
            return Mockito.mock(UserInputService.class);
        }

        @Bean
        public PrintOutService printOutServiceImpl() {
            return new PrintOutServiceImpl();
        }
    }

    @Autowired
    private UserInputService userInputServiceMock;

    @Autowired
    private CommandService commandService;

    @Test
    public void shouldCorrectlyValidateOpenCommand() {
        final var simulatedUserOpenCommand = "O 2";
        assertEquals(Command.OPEN, commandService.validateCommand(simulatedUserOpenCommand));
    }

    @Test
    public void shouldCorrectlyValidateTransferCommand() {
        final var simulatedUserTransferCommand = "T 2 3 232.23";
        assertEquals(Command.TRANSFER, commandService.validateCommand(simulatedUserTransferCommand));
    }

    @Test
    public void shouldCorrectlyValidateCloseCommand() {
        final var simulatedUserCloseCommand = "C 2";
        assertEquals(Command.CLOSE, commandService.validateCommand(simulatedUserCloseCommand));
    }

    @Test
    public void shouldCorrectlyValidateBalanceCommand() {
        final var simulatedUserBalanceCommand = "B 2";
        assertEquals(Command.BALANCE, commandService.validateCommand(simulatedUserBalanceCommand));
    }

    @Test
    public void shouldCorrectlyValidateListCommand() {
        final var simulatedUserListCommand = "L";
        assertEquals(Command.LIST, commandService.validateCommand(simulatedUserListCommand));
    }

    @Test
    public void shouldCorrectlyValidateQuitCommand() {
        final var simulatedUserListCommand = "Q";
        when(userInputServiceMock.read()).thenReturn("Y");
        final var command = commandService.validateCommand(simulatedUserListCommand);
        assertEquals(Command.QUIT, command);
    }

    @Test
    public void shouldReturnInvalidOnUnrecognizedCommand() {
        final var simulatedUserListCommand = "R";
        final var command = commandService.validateCommand(simulatedUserListCommand);
        assertEquals(Command.INVALID, command);
    }

    @Test
    public void shouldOnlyAcceptPositiveAccountNumbers() {
        final var simulatedUserOpenCommand = "O -2";
        assertEquals(Command.INVALID, commandService.validateCommand(simulatedUserOpenCommand));
    }

    @Test
    public void shouldOnlyAcceptPositiveTransferAmounts() {
        final var simulatedUserOpenCommand = "T 2 3 -50";
        assertEquals(Command.INVALID, commandService.validateCommand(simulatedUserOpenCommand));
    }

    @Test
    public void shouldOnlyAcceptNumericTransferAmounts() {
        final var simulatedUserOpenCommand = "T 2 3 asd";
        assertEquals(Command.INVALID, commandService.validateCommand(simulatedUserOpenCommand));
    }

    @Test
    public void shouldOnlyAcceptNumericAccountNumbers() {
        final var simulatedUserOpenCommand = "T 2asd 3 232";
        assertEquals(Command.INVALID, commandService.validateCommand(simulatedUserOpenCommand));
    }

    @Test
    public void shouldReturnInvalidOnNullCommand() {
        final String simulatedUserOpenCommand = null;
        assertEquals(Command.INVALID, commandService.validateCommand(simulatedUserOpenCommand));
    }

    @Test
    public void shouldReturnInvalidOnBlankCommand() {
        final String simulatedUserOpenCommand = " ";
        assertEquals(Command.INVALID, commandService.validateCommand(simulatedUserOpenCommand));
    }
}
