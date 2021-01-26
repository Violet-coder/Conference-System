package controller;

import gateway.SerializableGateway;
import gateway.UserGateway;
import usecase.AttendeeManager;
import usecase.SpeakerManager;

/**
 * RegisterController: handle the register process for users
 *                     provide method to check duplicate username
 */

public class RegisterController {
    private SpeakerManager speakerManager;
    private AttendeeManager attendeeManager;

    public RegisterController (SpeakerManager speakerManager, AttendeeManager attendeeManager) {
        this.speakerManager = speakerManager;
        this.attendeeManager = attendeeManager;
    }

    UserGateway userGateway = new UserGateway();

    /**
     * Create an account for a user.
     * @param username      The username of the user.
     * @param password      The password of the user.
     * @param usertype      The usertype of the user.
     * @param name          The name of the the user.
     * @return
     *                      true the account data has been inserted in the database successfully.
     *                      false if failed to insert the data into database.
     */
    public boolean register(String username, String password, String usertype, String name) {
        boolean flag = false;

        //write to user manager
        SerializableGateway generalGateway = new SerializableGateway();
        switch(usertype) {
            case "speaker":

//                SpeakerManager speakerManager = classCollection.speakerManager;
                int speakerId = speakerManager.addSpeaker(username, password, name);
                try {
                    generalGateway.saveToFile("phase2/resources/speaker.ser", speakerManager);
                    userGateway.writeToTxt(speakerId, username, password, usertype,name);
                }
                catch(Exception e) {
                    flag = true;
                    //nice to meet you :)
                }
            break;

            case "attendee":

//                AttendeeManager attendeeManager = classCollection.attendeeManager;
                int attendeeId = attendeeManager.addAttendee(username, password, name);
                
                try {
                    generalGateway.saveToFile("phase2/resources/attendee.ser", attendeeManager);
                    userGateway.writeToTxt(attendeeId, username, password, usertype, name);
                }
                catch(Exception e) {
                    flag = true;
                    //hope to see you again :)
                }
            break;
        }


        return flag;

    }

    /**
     * Check if there is this is a duplicate username.
     * @param username      The username of the user.
     * @return
     *                      true the username passed in has already in the database.
     *                      false if the username is not duplicate .
     */
    public boolean duplicateUsername(String username){
        boolean flag = false;
        String sameUserName = userGateway.getUsernameByGivenName(username);
        if(sameUserName!=null){
            flag = true;
        }
        return flag;
    }


}
