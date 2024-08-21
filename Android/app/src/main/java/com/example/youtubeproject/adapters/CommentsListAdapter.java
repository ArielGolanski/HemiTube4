package com.example.youtubeproject.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtubeproject.R;
import com.example.youtubeproject.entities.Comment;
import com.example.youtubeproject.entities.SessionManager;
import com.example.youtubeproject.entities.User;
import com.example.youtubeproject.viewmodels.VideoViewModel;
import com.example.youtubeproject.viewmodels.UserViewModel;

import java.util.List;

public class CommentsListAdapter extends RecyclerView.Adapter<CommentsListAdapter.CommentViewHolder> {

    private final LayoutInflater mInflater;
    private List<Comment> commentList;
    private List<User> users;
    private String videoId;
    private String videoPublisherId;
    private final LiveData<List<User>> usersLiveData;
    private final LifecycleOwner lifecycleOwner;
    private final VideoViewModel videoViewModel;

    public CommentsListAdapter(Context context, UserViewModel userViewModel, LifecycleOwner lifecycleOwner, String videoId, String videoPublisherId) {
        mInflater = LayoutInflater.from(context);
        this.usersLiveData = userViewModel.getAllUsers();
        this.lifecycleOwner = lifecycleOwner;
        this.videoViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(VideoViewModel.class);
        this.videoId = videoId;
        this.videoPublisherId = videoPublisherId;

        // Observe the specific LiveData list
        usersLiveData.observe(lifecycleOwner, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> userList) {
                users = userList;
                notifyDataSetChanged();
            }
        });
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.comment_layout, parent, false);
        return new CommentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        if (commentList != null) {
            Comment currentComment = commentList.get(position);
            holder.content.setText(currentComment.getText());
            holder.uploader.setText(getUsernameById(currentComment.getUserId()));

            holder.btnDelete.setOnClickListener(v -> {
                String publisherId = currentComment.getUserId();  // Assuming you have this field in Comment
                String authToken = "Bearer " + SessionManager.getInstance().getToken();
                videoViewModel.deleteComment(publisherId, videoId, currentComment.getId()).observe(lifecycleOwner, isDeleted -> {
                    if (isDeleted != null && isDeleted) {
                        removeComment(currentComment);
                        Toast.makeText(holder.itemView.getContext(), "Comment deleted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(holder.itemView.getContext(), "Failed to delete comment", Toast.LENGTH_SHORT).show();
                    }
                });
            });

            holder.btnEdit.setOnClickListener(v -> {
                showEditCommentDialog(holder, currentComment);
            });

            if (SessionManager.getInstance().isLogedIn()) {
                if (SessionManager.getInstance().getLoggedUser().getId().equals(currentComment.getUserId())) {
                    holder.btnDelete.setVisibility(View.VISIBLE);
                    holder.btnEdit.setVisibility(View.VISIBLE);
                } else {
                    holder.btnDelete.setVisibility(View.GONE);
                    holder.btnEdit.setVisibility(View.GONE);
                }
            } else {
                holder.btnDelete.setVisibility(View.GONE);
                holder.btnEdit.setVisibility(View.GONE);
            }
        }
    }


    private void showEditCommentDialog(CommentViewHolder holder, Comment currentComment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
        LayoutInflater inflater = LayoutInflater.from(holder.itemView.getContext());
        View dialogView = inflater.inflate(R.layout.dialog_edit_comment, null);
        builder.setView(dialogView);

        EditText editCommentInput = dialogView.findViewById(R.id.editCommentInput);
        editCommentInput.setText(currentComment.getText());

        builder.setPositiveButton("Update", (dialog, id) -> {
            String updatedText = editCommentInput.getText().toString().trim();
            if (!updatedText.isEmpty()) {
                String authToken = "Bearer " + SessionManager.getInstance().getToken();
                videoViewModel.updateComment(videoPublisherId, videoId, currentComment.getId(), updatedText).observe(lifecycleOwner, updatedComment -> {
                    if (updatedComment != null) {
                        currentComment.setText(updatedComment.getContent());
                        notifyItemChanged(holder.getAdapterPosition());
                        Toast.makeText(holder.itemView.getContext(), "Comment updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(holder.itemView.getContext(), "Failed to update comment", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(holder.itemView.getContext(), "Comment cannot be empty", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return commentList != null ? commentList.size() : 0;
    }

    public void setComments(List<Comment> comments) {
        this.commentList = comments;
        notifyDataSetChanged();
    }

    public void addComment(Comment comment) {
        if (commentList != null) {
            commentList.add(comment);
            notifyItemInserted(commentList.size() - 1);
        }
    }

    public void removeComment(Comment comment) {
        int position = commentList.indexOf(comment);
        if (position != -1) {
            commentList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, commentList.size());
        }
    }

    private String getUsernameById(String userId) {
        if (users != null) {
            for (User user : users) {
                if (user.getId().equals(userId)) {
                    return user.getUsername();
                }
            }
        }
        return null;
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        private final TextView uploader;
        private final TextView content;
        private final Button btnDelete;
        private final Button btnEdit;

        private CommentViewHolder(View itemView) {
            super(itemView);
            uploader = itemView.findViewById(R.id.commentUsername);
            content = itemView.findViewById(R.id.commentText);
            btnDelete = itemView.findViewById(R.id.deleteBtn);
            btnEdit = itemView.findViewById(R.id.editBtn);
        }
    }
}
