import React, { useEffect, useState } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './App.css';
import HomePage from './pages/HomePage/HomePage';
import SignUp from './pages/SignUp/SignUp';
import SignIn from './pages/SignIn/SignIn';
import NotFound from './pages/NotFound/NotFound';
import TopVids from './pages/TopVids/TopVids';
//import videos from './components/videoItem/videos';
import videoTable from './components/videoItem/Videos.json'
import usersTable from './components/Users.json'
import VideoView from './pages/VideoView/VideoView';
import AddVideo from './pages/AddVideo/Addvideo';
import DarkModeToggle from './components/darkmode/Darkmode'
import ProfilePage from './pages/ProfilePage/ProfilePage'; // Import ProfilePage
import MyVideos from './pages/MyVideos/MyVideos';
import { fetchVideos } from './DataAccess/videos';
import { addDeafault, addDeafaultUser, fetchUsers, registerUser } from './DataAccess/users';

export const serverAddress = 'http://localhost:5001';


function App() {


  const initialVideoList = Object.values(videoTable);
  const initialUsers = Object.values(usersTable);
  const [userslist, setUsersList] = useState([]);
  const [videoList, setVideoList] = useState([]);
  const [filteredVideoList, setFilteredVideoList] = useState([]);
  const [currentUser, setCurrentUser] = useState("");
  //const [usersVideos, setUsersVideos] = useState([]); 
  const [likedVideos, setLikedVideos] = useState([]);



  useEffect(() => {
    //setFilteredVideoList(videoList);
    addDeafaultUser();

    const fetchVideos = async () => {
      try {
        const response = await fetch(`${serverAddress}/api/videosHemi`, {
          method: 'get',
          headers: {
            'Content-Type': 'application/json',
            'authorization': 'Bearer ' + localStorage.getItem('JWT'),
          }
        });

        if (!response.ok) {
          throw new Error("Coudn't fetch any videos");
        }

        const data = await response.json();
        setFilteredVideoList(data)
        setVideoList(data)

      }

      catch (error) {
        return "Ooopss! We've run into a problem :(\nPlease try again later";
      }
    }

    fetchVideos();
  }, []);



  const doSearch = (q) => {
    if (!q) {
      setFilteredVideoList(videoList)
    }
    const filteredVideos = videoList.filter((video) => video.title.includes(q));
    setFilteredVideoList(filteredVideos);
  };

  ///////comment section
  // const updateComments = (videoId, comments) => {
  //   const updatedVideoList = videoList.map(video =>
  //     video.id === videoId ? { ...video, commentsArr: comments } : video
  //   );
  //   setVideoList(updatedVideoList);
  // };
  ////////////delete video 
  const deleteVideo = (videoId) => {
    const updatedVideoList = videoList.filter(video => video.id !== videoId);
    setVideoList(updatedVideoList);
  };
  //////////edit video
  const updateVideoDetails = (videoId, title, description) => {
    const updatedVideoList = videoList.map(video =>
      video.id === videoId ? { ...video, title, description } : video
    );
    setVideoList(updatedVideoList);
  };

  //////////add like
  const toggleLike = (videoId) => {
    setLikedVideos(prevLikedVideos =>
      prevLikedVideos.includes(videoId)
        ? prevLikedVideos.filter(id => id !== videoId)
        : [...prevLikedVideos, videoId]
    );
  };
  //dark mode modifications
  const [isDarkMode, setIsDarkMode] = useState(false);

  useEffect(() => {
    document.body.classList.toggle('dark-mode', isDarkMode);
  }, [isDarkMode]);

  const toggleDarkMode = () => {
    setIsDarkMode((prevMode) => !prevMode);
  };

  return (
    <div className="container-fluid">
      <Router>
        <DarkModeToggle isDarkMode={isDarkMode} toggleDarkMode={toggleDarkMode} />
        <Routes>
          <Route path="/" element={<HomePage doSearch={doSearch} filteredVideoList={filteredVideoList} currentUser={currentUser}
            setCurrentUser={setCurrentUser} videoList={videoList} />} />
          <Route path="/video/:id" element={<VideoView doSearch={doSearch} filteredVideoList={filteredVideoList}
            videoList={videoList} deleteVideo={deleteVideo} updateVideoDetails={updateVideoDetails}
            toggleLike={toggleLike} likedVideos={likedVideos} />} />
          <Route path="/signup" element={<SignUp />} />
          <Route path="/signin" element={<SignIn />} />
          <Route path="/addVideo" element={<AddVideo
            videoList={videoList} setVideoList={setVideoList} currentUser={currentUser} />} />
          <Route
            path="/profile"
            element={<ProfilePage currentUser={currentUser} setCurrentUser={setCurrentUser} />}
          />
          <Route path="/:username" element={<MyVideos />} />
          <Route path="/TopVids" element={<TopVids />} />
          <Route path="*" element={<NotFound />} />
        </Routes>

      </Router>
    </div>
  );
}

export default App;
