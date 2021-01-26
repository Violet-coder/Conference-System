package usecase;

import entity.Organizer;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * the class of use case for organizer
 */
public class OrganizerManager implements IOrganizerManager, Serializable {

    private int nextID;
    private ArrayList<Organizer> listOfOrganizers;

    /**
     * the constructor of organizer, initialize max id and list of organizers
     */
    public OrganizerManager() {
        this.nextID = 0;
        this.listOfOrganizers = new ArrayList<>();
    }

    /**
     * add organizer to the list that this use case maintains
     * @param username the username of organizer
     * @param password the password of organizer
     * @param name the name of organizer
     * @return the id of newly created organizer
     */
    public int addOrganizer(String username, String password, String name) {
        for (Organizer organizer : this.listOfOrganizers) {
            if (organizer.getUsername().equals(username)) {
                return -1;
            }
        }
        Organizer newOrganizer = new Organizer(this.nextID, username, password, name);
        this.nextID += 1;
        this.listOfOrganizers.add(newOrganizer);
        return newOrganizer.getId();
    }

    /**
     * create formatted strings
     * @param id        ID of speaker
     * @return the formatted string of organizer details
     */
    public String printOrganizer(int id) {
        for (Organizer organizer : this.listOfOrganizers) {
            if (organizer.getId() == id) {
                return organizer.toString();
            }
        }
        return "";
    }
}