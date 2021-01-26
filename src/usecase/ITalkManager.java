package usecase;

import entity.NullSpeakerTalk;

import java.util.List;

public interface ITalkManager {

    /**
     *
     * @param talk     The IDs of some talks.
     * @return          The starting and end datetimes of each talk.
     *                  Each inner list contains the starting and end datetimes of a unique talk.
     *                  e.g. There are two talks, talk1 starts at 2020-11-04 09:30 and ends at
     *                       2020-11-04 13:30, talk2 starts at 2020-11-05 14:00 and ends at
     *                       2020-11-05 16:00. For these two talks, the method returns a list as below:
     *                       [["202011040930", "202011041330"], ["202011051400", "202011051600"]]
     */
    List<List<String>> getDateTimesOfTalks(List<Integer> talk);


    /**
     *
     * @param talk     The ID of a talk.
     * @return         The list of all attendee ids
     */
    List<Integer> getAttendees(List<Integer> talk);

    /**
     * Add a talk to the collection of talks in the talk manager.
     * @param title             Title of talk
     * @param speakerID         Speaker ID of talk
     * @param date              Date of talk
     * @param startingTime      Starting time of talk
     * @param endTime           End time of talk
     * @param roomID            Room ID of talk
     * @param eventCapacity     Event capacity of talk
     * @param OrgId             Organizer ID of talk
     * @return                  ID of talk
     */
    int addTalk(String title, int speakerID, String date, String endDate,
                String startingTime, String endTime, int roomID, int eventCapacity, int OrgId);

    /**
     * Add a party to the collection of talks in the talk manager.
     * @param title             Title of talk
     * @param date              Date of talk
     * @param startingTime      Starting time of talk
     * @param endTime           End time of talk
     * @param roomID            Room ID of talk
     * @param eventCapacity     Event capacity of talk
     * @param OrgId             Organizer ID of talk
     * @return                  ID of talk
     */
    int addParty(String title, String date, String endDate, String startingTime, String endTime, int roomID, int eventCapacity, int OrgId);

    /**
     * Add a panel discussion to the collection of talks in the talk manager.
     * @param title             Title of talk
     * @param speakerID         Speaker ID of talk
     * @param date              Date of talk
     * @param startingTime      Starting time of talk
     * @param endTime           End time of talk
     * @param roomID            Room ID of talk
     * @param eventCapacity     Event capacity of talk
     * @param OrgId             Organizer ID of talk
     * @return                  ID of talk
     */
    int addPanelDiscussion(String title, int speakerID, String date, String endDate,
                       String startingTime, String endTime, int roomID, int eventCapacity, int OrgId);


    /**
     * Adds the given speaker ID to the list of speakers in the panel discussion
     * with the given talkID
     * @param speakerID         ID of the speaker being added
     * @param talkID            ID of the panel discussion the speaker is added to
     */
    void addSpeakerToPanelDiscussion(int speakerID, int talkID);

    /**
     * Print the info of a talk (using Talk class's toString() method).
     * @param id        ID of talk
     * @return          String containing talk info
     */
    String printTalk(int id);

    /**
     * Check if a user with specified user ID has signed up for a talk. Return false if the
     * talk is not existing.
     * @param userID          ID of user
     * @param talkID          ID of talk
     * @return                true for user has signed up for the talk; otherwise false
     * */
    boolean checkIfUserSingedUpForATalk(int userID, int talkID);

    /**
     * Remove an attendee from the attendee list of the talk in the collection of talks.
     * @param userId      ID of the user
     * @param talkId      ID of the talk
     * */
    boolean addAttendeeToATalk(int userId, int talkId);

    /**
    * Remove an attendee from the attendee list of the talk in the collection of talks.
    * If the userID doesn't exist in the talk's attendees list or the talk ID doesn't exist, return false.
    * @param userId      ID of the user
    * @param talkId      ID of the talk
    * */
    boolean removeAttendeeFromATalk(int userId, int talkId);

    /**
     * Get the number of attendee for a specified talk.
     * @param talkID        ID of talk
     * @return              the attendee ID if add the attendee successfully;
     *                      otherwise return -1 if the username already exists
     * */
    int getNumAttendeesByTalkID(int talkID);

    /**
     * Get the room ID for a particular talk by given talk ID. Return -1 if the talk doesn't exist.
     * @param talkID        ID of the talk
     * @return              the room ID of the talk
     * */
    int getRoomIDByTalkID(int talkID);

    /**
     * Check if a talk exists with the talk ID.
     * @param talkID        ID of talk
     * @return              true for talk existing; otherwise false
     * */
    boolean isTalkExisting(int talkID);

    /**
     * Get a list of talk ids that hasn't expired.
     * @return              list of talk IDs
     * */
    List<Integer> getUnexpiredTalkIDs();

    /**
     * Gets a list of panel discussion IDs
     * @return              list of panel discussion IDs
     */
    List<Integer> getPanelDiscussionIDs();

    /**
     * Return the speaker id with given talk id.
     * @param id    the id of talk
     * @return  the speaker id who gives the talk
     * */
    int getSpeakerIDByTalkID(int id);

    /**
     * Return a string containing all the talk schedule related information,
     * including the talk id, date and time, and talk title.
     * @param talkID the talk id
     * @return a string containing information of the talk
     * */
    String getTalkSchedule(int talkID);

    /**
     * Return the talk title with the given talk id.
     * @param talkID    the id of the talk
     * @return  the title of the talk
     * */
    public String getTitleByTalkID(int talkID);

