package userInputController;

import controller.ClassCollection;
import controller.LoginController;
import controller.SignUpController;
import presenter.AttendeeUserPresenter;
import presenter.TableGenerator;
import presenter.TextPresenter;
import usecase.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * SignUpInputController handles the input and request for signing up and cancel an event
 * for the attendee user.
 *
 * */
public class SignUpInputController {
    private ITalkManager talkManager;
    private IAttendeeManager attendeeManager;
    private IRoomManager roomManager;
    private ISpeakerManager speakerManager;
    private SignUpController signUpController;
    private LoginController loginController;
    private RequestInputController requestPresenter;
    private final String TEXT_RESET = "\u001B[0m";
    private final String TEXT_RED = "\u001B[31m";
    private final String TEXT_GREEN = "\u001B[32m";
    private TextPresenter textPresenter = new TextPresenter();
    private AttendeeUserPresenter attendeeUserPresenter = new AttendeeUserPresenter();
    /**
     * Constructor of the SignUpPresenter.
     * */
    public SignUpInputController(ClassCollection classCollection){
        this.talkManager = classCollection.talkManager;
        this.attendeeManager = classCollection.attendeeManager;
        this.roomManager = classCollection.roomManager;
        this.speakerManager = classCollection.speakerManager;
        this.signUpController = classCollection.signUpController;
        this.loginController = classCollection.loginController;
        this.requestPresenter = new RequestInputController(classCollection);
    }



    /**
     * Handle the command from user input in the attendee menu. It handles the
     * two available command "see-talks-I-have-signed-up" and "see-talks-I-can-sign-up"
     * by calling the corresponding method directly.
     * Once receiving a input parameter which is not contained in the available commands,
     * it will return to the attendee menu.
     *
     * @param cmd       the user input passed from AttendeePresenter
     * */
    public void handleCmd(String cmd) {
        if(cmd.equalsIgnoreCase("see-events-I-have-signed-up")) {
            viewSignedUpTalks();
        } else if(cmd.equalsIgnoreCase("see-events-I-can-sign-up")) {
            printOrDownload();
        } else {
            textPresenter.printErrorText("Invalid command\n");
        }
    }

    /**
     * Display available commands to users: cancel-a-talk, go-back and
     * the schedule of talks that the current user has signed up for.
     * Get user input for canceling a talk and call the function cancelATalk
     * to handle the request appropriately.
     * */
    public void viewSignedUpTalks() {
        int userID = loginController.getUserId();
        List<Integer> signedUpTalkIDs = attendeeManager.getSignedUpTalks(userID);
        List<Integer> sortedSignedUpTalkIDs = signUpController.sortTalkIDByTime(signedUpTalkIDs);

        if(signedUpTalkIDs.size() == 0){
            textPresenter.printSuccessText("You haven't signed up for any event!\n");
            textPresenter.printDividerLine();
            textPresenter.printNormalText("Here are the commands you can use:");
            textPresenter.printNormalText("go-back");
            textPresenter.printDividerLine();
        } else {
            textPresenter.printNotificationText("Here is the schedule for your signed up events:");

            printTalkScheduleTable(sortedSignedUpTalkIDs);

            attendeeUserPresenter.printOptionsForCancelEvent();
        }

        textPresenter.printNormalText("Enter a command:");
        Scanner sc = new Scanner(System.in);
        String nextCmd = sc.nextLine().trim();

        if(nextCmd.equalsIgnoreCase("cancel-an-event")){
            cancelATalk();
        }
        else if(nextCmd.equalsIgnoreCase("send-additional-request")){
            textPresenter.printNormalText("Please enter the event ID you want to send request.");
            int talkID = getTalkID();
            if(!signedUpTalkIDs.stream().anyMatch(x-> x== talkID)){
                textPresenter.printErrorText("The event with ID: " + talkID +
                        " doesn't exist for you.\n");
                return;
            }
            requestPresenter.sendRequest(userID, talkID);
        }
        else if(nextCmd.equalsIgnoreCase("go-back")){
            return;
        }else {
            textPresenter.printErrorText("Invalid command\n");
        }

    }

