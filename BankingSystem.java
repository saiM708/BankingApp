import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import javax.activation.*;

// 1. The Exception Class
class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}

// 2. The Account Class
class Account {
    private String accountNumber;
    private String accountHolderName;
    private String email;
    private double balance;
    private List<String> transactionHistory = new ArrayList<>();

    public Account(String accountNumber, String accountHolderName, String email, double initialBalance) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.email = email;
        this.balance = initialBalance;
    }

    public String getEmail() { return email; }

    public double getBalance() { return balance; }

    public List<String> getTransactionHistory() { return transactionHistory; }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            transactionHistory.add("Deposited $" + amount + ". Balance: $" + balance);
            System.out.println("Deposited $" + amount + ". New Balance: $" + balance);
        }
    }

    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (amount > balance) {
            throw new InsufficientFundsException("Insufficient funds. Balance: " + balance);
        }
        if (balance - amount < 100) {
            throw new InsufficientFundsException("Cannot withdraw. Minimum balance of $100 must be maintained. Current balance: " + balance);
        }
        balance -= amount;
        transactionHistory.add("Withdrew $" + amount + ". Balance: $" + balance);
        System.out.println("Withdrew $" + amount + ". Remaining Balance: $" + balance);
    }

    public void saveTransactionHistoryToFile() {
        String fileName = "transaction_history_" + accountNumber + ".txt";
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("Transaction History for Account: " + accountNumber + "\n");
            writer.write("Account Holder: " + accountHolderName + "\n");
            writer.write("Current Balance: $" + balance + "\n\n");
            writer.write(String.format("%-30s %-15s\n", "Transaction", "Balance"));
            writer.write(String.format("%-30s %-15s\n", "------------", "-------"));
            for (String transaction : transactionHistory) {
                // Parse the transaction to extract description and balance
                String[] parts = transaction.split("\\. Balance: \\$");
                String desc = parts[0];
                String bal = parts.length > 1 ? "$" + parts[1] : "";
                writer.write(String.format("%-30s %-15s\n", desc, bal));
            }
            writer.flush(); // Ensure all data is written
            System.out.println("Transaction history saved to " + fileName);
            Bank.sendTransactionHistoryEmail(email, new java.io.File(fileName).getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Error saving transaction history: " + e.getMessage());
        }
    }
}

// 3. The Bank Class
class Bank {
    private Map<String, Account> accounts = new HashMap<>();

    public void createAccount(String accNum, String name, String email, double initialBalance) {
        accounts.put(accNum, new Account(accNum, name, email, initialBalance));
        System.out.println("Account created for " + name);
        sendAccountCreationEmail(email, name, accNum, initialBalance);
    }

    public Account getAccount(String accNum) {
        return accounts.get(accNum);
    }

    public void transferFunds(String fromAcc, String toAcc, double amount) throws InsufficientFundsException, IllegalArgumentException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive.");
        }
        Account source = accounts.get(fromAcc);
        Account destination = accounts.get(toAcc);

        if (source == null) {
            throw new IllegalArgumentException("Source account " + fromAcc + " does not exist.");
        }
        if (destination == null) {
            throw new IllegalArgumentException("Destination account " + toAcc + " does not exist.");
        }

        source.withdraw(amount);
        destination.deposit(amount);
        source.getTransactionHistory().add("Transferred $" + amount + " to " + toAcc + ". Balance: $" + source.getBalance());
        destination.getTransactionHistory().add("Received $" + amount + " from " + fromAcc + ". Balance: $" + destination.getBalance());
        System.out.println("Transfer successful!");
    }

    public void deleteAccount(String accNum) {
        if (accounts.containsKey(accNum)) {
            accounts.remove(accNum);
            System.out.println("Account " + accNum + " deleted successfully.");
        } else {
            System.out.println("Account " + accNum + " does not exist.");
        }
    }

    public static void sendAccountCreationEmail(String toEmail, String name, String accNum, double balance) {
        String from = "prodbot8@gmail.com";
        String password = "yjymwbjzfaszdmzj";
        String host = "smtp.gmail.com";
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject("Account Created Successfully");
            message.setText("Dear " + name + ",\n\nYour account has been created successfully.\n\nAccount Number: " + accNum + "\nInitial Balance: $" + balance + "\n\nThank you for banking with us!");

            Transport.send(message);
            System.out.println("Account creation email sent to " + toEmail);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    public static void sendTransactionHistoryEmail(String toEmail, String fileName) {
        String from = "prodbot8@gmail.com";
        String password = "yjymwbjzfaszdmzj";
        String host = "smtp.gmail.com";
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject("Your Transaction History");

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Dear Account Holder,\n\nPlease find attached your transaction history.\n\nThank you for banking with us!");

            // Create the attachment part
            MimeBodyPart attachmentPart = new MimeBodyPart();
            DataSource source = new FileDataSource(fileName);
            attachmentPart.setDataHandler(new DataHandler(source));
            attachmentPart.setFileName(fileName);

            // Create multipart
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentPart);

            // Set the multipart as the message content
            message.setContent(multipart);

            Transport.send(message);
            System.out.println("Transaction history email sent to " + toEmail);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}

// 4. The Main Class
public class BankingSystem {
    public static void main(String[] args) {
        // Launch the GUI
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new BankingGUI().setVisible(true);
            }
        });
    }
}
