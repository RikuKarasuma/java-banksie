package xyz.company.bank.service.impl;

import org.springframework.stereotype.Service;
import xyz.company.bank.service.UserInputService;

import java.util.Scanner;

@Service
public class UserInputServiceImpl implements UserInputService {

    private final Scanner stdIn = new Scanner(System.in);

    @Override
    public String read() {
        return stdIn.nextLine();
    }
}
