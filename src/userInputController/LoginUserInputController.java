package userInputController;

import controller.ClassCollection;
import presenter.TextPresenter;

import java.util.Scanner;

/**
 * LoginUserInputController: handles user input, validate the user input and pass the user input to loginController
 */
public class LoginUserInputController {
    public ClassCollection classCollection;
    private TextPresenter textPresenter = new TextPresenter();
    private static final String TEXT_RED = "\u001B[31m";
    private static final String TEXT_GREEN = "\u001B[32m";
    private static final String TEXT_RESET = "\u001B[0m";


    /**
     * Constructor of LoginPresenter that receives the parameter ClassCollection.
     * @param classCollection       a collection of all use cases and controllers
     * */
    public LoginUserInputController(ClassCollection classCollection){
        this.classCollection = classCollection;
    }


    /**
     * Interact with the user to enter username and password to login.
     */
    public boolean login(){
        Scanner scanner = new Scanner(System.in);

        textPresenter.printNormalText("Welcome to login:");
        textPresenter.printNormalText("Please enter your username:");
        String username = scanner.nextLine().trim();
        while(username.length()<1){
            textPresenter.printErrorText("Username can not be empty.");
            textPresenter.printNormalText("Please enter your username:");
            username = scanner.nextLine().trim();
        }
        textPresenter.printNormalText("Please enter your password:");
        String password = scanner.nextLine().trim();
        while(password.length()<1){
            textPresenter.printErrorText("Password can not be empty.");
            textPresenter.printNormalText("Please enter your password:");
            password = scanner.nextLine().trim();
        }
        boolean flag = classCollection.loginController.login(username,password);
        if (flag){
            textPresenter.printSuccessText("Login successfully.");
        } else {
            textPresenter.printErrorText("Login failed: wrong username or password.");
        }
        return flag;

    }



}
