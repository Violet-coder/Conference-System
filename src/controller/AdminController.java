package controller;

import gateway.SerializableGateway;
import gateway.UserGateway;
import usecase.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AdminController contains methods for adding a user, scheduling an event or modifying an event.
 * It uses methods in various manager classes (use cases) to create entities.
 */
public class AdminController {

    private String lowerBoundTime;
    private String higherBoundTime;
    private ITalkManager talkManager;
    private ISpeakerManager speakerManager;
    private IRoomManager roomManager;
    private IAttendeeManager attendeeManager;
    private IOrganizerManager organizerManager;
    private IAdminManager adminManager;
    private SerializableGateway generalGateway;
    private RegisterController registerController;
    private LoginController loginController;

    public AdminController(ITalkManager talkManager, ISpeakerManager speakerManager,
                           IRoomManager roomManager, String lowerBoundTime, String higherBoundTime,
                           SerializableGateway generalGateway, RegisterController registerController,
                           LoginController loginController, IAttendeeManager attendeeManager,
                           IOrganizerManager organizerManager, IAdminManager adminManager) {
        this.talkManager = talkManager;
        this.speakerManager = speakerManager;
        this.roomManager = roomManager;
        this.lowerBoundTime = lowerBoundTime;
        this.higherBoundTime = higherBoundTime;
        this.generalGateway = generalGateway;
        this.registerController = registerController;
        this.loginController = loginController;
        this.attendeeManager = attendeeManager;
        this.organizerManager = organizerManager;
        this.adminManager = adminManager;
    }

    /**
     * Create an account for a attendee/speaker/organizer/admin.
     * @param username      The username of the account.
     * @param password      The password of the account.
     * @param name          The name of the user.
     * @return              The ID (non-negative number) assigned to the user if success.
     *                      -1 if username is duplicate.
     *                      -2 if failed to save data to file.
     */
    public int addAttendee(String username, String password, String name) {
        int res = attendeeManager.addAttendee(username, password, name);

        if (res >= 0) {
            try {
                generalGateway.saveToFile("phase2/resources/attendee.ser", attendeeManager);
                UserGateway userGateway = new UserGateway();
                userGateway.writeToTxt(res, username, password, "attendee", name);
//              registerController.register(username, password, "attendee", name);
            } catch (IOException e) {
                return -2;
            }
        }

        return res;
    }

    public int addSpeaker(String username, String password, String name) {
        int res = speakerManager.addSpeaker(username, password, name);

        if (res >= 0) {
            try {
                generalGateway.saveToFile("phase2/resources/speaker.ser", speakerManager);
                UserGateway userGateway = new UserGateway();
                userGateway.writeToTxt(res, username, password, "speaker", name);
//              registerController.register(username, password, "speaker", name);
            } catch (IOException e) {
                return -2;
            }
        }

        return res;
    }

    public int addOrganizer(String username, String password, String name) {
        int res = organizerManager.addOrganizer(username, password, name);

        if (res >= 0) {
            try {
                generalGateway.saveToFile("phase2/resources/organizer.ser", organizerManager);
                UserGateway userGateway = new UserGateway();
                userGateway.writeToTxt(res, username, password, "organizer", name);
//              registerController.register(username, password, "organizer", name);
            } catch (IOException e) {
                return -2;
            }
        }

        return res;
    }

    public int addAdmin(String username, String password, String name) {
        int res = adminManager.addAdmin(username, password, name);

        if (res >= 0) {
            try {
                generalGateway.saveToFile("phase2/resources/admin.ser", adminManager);
                UserGateway userGateway = new UserGateway();
                userGateway.writeToTxt(res, username, password, "admin", name);
//              registerController.register(username, password, "admin", name);
            } catch (IOException e) {
                return -2;
            }
        }

        return res;
    }

    /**
     * Add a room.
     * @param name          The name of the room.
     * @param capacity      The capacity of the room.
     * @return              The ID (non-negative number) assigned to the room if success.
     *                      -1 if room name is duplicate.
     *                      -2 if failed to save data to file.
     */
    public int addRoom(String name, int capacity, List<String> techRequirements, List<String> roomConditions) {
        int res = roomManager.addRoom(name, capacity, techRequirements, roomConditions);

        if (res >= 0) {
            try {
                generalGateway.saveToFile("phase2/resources/room.ser", roomManager);
            } catch (IOException e) {
                return -2;
            }
        }

        return res;
    }

