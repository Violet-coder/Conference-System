package factory;

import controller.ClassCollection;
import userInputController.*;

/**
 * UserInputControllerFactory produces instances of sub-classes of UserInputController.
 */
public class UserInputControllerFactory {
    /**
     * Create an instance of a given sub-class of UserInputController.
     * @param type                  The type of the user.
     * @param classCollection       An instance of ClassCollection.
     * @return                      A reference to the created instance of the sub-class of UserInputController.
     */
    public UserInputController getUserInputController(String type, ClassCollection classCollection) {
        UserInputController userInputController;
        switch (type) {
            case "speaker":
                userInputController = new SpeakerUserInputController(classCollection);
                break;
            case "attendee":
                userInputController = new AttendeeInputController(classCollection);
                break;
            case "admin":
                userInputController = new AdminUserInputController(classCollection);
                break;
            default:
                userInputController = new OrganizerInputController(classCollection);
        }
        return userInputController;
    }
}
