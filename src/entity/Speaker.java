package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * Speaker entity class
 * */
public class Speaker extends User implements Serializable {
    private List<Integer> talksToGive;
    private List<Integer> talksToListen;
    /**
     * constructor of Speaker entity class
     * */
    public Speaker(int id, String username, String password, String name) {
        super(id, username, password, name);
        this.talksToGive = new ArrayList<>();
        this.talksToListen = new ArrayList<>();
    }

    public void addTalkToGive(int talkID) {
        this.talksToGive.add(talkID);
    }

    public void addTalkToListen(int talkID) {
        this.talksToListen.add(talkID);
    }

    @Override
    public String toString() {
        return "Name: " + this.getName() + "\n" +
                "ID: " + this.getId() + "\n" +
                "Username: " + this.getUsername() + "\n" +
                "Talks to give: " + this.getTalksToGive() + "\n";
    }

    public List<Integer> getTalksToGive() {
        return this.talksToGive;
    }
}
