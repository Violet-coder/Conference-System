package controller;
import gateway.UserGateway;

/**
 * LoginController: handle the login process for users
 *                  provide setter and getter for the userid and usertype
 */

public class LoginController {


    private int userId;
    private String userType;



    UserGateway userGateway = new UserGateway();

    /**
     * Dealwith login process for users:get the username ands password entered to search in the database for the account.
     * @param username      The username of the user account.
     * @param password      The password of the user account.
     * @return
     *                      true if find the account in the database.
     *                      false if not can not find the account in the database.
     */
    public boolean login(String username, String password){
        boolean flag = userGateway.isLogin(username, password);
        if(flag){
            setUserType(username, password);
            setUserId(username);
        }
        return flag;
    }

    //Set user type
    private void setUserType(String username, String password){
        String userType = userGateway.getUserType(username,password);
        if(userType!=null){
            this.userType = userType;
        }
    }

    //Set user Id
    private void setUserId(String username){
        int userId = userGateway.getUserIdByName(username);
        if(userId!=-1){
            this.userId = userId;
        }
    }

    //Get user type
    public String getUserType() {
        return this.userType;
    }

    //Get user Id
    public int getUserId(){
        return this.userId;
    }


}
