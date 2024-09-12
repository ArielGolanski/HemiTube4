import React, { useState ,useEffect } from 'react';
import { Link , useParams} from 'react-router-dom';
import VideoItem from '../videoItem/VideoItem';
import videoTable from '../videoItem/Videos.json';
import './VideoViewVidResults.css';

export const serverAddress = 'http://localhost:5001';

function VideoViewVidResuls() {
  const [recommendedVideos, setRecommendedVideos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  const { id, pid } = useParams();  // Extract userId (id) and videoId (pid) from URL parameters

  // Fetch recommended videos from the API when the component is mounted
  useEffect(() => {
    const fetchRecommendedVideos = async () => {
      try {
        const response = await fetch(`${serverAddress}/api/users/${id}/videos/${pid}/recommended`);
        if (!response.ok) {
          throw new Error('Failed to fetch recommended videos');
        }
        const data = await response.json();
        setRecommendedVideos(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchRecommendedVideos();
  }, [id, pid]);

  if (loading) {
    return <label>Loading recommended videos...</label>;
  }

  if (error) {
    return <label>{error}</label>;
  }

  if (recommendedVideos.length === 0) {
    return <label>No recommended videos available!</label>;
  }

  const videoLinks = recommendedVideos.map((video, index) => (
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
