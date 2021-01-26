package usecase;

import entity.MultiSpeakerTalk;
import entity.NullSpeakerTalk;
import entity.Talk;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Use case for all types of event entities (NullSpeakerTalk, Talk and MultiSpeakerTalk).
 * Contains a collection of events and methods for adding, modifying, reading,  deleting and manipulating events.
 */
public class TalkManager implements ITalkManager, Serializable {
    // The commented code was for testing
    private final Map<Integer, NullSpeakerTalk> talks;
    private int talkIdNum = 0;

    public TalkManager() {
        talks = new HashMap<>();
    }


    @Override
    public String toString(){
        StringBuilder res = new StringBuilder();
        for(NullSpeakerTalk t : talks.values()){
            res.append(t.toString()).append('\n');
        }

        return res.toString();
    }

    public List<List<String>> getDateTimesOfTalks(List<Integer> talk) {
        List<List<String>> res = new ArrayList<>();
        for(int i : talk) {
            List<String> times = new ArrayList<>();
            times.add(talks.get(i).getDate().replaceAll("[^0-9A-Za-z]","") +
                    talks.get(i).getStartingTime().replaceAll("[^0-9A-Za-z]",""));
            times.add(talks.get(i).getEndDate().replaceAll("[^0-9A-Za-z]","") +
                    talks.get(i).getEndTime().replaceAll("[^0-9A-Za-z]",""));
            res.add(times);
        }
        return res;

    }

    public List<Integer> getAttendees(List<Integer> talk) {
        List<Integer> attendees = new ArrayList<>();
        //attendee.add(1); //testing
        for (int i : talk) {
            if (isTalkExisting(i)) {
                attendees.addAll(talks.get(i).getAttendees());
            }
        }
        return attendees;
    }

    public int addTalk(String title, int speakerID, String date, String endDate,
                       String startingTime, String endTime, int roomID, int eventCapacity,int OrgId) {
        // Im assuming the id is just the position of the talk in the hashmap
        //return 1; //testing
        int id = talkIdNum;
        Talk t = new Talk(id, title, speakerID, date, endDate, startingTime, endTime, roomID, eventCapacity,OrgId);
        talks.put(id, t);
        talkIdNum += 1;
        return id;
    }

    // TODO: NEW!
    public int addParty(String title, String date, String endDate, String startingTime, String endTime, int roomID, int eventCapacity, int OrgId) {
        int id = talkIdNum;
        NullSpeakerTalk t = new NullSpeakerTalk(id, title, date, endDate, startingTime, endTime, roomID, eventCapacity, OrgId);
        talks.put(id, t);
        this.talkIdNum += 1;
        return id;
    }
    // TODO: NEW!
    public int addPanelDiscussion(String title, int speakerID, String date, String endDate,
                                  String startingTime, String endTime, int roomID, int eventCapacity, int OrgId) {
        int id = talkIdNum;
        MultiSpeakerTalk t = new MultiSpeakerTalk(id, title, speakerID, date, endDate, startingTime, endTime, roomID, eventCapacity, OrgId);
        talks.put(id, t);
        this.talkIdNum += 1;
        return id;
    }

    public void addSpeakerToPanelDiscussion(int speakerID, int talkID) {
        for (NullSpeakerTalk talk : getAllTalks().values()) {
            if (talk instanceof MultiSpeakerTalk && (talk.getId() == talkID)) {
                ((MultiSpeakerTalk) talk).addSpeaker(speakerID);
            }
        }
    }

    // TODO: Is this right? Replaced specific implementation with broader toString method
    // It's right
    public String printTalk(int id) {
        //return ""; //testing
        if(!isTalkExisting(id)){
            return "";
        }
        return talks.get(id).toString();
    }


    public boolean checkIfUserSingedUpForATalk(int userID, int talkID) {
        List<Integer> check = talks.get(talkID).getAttendees();
        for (int i = 0; i < check.size(); i++) {
            if(check.get(i) == userID) {
                return true;
            }
        }
        return false;
    }

