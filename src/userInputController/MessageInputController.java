package userInputController;

import controller.ClassCollection;
import entity.Message;
import presenter.TextPresenter;

import java.util.*;
import java.util.regex.*;
import java.util.regex.Pattern;

/**
 * the controller handles input of user and communicate between messaging controller and presenter
 */
public class MessageInputController {
    private ClassCollection classCollection;
    private TextPresenter presenter;

    /**
     * constructor of message controller
     * @param classCollection
     */
    public MessageInputController(ClassCollection classCollection) {
        this.classCollection = classCollection;
        this.presenter = new TextPresenter();
    }

    /**
     * Handle different cases for user type and user input, call send message in message controller.
     *
     * @param userType the user type of sender
     */

    public void sendMessage(String userType) {
        Scanner sc = new Scanner(System.in);

        switch (userType) {

            case "speaker": {
                presenter.printNormalText("available command: to-all-attendees, to-attendees-of-one-talk, to-one-attendee");
                String cmd = sc.nextLine().trim();
                int speakerID = classCollection.loginController.getUserId();

                if (cmd.equalsIgnoreCase("to-all-attendees")) {
                    classCollection.messagingController.handleInput("speaker", speakerID, "all-attendees", -1);
                }
                else if (cmd.equalsIgnoreCase("to-attendees-of-one-talk")) {
                    List<Integer> talks = classCollection.speakerManager.getTalksGivenBySpeaker(speakerID);
                    presenter.printNormalText("Input talk id:");
                    presenter.printNormalText("These are your talks:" + talks);
                    int talkId = Integer.parseInt(sc.nextLine().trim());
                    classCollection.messagingController.handleInput("speaker", speakerID, "attendees-of-one-talk", talkId);
                }
                else if (cmd.equalsIgnoreCase("to-one-attendee")) {
                    presenter.printNormalText("input attendee username:");
                    String attendeeName = sc.nextLine().trim();
                    classCollection.messagingController.handleInput("speaker", speakerID, "one-attendee", attendeeName);
                }
                else {
                    presenter.printErrorText("invalid command.");
                }
                break;
            }

            case "organizer": {
                presenter.printNormalText("available command: to-all-attendees, to-all-speakers, to-one-attendee, to-one-speaker");
                String cmd = sc.nextLine().trim();
                int organizerID = classCollection.loginController.getUserId();

                if (cmd.equalsIgnoreCase("to-all-attendees")) {
                    classCollection.messagingController.handleInput("organizer", organizerID, "all-attendees", -1);
                }
                else if (cmd.equalsIgnoreCase("to-all-speakers")) {
                    classCollection.messagingController.handleInput("organizer", organizerID, "all-speakers", -1);
                }
                else if (cmd.equalsIgnoreCase("to-one-attendee")) {
                    presenter.printNormalText("input attendee's username:");
                    String attendeeName = sc.nextLine().trim();
                    classCollection.messagingController.handleInput("organizer", organizerID, "one-attendee", attendeeName);
                }
                else if (cmd.equalsIgnoreCase("to-one-speaker")) {
                    presenter.printNormalText("input speaker username:");
                    String speakerName = sc.nextLine().trim();
                    classCollection.messagingController.handleInput("organizer", organizerID, "one-speaker", speakerName);
                }
                else {
                    presenter.printErrorText("invalid command.");
                }
                break;
            }

            case "attendee": {
                presenter.printNormalText("available command: to-one-speaker, to-one-attendee");
                String cmd = sc.nextLine().trim();
                int attendeeID = classCollection.loginController.getUserId();

                if (cmd.equalsIgnoreCase("to-one-attendee")) {
                    presenter.printNormalText("input attendee username:");
                    String attendeeName = sc.nextLine().trim();
                    classCollection.messagingController.handleInput("attendee", attendeeID, "one-attendee", attendeeName);
                }
                else if (cmd.equalsIgnoreCase("to-one-speaker")) {
                    presenter.printNormalText("input speaker username:");
                    String speakerName = sc.nextLine().trim();
                    classCollection.messagingController.handleInput("attendee", attendeeID, "one-speaker", speakerName);
                }
                else {
                    presenter.printErrorText("invalid command.");
                }
                break;
            }

            default:
                presenter.printErrorText("invalid user.");
                break;
        }
    }