    /**
     * offers user the option to download html version or print all events or search by keywords
     */
    public void printOrDownload() {
        Scanner sc = new Scanner(System.in);
        attendeeUserPresenter.printOptionsForViewEvents();
        String cmd = sc.nextLine().trim();
        if (cmd.equalsIgnoreCase("download-a-HTML-version")) {
            downloadEvents();
        }
        else if (cmd.equalsIgnoreCase("print-all-events-on-screen")) {
            viewAllTalks();
        }
        else if (cmd.equalsIgnoreCase("find-my-interested-events")) {
            filterEvents();
        } else {
            textPresenter.printErrorText("Invalid Command.\n");
        }
    }

    /**
     * Display the available commands and all the talks that the current user can sign up for
     * which include the unexpired talks that the current user didn't sign up. Call the method
     * signUpForATalk to handle the request for signing up for a talk with specified talk ID from
     * user input.
     * */
    public void viewAllTalks() {
        //get user id
        int userID = loginController.getUserId();
        List<Integer> unenrolledUnexpiredTalks = getUnenrolledUnexpiredTalkIDs(userID);
        List<Integer> sortedUnenrolledUnexpiredTalks = signUpController.sortTalkIDByTime(unenrolledUnexpiredTalks);
        textPresenter.printNotificationText("Here are all the events you can sign up:\n");
        printEventList(sortedUnenrolledUnexpiredTalks);
    }

    /**
     * print a list of events
     * @param EventId a list of event id
     */
    private void printEventList(List<Integer> EventId) {
        if(EventId.size() == 0) {
            textPresenter.printSuccessText("No available events.\n");
            attendeeUserPresenter.printOptionsForGoBack();
            textPresenter.printNormalText("Enter a command:");
        } else {
            printTalkScheduleTable(EventId);

            attendeeUserPresenter.printOptionsForSignUp();
            textPresenter.printNormalText("Enter a command:");
        }
        Scanner sc = new Scanner(System.in);
        String nextCmd = sc.nextLine().trim();

        if(nextCmd.equalsIgnoreCase("sign-up-for-an-event")){
            signUpForATalk();
        } else if(nextCmd.equalsIgnoreCase("go-back")){
            return;
        } else {
            textPresenter.printErrorText("Invalid command\n");
        }
    }

    /**
     * filter events by user input
     */
    public void filterEvents() {
        Scanner sc = new Scanner(System.in);
        attendeeUserPresenter.printOptionsForFilterEvents();
        String cmd = sc.nextLine().trim();
        String filter = "";
        String keyword = "";

        if (cmd.equalsIgnoreCase("filter-by-day")) {
            filter = "date";
            attendeeUserPresenter.requestInputDate();
            keyword = sc.nextLine().trim();
        }
        else if (cmd.equalsIgnoreCase("filter-by-time")) {
            filter = "time";
            attendeeUserPresenter.requestInputTime("Starting Time");
            String startTime = sc.nextLine().trim();
            attendeeUserPresenter.requestInputTime("End Time");
            String endTime = sc.nextLine().trim();
            keyword = startTime + endTime;
        }
        else if (cmd.equalsIgnoreCase("search-by-title")) {
            filter = "title";
            attendeeUserPresenter.requestInputTitle();
            keyword = sc.nextLine().trim();
        }
        else if (cmd.equalsIgnoreCase("search-by-liked")) {
            textPresenter.printErrorText("Not implemented");
        }
        else if (cmd.equalsIgnoreCase("go-back")) {
            return;
        }
        else {
            textPresenter.printErrorText("Invalid command.\n");
        }

        List<Integer> eventIds = signUpController.filterEvents(filter, keyword);
        printEventList(eventIds);
    }

    /**
     * download a html version of events
     */
    public void downloadEvents() {
        int userID = loginController.getUserId();
        List<Integer> unenrolledUnexpiredTalks = getUnenrolledUnexpiredTalkIDs(userID);
        List<Integer> sortedUnenrolledUnexpiredTalks = signUpController.sortTalkIDByTime(unenrolledUnexpiredTalks);
        signUpController.downloadEvents(sortedUnenrolledUnexpiredTalks);
    }


