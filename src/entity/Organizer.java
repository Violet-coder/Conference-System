package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * Organizer user entity class.
 * */
public class Organizer extends User implements Serializable {

    private ArrayList<Integer> talksToListen;

    public Organizer(int id, String username, String password, String name) {
        super(id, username, password, name);
        this.talksToListen = new ArrayList<>();
    }

    public void addTalkToListen(int talkID) {
        this.talksToListen.add(talkID);
    }

    @Override
    public String toString() {
        return "Name: " + this.getName() + "\n" +
                "ID: " + this.getId() + "\n" +
                "Username: " + this.getUsername() + "\n";
    }
}
