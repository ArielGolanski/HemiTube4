const jwt = require('jsonwebtoken');
const User = require('../models/user');

const key = "secret key foo foo foo bar";


async function getUserByUsername(username) {
    return User.findOne({username: username});
}

function generateToken(username) {
    const data = {username};
    return jwt.sign(data, key);
}


module.exports = {getUserByUsername, generateToken};