    /**
     * Check whether a talk is available to sign up for and call SignUpController to
     * get the user signed up.
     * */
    private void signUpForATalk() {
        attendeeUserPresenter.requestID("Event ID");
        // get current user id
        int userID = loginController.getUserId();
        int talkID = getTalkID();

        boolean isTalkExisting = talkManager.isTalkExisting(talkID);
        List<Integer> unexpiredTalkIDs = talkManager.getUnexpiredTalkIDs();
        boolean isTalkUnxpired = unexpiredTalkIDs.contains(talkID);

        if(!isTalkExisting){
            textPresenter.printErrorText("The talk with ID: "
                    + talkID + " doesn't exist.\n");
            return;
        }
        if(!isTalkUnxpired) {
            textPresenter.printErrorText("Signing up failed. The talk with ID: "
                    + talkID + " doesn't exist.\n");
            return;
        }

        boolean userSignedForTalk = talkManager.checkIfUserSingedUpForATalk(userID,talkID);
        boolean spaceAvailable = signUpController.checkAvailableSpace(talkID);

        if(!userSignedForTalk && spaceAvailable){
           boolean signUpSuccess  = signUpController.singUpForTalk(userID,talkID);
           if(signUpSuccess){
               textPresenter.printSuccessText("Sign up successfully for the following event:");
               printTalkSchedule(talkID);
           } else {
               //local data error
               textPresenter.printErrorText("Cannot sign up for the event.");
            }
        } else {
            if(userSignedForTalk){
                textPresenter.printErrorText("You have already signed up for the event with ID: "
                        +talkID +".");
                printTalkSchedule(talkID);

            } else if( !spaceAvailable ){
                textPresenter.printErrorText("Cannot sign up because the event is full.\n");
            }

        }

    }


    private void cancelATalk() {
        textPresenter.printNotificationText("Please enter the event ID you want to cancel.");
        //get current user id from loginController
        int userID = loginController.getUserId();
        int talkID = getTalkID();

        boolean isTalkExisting = talkManager.isTalkExisting(talkID);

        if(!isTalkExisting){
            textPresenter.printErrorText("The event with ID: " + talkID +
                    " doesn't exist.\n");
            return;
        }

        boolean userSignedUp = talkManager.checkIfUserSingedUpForATalk(userID, talkID);

        if(userSignedUp){
            //get the talk start time
            List<Integer> talks = new ArrayList<Integer>();
            talks.add(talkID);
            List<List<String>> dateTimes = talkManager.getDateTimesOfTalks(talks);
            String startDateTime = dateTimes.get(0).get(0);

            if(isDateTimePast(startDateTime)){
                //System.out.println(TEXT_RED + "Cannot cancel an event that has started!\n" + TEXT_RESET);
                attendeeUserPresenter.printCancelEventFailureMsg("haveStarted", talkID);
                return;
            } else {
                boolean cancelTalkSuccess = signUpController.cancelTalk(userID, talkID);
                if(cancelTalkSuccess){
                    textPresenter.printSuccessText("Cancel the following event successfully!");
                    printTalkSchedule(talkID);
                } else {
                    //local data error
                    //System.out.println(TEXT_RED + "Cannot cannot find the attendee id in the corresponding event!\n" + TEXT_RESET);
                    attendeeUserPresenter.printCancelEventFailureMsg("dataError", talkID);
                }
            }
        } else {
            /*System.out.println(TEXT_RED + "Cannot cancel the event because you didn't sign up for " +
                    "the event with ID: " + talkID +".\n" + TEXT_RESET);*/
            attendeeUserPresenter.printCancelEventFailureMsg("notSignedUp", talkID);
        }
    }

