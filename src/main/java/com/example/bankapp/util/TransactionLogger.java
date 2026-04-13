package com.example.bankapp.util;

import java.io.FileWriter;
import java.io.IOException;

public class TransactionLogger {

    public static void log(String message) {
        try (FileWriter writer = new FileWriter("transactions.log", true)) {
            writer.write(message + "\n");
        } catch (IOException e) {
            System.err.println("Failed to log transaction: " + e.getMessage());
        }
    }
}