    public boolean addAttendeeToATalk(int userId, int talkId) {
        AttendeeManager man = new AttendeeManager();
        if (!checkIfUserSingedUpForATalk(userId, talkId)) {
            talks.get(talkId).addAttendee(userId);
            return true;
        } else {
            return false;
        }

    }

    public boolean removeAttendeeFromATalk(int userId, int talkId) {
        List<Integer> currentAttendees = new ArrayList<>(talks.get(talkId).getAttendees());
        for(int i : currentAttendees) {
            if(i == userId) {
                talks.get(talkId).getAttendees().remove(new Integer(userId));
                return true;
            }
        }
        return false;
    }

    public int getNumAttendeesByTalkID(int talkID) {
        return talks.get(talkID).getAttendees().size();
    }

    public int getRoomIDByTalkID(int talkID) {
        int roomID = talks.get(talkID).getRoomID();
        //return -1 if room not found
        for(NullSpeakerTalk t : talks.values()) {
            if (t.getRoomID() == talks.get(talkID).getRoomID()) {
                return t.getRoomID();
            }
        }
        return -1;
    }

    public boolean isTalkExisting(int talkID) {
        for (NullSpeakerTalk t : talks.values()) {
            if(t.getId() == talkID) {
                return true;
            }
        }
        return false;
    }

    public Map<Integer, NullSpeakerTalk> getAllTalks() {
        return talks;
    }

    public List<Integer> getUnexpiredTalkIDs(){
        List<Integer> unexpiredTalkIDs = new ArrayList<>();
        for(Map.Entry<Integer, NullSpeakerTalk> entry : talks.entrySet()){
            NullSpeakerTalk cTalk = entry.getValue();
            String cTalkStartTime = cTalk.getDateTimeStr().get(0);
            boolean isTalkExpired = isDateTimePast(cTalkStartTime);

            if(!isTalkExpired){
                unexpiredTalkIDs.add(cTalk.getId());
            }
        }
        return unexpiredTalkIDs;

    }

    public List<Integer> getEventsByDate(String date) {
        List<Integer> eventsOfADate = new ArrayList<>();

        for(Map.Entry<Integer, NullSpeakerTalk> entry : talks.entrySet()){
            NullSpeakerTalk event = entry.getValue();
            String eventDate = event.getDate();
            if(eventDate.equals(date)){
                eventsOfADate.add(event.getId());
            }
        }
        return eventsOfADate;
    }

    public List<Integer> getEventsByTime(String startTime, String endTime) {
        List<Integer> eventsOfATime = new ArrayList<>();

        for(Map.Entry<Integer, NullSpeakerTalk> entry : talks.entrySet()){
            NullSpeakerTalk event = entry.getValue();
            String eventStartTime = event.getStartingTime();
            String eventEndTime = event.getEndTime();

            if(eventStartTime.equals(startTime) && eventEndTime.equals(endTime)){
                eventsOfATime.add(event.getId());
            }
        }
        return eventsOfATime;
    }

    public List<Integer> getEventsByTitle(String title) {
        List<Integer> eventsOfATitle = new ArrayList<>();

        for(Map.Entry<Integer, NullSpeakerTalk> entry : talks.entrySet()){
            NullSpeakerTalk event = entry.getValue();
            String eventTitle = event.getTitle();

            if(eventTitle.contains(title)){
                eventsOfATitle.add(event.getId());
            }
        }
        return eventsOfATitle;
    }

    public List<Integer> getPanelDiscussionIDs() {
        ArrayList<Integer> result = new ArrayList<>();
        for (NullSpeakerTalk talk : this.talks.values()) {
            if (talk instanceof MultiSpeakerTalk) {
                result.add(talk.getId());
            }
        }
        return result;
    }