    private int getTalkID(){
        Scanner sc = new Scanner(System.in);
        textPresenter.printNormalText("Enter the event ID:");
        int talkID;
        while ( !sc.hasNextInt() ){
            textPresenter.printNotificationText("The event ID should be an integer. Please try again.\n");
            textPresenter.printNormalText("Enter the event ID:");
            //talkID = sc.nextInt();
            sc.next();
        }
        talkID = sc.nextInt();
        return talkID;
    }

    private void printTalkScheduleTable(List<Integer> talkIDs){
        List<String> header = Arrays.asList("Event ID", "Event Title","Type","Speaker(s)", "Start Date", "End Date",
                "Start Time", "End Time", "Room Name", "Capacity");
        List<List<String>> rows = talkManager.getTalkScheduleRowsByIDs(talkIDs);

        //add room and speaker information to the row
        int index = 0;
        for(int talkID : talkIDs)
        {

            //print room information
            int roomID = talkManager.getRoomIDByTalkID(talkID);
            String roomName = roomManager.getRoomNameByID(roomID) == null ? ""
                    : roomManager.getRoomNameByID(roomID);
            //get index of the current talkID
            rows.get(index).set(8, roomName);

            //print speaker information
            List<Integer> speakerIDList = talkManager.getSpeakerIDListByTalkID(talkID);
            // don't print speaker name if no speaker
            // else print all speaker names
            String speakerNames = "";
            int speakerIndex = 0;
            for (int speakerID : speakerIDList) {
                String speakerName = speakerManager.getSpeakerNameByID(speakerID) == null ? ""
                        : speakerManager.getSpeakerNameByID(speakerID);
                speakerNames += speakerName;
                if(speakerIndex++ < speakerIDList.size() - 1){
                    speakerNames += ", ";
                }
            }
            rows.get(index++).set(3, speakerNames);
        }
        //print the table
        TableGenerator tableGenerator = new TableGenerator();
        tableGenerator.printAsTable(header, rows);
    }

    private void printTalkSchedule(int talkID){
        textPresenter.printInfoText(talkManager.getTalkSchedule(talkID));
        //print room information
        int roomID = talkManager.getRoomIDByTalkID(talkID);
        String roomName = roomManager.getRoomNameByID(roomID) == null ? ""
                : roomManager.getRoomNameByID(roomID);
        textPresenter.printInfoText("Room Name: "+ roomName);

        //print speaker information
        List<Integer> speakerIDList = talkManager.getSpeakerIDListByTalkID(talkID);
        // don't print speaker name if no speaker
        if(speakerIDList.size() == 0) {
            textPresenter.printDividerLine();
            return;
        }
        // else print all speaker names
        String speakerNames = "";
        int speakerIndex = 0;
        for(int speakerID : speakerIDList) {
            String speakerName = speakerManager.getSpeakerNameByID(speakerID) == null ? ""
                    : speakerManager.getSpeakerNameByID(speakerID);
            speakerNames += speakerName;
            if(speakerIndex++ < speakerIDList.size() - 1){
                speakerNames += ", ";
            }
        }
        textPresenter.printInfoText("Speakers: " + speakerNames);
        textPresenter.printDividerLine();
    }

    /**
     * Get the ids of unexpired talk that the current user didn't sign up for, which are
     * the ids of the talks that the current user can sign up for.
     * */
    public List<Integer> getUnenrolledUnexpiredTalkIDs(int useID){
        List<Integer> unexpiredTalkIDs = talkManager.getUnexpiredTalkIDs();
        List<Integer> enrolledTalkIDs = attendeeManager.getSignedUpTalks(useID);

        Iterator<Integer> iterator = unexpiredTalkIDs.iterator();
        while (iterator.hasNext()){
            int id = iterator.next();
            if(enrolledTalkIDs.contains(id)){
                iterator.remove();
            }
        }
        return unexpiredTalkIDs;
    }

    private boolean isDateTimePast(String dateTime) {
        boolean res = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        Date dateToCompare = null;
        try {
            dateToCompare = sdf.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date now = new Date();

        if(now.after(dateToCompare)) {
            res = true;
        }

        return res;
    }


}
