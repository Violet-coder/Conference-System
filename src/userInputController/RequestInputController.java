package userInputController;

import controller.ClassCollection;
import controller.RequestController;
import presenter.TextPresenter;

import java.util.Scanner;

/**
 * the request input controller handles user input and communicate between request controller and presenter
 */
public class RequestInputController {
    private ClassCollection classCollection;
    private RequestController requestController;
    private Scanner sc;
    private TextPresenter presenter;

    /**
     * the constructor of request input controller
     * @param classCollection
     */
    public RequestInputController(ClassCollection classCollection) {
        this.classCollection = classCollection;
        this.requestController = classCollection.requestController;
        this.sc = new Scanner(System.in);
        this.presenter = new TextPresenter();
    }

    /**
     * handle input of sending request
     * @param userId user id
     * @param talkId talk id
     */
    public void sendRequest(int userId, int talkId) {
        presenter.printNormalText("Enter your additional request:");
        String requestContent = sc.nextLine();
        String output = requestController.createRequest(requestContent, userId, talkId);
        presenter.printNormalText(output);
    }

    /**
     * handle input of read request
     */
    public void readRequest() {
        String output = requestController.getRequest();
        presenter.printNormalText(output);
        presenter.printNormalText("Please enter request Id to indicate you want to process, or enter cancel to return.");
        String cmd = sc.nextLine().trim();
        if (cmd.equalsIgnoreCase("cancel")) {
            return;
        }
        else if (requestController.checkExist(Integer.parseInt(cmd))) {
            presenter.printNormalText("What to you want to do with the request? 1 for addressed, 2 for rejected.");
            int status = Integer.parseInt(sc.nextLine().trim());
            if (status!=1 && status!=2) {
                presenter.printErrorText("invalid command.");
                return;
            }
            output = requestController.markRequest(Integer.parseInt(cmd), status);
            presenter.printNormalText(output);
        }
        else {
            presenter.printErrorText("invalid command.");
            return;
        }
    }

    /**
     * handle input of review request
     * @param id user id
     */
    public void reviewRequest(int id) {
        String output = requestController.reviewRequest(id);
        presenter.printNormalText(output);
    }
}
