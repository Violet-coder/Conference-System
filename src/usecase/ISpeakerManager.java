package usecase;

import java.util.List;
import java.util.Map;

public interface ISpeakerManager {

    /**
     *
     * @param speakerID    The ID of the speaker.
     * @return             The IDs of all the talks given by the speaker.
     */
    List<Integer> getTalksGivenBySpeaker(int speakerID);

    /**
     *
     * @return         The list of all speaker ids
     */
    List<Integer> getAllSpeakers();

    // SpeakerManager should have a collection of all speakers
    // add a new speaker to the collection of speakers
    // return the id of the added speaker
    // if unable to add speaker (due to duplicate username, since username has to be unique) return -1
    int addSpeaker(String username, String password, String name);

    /**
     * Print the info of a speaker (using Speaker class's toString() method).
     * @param id        ID of speaker
     */
    String printSpeaker(int id);

    /**
     * Add a the ID of a talk to a speaker's talksToGive list
     * @param speakerID     ID of speaker
     * @param talkID        ID of talk
     * @return              True if successfully added talkID to speaker's talksToGive list
     *                      False if talkID already exists in speaker's talksToGive list
     */
    boolean addTalkToSpeaker(int speakerID, int talkID);

    /**
     * Get the ID and name of each speaker.
     * @return          A map containing key-value pairs of ID and name for all speakers.
     */
    Map<Integer, String> getAllSpeakerIdsAndNames();

    /**
     * Return the name of speaker with given speaker ID;
     * @return          name of speaker
     * */
    String getSpeakerNameByID(int speakerID);

    /**
     * Check if a username is already taken by an existing speaker.
     * @param username      The username.
     * @return              True if taken, false otherwise.
     */
    boolean isUsernameExisting(String username);

    /**
     * Assign talk from its original speaker to a new speaker.
     * @param speakerID        id of new speaker
     * @param talkID           id of talk
     */
    public void modifyTalkSpeaker(int speakerID, int talkID);

    /**
     * Cancel a talk for a speaker.
     * @param speakerID the speaekr id
     * @param talkID the event id
     * @return if cancel successfully, return the speaker id, else return -1 for failure
     * */
    int cancelTalkForSpeaker(int speakerID, int talkID);
}
