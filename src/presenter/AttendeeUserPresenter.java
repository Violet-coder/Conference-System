package presenter;

import java.util.Arrays;
import java.util.List;

/**
 * AttendeeUserPresenter is the presenter class for printing attendee user menus,
 * and messages that notifies users what to do next.
 * */
public class AttendeeUserPresenter {
    private final String TEXT_RESET = "\u001B[0m";
    private final String TEXT_RED = "\u001B[31m";
    private final String TEXT_GREEN = "\u001B[32m";
    private final String TEXT_BLUE = "\u001B[34m";

    private TableGenerator tableGenerator = new TableGenerator();
    public void printAttendeeMenu() {
        System.out.println("\n---------------------------------------------------------");
        System.out.println("Welcome to attendee menu!");
        System.out.println("Here are available commands:");
        System.out.println("Events System");
        System.out.println(TEXT_GREEN +  tableGenerator.formatTitles(Arrays.asList("see-events-I-can-sign-up", "see-events-I-have-signed-up"),30) + TEXT_RESET);
        System.out.println("---------------------------------------------------------");
        System.out.println("Message System");
        System.out.println(TEXT_GREEN + tableGenerator.formatTitles(Arrays.asList("send-message", "read-message"), 30) + TEXT_RESET);
        System.out.println(TEXT_GREEN + tableGenerator.formatTitles(Arrays.asList("see-all-messages", "delete-message"), 30) + TEXT_RESET);
        System.out.println(TEXT_GREEN + tableGenerator.formatTitles(Arrays.asList("see-unread", "mark-as-unread"), 30) + TEXT_RESET);
        System.out.println(TEXT_GREEN + tableGenerator.formatTitles(Arrays.asList("see-archive", "archive-message"), 30) + TEXT_RESET);
        System.out.println("---------------------------------------------------------");
        System.out.println("Request System");
        System.out.println(TEXT_GREEN + "review-my-request");
        System.out.println( TEXT_BLUE +"logout" + TEXT_RESET);
        System.out.println("---------------------------------------------------------\n");
    }



    /**
     * print the commands the attendee user can use after viewing the events they have signed up for.
     * */
    public void printOptionsForCancelEvent() {
        System.out.println("Here are the commands you can use: ");
        System.out.println("cancel-an-event");
        System.out.println("send-additional-request");
        System.out.println("go-back");
        System.out.println("---------------------------------------------------------");
    }

    /**
     * Print the optional commands the user can use after entering the command
     * "see-events-I-can-sign-up".
     * */
    public void printOptionsForViewEvents() {
        System.out.println("Here are the commands you can use: ");
        System.out.println("download-a-HTML-version");
        System.out.println("print-all-events-on-screen");
        System.out.println("find-my-interested-events");
    }
    /**
     * Print command for going back to last menu.
     * */
    public void printOptionsForGoBack() {
        System.out.println("What would you like to do next?");
        System.out.println("Here are the commands you can use:");
        System.out.println("go-back");
        System.out.println("---------------------------------------------------------");

    }

    /**
     * Print notification and options to guide user to enter the next command
     * after viewing the event he/she can sign up for.
     * */
    public void printOptionsForSignUp() {
        System.out.println("What would you like to do next?");
        System.out.println("Here are the commands you can use:");
        System.out.println("sign-up-for-an-event");
        System.out.println("go-back");
        System.out.println("---------------------------------------------------------");
    }

    /**
     * Print the command which can be used to filter events.
     * */
    public void printOptionsForFilterEvents() {
        System.out.println("Here are the commands you can use:");
        System.out.println("filter-by-day");
        System.out.println("filter-by-time");
        System.out.println("search-by-title");
        System.out.println("go-back");
        System.out.println("---------------------------------------------------------");
        System.out.println("Enter a command:");
    }

    /**
     * Print message to user requesting entering a date with specified format.
     * */
    public void requestInputDate() {
        System.out.println("Enter date in YYYY-MM-DD: ");
    }

    /**
     * Print message to user requesting entering time with specified format.
     * */
    public void requestInputTime(String inputName) {
        System.out.println("Enter "+inputName+" in HH:mm: ");
    }

    /**
     * Request user to input the event title for filtering events.
     * */
    public void requestInputTitle() {
        System.out.println("Enter full title or part of the title of an event: ");
    }

    /**
     * Request user to input a valid ID.
     * */
    public void requestID(String inputName) {
        System.out.println("Please enter the "+inputName+" you want to sign up.\n");

    }

    /**
     * print the failure messages for canceling an event.
     * */
    public void printCancelEventFailureMsg(String reason, int talkID) {
        switch (reason) {
            case "haveStarted":
                System.out.println(TEXT_RED + "Cannot cancel an event that has started!\n" + TEXT_RESET);
                break;
            case "dataError":
                System.out.println(TEXT_RED + "Cannot cannot find the attendee id in the corresponding event!\n" + TEXT_RESET);
                break;
            case "notSignedUp":
                System.out.println(TEXT_RED + "Cannot cancel the event because you didn't sign up for " +
                        "the event with ID: " + talkID +".\n" + TEXT_RESET);
                break;
            default:
                System.out.println(TEXT_RED +"Cannot cancel the talk!\n"+ TEXT_RESET);


        }
    }
}
