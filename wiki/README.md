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
