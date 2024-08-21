
const express = require('express');
const commentController = require('../controllers/comment');
const videoController = require('../controllers/video');
const userController = require('../controllers/user');

const router = express.Router();

router.route('/:vid/comments')
    .get(videoController.getAllCommentsByVideoId) // Get all comments for a video by video ID (:vid)
    //.post(commentController.isLoggedIn, videoController.addCommentToVideo); // Add a new comment to a video by video ID (:vid)
    .post(videoController.addCommentToVideo); // Add a new comment to a video by video ID (:vid)

router.route('/:vid/comments/:cid')
    .get(videoController.getCommentFromVideo) // Get details of a comment by comment ID (:cid)
    .put(videoController.updateComment) // Update a comment by comment ID (:cid)
    .delete(videoController.deleteComment); // Delete a comment by comment ID (:cid)

module.exports = router;