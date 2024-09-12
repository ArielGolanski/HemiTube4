const getRecommendations = require('../routes/RecommendationClient'); // Reuse the existing client function


const getRecommendationsFromTCP = (userId, videoId) => {
    return new Promise((resolve, reject) => {
        getRecommendations(userId, videoId, (err, data) => {
            if (err) {
                return reject(err);
            }

            const trimmedData = data.trim();
            
            // Check if the response is "No recommendations available"
            if (trimmedData === "No recommendations available") {
                resolve([]);  // Return an empty array if no recommendations are available
            } else {
                // Clean up the response and filter valid ObjectIds
                const recommendedVideoIds = trimmedData
                    .split(' ')
                    .filter(id => /^[a-f\d]{24}$/i.test(id));  // Only keep valid 24-character hex strings (valid MongoDB ObjectId)

                resolve(recommendedVideoIds);  // Return the valid video IDs
            }
        });
    });
};


module.exports = { getRecommendationsFromTCP };
