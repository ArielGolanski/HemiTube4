const multer = require('multer');
const path = require('path');

const storage = multer.diskStorage({
    destination: function (req, file, cb) {
        cb(null, path.join(__dirname, '..', 'uploads'));
    },
    filename: function (req, file, cb) {
        cb(null, Date.now() + '-' + file.originalname);
    }
});

const upload = multer({ storage: storage });

// Configure multer to handle both video and thumbnail uploads
const uploadFields = upload.fields([
    { name: 'file', maxCount: 1 }, // Video file
    { name: 'thumbnail', maxCount: 1 } // Thumbnail image
]);

module.exports = uploadFields;
