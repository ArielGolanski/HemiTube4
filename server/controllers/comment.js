const commentService = require('../services/comment') 
const videoService = require('../services/video') 


const jwt = require('jsonwebtoken'); 

const key = "secret key foo foo foo bar";


const getComment = async (req, res) => {
    try {
        const comment = await commentService.getCommentById(req.params.cid);
        if (!comment) {
            return res.status(404).json({ errors: ['Comment not found'] });
        }
        res.json(comment);
    } catch (error) {
        res.status(500).json({ errors: [error.message] });
    }
};


const updateComment = async (req, res) => {
    try {
        const comment = await commentService.getCommentById(req.params.cid);
        if (!comment) {
            return res.status(404).json({ errors: ['Comment not found'] });
        }
        const updatedComment = await commentService.updateComment(req.params.cid, req.body.content);
        res.json(updatedComment);
    } catch (error) {
        res.status(500).json({ errors: [error.message] });
    }
};

const getComments = async (_, res) => {
    res.json(await commentService.getComments())
};
const deleteComment = async (req, res) => {
    try {
        const comment = await commentService.deleteComment(req.params.cid);
        if (!comment) {
            return res.status(404).json({ errors: ['Comment not found'] });
        }
        res.json({ message: 'Comment deleted successfully', comment });
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

const deleteCommentsByUsername = async (req, res) => {
    try {
        const result = await commentService.deleteCommentsByUsername(req.params.id);
        res.status(200).json(result);
    } catch (error) {
        res.status(500).json({ errors: [error.message] });
    }
};


module.exports = {getComment, updateComment, getComments, deleteComment, isLoggedIn, deleteCommentsByUsername}