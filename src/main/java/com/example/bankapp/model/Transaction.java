package com.example.bankapp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long accountId;
    private String type;
    private double amount;
    private LocalDateTime timestamp;

    // ✅ NEW FIELDS
    private double currentBalance; // Balance AFTER transaction
    private String source;         // "ATM", "Transfer from...", etc.

    public Transaction() {}

    // Updated Constructor
    public Transaction(Long accountId, String type, double amount, double currentBalance, String source) {
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.currentBalance = currentBalance;
        this.source = source;
        this.timestamp = LocalDateTime.now();
    }

    // Getters
    public Long getId() { return id; }
    public Long getAccountId() { return accountId; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public double getCurrentBalance() { return currentBalance; }
    public String getSource() { return source; }
}