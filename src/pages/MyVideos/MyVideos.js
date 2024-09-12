import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import VideoListResults from '../../components/videoListResults/VideoListResults';
import './MyVideos.css';

export const serverAddress = 'http://localhost:5001';

function MyVideos() {
  const { username } = useParams();
  const [userVideos, setUserVideos] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchUserVideos = async () => {
      try {
        const response = await fetch(`${serverAddress}/api/users/${username}/videos`);
        // if (!response.ok) {
        //   throw new Error('No videos found for this user');
        // }
        const data = await response.json();
        setUserVideos(data);
      } catch (error) {
        setError(error.message);
      }
    };

    fetchUserVideos();
  }, [username]);

  if (error) {
    return <div>Error: {error}</div>;
  }

  if (!userVideos.length) {
    return <div>No videos found for user: {username}</div>;
  }

  return (
    <div className="my-videos-container">
      <h1>{username}'s Videos</h1>
      <VideoListResults filteredVideoList={userVideos} />
      <Link to="/">
                <button type="button" className="btn btn-outline-danger list-group-item d-flex align-items-center">
                  Back to Home Page
                </button>
      </Link>
    </div>
  );
}

export default MyVideos;
