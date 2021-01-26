package controller;

import presenter.TextPresenter;
import usecase.*;
/**
 * It is a collection of controllers, use cases and presenters. It is instantiated in
 * MainController and is passed to userInputControllers which interact with controllers,
 * use cases and presenters.
 * */
public class ClassCollection {
    //controllers
    public SignUpController signUpController;
    public LoginController loginController;
    public MessagingController messagingController;
    public AdminController adminController;
    public AdminUserController adminUserController;
    public RequestController requestController;

    //usecases
    public TalkManager talkManager;
    public RoomManager roomManager;
    public SpeakerManager speakerManager;
    public AttendeeManager attendeeManager;
    public MessageManager messageManager;
    public OrganizerManager organizerManager;
    public RequestManager requestManager;
    public IAdminManager adminManager;

    //presenter
    public TextPresenter textPresenter;

    /**
     * Constructor of the ClassCollection: controllers and usecases presenters interacts with encapsulated in the ClassCollection
     * */
    public ClassCollection(SignUpController signUpController, LoginController loginController,
                           MessagingController messagingController,AdminController adminController,
                           AdminUserController adminUserController, RequestController requestController,
                           TalkManager talkManager, RoomManager roomManager,SpeakerManager speakerManager,
                           AttendeeManager attendeeManager, OrganizerManager organizerManager,
                           AdminManager adminManager,
                           MessageManager messageManager, RequestManager requestManager,
                           TextPresenter textPresenter){
        this.signUpController = signUpController;
        this.loginController = loginController;
        this.messagingController = messagingController;
        this.adminController = adminController;
        this.adminUserController = adminUserController;
        this.requestController = requestController;


        this.talkManager = talkManager;
        this.roomManager = roomManager;
        this.speakerManager = speakerManager;
        this.attendeeManager = attendeeManager;
        this.organizerManager = organizerManager;
        this.adminManager = adminManager;
        this.messageManager = messageManager;
        this.requestManager = requestManager;

        this.textPresenter = textPresenter;

    }
}
