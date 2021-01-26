package usecase;

import entity.Room;

import java.io.Serializable;
import java.util.*;

/**
 * RoomManager is the use case for room entity and
 * handles all methods interact with the room entity
 */
public class RoomManager implements IRoomManager, Serializable {

    private ArrayList<Room> AllRooms;
    private int totalNum;

    public RoomManager() {
        this.AllRooms = new ArrayList<Room>();
        this.totalNum = 0;

    }

    public List<Integer> getTalksGivenInRoom(int roomID) {
        List<Integer> res = new ArrayList<>();
        for (Room room : this.AllRooms) {
            if (room.getId() == roomID){
                res.addAll(room.getTalks());
            }
        }
        return res;
    }

    public Map<Integer, Room> getAllRooms() {
        Map<Integer, Room> res = new HashMap<>();
        for (Room r : this.AllRooms){
            res.put(r.getId(), r);
        }
        return res;
    }

    public int getRoomCapacityByID(int roomID) {
        int capacity = 0;
        capacity = this.getAllRooms().get(roomID).getCapacity();
        return capacity;
    }

    public int addRoom(String name, int capacity,List<String> techRequirements, List<String> roomConditions){
        for (Room room : this.AllRooms) {
            if (room.getName().equals(name)) {
                return -1;
            }
        }
        Room newroom = new Room(totalNum++, name, capacity, techRequirements, roomConditions);
        this.AllRooms.add(newroom);
        return newroom.getId();

    }

    public String printRoom(int id){
        String printed = "";
        for (Room room : this.AllRooms) {
            if (room.getId() == id){
                printed = "ID: " + id + "\n" +
                        "Name: " + room.getName() + "\n" +
                        "Capacity: " + room.getCapacity() + "\n";
                if (room.getTechRequirementsList().size()>0)
                {
                    printed = printed + "Technology Requirements: " + room.gettechRequirements()+"\n";
                }
                if (room.getRoomConditionsList().size()>0){
                    printed = printed +  "Room Conditions: " + room.getRoomConditions()+"\n";
                }

            }
        }
        return printed;
    }

    public Map<Integer, String> getAllRoomIdsAndNames() {
        Map<Integer, String> RoomIdtoName = new HashMap<>();
        for (Room room : this.AllRooms){
            RoomIdtoName.put(room.getId(), room.getName());
        }
        return RoomIdtoName;
    }

    public List<Integer> getSuggestedRoomIds(int capacity, List<String> techRequirements, List<String> roomConditions) {
        List<Integer> suggestRoomIds = new ArrayList<Integer>();
        for (Room room : this.AllRooms){
            if (capacity<= room.getCapacity()&&room.getTechRequirementsList().containsAll(techRequirements) && room.getRoomConditionsList().containsAll(roomConditions)){
                suggestRoomIds.add(room.getId());
            }
        }
        return suggestRoomIds;
    }

    public boolean hasBiggerRoom(int capacity){
        boolean hasBiggerRoom = false;
        for (Room room : this.AllRooms){
            if (capacity<= room.getCapacity()){
                hasBiggerRoom = true;
                break;
            }
        }
        return hasBiggerRoom;
    }

    public int biggestRoomCapacity(){
        ArrayList<Integer> roomCapacityList = new ArrayList<Integer>();
        for (Room room : this.AllRooms) {
            roomCapacityList.add(getRoomCapacityByID(room.getId()));
        }
        Integer maxCapacity = Collections.max(roomCapacityList);
        return maxCapacity;
    }

    public String getRoomNameByID(int roomID) {
        Map<Integer, String> RoomIdtoName = this.getAllRoomIdsAndNames();
        return RoomIdtoName.get(roomID);
    }

    public boolean addTalkToRoom(int roomID, int talkID) {
        for (Room room : this.AllRooms) {
            if ((room.getId() == roomID) && !(room.getTalks().contains(talkID))) {
                room.addTalk(talkID);
                return true;
            }
        }
        return false;
    }

    public boolean isNameExisting(String name) {
        for (Room room : this.AllRooms) {
            if (room.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public int maxTechRequirementsRoom(int capacity){
        Map<Integer, Integer> res = new HashMap<>();
        for (Room r : this.AllRooms){
            res.put(r.getId(), r.getTechRequirementsList().size());
        }
        int max = 0;
        int maxRoomId =-1;
        int value = 0;
        for(Integer key:res.keySet()){
            value = res.get(key);
            if(max<value&&capacity<=getRoomCapacityByID(key)){
                max = value;
                maxRoomId = key;
            }
        }

        return maxRoomId;
    }

    public int maxRoomConditionsRoom(int capacity) {
        Map<Integer, Integer> res = new HashMap<>();
        for (Room r : this.AllRooms) {
            res.put(r.getId(), r.getRoomConditionsList().size());
        }
        int max = 0;
        int maxRoomId = -1;
        int value = 0;
        for (Integer key : res.keySet()) {
            value = res.get(key);
            if (max < value && capacity <= getRoomCapacityByID(key)) {
                max = value;
                maxRoomId = key;
            }
        }

        return maxRoomId;
    }


    public void modifyTalkRoom(int roomID, int talkID) {
        Room r;
        boolean found = false;
        for (int i = 0; i < AllRooms.size(); i++) {
            r = AllRooms.get(i);
            List<Integer> talks = r.getTalks();
            for (int j = 0; j < talks.size(); j++) {
                if (talks.get(j) == talkID) {
                    talks.remove(j);
                    found = true;
                    break;
                }
            }
            if (found) break;
        }
        for (int i = 0; i < AllRooms.size(); i++) {
            r = AllRooms.get(i);
            if (r.getId() == roomID) {
                r.addTalk(talkID);
                break;
            }
        }
    }

    /**
     * Remove a talk from the room with given id.
     * @param roomID room id
     * @param talkID talk id to be removed
     * @return id of the updated room
     * */
    public int removeTalkFromRoom(int roomID, int talkID) {
        // not check if the talk ID exists or not
        for(Room room : AllRooms) {
            if(room.getId() == roomID) {
                List<Integer> talks = room.getTalks();
                talks.remove(new Integer(talkID));
                return roomID;
            }
        }

        return -1;
    }

    /**
     * Convert all the room info into a String.
     * */
    public String allRoomsInfo() {
        String allRoomsInfo = "";
        for(Room room : AllRooms) {
            allRoomsInfo += room.toString();
        }

        return allRoomsInfo;
    }

    /**
     * Export all the room entity information into a list. Each row
     * represents a single room.
     * */
    public List<List<String>> getAllRoomRows() {
        List<List<String>> allRooms = new ArrayList<>();
        for(Room room : AllRooms) {
           List<String> row = buildRoomRow(room);
           allRooms.add(row);
        }
        return allRooms;
    }

    private List<String> buildRoomRow(Room room){
        return Arrays.asList(String.valueOf(room.getId()), room.getName(), String.valueOf(room.getCapacity()),
                room.gettechRequirements(), room.getRoomConditions(), room.getTalks().toString());
    }
}