package controller;
import entity.Attendee;
import entity.Message;
import gateway.SerializableGateway;
import gateway.UserGateway;
import userInputController.MessageInputController;
import usecase.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * the class of message controller
 */
public class MessagingController {
    private IMessageManager messageManager;
    private ITalkManager talkManager;
    private ISpeakerManager speakerManager;
    private IAttendeeManager attendeeManager;
    private MessageInputController messagePresenter;
    private ClassCollection classCollection;

    // Parameters
    private String context;

    /**
     * constructor for message controller
     * @param messageManager
     * @param talkManager
     * @param speakerManager
     * @param attendeeManager
     */
    public MessagingController(IMessageManager messageManager, ITalkManager talkManager,
                               ISpeakerManager speakerManager, IAttendeeManager attendeeManager) {
        this.messageManager = messageManager;
        this.talkManager = talkManager;
        this.speakerManager = speakerManager;
        this.attendeeManager = attendeeManager;
        this.messagePresenter = new MessageInputController(classCollection);
    }

    /**
     * Handle input passes from presenter, maintain a receiver list, and call send message.
     *
     * @param usertype user type of sender
     * @param senderId id of sender
     * @param cmd user command
     * @param additionalInfo talk id or receiver username
     */

    public void handleInput(String usertype, int senderId, String cmd, Object additionalInfo) {
        List<String> receiver = new ArrayList<>();
        UserGateway userGateway = new UserGateway();
        String senderName;

        switch(usertype) {
            case "organizer": {
                senderName = userGateway.getUsernameByIdandUsertype(senderId, "organizer");

                switch (cmd) {
                    case "all-attendees":
                        List<Attendee> allAttendees = attendeeManager.getAllAttendees();
                        for (Attendee attendee : allAttendees) {
                            int receiverId = attendee.getId();
                            String receiverName = userGateway.getUsernameByIdandUsertype(receiverId, "attendee");
                            receiver.add(receiverName);
                        }
                        break;
                    case "all-speakers":
                        List <Integer> receiverId = speakerManager.getAllSpeakers();
                        for (int id : receiverId) {
                            String receiverName = userGateway.getUsernameByIdandUsertype(id, "speaker");
                            receiver.add(receiverName);
                        }
                        break;
                    case "one-attendee":
                        if (checkExist("attendee", (String)additionalInfo)) {
                            receiver.add((String)additionalInfo);
                        }
                        else {
                            messagePresenter.showUserNotExist(additionalInfo);
                            return;
                        }
                        break;
                    case "one-speaker":
                        if (checkExist("speaker", (String)additionalInfo)) {
                            receiver.add((String)additionalInfo);
                        }
                        else {
                            messagePresenter.showUserNotExist(additionalInfo);
                            return;
                        }
                        break;
                }
                sendMessage(senderName, receiver);
                break;
            }

            case "speaker": {
                senderName = userGateway.getUsernameByIdandUsertype(senderId, "speaker");
                switch (cmd) {
                    case "all-attendees":
                        List<Integer> talks = speakerManager.getTalksGivenBySpeaker(senderId);
                        List<Integer> receiverIds = talkManager.getAttendees(talks);
                        for (int id : receiverIds) {
                            String receiverName = userGateway.getUsernameByIdandUsertype(id, "attendee");
                            if (receiver.stream().noneMatch(x -> x.equals(receiverName))) {
                                receiver.add(receiverName);
                            }
                        }
                        break;
                    case "attendees-of-one-talk":
                        List<Integer> talk = new ArrayList<>();
                        talks = speakerManager.getTalksGivenBySpeaker(senderId);
                        if (talks.stream().anyMatch(x -> x.equals((int)additionalInfo))){
                            talk.add((int)additionalInfo);
                            List<Integer> receiverId = talkManager.getAttendees(talk);
                            for (int id : receiverId) {
                                String receiverName = userGateway.getUsernameByIdandUsertype(id, "attendee");
                                receiver.add(receiverName);
                            }
                        }
                        else {
                            messagePresenter.showTalkNotExist();
                            return;
                        }
                        break;
                    case "one-attendee":
                        if (checkExist("attendee", (String)additionalInfo)) {
                            receiver.add((String)additionalInfo);
                        }
                        else {
                            messagePresenter.showUserNotExist(additionalInfo);
                            return;
                        }
                        break;
                }
                sendMessage(senderName, receiver);
                break;
            }

            case "attendee": {
                senderName = userGateway.getUsernameByIdandUsertype(senderId, "attendee");
                if (cmd.equals("one-speaker")) {
                    if (checkExist("speaker", (String)additionalInfo)) {
                        receiver.add((String)additionalInfo);
                    }
                    else {
                        messagePresenter.showUserNotExist(additionalInfo);
                        return;
                    }
                }
                else if (cmd.equals("one-attendee")) {
                    if (checkExist("attendee", (String)additionalInfo)) {
                        receiver.add((String)additionalInfo);
                    }
                    else {
                        messagePresenter.showUserNotExist(additionalInfo);
                        return;
                    }
                }
                sendMessage(senderName, receiver);
                break;
            }

            case "reply": {
                receiver.add((String)additionalInfo);
                break;
            }
        }
    }

    /**
     * Call message manager to create a new message and save the manager again
     *
     * @param sender the sender's username
     * @param receivers a list of usernames of receivers
     */

