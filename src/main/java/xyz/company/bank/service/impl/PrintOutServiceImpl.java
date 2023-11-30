package xyz.company.bank.service.impl;

import org.springframework.stereotype.Service;
import xyz.company.bank.service.PrintOutService;

@Service
public class PrintOutServiceImpl implements PrintOutService {
    @Override
    public void printOut(final String message) {
        System.out.println(message);
    }

    @Override
    public void printFormatted(final String message, final Object... values) {
        System.out.printf(message, values);
    }
}
