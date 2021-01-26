package gateway;
import java.io.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


/**
 * UserGateway: handle input and interact with database(user.txt) which stored the information of all the user accounts.
 */

public class UserGateway {
    //Create file instance
    private static File textFile = new File("phase2/resources/user.txt");
    private static String AbsolutePath=textFile.getAbsolutePath();
    private static File file = new File(AbsolutePath);

    static {
        if (!file.exists()) {
            System.out.println("File does not exits");
            try {
                file.createNewFile();
//                System.out.println("User file has been created.");
            } catch (IOException e) {
//                e.printStackTrace();
            }
        }
    }

    /**
     * Insert user account data as a row in the database(user.txt).
     * @param userId      The userId of the user.
     * @param username    The username of the user.
     * @param password    The password of the user.
     * @param usertype    The usertype of the user.
     * @param name        The real name of the user.
     **/
    public void writeToTxt(int userId, String username, String password, String usertype, String name){
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file, true));
            // write data
            bw.newLine();
            bw.write(userId + "," + username+ "," + password+ "," + usertype + "," + name);
            bw.flush();
        }catch (IOException e) {
//            System.out.println("fail to register a new user");
        }finally{
            if (bw != null){
                try {
                    bw.close();
                } catch (IOException e) {
//                    System.out.println("fail to release the resource when register.");
                }
            }
        }

    }

    /**
     * Search the user account in the database(user.txt) by username and password.
     * @param username    The username of the user.
     * @param password    The password of the user.
     * @return
     *                     true if the row found.
     *                     false if failed to find the row.
     */
    public boolean isLogin(String username, String password){
        boolean flag = false;
        BufferedReader br = null;

        try{
            br = new BufferedReader(new FileReader(file));
            String line = null;
            //if the lines in the text file is not null, enter the loop
            while ((line = br.readLine()) != null){
                String[] strings = line.split(",");
                if(strings.length>1){
                    if(username.equals(strings[1])&&password.equals(strings[2])){
                        flag = true;

                    }
                }
            }
            br.close();

        } catch(Exception e){
//            e.printStackTrace();
        }
        return flag;
    }


    /**
     * Find the usertype of the user by username and password.
     * @param username      The username of the user.
     * @param password      The password of the user.
     * @return              The usertype of the user.
     *                      usertype if the row found.
     *                      null if failed to find the usertype.
     **/
    public String getUserType(String username, String password){
        String userType= null;
        BufferedReader br = null;
        try{
            br = new BufferedReader(new FileReader(file));
            String line = null;
            //if the lines in the text file is not null, enter the loop
            while ((line = br.readLine()) != null){
                String[] strings = line.split(",");
                if(strings.length>1){
                    if(username.equals(strings[1])&&password.equals(strings[2])){
                        userType=strings[3];
                    }
                }

            }
            br.close();

        } catch(Exception e){
//            e.printStackTrace();
        }
        return userType;
    }


    /**
     * Find the userId of the user by username.
     * @param username      The username of the user..
     * @return              The userId(non-negative number) of the user.
     *                      userId if the userId is found.
     *                      -1 if failed to find the userId.
     */
    public int getUserIdByName(String username){
        int userId = -1;
        BufferedReader br = null;
        try{
            br = new BufferedReader(new FileReader(file));
            String line = null;
            //if the lines in the text file is not null, enter the loop
            while ((line = br.readLine()) != null){
                String[] strings = line.split(",");
                if(strings.length>1){
                    if(username.equals(strings[1])){
                        userId = Integer.parseInt(strings[0]);
                    }
                }
            }
            br.close();

        } catch(Exception e){
//            e.printStackTrace();
        }
        return userId;
    }

    /**
     * Check if there is a username in the database as same as the user entered.
     * @param givenName     The username passed in.
     * @return
     *                      username if the username passed in is the same with one in the database.
     *                      null if failed to find the same username in the database.
     */
    public String getUsernameByGivenName(String givenName){
        String result = null;
        BufferedReader br = null;
        try{
            br = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = br.readLine()) != null){
                String[] strings = line.split(",");
                if (strings.length>1&&givenName.equals(strings[1])){
                    result = strings[1];
                }

            }
            br.close();

        } catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Find the username by userId and usertype.
     * @param userId     The userId passed in.
     * @param usertype     The usertype passed in.
     * @return
     *                      username if find the username by userId and usertype.
     *                      null if failed to find username by userId and usertype.
     */
    public String getUsernameByIdandUsertype(int userId, String usertype){
        String username = null;
        BufferedReader br = null;
        try{
            br = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = br.readLine()) != null){
                String[] strings = line.split(",");
                if(strings.length>1){
                    if(userId==(Integer.parseInt(strings[0]))&&usertype.equals(strings[3])){
                        username = strings[1];
                    }
                }

            }
            br.close();

        } catch(Exception e){
//            e.printStackTrace();
        }
        return username;
    }



}
