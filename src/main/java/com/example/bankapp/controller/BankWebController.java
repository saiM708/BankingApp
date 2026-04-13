package com.example.bankapp.controller;

import com.example.bankapp.model.Account;
import com.example.bankapp.service.AccountService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BankWebController {

    private final AccountService accountService;

    public BankWebController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String email, HttpSession session, Model model) {
        Account account = accountService.login(email);
        if (account != null) {
            session.setAttribute("user", account);
            return "redirect:/dashboard";
        }
        model.addAttribute("error", "Invalid Email");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) return "redirect:/";

        Account latestUser = accountService.findAccount(user.getId());
        model.addAttribute("user", latestUser);
        return "dashboard";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @PostMapping("/signup")
    public String processSignup(@RequestParam String holderName,
                                @RequestParam String email,
                                @RequestParam double balance,
                                @RequestParam String type, // ✅ New Parameter
                                Model model) {
        try {
            accountService.createAccount(holderName, email, balance, type);
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "signup";
        }
    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam double amount, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        accountService.deposit(user.getId(), amount, "Self Deposit"); // ✅ Default Source
        return "redirect:/dashboard";
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam double amount, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        accountService.withdraw(user.getId(), amount, "ATM Withdrawal"); // ✅ Default Source
        return "redirect:/dashboard";
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam Long toId, @RequestParam double amount, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        accountService.transfer(user.getId(), toId, amount);
        return "redirect:/dashboard";
    }

    @GetMapping("/history")
    public String history(HttpSession session, Model model) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) return "redirect:/";

        model.addAttribute("transactions", accountService.getTransactionHistory(user.getId()));
        return "history";
    }
}