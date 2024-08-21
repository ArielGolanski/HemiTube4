
const express = require('express');
const videoController = require('../controllers/video');
const userController = require('../controllers/user');

const router = express.Router();

// Routes to manage videos under a specific user
router.route('/api/videosHemi')
    .get(videoController.getVideos) // Get all videos for a user by user ID (:id)
router.route('/api/videos')
    .get(videoController.getTopVids) // Get details of a video by video ID (:pid)
router.route('/api/addDefaultVideo')
    .post(videoController.addDefaultVideo) // add default video    


module.exports = router;