    public void sendMessage(String sender, List<String> receivers) {
        // send message
        if (receivers.isEmpty()) {
            messagePresenter.showEmptyReceiver();
        }
        else {
            context = messagePresenter.handleContextInput();
            for (String receiver : receivers) {
                //in phase two we are using id as unique identifier, so leave the ids as params
                if (receiver != null) {
                    Timestamp timestamp = Timestamp.from(Instant.now());
                    messageManager.createMessage(-1, sender, -1, receiver, context, timestamp);
                }
                if (receiver == null) {
                    messagePresenter.showUserNotExist(receiver);
                }
            }

            //saving to file
            SerializableGateway generalGateway = new SerializableGateway();
            try {
                generalGateway.saveToFile("phase2/resources/message.ser", messageManager);
            }
            catch (Exception e) {
                //Do nothing...
            }

            //show message on screen
            messagePresenter.showSentMessage(context, receivers);
        }
    }

    /**
     * Call message manager to get messages, and filter the ones of a specific receiver
     *
     * @param receiverId    the id of receiver
     * @param usertype      the usertype of receiver
     *
     * @return              messages for receiver
     */


    public List<Message> getMessage(int receiverId, String usertype) {
        UserGateway userGateway = new UserGateway();
        String username = userGateway.getUsernameByIdandUsertype(receiverId, usertype);
        List<Message> messages = messageManager.getMessage();
        List<Message> messagesForReceiver = messages.stream()
                .filter(m -> m.getReceiverName().equals(username))
                .collect(Collectors.toList());

        return messagesForReceiver;
    }

    /**
     * get archived messages
     * @param receiverId receiver id
     * @param usertype usertype
     * @return list of archived messages
     */
    public List<Message> getArchivedMessages(int receiverId, String usertype) {
        UserGateway userGateway = new UserGateway();
        String username = userGateway.getUsernameByIdandUsertype(receiverId, usertype);
        List<Message> messages = messageManager.getArchived();
        List<Message> messagesForReceiver = messages.stream()
                .filter(m -> m.getReceiverName().equals(username))
                .collect(Collectors.toList());
        return messagesForReceiver;
    }

    /**
     * get unread messages
     * @param receiverId receiver id
     * @param usertype usertype
     * @return list of unread messages
     */
    public List<Message> getUnreadMessages(int receiverId, String usertype) {
        UserGateway userGateway = new UserGateway();
        String username = userGateway.getUsernameByIdandUsertype(receiverId, usertype);
        List<Message> messages = messageManager.getUnread();
        List<Message> messagesForReceiver = messages.stream()
                .filter(m -> m.getReceiverName().equals(username))
                .collect(Collectors.toList());
        List<Message> copy = new ArrayList<>(messagesForReceiver);
        messageManager.getUnread().removeAll(messagesForReceiver);
        SerializableGateway generalGateway = new SerializableGateway();
        try {
            generalGateway.saveToFile("phase2/resources/message.ser", messageManager);
        }
        catch (Exception e) {
        }
        return copy;
    }

    /**
     * mark message as unread
     * @param messageID message id
     */
    public void markMessageAsUnread(int messageID) {
            messageManager.setMessageUnread(messageID);
            SerializableGateway generalGateway = new SerializableGateway();
            try {
                generalGateway.saveToFile("phase2/resources/message.ser", messageManager);
            }
            catch (Exception e) {
            }
        }

    /**
     * mark a message as archive
     * @param messageID message id
     */
    public void archiveMessage(int messageID) {
        messageManager.archiveMessage(messageID);
        SerializableGateway generalGateway = new SerializableGateway();
        try {
            generalGateway.saveToFile("phase2/resources/message.ser", messageManager);
        }
        catch (Exception e) {
        }
    }

    /**
     * handles the event of replying message
     *
     * @param senderId          the id of sender
     * @param senderType        the usertype of sender
     * @param receiverName      the username of receiver
     */

    public void reply(int senderId, String senderType, String receiverName) {
        UserGateway userGateway = new UserGateway();
        String senderName = userGateway.getUsernameByIdandUsertype(senderId, senderType);
        System.out.println(receiverName);
        if (receiverName.equals("group_0129")) {
            System.out.println("You can not reply to a system message.");
        }
        else {
            List<String> receiver = new ArrayList<>();
            receiver.add(receiverName);
            sendMessage(senderName, receiver);
        }
    }

    /**
     * check if the input username exists
     *
     * @param userType      the user type of receiver
     * @param input         the username of receiver
     * @return              if the user exists
     */

    private boolean checkExist(String userType, String input) {
        UserGateway userGateway = new UserGateway();
        ArrayList<String> allAttendee = new ArrayList<>();
        ArrayList<String> allSpeaker = new ArrayList<>();

        List<Attendee> allAttendees = attendeeManager.getAllAttendees();
        for (Attendee attendee : allAttendees) {
            String receiverName = attendee.getUsername();
            allAttendee.add(receiverName);
        }

        List<Integer> receiverId = speakerManager.getAllSpeakers();
        for (int id : receiverId) {
            String receiverName = userGateway.getUsernameByIdandUsertype(id, "speaker");
            allSpeaker.add(receiverName);
        }

        if (userType.equals("attendee")) {
            return allAttendee.stream().anyMatch(x -> x.equals(input));
        }

        if (userType.equals("speaker")) {
            return allSpeaker.stream().anyMatch(x -> x.equals(input));
        }

        return false;
    }

    /**
     * delete a message
     * @param messageId message id
     */
    public void deleteMessage(int messageId) {
        messageManager.removeMessage(messageId);
        SerializableGateway generalGateway = new SerializableGateway();
        try {
            generalGateway.saveToFile("phase2/resources/message.ser", messageManager);
        }
        catch (Exception e) {
        }
    }
}
