package xyz.company.bank.service;

import xyz.company.bank.pojo.Account;
import xyz.company.bank.repo.AccountRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface AccountService extends AccountRepository {

    void closeAccount(int accountNumber);
    void openAccount(int accountNumber);
    void transfer(int sourceAccountNumber,
                  int destinationAccountNumber,
                  BigDecimal amount);
    void balance(int accountNumber);
    void list();
    Optional<Account> findById(int accountNumber);
}
