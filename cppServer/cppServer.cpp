#include <iostream>
#include <thread>
#include <vector>
#include <unordered_map>
#include <mutex>
#include <cstring>
#include <netinet/in.h>
#include <unistd.h>

using namespace std;

const int server_port = 5000;
unordered_map<string, vector<string>> user_video_map;  // user_id is a string
mutex mtx;

void handle_client(int client_sock) {
    cout << "Handling new client connection on socket: " << client_sock << endl;

    char buffer[4096];
    int read_bytes = recv(client_sock, buffer, sizeof(buffer), 0);

    if (read_bytes > 0) {
        buffer[read_bytes] = '\0';  // Null-terminate the received string
        cout << "Received data from client: " << buffer << endl;

        string user_data(buffer);
        size_t delimiter_pos = user_data.find(":");

        if (delimiter_pos != string::npos) {
            string user_id = user_data.substr(0, delimiter_pos);
            string video_id = user_data.substr(delimiter_pos + 1);

            if (!user_id.empty() && !video_id.empty()) {
                cout << "Parsed user_id: " << user_id << ", video_id: " << video_id << endl;

                // Update user's video history
                user_video_map[user_id].push_back(video_id);
                cout << "Updated video history for user " << user_id << endl;

                // Recommendation logic: Based on common videos watched by other users
                string recommendations;
                for (const auto& entry : user_video_map) {
                    if (entry.first != user_id) {
                        for (const string& watched_video : entry.second) {
                            recommendations += " " + watched_video;
                        }
                    }
                }

                if (recommendations.empty()) {
                    recommendations = "No recommendations available";
                }

                cout << "Generated recommendations: " << recommendations << endl;

                // Send back recommendations to the client
                int sent_bytes = send(client_sock, recommendations.c_str(), recommendations.length(), 0);
                if (sent_bytes < 0) {
                    perror("Error sending to client");
                } else {
                    cout << "Sent recommendations to client." << endl;
                }
            } else {
                cerr << "Invalid user_id or video_id received" << endl;
            }
        } else {
            cerr << "Invalid message format received from client" << endl;
        }
    } else {
        cerr << "No data received from client or error reading from socket." << endl;
    }

    cout << "Closing client socket: " << client_sock << endl;
    close(client_sock);  // Close the client socket after handling
}


int main() {
    cout << "Starting server on port " << server_port << endl;

    int sock = socket(AF_INET, SOCK_STREAM, 0);
    if (sock < 0) {
        perror("Error creating socket");
        return 1;
    }

    struct sockaddr_in sin;
    memset(&sin, 0, sizeof(sin));
    sin.sin_family = AF_INET;
    sin.sin_addr.s_addr = INADDR_ANY;
    sin.sin_port = htons(server_port);

    if (bind(sock, (struct sockaddr *) &sin, sizeof(sin)) < 0) {
        perror("Error binding socket");
        return 1;
    }

    if (listen(sock, 5) < 0) {
        perror("Error listening on socket");
        return 1;
    }

    cout << "Server listening on port " << server_port << endl;

    while (true) {
        struct sockaddr_in client_sin;
        unsigned int addr_len = sizeof(client_sin);
        int client_sock = accept(sock, (struct sockaddr *) &client_sin, &addr_len);
        if (client_sock < 0) {
            perror("Error accepting client");
            continue;
        }

        cout << "Accepted new client connection: " << client_sock << endl;

        // Handle each client in a new thread
        thread client_thread(handle_client, client_sock);
        // Detach the thread to allow it to run independently
        client_thread.detach();
    }

    close(sock);
    return 0;
}
