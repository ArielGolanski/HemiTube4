const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const VideoSchema = new Schema({
    publisher: {
        type: Schema.Types.ObjectId,
        ref: 'User',
        required: true
    },
    comments: [
        {
            userID: {
                type: Schema.Types.ObjectId,
                ref: 'User',
                required: true
            },
            text: {
                type: String,
                required: true
            }
        }
    ],
    title: {
        type: String,
        required: true
    },
    description: {
        type: String,
        required: true
    },
    views: {
        type: String,
        required: false
    },
    uploadDate: {
        type: Date,
        default: Date.now
    },
    thumbnail_name: {
        type: String,
        default: "Name Unknown"
    },
    thumbnail_data: {
        type: String,
        default: null
    },
    file_name: {
        type: String,
        default: "Name Unknown"
    },
    file_data: {
        type: String,
        default: null
    }
});

module.exports = mongoose.model('Video', VideoSchema);
