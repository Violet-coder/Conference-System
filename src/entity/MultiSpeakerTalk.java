package entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity class for an event that has 1 or more than 1 speakers, i.e. panel discussion.
 * */
public class MultiSpeakerTalk extends Talk {

    private List<Integer> speakerIDs;

    /**
     * Constructor of MultiSpeakerTalk without specifying capacity.
     * */
    public MultiSpeakerTalk(int id, String title, int speakerID, String date, String endDate,
                            String startingTime, String endTime, int roomID, int orgId) {
        super(id, title, speakerID, date, endDate, startingTime, endTime, roomID, orgId);
        this.speakerIDs = new ArrayList<>();
        this.speakerIDs.add(speakerID);
    }

    /**
     * Constructor of MultiSpeakerTalk with capacity.
     *
     * */
    public MultiSpeakerTalk(int id, String title, int speakerID, String date, String endDate,
                            String startingTime, String endTime, int roomID, int capacity,int orgId) {
        super(id, title, speakerID, date, endDate, startingTime, endTime, roomID, capacity, orgId);
        this.speakerIDs = new ArrayList<>();
        this.speakerIDs.add(speakerID);
    }

    public List<Integer> getSpeakerIDs() {
        return new ArrayList<>(this.speakerIDs);
    }

    // TODO: WHAT TO DO WITH THIS???
    public void addSpeaker(int speakerID) {
        this.speakerIDs.add(speakerID);
    }

    @Override
    public String toString() {
        return "Panel Discussion ID: " + this.getId() + "\n" +
                "Panel Discussion Title: " + this.getTitle() + "\n" +
                "Speaker IDs: " + this.getSpeakerIDs() + "\n" +
                "Start Date: " + this.getDate() + "\n" +
                "End Date: " + this.getEndDate() + "\n" +
                "Start Time: " + this.getStartingTime() + "\n" +
                "End Time: " + this.getEndTime() + "\n" +
                "Room ID: " + this.getRoomID() + "\n" +
                "Capacity: " + this.getCapacity() + "\n";
    }
}
