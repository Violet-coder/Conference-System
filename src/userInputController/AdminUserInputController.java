package userInputController;

import controller.ClassCollection;
import presenter.AdminUserPresenter;
import presenter.TableGenerator;
import presenter.TextPresenter;
import usecase.MessageManager;

import java.util.*;
import java.util.stream.Collectors;
/**
 * AdminUserInputController handles all the admin user input, deals with the user request
 * including deleting message by id, username or keyword, and deleting empty talks. It
 * interact with multiple use cases and presenters to complete the functionalities.
 * */
public class AdminUserInputController extends UserInputController {
    public ClassCollection classCollection;
    private static final String TEXT_RESET = "\u001B[0m";
    private static final String TEXT_RED = "\u001B[31m";
    private static final String TEXT_YELLOW = "\u001B[33m";
    private final String TEXT_GREEN = "\u001B[32m";
    private static final String TEXT_BLUE = "\u001B[34m";
    private TableGenerator tableGenerator = new TableGenerator();
    private TextPresenter textPresenter = new TextPresenter();
    private AdminUserPresenter adminUserPresenter = new AdminUserPresenter();

    /**
     * Constructor for AdminUserInputController requires the classCollection which
     * contains all the instantiated controllers and user case instances.
     * */
    public AdminUserInputController(ClassCollection classCollection) {
        this.classCollection = classCollection;
    }

    /**
     * The function get user input at the main menu for admin user.
     * Based on the input, call the corresponding method to complete user request.
     * */
    @Override
    public void getCommand() {
        Scanner scanner = new Scanner(System.in);
        textPresenter.printNormalText("Enter a command:");
        String line = scanner.nextLine();
        String[] cmdAndArgs = line.trim().split("\\s+");
        String cmd = cmdAndArgs[0];

        switch (cmd) {
            case "1":
                handleDeleteMessageByID();
                break;
            case "2":
                handleDeleteMessageByUsername();
                break;
            case "3":
                handleDeleteMessageByKeyword();
                break;
            case "4":
                deleteEmptyTalkByID();
                break;
            case "5":
                displayAllTalks();
                break;
            case "6":
                displayAllSpeakers();
                break;
            case "7":
                displayAllRooms();
                break;
            case "8":
                isLoggedIn = false;
                break;
            default:
                textPresenter.printErrorText("Invalid Command.\n");
        }
    }

    /**
     * Display the main menu for admin user.
     * */
    @Override
    public void printMenu() {
        adminUserPresenter.printAdminUserMenu();
    }

    /**
     * It is called when user select the option for deleting messages at the main menu.
     * The method interact with presenters and use cases to handle the user request.
     * */
    public void handleDeleteMessageByID() {

        // return if no message to delete
        if(classCollection.messageManager.getMessage().size() == 0){
            textPresenter.printNotificationText("There is no message stored.");
            return;
        }

        while (true) {
            // display a list of all messages
            displayAllMessages();
            // get user input and delete the message
            int code = deleteMessageByID();
            // code -4 represents that user cancels the operation
            if(code == -4) {
                break;
            }
        }
    }


    private int deleteMessageByID() {
        // get user input of message id
        int messageID = getInputID("Message ID");
        // if the message id < 0 , it means the user cancel the operation
        if(messageID < 0) {
            // use -4 as the code for cancelling
            return -4;
        }
        // call use case method to get the message info and controller method to delete the message
        String messageInfo = classCollection.messageManager.printMessageByID(messageID);
        int deletedMessageID = classCollection.adminUserController.deleteMessageByID(messageID);
        // the response from controller represents the deleting result
        printDeleteMessageByIDResult(messageID, messageInfo, deletedMessageID);
        return deletedMessageID;
    }

    private void printDeleteMessageByIDResult(int messageID, String messageInfo,int deletedMessageID){
        if(deletedMessageID > -1) {
            textPresenter.printSuccessText("Delete the following message with ID "+ messageID +" successfully!" );
            textPresenter.printInfoText(messageInfo);
            textPresenter.printDividerLine();
        } else if(deletedMessageID == -1) {
            textPresenter.printErrorText("The Message with ID " + messageID + " doesn't exist.\n");
        } else if(deletedMessageID == -2) {
            textPresenter.printErrorText("Exception occurs when save the change to local file.");
        }
        else {
            textPresenter.printErrorText("Cannot delete the message with ID: " + messageID + ".");
        }
    }

    /**
     * Handle the user request for deleting message by username.
     * */
    public void handleDeleteMessageByUsername() {
        while (true) {
            textPresenter.printNormalText("Please enter the "+ TEXT_YELLOW+ " username" + TEXT_RESET+" for filtering messages:");
            textPresenter.printNormalText("(Or enter \"cancel\" to cancel the operation.)");
            String username = getInputString();
            if(username.equals("cancel")) {
                textPresenter.printNotificationText("The operation has been cancelled." );
                return;
            }

            int numMessages = displayMessagesOfAUser(username);
            //if find messages, print the next menu; else break
            if(numMessages > 0) {
                adminUserPresenter.printMenuForDeleteMsgByUsername();
            } else {
                continue;
            }

            // get next cmd
            textPresenter.printNormalText("Enter the command:");
            Scanner sc = new Scanner(System.in);
            String cmd = sc.nextLine().trim();
            switch (cmd) {
                //delete-message-by-id
                case "1":
                {
                    while (true) {
                        displayMessagesOfAUser(username);
                        int code = deleteMessageByID();
                        // code -4 represents that user cancels the operation
                        if(code == -4) {
                            //
                            return;
                        }

                    }
                }
                default:
                    return;
            }
        }

    }

