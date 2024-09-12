const net = require('net');

function getRecommendations(userId, videoId, callback) {
  const client = new net.Socket();

  client.connect(5000, '127.0.0.1', () => {
    console.log('Connected to C++ server');
    // Send only userId:video formatted string
    const message = `${userId}:${videoId}`;
    console.log('Sending message:', message);
    client.write(message);
  });

  client.on('data', (data) => {
    console.log('Received recommendation:', data.toString());
    callback(null, data.toString());
    client.destroy(); // Close the connection after receiving data
  });

  client.on('error', (err) => {
    console.error('TCP error:', err);
    callback(err, null);
  });

  client.on('close', () => {
    console.log('Connection closed');
  });
}

module.exports = getRecommendations;
