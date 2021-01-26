package userInputController;

import controller.AdminController;
import controller.ClassCollection;
import controller.LoginController;
import entity.NullSpeakerTalk;
import gateway.UserGateway;
import presenter.TableGenerator;
import usecase.*;
import presenter.TextPresenter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

/**
 * AdminInputController contains methods for getting and validating user input when an organizer is adding a user, scheduling an event or modifying an event.
 * It passes valid user input to methods in AdminController for further processing.
 */
public class AdminInputController {

    // Talks can be scheduled between lowerBoundTime and higherBoundTime
    private String lowerBoundTime = "00:00";
    private String higherBoundTime = "23:59";
    private ITalkManager talkManager;
    private ISpeakerManager speakerManager;
    private IRoomManager roomManager;
    private AdminController adminController;
    private IAttendeeManager attendeeManager;
    private IOrganizerManager organizerManager;
    private IAdminManager adminManager;
    private LoginController loginController;
    private UserGateway userGateway;
    private TextPresenter textPresenter;
    private TableGenerator tableGenerator = new TableGenerator();

    /**
     * AdminInputController constructor requires the class collection as parameter,
     * which contains all the instantiated managers and controllers.
     * */
    public AdminInputController(ClassCollection classCollection) {
        this.talkManager = classCollection.talkManager;
        this.speakerManager = classCollection.speakerManager;
        this.roomManager = classCollection.roomManager;
        this.adminController = classCollection.adminController;
        this.attendeeManager =classCollection.attendeeManager;
        this.organizerManager =classCollection.organizerManager;
        this.adminManager =classCollection.adminManager;
        this.userGateway = new UserGateway();
        this.loginController = classCollection.loginController;
        this.textPresenter = classCollection.textPresenter;
    }

    /**
     * Interact with the user to add an attendee(create an attendee account).
     */
    public void addAttendee() {
        String username;
        String password;
        String name;

        username = getUsername();
        password = getPassword();
        name = getName();

        int attendeeID = adminController.addAttendee(username, password, name);

        if (attendeeID < 0) {
            // Duplicate username
            if (attendeeID == -1) textPresenter.printErrorText("Unable to add the attendee. The username is already taken.");
                // IOException when saving to file
            else textPresenter.printErrorText("Unable to add the attendee. Failed to save the attendee to file.");
        }
        else {
            // Success
            textPresenter.printSuccessText("The following attendee has been added:");
            textPresenter.printSuccessText(attendeeManager.printAttendee(attendeeID));
        }
    }

    /**
     * Interact with the user to add a speaker.
     */
    public void addSpeaker() {
        String username;
        String password;
        String name;

        username = getUsername();
        password = getPassword();
        name = getName();

        int speakerID = adminController.addSpeaker(username, password, name);

        if (speakerID < 0) {
            // Duplicate username
            if (speakerID == -1) textPresenter.printErrorText("Unable to add the speaker. The username is already taken.");
            // IOException when saving to file
            else textPresenter.printErrorText("Unable to add the speaker. Failed to save the speaker to file.");
        }
        else {
            // Success
            textPresenter.printSuccessText("The following speaker has been added:");
            textPresenter.printInfoText(speakerManager.printSpeaker(speakerID));
        }
    }

    /**
     * Interact with the user to add an organizer(create an organizer account).
     */
    public void addOrganizer() {
        String username;
        String password;
        String name;

        username = getUsername();
        password = getPassword();
        name = getName();

        int organizerID = adminController.addOrganizer(username, password, name);

        if (organizerID < 0) {
            // Duplicate username
            if (organizerID == -1) textPresenter.printErrorText("Unable to add the organizer. The username is already taken.");
                // IOException when saving to file
            else textPresenter.printErrorText("Unable to add the organizer. Failed to save the organizer to file.");
        }
        else {
            // Success
            textPresenter.printSuccessText("The following organizer has been added:");
            textPresenter.printSuccessText(organizerManager.printOrganizer(organizerID));
        }
    }

    /**
     * Interact with the user to add an admin(create an admin account).
     */
    public void addAdmin() {
        String username;
        String password;
        String name;

        username = getUsername();
        password = getPassword();
        name = getName();

        int adminID = adminController.addAdmin(username, password, name);

        if (adminID < 0) {
            // Duplicate username
            if (adminID == -1) textPresenter.printErrorText("Unable to add the admin. The username is already taken.");
                // IOException when saving to file
            else textPresenter.printErrorText("Unable to add the admin. Failed to save the admmin to file.");
        }
        else {
            // Success
            textPresenter.printSuccessText("The following admin has been added:");
            textPresenter.printSuccessText(adminManager.printAdmin(adminID));
        }
    }

    /**
     * Interact with the user to add a room.
     */
    public void addRoom() {
        String name;
        int capacity;
        List<String> techRequirements;
        List<String> roomConditions;

        name = getRoomName();
        capacity = getRoomCapacity();
        techRequirements = getTechRequirements();
        roomConditions = getRoomConditions();




        int roomID = adminController.addRoom(name, capacity, techRequirements, roomConditions);

        if (roomID < 0) {
            // Duplicate name
            if (roomID == -1) textPresenter.printErrorText("Unable to add the room. The name of the room is already taken.");
                // IOException when saving to file
            else textPresenter.printErrorText("Unable to add the room. Failed to save the room to file.");
        }
        else {
            // Success
            textPresenter.printSuccessText("The following room has been added:");
            textPresenter.printSuccessText(roomManager.printRoom(roomID));
        }
    }