    /**
     * Handle user input of read-message, call controller.
     *
     * @param userId the id of receiver
     * @param usertype the usertype of receiver
     */

    public void readMessage(int userId, String usertype) {
        Scanner sc = new Scanner(System.in);
        List<Message> messagesForReceiver = classCollection.messagingController.getMessage(userId, usertype);

        if(messagesForReceiver.isEmpty()) {
            presenter.printNormalText("We have no messages for you.");
        }
        else {
            List<String> sender = new ArrayList<>();
            presenter.printNormalText("These are your messages:");
            for (Message message : messagesForReceiver) {
                sender.add(message.getSenderName());
                Date time = new Date(message.getTimestamp().getTime());
                presenter.printNormalText("Context: " + message.getText() + "\n"
                        + "Sender: " + message.getSenderName() + "\n"
                        + "Time: " + time);
                presenter.printDividerLine();
            }

            presenter.printNormalText("Enter the sender username to reply. Enter cancel to return to homepage.");
            String cmd = sc.nextLine().trim();
            if (cmd.equalsIgnoreCase("cancel")) {
                return;
            }
            else if (sender.stream().anyMatch(x -> x.equals(cmd)) ) {
                String receiverName = cmd;
                classCollection.messagingController.reply(userId, usertype, receiverName);
            }
            else {
                presenter.printErrorText("invalid command.");
                return;
            }
        }

    }

    /**
     * Print sent message to screen.
     *
     * @param context the context of sent message
     * @param receiver the list of receiver names of sent message
     */

    public void showSentMessage(String context, List<String> receiver) {
        presenter.printSuccessText("Message sent.");
        presenter.printNormalText("Context: " + context + "\n" + "Receiver: " + receiver);
    }

    /**
     * Print message to screen if the input user doesn't exist.
     *
     * @param user the invalid username to be shown in message
     */

    public void showUserNotExist(Object user) {
        presenter.printErrorText("User " + (String)user + " doesn't exist.");
    }

    /**
     * call presenter to print talk not exist
     */
    public void showTalkNotExist() {
        presenter.printErrorText("You are not holding this talk.");
    }

    /**
     * handle user input
     * @return input as a string
     */
    public String handleContextInput() {
        Scanner sc = new Scanner(System.in);
        presenter.printNormalText("Enter message context: ");
        return sc.nextLine().trim();
    }

    /**
     * call presenter to print alert
     */
    public void showEmptyReceiver() {
        presenter.printErrorText("No message sent, since the user group is empty.");
    }

    /**
     * Inbox Functionality
     */

    private boolean isNumeric(String input) {
        Pattern p = Pattern.compile("[0-9]+");
        if (input != null) {
            p.matcher(input);
            Matcher m = p.matcher(input);
            return m.matches();
        }
        return false;
    }

    /**
     * read archived messages
     * @param userId user id
     * @param userType user type
     */
    public void seeArchive(int userId, String userType) {
        if(classCollection.messagingController.getArchivedMessages(userId, userType).isEmpty()) {
            presenter.printErrorText("Your Archive is empty");
        } else {
            List<Message> archive = classCollection.messagingController.getArchivedMessages(userId, userType);
            String msgList = "";
            for (Message m: archive) {
                msgList += "Message ID: " + m.getMessageID() + "\n" + "Message Text: " + m.getText() + "\n";
            }
            presenter.printNormalText("Here are your archived messages\n" + msgList);
        }
    }

