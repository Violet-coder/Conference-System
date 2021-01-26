package usecase;


public interface IAdminManager {

    // AdminManager should have a collection of all Admins
    // add a new admin to the collection of admins
    // return the id of the added admin
    // if unable to add admin (due to duplicate username, since username has to be unique) return -1
    int addAdmin(String username, String password, String name);

    /**
     * Print the info of a admin (using admin class's toString() method).
     * @param id        ID of speaker
     */
    String printAdmin(int id);
}