    /**
     * Interact with the user to schedule a talk.
     */
    public void scheduleTalk() {
        Map<Integer, String> availableSpeakers = speakerManager.getAllSpeakerIdsAndNames();
        Map<Integer, String> availableRooms = roomManager.getAllRoomIdsAndNames();

        if (availableSpeakers.isEmpty()) {
            textPresenter.printErrorText("Unable to schedule a talk. No speaker is available.");
            return;
        }
        if (availableRooms.isEmpty()) {
            textPresenter.printErrorText("Unable to schedule a talk. No room is available.");
            return;
        }

        // Parameters to construct a talk
        String title;
        int speakerID;
        int roomID;
        String date;
        String endDate;
        String startingTime;
        String endTime;

        // Get values for parameters
        title = getTitle();

        speakerID = getSpeakerID(availableSpeakers);
        List<Integer> talksSpeaker = speakerManager.getTalksGivenBySpeaker(speakerID);
        if (!talksSpeaker.isEmpty()) {
            List<List<String>> dateTimesSpeaker = talkManager.getDateTimesOfTalks(talksSpeaker);
            textPresenter.printNormalText("The speaker is occupied during:");
            textPresenter.printInfoText(formatDateTimes(dateTimesSpeaker));
        }

        int eventCapacity = geteventCapacity();

        roomID = getSuggestedRoomIds(eventCapacity);
//        roomID = getRoomID(availableRooms);
        List<Integer> talksRoom = roomManager.getTalksGivenInRoom(roomID);
        if (!talksRoom.isEmpty()) {
            List<List<String>> dateTimesRoom = talkManager.getDateTimesOfTalks(talksRoom);
            textPresenter.printNormalText("The room is occupied during:");
            textPresenter.printInfoText(formatDateTimes(dateTimesRoom));
        }

        date = getDate();

        endDate = getEndDate(date);

        startingTime = getStartingTime();

        endTime = getEndTime(startingTime, date, endDate);

        int talkID = adminController.scheduleTalk(title, speakerID, roomID, date, endDate, startingTime, endTime, eventCapacity);

        if (talkID < 0) {
            // Time conflict
            if (talkID == -5) textPresenter.printErrorText("Unable to schedule the talk. The room has a time conflict.");
            else if (talkID == -4) textPresenter.printErrorText("Unable to schedule the talk. The speaker has a time conflict.");
            else if (talkID == -3) textPresenter.printErrorText("Unable to schedule the talk. Both the room and speaker have time conflicts.");
            // IOException when saving to file
            else textPresenter.printErrorText("Unable to schedule the talk. Failed to save the talk to file.");
        }
        else {
            // Success
            textPresenter.printNormalText("The following talk has been scheduled:");
            textPresenter.printSuccessText(talkManager.printTalk(talkID));
        }
    }

    // Methods for modifying talk
    private int getTalkIDToModify(int speakerID) {
        List<Integer> events = speakerManager.getTalksGivenBySpeaker(speakerID);
        List<Integer> talks = talkManager.filterTalksFromIds(events);
        // If speaker has no talks to give, return -1
        if (talks.size() <= 0) {
            return -1;
        }
        Scanner sc = new Scanner(System.in);
        int talkID;
        while (true) {
            textPresenter.printNormalText("All talks given by speaker "+speakerID+":");
            for (int tid : talks) {
                textPresenter.printNormalText("--------------------------------------------------");
                textPresenter.printInfoText(talkManager.printTalk(tid));
            }
            textPresenter.printNormalText("--------------------------------------------------");
            textPresenter.printNormalText("Enter the ID of the talk to modify: ");
            while (!sc.hasNextInt()) {
                textPresenter.printErrorText("Please enter an integer.");
                textPresenter.printNormalText("All talks given by speaker "+speakerID+":");
                for (int tid : talks) {
                    textPresenter.printNormalText("--------------------------------------------------");
                    textPresenter.printInfoText(talkManager.printTalk(tid));
                }
                textPresenter.printNormalText("--------------------------------------------------");
                textPresenter.printNormalText("Enter the ID of the talk to modify: ");
                sc.next();
            }
            talkID = sc.nextInt();
            // Ensure talk exists
            if (talks.contains(talkID)) {
                break;
            }
            textPresenter.printErrorText("Please enter a valid talk ID.");
        }
        return talkID;
    }

    private boolean yesOrNo(String message) {
        textPresenter.printNormalText(message);
        textPresenter.printNormalText("Type y for yes or n for no.");
        Scanner sc = new Scanner(System.in);
        String r = sc.nextLine().trim();
        while (!r.equalsIgnoreCase("y") && !r.equalsIgnoreCase("n")) {
            textPresenter.printErrorText("Invalid input. Type y for yes or n for no.");
            r = sc.nextLine().trim();
        }
        return r.equalsIgnoreCase("y") ? true : false;
    }

    private String getTitleToModify(int talkID) {
        if (!yesOrNo("Do you want to modify the title?")) return talkManager.getTitleByTalkID(talkID);

        return getTitle();
    }

    private String getDateToModify(int talkID) {
        if (!yesOrNo("Do you want to modify the starting date?")) return talkManager.getDateByTalkID(talkID);

        return getDate();
    }

    private String getEndDateToModify(int talkID, String startingDate) {
        String endDate = talkManager.getEndDateByTalkID(talkID);
        if (isDate1BeforeDate2(endDate, startingDate) && !endDate.equals(startingDate)) {
            textPresenter.printNormalText("The new starting date is after the original end date.");
            textPresenter.printNormalText("Please specify a new end date.");
            return getEndDate(startingDate);
        }

        if (!yesOrNo("Do you want to modify the end date?")) return endDate;

        return getEndDate(startingDate);
    }

    private String getStartingTimeToModify(int talkID) {
        if (!yesOrNo("Do you want to modify the starting time?")) return talkManager.getStartingTimeByTalkID(talkID);

        return getStartingTime();
    }

    private String getEndTimeToModify(int talkID, String startingTime, String startingDate, String endDate) {
        String endTime = talkManager.getEndTimeByTalkID(talkID);
        if (startingDate.equals(endDate) && isT1BeforeT2(endTime, startingTime)) {
            textPresenter.printNormalText("The new starting time is after the original end time.");
            textPresenter.printNormalText("Please specify a new end time.");
            return getEndTime(startingTime, startingDate, endDate);
        }

        if (!yesOrNo("Do you want to modify the end time?")) return endTime;

        return getEndTime(startingTime, startingDate, endDate);
    }

    private int getSpeakerIDToModify(int talkID, Map<Integer, String> availableSpeakers) {
        if (!yesOrNo("Do you want to modify the speaker?")) return talkManager.getSpeakerIDByTalkID(talkID);

        return getSpeakerID(availableSpeakers);
    }

    private int getEventCapacityToModify(int talkID) {
        if (!yesOrNo("Do you want to modify the event capacity?")) return talkManager.getEventCapacityByTalkID(talkID);

        return geteventCapacity();
    }

    private int getRoomIDToModify(int talkID, int eventCapacity) {
        int roomID = talkManager.getRoomIDByTalkID(talkID);
        int roomCapacity = roomManager.getRoomCapacityByID(roomID);
        if (roomCapacity < eventCapacity) {
            textPresenter.printNormalText("The capacity of the original room (Room "+roomID+") is lower than the new event capacity ("+eventCapacity+").");
            textPresenter.printNormalText("Please specify a new room.");
            return getSuggestedRoomIds(eventCapacity);
        }

        if (!yesOrNo("Do you want to modify the room?")) return roomID;

        return getSuggestedRoomIds(eventCapacity);
    }

