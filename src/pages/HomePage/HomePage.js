import React from 'react';
import LeftMenu from '../../components/leftMenu/LeftMenu';
import Search from '../../components/search/Search';
import VideoListResults from '../../components/videoListResults/VideoListResults';

import logo from '../../components/hemitubeLogoForC.jpeg';
import './Homepage.css';

function HomePage({ doSearch, videoList, setCurrentUser, currentUser, filteredVideoList }) {
  if (!filteredVideoList) {
    return <div>No videos</div>;
  }
  console.log("Current User: " + localStorage.getItem("username"));

  return (
    <div className="container-fluid">
      <div className="row">
        {/* Left Menu */}
        <div className="col-12 col-md-2 sidebar2">
          <LeftMenu />
        </div>

        {/* Search and Video List Results */}
        <div className="col-12 col-md-10 main-content2">
          <div className="search-bar2 d-flex align-items-center mb-3 justify-content-center">
            <div className="logo-container3 me-3">
              <img src={logo} alt="Logo" className="logo3" width="50px" />
            </div>
            <div className="search-container">
              <Search doSearch={doSearch} className="form-control-lg" />
            </div>
          </div>
          <VideoListResults filteredVideoList={filteredVideoList} />
        </div>
      </div>
    </div>
  );
}

export default HomePage;
