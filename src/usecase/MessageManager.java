package usecase;
import entity.Message;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Pattern;

/**
 * MessageManager is the use case class that stores all message entities, and
 * contain methods interacting with the Message entity.
 * */
public class MessageManager implements IMessageManager, Serializable, Iterable<Message>{
    //private List<ChatHistory> allChats;
    private List<Message> allMessages = new ArrayList<>();
    private List<Message> unreadMessages = new ArrayList<>();
    private List<Message> archivedMessages = new ArrayList<>();
    private int nextID;

    public MessageManager() {
        this.nextID = 0;
    }

    /**
     *
     * @param sender            the id of sender (not used now)
     * @param senderName        the username of sender
     * @param receiver          the id of receiver (not used now)
     * @param receiverName      the username of receiver
     * @param context           message context
     * @param timestamp         message timestamp
     *
     * @return                  the newly created message
     */

    public Message createMessage(int sender, String senderName, int receiver, String receiverName, String context, Timestamp timestamp) {

        Message msg = new Message(nextID,sender, senderName, receiver, receiverName, context, timestamp);
        this.nextID += 1;
        allMessages.add(msg);
        unreadMessages.add(msg);
        return msg;
    }

    /**
     * Gets number of all messages entities
     *
     * @return      number of all messages entities
     */
    public int getNumAllMessaegs() {return allMessages.size();}

    /**
     * @return all message entities
     * */
    public List<Message> getMessage() {
        return allMessages;
    }

    /**
     * Get all message rows
     * @return the message row
     */
    public List<List<String>> getAllMessageRows() {
        List<List<String>> rows = new ArrayList<>();
        for(Message message : this) {
            rows.add(Arrays.asList(String.valueOf(message.getMessageID()),message.getSenderName(),
                    message.getReceiverName(),message.getText()));
        }
        //return res;
        return rows;
    }

    /**
     * @param messageID
     * @return the message entity with specified id
     * */
    public Message getMessageByID(int messageID) {
        if (isMessageExisting(messageID)) {
            for (Message m : this) {
                if (m.getMessageID() == messageID) {
                    return m;
                }
            }
        }
        return null;
    }


    /**
     * @return true for message archived; otherwise false
     * */
    public boolean isMessageArchived(int messageID) {
        for(Message m : archivedMessages) {
            if (m.getMessageID() == messageID) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return true for message read; otherwise false
     */
    public boolean isMessageUnread(int messageID) {
        for(Message m : unreadMessages) {
            if(m.getMessageID() == messageID) {
                return true;
            }
        }
        return false;
    }

    /**
     * Archive a message with id.
     * */
    public void archiveMessage(int messageID) {
        archivedMessages.add(getMessageByID(messageID));
    }

    /**
     * @return all archived messages.
     * */
    public List<Message> getArchived() {
        return archivedMessages;
    }

    /**
     * @return unread messages
     * */
    public List<Message> getUnread() {
        return unreadMessages;
    }

    /**
     * Set message as unread.
     * @param  messageID
     * */
    public void setMessageUnread(int messageID) {
        unreadMessages.add(getMessageByID(messageID));
    }

    /**
     * Delete message with id from allMessages, archivedMessages and unreadMessages.
     * @param messageID
     * @return the deleted message id.
     * */
    public int deleteMessageByID(int messageID) {
        Message obj = getMessageByID(messageID);
        if(getMessageByID(messageID) == null) {
            return -1;
        } else {
            allMessages.remove(obj);
            unreadMessages.remove(obj);
            archivedMessages.remove(obj);
            return obj.getMessageID();
        }

    }

    /**
     * Delete a list of messages with given id list.
     * @param messagesIDs list of message ids
     * @return number of deleted messages
     * */
    public int deleteMessagesByIDs(List<Integer> messagesIDs) {
        int num = 0;
        for(int id : messagesIDs) {
            if(deleteMessageByID(id) != -1) {
                num += 1;
            }
        }
        return num;
    }
    /**
     * Remove a message with id.
     * */
    public void removeMessage(int messageID) {
        Message obj = getMessageByID(messageID);
        if(getMessageByID(messageID) == null) {
            return;
        } else {
            allMessages.remove(obj);
            unreadMessages.remove(obj);
            archivedMessages.remove(obj);
        }
    }

    /**
     * Print a message with id.
     * @param messageID message id
     * */
    public String printMessageByID(int messageID) {
        String res = "";
        for(Message message : this) {
            if(message.getMessageID() == messageID) {
                res += message.toString();
            }
        }
        return res;

    }
    /**
     * @param messageID message id
     * @return true for message existing; otherwise false
     * */
    public boolean isMessageExisting(int messageID) {
        for(Message message : this) {
            if(message.getMessageID() == messageID){
                return true;
            }
        }
        return false;
    }

    /**
     * Get all the messages received or sent by a user.
     * @param username username
     * @return list of messages' infomation
     * */
    public List<List<String>> getMessagesByUsername(String username) {
        List<List<String>> rows = new ArrayList<>();
        for(Message msg : this) {
            if(msg.getSenderName().equals(username) || msg.getReceiverName().equals(username)) {
                rows.add(Arrays.asList(String.valueOf(msg.getMessageID()),msg.getSenderName(),
                        msg.getReceiverName(),msg.getText()));
            }
        }
        return rows;
    }

    /**
     * Filter the messages by keyword.
     * @param keyword keyword
     * @return a map containing all the filtered message info
     * */
    public Map<Integer, List<String>> getMessagesByKeywords(String keyword) {
        Map<Integer, List<String>> res = new HashMap<>();
        List<String> keywordList = Arrays.asList(keyword.split(" "));

        StringBuilder regex = new StringBuilder();
        for(String word : keywordList) {
            regex.append("(?=.*").append(word).append(")");
        }
        Pattern pattern = Pattern.compile(regex.toString());

        for(Message message: this) {
            String messageStr = message.getText();
            if(pattern.matcher(messageStr).find()) {
                List<String> messageList = Arrays.asList(String.valueOf(message.getMessageID()),message.getSenderName(),
                        message.getReceiverName(),message.getText());
                res.put(message.getMessageID(), messageList);
            }
        }

        return res;
    }

    /**
     * Implement Iterator for MessageManager, which iterates over the messages
     * stored in it.
     * */
    public Iterator<Message> iterator() {
        return new MessageManagerIterator();
    }

    private class MessageManagerIterator implements Iterator<Message> {
        private int current = 0;

        public  boolean hasNext() {
            return current < allMessages.size();
        }

        public Message next() {
            Message res;

            try {
                res = allMessages.get(current);
            } catch (IndexOutOfBoundsException e) {
                throw new NoSuchElementException();
            }
            current += 1;
            return res;
        }

        @Override
        public void remove() {
            allMessages.remove( current - 1);
        }



    }



    }