    /**
     * Interact with the user to modify a talk.
     */
    public void modifyTalk() {
        Map<Integer, String> availableSpeakers = speakerManager.getAllSpeakerIdsAndNames();

        if (availableSpeakers.isEmpty()) {
            textPresenter.printErrorText("Unable to modify a talk. No speaker is available.");
            return;
        }

        int speakerID = getSpeakerID(availableSpeakers);

        int talkID = getTalkIDToModify(speakerID);
        if (talkID == -1) {
            textPresenter.printErrorText("Unable to modify a talk. No talk is going to be given by speaker "+speakerID+".");
            return;
        }

        String title = getTitleToModify(talkID);

        speakerID = getSpeakerIDToModify(talkID, availableSpeakers);
        List<Integer> talksSpeaker = speakerManager.getTalksGivenBySpeaker(speakerID);
        if (!talksSpeaker.isEmpty()) {
            List<List<String>> dateTimesSpeaker = talkManager.getDateTimesOfTalks(talksSpeaker);
            textPresenter.printNormalText("The speaker is occupied during:");
            textPresenter.printInfoText(formatDateTimes(dateTimesSpeaker));
        }

        int eventCapacity = getEventCapacityToModify(talkID);

        int roomID = getRoomIDToModify(talkID, eventCapacity);
        List<Integer> talksRoom = roomManager.getTalksGivenInRoom(roomID);
        if (!talksRoom.isEmpty()) {
            List<List<String>> dateTimesRoom = talkManager.getDateTimesOfTalks(talksRoom);
            textPresenter.printNormalText("The room is occupied during:");
            textPresenter.printInfoText(formatDateTimes(dateTimesRoom));
        }

        String date = getDateToModify(talkID);

        String endDate = getEndDateToModify(talkID, date);

        String startingTime = getStartingTimeToModify(talkID);

        String endTime = getEndTimeToModify(talkID, startingTime, date, endDate);

        int tid = adminController.modifyTalk(talkID, title, speakerID, roomID, date, endDate, startingTime, endTime, eventCapacity);

        if (tid < 0) {
            // Time conflict
            if (tid == -5) textPresenter.printErrorText("Unable to modify the talk. The room has a time conflict.");
            else if (tid == -4) textPresenter.printErrorText("Unable to modify the talk. The speaker has a time conflict.");
            else if (tid == -3) textPresenter.printErrorText("Unable to modify the talk. Both the room and speaker have time conflicts.");
                // IOException when saving to file
            else textPresenter.printErrorText("Unable to modify the talk. Failed to save the talk to file.");
        }
        else {
            // Success
            textPresenter.printNormalText("Talk "+tid+" has been modified to:");
            textPresenter.printErrorText(talkManager.printTalk(tid));
        }
    }

    // Methods for modifying party
    private int getPartyIDToModify() {
        List<Integer> parties = talkManager.getAllParties();
        // If no parties, return -1
        if (parties.size() <= 0) {
            return -1;
        }
        Scanner sc = new Scanner(System.in);
        int party;
        while (true) {
            textPresenter.printNormalText("All parties:");
            for (int p : parties) {
                textPresenter.printNormalText("--------------------------------------------------");
                textPresenter.printInfoText(talkManager.printTalk(p));
            }
            textPresenter.printNormalText("--------------------------------------------------");
            textPresenter.printNormalText("Enter the ID of the party to modify: ");
            while (!sc.hasNextInt()) {
                textPresenter.printErrorText("Please enter an integer.");
                textPresenter.printNormalText("All parties:");
                for (int p : parties) {
                    textPresenter.printNormalText("--------------------------------------------------");
                    textPresenter.printInfoText(talkManager.printTalk(p));
                }
                textPresenter.printNormalText("--------------------------------------------------");
                textPresenter.printNormalText("Enter the ID of the party to modify: ");
                sc.next();
            }
            party = sc.nextInt();
            // Ensure party exists
            if (parties.contains(party)) {
                break;
            }
            textPresenter.printErrorText("Please enter a valid party ID.");
        }
        return party;
    }

    /**
     * Interact with the user to modify a party.
     */
    public void modifyParty() {

        int partyID = getPartyIDToModify();
        if (partyID == -1) {
            textPresenter.printErrorText("Unable to modify a party. No party is going to be held.");
            return;
        }

        String title = getTitleToModify(partyID);

        int eventCapacity = getEventCapacityToModify(partyID);

        int roomID = getRoomIDToModify(partyID, eventCapacity);
        List<Integer> talksRoom = roomManager.getTalksGivenInRoom(roomID);
        if (!talksRoom.isEmpty()) {
            List<List<String>> dateTimesRoom = talkManager.getDateTimesOfTalks(talksRoom);
            textPresenter.printNormalText("The room is occupied during:");
            textPresenter.printInfoText(formatDateTimes(dateTimesRoom));
        }

        String date = getDateToModify(partyID);

        String endDate = getEndDateToModify(partyID, date);

        String startingTime = getStartingTimeToModify(partyID);

        String endTime = getEndTimeToModify(partyID, startingTime, date, endDate);

        int pid = adminController.modifyParty(partyID, title, roomID, date, endDate, startingTime, endTime, eventCapacity);

        if (pid < 0) {
            // Time conflict
            if (pid == -3) textPresenter.printErrorText("Unable to modify the party. The room has a time conflict.");
                // IOException when saving to file
            else textPresenter.printErrorText("Unable to modify the party. Failed to save the party to file.");
        }
        else {
            // Success
            textPresenter.printNormalText("Party "+pid+" has been modified to:");
            textPresenter.printSuccessText(talkManager.printTalk(pid));
        }
    }

    // Methods for modifying panel discussion
    private int getPanelDiscussionIDToModify() {
        List<Integer> pds = talkManager.getAllPanelDiscussions();
        // If no panel discussions, return -1
        if (pds.size() <= 0) {
            return -1;
        }
        Scanner sc = new Scanner(System.in);
        int pd;
        while (true) {
            textPresenter.printNormalText("All panel discussions:");
            for (int p : pds) {
                textPresenter.printNormalText("--------------------------------------------------");
                textPresenter.printInfoText(talkManager.printTalk(p));
            }
            textPresenter.printNormalText("--------------------------------------------------");
            textPresenter.printNormalText("Enter the ID of the panel discussion to modify: ");
            while (!sc.hasNextInt()) {
                textPresenter.printErrorText("Please enter an integer.");
                textPresenter.printNormalText("All panel discussions:");
                for (int p : pds) {
                    textPresenter.printNormalText("--------------------------------------------------");
                    textPresenter.printInfoText(talkManager.printTalk(p));
                }
                textPresenter.printNormalText("--------------------------------------------------");
                textPresenter.printNormalText("Enter the ID of the panel discussion to modify: ");
                sc.next();
            }
            pd = sc.nextInt();
            // Ensure party exists
            if (pds.contains(pd)) {
                break;
            }
            textPresenter.printErrorText("Please enter a valid panel discussion ID.");
        }
        return pd;
    }

