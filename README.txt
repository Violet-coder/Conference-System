1.**Background**
This project is a conference system that allows users to participate in conferences, organizers to manage conferences and all types of users to communicate with each other.
The interface of the project is text UI, users could read menu items from screen printings and enter the command from the keyboard to interact with the program.


2.**Configuration**
Please set the working directory as “../group_0129”, and mark "../group_0129/phase1/src" as source root to run the program.


3.**List of Features**
Mandatory Extensions: 1-5
Optional Entensions: Number 1, 2, 3, 4，5
1. Allow the same users to log in and select which conference they want to participate in. Here, participation means viewing and signing up for events. The inbox can be conference-specific, or one general inbox for all messages from all conferences to that user. You decide which one.


2. Enhance the user's messaging experience by allowing them to "mark as unread", delete, or archive messages after reading them.


3. Have the program produce a neatly formatted program or schedule for the conference that users have the option of "downloading" (outputting it as html, pdf, or similar). Alternatively, if you just want the program to print the schedule to the screen, then users should be able to request a schedule by at least three of: day, by speaker, by time (all 3-4 pm talks on all days), or just the ones they have signed up for, or "liked" events (where you have to enable users to "like" events).


4. Add additional constraints to the scheduling for various types of events (e.g. technology requirements for presentations, tables vs rows of chairs). When organizers are creating events, they can see a suggested list of rooms that fit the requirements of their event.


5.  Allow the system to support additional user requests (e.g. dietary restrictions, accessibility requirements) where organizers can tag a request as "pending" or "addressed". All organizers see the same list of requests, so if one tags a request as addressed, all other organizers can log in and see that.


New Features:








4.**Usage**
Run mainController.java to start the system. 
We have a built-in organizer account(username:group_0129, password:group_0129) for TA to login as an organizer without registration. 
Users interact with the program by entering command from the keyboard.
There are four different user types: organizer, admin, speaker, attendee. Organizer and admin users can only be created by existing organizers.
To test functionalities for speaker and user, please register first.


4.1 **Before login:**
Users will see the welcome message(“Welcome to use the Conference Management System”) after entering the system.
Home menu:
**Exit**: enter Exit will shutdown the system.


4.2 **Register**: enter Register will direct the user to the register interface, follow the prompts to enter username, password and usertype(enter the number) to register(Note: username can not be the name which has already in the database(user.txt)) 
Success message: 
"Sign up successfully and now you could login."
Failure message: 
“The username should only contain alphanumeric characters (A-Z, a-z or 0-9) or underscore, and should begin with a letter.” (illegal username format).”
“The username should be 6-30 characters long.(illegal username length).”
“The password should be 10-30 characters long.”(illegal password length).”
“Please enter the right number of the option.” (illegal usertype option)
“The username is already taken.”(Duplicate username)
“Sign up failed.” (fail to write in database)


4.3 **Login**: enter Login will direct the user to the login interface, follow the prompts to enter username and password. If login successfully the user will be directed to his userpage, otherwise he will be directed to the home menu with an error message.
Success message: “Login successfully.”
Failure message: “Login failed: wrong username or password.”


