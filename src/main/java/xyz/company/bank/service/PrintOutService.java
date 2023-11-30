package xyz.company.bank.service;

public interface PrintOutService {

    default void printOut(Object message) {
        printOut(message.toString());
    }

    void printOut(String message);

    void printFormatted(String message, Object... values);
}
