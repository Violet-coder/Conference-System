package presenter;

import java.util.Arrays;

/**
 * Print menus and options for Admin user.
 * */
public class AdminUserPresenter {
    private static final String TEXT_YELLOW = "\u001B[33m";
    private final String TEXT_RESET = "\u001B[0m";

    private TableGenerator tableGenerator = new TableGenerator();
    private TextPresenter textPresenter = new TextPresenter();
    /**
     * Print the Main menu for admin user.
     * */
    public void printAdminUserMenu() {
        System.out.println("\n---------------------------------------------------------");
        System.out.println("Welcome to admin menu!");
        System.out.println("Here are available commands("+TEXT_YELLOW+"Enter the number of command"+TEXT_RESET+"):");
        System.out.println("Message Management");
        textPresenter.printSuccessText(tableGenerator.formatTitles(Arrays.asList("1.delete-message", "2.delete-message-by-username"), 30));
        textPresenter.printSuccessText("3.delete-message-by-keyword");
        System.out.println("---------------------------------------------------------");

        System.out.println("Event Management");
        textPresenter.printSuccessText(tableGenerator.formatTitles(Arrays.asList("4.delete-empty-talks", "5.view-all-talks"), 30));

        System.out.println("---------------------------------------------------------");
        System.out.println("Other Functionalities");
        textPresenter.printSuccessText(tableGenerator.formatTitles(Arrays.asList("6.view-all-speakers", "7.view-all-rooms"), 30));
        textPresenter.printInfoText("8.logout");
        System.out.println("---------------------------------------------------------");
    }

    /**
     * Print optional commands that user can select when delete message by keyword.
     * */
    public void printMenuForDeleteMsg() {
        System.out.println("---------------------------------------------------------");
        System.out.println("What would you like to do next?");
        System.out.println("Here are the commands you can use("+TEXT_YELLOW+"Enter the number of command"+TEXT_RESET+"):");
        System.out.println("1.delete-message-by-id");
        System.out.println("2.delete-all-filtered-messages");
        System.out.println("3.go-back");
        System.out.println("---------------------------------------------------------");
    }

    /**
     * Print optional commands that user can select when delete message by userbane.
     * */
    public void printMenuForDeleteMsgByUsername() {
        System.out.println("---------------------------------------------------------");
        System.out.println("What would you like to do next?");
        System.out.println("Here are the commands you can use("+TEXT_YELLOW+"Enter the number of command"+TEXT_RESET+"):");
        System.out.println("1.delete-message-by-id");
        System.out.println("2.go-back");
        System.out.println("---------------------------------------------------------");
    }
}
