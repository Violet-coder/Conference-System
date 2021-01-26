package gateway;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

/**
 * the gateway handles input and save it to a html file
 */
public class htmlGateway {
    /**
     * this method downloads a html file to system
     * @param eventList the list of all events passed by controller
     */
    public void downloadHTML(StringBuilder eventList) {
        try{
            PrintStream printStream = new PrintStream(new FileOutputStream("./phase2/userDownload/Schedule.html"));
            try{
                printStream.println(eventList.toString());
                System.out.println("The file is downloaded to userDownload folder.");
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
