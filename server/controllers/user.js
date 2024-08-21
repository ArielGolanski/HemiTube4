const userService = require('../services/user');
const videoService = require('../services/video');

const jwt = require('jsonwebtoken'); 

const key = "secret key foo foo foo bar";

const createUser = async (req, res) => {
    const user = await userService.getUser(req.body.username);
    if (user) {
        return res.status(409).json({message: 'Username is already taken'});
    }

    const newUser = await userService.createUser(req.body.username, req.body.password,
        req.body.nickName, req.body.profilePic);
    if (!newUser) {
        return res.status(500).send("Error creating user.");
    }

    // Return 200 status code and the saved user object
    return res.status(200).json({
        username: req.body.username,
        nickName: req.body.nickName,
        profilePic: req.body.profilePic
    });

};

// Define a function that responds with a json response.
// Only logged-in users should be able to execute this function
const getUser = async (req, res) => {
    const user = await userService.getUser(req.params.id);
    res.status(200).json(user ? {id: user.id, username: user.username, nickName: user.nickName, profilePic: user.profilePic} : null);
}

const updateUser = async (req, res) => {
    if (!(await userService.updateUser(req.params.id, req.body.newPic, req.body.newNickName))) {
        res.status(500).send('Error updating user');
    }
    res.status(200).json({});
}

const deleteUser = async (req, res) => {
    try {
        const username = req.params.id;

        // Fetch the user to get their ID
        const user = await userService.getUser(username);
        if (!user) {
            return res.status(404).json({ errors: ['User not found'] });
        }
        const userId = user.id;

        // Delete videos and comments by user ID
        await videoService.deleteVideosByUsername(username);
        await videoService.deleteAllCommentsByUsername(username);
        // await videoService.deleteCommentsByUserId(userId); // Uncomment if needed

        // Delete the user by username
        const deletedUser = await userService.deleteUser(username);
        if (!deletedUser) {
            return res.status(404).json({ errors: ['User not found'] });
        }
        res.status(200).json({ message: 'User deleted successfully' });
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
const getAllUsers = async (req, res) => {
    try {

        console.log("getting all users:")
        const users = await userService.getAllUsers();
        res.status(200).json(users);
    } catch (error) {
        res.status(500).json({ errors: [error.message] });
    }
};

module.exports = {createUser, isLoggedIn, getUser, updateUser, deleteUser, getAllUsers};