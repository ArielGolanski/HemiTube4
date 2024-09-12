import React from 'react';
import './VideoItem.css'

function VideoItem({ title, author, views, time, img}){
    return (
        <div className="card col-xl-2 col-lg-3 col-md-4 col-sm-6 border-0 p-1">
              <img src={img} className="card-img-top thumbnail" alt="..."></img>
              <div className="card-body">
                <h5 className="card-title">{title}</h5>
                <div className="card-author">{author}</div>
                <div className="card-text">{views} views. {time} ago</div>
              </div>
            </div>
    );
}

export default VideoItem