    /**
     * Interact with the user to modify a panel discussion.
     */
    public void modifyPanelDiscussion() {

        int pdID = getPanelDiscussionIDToModify();
        if (pdID == -1) {
            textPresenter.printErrorText("Unable to modify a panel discussion. No panel discussion is going to be held.");
            return;
        }

        String title = getTitleToModify(pdID);

        int eventCapacity = getEventCapacityToModify(pdID);

        int roomID = getRoomIDToModify(pdID, eventCapacity);
        List<Integer> talksRoom = roomManager.getTalksGivenInRoom(roomID);
        if (!talksRoom.isEmpty()) {
            List<List<String>> dateTimesRoom = talkManager.getDateTimesOfTalks(talksRoom);
            textPresenter.printNormalText("The room is occupied during:");
            textPresenter.printInfoText(formatDateTimes(dateTimesRoom));
        }

        String date = getDateToModify(pdID);

        String endDate = getEndDateToModify(pdID, date);

        String startingTime = getStartingTimeToModify(pdID);

        String endTime = getEndTimeToModify(pdID, startingTime, date, endDate);

        int pid = adminController.modifyPanelDiscussion(pdID, title, roomID, date, endDate, startingTime, endTime, eventCapacity);

        if (pid < 0) {
            // Time conflict
            if (pid == -3) textPresenter.printErrorText("Unable to modify the panel discussion. The room has a time conflict.");
                // IOException when saving to file
            else textPresenter.printErrorText("Unable to modify the panel discussion. Failed to save the panel discussion to file.");
        }
        else {
            // Success
            textPresenter.printNormalText("Panel discussion "+pid+" has been modified to:");
            textPresenter.printSuccessText(talkManager.printTalk(pid));
        }
    }

    /**
     * Interact with the user to schedule a party.
     */
    public void scheduleParty() {
        Map<Integer, String> availableRooms = roomManager.getAllRoomIdsAndNames();

        if (availableRooms.isEmpty()) {
            textPresenter.printErrorText("Unable to schedule a party. No room is available.");
            return;
        }

        // Declaring Party parameters
        String title;
        int roomID;
        String date;
        String endDate;
        String startingTime;
        String endTime;

        // Get values for parameters
        title = getTitle();

        int eventCapacity = geteventCapacity();

        roomID = getSuggestedRoomIds(eventCapacity);
//        roomID = getRoomID(availableRooms);
        List<Integer> partyRoom = roomManager.getTalksGivenInRoom(roomID);
        if (!partyRoom.isEmpty()) {
            List<List<String>> dateTimesRoom = talkManager.getDateTimesOfTalks(partyRoom);
            textPresenter.printNormalText("The room is occupied during:");
            textPresenter.printInfoText(formatDateTimes(dateTimesRoom));
        }

        date = getDate();

        endDate = getEndDate(date);

        startingTime = getStartingTime();

        endTime = getEndTime(startingTime, date, endDate);

        int partyID = adminController.scheduleParty(title, roomID, date, endDate, startingTime, endTime, eventCapacity, loginController.getUserId());

        if (partyID < 0) {
            // Time conflict
            if (partyID == -3) textPresenter.printErrorText("Unable to schedule the party. The room has a time conflict.");
            else textPresenter.printErrorText("Unable to schedule the party. Failed to save the party to file.");
        }
        else {
            // Success
            textPresenter.printSuccessText("The following party has been scheduled:");
            textPresenter.printSuccessText(talkManager.printTalk(partyID));
        }
    }

    /**
     * Interact with the user to schedule a panel discussion.
     */
    public void schedulePanelDiscussion() {
        Map<Integer, String> availableSpeakers = speakerManager.getAllSpeakerIdsAndNames();
        Map<Integer, String> availableRooms = roomManager.getAllRoomIdsAndNames();

        if (availableSpeakers.isEmpty()) {
            textPresenter.printErrorText("Unable to schedule a panel discussion. No speaker is available.");
            return;
        }
        if (availableRooms.isEmpty()) {
            textPresenter.printErrorText("Unable to schedule a panel discussion. No room is available.");
            return;
        }

        // Parameters to construct a talk
        String title;
        int speakerID;
        int roomID;
        String date;
        String endDate;
        String startingTime;
        String endTime;

        // Get values for parameters
        title = getTitle();

        speakerID = getSpeakerID(availableSpeakers);
        List<Integer> talksSpeaker = speakerManager.getTalksGivenBySpeaker(speakerID);
        if (!talksSpeaker.isEmpty()) {
            List<List<String>> dateTimesSpeaker = talkManager.getDateTimesOfTalks(talksSpeaker);
            textPresenter.printNormalText("The speaker is occupied during:");
            textPresenter.printInfoText(formatDateTimes(dateTimesSpeaker));
        }

        int eventCapacity = geteventCapacity();

        roomID = getSuggestedRoomIds(eventCapacity);
//        roomID = getRoomID(availableRooms);
        List<Integer> talksRoom = roomManager.getTalksGivenInRoom(roomID);
        if (!talksRoom.isEmpty()) {
            List<List<String>> dateTimesRoom = talkManager.getDateTimesOfTalks(talksRoom);
            textPresenter.printNormalText("The room is occupied during:");
            textPresenter.printInfoText(formatDateTimes(dateTimesRoom));
        }

        date = getDate();

        endDate = getEndDate(date);

        startingTime = getStartingTime();

        endTime = getEndTime(startingTime, date, endDate);

        int talkID = adminController.schedulePanelDiscussion(title, speakerID, roomID, date, endDate, startingTime, endTime, eventCapacity);

        if (talkID < 0) {
            // Time conflict
            if (talkID == -5) textPresenter.printErrorText("Unable to schedule the panel discussion. The room has a time conflict.");
            else if (talkID == -4) textPresenter.printErrorText("Unable to schedule the panel discussion. The speaker has a time conflict.");
            else if (talkID == -3) textPresenter.printErrorText("Unable to schedule the panel discussion. Both the room and speaker have time conflicts.");
                // IOException when saving to file
            else textPresenter.printErrorText("Unable to schedule the panel discussion. Failed to save the talk to file.");
        }
        else {
            // Success
            textPresenter.printSuccessText("The following panel discussion has been scheduled:");
            textPresenter.printSuccessText(talkManager.printTalk(talkID));
        }
    }

