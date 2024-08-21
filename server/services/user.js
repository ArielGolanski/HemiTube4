//const { deleteVideosByUserId } = require('../controllers/video');
const User = require('../models/user');
//const { deleteCommentsByUserId } = require('../services/comment');

const createUser = async (username, password, nickName, profilePic) => {
    try {
        const user = new User({
            username: username,
            password: password,
            nickName: nickName,
            profilePic: profilePic

        });
        await user.save();
        return 'success';
    } catch (error) {
        console.log("Error creating user: " + error);
        return null;
    }
};

// const getUserById = async (id) => {
//     try {
//         return await User.findById(id);
//     } catch (error) {
//         console.log("Error fetching user by ID: " + error);
//         throw new Error('Could not fetch user');
//     }
// };

const getUser = async (username) => {
    try {
        const user = await User.findOne(
            {username},
            {username: 1, _id: 1, password: 1, profilePic: 1, nickName: 1}
        ).lean();

        if (user) {
            const {_id, ...userData} = user;
            return {id: _id, ...userData};
        } else {
            return null;
        }
    } catch (error) {
        console.log("Error in finding user: " + error);
        return null;
    }
};

const updateUser = async (username, newPic, newNickName) => {
    try {
        if (newPic) {
            await User.findOneAndUpdate(
                {username: username}, // Filter condition to find the user
                {profilePic: newPic}, // Updated field and value
                {new: false}
            );
        }
        if (newNickName) {
            await User.findOneAndUpdate(
                {username: username}, // Filter condition to find the user
                {nickName: newNickName}, // Updated field and value
                {new: false}
            );
        }
    } catch (error) {
        console.log("Error updating values: " + error);
        return null;
    }
    return 'success';
}


const deleteUser = async (username) => {
    try {
        const user = await User.findOneAndDelete({ username: username });
        if (!user) {
            throw new Error('User not found');
        }
        return user;
    } catch (error) {
        console.log("Error deleting user: " + error);
        throw new Error('Could not delete user');
    }
};

const getAllUsers = async () => {
    try {
        return await User.find({});
    } catch (error) {
        console.log("Error fetching users: ", error);
        throw new Error('Could not fetch users');
    }
};



module.exports = {createUser, updateUser, deleteUser, getUser, getAllUsers}