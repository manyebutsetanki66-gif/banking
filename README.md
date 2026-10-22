# Banking System

A command-line banking system written in Java. It models savings and current accounts,
supports deposits, withdrawals, transfers, interest calculation and loan requests, and
records every transaction to an audit log.

The project lives in the [`Banksystem/`](Banksystem) folder.

## Features

| # | Menu option          | Description                                                |
|---|----------------------|------------------------------------------------------------|
| 1 | Display all accounts | Shows the details of every account in the system            |
| 2 | Deposit              | Adds funds to an account                                   |
| 3 | Withdraw             | Removes funds, subject to per-account rules                |
| 4 | Transfer             | Moves funds between two accounts                           |
| 5 | Calculate interest   | Applies interest to the selected account                    |
| 6 | Request loan         | Requests a loan against the selected account               |
| 7 | Account details      | Shows the details of one account                           |
| 8 | Exit                 | Ends the session                                           |

## Account rules

**SavingsAccount**
- Minimum balance of M100.00 — a withdrawal that would breach it is rejected.
- Earns 5% annual interest, calculated on the current balance.
- Overdraft limit: none.

**CurrentAccount**
- Overdraft limit of M2,000.00 — the balance may go negative, but not below -M2,000.00.
- Earns no interest.

**Loans (both account types)**
- Maximum loan amount of M10,000.00.
- A loan must be for a positive amount.

## Transaction log

Every attempt — successful or rejected — is appended to `transactions.txt` in the format:

```
2026-10-22 11:33:34 | Account: SA001 | Type: Deposit | Amount: M300.00 | Balance: M1300.00 | Result: Deposit successful
```

Rejected operations are logged too, with the rejection reason as the result. A transfer
is recorded against both the source and the destination account.

## Project structure

```
Banksystem/
├── src/
│   ├── BankingSystemMain.java      # Entry point and menu handling
│   └── banking/
│       ├── BankAccount.java        # Abstract superclass (encapsulation of balance)
│       ├── SavingsAccount.java     # Savings rules
│       ├── CurrentAccount.java     # Current account rules
│       ├── Loanable.java           # Interface for loan requests
│       └── TransactionLogger.java  # Appends records to transactions.txt
├── lib/                            # Dependency JARs (none required)
├── .vscode/settings.json           # VS Code Java project settings
├── transactions.txt                # Generated transaction log
└── README.md
```

## Design notes

- `BankAccount` is abstract and owns the balance. `balance` is private, and subclasses
  change it through the `protected setBalance(...)` method rather than directly.
- `deposit`, `withdraw` and `calculateInterest` are declared abstract so each account type
  enforces its own rules.
- `Loanable` is an interface implemented by both account types, so loan requests are
  handled polymorphically.
- `TransactionLogger` is a `final` utility class with a private constructor, so it cannot
  be instantiated or extended.
- A transfer only credits the destination after the source withdrawal succeeds. If the
  destination deposit fails, the source balance is restored.

## Requirements

- JDK 8 or newer (developed and verified on JDK 26)

## How to run

Compile and run from the `Banksystem` folder:

```bash
cd Banksystem

# Compile
javac -d bin src/BankingSystemMain.java src/banking/*.java

# Run
java -cp bin BankingSystemMain
```

The program starts with two demo accounts:

| Account number | Holder | Type     | Opening balance |
|----------------|--------|----------|-----------------|
| SA001          | Thabo  | Savings  | M1,000.00       |
| CA001          | Lerato | Current  | M500.00         |

Note that `transactions.txt` is written to the working directory, so run the program from
the folder where you want the log to be created.

## VS Code

Open the `Banksystem` folder in VS Code with the Extension Pack for Java installed. The
`.vscode/settings.json` file configures `src` as the source path and `bin` as the output
path, so you can use **Run Java** without additional setup.
