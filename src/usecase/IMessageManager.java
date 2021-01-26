package usecase;

import entity.Message;

import java.sql.Timestamp;
import java.util.List;

public interface IMessageManager {

    /**
     *
     * @param senderId        sender id
     * @param receiverId      receiver id
     * @param senderName      the username of sender
     * @param receiverName    the username of receiver
     * @param context       message context
     * @param timestamp     message timestamp
     *
     * @return              the message id created
     */
    Message createMessage(int senderId, String senderName, int receiverId, String receiverName,
                          String context, Timestamp timestamp);


    /**
     *
     * @return              the message created
     */
    List<Message> getMessage();

    /**
     *
     * @return              list of unread messages
     */
    List<Message> getUnread();

    /**
     *
     * @param messageID     ID of the message
     */
    void removeMessage(int messageID);

    /**
     *
     * @param messageID     ID of the message
     */
    void archiveMessage(int messageID);

    /**
     *
     * @param messageID     ID of the message
     */
    void setMessageUnread(int messageID);

    /**
     *
     * @return              List of archived messages
     */
    List<Message> getArchived();

    /**
     * Create rows of messages containing the information to build a table.
     * Each item in the list contains the following information in sequence:
     * message id, sender username, receiver username, message content.
     * */
    List<List<String>> getAllMessageRows();



}