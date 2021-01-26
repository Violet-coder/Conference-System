package entity;

import java.io.Serializable;
/**
 * Request entity class.
 * */
public class Request implements Serializable {

    private int requestID;
    private String description;
    private String status;
    private String timestamp;
    private int attendeeID;
    private int talkID;

    /**
     *
     * @param requestID id of a request
     * @param description description of a request
     * @param status status of a request
     * @param timestamp timestamp of a request
     * @param attendeeID the sender id of the request
     * @param talkID the talk id that the request is about
     */
    public Request(int requestID, String description, String status, String timestamp, int attendeeID, int talkID) {
        this.requestID = requestID;
        this.description = description;
        this.status = status;
        this.timestamp = timestamp;
        this.attendeeID = attendeeID;
        this.talkID = talkID;
    }

    /**
     *
     * @return request id
     */
    public int getRequestID() {
        return requestID;
    }

    /**
     *
     * @return request description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     *
     * @return request status
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * set the request status
     * @return
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     *
     * @return attendee id
     */
    public int getAttendeeID() {
        return attendeeID;
    }

    /**
     *
     * @return talk id
     */
    public int getTalkID() {
        return talkID;
    }

    /**
     *
     * @return a string of the request detail
     */
    @Override
    public String toString() {
        return  "Request ID: " + requestID + "\n" +
                "Description: " + description + "\n" +
                "Status: " + status + "\n" +
                "Timestamp: " + timestamp + "\n" +
                "Attendee ID: " + attendeeID + "\n" +
                "Talk ID: " + talkID + "\n" + "--------\n";
    }
}

