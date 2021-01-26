package presenter;

import java.util.Arrays;

/**
 * Presenter for organizer user.
 * */
public class OrganizerUserPresenter {
    private TextPresenter textPresenter = new TextPresenter();
    private TableGenerator tableGenerator = new TableGenerator();

    /**
     * Print main menu for organizer user.
     * */
    public void printMenu() {
        System.out.println("\n----------------------------------------------------------------------------------------------");
        System.out.println("Welcome to organizer menu!");
        System.out.println("Here are available commands:");

        System.out.println("Create A New Event");
        textPresenter.printSuccessText(tableGenerator.formatTitles(Arrays.asList("add-talk", "add-party","add-panel-discussion" ), 30));
        System.out.println("-----------------------------------------------------------------------------------------------");
        System.out.println("Update An Event");
        textPresenter.printSuccessText(tableGenerator.formatTitles(Arrays.asList("modify-talk", "modify-party","add-speaker-to-panel-discussion"), 30));
        textPresenter.printSuccessText(tableGenerator.formatTitles(Arrays.asList("modify-panel-discussion", "cancel-event"), 30));

        System.out.println("-----------------------------------------------------------------------------------------------");
        System.out.println("Message System");
        textPresenter.printSuccessText(tableGenerator.formatTitles(Arrays.asList("send-message", "see-all-messages","delete-message"), 30));
        textPresenter.printSuccessText(tableGenerator.formatTitles(Arrays.asList("see-unread", "mark-as-unread"), 30));
        textPresenter.printSuccessText(tableGenerator.formatTitles(Arrays.asList("see-archive", "archive-message"), 30));
        System.out.println("-----------------------------------------------------------------------------------------------");

        textPresenter.printNormalText("Create New Room");
        textPresenter.printSuccessText("add-room");
        System.out.println("-----------------------------------------------------------------------------------------------");

        textPresenter.printNormalText("Create New User Account");
        textPresenter.printSuccessText(tableGenerator.formatTitles(Arrays.asList("add-attendee", "add-speaker"), 30));
        textPresenter.printSuccessText(tableGenerator.formatTitles(Arrays.asList("add-organizer", "add-admin"), 30));
        System.out.println("-----------------------------------------------------------------------------------------------");

        textPresenter.printNormalText("Request System");
        textPresenter.printSuccessText(tableGenerator.formatTitles(Arrays.asList("view-request"), 30));
        textPresenter.printInfoText("logout");
        System.out.println("-----------------------------------------------------------------------------------------------");
    }
}