    /**
     * Handle the user request for deleting messages by keyword.
     * */
    public void handleDeleteMessageByKeyword() {
        while (true) {
            textPresenter.printNormalText("Please enter the"+ TEXT_YELLOW +" keyword" + TEXT_RESET +" to filter messages:");
            textPresenter.printNormalText("(Or enter \"cancel\" to cancel the operation.)");
            String keyword = getInputString();
            if(keyword.equals("cancel")) {
                textPresenter.printNotificationText("The operation has been cancelled.");
                return;
            }

            int numMessages = displayMessagesByKeyword(keyword);
            //if find messages, print the next menu; else break
            if(numMessages > 0) {
                printMenuForDeleteMsg();
            } else {
                continue;
            }

            // get next cmd
            textPresenter.printNormalText("Enter the command:");
            Scanner sc = new Scanner(System.in);
            String cmd = sc.nextLine().trim();
            switch (cmd) {
                // delete-message-by-id
                case "1":
                {
                    while (true) {
                        int numRemainingMsg = displayMessagesByKeyword(keyword);
                        if(numRemainingMsg == 0) {
                            break;
                        }
                        int code = deleteMessageByID();
                        // code -4 represents that user cancels the operation
                        if(code == -4) {
                            // continue;
                            return;
                        }
                    }
                }
                //delete-all-filtered-messages
                case "2":
                    deleteAllMessagesByKeyword(keyword);
                    break;
                //go-back
                case "3":
                    break;
                default:
                    textPresenter.printErrorText("Invalid command." );
                    return;
            }
        }
    }


    private int deleteAllMessagesByKeyword(String keyword) {
        Map<Integer, List<String>> messagesByKeywords = classCollection.messageManager.getMessagesByKeywords(keyword);
        if(messagesByKeywords.size() == 0) {
            textPresenter.printErrorText( "Find no matching messages for the keyword: " + keyword);
            return 0;
        }
        // call use case method to delete messages by ids
        List<Integer> messageIds = messagesByKeywords.keySet().stream().collect(Collectors.toList());
        int res = classCollection.messageManager.deleteMessagesByIDs(messageIds);
        if(res == messageIds.size()) {
            textPresenter.printSuccessText("Successfully delete " + res + " messages!");
        } else {
            // shouldn't occur
            textPresenter.printErrorText("Error: fail to delete messages!");
        }
        return res;
    }


    private int displayMessagesByKeyword(String keyword){
        Map<Integer, List<String>> messagesByKeywords = classCollection.messageManager.getMessagesByKeywords(keyword);
        List<List<String>> messageRows = new ArrayList<List<String>>(messagesByKeywords.values());
        if(messagesByKeywords.size() == 0) {
            textPresenter.printNormalText("Cannot find messages for the keyword: " + keyword +"\n");
            return 0;
        }
        textPresenter.printNotificationText("Here are the filtered messages for the keyword: " + keyword );

        List<String> header = Arrays.asList("Message ID", "Sender Username", "Receiver Username", "Content");
        tableGenerator.printAsTable(header,messageRows);

        return messagesByKeywords.size();
    }

    private void printMenuForDeleteMsg() {
        adminUserPresenter.printMenuForDeleteMsg();
    }

    private int displayMessagesOfAUser(String username) {
        List<List<String>> messageOfUser = classCollection.messageManager.getMessagesByUsername(username);
        if(messageOfUser.size() == 0) {
            textPresenter.printNotificationText("Cannot find messages sent or received by the user: " + username +"\n");
            return 0;
        }
        textPresenter.printNotificationText("Here are the filtered messages sent or received by the user: " + username);

        // print the list of talks as table
        List<String> header = Arrays.asList("Message ID", "Sender Username", "Receiver Username", "Content");
        tableGenerator.printAsTable(header,messageOfUser);

        return messageOfUser.size();
    }

    private void deleteEmptyTalkByID() {
        // print all empty talks
        int num = displayEmptyTalks();
        if(num == 0 ) {
            //no empty talks
            return;
        }
        int talkID = getInputID("Talk ID");
        // return if talkID not an integer or talk not exists
        if(talkID < 0 || !classCollection.talkManager.isTalkExisting(talkID)) {
            return;
        }

        // check whether the talk is empty;
        // admin cannot delete a talk that has attendees
        int numAttendees = classCollection.talkManager.getNumAttendeesByTalkID(talkID);
        if(numAttendees > 0) {
            textPresenter.printErrorText("Cannot delete a non-empty event!");
            return;
        }

        // get the talk info with specified id
        String talkInfo = classCollection.talkManager.printTalk(talkID);

        // delete Talk BY ID and print result
        Map<Integer, Integer> res = classCollection.adminUserController.deleteEmptyTalkByID(talkID);
        printDeleteTalkResult(talkID, talkInfo, res);
    }

