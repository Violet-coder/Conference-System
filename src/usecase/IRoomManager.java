package usecase;

import entity.Room;

import java.util.List;
import java.util.Map;

public interface IRoomManager {

    /**
     *
     * @param roomID        The ID of the room.
     * @return              The IDs of all the talks given in the room.
     */
    List<Integer> getTalksGivenInRoom(int roomID);

    /**
     *
     * @return      The map containing all pairs of room id and corresponding room.
     */
    Map<Integer, Room> getAllRooms();

    /**
     * Get the room capacity with specified room ID.
     * @param roomID        ID of room
     * @return              the room capacity
     * */
    int getRoomCapacityByID(int roomID);

    /**
     * Create a new Room with specified name and capacity and add the room to the collection of
     * rooms in the RoomManager.
     * Return -1 if the room name duplicates and won't create a new room.
     * @param name      name of the room
     * @param capacity  capacity of the room
     * @return          return the room ID if add the room successfully;
     *                  return -1 if the room name already exists
     * */

    int addRoom(String name, int capacity, List<String> techRequirements, List<String> roomConditions);

    /**
     * Print the info of a room (using Room class's toString() method).
     * @param id        ID of room
     */
    String printRoom(int id);

    /**
     * Get the ID and name of each room.
     * @return          A map containing key-value pairs of ID and name for all rooms.
     */
    Map<Integer, String> getAllRoomIdsAndNames();

    /**
     * Return the name of room with given room ID;
     * @param roomID    ID of room
     * @return          name of room
     * */
    String getRoomNameByID(int roomID);

    /**
     * Add a talk to a room where the talk will take place.
     * @param roomID        The id of the room.
     * @param talkID        The id of the talk.
     * @return              False if talk already added to room, true otherwise.
     */
    boolean addTalkToRoom(int roomID, int talkID);

    /**
     * Check if a name is already taken by an existing room.
     * @param name          The name.
     * @return              True if taken, false otherwise.
     */
    boolean isNameExisting(String name);


    /**
     * get rooms which meet the requirements of user's tech requirements and room conditions.
     * @param capacity          the capacity
     * @param techRequirements  the list of the tech requirements
     * @param roomConditions    the list of room conditions
     * @return                  list of IDs of rooms which meet the requirements
     */
    List<Integer> getSuggestedRoomIds(int capacity, List<String> techRequirements, List<String> roomConditions);


    /**
     * Check if there is a room whose capacity is bigger than the user input in the database.
     * @param capacity      The capacity entered by the user.
     * @return              True if we have larger room, false if not.
     */
    boolean hasBiggerRoom(int capacity);


    /**
     * get the biggest room capacity in the system.
     * @return                  the capacity of the biggest room in the system.
     */
    int biggestRoomCapacity();

    /**
     * Find the room which has the most tech requirements in the system.
     * @param capacity      The capacity entered by the user.
     * @return              The room id of the room which has the most tech requirements in the system.
     */
    int maxTechRequirementsRoom(int capacity);

    /**
     * Find the room which has the most room conditions in the system.
     * @param capacity      The capacity entered by the user.
     * @return              The room id of the room which has the most room conditions in the system.
     */
    int maxRoomConditionsRoom(int capacity);


    /**
     * Assign talk from its original room to a new room.
     * @param roomID        id of new room
     * @param talkID        id of talk
     */
    public void modifyTalkRoom(int roomID, int talkID);

    /**
     * Remove the talk from the a given room's talk list.
     * @param roomID      The id of the room we need to operate.
     * @param talkID      The id of the talk that we want to remove from it's room
     * @return            The room id of the room if removed successfully
                          -1 if not removed successfully
     */
    int removeTalkFromRoom(int roomID, int talkID);


    /**
     * Format all the room information into rows
     * @return            A embedded list of the information of all the rooms
     */
    List<List<String>> getAllRoomRows();
}