    /**
     * Add a speaker to an existing panel discussion.
     */
    public void addSpeakerToPanelDiscussion() {
        Map<Integer, String> availableSpeakers = speakerManager.getAllSpeakerIdsAndNames();
        List<Integer> availablePanelDiscussions = talkManager.getPanelDiscussionIDs();

        if (availableSpeakers.isEmpty()) {
            textPresenter.printErrorText("Unable to add a speaker. No speaker is available.");
            return;
        }
        if (availablePanelDiscussions.isEmpty()) {
            textPresenter.printErrorText("There are currently no panel discussions scheduled.");
            return;
        }

        int speakerID;
        int panelDiscussionID;

        speakerID = getSpeakerID(availableSpeakers);

        panelDiscussionID = getPanelDiscussionID(availablePanelDiscussions);

        List<Integer> talksSpeaker = speakerManager.getTalksGivenBySpeaker(speakerID);
        if (!talksSpeaker.isEmpty()) {
            List<List<String>> dateTimesSpeaker = talkManager.getDateTimesOfTalks(talksSpeaker);
            textPresenter.printNormalText("The speaker is occupied during:");
            textPresenter.printInfoText(formatDateTimes(dateTimesSpeaker));
        }

        int result = adminController.addSpeakerToPanelDiscussion(speakerID, panelDiscussionID);

        if (result < 0) {
            // Time conflict
            if (result == -1) textPresenter.printErrorText("Unable to add speaker to the panel discussion. The speaker has a time conflict.");
            else textPresenter.printErrorText("Unable to schedule the panel discussion. Failed to save the talk to file.");
        }
        else {
            // Success
            textPresenter.printSuccessText("The specified speaker has been added to the following panel discussion:");
            textPresenter.printSuccessText(talkManager.printTalk(panelDiscussionID));
        }
    }

    // Methods for adding users

    private String getName() {
        Scanner sc = new Scanner(System.in);
        String name;
        textPresenter.printNormalText("Enter user real name: ");
        name = sc.nextLine().trim();
        return name;
    }

    private String getUsername() {
        int lengthLow = 6;
        int lengthHigh = 30;

        Scanner sc = new Scanner(System.in);
        String name;

        // Check duplicate
        textPresenter.printNormalText("Enter username: ");
        name = sc.nextLine().trim();
        while (userGateway.getUserIdByName(name) >= 0) {
            textPresenter.printErrorText("The username is already taken.");
            textPresenter.printNormalText("Enter username: ");
            name = sc.nextLine().trim();
        }

        // Check characters
        boolean isCharsValid = name.matches("[A-Za-z][A-Za-z0-9_]+");

        // Check length
        boolean isLengthValid = name.length() >= lengthLow && name.length() <= lengthHigh;

        while (!(isCharsValid && isLengthValid)) {
            if (!isCharsValid) {
                textPresenter.printErrorText("The username should only contain alphanumeric characters (A-Z, a-z or 0-9)" +
                        " or underscore, and should begin with a letter.");
            }
            if (!isLengthValid) {
                textPresenter.printErrorText("The username should be "+lengthLow+"-"+lengthHigh+" characters long.");
            }

            textPresenter.printNormalText("Enter username: ");
            name = sc.nextLine().trim();
            while (userGateway.getUserIdByName(name) >= 0) {
                textPresenter.printErrorText("The username is already taken.");
                textPresenter.printNormalText("Enter username: ");
                name = sc.nextLine().trim();
            }

            isCharsValid = name.matches("[A-Za-z][A-Za-z0-9_]+");

            isLengthValid = name.length() >= lengthLow && name.length() <= lengthHigh;
        }

        return name;
    }

    private String getPassword() {
        int lengthLow = 10;
        int lengthHigh = 30;

        Scanner sc = new Scanner(System.in);
        String password;
        textPresenter.printNormalText("Enter password: ");
        password = sc.nextLine().trim();

        // Check length
        boolean isLengthValid = password.length() >= lengthLow && password.length() <= lengthHigh;
        while (!isLengthValid) {
            textPresenter.printErrorText("The password should be "+lengthLow+"-"+lengthHigh+" characters long.");

            textPresenter.printNormalText("Enter password: ");
            password = sc.nextLine().trim();

            isLengthValid = password.length() >= lengthLow && password.length() <= lengthHigh;
        }

        return password;
    }

    // Methods for adding room
    private String getRoomName() {
        Scanner sc = new Scanner(System.in);
        String name;
        textPresenter.printNormalText("Enter room name: ");
        name = sc.nextLine().trim();
        while (roomManager.isNameExisting(name)) {
            textPresenter.printErrorText("The name of the room is already taken.");
            textPresenter.printNormalText("Enter room name: ");
            name = sc.nextLine().trim();
        }
        return name;
    }

    private int getRoomCapacity() {
        Scanner sc = new Scanner(System.in);
        int capacity;
        while (true) {
            textPresenter.printNormalText("Enter room capacity: ");
            while (!sc.hasNextInt()) {
                textPresenter.printErrorText("Please enter an integer.");
                textPresenter.printNormalText("Enter room capacity: ");
                sc.next();
            }
            capacity = sc.nextInt();
            // Ensure capacity is positive
            if (capacity > 0) {
                break;
            }
            textPresenter.printErrorText("Please enter a positive integer.");
        }
        return capacity;
    }

    private int geteventCapacity() {
        Scanner sc = new Scanner(System.in);
        int capacity;
        int biggestRoomCapacity = roomManager.biggestRoomCapacity();
        while (true) {
            textPresenter.printNormalText("Enter maximum number of people in your event: ");
            while (!sc.hasNextInt()) {
                textPresenter.printErrorText("Please enter an integer.");
                textPresenter.printNormalText("Enter event capacity: ");
                sc.next();
            }

            capacity = sc.nextInt();
            while(!roomManager.hasBiggerRoom(capacity)){
                textPresenter.printErrorText("The capacity of the largest room in our system is "+biggestRoomCapacity);
                textPresenter.printNormalText("Enter event capacity: ");
                capacity = sc.nextInt();
            }

            // Ensure capacity is positive
            if (capacity > 0) {
                break;
            }
            textPresenter.printErrorText("Please enter a positive integer.");
        }
        return capacity;
    }

