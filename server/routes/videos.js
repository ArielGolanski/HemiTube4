// server/routes/videos.js
const express = require('express');
const videoController = require('../controllers/video');
const userController = require('../controllers/user');
const uploadFields = require('../middleware/multer');

const router = express.Router();

router.route('/:id/videos')
    .get(videoController.getVideosByUsername) // Get all videos for a user by user ID (:id)
    .post(userController.isLoggedIn, uploadFields, videoController.createVideo); // Create a new video for a user by user ID (:id)

router.route('/:id/videos/:pid')
    .get(videoController.getVideo) // Get details of a video by video ID (:pid)
    .put(videoController.updateVideo) // Update a video by video ID (:pid)
    .delete(videoController.deleteVideo); // Delete a video by video ID (:pid)

router.put('/:id/videos/:pid/incrementViews', videoController.incrementViews); // Increment views by video ID

    //////// tcp stuff//////
router.route('/:id/videos/:pid/recommended')
.get(videoController.getRecommendedVideos); //gets the reccomended videos by video ID (:pid)
    


module.exports = router;
