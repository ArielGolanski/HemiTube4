
# HemiTube4
our self-made logo: as you can see, this is hemi, holding the YouTube sign
![Image Alt](https://github.com/ArielGolanski/HemiTube4/blob/main/wiki/HemitubeLogoHorizontal.jpeg)


# Getting started:
To run the HemiTube website/android app, with the reccomendation algorithem from the TCp server:

1. Clone the repository

2. Make sure that you have installed all required dependencies: npm i express cors body-parser mongoose custom-env path htpp multer

3. if you have a data base calles "test", make sure to clear it complately to avoid problems 

4. open your Ubuntu terminal and write cd /mnt/c/Users/{the user you are in}/Hemitube4/cppServer (mnt/c/{the path to you cppServer})

5. compile the cpp server using 'g++ -std=c++11 cppServer.cpp -o sppServer'

6. run the cpp server using ./cppServer

7. in the terminal, type "cd server" so you can run the node.js server 

8. Run the server using npm test, (or export NODE_ENV=test && node app.js for macOS/Linux, set NODE_ENV=test && node app.js for Windows). The server should be running.

9. Open your browser and visit http://localhost:5001 to access the HemiTube website. (after that, you can also open the android app using the instructions in the android wiki page)

10. we added a default user called "Israel" and default videos (that Israel uploaded)

# API Functionality:
In addiotion to the routes we created in the previous parts, we also added:

GET /api/users/:id/videos/:pid/reccomeded Retrieve the recommended videos according to the video you just watched

# About the servers and their connection

In our node.js server, we have a function that sends and recives information from and to the TCP server. 

It sends a username and video id. 

If you are a registered and signed in user and you just watched a video, the function sends your username and the id of the video you just watched, to the TCP server. 

The server gats this information and returns (according to the reccomendation algorithem) a list of reccomended videos. in case there are less than 20 reccomended videos recives from the TCP server, our node.js server will complate the list using the most viwed videos from the app (the first videos you will see are the reccomended ones, and the last will be the top videos on the app).

in case you are the first person who watched the video and there are no reccomendations yet, you will get the top 20 videos.

In case tou are noe logged in, you will also get the top 20 videos, in this case, the videos you watch are not going to affect the algorithm because in case of an unregistered user, the TCP server is not getting any information.

In the web, you can see the reccomended videos on the side of the video-view page (on the left side, just like in youtube), and on the android app you can see the reccomended videos under the comments (also in the video view page, bit on the bottom of it.

- some more information about the TCP server:
  As we said, the server works according to the TCP protocol.
  The socket links between a requst and an other socket, the new socket handles the request and the main socket only handles the "linking" (it also enables multithreading).

# The reccomendation algorithm
The algorithm gets a username am dvideo id and returns the reccomended videos according to the video you watched (and according to the videos other peaople watched).

For example, if a watched the videos that their id's are 1-5, and the user Israel watched 5-10, so if a new user will watch video number 5, it will reaive from the reccomendation algorithm the videos 1-10 (those are the videos that people who watched video 5 watched as well).

# Demo Run using Recommendation Algorithm:

lets start from skretch, lets make sure we connected to mongodb://localhost:27017 and the database called "test" is clear from users and videos:
<br/>

![Image Alt](https://github.com/ArielGolanski/HemiTube4/blob/main/wiki/proof/Screenshot%202024-09-18%20134119.png)

then we open the ubuntu (you can use any other linux operator) and open the package "cppServer" in the project,
we will compile the cppServer using this line: "g++ -std=c++11 cppServer.cpp -o cppServer"
then we will run the server using this: "./cppServer"
<br/>

![Image Alt](https://github.com/ArielGolanski/HemiTube4/blob/main/wiki/proof/Screenshot%202024-09-18%20134914.png)

after this we gonna open the vsc and open the project package, we will open terminal, type "cd server", then "npm start"
and the nodejs server will start to work
<br/>

![Image Alt](https://github.com/ArielGolanski/HemiTube4/blob/main/wiki/proof/Screenshot%202024-09-18%20135001.png)

![Image Alt](https://github.com/ArielGolanski/HemiTube4/blob/main/wiki/proof/Screenshot%202024-09-18%20135032.png)

then we will open the chrome for example and we will enter http://localhost:5001/ to the search bar on the browser,
and checked to see if the default user and 20 default videos created:
<br/>


![Image Alt](https://github.com/ArielGolanski/HemiTube4/blob/main/wiki/proof/Screenshot%202024-09-18%20135115.png)

then, open "Android" package on android studio
and run the app, recommended device is google pixel 7:
<br/>

![Image Alt](https://github.com/ArielGolanski/HemiTube4/blob/main/wiki/proof/Screenshot%202024-09-18%20135428.png)

we will open random video and we can see that the algorithm return 20 videos for guest mode:
<br/>

![Image Alt](https://github.com/ArielGolanski/HemiTube4/blob/main/wiki/proof/Screenshot%202024-09-18%20135455.png)

we will register a user:
<br/>

![Image Alt](https://github.com/ArielGolanski/HemiTube4/blob/main/wiki/proof/Screenshot%202024-09-18%20135525.png)

![Image Alt](https://github.com/ArielGolanski/HemiTube4/blob/main/wiki/proof/Screenshot%202024-09-18%20135631.png)

we moved to the "You" page where the user can see all his uploads, at this moment the user dont upload any video
<br/>

![Image Alt](https://github.com/ArielGolanski/HemiTube4/blob/main/wiki/proof/Screenshot%202024-09-18%20135650.png)

now as we are logged in, we will let the algorithm work and i will build a group of videos that the algorithm going to return as recommended
i am going to watch from the logged user many videos that their thumbnail includes dogs, then i will create a new user and i will log in to it 
on the web app and we will going to see that the algorithm recommed us on the dogs videos we chose!
<br/>

![Image Alt](https://github.com/ArielGolanski/HemiTube4/blob/main/wiki/proof/Screenshot%202024-09-18%20135727.png)

![Image Alt](https://github.com/ArielGolanski/HemiTube4/blob/main/wiki/proof/Screenshot%202024-09-18%20135751.png)

![Image Alt](https://github.com/ArielGolanski/HemiTube4/blob/main/wiki/proof/Screenshot%202024-09-18%20135813.png)

![Image Alt](https://github.com/ArielGolanski/HemiTube4/blob/main/wiki/proof/Screenshot%202024-09-18%20135850.png)

![Image Alt](https://github.com/ArielGolanski/HemiTube4/blob/main/wiki/proof/Screenshot%202024-09-18%20135912.png)

also i added a video to show that this functionallity is still working:
<br/>

![Image Alt](https://github.com/ArielGolanski/HemiTube4/blob/main/wiki/proof/Screenshot%202024-09-18%20140029.png)

the video is now shown on my videos list:
<br/>

![Image Alt](https://github.com/ArielGolanski/HemiTube4/blob/main/wiki/proof/Screenshot%202024-09-18%20140101.png)

here we create the second user:
<br/>

![Image Alt](https://github.com/ArielGolanski/HemiTube4/blob/main/wiki/proof/Screenshot%202024-09-18%20140149.png)

here we entered to a dogs video and getting recommended to another dogs video as exepted:
<br/>

![Image Alt](https://github.com/ArielGolanski/HemiTube4/blob/main/wiki/proof/Screenshot%202024-09-18%20140322.png)

we also editing the video description to show that this functionallity is still working, for the rest of functionallities that was needed in part 2 and 3 of the project
you can check the other 2 readme files that showing the rest of functionallities.
<br/>

![Image Alt](https://github.com/ArielGolanski/HemiTube4/blob/main/wiki/proof/Screenshot%202024-09-18%20140330.png)

the comment has been added:
<br/>

![Image Alt](https://github.com/ArielGolanski/HemiTube4/blob/main/wiki/proof/Screenshot%202024-09-18%20140354.png)

now we are opening the web application, we signed as the second user
<br/>

![Image Alt](https://github.com/ArielGolanski/HemiTube4/blob/main/wiki/proof/Screenshot%202024-09-18%20140442.png)

we can see that the video we upload from the android is being shown in the homepage:
<br/>

![Image Alt](https://github.com/ArielGolanski/HemiTube4/blob/main/wiki/proof/Screenshot%202024-09-18%20140558.png)

now we are entering dogs thumbnail video and we can see that from all the videos we watched on the android app with the first user
our recommendation algorithm return us list of dogs thumbnails videos as exepted, also the algorithm is completing the list with (20 - (number of recommended videos))
videos, as the task requiered:
<br/>

![Image Alt](https://github.com/ArielGolanski/HemiTube4/blob/main/wiki/proof/Screenshot%202024-09-18%20140611.png)

now we see screenshots of the communication between the nodejs server to the cpp server:
<br/>

![Image Alt](https://github.com/ArielGolanski/HemiTube4/blob/main/wiki/proof/Screenshot%202024-09-18%20140721.png)

![Image Alt](https://github.com/ArielGolanski/HemiTube4/blob/main/wiki/proof/Screenshot%202024-09-18%20140736.png)

also we can see how the cpp server working, handeling sockets from the client and generate recommendations for the logged user:
<br/>

![Image Alt](https://github.com/ArielGolanski/HemiTube4/blob/main/wiki/proof/Screenshot%202024-09-18%20140756.png)

