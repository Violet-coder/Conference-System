package entity;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class ChatHistory implements Serializable {
    // did not include an id of the chat as I was not sure how to format the it. For the time being,
    // each "groupchat" is distinguished from eachother by comparing the memberslists since there can be no two
    // groupchats with the same members.
    private List<Integer> members;
    private List<Message> messageList;
    private int chatId;

    public ChatHistory(int id, List<Integer> members, List<Message> messageList) {
        Collections.sort(members);
        this.members = members;
        this.messageList = messageList;
        this.chatId = id;
    }

    public void recordMessage(Message message) {
        this.messageList.add(message);
    }

    public List<Integer> getMembers() {return members;}

    //returns the full list of the messages in this specific chat
    public List<Message> getMessageHistory() {return messageList;}

    public int getChatId() {return chatId;}
}