4.4 **After login:**
Login successfully to see the menu according to usertype.
4.4.1 **Login as organizer:**
Organizer menu:
1. **add-talk**: create a talk in the conference system.
A talk is an event that has exactly one speaker. Enter “add-talk” and follow the instructions to enter the values for all the parameters. Double-booking of the speaker or room (the specified time would cause a time conflict of the speaker or room) is prohibited.
Title: Can contain any characters. Can be of any length greater than 0.
Speaker ID: Can be the ID of any available speaker. The list of all speakers will be shown before it is entered. After it is entered, the time intervals when that speaker is occupied will be shown.
Event capacity: It is the maximum number of attendees of the event. Can be any positive integer no greater than the capacity of the largest room.
Technology requirements: Can be chosen from five options (A: Projector and Screen, B: Sound System, C: Podium and Microphone, D: High Speed Wired and Wireless Internet, E: LED Monitor). Multiple requirements are permitted.
Room conditions: Can be chosen from five options (A: Flexible Room Arrangement, B: Bar Counter, C: Dining Area, D: Double-height Window and City View, E: Sound Proofing). Multiple conditions are permitted.
Room ID: Can be chosen from a list of recommended rooms. The list contains all the rooms that meet the event capacity, technology requirements and room conditions. If no such room exists, technology requirements and room conditions will be asked again, until there is a suitable room. After the room ID is entered, the time intervals when that room is occupied will be shown.
Starting date: Can be any date not before today. Must be in “YYYY-MM-DD” format.
Ending date: Can be any date not before the starting date. Must be in “YYYY-MM-DD” format.
Starting time: Can be any time of a day. Must be in “hh:mm” format.
Ending time: If the starting date and ending date are the same day, the ending time must be after the starting time. If not, it can be any time of a day. Must be in “hh:mm” format.
Success message: 
“The following talk has been scheduled:
ID: the event ID assigned by the system
Title: the title user has entered
Speaker ID: the speaker ID user has entered 
Start Date: the start date user has entered
End Date: the end date user has entered
Start Time: the starting time user has entered
End Time: the end time user has entered
Room ID: the room ID user has entered
Capacity: the event capacity user has entered”
Failure message: 
“Unable to schedule the talk. The speaker has a time conflict.”
“Unable to schedule the talk. The room has a time conflict.”


2. **add-party**: create a party in the conference system. 
A party is an event that has no speaker. Enter “add-party” and follow the instructions to enter the values for all the parameters. Double-booking of the room (the specified time would cause a time conflict of the room) is prohibited.
Title: Can contain any characters. Can be of any length greater than 0.
Event capacity: It is the maximum number of attendees of the event. Can be any positive integer no greater than the capacity of the largest room.
Technology requirements: Can be chosen from five options (A: Projector and Screen, B: Sound System, C: Podium and Microphone, D: High Speed Wired and Wireless Internet, E: LED Monitor). Multiple requirements are permitted.
Room conditions: Can be chosen from five options (A: Flexible Room Arrangement, B: Bar Counter, C: Dining Area, D: Double-height Window and City View, E: Sound Proofing). Multiple conditions are permitted.
Room ID: Can be chosen from a list of recommended rooms. The list contains all the rooms that meet the event capacity, technology requirements and room conditions. If no such room exists, technology requirements and room conditions will be asked again, until there is a suitable room. After the room ID is entered, the time intervals when that room is occupied will be shown.
Starting date: Can be any date not before today. Must be in “YYYY-MM-DD” format.
Ending date: Can be any date not before the starting date. Must be in “YYYY-MM-DD” format.
Starting time: Can be any time of a day. Must be in “hh:mm” format.
Ending time: If the starting date and ending date are the same day, the ending time must be after the starting time. If not, it can be any time of a day. Must be in “hh:mm” format.
Success message: 
“The following party has been scheduled:
ID: the event ID assigned by the system
Title: the title user has entered
Start Date: the start date user has entered
End Date: the end date user has entered
Start Time: the starting time user has entered
End Time: the end time user has entered
Room ID: the room ID user has entered
Capacity: the event capacity user has entered”
Failure message: 
“Unable to schedule the party. The room has a time conflict.”


