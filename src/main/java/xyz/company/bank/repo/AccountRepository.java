package xyz.company.bank.repo;

import xyz.company.bank.pojo.Account;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public interface AccountRepository {

    default List<Account> getAccountsTable() {

        final var defaultAccounts = new ArrayList<Account>();
        defaultAccounts.add(new Account(999, BigDecimal.valueOf(1000)));
        return defaultAccounts;
    };
}
