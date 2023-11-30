package xyz.company.bank.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.company.bank.pojo.Account;
import xyz.company.bank.service.AccountService;
import xyz.company.bank.service.PrintOutService;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Handles account retrieval and user interactions.
 *
 * @See {@link Account}
 * @See {@link xyz.company.bank.repo.AccountRepository}
 * @See {@link ExecutionServiceImpl}
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private PrintOutService printOutService;

    private static final Comparator<Account> SORTER = (account, account2) -> {
        if (account.getId() < account2.getId()) {
            return -1;
        } else if (account.getId() > account2.getId()) {
            return 1;
        }
        return 0;
    };

    private final List<Account> ACCOUNTS = getAccountsTable();

    @Override
    public void closeAccount(final int accountNumber) {
        if (accountNumber == 999) {
            printOutService.printOut("We are not allowed to close account 999.");
            return;
        }

        findById(accountNumber).ifPresentOrElse(
            account -> {
                if (account.getBalance().compareTo(BigDecimal.ZERO) > 0)
                    printOutService.printFormatted("Account %d can't be close as it has a balance of %s remaining.\n",
                                                   accountNumber,
                                                   account.getBalance());
                else
                    ACCOUNTS.remove(account);
            },
            () -> printOutService.printFormatted("An account with that Id %d doesn't exist.\n", accountNumber)
        );
    }

    @Override
    public void openAccount(final int accountNumber) {

        if (findById(accountNumber).isPresent())
            printOutService.printFormatted("Account with Id %d already exists\n", accountNumber);
        else
            ACCOUNTS.add(new Account(accountNumber));
    }

    @Override
    public void transfer(final int sourceAccountNumber,
                         final int destinationAccountNumber,
                         final BigDecimal amount) {

        findById(sourceAccountNumber).ifPresentOrElse(
            sourceAccount -> {

                if (amount.compareTo(sourceAccount.getBalance()) > 0)
                    printOutService.printFormatted("Account %d can't transfer as it has a balance of %s which is less than the requested %s.\n",
                                      sourceAccountNumber,
                                      sourceAccount.getBalance(),
                                      amount);
                else {
                    findById(destinationAccountNumber).ifPresentOrElse(
                        destAccount -> {
                            sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
                            destAccount.setBalance(destAccount.getBalance().add(amount));
                        },
                        () -> printOutService.printFormatted("An account with that Id %d doesn't exist.\n", destinationAccountNumber)
                    );
                }
            },
            () -> printOutService.printFormatted("An account with that Id %d doesn't exist.\n", sourceAccountNumber)
        );
    }

    @Override
    public void balance(final int accountNumber) {
        findById(accountNumber).ifPresentOrElse(
            printOutService::printOut,
            () -> printOutService.printFormatted("An account with that Id %d doesn't exist.\n", accountNumber)
        );
    }

    @Override
    public void list() {
        ACCOUNTS.stream()
                .sorted(SORTER)
                .forEach(printOutService::printOut);
    }

    @Override
    public Optional<Account> findById(final int accountNumber) {
        return ACCOUNTS.stream()
                       .filter(acc -> acc.getId() == accountNumber)
                       .findAny();
    }
}