    private boolean isDateTimePast(String dateTime) {
        boolean res = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        Date dateToCompare = null;
        try {
            dateToCompare = sdf.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date now = new Date();

        if(now.after(dateToCompare)) {
            res = true;
        }

        return res;
    }

    public int getSpeakerIDByTalkID(int talkID) {
        //return -1 if room not found
        ArrayList<Talk> listOfTalks = new ArrayList<>();
        for (NullSpeakerTalk t : talks.values()) {
            if (t instanceof Talk) {
                listOfTalks.add((Talk) t);
            }
        }
        for (Talk t : listOfTalks) {
            if (t.getId() == talkID) {
                return t.getSpeakerID();
            }
        }
        return -1;
    }

    public String getTalkSchedule(int id) {
        if (!isTalkExisting(id)) {
            return "";
        }
        return "ID: " + id + "\nTalk Title: " + talks.get(id).getTitle() +
                "\nDate: " + talks.get(id).getDate() + "  " +
                 talks.get(id).getStartingTime() + " to " + talks.get(id).getEndTime();
    }

    public String getTitleByTalkID(int talkID) {
        ArrayList<Talk> listOfTalks = new ArrayList<>();
        for (NullSpeakerTalk t : talks.values()) {
//            if (t instanceof Talk) {
//                listOfTalks.add((Talk) t);
//            }
//        }
//        for (Talk t : listOfTalks) {
            if (t.getId() == talkID) {
                return t.getTitle();
            }
        }
        return null;
    }

    public String getDateByTalkID(int talkID) {
        ArrayList<Talk> listOfTalks = new ArrayList<>();
        for (NullSpeakerTalk t : talks.values()) {
//            if (t instanceof Talk) {
//                listOfTalks.add((Talk) t);
//            }
//        }
//        for (Talk t : listOfTalks) {
            if (t.getId() == talkID) {
                return t.getDate();
            }
        }
        return null;
    }

    public String getEndDateByTalkID(int talkID) {
        ArrayList<Talk> listOfTalks = new ArrayList<>();
        for (NullSpeakerTalk t : talks.values()) {
//            if (t instanceof Talk) {
//                listOfTalks.add((Talk) t);
//            }
//        }
//        for (Talk t : listOfTalks) {
            if (t.getId() == talkID) {
                return t.getEndDate();
            }
        }
        return null;
    }

    public String getStartingTimeByTalkID(int talkID) {
        ArrayList<Talk> listOfTalks = new ArrayList<>();
        for (NullSpeakerTalk t : talks.values()) {
//            if (t instanceof Talk) {
//                listOfTalks.add((Talk) t);
//            }
//        }
//        for (Talk t : listOfTalks) {
            if (t.getId() == talkID) {
                return t.getStartingTime();
            }
        }
        return null;
    }

    public String getEndTimeByTalkID(int talkID) {
        ArrayList<Talk> listOfTalks = new ArrayList<>();
        for (NullSpeakerTalk t : talks.values()) {
//            if (t instanceof Talk) {
//                listOfTalks.add((Talk) t);
//            }
//        }
//        for (Talk t : listOfTalks) {
            if (t.getId() == talkID) {
                return t.getEndTime();
            }
        }
        return null;
    }

    public int getEventCapacityByTalkID(int talkID) {
        ArrayList<Talk> listOfTalks = new ArrayList<>();
        for (NullSpeakerTalk t : talks.values()) {
//            if (t instanceof Talk) {
//                listOfTalks.add((Talk) t);
//            }
//        }
//        for (Talk t : listOfTalks) {
            if (t.getId() == talkID) {
                return t.getCapacity();
            }
        }
        return -1;
    }

    public int modifyTalk(int talkID, String title, int speakerID, String date, String endDate, String startingTime, String endTime, int roomID, int eventCapacity) {
        if (!talks.containsKey(talkID)) return -1;

        Talk talk = (Talk)talks.get(talkID);

        talk.setTitle(title);
        talk.setSpeakerID(speakerID);
        talk.setDate(date);
        talk.setEndDate(endDate);
        talk.setStartingTime(startingTime);
        talk.setEndTime(endTime);
        talk.setRoomID(roomID);
        talk.setCapacity(eventCapacity);

        return talkID;
    }

    public int modifyParty(int partyID, String title, String date, String endDate, String startingTime, String endTime, int roomID, int eventCapacity) {
        if (!talks.containsKey(partyID)) return -1;

        NullSpeakerTalk talk = talks.get(partyID);

        talk.setTitle(title);
        talk.setDate(date);
        talk.setEndDate(endDate);
        talk.setStartingTime(startingTime);
        talk.setEndTime(endTime);
        talk.setRoomID(roomID);
        talk.setCapacity(eventCapacity);

        return partyID;
    }

    public int modifyPanelDiscussion(int panelDiscussionID, String title, String date, String endDate, String startingTime, String endTime, int roomID, int eventCapacity) {
        if (!talks.containsKey(panelDiscussionID)) return -1;

        MultiSpeakerTalk talk = (MultiSpeakerTalk)talks.get(panelDiscussionID);

        talk.setTitle(title);
        talk.setDate(date);
        talk.setEndDate(endDate);
        talk.setStartingTime(startingTime);
        talk.setEndTime(endTime);
        talk.setRoomID(roomID);
        talk.setCapacity(eventCapacity);

        return panelDiscussionID;
    }

    private List<String> buildTalkRow(NullSpeakerTalk talk) {
        List<String> row;
        if(talk instanceof MultiSpeakerTalk) {
            row = Arrays.asList(String.valueOf(talk.getId()), talk.getTitle(), "Panel Discussion",((MultiSpeakerTalk) talk).getSpeakerIDs().toString(),
                    talk.getDate(), talk.getEndDate(), talk.getStartingTime(), talk.getEndTime(),
                    String.valueOf(talk.getRoomID()), String.valueOf(talk.getCapacity()));
        } else if (talk instanceof Talk) {
            row = Arrays.asList(String.valueOf(talk.getId()), talk.getTitle(), "Talk",String.valueOf(((Talk) talk).getSpeakerID()),
                    talk.getDate(), talk.getEndDate(), talk.getStartingTime(), talk.getEndTime(),
                    String.valueOf(talk.getRoomID()), String.valueOf(talk.getCapacity()));
        } else {
            row = Arrays.asList(String.valueOf(talk.getId()), talk.getTitle(), "Party","",
                    talk.getDate(), talk.getEndDate(), talk.getStartingTime(), talk.getEndTime(),
                    String.valueOf(talk.getRoomID()), String.valueOf(talk.getCapacity()));
        }
        return row;
    }

    /**
     * Get a list containing all the events info.
     * @return the list of all empty
     * */
    public List<List<String>> getAllTalkRows() {
        List<List<String>> rows = new ArrayList<>();

        for(NullSpeakerTalk talk : talks.values()) {
            List<String>  row = buildTalkRow(talk);
            rows.add(row);

        }

        return rows;
    }

    /**
     * Get a list containing all the empty events info.
     * @return the list of all empty
     * */
    public List<List<String>> getEmptyTalkRows() {
        List<NullSpeakerTalk> emptyTalks = getEmptyTalks();
        List<List<String>> rows = new ArrayList<>();

        for(NullSpeakerTalk talk : emptyTalks) {
            List<String> row = buildTalkRow(talk);
            rows.add(row);
        }

        return rows;
    }

    /**
     * Get a list containing the event schedule info with event ids.
     * @param talkIDs a list of talk ids
     * @return the list of talk schedule
     * */
    public List<List<String>> getTalkScheduleRowsByIDs(List<Integer> talkIDs) {
        List<List<String>> rows = new ArrayList<>();

        for(NullSpeakerTalk talk : talks.values()) {
            if(talkIDs.contains(talk.getId())) {
                List<String> row = buildTalkRow(talk);
                rows.add(row);
            }
        }

        return rows;
    }

    private List<NullSpeakerTalk> getEmptyTalks() {
        List<NullSpeakerTalk> emptyTalks = new ArrayList<>();
        for(NullSpeakerTalk talk : talks.values()){
            if(talk.getAttendees().size() == 0 ){
                emptyTalks.add(talk);
            }
        }
        return emptyTalks;
    }

    /**
     * Delete an empty event without attendees.
     * @param talkID the talk id to delete
     * @return the deleted talk id
     * */
    public int deleteEmptyTalkByID(int talkID) {
        List<NullSpeakerTalk> emptyTalks = getEmptyTalks();
        for(Iterator<NullSpeakerTalk> iter = emptyTalks.iterator(); iter.hasNext();) {
            NullSpeakerTalk talk = iter.next();
            if(talk.getId() == talkID) {
                talks.remove(talkID);
                return talkID;
            }
        }
        // return -1 if cannot find the talk id in empty talks
        return -1;
    }


    /**
     * Get the list of speaker ids of a talk.
     * @param talkID the talk id
     * @return the speaker id list for the talk
     * */
    public List<Integer> getSpeakerIDListByTalkID(int talkID) {
        List<Integer> speakerIDs = new ArrayList<>();
        for(NullSpeakerTalk talk : talks.values()) {
            if(talk.getId() == talkID) {
                if(talk instanceof MultiSpeakerTalk) {
                    return ((MultiSpeakerTalk) talk).getSpeakerIDs();
                } else if (talk instanceof Talk) {
                    speakerIDs.add(((Talk) talk).getSpeakerID());
                    return speakerIDs;
                }
            }
        }

        return speakerIDs;
    }


    public List<Integer> getAllParties() {
        List<Integer> res = new ArrayList<>();
        for (NullSpeakerTalk talk : talks.values()) {
            if (talk instanceof NullSpeakerTalk && !(talk instanceof Talk) && !(talk instanceof MultiSpeakerTalk)) res.add(talk.getId());
        }
        return res;
    }

    public List<Integer> getAllPanelDiscussions() {
        List<Integer> res = new ArrayList<>();
        for (NullSpeakerTalk talk : talks.values()) {
            if (talk instanceof MultiSpeakerTalk) res.add(talk.getId());
        }
        return res;
    }

    public List<NullSpeakerTalk> getOrgEvents(int orgId){
        List<Integer> unexpiredEvents = getUnexpiredTalkIDs();
        List <NullSpeakerTalk>  orgEvents = new ArrayList<>();
        for (NullSpeakerTalk  talk : talks.values()){
            if (talk.getOrgId()==orgId&&unexpiredEvents.contains(talk.getId())){
                orgEvents.add(talk);
            }
        }
        return orgEvents;
    }


    public List<List<String>> getOrgTalkRows(List<NullSpeakerTalk> orgEvents) {
        List<List<String>> rows = new ArrayList<>();
        for(NullSpeakerTalk talk: orgEvents){
            List<String>  row = buildTalkRow(talk);
            rows.add(row);
        }
        return rows;
    }

    public int deleteEventByID(int eventId){
        for(Iterator<NullSpeakerTalk> iter = talks.values().iterator(); iter.hasNext();) {
            NullSpeakerTalk talk = iter.next();
            if(talk.getId() == eventId) {
                talks.remove(eventId);
                return eventId;
            }
        }
        // if cannot find the talk with specified id, return -1
        return -1;
    }

    public List<Integer> getAttendeesOftheEvent(int eventId){
        return talks.get(eventId).getAttendees();
    }

    public List<Integer> filterTalksFromIds(List<Integer> events) {
        List<Integer> res = new ArrayList<>();
        for (int e : events) {
            NullSpeakerTalk t = talks.get(e);
            if (t instanceof Talk && !(t instanceof MultiSpeakerTalk)) {
                res.add(e);
            }
        }
        return res;
    }

}
