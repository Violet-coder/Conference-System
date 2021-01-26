package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * Entity class for an event without speaker, i.e. party.
 * */
public class NullSpeakerTalk implements Serializable {

    private int id;
    private String title;
    // Starting date. Format: "YYYY-MM-DD"
    private String date;
    // End date. Format: "YYYY-MM-DD"
    private String endDate;
    // Format: "hh:mm"
    private String startingTime;
    // Format: "hh:mm"
    private String endTime;
    private int roomID;
    private int capacity;
    private List<Integer> attendees;
    private int orgId;

    /**
     * constructor without capacity: default to 10
     * */
    public NullSpeakerTalk(int id, String title, String date, String endDate,
                String startingTime, String endTime, int roomID, int orgId) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.endDate = endDate;
        this.startingTime = startingTime;
        this.endTime = endTime;
        this.roomID = roomID;
        this.capacity = 10;
        this.attendees = new ArrayList<>();
        this.orgId = orgId;
    }

    /**
     * constructor with capacity
     * */
    public NullSpeakerTalk(int id, String title, String date, String endDate,
                           String startingTime, String endTime, int roomID, int capacity, int orgId) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.endDate = endDate;
        this.startingTime = startingTime;
        this.endTime = endTime;
        this.roomID = roomID;
        this.capacity = capacity;
        this.attendees = new ArrayList<>();
        this.orgId = orgId;
    }

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartingTime() {
        return this.startingTime;
    }

    public void setStartingTime(String startingTime) {
        this.startingTime = startingTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getRoomID() {
        return this.roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<Integer> getAttendees() {
        return this.attendees;
    }

    public void addAttendee(int attendeeID) {
        this.attendees.add(attendeeID);
    }

    /**
     * Get a list of string containing the datetime of an event.
     * */
    public List<String> getDateTimeStr(){
        List<String> dateTimeStrList = new ArrayList<>();
        String startDateTime = this.getDate().replaceAll("[^0-9A-Za-z]","") +
                this.getStartingTime().replaceAll("[^0-9A-Za-z]","");
        String endDateTime = this.getEndDate().replaceAll("[^0-9A-Za-z]","") +
                this.getEndTime().replaceAll("[^0-9A-Za-z]","");
        dateTimeStrList.add(startDateTime);
        dateTimeStrList.add(endDateTime);

        return dateTimeStrList;
    }

    public String toString(){
        return "Party ID: " + this.id + "\n" +
                "Party Title: " + title + "\n" +
                "Start Date: " + date + "\n" +
                "End Date: " + endDate + "\n" +
                "Start Time: " + startingTime + "\n" +
                "End Time: " + endTime + "\n" +
                "Room ID: " + roomID + "\n" +
                "Party Capacity: " + capacity + "\n";
    }

    public Integer getOrgId(){return this.orgId;}
}
