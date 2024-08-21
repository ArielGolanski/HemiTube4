const mongoose = require('mongoose');

const Schema = mongoose.Schema;
const User = new Schema({
    _id: {
        type: Schema.Types.ObjectId,
        default: null
    },
    username: {
        type: String,
        required: true
    },
    password: {
        type: String,
        required: true
    },
    nickName: { 
        type: String,
        required: true
    },
    profilePic: {
        type: String,
        required: true
    },
});

module.exports = mongoose.model('User', User);