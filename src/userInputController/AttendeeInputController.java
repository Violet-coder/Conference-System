package userInputController;

import controller.ClassCollection;
import presenter.AttendeeUserPresenter;

import java.util.Scanner;

/**
 * AttendeeInputController contains methods for getting user input at the main menu,
 * and calling other controllers based on the command from user. It works as a Facade
 * to interact with all kinds of controllers with specific functionalities.
 *
 * */
public class AttendeeInputController extends UserInputController {

    public ClassCollection classCollection;
    private AttendeeUserPresenter attendeeUserPresenter = new AttendeeUserPresenter();

    /**
     * Constructor of AttendeePresenter that receives the parameter ClassCollection.
     * @param classCollection       a collection of all use cases and controllers
     * */
    public AttendeeInputController(ClassCollection classCollection){
        this.classCollection = classCollection;
    }

    /**
     * Print the Attendee Menu that includes all the commands that users can use.
     * */
    public void printMenu() {
       attendeeUserPresenter.printAttendeeMenu();
    }

    /**
     * Get the user input from scanner, and redirect the user to different Presenters accordingly.
     * */
    public void getCommand() {
        Scanner scanner = new Scanner(System.in);
        MessageInputController messagePresenter = new MessageInputController(classCollection);
        SignUpInputController signUpInputController = new SignUpInputController(classCollection);
        RequestInputController requestPresenter = new RequestInputController(classCollection);
        System.out.println("Enter a command:");
        String line = scanner.nextLine();
        String[] cmdAndArgs = line.trim().split("\\s+");
        String cmd = cmdAndArgs[0];
        switch (cmd) {
            case "see-events-I-can-sign-up":
                //need to be implemented
                signUpInputController.handleCmd("see-events-I-can-sign-up");
                break;
            case "see-events-I-have-signed-up":
                signUpInputController.handleCmd("see-events-I-have-signed-up");
                break;
            case "send-message":
                messagePresenter.sendMessage("attendee");
                break;
            case "read-message":
                messagePresenter.readMessage(classCollection.loginController.getUserId(), "attendee");
                break;
            case "see-all-messages":
                messagePresenter.seeMessage(classCollection.loginController.getUserId(), "attendee");
                break;
            case "delete-message":
                messagePresenter.deleteMessage(classCollection.loginController.getUserId(), "attendee");
                break;
            case "see-unread":
                messagePresenter.seeUnread(classCollection.loginController.getUserId(), "attendee");
                break;
            case "mark-as-unread":
                messagePresenter.markAsUnread(classCollection.loginController.getUserId(), "attendee");
                break;
            case "archive-message":
                messagePresenter.archiveMessage(classCollection.loginController.getUserId(), "attendee");
                break;
            case "see-archive":
                messagePresenter.seeArchive(classCollection.loginController.getUserId(), "attendee");
                break;
            case "review-my-request":
                requestPresenter.reviewRequest(classCollection.loginController.getUserId());
                break;
            case "logout":
                isLoggedIn = false;
                break;
            default:
                System.out.println("Invalid command\n");

        }




    }
}
