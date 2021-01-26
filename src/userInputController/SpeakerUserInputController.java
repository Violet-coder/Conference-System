package userInputController;

import controller.ClassCollection;
import presenter.SpeakerPresenter;
import presenter.TextPresenter;
import usecase.ISpeakerManager;
import usecase.ITalkManager;

import java.util.List;
import java.util.Scanner;

/**
 * SpeakerUserInputController is the controller that handles the speaker user input
 * and requests.
 * */
public class SpeakerUserInputController extends UserInputController {
    public ClassCollection classCollection;
    private SpeakerPresenter speakerPresenter = new SpeakerPresenter();
    private TextPresenter textPresenter = new TextPresenter();


    /**
     * Constructor of SpeakerPresenter.
     * */
    public SpeakerUserInputController(ClassCollection classCollection){
        this.classCollection = classCollection;
    }


    /**
     * Print the speaker menu which includes all tha commands that the speaker can use.
     * */
    public void printMenu() {
        speakerPresenter.printSpeakerUserMenu();
    }

    /**
     * Get the user input from scanner, and redirect the user to different Presenters accordingly.
     * */
    public void getCommand() {
        Scanner scanner = new Scanner(System.in);
        MessageInputController messagePresenter = new MessageInputController(classCollection);

        textPresenter.printSuccessText("Enter a command:");
        String line = scanner.nextLine();
        String[] cmdAndArgs = line.trim().split("\\s+");
        String cmd = cmdAndArgs[0];
        switch (cmd) {
            case "see-my-talks":
                //need to be implemented
                seeMyTalks();
                break;
            case "send-message":
                messagePresenter.sendMessage("speaker");
                break;
            case "read-message":
                messagePresenter.readMessage(classCollection.loginController.getUserId(), "speaker");
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
            case "logout":
                isLoggedIn = false;
                break;
            default:
                textPresenter.printErrorText("Invalid command.");
        }
    }

    /**
     * Display all the talks that the speaker to give. The schedule of the talks
     * is sorted by time, from the latest to the oldest.
     * */
    public void seeMyTalks(){
        ISpeakerManager speakerManager = this.classCollection.speakerManager;
        ITalkManager talkManager = this.classCollection.talkManager;

        int speakerID = this.classCollection.loginController.getUserId();
        List<Integer> myTalkIDs = speakerManager.getTalksGivenBySpeaker(speakerID);
        List<Integer> sortedMyTalkIDs = this.classCollection.signUpController.sortTalkIDByTime(myTalkIDs);

        if(myTalkIDs.size() == 0) {
            textPresenter.printErrorText("You have no talks to give.");
        } else {
            textPresenter.printSuccessText("Here are your talks to give.");
        }

        for(int talkID : sortedMyTalkIDs){
            textPresenter.printNormalText(talkManager.getTalkSchedule(talkID));
            int roomID = talkManager.getRoomIDByTalkID(talkID);
            String roomName = classCollection.roomManager.getRoomNameByID(roomID) == null ? ""
                    : classCollection.roomManager.getRoomNameByID(roomID);
            textPresenter.printNormalText("Room Name: "+ roomName);
            int cSpeakerID = talkManager.getSpeakerIDByTalkID(talkID);
            String speakerName = speakerManager.getSpeakerNameByID(speakerID) == null ? ""
                    : speakerManager.getSpeakerNameByID(cSpeakerID);
            textPresenter.printNormalText("Speaker Name: " + speakerName);
            textPresenter.printNormalText("---------------------------------------------------------");
        }
    }
}
