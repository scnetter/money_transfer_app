package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private final AccountService accountService = new AccountService();
    private final UserService userService = new UserService();

    private final TransferService transferService = new TransferService();

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {

        accountService.setAuthToken(currentUser.getToken());

		Account account = accountService.getAccountByUserId(currentUser.getUser().getId());
        System.out.println("Your current balance is: $" + account.getBalance());
	}

	private void viewTransferHistory() {
		// TODO Auto-generated method stub
		
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}

	private void sendBucks() {
        userService.setAuthToken(currentUser.getToken());
        // TODO Auto-generated method stub
        BigDecimal transferAmount = new BigDecimal(0);
        User[] users = userService.getUsers();
        List<Integer> userIds = new ArrayList<>();

        for (User user : users) {
            if (currentUser.getUser().getId() != user.getId()) {
                System.out.println(user.getId() + " " + user.getUsername());
                userIds.add(user.getId());
            }
        }


        int transferUserId = 0;
        while (true) {
            transferUserId = consoleService.promptForInt("\nEnter the User ID of the user to send money: ");
            if (!userIds.contains(transferUserId)) {
                System.out.println("You must select a valid User ID.");
                continue;
            }
            break;
        }

        while(true) {
            transferAmount = consoleService.promptForBigDecimal("\n Enter the amount to transfer: ");
            if (transferAmount.compareTo(new BigDecimal(0)) == 0) {
                System.out.println("You must enter a valid amount.");
                continue;
            }
            break;
        }

        accountService.setAuthToken(currentUser.getToken());
        Account toAccount = accountService.getAccountByUserId(transferUserId);
        Account fromAccount = accountService.getAccountByUserId(currentUser.getUser().getId());

        TransferDto transferDto = new TransferDto(2, fromAccount.getAccount_id(),
                toAccount.getAccount_id(), transferAmount );

        transferService.setAuthToken(currentUser.getToken());
        if(transferService.sendBucks(transferDto)){
            System.out.println("Transfer Successful.");
        } else {
            System.out.println("Transfer Failed.");
        }
    }

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}

}
