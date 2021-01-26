package controller;

import factory.UserInputControllerFactory;
import gateway.SerializableGateway;
import presenter.TextPresenter;
import userInputController.*;
import usecase.*;

import java.io.IOException;
import java.util.Scanner;

// Top-level controller
public class MainController {
    private static final String TEXT_RESET = "\u001B[0m";
    private static final String TEXT_RED = "\u001B[31m";
    private static final String TEXT_YELLOW = "\u001B[33m";

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        conferenceSystemRunner();
    }



    public static void conferenceSystemRunner() throws IOException,ClassNotFoundException{


        //file path
        String serializableTalkInfo = "phase2/resources/talk.ser";
        String serializableRoomInfo = "phase2/resources/room.ser";
        String serializableSpeakerInfo = "phase2/resources/speaker.ser";
        String serializableAttendeeInfo = "phase2/resources/attendee.ser";
        String serializableMessageInfo = "phase2/resources/message.ser";
        String serializableRequestInfo = "phase2/resources/request.ser";
        String serializableOrganizerInfo = "phase2/resources/organizer.ser";
        String serializableAdminInfo = "phase2/resources/admin.ser";

        // initialization managers
        SerializableGateway generalGateway = new SerializableGateway();
        TalkManager talkManager;
        try {
            talkManager = (TalkManager) generalGateway.readFromFile(serializableTalkInfo);
        } catch (ClassCastException e) {
            talkManager = new TalkManager();
        }

        RoomManager roomManager;
        try {
            roomManager = (RoomManager) generalGateway.readFromFile(serializableRoomInfo);

        } catch (ClassCastException e) {
            roomManager = new RoomManager();
        }

        SpeakerManager speakerManager;
        try {
            speakerManager = (SpeakerManager) generalGateway.readFromFile(serializableSpeakerInfo);

        } catch (ClassCastException e) {
            speakerManager = new SpeakerManager();
        }

        AttendeeManager attendeeManager;
        try {
            attendeeManager = (AttendeeManager) generalGateway.readFromFile(serializableAttendeeInfo);

        } catch (ClassCastException e) {
            attendeeManager = new AttendeeManager();
        }

        OrganizerManager organizerManager;
        try {
            organizerManager = (OrganizerManager) generalGateway.readFromFile(serializableOrganizerInfo);

        } catch (ClassCastException e) {
            organizerManager = new OrganizerManager();
        }

        AdminManager adminManager;
        try {
            adminManager = (AdminManager) generalGateway.readFromFile(serializableOrganizerInfo);

        } catch (ClassCastException e) {
            adminManager = new AdminManager();
        }

        MessageManager messageManager;
        try {
            messageManager = (MessageManager) generalGateway.readFromFile(serializableMessageInfo);

        } catch (ClassCastException e) {
            System.out.println(e);
            messageManager = new MessageManager();
        }

        RequestManager requestManager;
        try {
            requestManager = (RequestManager) generalGateway.readFromFile(serializableRequestInfo);

        } catch (ClassCastException e) {
            System.out.println(e);
            requestManager = new RequestManager();
        }


        //Instantiate controllers
        LoginController loginController = new LoginController();
        SignUpController signUpController = new SignUpController(talkManager, attendeeManager, roomManager, speakerManager, generalGateway);
        MessagingController messagingController = new MessagingController(messageManager, talkManager,
                speakerManager, attendeeManager);
        RegisterController  registerController= new RegisterController(speakerManager, attendeeManager);
        AdminController adminController = new AdminController(talkManager, speakerManager,
                roomManager, "9:00", "17:00",generalGateway, registerController,loginController, attendeeManager, organizerManager, adminManager);
        AdminUserController adminUserController = new AdminUserController(messageManager, generalGateway, talkManager, speakerManager, roomManager);
        RequestController requestController = new RequestController(requestManager);
        Scanner scanner = new Scanner(System.in);

//        System.out.println(TEXT_YELLOW+"Welcome to use the Conference Management System."+TEXT_RESET);

        //Instantiate presenter
        TextPresenter textPresenter = new TextPresenter();

        ClassCollection classCollection = new ClassCollection( signUpController, loginController, messagingController, adminController, adminUserController, requestController, talkManager,  roomManager, speakerManager, attendeeManager, organizerManager, adminManager, messageManager, requestManager, textPresenter );


        while (true) {
            Boolean isLoggedIn = false;
            //Home Menu
            System.out.println("\n---------------------------------------------------------");
            System.out.println(TEXT_YELLOW+"Welcome to use the Conference Management System."+TEXT_RESET);
            System.out.println("Here are available commands:");
            System.out.println("Login");
            System.out.println("Register");
            System.out.println("Exit");
            System.out.println("---------------------------------------------------------");
            System.out.println("Enter a command: ");




            String line = scanner.nextLine();
            String[] cmdAndArgs = line.trim().split("\\s+");
            String cmd = cmdAndArgs[0];
            if (cmd.equalsIgnoreCase("login")) {
                LoginUserInputController loginUserInputController = new LoginUserInputController(classCollection);
                UserInputController userInputController;
                UserInputControllerFactory userInputControllerFactory = new UserInputControllerFactory();
                boolean flag = loginUserInputController.login();
                if (flag) {
                    userInputController = userInputControllerFactory.getUserInputController(loginController.getUserType(), classCollection);
                    userInputController.isLoggedIn = true;

//                    switch (loginController.getUserType()) {
//                        case "speaker":
//                            userInputController = new SpeakerUserInputController(classCollection);
//                            userInputController.isLoggedIn = true;
//                            break;
//                        case "attendee":
//                            userInputController = new AttendeeInputController(classCollection);
//                            userInputController.isLoggedIn = true;
//                            break;
//                        case "admin":
//                            userInputController = new AdminUserInputController(classCollection);
//                            userInputController.isLoggedIn = true;
//                            break;
//                        default:
//                            userInputController = new OrganizerInputController(classCollection);
//                            userInputController.isLoggedIn = true;
//                    }
                    while (userInputController.isLoggedIn) {
                        userInputController.printMenu();
                        userInputController.getCommand();
                    }

                }


            }
            else if (cmd.equalsIgnoreCase("register")){
                RegisterUserInputController registerUserInputController = new RegisterUserInputController(classCollection);
                registerUserInputController.register();
            }
            else if (cmd.equalsIgnoreCase("exit")) {
                System.out.println("System has been shut down.");
                System.exit(0);
            } else {
                System.out.println(TEXT_RED+"Invalid command."+TEXT_RESET);
            }


        }
    }




}