    /**
     * Schedule a talk. Prevent double booking of room or speaker.
     * @param title             The title of the talk.
     * @param speakerID         The ID of the speaker of the talk.
     * @param roomID            The ID of the room of the talk.
     * @param date              The date of the talk.
     * @param startingTime      The starting time of the talk.
     * @param endTime           The end time of the talk.
     * @param eventCapacity     The capacity of the talk.
     * @return                  The ID (non-negative number) assigned to the talk if success.
     *                          -2 if failed to save data to file.
     *                          -3 if both speaker and room have time conflict.
     *                          -4 if only speaker has time conflict.
     *                          -5 if only room has time conflict.
     */
    public int scheduleTalk(String title, int speakerID, int roomID,
                            String date, String endDate, String startingTime, String endTime, int eventCapacity) {

        boolean isSpeakerAvailable = isSpeakerAvailable(speakerID, date, endDate, startingTime, endTime);
        boolean isRoomAvailable = isRoomAvailable(roomID, date, endDate, startingTime, endTime);

        // Both speaker and room should be available in provided date time
        if (isSpeakerAvailable && isRoomAvailable) {
            // Add talk
            int talkID = talkManager.addTalk(title, speakerID, date, endDate, startingTime, endTime, roomID, eventCapacity, loginController.getUserId());
            roomManager.addTalkToRoom(roomID, talkID);
            speakerManager.addTalkToSpeaker(speakerID, talkID);

            String pathTalk = "phase2/resources/talk.ser";
            String pathRoom = "phase2/resources/room.ser";
            String pathSpeaker = "phase2/resources/speaker.ser";
            try {
                generalGateway.saveToFile(pathTalk, talkManager);
                generalGateway.saveToFile(pathRoom, roomManager);
                generalGateway.saveToFile(pathSpeaker, speakerManager);
            } catch (IOException e) {
                return -2;
            }

            return talkID;
        }
        else if (!isSpeakerAvailable && !isRoomAvailable) {
            return -3;
        }
        else if (!isSpeakerAvailable) {
            return -4;
        }
        else {
            return -5;
        }

    }

    /**
     * Modify a talk. Prevent double booking of room or speaker.
     * @param talkID            The id of the talk to modify.
     * @param title             The new title of the talk.
     * @param speakerID         The new ID of the speaker of the talk.
     * @param roomID            The new ID of the room of the talk.
     * @param date              The new date of the talk.
     * @param startingTime      The new starting time of the talk.
     * @param endTime           The new end time of the talk.
     * @param eventCapacity     The new capacity of the talk.
     * @return                  The ID (non-negative number) of the talk if success.
     *                          -2 if failed to save data to file.
     *                          -3 if both speaker and room have time conflict.
     *                          -4 if only speaker has time conflict.
     *                          -5 if only room has time conflict.
     */
    public int modifyTalk(int talkID, String title, int speakerID, int roomID,
                            String date, String endDate, String startingTime, String endTime, int eventCapacity) {

        boolean isSpeakerAvailable = isSpeakerAvailableToModify(talkID, speakerID, date, endDate, startingTime, endTime);
        boolean isRoomAvailable = isRoomAvailableToModify(talkID, roomID, date, endDate, startingTime, endTime);

        // Both speaker and room should be available in provided date time
        if (isSpeakerAvailable && isRoomAvailable) {
            int tid = talkManager.modifyTalk(talkID, title, speakerID, date, endDate, startingTime, endTime, roomID, eventCapacity);
            if (tid == -1) return tid;

            roomManager.modifyTalkRoom(roomID, talkID);
            speakerManager.modifyTalkSpeaker(speakerID, talkID);

            String pathTalk = "phase2/resources/talk.ser";
            String pathRoom = "phase2/resources/room.ser";
            String pathSpeaker = "phase2/resources/speaker.ser";
            try {
                generalGateway.saveToFile(pathTalk, talkManager);
                generalGateway.saveToFile(pathRoom, roomManager);
                generalGateway.saveToFile(pathSpeaker, speakerManager);
            } catch (IOException e) {
                return -2;
            }

            return tid;
        }
        else if (!isSpeakerAvailable && !isRoomAvailable) {
            return -3;
        }
        else if (!isSpeakerAvailable) {
            return -4;
        }
        else {
            return -5;
        }

    }

