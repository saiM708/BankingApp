package com.example.bankapp.service;

import com.example.bankapp.model.Account;
import com.example.bankapp.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class BalanceMonitorService {

    private static final double LOW_BALANCE_THRESHOLD = 100.0;

    private final EmailNotificationService emailNotificationService;
    private final AccountRepository accountRepository;

    public BalanceMonitorService(EmailNotificationService emailNotificationService, AccountRepository accountRepository) {
        this.emailNotificationService = emailNotificationService;
        this.accountRepository = accountRepository;
    }

    public void checkBalance(Account account) {
        if (account.getAccountBalance() < LOW_BALANCE_THRESHOLD && !account.isBalanceAlertSent()) {
            emailNotificationService.sendLowBalanceAlert(account.getAccountEmail(), account.getAccountHolderName(), account.getAccountBalance());
            account.setBalanceAlertSent(true);
            accountRepository.save(account);
        } else if (account.getAccountBalance() >= LOW_BALANCE_THRESHOLD) {
            account.setBalanceAlertSent(false);
            accountRepository.save(account);
        }
    }
}
