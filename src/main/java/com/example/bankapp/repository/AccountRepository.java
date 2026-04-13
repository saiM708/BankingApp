package com.example.bankapp.repository;

import com.example.bankapp.model.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long> {

    Account findByAccountEmail(String accountEmail);
}