package xyz.company.bank.service;

import xyz.company.bank.Command;

public interface CommandService {

    Command validateCommand(String command);

    boolean validateOpenCommand(String[] commandPlusArgs);
    boolean validateTransferCommand(String[] commandPlusArgs);
    boolean validateCloseCommand(String[] commandPlusArgs);
    boolean validateBalanceCommand(String[] commandPlusArgs);
    boolean validateExitCommand(String[] commandPlusArgs);
}