    /**
     * Modify a party. Prevent double booking of room.
     * @param partyID           The id of the party to modify.
     * @param title             The new title of the party.
     * @param roomID            The new ID of the room of the party.
     * @param date              The new date of the party.
     * @param startingTime      The new starting time of the party.
     * @param endTime           The new end time of the party.
     * @param eventCapacity     The new capacity of the party.
     * @return                  The ID (non-negative number) of the party if success.
     *                          -2 if failed to save data to file.
     *                          -3 if room has time conflict.
     */
    public int modifyParty(int partyID, String title, int roomID,
                          String date, String endDate, String startingTime, String endTime, int eventCapacity) {

        boolean isRoomAvailable = isRoomAvailableToModify(partyID, roomID, date, endDate, startingTime, endTime);

        // Room should be available in provided date time
        if (isRoomAvailable) {
            int pid = talkManager.modifyParty(partyID, title, date, endDate, startingTime, endTime, roomID, eventCapacity);
            if (pid == -1) return pid;

            roomManager.modifyTalkRoom(roomID, partyID);

            String pathTalk = "phase2/resources/talk.ser";
            String pathRoom = "phase2/resources/room.ser";
            try {
                generalGateway.saveToFile(pathTalk, talkManager);
                generalGateway.saveToFile(pathRoom, roomManager);
            } catch (IOException e) {
                return -2;
            }

            return pid;
        }
        else {
            return -3;
        }

    }

    /**
     * Modify a panel discussion. Prevent double booking of room.
     * @param panelDiscussionID The id of the panel discussion to modify.
     * @param title             The new title of the panel discussion.
     * @param roomID            The new ID of the room of the panel discussion.
     * @param date              The new date of the panel discussion.
     * @param startingTime      The new starting time of the panel discussion.
     * @param endTime           The new end time of the panel discussion.
     * @param eventCapacity     The new capacity of the panel discussion.
     * @return                  The ID (non-negative number) of the panel discussion if success.
     *                          -2 if failed to save data to file.
     *                          -3 if room has time conflict.
     */
    public int modifyPanelDiscussion(int panelDiscussionID, String title, int roomID,
                           String date, String endDate, String startingTime, String endTime, int eventCapacity) {

        boolean isRoomAvailable = isRoomAvailableToModify(panelDiscussionID, roomID, date, endDate, startingTime, endTime);

        // Room should be available in provided date time
        if (isRoomAvailable) {
            int pid = talkManager.modifyPanelDiscussion(panelDiscussionID, title, date, endDate, startingTime, endTime, roomID, eventCapacity);
            if (pid == -1) return pid;

            roomManager.modifyTalkRoom(roomID, panelDiscussionID);

            String pathTalk = "phase2/resources/talk.ser";
            String pathRoom = "phase2/resources/room.ser";
            try {
                generalGateway.saveToFile(pathTalk, talkManager);
                generalGateway.saveToFile(pathRoom, roomManager);
            } catch (IOException e) {
                return -2;
            }

            return pid;
        }
        else {
            return -3;
        }

    }

    /**
     * Schedule a party. Prevent double booking of room.
     * @param title             The title of the party.
     * @param roomID            The ID of the room of the party.
     * @param date              The date of the party.
     * @param startingTime      The starting time of the party.
     * @param endTime           The end time of the party.
     * @param eventCapacity     The capacity of the party.
     * @return                  The ID (non-negative number) assigned to the party if successful.
     *                          -2 if failed to save data to file.
     *                          -3 if only room has time conflict.
     */
    public int scheduleParty(String title, int roomID,
                            String date, String endDate, String startingTime, String endTime, int eventCapacity, int orgId) {

        boolean isRoomAvailable = isRoomAvailable(roomID, date, endDate, startingTime, endTime);

        // Both speaker and room should be available in provided date time
        if (isRoomAvailable) {
            // Add talk
            int partyID = talkManager.addParty(title, date, endDate, startingTime, endTime, roomID, eventCapacity, orgId );
            roomManager.addTalkToRoom(roomID, partyID);

            String pathParty = "phase2/resources/talk.ser"; // Should be talk.ser
            String pathRoom = "phase2/resources/room.ser";
            try {
                generalGateway.saveToFile(pathParty, talkManager);
                generalGateway.saveToFile(pathRoom, roomManager);
            } catch (IOException e) {
                return -2;
            }

            return partyID;
        }
        else {
            return -3;
        }

    }

