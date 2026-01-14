# 🏦 Banking System Project

A Java-based banking system with both console and graphical user interface (GUI) support. The system allows users to perform various banking operations such as creating accounts, depositing/withdrawing funds, transferring money, and viewing transaction history.

## ✨ Features

- **👤 Account Management**: Create, delete, and manage bank accounts
- **💰 Fund Operations**: Deposit and withdraw money with balance validation
- **🔄 Money Transfer**: Transfer funds between accounts with minimum balance checks
- **📜 Transaction History**: View and save transaction history to file
- **📧 Email Notifications**: Automatic email notifications for account creation and transaction history
- **🖥️ GUI Interface**: User-friendly graphical interface using Java Swing
- **💻 Console Output**: Transaction history displayed in console for logging

## 📋 Prerequisites

- ☕ Java Development Kit (JDK) 8 or higher
- 📬 JavaMail API (javax.mail) - included as mail.jar
- 📦 JavaBeans Activation Framework (JAF) - included as activation.jar

## 🚀 Installation

1. 📥 Clone or download the project files
2. ✅ Ensure the following JAR files are present in the project directory:
   - `mail.jar` (JavaMail API)
   - `activation.jar` (JavaBeans Activation Framework)

## 🔨 Compilation

Compile the Java files with the required dependencies:

```bash
javac -cp mail.jar;activation.jar BankingSystem.java BankingGUI.java
```

## ▶️ Running the Application

Run the application using:

```bash
java -cp .;mail.jar;activation.jar BankingSystem
```

This will launch the GUI interface. The application provides buttons for all banking operations.

## 📖 Usage

### 🖱️ GUI Operations

1. **➕ Create Account**: Enter account number, holder name, email, and initial balance
2. **💸 Deposit Funds**: Enter account number and deposit amount
3. **💳 Withdraw Funds**: Enter account number and withdrawal amount (minimum balance of $100 must be maintained)
4. **🔄 Transfer Funds**: Enter source account, destination account, and transfer amount
5. **📊 Check Balance**: Enter account number to view current balance
6. **📜 Transaction History**: Enter account number to view and save transaction history (displayed in console and dialog)
7. **🗑️ Delete Account**: Enter account number to delete the account
8. **🚪 Exit**: Close the application

### ⚖️ Business Rules

- 💵 Minimum balance of $100 must be maintained in accounts
- 🚫 Withdrawal amounts cannot exceed available balance
- ➕ Transfer amounts must be positive
- 🔍 All operations validate account existence
- 📧 Email notifications are sent for account creation and transaction history

## 📁 File Structure

- `BankingSystem.java`: Main application class that launches the GUI
- `BankingGUI.java`: Graphical user interface implementation
- `Bank.java`: Bank class handling account management and operations
- `Account.java`: Account class managing individual account data
- `InsufficientFundsException.java`: Custom exception for insufficient funds
- `mail.jar`: JavaMail API library
- `activation.jar`: JavaBeans Activation Framework library

## 📧 Email Configuration

The system is configured to send emails using Gmail SMTP. To use email functionality:

1. ⚙️ Update the email credentials in `Bank.java`:
   - `from`: Sender email address
   - `password`: App password (not regular password)

2. 🔐 Ensure the sender email has "Less secure app access" enabled or uses an app password.

## 📦 Dependencies

- **📬 JavaMail API**: For sending emails
- **📦 JavaBeans Activation Framework**: Required for email attachments

## ⚠️ Error Handling

The application includes comprehensive error handling for:
- ❌ Invalid input amounts
- 💸 Insufficient funds
- 🔍 Non-existent accounts
- 📧 Email sending failures

## 💻 Console Output

- 📜 Transaction history is displayed in the console when viewed through the GUI
- 📧 Email sending confirmations are logged to console
- 🔍 Operation results are printed for debugging

## 💡 Development Notes

- 🖥️ The system uses Java Swing for the GUI
- 🌐 Email functionality requires internet connection and valid SMTP credentials
- 📄 Transaction history is saved as text files in the project directory
- 💵 All monetary values are handled as doubles

## 📄 License

This project is for educational purposes. Modify and distribute as needed.

## 🤝 Contributing

Feel free to contribute improvements, bug fixes, or additional features to the banking system.
