package userInputController;

import controller.ClassCollection;
import controller.RegisterController;
import presenter.TextPresenter;

import java.util.Scanner;

/**
 * RegisterUserInputController: handles user input, validate the user input and pass the user input to registerController
 */
public class RegisterUserInputController {
    private  RegisterController registerController;
    private  TextPresenter textPresenter = new TextPresenter();

    /**
     * Constructor of RegisterPresenter that receives the parameter ClassCollection.
     * @param classCollection       a collection of all use cases and controllers
     * */
    public RegisterUserInputController(ClassCollection classCollection){
        this.registerController = new RegisterController(classCollection.speakerManager, classCollection.attendeeManager);

    }

    /**
     * Interact with the user to  to create a new account.
     */
    public void register(){
        String username;
        String password;
        String usertype;
        String name;

        String welcome = "Welcome to register:";
        textPresenter.printNotificationText(welcome);
        username = getUsername();
        name = getName();
        password = getPassword();
        usertype = getUsertype();


        boolean flag = registerController.register(username,password,usertype,name);
        if (flag){
            String fail = "Sign up failed.";
            textPresenter.printErrorText(fail);
        }else{
            String success = "Sign up successfully and now you could login.";
            textPresenter.printSuccessText(success);
        }
    }
    /**
     * Interact with the user to get his real name.
     */
    public String getName(){
        Scanner sc = new Scanner(System.in);
        String name;
        textPresenter.printNormalText("Please enter your real name: ");
        name = sc.nextLine().trim();
        return name;
    }

    /**
     * Interact with the user to get his username and do the format check.
     */
    public String getUsername(){
        int lengthLow = 6;
        int lengthHigh = 30;

        Scanner sc = new Scanner(System.in);
        textPresenter.printNormalText("Please enter your username: ");
        String username = sc.nextLine().trim();
        // Check characters
        boolean isCharsValid = username.matches("[A-Za-z][A-Za-z0-9_]+");
        // Check length
        boolean isLengthValid = username.length() >= lengthLow && username.length() <= lengthHigh;
        boolean isDuplicateUsername = registerController.duplicateUsername(username);

        while(!(isCharsValid&&isLengthValid&&(!isDuplicateUsername))){
            if (!isCharsValid) {
                textPresenter.printErrorText("The username should only contain alphanumeric characters (A-Z, a-z or 0-9)" +
                        " or underscore, and should begin with a letter.");
            }
            if (!isLengthValid) {
                textPresenter.printErrorText("The username should be "+lengthLow+"-"+lengthHigh+" characters long.");
            }
            if(isDuplicateUsername){
                textPresenter.printErrorText("The username is already taken.");
            }
            textPresenter.printNormalText("Please enter your username:");
            username = sc.nextLine().trim();

            isCharsValid = username.matches("[A-Za-z][A-Za-z0-9_]+");
            isLengthValid = username.length() >= lengthLow && username.length() <= lengthHigh;
            isDuplicateUsername = registerController.duplicateUsername(username);
        }
        return username;
    }

    /**
     * Interact with the user to get his password and do the format check.
     */
    public String getPassword(){
        int lengthLow = 10;
        int lengthHigh = 30;

        Scanner sc = new Scanner(System.in);
        textPresenter.printNormalText("Please enter your password: ");
        String password = sc.nextLine().trim();

        // Check length
        boolean isLengthValid = password.length() >= lengthLow && password.length() <= lengthHigh;
        while (!isLengthValid) {
            textPresenter.printErrorText("The password should be "+lengthLow+"-"+lengthHigh+" characters long.");
            textPresenter.printNormalText("Please enter your password: ");
            password = sc.nextLine().trim();
            isLengthValid = password.length() >= lengthLow && password.length() <= lengthHigh;
        }

        return password;
    }


    /**
     * Interact with the user to get the option number of the usertype and do the format check.
     */
    public String getUsertype(){
        Scanner sc = new Scanner(System.in);
        textPresenter.printNormalText("Please choose your usertype(enter number):");
        textPresenter.printNormalText("1: Attendee");
        textPresenter.printNormalText("2: Speaker");
        String typeChoice = sc.nextLine().trim();
        boolean isChoiceValid =typeChoice.equals("1")||typeChoice.equals("2");
        while(!isChoiceValid){
            textPresenter.printErrorText("Please enter the right number of the option.");
            textPresenter.printNormalText("Please choose your usertype(enter number):");
            typeChoice = sc.nextLine().trim();
            isChoiceValid = typeChoice.equals("1")||typeChoice.equals("2");

        }

        if (typeChoice.equals("1")){
            return "attendee";
        }else {
            return "speaker";
        }

    }





}