3. **add-panel-discussion**: create a panel discussion in the conference system. 
A panel discussion is an event that has one or more speakers. Enter “add-panel-discussion” and follow the instructions to enter the values for all the parameters. Note that only one speaker can be added using this command. If more speakers need to be added, use the “add-speaker-to-panel-discussion” command later. Double-booking of the speaker or room (the specified time would cause a time conflict of the speaker or room) is prohibited.
Title: Can contain any characters. Can be of any length greater than 0.
Speaker ID: Can be the ID of any available speaker. The list of all speakers will be shown before it is entered. After it is entered, the time intervals when that speaker is occupied will be shown.
Event capacity: It is the maximum number of attendees of the event. Can be any positive integer no greater than the capacity of the largest room.
Technology requirements: Can be chosen from five options (A: Projector and Screen, B: Sound System, C: Podium and Microphone, D: High Speed Wired and Wireless Internet, E: LED Monitor). Multiple requirements are permitted.
Room conditions: Can be chosen from five options (A: Flexible Room Arrangement, B: Bar Counter, C: Dining Area, D: Double-height Window and City View, E: Sound Proofing). Multiple conditions are permitted.
Room ID: Can be chosen from a list of recommended rooms. The list contains all the rooms that meet the event capacity, technology requirements and room conditions. If no such room exists, technology requirements and room conditions will be asked again, until there is a suitable room. After the room ID is entered, the time intervals when that room is occupied will be shown.
Starting date: Can be any date not before today. Must be in “YYYY-MM-DD” format.
Ending date: Can be any date not before the starting date. Must be in “YYYY-MM-DD” format.
Starting time: Can be any time of a day. Must be in “hh:mm” format.
Ending time: If the starting date and ending date are the same day, the ending time must be after the starting time. If not, it can be any time of a day. Must be in “hh:mm” format.
Success message: 
“The following panel discussion has been scheduled:
ID: the event ID assigned by the system
Title: the title user has entered
Speaker IDs: [the speaker ID user has entered]
Start Date: the start date user has entered
End Date: the end date user has entered
Start Time: the starting time user has entered
End Time: the end time user has entered
Room ID: the room ID user has entered
Capacity: the event capacity user has entered”
Failure message: 
“Unable to schedule the panel discussion. The speaker has a time conflict.”
“Unable to schedule the panel discussion. The room has a time conflict.”


4. **add-speaker-to-panel-discussion**: add a speaker to a scheduled panel discussion. 
Enter the ID of a speaker and the ID of an existing panel discussion. The speaker will be added to the panel discussion if the speaker has no time conflict, otherwise the command will fail.


5. **modify-talk**: change the parameters of a scheduled talk. 
Enter the ID of a speaker. All the talks that are going to be given by that speaker will be shown. Select one of them by entering its ID to modify it. For each parameter, enter “y” to change the value or “n” to keep the original value. Note that if the event capacity is changed to a value greater than the capacity of the original room, a new room with enough capacity has to be specified. Note also that if the starting datetime is changed to a value later than the original end datetime, a new valid end datetime has to be specified. If the new time of the event causes any speaker or room conflict, the command will fail.


6. **modify-party**: change the parameters of a scheduled party. 
All the open parties will be shown. Select one of them by entering its ID to modify it. For each parameter, enter “y” to change the value or “n” to keep the original value. Note that if the event capacity is changed to a value greater than the capacity of the original room, a new room with enough capacity has to be specified. Note also that if the starting datetime is changed to a value later than the original end datetime, a new valid end datetime has to be specified. If the new time of the event causes any room conflict, the command will fail.


7. **modify-panel-discussion**: change the parameters of a scheduled panel discussion.
All the open panel discussions will be shown. Select one of them by entering its ID to modify it. For each parameter, enter “y” to change the value or “n” to keep the original value. Note that if the event capacity is changed to a value greater than the capacity of the original room, a new room with enough capacity has to be specified. Note also that if the starting datetime is changed to a value later than the original end datetime, a new valid end datetime has to be specified. If the new time of the event causes any room conflict, the command will fail. The speaker(s) cannot be modified in this command. Use “add-speaker-to-panel-discussion” instead.


8. **view-request**: view all requests from all attendees.
The detailed info (request ID, description, status, timestamp, attendee ID and event ID) of all requests from all attendees will be shown. Enter the ID of a request to tag it as “addressed” or “rejected”. The user sent that request can see the status update as well.


