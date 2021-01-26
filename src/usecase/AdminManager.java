package usecase;

import entity.Admin;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * this is the use case class is for admin entity
 */
public class AdminManager implements IAdminManager, Serializable {

    private int nextID;
    private ArrayList<Admin> listOfAdmins;

    /**
     * the constructor of admin manager, which sets initial id to 0, and creates an empty admin list
     */
    public AdminManager() {
        this.nextID = 0;
        this.listOfAdmins = new ArrayList<>();
    }

    /**
     * add a new admin entity to admin list
     * @param username the username of admin user
     * @param password the password of admin user
     * @param name the real name of admin user
     * @return the id of newly added amin
     */
    @Override
    public int addAdmin(String username, String password, String name) {
        for (Admin admin : this.listOfAdmins) {
            if (admin.getUsername().equals(username)) {
                return -1;
            }
        }
        Admin newAdmin = new Admin(this.nextID, username, password, name);
        this.nextID += 1;
        this.listOfAdmins.add(newAdmin);
        return newAdmin.getId();
    }

    /**
     * use toString() method to create output string for printing details of admin user
     * @param id        ID of speaker
     * @return the output string
     */
    @Override
    public String printAdmin(int id) {
        for (Admin admin : this.listOfAdmins) {
            if (admin.getId() == id) {
                return admin.toString();
            }
        }
        return "";
    }
}