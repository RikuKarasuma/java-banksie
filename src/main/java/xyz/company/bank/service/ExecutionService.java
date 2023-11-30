package xyz.company.bank.service;

public interface ExecutionService {

    void initAndRead(boolean continueUntilQuit);

    void executeOpenAccount(String userInput);

    void executeTransfer(String userInput);

    void executeBalance(String userInput);

    void executeCloseAccount(String userInput);

    void executeListAccounts();

    void executeExit();
}
