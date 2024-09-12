import React, { useRef, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { createVideo } from '../../DataAccess/videos';
import logo from '../../components/hemitubeLogoForC.jpeg';

function AddVideo({ currentUser, videoList, setVideoList }) {
    const titleTextBox = useRef(null);
    const descriptionBox = useRef(null);
    const navigate = useNavigate();
    const VideoInputRef = useRef(null);
    const ThumbnailInputRef = useRef(null);
    const [selectedVideo, setSelectedVideo] = useState(null);
    const [selectedThumbnail, setSelectedThumbnail] = useState(null);

    const handleVideoUpload = (event) => {
        const selectedFile = event.target.files[0];
        setSelectedVideo(selectedFile);
    };

    const handleThumbnailUpload = (event) => {
        const selectedFile = event.target.files[0];
        setSelectedThumbnail(selectedFile);
    };

    const handleVideoAdding = async (event) => {
        event.preventDefault();
        if (
            titleTextBox.current.value.length < 1 ||
            descriptionBox.current.value.length < 1 ||
            !selectedVideo ||
            !selectedThumbnail
        ) {
            alert('You must fill all fields');
            return false;
        }

        const formData = new FormData();
        formData.append('publisher', localStorage.getItem("userId"));
        formData.append('title', titleTextBox.current.value);
        formData.append('description', descriptionBox.current.value);
        formData.append('file', selectedVideo);
        formData.append('thumbnail', selectedThumbnail);

        try {
            const newVideo = await createVideo(formData);
            console.log('Video created successfully:', newVideo);
            setVideoList([...videoList, newVideo]);
            navigate('/');
            window.location.reload();
        } catch (error) {
            console.log('Adding the video failed:', error);
        }
    };

    return (
        <section className="text-center text-lg-start">
            <div className="container py-4 align-items-center">
                <div className="card cascading-right bg-body-tertiary shadow-5" style={{ backdropFilter: 'blur(30px)' }}>
                    <div className="card-body p-5 shadow-5 text-center">
                        <div className="logo-container mb-4">
                            <img src={logo} alt="HemiTube Logo" className="logo" />
                        </div>
                        <div>
                            <h4>Hello {localStorage.getItem("username") ? localStorage.getItem("username") : 'Guest'}, let's add a video</h4>
                            <div>
                                <input
                                    type="text"
                                    className="form-control"
                                    placeholder="Choose a title for your new video"
                                    aria-label="title"
                                    ref={titleTextBox}
                                />
                            </div>
                            <br />
                            <div>
                                <input
                                    type="text"
                                    className="form-control"
                                    placeholder="Choose a description for your new video"
                                    aria-label="description"
                                    ref={descriptionBox}
                                />
                            </div>
                            <div>
                                {'Video:'}
                                <input type="file" onChange={handleVideoUpload} accept="video/*" ref={VideoInputRef} />
                            </div>
                            <div>
                                {'Thumbnail:'}
                                <input type="file" onChange={handleThumbnailUpload} accept="image/*" ref={ThumbnailInputRef} />
                            </div>

                            <button type="button" className="btn btn-outline-danger" onClick={handleVideoAdding}>
                                Add the video
                            </button>
                            <Link to="/">
                                <button type="button" className="btn btn-outline-danger list-group-item d-flex align-items-center">
                                    Back to Home Page
                                </button>
                            </Link>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    );
}

export default AddVideo;