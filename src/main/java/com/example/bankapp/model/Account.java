package com.example.bankapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountHolderName;
    private String accountEmail;
    private double accountBalance;
    private boolean balanceAlertSent;

    // ✅ NEW FIELD
    private String accountType;

    // Constructor used for Signup
    public Account(String accountHolderName, String accountEmail, double accountBalance, String accountType) {
        this.accountHolderName = accountHolderName;
        this.accountEmail = accountEmail;
        this.accountBalance = accountBalance;
        this.accountType = accountType;
        this.balanceAlertSent = false;
    }
}