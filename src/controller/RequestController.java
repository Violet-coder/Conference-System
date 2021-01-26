package controller;

import entity.Request;
import gateway.SerializableGateway;
import usecase.RequestManager;

import java.io.IOException;
import java.rmi.server.ExportException;
import java.util.Map;
import java.util.Scanner;

/**
 * the class of request controller communicates with input controller and use case
 */
public class RequestController {
    private RequestManager requestManager;

    /**
     *
     * @param requestManager
     */
    public RequestController(RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    /**
     * it calls manager to create a request
     * @param text user input
     * @param userId user id
     * @param talkId talk id
     * @return
     */
    public String createRequest(String text, int userId, int talkId) {
        int reqId = requestManager.addRequest(text, userId, talkId);
        try {
            saveManager();
            return printOneRequest(reqId);
        } catch (Exception e) {
            return ("A mistake occurs in system. Request not saved.");
        }
    }

    /**
     * It gets all request
     * @return all requests as string
     */
    public String getRequest() {
        return printAllRequests();
    }

    /**
     * it passes all requests to presenter
     * @return all requests
     */
    public String printAllRequests() {
        Map<Integer, Request> requests = requestManager.getAllRequests();
        String requestInfo = "";
        if (requests.isEmpty()) {
            return ("There are no requests for you.");
        }
        for (Request r : requests.values()) {
            requestInfo += r.toString();
        }
        return requestInfo;
    }

    /**
     * mark request basing on user input
     * @param id request id
     * @param input user input. 1 for addressed, and 2 for rejected
     * @return the updated request
     */
    public String markRequest(int id, int input) {
        String status;
        if (input==1) {
            status = "Addressed";
        } else if (input==2) {
            status = "Rejected";
        } else {
            status = "Pending";
        }
        requestManager.tagRequest(id, status);
        try {
            saveManager();
            return printOneRequest(id);
        } catch (Exception e) {
            return ("A mistake occurs in system. Request not saved.");
        }
    }

    /**
     * pass one request to presenter
     * @param id request id
     * @return the needed request
     */
    public String printOneRequest(int id) {
        Map<Integer, Request> requests = requestManager.getAllRequests();
        return (requests.get(id).toString());
    }

    /**
     * allow users to review all requests send by him
     * @param id userid
     * @return all requests of this user
     */
    public String reviewRequest(int id) {
        return printRequestsByUser(id);
    }

    /**
     * allow users to review all requests send by him
     * @param id userid
     * @return all requests of this user
     */
    public String printRequestsByUser(int id) {
        Map<Integer, Request> requests = requestManager.getAllRequests();
        String requestInfo = "";
        if (requests.isEmpty()) {
            return ("You haven't made any requests.");
        }
        for (Request r : requests.values()) {
            if (r.getAttendeeID() == id) {
                requestInfo += r.toString();
            }
        }
        return requestInfo;
    }

    /**
     * check if a request exists
     * @param id input request id
     * @return boolean value for exist or not
     */
    public boolean checkExist(int id) {
        return requestManager.checkExist(id);
    }

    /**
     * save the manager everytime there is an update
     * @throws IOException
     */
    private void saveManager() throws IOException {
        //saving to file
        SerializableGateway generalGateway = new SerializableGateway();
        generalGateway.saveToFile("phase2/resources/request.ser", requestManager);
    }
}