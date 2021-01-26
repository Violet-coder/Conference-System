package controller;

import gateway.SerializableGateway;
import gateway.htmlGateway;
import usecase.IRoomManager;
import usecase.ISpeakerManager;
import usecase.ITalkManager;
import usecase.IAttendeeManager;

import java.io.IOException;
import java.util.*;


public class SignUpController {
    private ITalkManager talkManager;
    private IAttendeeManager attendeeManager;
    private IRoomManager roomManager;
    private ISpeakerManager speakerManager;
    private SerializableGateway generalGateway;

    /**
     * Constructor of the SignUpController: initialize all the controllers and
     * use cases it interacts with.
     * */
    public SignUpController(ITalkManager talkManager, IAttendeeManager attendeeManager, IRoomManager roomManager, ISpeakerManager speakerManager, SerializableGateway generalGateway) {
        this.talkManager = talkManager;
        this.attendeeManager = attendeeManager;
        this.roomManager = roomManager;
        this.generalGateway = generalGateway;
        this.speakerManager = speakerManager;
    }

    /**
     * add html strings
     * @param events events that need to be downloaded
     */
    public void downloadEvents(List<Integer> events) {
        htmlGateway gateway = new htmlGateway();
        StringBuilder stringHtml = new StringBuilder();
        stringHtml.append("<html><head>");
        stringHtml.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=GBK\">");
        stringHtml.append("<title>Event Schedule</title>");
        stringHtml.append("<style>");
        stringHtml.append("tr:nth-child(even) {background-color: #eee; text-align:center;}");
        stringHtml.append("tr:nth-child(odd) {background-color: #fff; text-align:center;}");
        stringHtml.append("</style>");
        stringHtml.append("</head>");
        stringHtml.append("<body style=\"text-align:center;\">");
        stringHtml.append("<h1>Event Schedule</h1> <hr>");
        stringHtml.append("<table style=\"width:800px; margin-left:auto; margin-right:auto\">");
        stringHtml.append("<tr>");
        stringHtml.append("<th>Title</th>");
        stringHtml.append("<th>Speaker</th>");
        stringHtml.append("<th>Date</th>");
        stringHtml.append("<th>Start Time</th>");
        stringHtml.append("<th>End Time</th>");
        stringHtml.append("<th>Room</th>");
        stringHtml.append("</tr>");
        for (int id: events) {
            stringHtml.append("<tr>");
            stringHtml.append("<td>" + talkManager.getTitleByTalkID(id) + "</td>");
            int speakerId = talkManager.getSpeakerIDByTalkID(id);
            stringHtml.append("<td>" + speakerManager.getSpeakerNameByID(speakerId) + "</td>");
            stringHtml.append("<td>" + talkManager.getDateByTalkID(id) + "</td>");
            stringHtml.append("<td>" + talkManager.getStartingTimeByTalkID(id) + "</td>");
            stringHtml.append("<td>" + talkManager.getEndTimeByTalkID(id) + "</td>");
            int roomId = talkManager.getRoomIDByTalkID(id);
            stringHtml.append("<td>" + roomManager.getRoomNameByID(roomId) + "</td>");
            stringHtml.append("</tr>");
        }
        stringHtml.append("</body></html>");
        gateway.downloadHTML(stringHtml);
    }

    /**
     *
     * @param filter the filter
     * @param keyword keyword
     * @return event id that matches the filter and keyword
     */
    public List<Integer> filterEvents(String filter, String keyword) {
        switch (filter) {
            case "date":
                return talkManager.getEventsByDate(keyword);
            case "time":
                String startTime = keyword.substring(0, 5);
                String endTime = keyword.substring(5, 10);
                return talkManager.getEventsByTime(startTime, endTime);
            case "title":
                return talkManager.getEventsByTitle(keyword);
            default:
                //this should never be called
                return new ArrayList<>();
        }
    }


    /**
     * Given user id and talk id, sign up the user for a particular talk.
     * Call the use case TalkManager and AttendeeManager to add the talk id or
     * attendee id to the corresponding entity Attendee and Talk. Call the gateway
     * method saveToFile to write the changes to local files.
     *
     * @param userID        ID of user
     * @param talkID        ID of talk to sign up for
     * */
    public boolean singUpForTalk(int userID,int talkID) {
        // update info in managers which have a collection of talks or attendees
        boolean addAttendeeToATalk = talkManager.addAttendeeToATalk(userID, talkID);
        boolean signUpAttendeeForATalk = attendeeManager.signUpAttendeeForATalk(userID, talkID);

        // update the information to local file
        String serializableTalkInfo = "phase2/resources/talk.ser";
        String serializableAttendeeInfo = "phase2/resources/attendee.ser";
        try{
        generalGateway.saveToFile(serializableTalkInfo, talkManager);
        generalGateway.saveToFile(serializableAttendeeInfo, attendeeManager);
        } catch (IOException e){
            // do nothing
        }
        // verify that the attendee has signed up successfully
        if(addAttendeeToATalk && signUpAttendeeForATalk){
            return true;
        } else {
            return false;
        }

    }

    /**
     * Given user id and talk id, cancel a particular talk for a user.
     * Call the use case TalkManager and AttendeeManager to remove the talk id or
     * attendee id to the corresponding entity Attendee and Talk. Call the gateway
     * method saveToFile to write the changes to local files.
     *
     * @param userID        ID of user
     * @param talkID        ID of talk to cancel
     * */
    public boolean cancelTalk(int userID, int talkID) {

        boolean removeAttendeeSuccess = talkManager.removeAttendeeFromATalk(userID, talkID);
        boolean removeTalkSuccess = attendeeManager.cancelATalkForAttendee(userID, talkID);
        // update the information to local file
        String serializableTalkInfo = "phase2/resources/talk.ser";
        String serializableAttendeeInfo = "phase2/resources/attendee.ser";

        try{
        generalGateway.saveToFile(serializableTalkInfo, talkManager);
        generalGateway.saveToFile(serializableAttendeeInfo, attendeeManager);
        } catch (IOException e) {
            //do nothing
        }
        return removeAttendeeSuccess && removeTalkSuccess;
    }



    /**
     * Check whether the talk with specified id exists and has available space.
     * @param talkId        ID of talk
     * @return              the boolean result for whether is available for signing up.
     * */
    public boolean checkAvailableSpace(int talkId) {
        int roomID = talkManager.getRoomIDByTalkID(talkId);
        // compare the number of the talk attendees with the room capacity
        if(roomID != -1) {
            int capacity = talkManager.getEventCapacityByTalkID(talkId);
            int numAttendees = talkManager.getNumAttendeesByTalkID(talkId);
            return (capacity > numAttendees);
        } else {
            return false;
        }

    }

    /**
     * Given a list talk ids, sort the talk ids by the starting time of the talk,
     * from the latest to the oldest.
     *
     * @param talkIDs       A list of talk ids
     * */
    public List<Integer> sortTalkIDByTime(List<Integer> talkIDs) {
        List<List<String>> dateTimes = talkManager.getDateTimesOfTalks(talkIDs);

        // get a list of starting times for the talks
        List<Long> startingDateTimes = new ArrayList<>();

        for(List<String> dateTime : dateTimes){
            startingDateTimes.add(Long.parseLong(dateTime.get(0)));
        }

        //assert (talkIDs.size() == startingDateTimes.size());

        // generate a map for talk id and its start time
        Map<Integer, Long> talkIDDateMap= new HashMap<>();
        for(int i = 0; i < talkIDs.size(); i++){
            talkIDDateMap.put(talkIDs.get(i), startingDateTimes.get(i));
        }
        // sort the map of talk id and start time by time values
        Map<Integer, Long> sortedTalkMap = sortMapByValues(talkIDDateMap);
        List<Integer> sortedTalkIDs = new ArrayList<>(sortedTalkMap.keySet());


        return sortedTalkIDs;
    }

    private Map<Integer, Long> sortMapByValues(Map<Integer, Long> map){
        List<Map.Entry<Integer, Long>> list = new LinkedList<Map.Entry<Integer, Long>>(map.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<Integer, Long>>() {
            public int compare(Map.Entry<Integer, Long> o1,
                               Map.Entry<Integer, Long> o2)
            {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<Integer, Long> sortedMap = new LinkedHashMap<Integer, Long>();
        for (Map.Entry<Integer, Long> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    private void printMap(Map<String, Integer> map)
    {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
        }
    }








}
