package xyz.company.bank.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.company.bank.service.*;
import xyz.company.bank.Command;

import java.math.BigDecimal;

import static xyz.company.bank.service.util.ServiceUtil.COMMAND_DELIMITER;


/**
 * Controls program flow based on user Commands.
 *
 * @See {@link Command}
 * @See {@link CommandServiceImpl}
 * @See {@link AccountServiceImpl}
 */
@Service
public class ExecutionServiceImpl implements ExecutionService {
    
    @Autowired
    private PrintOutService printOutService;
    @Autowired
    private UserInputService userInputService;
    @Autowired
    private CommandService commandService;
    @Autowired
    private AccountService accountService;

    @Override
    public void initAndRead(boolean continueUntilQuit) {
        do {
            printOutService.printOut("Enter command (O, C, T, B, L, Q)");
            final var userInput = userInputService.read();
            final Command userCommand = commandService.validateCommand(userInput);

            if (Command.INVALID.equals(userCommand))
                continue;

            switch (userCommand) {
                case OPEN -> executeOpenAccount(userInput);
                case CLOSE -> executeCloseAccount(userInput);
                case TRANSFER -> executeTransfer(userInput);
                case BALANCE -> executeBalance(userInput);
                case LIST -> executeListAccounts();
                case QUIT -> executeExit();
            }

        // Depend on user input Q to exit via System.exit.
        } while(continueUntilQuit);
    }

    @Override
    public void executeOpenAccount(final String userInput) {
        printOutService.printOut("Opening account...");
        final var accountNumber = Integer.parseInt(userInput.split(COMMAND_DELIMITER)[1]);
        accountService.openAccount(accountNumber);
    }

    @Override
    public void executeTransfer(final String userInput) {
        printOutService.printOut("Executing account transfer...");
        final var sourceAccountNumber = Integer.parseInt(userInput.split(COMMAND_DELIMITER)[1]);
        final var destinationAccountNumber = Integer.parseInt(userInput.split(COMMAND_DELIMITER)[2]);
        final var transferAmount = new BigDecimal(userInput.split(COMMAND_DELIMITER)[3]);
        accountService.transfer(sourceAccountNumber, destinationAccountNumber, transferAmount);
    }

    @Override
    public void executeBalance(final String userInput) {
        printOutService.printOut("Checking account balance...");
        final var accountNumber = Integer.parseInt(userInput.split(COMMAND_DELIMITER)[1]);
        accountService.balance(accountNumber);
    }

    @Override
    public void executeCloseAccount(final String userInput) {
        printOutService.printOut("Closing account...");
        final var accountNumber = Integer.parseInt(userInput.split(COMMAND_DELIMITER)[1]);
        accountService.closeAccount(accountNumber);
    }

    @Override
    public void executeListAccounts() {
        printOutService.printOut("Listing accounts...");
        accountService.list();
    }

    @Override
    public void executeExit() {
        printOutService.printOut("Shutting down, bye");
        System.exit(0);
    }
}