    /**
     * Return the date of the talk with the given talk id.
     * @param talkID    the id of the talk
     * @return  the date of the talk
     * */
    public String getDateByTalkID(int talkID);

    /**
     * Return the end date of the talk with the given talk id.
     * @param talkID    the id of the talk
     * @return  the end date of the talk
     * */
    public String getEndDateByTalkID(int talkID);

    /**
     * Return the starting time of the talk with the given talk id.
     * @param talkID    the id of the talk
     * @return  the starting time of the talk
     * */
    public String getStartingTimeByTalkID(int talkID);

    /**
     * Return the end time of the talk with the given talk id.
     * @param talkID    the id of the talk
     * @return  the end time of the talk
     * */
    public String getEndTimeByTalkID(int talkID);

    /**
     * Return the capacity of the talk with the given talk id.
     * @param talkID    the id of the talk
     * @return  the capacity of the talk
     * */
    public int getEventCapacityByTalkID(int talkID);

    /**
     * Modify the talk with the given id.
     * @param talkID                the id of the talk to be modified
     * @param title                 the new title of the talk
     * @param speakerID             the new speaker id of the talk
     * @param date                  the new date of the talk
     * @param startingTime          the new starting time of the talk
     * @param endTime               the new end time of the talk
     * @param roomID                the new room id of the talk
     * @param eventCapacity         the max number of attendees of the talk
     * @return                      the id of the talk that has been modified on success
     *                              -1 otherwise
     */
    public int modifyTalk(int talkID, String title, int speakerID, String date, String endDate, String startingTime, String endTime, int roomID, int eventCapacity);

    /**
     * Modify the party with the given id.
     * @param partyID                the id of the party to be modified
     * @param title                 the new title of the party
     * @param date                  the new date of the party
     * @param startingTime          the new starting time of the party
     * @param endTime               the new end time of the party
     * @param roomID                the new room id of the party
     * @param eventCapacity         the max number of attendees of the party
     * @return                      the id of the party that has been modified on success
     *                              -1 otherwise
     */
    public int modifyParty(int partyID, String title, String date, String endDate, String startingTime, String endTime, int roomID, int eventCapacity);

    /**
     * Modify the panel discussion with the given id.
     * @param panelDiscussionID     the id of the panel discussion to be modified
     * @param title                 the new title of the panel discussion
     * @param date                  the new date of the panel discussion
     * @param startingTime          the new starting time of the panel discussion
     * @param endTime               the new end time of the panel discussion
     * @param roomID                the new room id of the panel discussion
     * @param eventCapacity         the max number of attendees of the panel discussion
     * @return                      the id of the panel discussion that has been modified on success
     *                              -1 otherwise
     */
    public int modifyPanelDiscussion(int panelDiscussionID, String title, String date, String endDate, String startingTime, String endTime, int roomID, int eventCapacity);

    /**
     * Get ids of all events on the specified date.
     * @param date      Date in "YYYY-MM-DD" format.
     * @return          List of ids of all events on the specified date.
     */
    List<Integer> getEventsByDate(String date);

    /**
     * Get ids of all events with the specified starting and end times.
     * @param startTime         Starting time in "hh:mm" format.
     * @param endTime           End time in "hh:mm" format.
     * @return                  List of ids of all events with the specified starting and end times.
     */
    List<Integer> getEventsByTime(String startTime, String endTime);

    /**
     * Get ids of all events with the specified title.
     * @param title      Title of the events.
     * @return           List of ids of all events with the specified title.
     */
    List<Integer> getEventsByTitle(String title);

    /**
     * Get id(s) of all speaker(s) of the event with the specified id.
     * @param talkID      Id of the event.
     * @return            List of id(s) of all speaker(s) of the event with the specified id.
     */
    List<Integer> getSpeakerIDListByTalkID(int talkID);

    /**
     * Get ids of all parties.
     * @return      List of ids of all parties.
     */
    List<Integer> getAllParties();

    /**
     * Get ids of all panel discussions.
     * @return      List of ids of all panel discussions.
     */
    List<Integer> getAllPanelDiscussions();

    /**
     * Get the list of ids of the unexpired events that constructed by the specific organizer
     * @param orgId                the id of organizer
     * @return                     the list of unexpired talks constructed by this organizer
     */
    List<NullSpeakerTalk> getOrgEvents(int orgId);

    /**
     * Format the information of the events into rows
     * @param orgEvents            the list of unexpired talks constructed by this organizer
     * @return                     a embedded list of the information of all the events
     */
    List<List<String>> getOrgTalkRows(List<NullSpeakerTalk> orgEvents);

    /**
     * Delete the talk from the talk manager.
     * @param eventId            the id of the event that need to be deleted
     * @return                   the id of the event that need to be deleted if it is deleted successfully
     *                           -1 if it was not deleted successfully
     */
    int deleteEventByID(int eventId);


    /**
     * Get the id of the attendees who have signed up the event.
     * @param eventId            the id of the event that need to be deleted
     * @return                   the list of ids of the attendee who have signed up the event
     */
    List<Integer> getAttendeesOftheEvent(int eventId);

    /**
     * Get a list containing the event schedule info with event ids.
     * @param talkIDs a list of talk ids
     * @return the list of talk schedule
     * */
    List<List<String>> getTalkScheduleRowsByIDs(List<Integer> talkIDs);

    List<Integer> filterTalksFromIds(List<Integer> events);

}
