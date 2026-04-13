package com.example.bankapp.service;

import com.example.bankapp.exception.InsufficientFundsException;
import com.example.bankapp.model.Account;
import com.example.bankapp.model.Transaction;
import com.example.bankapp.repository.AccountRepository;
import com.example.bankapp.repository.TransactionRepository;
import com.example.bankapp.util.TransactionLogger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final BalanceMonitorService balanceMonitorService;
    private final EmailNotificationService emailNotificationService;

    public AccountService(AccountRepository accountRepository,
                          TransactionRepository transactionRepository,
                          BalanceMonitorService balanceMonitorService,
                          EmailNotificationService emailNotificationService) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.balanceMonitorService = balanceMonitorService;
        this.emailNotificationService = emailNotificationService;
    }

    public Account login(String email) {
        return accountRepository.findByAccountEmail(email);
    }

    public Account findAccount(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    // ✅ UPDATED: Accepts 'accountType'
    public Account createAccount(String accountHolderName, String accountEmail, double accountBalance, String accountType) {
        if (accountBalance < 100) {
            throw new IllegalArgumentException("Minimum initial balance must be $100");
        }

        Account account = new Account(accountHolderName, accountEmail, accountBalance, accountType);
        Account savedAccount = accountRepository.save(account);

        // Send Welcome Email
        emailNotificationService.sendNotification(
                accountEmail,
                "Welcome to BankApp!",
                "Hello " + accountHolderName + ",\n\nYour " + accountType + " account has been created successfully.\nYour Account ID is: " + savedAccount.getId()
        );

        // Log Initial Deposit
        transactionRepository.save(new Transaction(savedAccount.getId(), "Initial Deposit", accountBalance, accountBalance, "Welcome Bonus"));

        TransactionLogger.log("ACCOUNT CREATED | ID=" + savedAccount.getId());
        return savedAccount;
    }

    // ✅ UPDATED: Accepts 'source'
    public void deposit(Long id, double amount, String source) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));

        account.setAccountBalance(account.getAccountBalance() + amount);
        accountRepository.save(account);

        // Save detailed history
        transactionRepository.save(new Transaction(id, "Deposit", amount, account.getAccountBalance(), source));

        TransactionLogger.log("DEPOSIT | ACCOUNT=" + id + " | AMOUNT=" + amount);
        balanceMonitorService.checkBalance(account);
    }

    // ✅ UPDATED: Accepts 'source'
    public void withdraw(Long id, double amount, String source) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
        if (account.getAccountBalance() < amount) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal");
        }

        account.setAccountBalance(account.getAccountBalance() - amount);
        accountRepository.save(account);

        // Save detailed history
        transactionRepository.save(new Transaction(id, "Withdraw", amount, account.getAccountBalance(), source));

        TransactionLogger.log("WITHDRAW | ACCOUNT=" + id + " | AMOUNT=" + amount);
        balanceMonitorService.checkBalance(account);
    }

    // ✅ UPDATED: Handles rich transfer descriptions
    public void transfer(Long fromId, Long toId, double amount) {
        Account fromAccount = findAccount(fromId);
        Account toAccount = findAccount(toId);

        if (fromAccount == null || toAccount == null) throw new RuntimeException("Invalid Account ID");

        // 1. Withdraw from Sender (Source describes where money went)
        withdraw(fromId, amount, "Transfer to " + toAccount.getAccountHolderName());

        // 2. Deposit to Receiver (Source describes who sent money)
        deposit(toId, amount, "Transfer from " + fromAccount.getAccountHolderName());

        TransactionLogger.log("TRANSFER | FROM=" + fromId + " | TO=" + toId + " | AMOUNT=" + amount);
    }

    public List<Transaction> getTransactionHistory(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }
}