9. **add-room**: create rooms in the conference system. 
Enter add-room to follow the instruction to enter the room name and room capacity. 
Then there will be multiple-choice questions of technology requirements and room conditions for organizers to add rooms with corresponding requirements.
Technology requirements: Can be chosen from five options (A: Projector and Screen, B: Sound System, C: Podium and Microphone, D: High Speed Wired and Wireless Internet, E: LED Monitor). Multiple requirements are permitted.
Room conditions: Can be chosen from five options (A: Flexible Room Arrangement, B: Bar Counter, C: Dining Area, D: Double-height Window and City View, E: Sound Proofing). 
Organizers could choose requirements to add the room he wants to the system.
Organizers will see the detailed information of the room he just added after entering all the required information correctly and back to the organizer menu.
Success message:
"The following room has been added: 
Room ID: room ID of the new room
Room Name: the name user has entered
Capacity: the capacity user has entered"
Technology Requirements: Projector and Screen
Room Conditions: Flexible Room Arrangement, Bar Counter, Dining Area
Failure message: 
“Unable to add the room. The name of the room is already taken.”


10. **add-speaker**: add speaker in the conference system
Enter add-speaker to follow the instructions to enter username, password(Note that the password should be 10-30 characters long) and speaker name.
Organizers will see the detailed information of the speaker he just added after entering all the required information correctly and back to the organizer menu.
Success message:
"The following speaker has been added: 
Name: the name user has entered
ID: the speaker ID of the new speaker
Username: the username the user has entered         
Talks to give: the talks this new speaker is going to give”        
Failure message: 
"Unable to add the speaker. The username is already taken."
        
11. **add-attendee**: ibid, the type of the new user is attendee


12. **add-organizer**: ibid, the type of the new user is organizer, organizer only could be created via organizer.


13. **add-admin**: ibid, the type of the new user is admin, admin only could be created via organizer.


14. **send-message**: send message to selected group or user
As an organizer, one can choose to send messages to all speakers, all attendees, one speaker and one attendee.
If the organizer chooses to send to one speaker or attendee, he needs to input the username of the person. And then, system will ask him to input the context, and the message will be sent.
Note that since no messages can be sent to organizer, the organizer have no choice to read messages.


15. **cancel-event**: if the organizer enters cancel-event, he will see all unexpired events constructed by him in a table. If he hasn't added any events, the error message "You haven't added any events so far." will show up instead of the events table. After seeing the events information, the organizer could enter the event ID to cancel the event. If the event is canceled successfully, the success message "Delete the following talk successfully!" and the information of  this event will show up, all the information related to this event will be updated including room, speaker and attendees.


16. **logout**: back to the home menu


4.4.2 **Login as attendee:**
Attendee Menu: The attendee menu displays the available commands that the attendee user can use.
1. **see-events-I-can-sign-up**:  Display all available events that the current user can sign up, including the unexpired events the user doesn’t enroll in.
Under the attendee menu, the user enters the command “see-events-I-can-sign-up” and will be directed to the secondary menu.
* **download-a-html-version**: All events that the user hasn't signed up are saved to a html file named schedule.html under userDownload folder.
* **print-all-events-on-screen**: All events that the user hasn’t signed up are printed on screen in a table.
* **find-my-interested-event**: A user can choose to filter by time, date or event title. Notice that it is case sensitive.


2. **sign-up-for-an-event**: After seeing the list of available events, the attendee user can enter the command “sign-up-for-an-event” and enter an event ID he/she wants to sign up. The program will check non-existing, expired events or events he/she has already enrolled.


3. **go-back**: After seeing the list of available events, the attendee user could also choose to go back to the last Attendee Menu by entering the command “go-back”.


4. **see-events-I-have-signed-up**: it will display all the events the current attendee user has signed up with schedule, room and speaker information.


