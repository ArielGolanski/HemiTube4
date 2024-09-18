# HemiTube2
our self-made logo: as you can see, this is hemi, holding the YouTube sign
![HemitubeLogoHorizontal](https://github.com/ArielGolanski/HemiTube2/assets/170665000/815d8c61-4e42-4ad7-9559-4309ca1bc596)


# Getting started:
To run the HemiTube website locally, follow these steps:

1. Clone the repository

2. Make sure that you have installed all required dependencies: npm i express cors body-parser mongoose custom-env path htpp multer

3. if you have a data base calles "test", make sure to clear it complately to avoid problems 

4. in the terminal, type "cd server" so you can run the server itself

5. Run the server using npm test, (or export NODE_ENV=test && node app.js for macOS/Linux, set NODE_ENV=test && node app.js for Windows). The server should be running.

6. Open your browser and visit http://localhost:5001 to access the HemiTube website.

7. we added a default user called "Israel" and default videos (that Israel uploaded)

# API Functionality:
The HemiTube website incorporates the following API endpoints and functionalities:

User:

- GET /api/users/:id Retrieve user details by providing the username.
- POST /api/users Create a new user.
- PUT /api/users/:id Change nickName or profilePicture for a specific user.
  
Token:
- POST /api/tokens: Generate a JSON Web Token (JWT) for registered users.
  
Videos:
- GET /api/users/:id/videos Retrieve the list of videos of the user.
- POST /api/users/:id/videos Create a new video.
- GET /api/users/:id/videos/:pid Retrieve a specific video.
- PUT /api/users/:id/videos/:pid Updates the description or title of a video
- DELETE /api/users/:id/videos/:pid Delete a specific video.
- Get /api/videos Retrieve the list of top 20 videos in the app.
- Get /api/videosHemi Retrieve the list of all the videos in the app.

Comments:

- GET /api/users/:id/videos/:pid/comments Retrieve the list of comments in a specific video.
- POST /api/users/:id/videos/:pid/comments Adds a comment to specific video
- GET /api/users/:id/videos/:pid/comments/:cid Gets a specific comment from a video
- PUT /api/users/:id/videos/:pid/comments/:cid Updates a specific comment
- DELETE /api/users/:id/videos/:pid/comments/:cid Delets a specific comment from a video.
  

Notice that we expanded the required API, but we ensured that the core functionalities remained intact.

# Some information about the app and how it works 

- in the signUp page, you will get an alert if you try to sign-up with an existing useranme ("invalid useranme" and 409 from the server),
   you will get a different alert if the fields are not filled properly.
- in the login page, you will get an alert if your password or username are incorrect.
- if the login worked (you filled a valid username and password), we will generate you a JWT, that will be used to monitor your access to things (like adding videos, comments, editing things etc')
- as a guest, you will have access to the videos and comments, without a possibility to edit or delete them. This way, we can protect the information etch user uploads.
- you can search videos (by their title) using the search bar in the top of the page.
- also, you can go to user videos page if you press the link in the name of a publisher of an existing video, in this page, you can see all of this user videos, and entern them.
- as a registered user, you will have access to your profile page (where you can edit your profile photo and nickName)
- also, you will have the ability to add video (link to add video page from the home page as well), delete your videos and to comment on existing videos.
- pay attention, when you delete a user, all of its videos and comments are deleted as well.
- when you add a user, video or a comment, it will be added to the data base. so if you refresh a page or even run the app all over again, you will see all of the things you uploaded.

# some examples of how our app works:

- the home-page you see as a logged-in registered user :
![Screenshot 2024-07-01 193542](https://github.com/ArielGolanski/HemiTube2/assets/129782728/30887f32-c067-47cd-bab2-68f3aa4bfefa)

- using the search-bar:
![Screenshot 2024-07-01 193633](https://github.com/ArielGolanski/HemiTube2/assets/129782728/c0cdcfd1-b91b-47ad-8df7-00b08a57c52b)

- clicking on a video: (video-View page)
![Screenshot 2024-07-01 193700](https://github.com/ArielGolanski/HemiTube2/assets/129782728/0559a189-ca7a-42a1-a964-b4b56914f180)

- this is a video shelly1 uploaded, shelly1 is logged in so she can delete and esit the video (as well as add comments, like any logged0in user)
![Screenshot 2024-07-01 193712](https://github.com/ArielGolanski/HemiTube2/assets/129782728/eb4ab7c3-a582-4bde-9048-2c3b174d6dcd)

- after adding comments, you can see them, delete and edit them:
![Screenshot 2024-07-01 193746](https://github.com/ArielGolanski/HemiTube2/assets/129782728/4ff8bcfd-dcca-4a15-92ea-b572612dcccc)

- example of a user editing her video:
![Screenshot 2024-07-01 193822](https://github.com/ArielGolanski/HemiTube2/assets/129782728/7ae991c8-161c-4b88-86fe-2ab47df4aa0a)

- you can see the description of the video changed (both in display and in the data base) after pushing the save changes button:
![Screenshot 2024-07-01 194203](https://github.com/ArielGolanski/HemiTube2/assets/129782728/dc6b5ec7-9a6a-4d50-a944-c32218a6df25)

- example of a user editing her comment
![Screenshot 2024-07-01 194215](https://github.com/ArielGolanski/HemiTube2/assets/129782728/9453bda5-abfb-49ae-b221-8714d7c7a3b2)
![Screenshot 2024-07-01 194226](https://github.com/ArielGolanski/HemiTube2/assets/129782728/09332a7f-eb2f-4d50-9f88-b8a0a90e39a3)

- the new comment, after the edit is complated
![Screenshot 2024-07-01 194305](https://github.com/ArielGolanski/HemiTube2/assets/129782728/b941c0e2-7699-4d0a-8374-b6418d2eedb3)

- the profile page of a user:
![Screenshot 2024-07-01 194333](https://github.com/ArielGolanski/HemiTube2/assets/129782728/a734d091-7043-4c46-b63d-ecd44dc466a1)

- user editing the profile page and changing the personal nickname:
![Screenshot 2024-07-01 194359](https://github.com/ArielGolanski/HemiTube2/assets/129782728/7eef0fcf-dc88-49e2-93f3-8b857b6a3356)

- profile page after the edit is complated
![Screenshot 2024-07-01 194415](https://github.com/ArielGolanski/HemiTube2/assets/129782728/048add80-ae41-4984-9381-ca4b240d56df)

- user in the add video page
![Screenshot 2024-07-01 194446](https://github.com/ArielGolanski/HemiTube2/assets/129782728/9fc39492-cb85-4730-b966-7b492e2b936a)

- user filling the add video form
![Screenshot 2024-07-01 194517](https://github.com/ArielGolanski/HemiTube2/assets/129782728/cda37b12-edde-4159-a3a4-3841742bdeae)

- the home page, after submiting the add video form (the video is in the data base, therefor shown in the home page with all the other videos on the app
![Screenshot 2024-07-01 194530](https://github.com/ArielGolanski/HemiTube2/assets/129782728/cfe999cc-3dd6-4811-be5c-80e825cfe08b)

- entering someone elses video (as you can see, you dont have an edit or delete buttons):
![Screenshot 2024-07-01 194632](https://github.com/ArielGolanski/HemiTube2/assets/129782728/873909d2-3cca-4129-a4fe-3a44d9eb59e9)

-user about to log out:
![Screenshot 2024-07-01 194435](https://github.com/ArielGolanski/HemiTube2/assets/129782728/a25de1af-0457-4d52-b2d0-789c0db3cfe7)

- the home page you see as a guest (you dont have add video or log out buttons, but you do have now sign in and sign up):
![Screenshot 2024-07-01 194646](https://github.com/ArielGolanski/HemiTube2/assets/129782728/8085dc8c-e03c-4605-bb98-9c3a511f9e5b)

- when you enter a video as a guest, you can see the comments and thir author, but you can edit them:
![Screenshot 2024-07-01 194700](https://github.com/ArielGolanski/HemiTube2/assets/129782728/d942e8e7-90cd-449a-aac7-29b51dbdc78b)

- using the search bar in the video-view page:
![Screenshot 2024-07-01 194716](https://github.com/ArielGolanski/HemiTube2/assets/129782728/1beebe95-0a40-440d-bcc2-a7dd3580ae61)

- top videos page (in this example we only have 8 videos, so all the videos are shown. when there are more than 20, only the top 20 videos will be displayed):
![Screenshot 2024-07-01 194743](https://github.com/ArielGolanski/HemiTube2/assets/129782728/d53115f8-e4c2-4812-aeab-d8142d0bf1b4)

- our sighin page
![Screenshot 2024-07-01 194758](https://github.com/ArielGolanski/HemiTube2/assets/129782728/b2f3595e-e274-4f49-8c9d-843cfc54f4ec)

- you can choose to hide or display you password
![Screenshot 2024-07-01 194819](https://github.com/ArielGolanski/HemiTube2/assets/129782728/4582af46-7607-4b1c-bbf3-635ac56bd10f)
![Screenshot 2024-07-01 194830](https://github.com/ArielGolanski/HemiTube2/assets/129782728/31a2eb15-9f5e-4ec2-a369-ef44ff31209d)

- trying to login with an unexisting username:
![Screenshot 2024-07-01 194841](https://github.com/ArielGolanski/HemiTube2/assets/129782728/00e96c72-24f6-420f-a865-1a9151f245a5)

- signup page
![Screenshot 2024-07-01 194953](https://github.com/ArielGolanski/HemiTube2/assets/129782728/318426d3-f14b-449c-b03e-dd213914b712)

- the alerts you get when not filling all fiels properly
![Screenshot 2024-07-01 195052](https://github.com/ArielGolanski/HemiTube2/assets/129782728/d9360d23-388a-4cb2-ad9e-a75fa0b24b2d)
![Screenshot 2024-07-01 195110](https://github.com/ArielGolanski/HemiTube2/assets/129782728/667fa18c-c93c-44db-950e-4198c62c8a30)




























