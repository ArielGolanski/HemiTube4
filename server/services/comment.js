//const Message = require('../models/message');
//const FilesAttach = require('../models/files');
const Comment = require('../models/comment.js');
const Video = require('../models/video')
const User = require('../models/user')
// const createComment = async (author, content) => {
//     try {
//         const comment = new Comment({
//             author: author || null, // Use `null` or an empty string if author is not provided
//             content: content
//         });
//         await comment.save();

//         const commentObject = comment.toObject();
//         commentObject.id = commentObject._id.toString();
//         delete commentObject._id;

//         return commentObject;
//     } catch (error) {
//         console.log('Error creating comment:', error);
//         throw new Error('Could not create comment');
//     }
// };

const getComments = async () => {
    return await Comment.find({});
};

const getCommentById = async (id) => {
    return await Comment.findById(id);
};

const updateComment = async (id, content) => {
    const comment = await getCommentById(id);
    if (!comment) return null;
    comment.content = content;
    await comment.save();
    return comment;
};

const deleteComment = async (id) => {
    const comment = await getCommentById(id);
    if (!comment) return null;
    await comment.deleteOne();
    return comment;
};

const deleteCommentsByUsername = async (username) => {
    try {
        // Find user by username
        const user = await User.findOne({ username });
        if (!user) {
            throw new Error('User not found');
        }
        const userId = user._id;

        // Delete comments by userId
        await Comment.deleteMany({ author: userId });

        return { message: `Comments by user ${username} deleted successfully` };
    } catch (error) {
        console.error('Error deleting comments by username:', error);
        throw new Error('Could not delete comments by username');
    }
};




module.exports = {getComments, getCommentById, updateComment, deleteComment, deleteCommentsByUsername}