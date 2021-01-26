package presenter;

import java.util.List;

/**
 * Print text messages to screen.
 */
public class TextPresenter {

    private final String TEXT_RESET = "\u001B[0m";
    private final String TEXT_RED = "\u001B[31m";
    private final String TEXT_GREEN = "\u001B[32m";
    private final String TEXT_BLUE = "\u001B[34m";
    private static final String TEXT_YELLOW = "\u001B[33m";


    /**
     * Print a text message in white color.
     * @param text      Content of the message.
     */
    public void printNormalText(String text) {
        System.out.println(text);
    }

    /**
     * Print a text message in green color.
     * @param text      Content of the message.
     */
    public void printSuccessText(String text) {
        System.out.println(TEXT_GREEN+text+TEXT_RESET);
    }

    /**
     * Print a text message in red color.
     * @param text      Content of the message.
     */
    public void printErrorText(String text) {
        System.out.println(TEXT_RED+text+TEXT_RESET);
    }

    /**
     * Print a text message in blue color.
     * @param text      Content of the message.
     */
    public void printInfoText(String text) {
        System.out.println(TEXT_BLUE+text+TEXT_RESET);
    }

    /**
     * Print a text message in yellow color.
     * @param text      Content of the message.
     */
    public void printNotificationText(String text) { System.out.println(TEXT_YELLOW+ text +TEXT_RESET); }

    /**
     * Print a divider line.
     */
    public void printDividerLine() { System.out.println("---------------------------------------------------------\n"); }

    /**
     * Print a formatted table.
     * @param header        List of column names.
     * @param rows          List of rows. Each row is a list of values of all columns.
     */
    public void printTable(List<String> header, List<List<String>> rows) {
        TableGenerator tableGenerator = new TableGenerator();
        tableGenerator.printAsTable(header, rows);
    }


}
