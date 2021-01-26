package userInputController;

import controller.ClassCollection;
import presenter.OrganizerUserPresenter;

import java.io.IOException;
import java.util.Scanner;

/**
 * Handles input from organizer user and deals with user requests.
 * */
public class OrganizerInputController extends UserInputController {
    public ClassCollection classCollection;
    private static final String TEXT_RESET = "\u001B[0m";
    private static final String TEXT_RED = "\u001B[31m";
    private OrganizerUserPresenter organizerUserPresenter =  new OrganizerUserPresenter();


    /**
     * Constructor of OrganizerPresenter that receives the parameter ClassCollection.
     * @param classCollection       a collection of all use cases and controllers
     * */
    public OrganizerInputController(ClassCollection classCollection){
        this.classCollection = classCollection;
    }

    /**
     * Print the Organizer Menu that includes all the commands that users can use.
     * */
    public void printMenu() {
        organizerUserPresenter.printMenu();
    }

    /**
     * Get the user input from scanner, and redirect the user to different Presenters accordingly.
     * */
    public void getCommand() throws IOException {
        AdminInputController adminPresenter = new AdminInputController(classCollection);
        MessageInputController messagePresenter = new MessageInputController(classCollection);
        RequestInputController requestPresenter = new RequestInputController(classCollection);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a command:");
        String line = scanner.nextLine();
        String[] cmdAndArgs = line.trim().split("\\s+");
        String cmd = cmdAndArgs[0];

        // TODO: implement adminpresenter and controller cases for new talks
        switch (cmd) {
            case "add-talk":
                adminPresenter.scheduleTalk();
                break;
            case "modify-talk":
                adminPresenter.modifyTalk();
                break;
            case "modify-party":
                adminPresenter.modifyParty();
                break;
            case "modify-panel-discussion":
                adminPresenter.modifyPanelDiscussion();
                break;
            case "add-party":
                adminPresenter.scheduleParty();
                break;
            case "add-panel-discussion":
                adminPresenter.schedulePanelDiscussion();
                break;
            case "add-speaker-to-panel-discussion":
                adminPresenter.addSpeakerToPanelDiscussion();
                break;
            case "add-room":
                adminPresenter.addRoom();
                break;
            case "add-attendee":
                adminPresenter.addAttendee();
                break;
            case "add-speaker":
                adminPresenter.addSpeaker();
                break;
            case "add-organizer":
                adminPresenter.addOrganizer();
                break;
            case "add-admin":
                adminPresenter.addAdmin();
                break;
            case "cancel-event":
                adminPresenter.cancelEvent();
                break;
            case "send-message":
                messagePresenter.sendMessage("organizer");
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
            case "view-request":
                requestPresenter.readRequest();
                break;
            case "logout":
                isLoggedIn = false;
                break;
            default:
                System.out.println(TEXT_RED+"Invalid command."+TEXT_RESET);
        }
    }

}



