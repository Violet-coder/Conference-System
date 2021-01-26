# List of Features

## Mandatory Extensions

- There will now be many types of events

  - **Talk** with only one speaker
  - **Discussion** with multiple speakers
  - **Party** with no speakers

  Can be found in organizer dashboard as ```add-talk```, ```add-panel-discussion``` and ```add-party```. Note that if you want to have more than one speakers for discussion, there is a individual command ```add-speaker-to-panel-discussion```.

- Events can be canceled by at least one organizer.

  - Every organizer can cancel events that are created by him.

  Can be found in organizer dashboard as ```cancel-event```.

- At least one more type of user will be included in your program.

  - **Admin** user can delete messages by sender, keyword or id.
  - **Admin** user can also delete empty talks.
  - **Admin** user can also view all talks, speakers or rooms.

  An admin user **must be added by an organizer**. All functionalities can be accessed after login as an admin.

- Organizers can also create Speaker accounts, Attendee accounts, and any other type of user accounts that are in your program.

  - Organizer can create accounts for **speaker**, **attendee**, **admin** and **other organizer**.

  Can be found in organizer dashboard as ```add-speaker```, ```add-attendee```, ```add-admin```, and ```add-organizer```.

- Each event has a maximum number of people who can attend it.

  - Each **event has a capacity** to limit the maximum number of attendees. This property is set when creating an event, and can be modified by organizer.
  - Every **room has a capacity** too. In the process of creating events, if event capacity is larger than room capacity, then the room is not available.

  Can be found in organizer dashboard as ```add-talk``` (or other types of events), ```add-room``` and ```modify-talk``` (and other types of events).

## Optional Extensions

- Enhance the user's messaging experience by allowing them to "mark as unread", delete, or archive messages after reading them.

  - A user can **send message**, **read message**, **reply message**, **mark as unread**, **mark as archive**, **read unread message**, **read achieved message**. After a message is read, it is automatically mark as read.

  Can be found in organizer, speaker and attendee dashboards with corresponding comments.

- Have the program produce a neatly formatted program or schedule for the conference that users have the option of "downloading". Alternatively, users should be able to request a schedule by at least three types of filters.

  - A **HTML version** of all events is downloaded to **userDownload** folder, which is parallel to src folder.
  - A user can search for events by **date**, **time** and **title**, or choose to only read the events that he have signed up.

  Can be found in attendee dashboard, ```view-events-I-can-sign-up``` and ```view-events-I-have-signed-up```.

- Add additional constraints to the scheduling for various types of events.

  - All rooms have **technology equipment** and **conditions**.
  - When creating events, system will ask for your required equipment and condition.
  - If a room meets the requirements, it will be suggested by system when creating events. If no room meets the requirements, system will suggest the room that meets some of the requirements.

  Can be found in organizer dashboard, ```add-room``` and ```add-talk``` (and other types of events).

- Allow the system to support additional user requests.

  - A user can send additional request for a signed-up talk to organizer and review it at any time. The request's initial status is **Pending**.
  - Organizer can process the request and mark it as **Addressed** or **Rejected**. All organizers share the same request list with the updated status.

  Can be found in attendee dashboard, ```view-events-I-have-signed-up``` -> ```send-addidtional-request``` and ```review-my-request```. In organizer dashboard ```read-request```.

## Our Own Features

- Organizer can modify events.

  - All details of an event can be modified by organizer.

  Can be found in organizer dashboard. ```modify-talk``` (and other types of events).

- A user can register as attendee or speaker.

  Can be found in homepage, ```register```.

- For some of optional extensions, we did more than required.