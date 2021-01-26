package entity;

import java.io.Serializable;
/**
 * Entity class for Admin user which extends the abstract class User.
 * */
public class Admin extends User implements Serializable {
    /**
     * Constructor of Admin requires id, username, password and name.
     * */
    public Admin(int id, String username, String password, String name) {
        super(id, username, password, name);
    }

    @Override
    public String toString() {
        return "Name: " + this.getName() + "\n" +
                "ID: " + this.getId() + "\n" +
                "Username: " + this.getUsername() + "\n";
    }
}
