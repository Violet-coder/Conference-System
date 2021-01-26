package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * Room entity class.
 * */
public class Room implements Serializable {
    private int id;
    private String name;
    private int capacity; // Max number of people except for the speaker the room can hold
    private List<Integer> talks;

    private List<String> techRequirements;
    private List<String> roomConditions;

    public Room(int id, String name, int capacity, List<String> techRequirements, List<String> roomConditions) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.techRequirements = techRequirements;
        this.roomConditions = roomConditions;
        this.talks = new ArrayList<>();
    }


    public void addTalk(int talkID) {
        this.talks.add(talkID);
    }

    public List<Integer> getTalks() {
        return this.talks;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public List<String> getTechRequirementsList() { return this.techRequirements;}
    public List<String> getRoomConditionsList() { return this.roomConditions;}

    public String gettechRequirements(){
        String requirements = "";
        for (int i=0; i < this.techRequirements.size();i++ ){
            if(i==0){
                requirements = this.techRequirements.get(i);
            } else {
                requirements = requirements + ", "+ this.techRequirements.get(i);
            }
        }
//        for (String req : this.techRequirements){
//            requirements = requirements + ", "+ req;
//        }
        return requirements;
    }

    public String getRoomConditions(){
        String requirements = "";
        for (int i=0; i < this.roomConditions.size();i++ ){
            if(i==0){
                requirements = this.roomConditions.get(i);
            } else {
                requirements = requirements + ", "+ this.roomConditions.get(i);
            }
        }
//        for (String req : this.techRequirements){
//            requirements = requirements + ", "+ req;
//        }
        return requirements;
    }
    @Override
    public String toString(){
        return "Room ID:" + this.id + "\n" +
                "Room Name: " + name + "\n" +
                "Capacity: " + capacity + "\n" +
                "Talks: " + talks + "\n";
    }
}