    private void printDeleteTalkResult(int talkID, String talkInfo,Map<Integer, Integer> res){
        int deletedTalkRes = res.get(talkID);

        if(deletedTalkRes < 0) {
            String messageToPrint = "Fail to delete the talk for the following possible reasons:\n";
            messageToPrint += "- The talk doesn't exist.\n";
            messageToPrint += "- The talk is not empty.";
            textPresenter.printErrorText(messageToPrint);
        } else {
            textPresenter.printSuccessText("Delete the following talk successfully!");
            textPresenter.printSuccessText(talkInfo);
        }

    }

    /**
     * Display all stored messages.
     * */
    public void displayAllMessages() {
        MessageManager messageManager = classCollection.messageManager;
        int allMessages = messageManager.getNumAllMessaegs();
        List<List<String>> allMessageRows = messageManager.getAllMessageRows();
        if(allMessages == 0) {
            textPresenter.printNotificationText("There are no stored messages.\n");
            textPresenter.printDividerLine();
            return;
        }

        textPresenter.printNotificationText("This is the table for all messages.\n");
        List<String> header = Arrays.asList("Message ID", "Sender Username", "Receiver Username", "Content");
        tableGenerator.printAsTable(header,allMessageRows);
    }

    /**
     * Display all stored events.
     * */
    public void displayAllTalks() {
        List<List<String>> allTalks = classCollection.talkManager.getAllTalkRows();
        if(allTalks.size() == 0) {
            textPresenter.printNotificationText("There are no talks.\n");
        } else {
            textPresenter.printNotificationText("Here are all the talks stored in the conference system." );
            List<String> header = Arrays.asList("Event ID", "Event Title","Type","Speaker ID(s)", "Start Date", "End Date",
                    "Start Time", "End Time", "Room ID", "Capacity");
            tableGenerator.printAsTable(header, allTalks);
        }
    }

    /**
     * Display the events without attendees.
     * */
    public int displayEmptyTalks() {
        List<List<String>> emptyTalks = classCollection.talkManager.getEmptyTalkRows();
        if(emptyTalks.size() == 0) {
            textPresenter.printNotificationText("There are no empty talks to be deleted.\n");
            return 0;
        } else {
            List<String> header = Arrays.asList("Event ID", "Event Title","Type","Speaker ID(s)", "Start Date", "End Date",
                    "Start Time", "End Time", "Room ID", "Capacity");
            tableGenerator.printAsTable(header,emptyTalks);
        }
        return 1;
    }

    /**
     * Display all speaker information.
     * */
    public void displayAllSpeakers(){
        String allSpeakersInfo = classCollection.speakerManager.allSpeakerInfo();
        if(allSpeakersInfo == "") {
            textPresenter.printNotificationText("There are no speaker.\n");
        } else {
            textPresenter.printNotificationText("Here are all the speakers stored in the conference system.");
            textPresenter.printNormalText(allSpeakersInfo);
        }
    }

    /**
     * Display all stored rooms in a table.
     * */
    public void displayAllRooms() {
        String allRoomsInfo = classCollection.roomManager.allRoomsInfo();
        if(allRoomsInfo == "") {
            textPresenter.printNotificationText("There are no room.\n");
        } else {
            textPresenter.printNotificationText("Here are all the rooms stored in the conference system.");
            List<String>  roomHeader = Arrays.asList("Room ID", "Room Name", "Capacity", "Technology Requirements",
                    "Room Conditions", "Talk IDs");
            tableGenerator.printAsTable(roomHeader, classCollection.roomManager.getAllRoomRows());
        }
    }

    private int getInputID(String inputName) {
        Scanner sc = new Scanner(System.in);
        textPresenter.printNotificationText("Please enter the "+ inputName + " you want to delete:");
        textPresenter.printNormalText("(Or enter \"cancel\" to cancel the operation.)");
        String inputIDCmd;

        inputIDCmd = sc.nextLine().trim();

        while( !inputIDCmd.matches("\\d+") && !inputIDCmd.equals("cancel")) {
            textPresenter.printDividerLine();
            textPresenter.printNotificationText("The "+ inputName + " should be a positive integer. Please try again." );
            textPresenter.printNormalText("Enter the "+inputName+" or cancel:");
            inputIDCmd = sc.nextLine().trim();
        }

        if(inputIDCmd == "cancel") {
            textPresenter.printNotificationText("The operation has been cancelled.");
            return -1;
        }
        try {
            int messageID = Integer.parseInt(inputIDCmd);
            return messageID;
        } catch (NumberFormatException e) {
            return -1;
        }
    }


    private String getInputString() {
        Scanner sc = new Scanner(System.in);
        String usernameCmd;
        usernameCmd = sc.nextLine().trim();
        return usernameCmd;
    }

}
