package usecase;

public interface IOrganizerManager {

    /**
     * OrganizerManager should have a collection of all organizers
     * add a new organizer to the collection of organizers
     * @return the id of the added organizer
     * if unable to add organizer (due to duplicate username, since username has to be unique) return -1
     * */
    int addOrganizer(String username, String password, String name);

    /**
     * Print the info of a organizer (using Organizer class's toString() method).
     * @param id        ID of speaker
     */
    String printOrganizer(int id);
}