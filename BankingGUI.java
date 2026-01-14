import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class BankingGUI extends JFrame {
    private Bank bank;
    private JButton createAccountBtn, depositBtn, withdrawBtn, transferBtn, checkBalanceBtn, transactionHistoryBtn, deleteAccountBtn, exitBtn;

    public BankingGUI() {
        bank = new Bank();
        setTitle("Banking System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(8, 1));

        createAccountBtn = new JButton("Create Account");
        depositBtn = new JButton("Deposit Funds");
        withdrawBtn = new JButton("Withdraw Funds");
        transferBtn = new JButton("Transfer Funds");
        checkBalanceBtn = new JButton("Check Balance");
        transactionHistoryBtn = new JButton("Transaction History");
        deleteAccountBtn = new JButton("Delete Account");
        exitBtn = new JButton("Exit");

        add(createAccountBtn);
        add(depositBtn);
        add(withdrawBtn);
        add(transferBtn);
        add(checkBalanceBtn);
        add(transactionHistoryBtn);
        add(deleteAccountBtn);
        add(exitBtn);

        createAccountBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAccount();
            }
        });

        depositBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                depositFunds();
            }
        });

        withdrawBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                withdrawFunds();
            }
        });

        transferBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transferFunds();
            }
        });

        checkBalanceBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkBalance();
            }
        });

        transactionHistoryBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTransactionHistory();
            }
        });

        deleteAccountBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteAccount();
            }
        });

        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void createAccount() {
        String accNum = JOptionPane.showInputDialog(this, "Enter account number:");
        if (accNum == null) return;
        String name = JOptionPane.showInputDialog(this, "Enter account holder name:");
        if (name == null) return;
        String email = JOptionPane.showInputDialog(this, "Enter email address:");
        if (email == null) return;
        String balanceStr = JOptionPane.showInputDialog(this, "Enter initial balance:");
        if (balanceStr == null) return;
        try {
            double initialBalance = Double.parseDouble(balanceStr);
            bank.createAccount(accNum, name, email, initialBalance);
            JOptionPane.showMessageDialog(this, "Account created for " + name);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid balance amount.");
        }
    }

    private void depositFunds() {
        String accNum = JOptionPane.showInputDialog(this, "Enter account number:");
        if (accNum == null) return;
        Account account = bank.getAccount(accNum);
        if (account == null) {
            JOptionPane.showMessageDialog(this, "Account not found.");
            return;
        }
        String amountStr = JOptionPane.showInputDialog(this, "Enter deposit amount:");
        if (amountStr == null) return;
        try {
            double amount = Double.parseDouble(amountStr);
            account.deposit(amount);
            JOptionPane.showMessageDialog(this, "Deposited $" + amount + ". New Balance: $" + account.getBalance());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount.");
        }
    }

    private void withdrawFunds() {
        String accNum = JOptionPane.showInputDialog(this, "Enter account number:");
        if (accNum == null) return;
        Account account = bank.getAccount(accNum);
        if (account == null) {
            JOptionPane.showMessageDialog(this, "Account not found.");
            return;
        }
        String amountStr = JOptionPane.showInputDialog(this, "Enter withdrawal amount:");
        if (amountStr == null) return;
        try {
            double amount = Double.parseDouble(amountStr);
            account.withdraw(amount);
            JOptionPane.showMessageDialog(this, "Withdrew $" + amount + ". Remaining Balance: $" + account.getBalance());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount.");
        } catch (InsufficientFundsException ex) {
            JOptionPane.showMessageDialog(this, "Withdrawal Failed: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Withdrawal Failed: " + ex.getMessage());
        }
    }

    private void transferFunds() {
        String fromAcc = JOptionPane.showInputDialog(this, "Enter source account number:");
        if (fromAcc == null) return;
        String toAcc = JOptionPane.showInputDialog(this, "Enter destination account number:");
        if (toAcc == null) return;
        String amountStr = JOptionPane.showInputDialog(this, "Enter transfer amount:");
        if (amountStr == null) return;
        try {
            double amount = Double.parseDouble(amountStr);
            bank.transferFunds(fromAcc, toAcc, amount);
            JOptionPane.showMessageDialog(this, "Transfer successful!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount.");
        } catch (InsufficientFundsException ex) {
            JOptionPane.showMessageDialog(this, "Transfer Failed: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Transfer Failed: " + ex.getMessage());
        }
    }

    private void checkBalance() {
        String accNum = JOptionPane.showInputDialog(this, "Enter account number:");
        if (accNum == null) return;
        Account account = bank.getAccount(accNum);
        if (account != null) {
            JOptionPane.showMessageDialog(this, "Balance: $" + account.getBalance());
        } else {
            JOptionPane.showMessageDialog(this, "Account not found.");
        }
    }

    private void showTransactionHistory() {
        String accNum = JOptionPane.showInputDialog(this, "Enter account number:");
        if (accNum == null) return;
        Account account = bank.getAccount(accNum);
        if (account != null) {
            List<String> history = account.getTransactionHistory();
            System.out.println("Transaction History:");
            for (String transaction : history) {
                System.out.println("- " + transaction);
            }
            StringBuilder sb = new StringBuilder("Transaction History:\n");
            for (String transaction : history) {
                sb.append("- ").append(transaction).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString());
            account.saveTransactionHistoryToFile();
        } else {
            JOptionPane.showMessageDialog(this, "Account not found.");
        }
    }

    private void deleteAccount() {
        String accNum = JOptionPane.showInputDialog(this, "Enter account number to delete:");
        if (accNum == null) return;
        bank.deleteAccount(accNum);
        JOptionPane.showMessageDialog(this, "Account " + accNum + " deleted successfully.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BankingGUI().setVisible(true);
            }
        });
    }
}
