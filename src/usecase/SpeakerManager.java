package usecase;

import entity.NullSpeakerTalk;
import entity.Speaker;

import java.io.Serializable;
import java.util.*;

/**
 * SpeakerManager is the use case for speaker entity and
 * handles all methods interact with the speaker entity
 */
public class SpeakerManager implements ISpeakerManager, Serializable {

    private int nextID;
    private ArrayList<Speaker> listOfSpeakers;

    public SpeakerManager() {
        this.nextID = 0;
        this.listOfSpeakers = new ArrayList<>();
    }

    public List<Integer> getTalksGivenBySpeaker(int speakerID) {
//        listOfSpeakers will be given
        for (Speaker speaker : listOfSpeakers) {
            if (speaker.getId() == speakerID) {
                return speaker.getTalksToGive();
            }
        }
        //for testing, pls comment out the following line when you write this function
        return new ArrayList<>();
    }

    public List<Integer> getAllSpeakers() {
        ArrayList<Integer> speakers = new ArrayList<>();
        for (Speaker speaker : this.listOfSpeakers) {
            speakers.add(speaker.getId());
        }
        return speakers;
    }

    @Override
    public int addSpeaker(String username, String password, String name) {
        for (Speaker speaker : this.listOfSpeakers) {
            if (speaker.getUsername().equals(username)) {
                return -1;
            }
        }
        Speaker newSpeaker = new Speaker(this.nextID, username, password, name);
        this.nextID += 1;
        this.listOfSpeakers.add(newSpeaker);
        return newSpeaker.getId();
    }

    @Override
    public String printSpeaker(int id) {
        for (Speaker speaker : this.listOfSpeakers) {
            if (speaker.getId() == id) {
                return speaker.toString();
            }
        }
        return "";
    }

    @Override
    public boolean addTalkToSpeaker(int speakerID, int talkID) {
        for (Speaker speaker : this.listOfSpeakers) {
            if ((speaker.getId() == speakerID) && !(speaker.getTalksToGive().contains(talkID))) {
                speaker.addTalkToGive(talkID);
                return true;
            }
        }
        return false;
    }

    public Map<Integer, String> getAllSpeakerIdsAndNames() {
        Map<Integer, String> result = new HashMap<>();
        for (Speaker speaker : this.listOfSpeakers) {
            result.put(speaker.getId(), speaker.getName());
        }
        return result; //delete this when implementing the method
    }

    @Override
    public String getSpeakerNameByID(int speakerID) {
        return this.getAllSpeakerIdsAndNames().get(speakerID);
    }

    public boolean isUsernameExisting(String username) {
        for (Speaker s : listOfSpeakers) {
            if (s.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public void modifyTalkSpeaker(int speakerID, int talkID) {
        Speaker s;
        boolean found = false;
        for (int i = 0; i < listOfSpeakers.size(); i++) {
            s = listOfSpeakers.get(i);
            List<Integer> talks = s.getTalksToGive();
            for (int j = 0; j < talks.size(); j++) {
                if (talks.get(j) == talkID) {
                    talks.remove(j);
                    found = true;
                    break;
                }
            }
            if (found) break;
        }
        for (int i = 0; i < listOfSpeakers.size(); i++) {
            s = listOfSpeakers.get(i);
            if (s.getId() == speakerID) {
                s.addTalkToGive(talkID);
                break;
            }
        }
    }

    /**
     * Cancel a talk for a speaker.
     * @param speakerID the speaekr id
     * @param talkID the event id
     * @return if cancel successfully, return the speaker id, else return -1 for failure
     * */
    public int cancelTalkForSpeaker(int speakerID, int talkID) {
        Iterator<Speaker> iter = listOfSpeakers.iterator();
        while (iter.hasNext()) {
            Speaker s = iter.next();
            if(s.getId() == speakerID) {
                s.getTalksToGive().remove(new Integer(talkID));
                return speakerID;
            }
        }
        return -1;
    }

    /**
     * Convert all the speaker entities into a String.
     * */
    public String allSpeakerInfo() {
        String res = "";
        for(Speaker speaker : listOfSpeakers) {
            res += speaker.toString();
            System.out.println("---------------------------------------------------------");
        }
        return res;
    }

}
