package usecase;


import entity.Attendee;

import java.util.List;
import java.util.ArrayList;

public interface IAttendeeManager {
    /**
     * @return         The list of all attendee ids
     */
    List<Attendee> getAllAttendees();

    /**
     * In the collection of all attendees, add a talk to the list talksToListen for the specified attendee.
     * If the attendee id doesn't exist or the attendee has signed up for the talk, the attendee cannot sign up and return false.
     * Return true if the attendee signs up successfully.
     * @param userID        ID of user
     * @param talkID        ID of talk
     * @return              true for signing up the user successfully; otherwise false
     * */
    boolean signUpAttendeeForATalk(int userID, int talkID);

    /**
     * In the collection of all attendees, remove a talk from the list talksToListen for the specified attendee.
     * If the attendee id doesn't exist or the attendee hasn't signed up for the talk, the attendee cannot cancel the talk and return false.
     * Return true if the attendee cancels the talk successfully.
     * */
    boolean cancelATalkForAttendee(int userID, int talkID);

    /**
     * Print the info of a attendee (using attendee class's toString() method).
     * @param id        ID of attendee
     */
    String printAttendee(int id);

    /**
     * Create a new attendee user with specified username, password and name and
     * add the attendee to the collection of attendees in AttendeeManager.
     * Return -1 if the username duplicates.
     * @param username       username of the attendee
     * @param password       password of the attendee
     * @param name           name of the attendee
     * @return               return the attendee ID if add the attendee successfully;
     *                       otherwise return -1 if the username already exists
     * */
    int addAttendee(String username, String password, String name);

    /**
     * Return a list of talk IDs that the specified attendee has signed up.
     * @param attendeeID    ID of the attendee user
     * @return              the list of talk IDs that the attendee has signed up
     * */
    ArrayList<Integer> getSignedUpTalks(int attendeeID);


}