    /**
     * Schedule a panel discussion. Prevent double booking of room or speaker.
     * @param title             The title of the panel discussion.
     * @param speakerID         The ID of the speaker of the panel discussion.
     * @param roomID            The ID of the room of the panel discussion.
     * @param date              The date of the panel discussion.
     * @param startingTime      The starting time of the panel discussion.
     * @param endTime           The end time of the panel discussion.
     * @param eventCapacity     The capacity of the panel discussion.
     * @return                  The ID (non-negative number) assigned to the panel discussion if successful.
     *                          -2 if failed to save data to file.
     *                          -3 if both speaker and room have time conflict.
     *                          -4 if only speaker has time conflict.
     *                          -5 if only room has time conflict.
     */
    public int schedulePanelDiscussion(String title, int speakerID, int roomID,
                            String date, String endDate, String startingTime, String endTime, int eventCapacity) {

        boolean isSpeakerAvailable = isSpeakerAvailable(speakerID, date, endDate, startingTime, endTime);
        boolean isRoomAvailable = isRoomAvailable(roomID, date, endDate, startingTime, endTime);

        // Both speaker and room should be available in provided date time
        if (isSpeakerAvailable && isRoomAvailable) {
            // Add panel discussion
            int panelDiscussionID = talkManager.addPanelDiscussion(title, speakerID, date, endDate, startingTime, endTime, roomID, eventCapacity, loginController.getUserId());
            roomManager.addTalkToRoom(roomID, panelDiscussionID);
            speakerManager.addTalkToSpeaker(speakerID, panelDiscussionID);

            String pathPanelDiscussion = "phase2/resources/talk.ser"; // Should be talk.ser
            String pathRoom = "phase2/resources/room.ser";
            String pathSpeaker = "phase2/resources/speaker.ser";
            try {
                generalGateway.saveToFile(pathPanelDiscussion, talkManager);
                generalGateway.saveToFile(pathRoom, roomManager);
                generalGateway.saveToFile(pathSpeaker, speakerManager);
            } catch (IOException e) {
                return -2;
            }

            return panelDiscussionID;
        }
        else if (!isSpeakerAvailable && !isRoomAvailable) {
            return -3;
        }
        else if (!isSpeakerAvailable) {
            return -4;
        }
        else {
            return -5;
        }

    }

    public int addSpeakerToPanelDiscussion(int speakerID, int panelDiscussionID) {
        String startingTime = talkManager.getStartingTimeByTalkID(panelDiscussionID);
        String endingTime = talkManager.getEndTimeByTalkID(panelDiscussionID);
        String date = talkManager.getDateByTalkID(panelDiscussionID);
        String endDate = talkManager.getEndDateByTalkID(panelDiscussionID);

        boolean isSpeakerAvailable = isSpeakerAvailable(speakerID, date, endDate, startingTime, endingTime);

        if (isSpeakerAvailable) {
            // Add speaker to panel discussion
            talkManager.addSpeakerToPanelDiscussion(speakerID, panelDiscussionID);
            speakerManager.addTalkToSpeaker(speakerID, panelDiscussionID);

            String pathPanelDiscussion = "phase2/resources/talk.ser";  // Should be talk.ser
            String pathSpeaker = "phase2/resources/speaker.ser";

            try {
                generalGateway.saveToFile(pathPanelDiscussion, talkManager);
                generalGateway.saveToFile(pathSpeaker, speakerManager);
            } catch (IOException e) {
                return -2;
            }

            return 0;

        }
        else {
            return -1;
        }
    }

    private boolean isSpeakerAvailableToModify(int talkID, int speakerID, String date, String endDate,
                                       String startingTime, String endTime) {
        List<Integer> talksSpeaker = speakerManager.getTalksGivenBySpeaker(speakerID);
        List<List<String>> dateTimesSpeaker = talkManager.getDateTimesOfTalks(talksSpeaker);
        int idx = talksSpeaker.indexOf(talkID);
        if (idx >= 0) dateTimesSpeaker.remove(idx);

        String startDateTime = getDateTimeStr(date, startingTime);
        String endDateTime = getDateTimeStr(endDate, endTime);

        return hasTimeConflict(dateTimesSpeaker, startDateTime, endDateTime);
    }

    private boolean isRoomAvailableToModify(int talkID, int roomID, String date, String endDate,
                                    String startingTime, String endTime) {
        List<Integer> talksRoom = roomManager.getTalksGivenInRoom(roomID);
        List<List<String>> dateTimesRoom = talkManager.getDateTimesOfTalks(talksRoom);
        int idx = talksRoom.indexOf(talkID);
        if (idx >= 0) dateTimesRoom.remove(idx);

        String startDateTime = getDateTimeStr(date, startingTime);
        String endDateTime = getDateTimeStr(endDate, endTime);

        return hasTimeConflict(dateTimesRoom, startDateTime, endDateTime);
    }

