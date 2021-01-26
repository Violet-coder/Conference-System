package entity;
/**
 * Entity class for event 1 speaker.
 * */
public class Talk extends NullSpeakerTalk {
    private int speakerID;

    /**
     * constructor without capacity
     * */
    public Talk(int id, String title, int speakerID, String date, String endDate,
                           String startingTime, String endTime, int roomID, int OrgId) {
        super(id, title, date, endDate, startingTime, endTime, roomID, OrgId);
        this.speakerID = speakerID;
    }

    /**
     * constructor with capacity
     * */
    public Talk(int id, String title, int speakerID, String date, String endDate,
                String startingTime, String endTime, int roomID, int capacity, int OrgId) {
        super(id, title, date, endDate, startingTime, endTime, roomID, capacity, OrgId);
        this.speakerID = speakerID;
    }

    public int getSpeakerID() {
        return this.speakerID;
    }

    public void setSpeakerID(int speakerID) {
        this.speakerID = speakerID;
    }

    @Override
    public String toString() {
        return "Talk ID: " + this.getId() + "\n" +
                "Talk Title: " + this.getTitle() + "\n" +
                "Speaker ID: " + this.getSpeakerID() + "\n" +
                "Start Date: " + this.getDate() + "\n" +
                "End Date: " + this.getEndDate() + "\n" +
                "Start Time: " + this.getStartingTime() + "\n" +
                "End Time: " + this.getEndTime() + "\n" +
                "Room ID: " + this.getRoomID() + "\n" +
                "Capacity: " + this.getCapacity() + "\n";
    }
}
