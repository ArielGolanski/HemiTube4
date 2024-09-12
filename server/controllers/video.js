
const Video = require('../models/video.js');

const videoService = require('../services/video');
const tcpService = require('../services/tcpServices.js');
const VidFile = require('../models/vidFile.js');
const jwt = require('jsonwebtoken'); 

const key = "secret key foo foo foo bar";
const createVideo = async (req, res) => {
    try {

        var title = req.body.title; 
        var description = req.body.description;
        var publisher = req.body.publisher


        const newVideo = await videoService.createVideo(
            title,
            description,
            publisher, 
            req.files.file[0].filename,
            req.files.file[0].path,
            req.files.thumbnail[0].filename,
            req.files.thumbnail[0].path
        );


        res.status(201).json(newVideo);
    } catch (error) {
        console.log("Detailed Error: ", error);
        res.status(500).json({ errors: [error.message] });
    }
};


const addDefaultVideo = async (req, res) => {
    try {

        var title = req.body.title; 
        var description = req.body.description;
        var publisher = req.body.publisher
        var file_name = req.body.file_name;
        var file_data = req.body.file_name;
        var thumbnail_name = req.body.thumbnail_name;
        var thumbnail_data = req.body.thumbnail_data;


        const newVideo = await videoService.createVideo(
            title,
            description,
            publisher, 
            file_name,
            file_data,
            thumbnail_name,
            thumbnail_data
        );

        res.status(201).json(newVideo);
    } catch (error) {
        console.log("Detailed Error: ", error);
        res.status(500).json({ errors: [error.message] });
    }
};








const getVideos = async (req, res) => {
    try {

        console.log("Create Video function - request log :")
        const videos = await videoService.getVideos();
        res.status(200).json(videos);
    } catch (error) {
        res.status(500).json({ errors: [error.message] });
    }
};

const getVideo = async (req, res) => {
    try {
        const video = await videoService.getVideoById(req.params.pid);
        if (!video) {
            return res.status(404).json({ errors: ['Video not found'] });
        }
        res.status(200).json(video);
    } catch (error) {
        res.status(500).json({ errors: [error.message] });
    }
};

const updateVideo = async (req, res) => {
    try {
        const updatedVideo = await videoService.updateVideo(req.params.pid, req.body);
        if (!updatedVideo) {
            return res.status(404).json({ errors: ['Video not found'] });
        }
        res.status(200).json(updatedVideo);
    } catch (error) {
        res.status(500).json({ errors: [error.message] });
    }
};

const deleteVideo = async (req, res) => {
    try {
        const deletedVideo = await videoService.deleteVideo(req.params.pid);
        if (!deletedVideo) {
            return res.status(404).json({ errors: ['Video not found'] });
        }
        res.status(200).json({ message: 'Video and associated file deleted successfully' });
    } catch (error) {
        res.status(500).json({ errors: [error.message] });
    }
};

const addCommentToVideo = async (req, res) => {
    try {
        const {content, userID, username} = req.body;
        const {vid } = req.params;
        if (!vid || !content) {
            return res.status(400).json({ errors: ['Video ID and content are required'] });
        }
        const newComment = await videoService.addCommentToVideo(vid, content, userID, username);
        res.status(201).json(newComment);
    } catch (error) {
        res.status(500).json({ errors: [error.message] });
    }
};

const isLoggedIn = (req, res, next) => {
    if (req.headers.authorization) {
        const token = req.headers.authorization.split(" ")[1];
        try {
            const decoded = jwt.verify(token, key);
            req.user = decoded; // Attach decoded token data to req.user
            return next();
        } catch (err) {
            return res.status(401).send("Invalid Token");
        }
    } else {
        return res.status(403).send('Token required');
    }
};

const getVideosByUsername = async (req, res) => {
    try {
        const username = req.params.id;
        const videos = await videoService.getVideosByUsername(username);
        res.status(200).json(videos);
    } catch (error) {
        res.status(500).json({ errors: [error.message] });
    }
};

const deleteVideosByUsername = async (req, res) => {
    try {
        const deletedVideo = await videoService.deleteVideosByUsername(req.params.id);
        if (!deletedVideo) {
            return res.status(404).json({ errors: ['Video not found'] });
        }
        res.status(200).json({ message: 'Video and associated file deleted successfully' });
    } catch (error) {
        res.status(500).json({ errors: [error.message] });
    }
};

const getAllCommentsByVideoId = async (req, res) => {
    try {
        const comments = await videoService.getAllCommentsByVideoId(req.params.vid);
        res.status(200).json(comments);
    } catch (error) {
        res.status(500).json({ errors: [error.message] });
    }
};

const getTopVids = async (req, res) => {
    try {
        const topVideos = await videoService.getTopVids();
        res.status(200).json(topVideos);
    } catch (error) {
        console.error('Error getting top videos:', error);
        res.status(500).json({ message: 'Failed to get top videos' });
    }
};


///////////////////////

const getCommentFromVideo = async (req, res) => {
    try {
        const { vid, cid } = req.params;
        if (!vid || !cid) {
            return res.status(400).json({ errors: ['Video ID and comment ID are required'] });
        }
        const comment = await videoService.getCommentFromVideo(vid, cid);
        res.status(200).json(comment);
    } catch (error) {
        res.status(500).json({ errors: [error.message] });
    }
};

const updateComment = async (req, res) => {
    try {
        const { vid, cid } = req.params;
        const { content } = req.body;
        if (!vid || !cid || !content) {
            return res.status(400).json({ errors: ['Video ID, comment ID, and content are required'] });
        }
        const updatedComment = await videoService.updateComment(vid, cid, content);
        res.status(200).json(updatedComment);
    } catch (error) {
        res.status(500).json({ errors: [error.message] });
    }
};

const deleteComment = async (req, res) => {
    try {
        const { vid, cid } = req.params;
        if (!vid || !cid) {
            return res.status(400).json({ errors: ['Video ID and comment ID are required'] });
        }
        const result = await videoService.deleteComment(vid, cid);
        res.status(200).json(result);
    } catch (error) {
        res.status(500).json({ errors: [error.message] });
    }
};

///////////////////new for the tcp server////////////////
const getRecommendedVideos = async (req, res) => {
    try {
        const { id: userId, pid: videoId } = req.params;  // Ensure the parameters are properly named

        console.log('userId:', userId, 'videoId:', videoId);  // Add this log to check if they are undefined

        if (!userId || !videoId) {
            return res.status(400).json({ errors: ['Invalid userId or videoId'] });
        }

        // Fetch recommended video IDs from the TCP server
        const recommendedVideoIds = await tcpService.getRecommendationsFromTCP(userId, videoId);

        // Check for recommendations and handle accordingly
        if (recommendedVideoIds.length === 0) {
            console.log('No recommendations available, fetching top 20 videos.');
            const topVideos = await videoService.getTopVids();
            return res.status(200).json(topVideos);
        }

        // Get full video details from MongoDB
        const recommendedVideos = await videoService.getVideosByIds(recommendedVideoIds);
        return res.status(200).json(recommendedVideos);
    } catch (error) {
        console.error('Error fetching recommended videos:', error);
        res.status(500).json({ errors: [error.message] });
    }
};










module.exports = { createVideo, getVideos, getVideo, updateVideo, deleteVideo,  getTopVids,
    addCommentToVideo, isLoggedIn , getVideosByUsername, getAllCommentsByVideoId, deleteVideosByUsername, 
    deleteComment, updateComment, getCommentFromVideo, addDefaultVideo, getRecommendedVideos};