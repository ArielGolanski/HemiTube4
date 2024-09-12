import React, { useEffect, useState } from 'react';
//import { useParams } from 'react-router-dom';
import VideoListResults from '../../components/videoListResults/VideoListResults';
import { Link } from 'react-router-dom';
//import './MyVideos.css';

export const serverAddress = 'http://localhost:5001';

function MyVideos() {
  //const { username } = useParams();
  const [topVids, setTopVids] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchTopVideos = async () => {
      try {
        const response = await fetch(`${serverAddress}/api/videos`);
        // if (!response.ok) {
        //   throw new Error('No videos found for this user');
        // }
        const data = await response.json();
        setTopVids(data);
      } catch (error) {
        setError(error.message);
      }
    };

    fetchTopVideos();
  }, []);

  if (error) {
    return <div>Error: {error}</div>;
  }

  return (
    <div className="my-videos-container">
        <h1>Top Videos</h1>
      <VideoListResults filteredVideoList={topVids} />
      <div>
        <Link to="/">
              <button type="button" className="btn btn-outline-danger list-group-item d-flex align-items-center">
                Back to Home Page
              </button>
            </Link>
    </div>
    </div>
    
  );
}

export default MyVideos;
