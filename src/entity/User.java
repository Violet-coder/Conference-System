package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * Abstract class User stores only the username, name, id and password.
 * */
public abstract class User implements Serializable {
    private String username;
    private String password;
    private String name;
    private int id;
    // Id's of all users this user can message
    private List<Integer> contacts;

    public User(int id, String username, String password, String name) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.contacts = new ArrayList<>();
    }

    public void addContact(int userID) {
        this.contacts.add(userID);
    }

    public String getUsername() {
        return this.username;
    }

    public String getName() {return this.name;}

    public int getId() { return this.id; }

}
