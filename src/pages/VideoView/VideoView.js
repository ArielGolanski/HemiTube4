import React, { useEffect, useState } from 'react';
import { useNavigate, useParams, Link } from 'react-router-dom';
import Search from '../../components/search/Search';
import NotFound from '../NotFound/NotFound';
import VideoListResults from '../../components/VideoViewVidResults/VideoViewVidResults';
import CommentSection from '../../components/CommentSection/CommentSection';
import { deleteVideo, updateVideo } from '../../DataAccess/videos'
import './VideoView.css';
import logo from '../../components/hemitubeLogoForC.jpeg';
export const serverAddress = 'http://localhost:5001';

function VideoView({
  doSearch, videoList, filteredVideoList, updateComments, likedVideos, toggleLike
}) {
  const { id } = useParams();
  const [video, setVideo] = useState(null);
  const navigate = useNavigate();
  const [isEditing, setIsEditing] = useState(false);
  const [editedTitle, setEditedTitle] = useState('');
  const [editedDescription, setEditedDescription] = useState('');
  const isLiked = likedVideos.includes(id);

  useEffect(() => {
    const fetchVideo = () => {
      const foundVideo = videoList.find(v => v._id === id);
      setVideo(foundVideo);
      if (foundVideo) {
        setEditedTitle(foundVideo.title);
        setEditedDescription(foundVideo.description);
      }
    };
    fetchVideo();
  }, [id, videoList]);

  if (!video) {
    return <NotFound />;
  }

  const handleDeleteVideo = () => {
    if (video.publisher._id == localStorage.getItem("userId")) {
      deleteVideo(video._id, video.publisher._id);
      navigate('/');
    }
    else {
      alert("only publisher of the video can delete it!")
    }
  };

  const handleEditVideo = () => {
    if (video.publisher._id == localStorage.getItem("userId")) {
      setIsEditing(true);
    }
    else {
      alert("only the publisher of the video can edit the video details!")
    }
  };

  const handleSaveChanges = () => {
    const updateData = {
      title: editedTitle,
      description: editedDescription
    };

    updateVideo(video._id, video.publisher.username, updateData);
    setIsEditing(false);
    window.location.reload();
  };

  const handleToggleLike = () => {
    toggleLike(id);
  };

  return (
    <div className="video-view-container">
      <div className="video-list-container">
        <VideoListResults filteredVideoList={filteredVideoList} />
      </div>
      <div className="main-content">
        <div className="search-bar2 d-flex align-items-center mb-3 justify-content-center">
          <div className="logo-container3 me-3">
            <img src={logo} alt="Logo" className="logo3" width="50px" />
          </div>
          <div className="search-container">
            <Search doSearch={doSearch} />
          </div>
        </div>
        <div>
          <h2>{video.title}</h2>
          <video controls width="100%" src={`${serverAddress}/uploads/${video.file_name}`} />
        </div>
        <div className="video-details">
          <p>Description: {video.description}</p>
          <p>
            Publisher:
            <Link to={`/${video.publisher.username}`} className="w-100 m-1 ms-3">
              {video.publisher.username}
            </Link>
          </p>
          <p>Views: {video.__v}</p>
          <p>Uploade date: {new Date(video.uploadDate).toLocaleDateString()}</p>
        </div>


        <div className="video-actions">
          <div className="action-buttons d-flex flex-wrap">
            <div className="dropdown">
              <button className="btn btn-outline-secondary rounded-pill dropdown-toggle" type="button" id="dropdownShareButton" data-bs-toggle="dropdown" aria-expanded="false">
                Share
              </button>
              <ul className="dropdown-menu" aria-labelledby="dropdownShareButton">
                <li>
                  <a className="dropdown-item" href="#">
                    <i className="bi bi-envelope me-2"></i> Email
                  </a>
                </li>
                <li>
                  <a className="dropdown-item" href="#">
                    <i className="bi bi-whatsapp me-2"></i> WhatsApp
                  </a>
                </li>
                <li>
                  <a className="dropdown-item" href="#">
                    <i className="bi bi-images me-2"></i> Gallery
                  </a>
                </li>
              </ul>
            </div>
            <button className="btn btn-outline-secondary rounded-pill">Subscribe</button>
            <div>
              {localStorage.getItem("username") === video.publisher.username && (
                <>
                  <button className="btn btn-outline-secondary rounded-pill" onClick={handleDeleteVideo}>Delete Video</button>
                  <button className="btn btn-outline-secondary rounded-pill" onClick={handleEditVideo}>Edit Video</button>
                </>
              )}
            </div>
            <button className="btn btn-outline-secondary rounded-pill" onClick={handleToggleLike}>
              {isLiked ? 'Unlike' : 'Like'}
            </button>
          </div>
        </div>

        {isEditing && (
          <div className="edit-section">
            <input
              type="text"
              value={editedTitle}
              onChange={(e) => setEditedTitle(e.target.value)}
              placeholder="Edit title"
            />
            <textarea
              value={editedDescription}
              onChange={(e) => setEditedDescription(e.target.value)}
              placeholder="Edit description"
            />
            <button className="rounded-pill" onClick={handleSaveChanges}>Save Changes</button>
          </div>
        )}
        <div className="comment-section">
          <CommentSection
            videoList={videoList}
          />
        </div>
        <Link to="/">
          <button type="button" className="btn btn-outline-danger list-group-item d-flex align-items-center">
            Back to Home Page
          </button>
        </Link>
      </div>
    </div>
  );
}

export default VideoView;