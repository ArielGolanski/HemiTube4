import React from 'react';
import pageNotFoundPic from './funnyerror.jpg'

function NotFound() {
  return (
    <div>
      <img src={pageNotFoundPic} alt="page not found" className="errorimage" />
    </div>
  );
}

export default NotFound;
