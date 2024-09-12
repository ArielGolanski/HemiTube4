import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';  // Import useParams to get the current video ID
import './VideoViewVidResults.css';

export const serverAddress = 'http://localhost:5001';

function VideoViewVidResuls({ currentUser }) {
  const [recommendedVideoList, setRecommendedVideoList] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  const { id } = useParams();  // Get the current video ID from the URL

  useEffect(() => {
    const fetchRecommendedVideos = async () => {
      try {
        const res = await fetch(`${serverAddress}/api/users/${localStorage.getItem('username')}/videos/${id}/recommended`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'authorization': 'Bearer ' + localStorage.getItem('JWT'),
          },
        });

        if (res.status === 200) {
          const data = await res.json();
          setRecommendedVideoList(data);  // Set the fetched data
        } else {
          throw new Error('Error fetching recommended videos');
        }
      } catch (error) {
        setError(error.message);  // Set error message
      } finally {
        setLoading(false);  // Stop the loading state
      }
    };

    fetchRecommendedVideos();
  }, [id]);  // Fetch videos every time the video ID changes

  // Handle loading and error states
  if (loading) {
    return <label>Loading recommended videos...</label>;
  }

  if (error) {
    return <label>{error}</label>;
  }

  if (recommendedVideoList.length === 0) {
    return <label>No recommended videos available!</label>;
  }

  // Generate video links for display
  const videoLinks = recommendedVideoList.map((video, index) => (
    <Link to={`/video/${video._id}`} key={index} style={{ textDecoration: 'none', color: 'inherit' }}>
      <div className="video-item-res">
        <img className="video-thumbnail" src={`${serverAddress}/uploads/${video.thumbnail_name}`} alt={video.title} />
        <div className="video-title">{video.title}</div>
        <div className="video-details">
          <p>{video.publisher.username}</p>
          <p>{video.views} views • {new Date(video.uploadDate).toLocaleDateString()}</p>
        </div>
      </div>
    </Link>
  ));

  return (
    <div className="video-list">
      {videoLinks}
    </div>
  );
}

export default VideoViewVidResuls;