Following the event list that the attendee user has signed up, it will display a submenu that contains three available commands: cancel-an-event, send-additional-request and go-back.


Submenu Under see-events-I-have-signed-up: 
5. **cancel-an-event**: By entering this command, the attendee user will be required entering an event ID that he/she wants to cancel. The user is not allowed to cancel an event that has already started, a non-existing event, or an event he/she didn’t sign up for.


6. **send-additional-request**: Enter the ID of an event the attendee has signed up for, and the request the attendee has for that event. A request can be dietary restrictions, accessibility requirements, etc. The request will be sent to all organizers for them to review.


After success or failure of an action including signing up for an event or canceling an event, the attendee user will be redirected to the Attendee Menu.


7. **review-my-request**: Review all the requests from the current attendee. The status of a request will be updated on the attendee side as an organizer updates it.


8. **send-message**: send message to another user
As an attendee, one can choose to send messages to one speaker or one attendee.
He needs to input the **username** of the person. And then, the system will ask him to input the context, and the message will be sent.


9. **read-message**: read and reply to messages
As an attendee, one can input read-message to read all the messages sent to him. Every message will contain the sender's username, context and time.
After all messages are listed, one can choose to reply to a message by input the username and context.


10. **logout**: back to the home menu


4.4.3 **Login as speaker:**
Speaker Menu:
1. **see-my-talks**: see all the talks that are given by the current speaker user ordered by time.


2. **send-message**: send message to selected group or user
As a speaker, one can choose to send messages to all attendees of all of his talks, all attendees of one of his talks, or one attendee.
If the organizer chooses to send to one attendee, he needs to input the username of the person. And then, the system will ask him to input the context, and the message will be sent.
Notice that since a speaker may receive and reply to the messages from attendees that haven't sign up for any of his talk, or a speaker may want to advertise himself,
if a speaker choose to send message to one attendee, he can enter all usernames but not just the ones that signed up for his talk.
However, send-to-all-attendees and send-to-all-attendees-of-one-talk limits the receiver in the ones that signed up for the speakers talks.


3. **read-message**: read and reply to messages
As a speaker, one can input read-message to read all the messages sent to him. Every message will contain the sender's username, context and time.
After all messages are listed, one can choose to reply to a message by input the username and context.


4. **logout**: back to the home menu


Message functionality:
5. **see-archive**: shows the archive where users can select messages they would like to “favourite” to be shown separate from all their messages.
6. **archive -message**:pick a valid message ID to be archived.A message will be printed indicating successful action. If the message is already archived, if the message id does not exist, then a failure message will be displayed.
7. **see-all-messages**: Shows all messages the user currently has.
8. **see-unread**:Will show all messages which the user has not yet viewed. Once the command is called, all currently viewed messages will be marked as read
9. **mark-as-unread**: pick a valid message ID to be marked as unread. Once the message is marked as unread, it cannot be marked as unread again until it is viewed. A message will notify the user if the message is successfully marked as unread or if failure due to invalid id, already marked, etc.
10. **delete-message**: Enter a valid id for a message to be deleted. Will be deleted from unread, archive, and message inbox Message will indicate successfully deleted or reason for failure.


4.4.4 **Login as Admin**
1. **delete-message**: list all the stored messages and admin user can delete a message by entering its message id.
2. **delete-message-by-username**: enter a username and the program will list all the messages received or sent by that user. Then admin can delete the message by specifying its id.
3. **delete-message-by-keyword**: enter a keyword and it will display all the messages containing that keyword. Admin can choose to delete all messages containing that keyword or delete one of them.
4. **delete-empty-talks**: it will list all the talks having no attendees. Admin can choose to delete an empty talk by specifying its talk id. Admin is only allowed to delete an empty talk.
5. **view-all-talks**: list all the talks stored in the conference system in a table.
6. **view-all-speakers**: list all the speakers in a table.
7. **view-all-rooms**: list all the rooms in a table.
8. **logout**: back to home menu.