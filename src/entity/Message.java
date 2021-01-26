package entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Message entity class contains the information of the sender, receiver and content of message.
 * */
public class Message implements Serializable {
    private int messageID;
    private int senderID;
    private int receiverID;
    private String senderName;
    private String receiverName;
    private String text;
    private Timestamp timestamp;

    /**
     * Message constructor requires the information of sender, receiver and content of message.
     * */
    public Message(int messageID, int senderID, String senderName, int receiverID, String receiverName, String text, Timestamp timestamp) {
        this.messageID = messageID;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.text = text;
        this.timestamp = timestamp;
    }

    /**
     * get message id
     * @return message id
     */
    public int getMessageID() {return this.messageID;}

    /**
     * get sender name
     * @return sender name
     */
    public String getSenderName() {return this.senderName; }

    /**
     * get receiver name
     * @return receiver name
     */
    public String getReceiverName() {return this.receiverName; }

    /**
     * get sender id
     * @return sender id
     */
    public int getSenderID() {
        return this.senderID;
    }

    /**
     * get receiver id
     * @return receiver id
     */
    public int getReceiverID() {
        return this.receiverID;
    }

    /**
     * get message context
     * @return message context
     */
    public String getText() {
        return this.text;
    }

    /**
     * get timestamp
     * @return timestamp
     */
    public Timestamp getTimestamp() {
        return this.timestamp;
    }

    /**
     * print message
     * @return string of message details
     */
    public String toString(){
        return  "Message ID: " + messageID + "\n" +
                "From Sender: " + this.senderName +"\n" +
                "To Receiver: " + this.receiverName + "\n" +
                "Message Content: " + this.text;
    }
}
