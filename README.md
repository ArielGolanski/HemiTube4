# HemiTube4
# לאחר שיחה עם המתרגל ובשל מקרה אישי וספציפי, קיבלנו אישור להגיש את החלק הנ"ל של הפרויקט עד ה20.9. לכן נבקש שלא לבדוק אותו לפני

our self-made logo: as you can see, this is hemi, holding the YouTube sign:

# Getting started:
To run the HemiTube website locally, follow these steps:

- Clone the repository

- Make sure that you have installed all required dependencies: npm i express cors body-parser mongoose custom-env path htpp multer

- if you have a data base calles "test", make sure to clear it complately to avoid problems

- in the terminal, type "cd server" so you can run the server itself

- Run the server using npm test, (or export NODE_ENV=test && node app.js for macOS/Linux, set NODE_ENV=test && node app.js for Windows). The server should be running.

- Open your browser and visit http://localhost:5001 to access the HemiTube website. (and so that the default videos and user will be uploaded to the DB)

- we added a default user called "Israel" and default videos (that Israel uploaded)
  
- open you android studio and clone the repository there
  
- open a new pixel 7 device with R
  
- run the app

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

<br />
<br />
<br />
<br />

lets start from skretch, lets make sure we connected to mongodb://localhost:27017 and the database called "test" is clear from users and videos:
<br />
![Screenshot 2024-07-20 165753](https://github.com/user-attachments/assets/ea07795c-f28e-4983-bcb4-25f9e997b340)
![Screenshot 2024-07-20 165805](https://github.com/user-attachments/assets/3bdd4073-f61f-45ce-912d-98bbda34daed)
![Screenshot 2024-07-20 165810](https://github.com/user-attachments/assets/9be2a9cf-a0bf-4fd8-a0a2-708cb3815756)

then we open the vsc and made clone to the repository then we open a terminal and run the server:
<br />
![Screenshot 2024-07-20 165921](https://github.com/user-attachments/assets/3ad32c75-08c8-441d-91a0-31ed85d0c71b)
![Screenshot 2024-07-20 165936](https://github.com/user-attachments/assets/a98a0faf-b776-4abd-a6cc-c41a6938f363)


we open the web app by entering http://localhost:5001/ to the search bar on the browser, and checked to see if the default user and default videos created:
<br />
![Screenshot 2024-07-20 170012](https://github.com/user-attachments/assets/fcffdaf3-f0ef-4398-80c6-4aa28a76c664)

then, open this package on android studio:
<br />
![Screenshot 2024-07-20 170112](https://github.com/user-attachments/assets/7eaad993-7999-42d5-8534-f82da4ab697c)

and run the app, recommended device is google pixel 7:
<br />
![Screenshot 2024-07-20 170228](https://github.com/user-attachments/assets/5c9c2f25-a44a-4d1f-a16e-9cbb4fd41b8a)

we can notice that the same videos from the web are now fetching in the android app, now lets register a user:
<br />
![Screenshot 2024-07-20 170506](https://github.com/user-attachments/assets/4ca4d9f3-6ff1-4b5e-b08f-c8b943ef8577)

we can see that the user has been added succesfuly to the mongodb database:
<br />
![Screenshot 2024-07-20 170530](https://github.com/user-attachments/assets/d7811ebb-0ed7-402b-bd60-7fda911741c8)

now lets login to the "FooBar" user we created:
<br />
![Screenshot 2024-07-20 170607](https://github.com/user-attachments/assets/e010aa5b-b029-4e2e-9be7-38f52b84626e)

and that "YouPage", here the user can see his uploads:
<br />
![Screenshot 2024-07-20 170623](https://github.com/user-attachments/assets/7d5ec3f7-dbbc-4258-bed5-02a0ac4b7ba5)

now lets upload a video:
<br />
![Screenshot 2024-07-20 170653](https://github.com/user-attachments/assets/56785941-b406-495a-a623-4c04c5510aa6)

we can see that the video uploaded succesfuly, and now displays on YouPage
<br />
![Screenshot 2024-07-20 170709](https://github.com/user-attachments/assets/514cda93-eeb2-436d-9055-89bf95377c06)

the video also being displayed on the web application, as the task required:
<br />
![Screenshot 2024-07-20 170727](https://github.com/user-attachments/assets/9b0c873c-1b19-4c8f-8e5a-4e221b7f60d4)

now lets look at the VideoViewPage:
<br />
![Screenshot 2024-07-20 170841](https://github.com/user-attachments/assets/768e61e5-a973-499b-8129-f416f777b3c0)

Lets add a comment to this video:
<br />
![Screenshot 2024-07-20 170901](https://github.com/user-attachments/assets/b51be848-5ce6-4691-9f50-cde91c19fde9)

the comment has been added succefuly and being displayed:
<br />
![Screenshot 2024-07-20 170919](https://github.com/user-attachments/assets/9f392172-32bd-4cb2-a635-f6c48e39e913)

the user that uploaded the comment has the permission to edit or delete the comment, lets edit:
<br />
![Screenshot 2024-07-20 170942](https://github.com/user-attachments/assets/b6567c76-8c5f-4d9f-b35b-5f855051c255)
![Screenshot 2024-07-20 170950](https://github.com/user-attachments/assets/85f8adb5-46b2-4b7d-a53d-ac9fd93cce2e)

now lets delete the comment:
<br />
![Screenshot 2024-07-20 171003](https://github.com/user-attachments/assets/caee1c67-9e33-4437-b0ac-89538edcf00d)

another thing we added is a link to a specific user uploads, when you are watching a user video for example, Israel's video and want to see all his uploads you can click on his name on VideoViewPage and move to this page:
<br />
![Screenshot 2024-07-20 171102](https://github.com/user-attachments/assets/5c54d62b-b06c-4b8a-8a4c-d967ce0238bf)