    private List<String> getTechRequirements() {
        Scanner sc = new Scanner(System.in);
        List<String> TechRequirements = new ArrayList<>();
        String options;
        textPresenter.printNormalText("Please choose the technology requirements(enter corresponding alphabet without other symbols):");
        textPresenter.printNormalText("A: Projector and Screen");
        textPresenter.printNormalText("B: Sound System");
        textPresenter.printNormalText("C: Podium and Microphone");
        textPresenter.printNormalText("D: High Speed Wired and Wireless Internet");
        textPresenter.printNormalText("E: LED Monitor");
        textPresenter.printNormalText("Enter options(alphabetical order without space or any symbol): ");
        options = sc.nextLine().trim();
        while (!isMultipleOptionsValid(options)) {
            textPresenter.printErrorText("Illegal input(alphabetical order without space or any symbol).");
            textPresenter.printNormalText("Enter options: ");
            options = sc.nextLine().trim();
        }
        char[] options_char =  options.toCharArray();
        for (int i=0; i<options_char.length;i++) {
            switch (options_char[i]){
                case 'A':
                    TechRequirements.add("Projector and Screen");
                    break;
                case 'B':
                    TechRequirements.add("Sound System");
                    break;
                case 'C':
                    TechRequirements.add("High Speed Wired and Wireless Internet");
                    break;
                case 'D':
                    TechRequirements.add("Podium and Microphone");
                    break;
                case 'E':
                    TechRequirements.add("LED Monitor");
                    break;
            }
        }

        return TechRequirements;
    }

    private List<String> getRoomConditions() {
        Scanner sc = new Scanner(System.in);
        List<String> roomConditions = new ArrayList<>();
        String options;
        textPresenter.printNormalText("Please choose the room conditions(enter corresponding alphabet without other symbols):");
        textPresenter.printNormalText("A: Flexible Room Arrangement");
        textPresenter.printNormalText("B: Bar Counter");
        textPresenter.printNormalText("C: Dinning Area");
        textPresenter.printNormalText("D: Double-height Window and City View");
        textPresenter.printNormalText("E: Sound Proofing");
        textPresenter.printNormalText("Enter options(alphabetical order without space or any symbol): ");
        options = sc.nextLine().trim();
        while (!isMultipleOptionsValid(options)) {
            textPresenter.printErrorText("Illegal input(alphabetical order without space or any symbol).");
            textPresenter.printNormalText("Enter options: ");
            options = sc.nextLine().trim();
        }
        char[] options_char =  options.toCharArray();
        for (int i=0; i<options_char.length;i++) {
            switch (options_char[i]){
                case 'A':
                    roomConditions.add("Flexible Room Arrangement");
                    break;
                case 'B':
                    roomConditions.add("Bar Counter");
                    break;
                case 'C':
                    roomConditions.add("Dinning Area");
                    break;
                case 'D':
                    roomConditions.add("Double-height Window and City View");
                    break;
                case 'E':
                    roomConditions.add("Sound Proofing");
                    break;
            }
        }

        return roomConditions;
    }

    // Methods for scheduling talk
    private String getTitle() {
        Scanner sc = new Scanner(System.in);
        String title;
        textPresenter.printNormalText("Enter title: ");
        title = sc.nextLine().trim();
        return title;
    }

    private int getSpeakerID(Map<Integer, String> availableSpeakers) {
        Scanner sc = new Scanner(System.in);
        int speakerID;
        while (true) {
            textPresenter.printNormalText("All speakers:");
            textPresenter.printInfoText(formatIdNames(availableSpeakers));
            textPresenter.printNormalText("Enter speaker ID: ");
            while (!sc.hasNextInt()) {
                textPresenter.printErrorText("Please enter an integer.");
                textPresenter.printNormalText("All speakers:");
                textPresenter.printInfoText(formatIdNames(availableSpeakers));
                textPresenter.printNormalText("Enter speaker ID: ");
                sc.next();
            }
            speakerID = sc.nextInt();
            // Ensure speaker exists
            if (availableSpeakers.containsKey(speakerID)) {
                break;
            }
            textPresenter.printErrorText("Please enter a valid speaker ID.");
        }
        return speakerID;
    }

    private int getPanelDiscussionID(List<Integer> availablePanelDiscussions) {
        Scanner sc = new Scanner(System.in);
        int panelDiscussionID;
        while (true) {
            textPresenter.printInfoText("All panel discussions:");
            for (int pid : availablePanelDiscussions) {
                textPresenter.printDividerLine();
                textPresenter.printInfoText(talkManager.printTalk(pid));
            }
            textPresenter.printDividerLine();
            textPresenter.printNormalText("Enter panel discussion ID: ");
            while (!sc.hasNextInt()) {
                textPresenter.printErrorText("Please enter an integer.");
                textPresenter.printInfoText("All panel discussions:");
                for (int pid : availablePanelDiscussions) {
                    textPresenter.printDividerLine();
                    textPresenter.printInfoText(talkManager.printTalk(pid));
                }
                textPresenter.printDividerLine();
                textPresenter.printNormalText("Enter panel discussion ID: ");
                sc.next();
            }
            panelDiscussionID = sc.nextInt();
            // Ensure speaker exists
            if (availablePanelDiscussions.contains(panelDiscussionID)) {
                break;
            }
            textPresenter.printErrorText("Please enter a valid panel discussion ID.");
        }
        return panelDiscussionID;
    }

    private int getRoomID(Map<Integer, String> availableRooms) {
        Scanner sc = new Scanner(System.in);
        int roomID;
        while (true) {
            textPresenter.printNormalText("All rooms:");
            textPresenter.printInfoText(formatIdNames(availableRooms));
            textPresenter.printNormalText("Enter Room ID: ");
            while (!sc.hasNextInt()) {
                textPresenter.printErrorText("Please enter an integer.");
                textPresenter.printNormalText("All rooms:");
                textPresenter.printInfoText(formatIdNames(availableRooms));
                textPresenter.printNormalText("Enter Room ID: ");
                sc.next();
            }
            roomID = sc.nextInt();
            if (availableRooms.containsKey(roomID)) {
                break;
            }
            textPresenter.printErrorText("Please enter a valid room ID.");
        }
        return roomID;
    }

