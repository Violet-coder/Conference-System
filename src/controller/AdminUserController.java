package controller;

import gateway.SerializableGateway;
import usecase.MessageManager;
import usecase.RoomManager;
import usecase.SpeakerManager;
import usecase.TalkManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AdminUserController receives the request from AdminUserInputController and deal with them by interacting with use cases
 * and gateways to update the entity information and store it to local files. Comparing with AdminUserInputController which
 * receives user input, AdminUserController doesn't interact with user but is more close to use case classes and gateways.
 * */
public class AdminUserController {
    private MessageManager messageManager;
    private SerializableGateway generaGateway;
    private TalkManager talkManager;
    private SpeakerManager speakerManager;
    private RoomManager roomManager;

    /**
     * Constructor for AdminUserController requires the instantiated use case instances it interacts with as parameters.
     * */
    public AdminUserController(MessageManager messageManager, SerializableGateway generalGateway,
                               TalkManager talkManager, SpeakerManager speakerManager, RoomManager roomManager) {
        this.messageManager = messageManager;
        this.generaGateway = generalGateway;
        this.talkManager = talkManager;
        this.speakerManager = speakerManager;
        this.roomManager = roomManager;
    }

    /**
     * Call the use case message manager to delete the entity and store the updated
     * message manager class to local.
     * @param messageID message id
     * @return the deleted message id
     * */
    public int deleteMessageByID(int messageID) {
        boolean isMessageExisting = messageManager.isMessageExisting(messageID);

        if(!isMessageExisting) {
            return -1;
        }

        int deletedMessageID = messageManager.deleteMessageByID(messageID);
        try {
            generaGateway.saveToFile("phase2/resources/message.ser", messageManager);
        } catch (IOException e) {
            // cannot save to local file
            return -2;
        }

        return deletedMessageID;
    }

    /**
     * Delete an empty event with id. It delete the event entity, remove the event from speaker's talks, and
     * remove the event from the room.
     * @param talkID id of the event
     * @return the result whether it has been deleted correctly in talkManager, speakerManager and roomManager
     * */
    public Map<Integer, Integer> deleteEmptyTalkByID(int talkID) {
        Map<Integer, Integer> cancelRes = new HashMap<>();
        // first get the room id and speaker ids of the talk
        int roomID = talkManager.getRoomIDByTalkID(talkID);
        List<Integer> speakerIds = talkManager.getSpeakerIDListByTalkID(talkID);
        //delete the empty talk
        int deletedEmptyTalkID = talkManager.deleteEmptyTalkByID(talkID);
        cancelRes.put(talkID, deletedEmptyTalkID);

        // if the talk has a speaker or multi speakers, delete the talk from the speaker entity
        // for the speakers of the talk, cancel the talk for them
        for(int speaker : speakerIds) {
            int res =  speakerManager.cancelTalkForSpeaker(speaker, talkID);
            cancelRes.put(speaker, res);
        }

        // remove the talk from the room
        int cancelRoomID = roomManager.removeTalkFromRoom(roomID, talkID);

        try {
            generaGateway.saveToFile("phase2/resources/talk.ser", talkManager);
            generaGateway.saveToFile("phase2/resources/speaker.ser", speakerManager);
            generaGateway.saveToFile("phase2/resources/room.ser", roomManager);
        } catch (IOException e) {
            // cannot save to local file
            cancelRes.put(-2,-2);
            return cancelRes;
        }

        return cancelRes;
    }
}
