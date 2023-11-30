package xyz.company.bank.service.util;

import java.math.BigDecimal;

public final class ServiceUtil {

    public static final String COMMAND_DELIMITER = " ";

    public static boolean validateNumberOfParams(final String[] params, final int expected) {

        // -1 as the first param is the command.
        final var parameterLength = params.length - 1;
        final var isExpected = parameterLength == expected;

        if (!isExpected) {
            if (parameterLength < expected)
                System.out.println("Not enough parameters.");
            else
                System.out.println("Too many parameters.");
        }

        return isExpected;
    }

    public static boolean validateAccountNumber(final String accountNumber, final String errorMessage) {
        try {
            final var integer = Integer.parseInt(accountNumber);
            if (integer < 0) {
                System.err.println(errorMessage);
                return false;
            }
            return true;
        } catch (NumberFormatException castingError) {
            System.err.println(errorMessage);
        }

        return false;
    }

    public static boolean validateCashDecimal(final String accountNumber, final String errorMessage) {
        try {
            final var decimalCash = new BigDecimal(accountNumber);
            if (decimalCash.compareTo(BigDecimal.ZERO) < 0) {
                System.err.println(errorMessage);
                return false;
            }

            return true;
        } catch (NumberFormatException castingError) {
            System.err.println(errorMessage);
        }

        return false;
    }
}
