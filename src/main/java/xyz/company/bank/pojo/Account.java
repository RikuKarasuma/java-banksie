package xyz.company.bank.pojo;

import java.math.BigDecimal;
import java.util.Objects;

public class Account {
    private int id;
    private BigDecimal balance = BigDecimal.ZERO;

    public Account() {}

    public Account(final int id) {
        this.setId(id);
    }

    public Account(final int id, final BigDecimal initBalance) {
        this.setId(id);
        this.setBalance(initBalance);
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        if (Objects.isNull(balance))
            throw new IllegalArgumentException("Balance must not be null!");
        else if (balance.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Balance must be ");

        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 1)
            throw new IllegalArgumentException("ID must be a positive integer!");

        this.id = id;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", balance=" + balance +
                '}';
    }
}
