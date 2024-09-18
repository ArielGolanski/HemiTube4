# HemiTube4
our self-made logo: as you can see, this is hemi, holding the YouTube sign
![HemitubeLogoHorizontal](https://github.com/ArielGolanski/HemiTube2/assets/170665000/815d8c61-4e42-4ad7-9559-4309ca1bc596)


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
In case tou are noe logged in, you will also get the top 20 videos.

# The reccomendation algorithm
The algorithm gets a username am dvideo id and returns the reccomended videos according to the video you watched (and according to the videos other peaople watched).
For example, if a watched the videos that their id's are 1-5, and the user Israel watched 5-10, so if a new user will watch video number 5, it will reaive from the reccomendation algorithm the videos 1-10 (those are the videos that people who watched video 5 watched as well).
