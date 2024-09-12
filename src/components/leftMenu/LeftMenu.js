import './LeftMenu.css';
import { Link } from 'react-router-dom';
import React from 'react';
import usersTable from '../Users.json';
import { useEffect, useState } from "react";
import { logOut, setUserJWT } from '../../DataAccess/users';

import logo from '../../components/hemitubeLogoForC.jpeg';

function LeftMenu({ }) {


  const [jwt, setJwt] = useState(localStorage.getItem('JWT'));
  const userId = localStorage.getItem('userId');
  const username = localStorage.getItem('username');

  useEffect(() => {
    setUserJWT(jwt);
  }, [jwt])



  return (
    <div className="left-menu">
      <ul className="list-group">
        <h1>HemiTube</h1>
        <li className="list-group-item d-flex align-items-center">
          <i className="bi bi-house-fill"></i>
          <span className="w-100 m-1 ms-3">Home</span>
          <span className="badge bg-primary rounded-pill">14</span>
        </li>
        <li className="list-group-item d-flex align-items-center">
          <i className="bi bi-search"></i>
          <span className="w-100 m-1 ms-3">Explore</span>
          <span className="badge bg-primary rounded-pill">2</span>
        </li>
        <li className="list-group-item d-flex align-items-center">
          <i className="bi bi-disc"></i>
          <span className="w-100 m-1 ms-3">Explore</span>
          <span className="badge bg-primary rounded-pill">1</span>
        </li>
        <li className="list-group-item d-flex align-items-center">
          <i className="bi bi-collection-play"></i>
          <span className="w-100 m-1 ms-3">Subscriptions</span>
          <span className="badge bg-primary rounded-pill">1</span>
        </li>
      </ul>
      <div>{(!jwt || jwt === 'undefined' || jwt === 'null') && (
        <Link to="/signin">
          <button type="button" className="btn btn-outline-danger list-group-item d-flex align-items-center">
            Sign In
          </button>
        </Link>
      )}</div>
      <div>{(!jwt || jwt === 'undefined' || jwt === 'null') && (
        <Link to="/signup">
          <button type="button" className="btn btn-outline-danger list-group-item d-flex align-items-center">
            Sign Up
          </button>
        </Link>
      )}</div>
      <div>{(jwt != 'null') && (
        <Link to="/addVideo">
          <button type="button" className="btn btn-outline-danger list-group-item d-flex align-items-center">
            Add Video
          </button>
        </Link>
      )}</div>
      <div>
        <Link to="/TopVids">
          <button type="button" className="btn btn-outline-danger list-group-item d-flex align-items-center">
            Top Videos
          </button>
        </Link>
      </div>
      <div>{(jwt !== 'null') && (
        <Link to="/profile">
          <button type="button" className="btn btn-outline-danger list-group-item d-flex align-items-center">
            Profile
          </button>
        </Link>
      )}</div>
      <div>{(jwt != 'null') && (
        <button
          type="button"
          className="btn btn-outline-danger list-group-item d-flex align-items-center"
          onClick={() => logOut()}>
          Log Out
        </button>
      )}</div>
      <div className="user-list mt-4 list-group-item d-flex align-items-center">
        <h4>Hello, {(!jwt || jwt === 'undefined' || jwt === 'null') ? "Guest" : localStorage.getItem("username")}</h4>
      </div>
      {(jwt !== 'null') && (
        <div className="user-pic">
          <img
            src={localStorage.getItem("profilePic")}
            alt="profile_pic"
            className="img-fluid rounded-circle"
          />
        </div>
      )}
    </div>
  );
}

export default LeftMenu;
