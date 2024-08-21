const userController = require('../controllers/user');

const express = require('express');
const router = express.Router();

router.route('/')
    .post(userController.createUser)
    .get(userController.getAllUsers)

router.route('/:id')
    .get(userController.getUser)
    .put(userController.isLoggedIn, userController.updateUser)
    .delete(userController.isLoggedIn, userController.deleteUser);


module.exports = router;