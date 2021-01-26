package usecase;

import entity.Attendee;
import entity.Speaker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * AttendeeManager contains all the attendee entities and methods interacting with attendees.
 *
 * */
public class AttendeeManager implements IAttendeeManager, Serializable {


    private ArrayList<Attendee> listOfAttendees;
    private int nextID;


    public AttendeeManager() {
        this.listOfAttendees = new ArrayList<>();
        this.nextID = 0;
    }

    public List<Attendee> getAllAttendees() {
        ArrayList<Attendee> res = new ArrayList<>();
        for (Attendee attendee : this.listOfAttendees) {
            res.add(attendee);
        }
        return res;
    }

    public boolean signUpAttendeeForATalk(int userID, int talkID) {
        for (Attendee attendee : this.listOfAttendees) {
            // Loops through the list of attendees and finds the corresponding Attendee with the given userID,
            // adds the talk to the Attendee's talksToListenTo list if the Attendee has not already signed up for it.
            if ((attendee.getId() == userID) && !(attendee.getTalksToListen().contains(talkID))) {
                attendee.addTalkToListen(talkID);
                return true;
            }
        }

        return false;
    }

    public String printAttendee(int id) {
        for (Attendee attendee: this.listOfAttendees) {
            if (attendee.getId() == id) {
                return attendee.toString();
            }
        }
        return "";
    }

    @Override
    public boolean cancelATalkForAttendee(int userID, int talkID) {
        for (Attendee attendee : this.listOfAttendees) {
            if ((attendee.getId() == userID) && attendee.getTalksToListen().contains(talkID)) {
                attendee.removeTalkToListen(talkID);
                return true;
            }
        }
        return false;
    }

    public int addAttendee(String username, String password, String name){
        for (Attendee attendee : this.listOfAttendees) {
            if (attendee.getUsername().equals(username)) {
                return -1;
            }
        }
        Attendee newAttendee = new Attendee(this.nextID, username, password, name);
        this.nextID += 1;
        this.listOfAttendees.add(newAttendee);
        return newAttendee.getId();
    }

    @Override
    public ArrayList<Integer> getSignedUpTalks(int attendeeID) {
        for (Attendee attendee : this.listOfAttendees) {
            if (attendee.getId() == attendeeID) {
                return attendee.getTalksToListen();
            }
        }
        ArrayList<Integer> result = new ArrayList<>();
        return result;
        // Returns empty arraylist if attendee does not exist
    }



}
