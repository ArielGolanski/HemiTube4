const http = require('http');

// Define the userId and videoId for testing
const userId = "66e15ab580b88a51e067fefa";
const videoId = "66e15ab580b88a51e067ff02";

// Options for the HTTP GET request
const options = {
  hostname: 'localhost',
  port: 5001,  // Use port 5001 where your Node.js HTTP server is running
  path: `/${userId}/videos/${videoId}/recommended`,
  method: 'GET',
};

// Function to test the recommendations route
const testRecommendationsRoute = () => {
  const req = http.request(options, (res) => {
    let data = '';

    // Collect the response data
    res.on('data', (chunk) => {
      data += chunk;
    });

    // Once the response has ended, log the data
    res.on('end', () => {
      try {
        console.log('Recommended videos:', JSON.parse(data));  // Log the response
      } catch (error) {
        console.error('Error parsing JSON:', error);
      }
    });
  });

  // Handle errors
  req.on('error', (error) => {
    console.error('Error testing route:', error);
  });

  // End the request
  req.end();
};

// Run the test
testRecommendationsRoute();