    /**
     * mark a message as archive
     * @param userId user id
     * @param usertype user type
     */
    public void archiveMessage(int userId, String usertype) {
        Scanner sc = new Scanner(System.in);
        presenter.printNormalText("Enter the Message Id to archive the Message. Type Cancel to return to the homepage");
        String cmd = sc.nextLine().trim();
        if (cmd.equalsIgnoreCase("cancel")) {
            return;
        } else {
            if (isNumeric(cmd)) {
                if (classCollection.messagingController.getMessage(userId, usertype).isEmpty()) {
                    presenter.printErrorText("Error, You have no messages");
                } else if (classCollection.messageManager.getMessageByID(Integer.parseInt(cmd)) == null) {
                    presenter.printErrorText("Invalid Message ID");
                } else if (classCollection.messageManager.getMessage().stream().anyMatch(x -> x.equals(
                        classCollection.messageManager.getMessageByID(Integer.parseInt(cmd))))) {
                    if (classCollection.messageManager.isMessageArchived(Integer.parseInt(cmd))) {
                        presenter.printErrorText("That message is already archived");
                    } else {
                        int selectedID = Integer.parseInt(cmd);
                        classCollection.messagingController.archiveMessage(selectedID);
                        presenter.printSuccessText("Message Successfully archived");
                    }
                }
            }else {
                presenter.printErrorText("Invalid Message ID");
            }
        }
    }

    /**
     * read unread messages
     * @param userId user id
     * @param userType user type
     */
    public void seeUnread(int userId, String userType) {
        List<Message> copy = classCollection.messagingController.getUnreadMessages(userId, userType);
        if (copy.isEmpty()) {
            presenter.printErrorText("You do not have any new messages");
        } else {
            String msgList = "";
            for (Message m: copy) {
                msgList += "Message ID: " + m.getMessageID() + "\n" + "Message Text: " + m.getText() + "\n";
            }
            presenter.printNormalText("Here are your unread messages\n" + msgList);
        }
    }

    /**
     * mark a message as unread
     * @param userId user id
     * @param usertype user type
     */
    public void markAsUnread(int userId, String usertype) {
        Scanner sc = new Scanner(System.in);
        presenter.printNormalText("Enter the Message Id to mark the Message as Unread. Type Cancel to return to the homepage");
        String cmd = sc.nextLine().trim();
        if (cmd.equalsIgnoreCase("cancel")) {
            return;
        } else {
            if (isNumeric(cmd)) {
                if (classCollection.messagingController.getMessage(userId, usertype).isEmpty()) {
                    presenter.printErrorText("Error, You have no messages");
                } else if (classCollection.messageManager.getMessageByID(Integer.parseInt(cmd)) == null) {
                    presenter.printErrorText("Invalid Message ID");
                } else if (classCollection.messageManager.getMessage().stream().anyMatch(x -> x.equals(
                        classCollection.messageManager.getMessageByID(Integer.parseInt(cmd))))) {
                    if (classCollection.messageManager.isMessageUnread(Integer.parseInt(cmd))) {
                        presenter.printErrorText("That message is already marked as unread");
                    } else {
                        int selectedID = Integer.parseInt(cmd);
                        classCollection.messagingController.markMessageAsUnread(selectedID);
                        presenter.printErrorText("Message Successfully marked as unread");
                    }
                }
            }
            else {
                presenter.printErrorText("Invalid Message ID");
            }
        }
    }

    /**
     * read messages
     * @param userId user id
     * @param userType user type
     */
    public void seeMessage(int userId, String userType) {
        List<Message> all = classCollection.messagingController.getMessage(userId, userType);
        if (all.isEmpty()) {
            presenter.printErrorText("You have no messages");
        } else {
            String allMsg = "";
            for (Message m : all) {
                allMsg += "Message ID: " + m.getMessageID() + "\n" + "Message Text: " + m.getText() + "\n";
            }
            presenter.printNormalText("Here are all your messages\n" + allMsg);
        }

    }

    /**
     * delete a message
     * @param userId user id
     * @param usertype user type
     */
    public void deleteMessage(int userId, String usertype) {
        List<Message> all = classCollection.messagingController.getMessage(userId, usertype);
        Scanner sc = new Scanner(System.in);
        presenter.printNormalText("Please enter the Id of the message you would like to delete. " +
                "Enter Cancel to return to the homepage");
        String cmd = sc.nextLine().trim();
        if (cmd.equalsIgnoreCase("cancel")) {
            return;
        } else if(isNumeric(cmd)) {
            if (all.isEmpty()) {
                presenter.printErrorText("You have no messages to delete");
            } else {
                classCollection.messagingController.deleteMessage(Integer.parseInt(cmd));
                presenter.printSuccessText("Message Successfully Deleted");
            }
        }else {
            presenter.printErrorText("Invalid ID");
        }
    }
}
