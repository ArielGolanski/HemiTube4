const Video = require('../models/video');
const VidFile = require('../models/vidFile.js');
const Comment = require('../models/comment');
const User = require('../models/user');

const createVideo = async (title, description, publisher, file_name, file_data, thumbnail_name, thumbnail_data) => {
    try {
        const comments = [
            
        ];
        const video = new Video({ title, description, publisher, comments, file_name, file_data, thumbnail_name, thumbnail_data });
        await video.save();
        return video.toObject();
    } catch (error) {
        console.log("Error creating video: ", error);
        throw new Error('Could not create video');
    }
};

const getVideos = async () => {
    try {
        return await Video.find({}).populate('publisher');
    } catch (error) {
        console.log("Error fetching videos: ", error);
        throw new Error('Could not fetch videos');
    }
};

const getVideoById = async (id) => {
    try {
        return await Video.findById(id)
        //.populate('publisher comments file'); // Populate comments and file
    } catch (error) {
        console.log("Error fetching video by ID: ", error);
        throw new Error('Could not fetch video');
    }
};

const updateVideo = async (id, updateData) => {
    try {
        return await Video.findByIdAndUpdate(id, updateData, { new: true })
            .populate('publisher comments file'); // Populate comments and file
    } catch (error) {
        console.log("Error updating video: ", error);
        throw new Error('Could not update video');
    }
};

const deleteVideo = async (id) => {
    try {
        const video = await Video.findById(id);
        if (video) {
            return await Video.findByIdAndDelete(id);
        }

    } catch (error) {
        console.log("Error deleting video: ", error);
        throw new Error('Could not delete video');
    }
};



const addCommentToVideo = async (videoId, commentContent, userId, username) => {
    try {

        // Find user by username
        const user = await User.findOne({ username });
        if (!user) {
            throw new Error('User not found');
        }

        // Create a new comment
        const comment = new Comment({ userID: userId, text: commentContent });


        // Find the video by its ID
        const video = await Video.findById(videoId);
        if (!video) {
            throw new Error('Video not found');
        }

        // Add the comment to the video's comments array
        video.comments.push(comment);
        await video.save();

        const populatedComment = await comment.populate('userID');

        return { _id: populatedComment._id, content: populatedComment.text, author: populatedComment.userID };
    } catch (error) {
        console.error("Error adding comment to video: ", error);
        throw new Error('Could not add comment to video');
    }
};

const getVideosByUsername = async (username) => {
    try {
        // Find user by username
        const user = await User.findOne({ username });
        if (!user) {
            throw new Error('User not found');
        }
        const userId = user._id;

        // Find videos by userId
        const videos = await Video.find({ publisher: userId });

        return videos.map(video => video.toObject());
    } catch (error) {
        console.error("Error getting videos by username: ", error);
        throw new Error('Could not get videos by username');
    }
};

const getAllCommentsByVideoId = async (videoId) => {
    try {
        const video = await Video.findById(videoId).populate({ path: 'comments', populate: { path: 'userID' } });
        if (!video) {
            throw new Error('Video not found');
        }

        return video.comments.map(comment => ({
            _id: comment._id,
            content: comment.text,
            author: comment.userID
        }));
    } catch (error) {
        console.error('Error getting comments by video ID:', error);
        throw new Error('Could not get comments');
    }
};

const deleteVideosByUsername = async (username) => {
    try {
        // Find user by username
        const user = await User.findOne({ username });
        if (!user) {
            throw new Error('User not found');
        }
        const userId = user._id;

        // Delete videos by userId
        await Video.deleteMany({ publisher: userId });

        return { message: `Videos by user ${username} deleted successfully` };
    } catch (error) {
        console.error("Error deleting videos by username: ", error);
        throw new Error('Could not delete videos by username');
    }
};

const getTopVids = async () => {
    try {
        const topVideos = await Video.find().sort({ views: -1 }).limit(20);
        return topVideos;
    } catch (error) {
        console.error('Error getting top videos:', error);
        throw new Error('Failed to get top videos');
    }
};


////////////////////////////////////////////////////
const getCommentFromVideo = async (videoId, commentId) => {
    try {
        // Find the video by its ID
        const video = await Video.findById(videoId).populate('comments.userID');
        if (!video) {
            throw new Error('Video not found');
        }

        // Find the comment by its ID
        const comment = video.comments.id(commentId);
        if (!comment) {
            throw new Error('Comment not found');
        }

        return { _id: comment._id, content: comment.text, author: comment.userID };
    } catch (error) {
        console.error("Error getting comment from video: ", error);
        throw new Error('Could not get comment from video');
    }
};

const updateComment = async (videoId, commentId, newContent) => {
    try {
        // Find the video by its ID
        const video = await Video.findById(videoId);
        if (!video) {
            throw new Error('Video not found');
        }

        // Find the comment by its ID
        const comment = video.comments.id(commentId);
        if (!comment) {
            throw new Error('Comment not found');
        }

        // Update the comment's content
        comment.text = newContent;
        await video.save();

        return { _id: comment._id, content: comment.text, author: comment.userID };
    } catch (error) {
        console.error("Error updating comment: ", error);
        throw new Error('Could not update comment');
    }
};

const deleteComment = async (videoId, commentId) => {
    try {
        // Find the video by its ID
        const video = await Video.findById(videoId);
        if (!video) {
            throw new Error('Video not found');
        }

        // Find the comment by its ID and remove it
        const comment = video.comments.id(commentId);
        if (!comment) {
            throw new Error('Comment not found');
        }

        video.comments.pull(commentId);
        await video.save();

        return { success: true };
    } catch (error) {
        console.error("Error deleting comment: ", error);
        throw new Error('Could not delete comment');
    }
};

const deleteAllCommentsByUsername = async (username) => {
    try {
        // Find user by username
        const user = await User.findOne({ username });
        if (!user) {
            throw new Error('User not found');
        }

        // Find all videos
        const videos = await Video.find();

        // Iterate through each video and remove comments by the user
        for (const video of videos) {
            video.comments = video.comments.filter(comment => comment.userID.toString() !== user._id.toString());
            await video.save();
        }

        return { success: true };
    } catch (error) {
        console.error("Error deleting comments by username from all videos: ", error);
        throw new Error('Could not delete comments by username from all videos');
    }
};




module.exports = {
    createVideo, getVideos, getVideoById, updateVideo, deleteVideo, getTopVids, deleteAllCommentsByUsername,
    addCommentToVideo, getVideosByUsername, getAllCommentsByVideoId, deleteVideosByUsername, deleteComment, updateComment, getCommentFromVideo
};