    private int getSuggestedRoomIds(int eventCapacity) {

        Scanner sc = new Scanner(System.in);
        int roomID;

        List<String> techRequirements = getTechRequirements();
        List<String> roomConditions = getRoomConditions();


        List<Integer> suggestedRooms = roomManager.getSuggestedRoomIds(eventCapacity, techRequirements,roomConditions);
        while(suggestedRooms.size()==0){
            textPresenter.printErrorText("There seems no rooms meet your requirements");
            if(roomManager.maxTechRequirementsRoom(eventCapacity) != -1){
                textPresenter.printInfoText("The room that has most technology requirements based on your event capacity:");
                textPresenter.printSuccessText(roomManager.printRoom(roomManager.maxTechRequirementsRoom(eventCapacity)));
            }else{
                textPresenter.printInfoText("All rooms in the system do not have any technology requirements so far.");
            }

            if(roomManager.maxRoomConditionsRoom(eventCapacity) != -1){
                textPresenter.printInfoText("The room that has most room conditions based on your event capacity:");
                textPresenter.printSuccessText(roomManager.printRoom(roomManager.maxRoomConditionsRoom(eventCapacity)));
            }else{
                textPresenter.printInfoText("All rooms in the system do not have any room conditions so far.");
            }
            techRequirements = getTechRequirements();
            roomConditions = getRoomConditions();
            suggestedRooms = roomManager.getSuggestedRoomIds(eventCapacity, techRequirements,roomConditions);
        }
            if(suggestedRooms.size()>0){
                textPresenter.printNormalText("Suggested rooms which meet your requirements:");
                for (int i=0;i<suggestedRooms.size();i++){
                    textPresenter.printSuccessText(roomManager.printRoom(suggestedRooms.get(i)));
                }
            }
            textPresenter.printNormalText("Enter Room ID: ");
            while (!sc.hasNextInt()) {
                textPresenter.printErrorText("Please enter an integer.");
                textPresenter.printNormalText("Suggested rooms which meet your requirements");
                for (int i=0;i<suggestedRooms.size();i++){
                    textPresenter.printSuccessText(roomManager.printRoom(suggestedRooms.get(i)));
                }
                textPresenter.printNormalText("Enter Room ID: ");
                sc.next();
            }
            roomID = sc.nextInt();
            while(!suggestedRooms.contains(roomID)){
                textPresenter.printErrorText("Please enter a valid room ID.");
                textPresenter.printNormalText("Enter Room ID: ");
                roomID = sc.nextInt();
            }

        return roomID;
    }

    private String getDate() {
        Scanner sc = new Scanner(System.in);
        String date;
        textPresenter.printNormalText("Enter starting date in YYYY-MM-DD: ");
        date = sc.nextLine().trim();
        while (!isDateValid(date)) {
            textPresenter.printErrorText("Please enter a valid date.");
            textPresenter.printNormalText("Enter starting date in YYYY-MM-DD: ");
            date = sc.nextLine().trim();
        }
        return date;
    }

    private String getEndDate(String startingDate) {
        Scanner sc = new Scanner(System.in);
        String endDate;
        textPresenter.printNormalText("Enter end date in YYYY-MM-DD: ");
        endDate = sc.nextLine().trim();
        while (!(isDateValid(endDate) && isDate1BeforeDate2(startingDate, endDate))) {
            textPresenter.printErrorText("Please enter a valid end date.");
            textPresenter.printNormalText("Enter end date in YYYY-MM-DD: ");
            endDate = sc.nextLine().trim();
        }
        return endDate;
    }

    private String getStartingTime() {
        Scanner sc = new Scanner(System.in);
        String startingTime;
        textPresenter.printNormalText("Enter starting time in HH:mm between "+lowerBoundTime+" and "+higherBoundTime+": ");
        startingTime = sc.nextLine().trim();
        while (!isStartingTimeValid(startingTime, lowerBoundTime, higherBoundTime)) {
            textPresenter.printErrorText("Please enter a valid starting time.");
            textPresenter.printNormalText("Enter starting time in HH:mm between "+lowerBoundTime+" and "+higherBoundTime+": ");
            startingTime = sc.nextLine().trim();
        }
        return startingTime;
    }

    private String getEndTime(String startingTime, String startingDate, String endDate) {
        Scanner sc = new Scanner(System.in);
        if (startingDate.equals(endDate)) {
            String endTime;
            textPresenter.printNormalText("Enter end time in HH:mm between "+lowerBoundTime+" and "+higherBoundTime+": ");
            endTime = sc.nextLine().trim();
            while (!isEndTimeValid(endTime, startingTime, lowerBoundTime, higherBoundTime)) {
                textPresenter.printErrorText("Please enter a valid end time.");
                textPresenter.printNormalText("Enter end time in HH:mm between "+lowerBoundTime+" and "+higherBoundTime+": ");
                endTime = sc.nextLine().trim();
            }
            return endTime;
        }
        else {
            String endTime;
            textPresenter.printNormalText("Enter end time in HH:mm between "+lowerBoundTime+" and "+higherBoundTime+": ");
            endTime = sc.nextLine().trim();
            while (!endTime.matches("^([0-1][0-9]|2[0-3]):([0-5][0-9])$") || !isTimeInRange(endTime, lowerBoundTime, higherBoundTime)) {
                textPresenter.printErrorText("The end time should be between "+lowerBoundTime+" and "+higherBoundTime+".");
                textPresenter.printErrorText("The end time must be in HH:mm format and must be a possible time.");
                textPresenter.printNormalText("Enter end time in HH:mm between "+lowerBoundTime+" and "+higherBoundTime+": ");
                endTime = sc.nextLine().trim();
            }
            return endTime;
        }
    }

