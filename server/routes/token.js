
const express = require('express');
const tokenController = require('../controllers/token');

const router = express.Router();

router.post('/', tokenController.login);

module.exports = router;