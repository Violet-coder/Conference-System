package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity class for Attendee user which extends the abstract class User.
 * */
public class Attendee extends User implements Serializable {
    private ArrayList<Integer> talksToListen;
    /**
     * Constructor of Attendee.
     * */
    public Attendee(int id, String username, String password, String name) {
        super(id, username, password, name);
        this.talksToListen = new ArrayList<>();
    }

    public void addTalkToListen(int talkID) {
        this.talksToListen.add(talkID);
    }

    public void removeTalkToListen(int takID) {
        this.talksToListen.remove(new Integer(takID));
    }
    /**
     * Get a list of talk ids that the attendee user listens to.
     * */
    public ArrayList<Integer> getTalksToListen() {
        ArrayList<Integer> result = new ArrayList<Integer>();
        for (Integer num : this.talksToListen) {
            result.add(num);
        }
        return result;
    }

    @Override
    public String toString() {
        return "Name: " + this.getName() + "\n" +
                "ID: " + this.getId() + "\n" +
                "Username: " + this.getUsername() + "\n" +
                "Talks to listen: " + this.getTalksToListen() + "\n";
    }

}

