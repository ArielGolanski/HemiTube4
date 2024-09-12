const getRecommendations = require('./routes/RecommendationClient');

// Simulate multiple users connecting to the C++ server
function simulateClients() {
    const users = [
      { userId: 1, videoId: 101 },
      { userId: 2, videoId: 102 },
      { userId: 3, videoId: 103 },
      { userId: 4, videoId: 104 }
    ];
  
    users.forEach((user, index) => {
      setTimeout(() => {
        getRecommendations(user.userId, user.videoId, (err, recommendations) => {
          if (err) {
            console.error(`Error getting recommendations for user ${user.userId}:`, err);
          } else {
            console.log(`User ${user.userId} received recommendations:`, recommendations);
          }
        });
      }, index * 100);  // Slight delay to simulate clients connecting almost simultaneously
    });
  }
  
  // Start the simulation
  simulateClients();