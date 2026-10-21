package com.bank;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        DBConnection.init();
        Scanner sc = new Scanner(System.in);

        AccountCreation account = new AccountCreation();
        Deposit deposit = new Deposit();
        Withdrawal withdrawal = new Withdrawal();
        FundTransfer transfer = new FundTransfer();
        TransactionHistory history = new TransactionHistory();

        while (true) {
            System.out.println("\n1.Create Account 2.Deposit 3.Withdraw 4.Transfer 5.History 6.Balance 0.Exit");
            try {
                System.out.print("Choose: ");
                int choice = Integer.parseInt(sc.nextLine());

                switch (choice) {
                    case 1 -> {
                        System.out.print("Name: ");
                        String name = sc.nextLine();
                        System.out.print("Email: ");
                        String email = sc.nextLine();
                        System.out.print("Phone: ");
                        String phone = sc.nextLine();
                        System.out.print("Opening balance: ");
                        double bal = Double.parseDouble(sc.nextLine());
                        System.out.println("Account number: " + account.createAccount(name, email, phone, bal));
                    }
                    case 2 -> {
                        System.out.print("Account: ");
                        int acc = Integer.parseInt(sc.nextLine());
                        System.out.print("Amount: ");
                        double amt = Double.parseDouble(sc.nextLine());
                        deposit.deposit(acc, amt);
                    }
                    case 3 -> {
                        System.out.print("Account: ");
                        int acc = Integer.parseInt(sc.nextLine());
                        System.out.print("Amount: ");
                        double amt = Double.parseDouble(sc.nextLine());
                        withdrawal.withdraw(acc, amt);
                    }
                    case 4 -> {
                        System.out.print("From: ");
                        int from = Integer.parseInt(sc.nextLine());
                        System.out.print("To: ");
                        int to = Integer.parseInt(sc.nextLine());
                        System.out.print("Amount: ");
                        double amt = Double.parseDouble(sc.nextLine());
                        transfer.transfer(from, to, amt);
                    }
                    case 5 -> {
                        System.out.print("Account: ");
                        history.showHistory(Integer.parseInt(sc.nextLine()));
                    }
                    case 6 -> {
                        System.out.print("Account: ");
                        account.checkBalance(Integer.parseInt(sc.nextLine()));
                    }
                    case 0 -> { return; }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}