    public void cancelEvent(){
        //Check if this organizer has constructed any event
        List<NullSpeakerTalk> orgEvents = talkManager.getOrgEvents(loginController.getUserId());
        List<Integer> orgEventsIds = new ArrayList<>();
        for (NullSpeakerTalk talk: orgEvents){
            orgEventsIds.add(talk.getId());
        }

        if(orgEvents.size()<1){
            textPresenter.printErrorText("You haven't add any events so far.");
        }else{
            List<List<String>> orgTalks = talkManager.getOrgTalkRows(orgEvents);
            //Display all the events that has constructed by the current organizer
            textPresenter.printSuccessText("The following events have not yet been held and you could cancel now.");
//            textPresenter.printNormalText();("------------------------------------------------------------------------------------------------------------------\n");
            List<String> header = Arrays.asList("Event ID", "Event Title","Type","Speaker ID(s)", "Start Date", "End Date",
                    "Start Time", "End Time", "Room ID", "Capacity");
            tableGenerator.printAsTable(header, orgTalks);
//            textPresenter.printNormalText();("------------------------------------------------------------------------------------------------------------------");

            //Get user input
            int eventIdToDelete = getEventIdToDelete(orgEventsIds);

            //Get the information of the event to cancel
            String eventToDeleteInfo = talkManager.printTalk(eventIdToDelete);

            //Delete the event
            Map<String, Integer> res = adminController.deleteEventById(eventIdToDelete);

            int deleteTalkRes = res.get("Event");
            int deleteSpeakerRes = res.get("Speaker");
            int deleteRoomRes = res.get("Room");
            int deleteAttendeeRes = res.get("Attendee");
            int deleteSavetofile = res.get("savetofile");

            if(deleteTalkRes<0){
                textPresenter.printErrorText("Failed to cancel event: fail to cancel the event from database.");
            } else if(deleteSpeakerRes<0){
                textPresenter.printErrorText("Failed to cancel event: fail to delete the corresponding speaker from database.");
            } else if(deleteRoomRes<0){
                textPresenter.printErrorText("Failed to cancel event: fail to delete the corresponding room from database.");
            } else if(deleteAttendeeRes<0){
                textPresenter.printErrorText("Failed to cancel event: fail to delete the corresponding attendees from database.");
            } else if(deleteSavetofile<0){
                textPresenter.printErrorText("Failed save the file.");
            } else {
                textPresenter.printSuccessText("Delete the following talk successfully!");
                textPresenter.printNormalText(eventToDeleteInfo);
            }

        }

    }


    private int getEventIdToDelete(List<Integer> orgEventsIds){
        Scanner sc = new Scanner(System.in);
        int eventIdToDelete;
        while(true){
            textPresenter.printNormalText("Please enter the event ID you want to delete: ");
            while (!sc.hasNextInt()) {
                textPresenter.printErrorText("Please enter an integer.");
                textPresenter.printNormalText("Enter event ID: ");
                sc.next();
            }
            eventIdToDelete = sc.nextInt();
            while(!orgEventsIds.contains(eventIdToDelete)){
                textPresenter.printErrorText("Please enter a valid event ID.");
                textPresenter.printNormalText("Enter Room ID: ");
                eventIdToDelete = sc.nextInt();
            }
            if (eventIdToDelete >= 0) {
                break;
            }

        }
        return eventIdToDelete;
    }





    // ****************************************
    // Helpers
    // ****************************************
    private String formatIdNames(Map<Integer, String> idNames) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, String> entry : idNames.entrySet()) {
            sb.append("- ID: ").append(entry.getKey()).append(", Name: ").append(entry.getValue()).append("\n");
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    private String formatDateTimes(List<List<String>> dateTimes) {
        StringBuilder sb = new StringBuilder();
        for (List<String> dt : dateTimes) {
            sb.append("- ").append(formatDateTime(dt.get(0))).append(" to ").append(formatDateTime(dt.get(1))).append("\n");
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    private String formatDateTime(String dt) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dt.length(); i++) {
            char c = dt.charAt(i);
            if (i == 4 || i == 6) {
                sb.append('-').append(c);
            }
            else if (i == 8) {
                sb.append(' ').append(c);
            }
            else if (i == 10) {
                sb.append(':').append(c);
            }
            else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private boolean isDateValid(String dateStr) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            df.setLenient(false);
            df.parse(dateStr);

            String[] ymd = dateStr.split("-");

            LocalDate today = LocalDate.now();
            int year = today.getYear();
            int month = today.getMonthValue();
            int day = today.getDayOfMonth();

            boolean flag;
            if (Integer.parseInt(ymd[0]) > year) {
                flag = true;
            }
            else if (Integer.parseInt(ymd[0]) == year) {
                if (Integer.parseInt(ymd[1]) > month) {
                    flag = true;
                }
                else if (Integer.parseInt(ymd[1]) == month) {
                    flag = Integer.parseInt(ymd[2]) >= day;
                }
                else {
                    flag = false;
                }
            }
            else {
                flag = false;
            }

            if (!flag) {
                textPresenter.printErrorText("The date cannot be before current date.");
            }

            return flag;
        } catch (ParseException e) {
            textPresenter.printErrorText("The date must be in YYYY-MM-DD format and must be a possible date.");
            return false;
        }
    }

    // TODO: when date is today, check if time is after current time
    private boolean isStartingTimeValid(String startingTime, String lowerBound, String higherBound) {
        if (startingTime.matches("^([0-1][0-9]|2[0-3]):([0-5][0-9])$")) {
            boolean isInRange = isTimeInRange(startingTime, lowerBound, higherBound);
            if (!isInRange) {
                textPresenter.printErrorText("The starting time should be between "
                        +lowerBoundTime+" and "+higherBoundTime+".");
            }
            return isInRange;
        }
        textPresenter.printErrorText("The starting time must be in HH:mm format and must be a possible time.");
        return false;
    }

    private boolean isEndTimeValid(String endTime, String startingTime,
                                   String lowerBound, String higherBound) {
        if (endTime.matches("^([0-1][0-9]|2[0-3]):([0-5][0-9])$")) {
            boolean isInRange = isTimeInRange(endTime, lowerBound, higherBound);
            boolean isAfterStartingTime = isT1BeforeT2(startingTime, endTime);
            if (!isInRange) {
                textPresenter.printErrorText("The end time should be between "
                        +lowerBoundTime+" and "+higherBoundTime+".");
            }
            if (!isAfterStartingTime) {
                textPresenter.printErrorText("The end time should be after the starting time.");
            }
            return isInRange && isAfterStartingTime;
        }
        textPresenter.printErrorText("The end time must be in HH:mm format and must be a possible time.");
        return false;
    }

    private boolean isTimeInRange(String timeStr, String lowerBound, String higherBound) {
        return isT1BeforeT2(timeStr, higherBound) &&
                isT1BeforeT2(lowerBound, timeStr);
    }

    private boolean isT1BeforeT2(String t1Str, String t2Str) {
        String t1 = "1" + String.join("", t1Str.split(":"));
        String t2 = "1" + String.join("", t2Str.split(":"));

        return Integer.parseInt(t1) <= Integer.parseInt(t2);
    }

    private boolean isDate1BeforeDate2(String date1, String date2) {
        String d1 = String.join("", date1.split("-"));
        String d2 = String.join("", date2.split("-"));

        return Integer.parseInt(d1) <= Integer.parseInt(d2);
    }

    private boolean isMultipleOptionsValid(String options){
        String regex = "A?B?C?D?E?";
        if (options.matches(regex)){
            return true;
        } else {
            return false;
        }
    }

}