    private boolean isSpeakerAvailable(int speakerID, String date, String endDate,
                                       String startingTime, String endTime) {
        List<Integer> talksSpeaker = speakerManager.getTalksGivenBySpeaker(speakerID);
        List<List<String>> dateTimesSpeaker = talkManager.getDateTimesOfTalks(talksSpeaker);

        String startDateTime = getDateTimeStr(date, startingTime);
        String endDateTime = getDateTimeStr(endDate, endTime);

        return hasTimeConflict(dateTimesSpeaker, startDateTime, endDateTime);
    }

    private boolean isRoomAvailable(int roomID, String date, String endDate,
                                    String startingTime, String endTime) {
        List<Integer> talksRoom = roomManager.getTalksGivenInRoom(roomID);
        List<List<String>> dateTimesRoom = talkManager.getDateTimesOfTalks(talksRoom);

        String startDateTime = getDateTimeStr(date, startingTime);
        String endDateTime = getDateTimeStr(endDate, endTime);

        return hasTimeConflict(dateTimesRoom, startDateTime, endDateTime);
    }

    private String getDateTimeStr(String date, String time) {
        return String.join("", date.split("-")) +
                String.join("", time.split(":"));
    }

    private boolean hasTimeConflict(List<List<String>> dateTimes, String startDateTime,
                                    String endDateTime) {
        for (List<String> dt : dateTimes) {
            if (isDateTimeOverlapping(startDateTime, endDateTime, dt.get(0), dt.get(1))) {
                return false;
            }
        }
        return true;
    }

    private boolean isDateTimeOverlapping(String start1, String end1, String start2, String end2) {
        return (Long.parseLong(start1) < Long.parseLong(end2)) &&
                (Long.parseLong(start2) < Long.parseLong(end1));
    }


    public Map<String, Integer> deleteEventById(int eventId){
        Map<String, Integer> cancelRes = new HashMap<>();

        //Get roomIds of the event to be canceled
        int roomID = talkManager.getRoomIDByTalkID(eventId);
        //Get speakerIds of the event to be canceled
        List<Integer> speakerIds = talkManager.getSpeakerIDListByTalkID(eventId);
        //Get getAttendeesIds of the event to be canceled
        List<Integer> attendeesOftheEvent = talkManager.getAttendeesOftheEvent(eventId);

        //delete the event from the talkManager
        int deletedEventStatus = talkManager.deleteEventByID(eventId);
        cancelRes.put("Event", deletedEventStatus);


        // if the talk has a speaker or multi speakers, delete the talk from the speaker entity
        // for the speakers of the talk, cancel the talk for them
        if(speakerIds.size()<1){
            cancelRes.put("Speaker", 1);
        }else{
            for(int speaker : speakerIds) {
                int res =  speakerManager.cancelTalkForSpeaker(speaker, eventId);
                cancelRes.put("Speaker", res);
                if (res == -1){
                    break;
                }
            }
        }

        // remove the talk from the room
        int cancelRoomID = roomManager.removeTalkFromRoom(roomID, eventId);
        cancelRes.put("Room", cancelRoomID);

        //remove the event from the attendee's events
        if(attendeesOftheEvent.size()<1){
            cancelRes.put("Attendee", 1);
        }else{
            for(int attendee : attendeesOftheEvent){
                boolean res = attendeeManager.cancelATalkForAttendee(attendee, eventId);
                if(res){
                    cancelRes.put("Attendee", 1);
                }else{
                    cancelRes.put("Attendee", -1);
                    break;
                }
            }
        }

        //update the ser file

        try {
            generalGateway.saveToFile("phase2/resources/talk.ser", talkManager);
            generalGateway.saveToFile("phase2/resources/speaker.ser", speakerManager);
            generalGateway.saveToFile("phase2/resources/room.ser", roomManager);
            generalGateway.saveToFile("phase2/resources/attendee.ser", attendeeManager);
        } catch (IOException e) {
            // cannot save to local file
            cancelRes.put("savetofile",-1);
            return cancelRes;
        }

        cancelRes.put("savetofile",1);
        return cancelRes;
    }
}
