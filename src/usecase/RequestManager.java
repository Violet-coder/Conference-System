package usecase;

import entity.Request;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * request manager is the use case for request entity, which handles all methods regarding the requests
 * it maintains a map of all requests
 */
public class RequestManager implements Serializable {

    // Key of map is request id, value of map is request object
    private Map<Integer, Request> requests;
    private int nextID;

    public RequestManager() {
        this.requests = new HashMap<>();
        this.nextID = 0;
    }

    // Get description from user input in RequestPresenter
    // AttendeeID is stored in LoginController.userId
    // Get talkID from user input in RequestPresenter
    public int addRequest(String description, int attendeeID, int talkID) {
        // Default status is pending
        Request r = new Request(nextID, description, "Pending", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), attendeeID, talkID);
        requests.put(nextID, r);
        nextID++;
        return nextID-1;
    }

    // Tag a request with <requestID> as <status>
    // Get requestID from user input in RequestPresenter
    // Get status from user input in RequestPresenter
    public void tagRequest(int requestID, String status) {
        requests.get(requestID).setStatus(status);
    }

    /**
     * the method gets all requests as a map
     * @return the request map
     */
    public Map<Integer, Request> getAllRequests() {
        return requests;
    }

    /**
     *
     * @param id the id of a request
     * @return if the request exist or not
     */
    public boolean checkExist(int id) {
        for (Request r : requests.values()) {
            if (r.getRequestID() == id) {
                return true;
            }
        }
        return false;
    }
}
