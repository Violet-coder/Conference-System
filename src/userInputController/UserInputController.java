package userInputController;

import java.io.IOException;
/**
 * Abstract class for controllers handling user input. It is
 * extended by the controllers with a particular user type.
 * */
public abstract class UserInputController {
    public boolean isLoggedIn = false;
    /**
     * The method is used to get user input and deal with user request.
     * */
    public abstract void getCommand() throws IOException;
    /**
     * Print multiple commands that user can select from.
     * */
    public abstract void printMenu();
}


