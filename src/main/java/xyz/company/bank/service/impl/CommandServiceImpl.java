package xyz.company.bank.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.company.bank.Command;
import xyz.company.bank.service.CommandService;
import xyz.company.bank.service.UserInputService;

import java.util.Objects;

import static xyz.company.bank.service.util.ServiceUtil.*;

/**
 * Handles reading from standard input, validation and parsing of Commands.
 *
 * @See {@link Command}
 * @See {@link ExecutionServiceImpl}
 */
@Service
public class CommandServiceImpl implements CommandService {

    @Autowired
    private UserInputService userInputService;

    @Override
    public Command validateCommand(final String commandStr) {

        if (Objects.isNull(commandStr) || commandStr.isBlank())
            return Command.INVALID;

        final var commandPlusArgs = commandStr.split(COMMAND_DELIMITER);
        final var command = commandPlusArgs[0];

        switch (command) {
            case "O" -> {
                if (validateOpenCommand(commandPlusArgs))
                    return Command.OPEN;
            }
            case "T" -> {
                if (validateTransferCommand(commandPlusArgs))
                    return Command.TRANSFER;
            }
            case "C" -> {
                if (validateCloseCommand(commandPlusArgs))
                    return Command.CLOSE;
            }
            case "B" -> {
                if (validateBalanceCommand(commandPlusArgs))
                    return Command.BALANCE;
            }
            case "L" -> {
                return Command.LIST;
            }
            case "Q" -> {
                if (validateExitCommand(commandPlusArgs))
                    return Command.QUIT;
            }
            default -> {
                System.out.println("Command not recognized");
                return Command.INVALID;
            }
        }

        return Command.INVALID;
    }

    @Override
    public boolean validateOpenCommand(final String[] commandPlusArgs) {
        return validateNumberOfParams(commandPlusArgs, 1) &&
               validateAccountNumber(commandPlusArgs[1], "Please enter a positive whole number as the Account Number.");
    }

    @Override
    public boolean validateTransferCommand(final String[] commandPlusArgs) {
        if(validateNumberOfParams(commandPlusArgs, 3)) {

            return validateAccountNumber(commandPlusArgs[1], "Please enter a positive whole number as the Source Account Number.") &&
                   validateAccountNumber(commandPlusArgs[2], "Please enter a positive whole number as the Destination Account Number.") &&
                   validateCashDecimal(commandPlusArgs[3], "Please enter a positive decimal number as the Transfer Amount.");
        }

        return false;
    }

    @Override
    public boolean validateCloseCommand(final String[] commandPlusArgs) {
        return validateNumberOfParams(commandPlusArgs, 1) &&
               validateAccountNumber(commandPlusArgs[1], "Please enter a positive whole number as the Close Account Number.");
    }

    @Override
    public boolean validateBalanceCommand(final String[] commandPlusArgs) {
        return validateNumberOfParams(commandPlusArgs, 1) &&
               validateAccountNumber(commandPlusArgs[1], "Please enter a positive whole number as the Balance Account Number.");
    }

    @Override
    public boolean validateExitCommand(final String[] commandPlusArgs) {
        System.out.println("Are you sure you wish to exit? (Y/N)");
        final var areYouSure = this.userInputService.read();
        return "Y".equals(areYouSure);
    }
}
