import React, { useState, useRef } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import './signup.css';
import usersTable from '../../components/Users.json';
import logo from '../../components/hemitubeLogoForC.jpeg';
import { registerUser } from '../../DataAccess/users';

function SignUp(userslist, setUsersList) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [verifyPassword, setVerifyPassword] = useState('');
  const [nickname, setNickname] = useState('');
  const [profilePhoto, setProfilePhoto] = useState(usersTable.s.pic_data);
  const [isPasswordValid, setIsPasswordValid] = useState(false);
  const [doPasswordsMatch, setDoPasswordsMatch] = useState(true);
  const [showPassword, setShowPassword] = useState(false);
  const [showVerificationPassword, setShowVerificationPassword] = useState(false);

  const validatePassword = (password) => {
    const minLength = /.{8,}/;
    const hasNumber = /[0-9]/;
    const hasLowercase = /[a-z]/;
    const hasUppercase = /[A-Z]/;

    return minLength.test(password) && hasNumber.test(password) && hasLowercase.test(password) && hasUppercase.test(password);
  };

  const handlePasswordChange = (event) => {
    const newPassword = event.target.value;
    setPassword(newPassword);
    const isValid = validatePassword(newPassword);
    setIsPasswordValid(isValid);
  };

  const handleVerifyPasswordChange = (event) => {
    const newVerifyPassword = event.target.value;
    setVerifyPassword(newVerifyPassword);
    setDoPasswordsMatch(newVerifyPassword === password);
  };

  const PasswordVisibility = () => {
    setShowPassword(!showPassword);
  };

  const PasswordVerificationVisibility = () => {
    setShowVerificationPassword(!showVerificationPassword);
  };

  const ThumbnailInputRef = useRef(null); // Ref for file input
  const handleThumbnailUpload = (event) => {
    const selectedThumbnailFile = event.target.files[0];
    if (selectedThumbnailFile) {
      const reader = new FileReader();
      reader.onload = (e) => {
        setProfilePhoto(e.target.result);
      };
      reader.readAsDataURL(selectedThumbnailFile);
    }
  };

  const navigate = useNavigate();

  const handleSignUp = async () => {
    if (!isPasswordValid || !doPasswordsMatch || username.length < 1 || nickname.length < 1) {
      console.log("oh no");
      alert("you must fill all fields properly");
      return false;
    }

    try {
      const result = await registerUser(username, password, nickname, profilePhoto);
      
      if (result === 'success') {
        return true;
      } else {
        alert("invalid username");
        return false;
      }
    } catch (error) {
      return false;
    }
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    const success = await handleSignUp();
    if (success) {
      navigate('/SignIn');
    } else {
      console.log('Sign-up failed. Staying on current page.');
    }
  };

  return (
    <section className="text-center text-lg-start">
      <div className="container py-4 align-items-center">
        <div className="card cascading-right bg-body-tertiary shadow-5" style={{ backdropFilter: 'blur(30px)' }}>
          <div className="card-body p-5 shadow-5 text-center">
            <div className="logo-container mb-4">
              <img src={logo} alt="HemiTube Logo" className="logo" />
            </div>
            <div>
              <h1>Welcome to HemiTube</h1>
              <div className="input-group mb-3">
                <input
                  type="text"
                  className={`form-control ${username ? 'valid' : 'invalid'}`}
                  placeholder="Choose Username"
                  aria-label="Username"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                />
              </div>
              <div className={`input-group mb-3 ${isPasswordValid ? 'valid' : 'invalid'}`}>
                <input
                  type={showPassword ? 'text' : 'password'}
                  className="form-control"
                  placeholder="Choose Password"
                  aria-label="Password"
                  value={password}
                  onChange={handlePasswordChange}
                />
                <button
                  type="button"
                  className="btn btn eye-icon"
                  onClick={PasswordVisibility}
                >
                  {showPassword ? <i className="bi bi-eye-slash"></i> : <i className="bi bi-eye"></i>}
                </button>
                {(!isPasswordValid) && (
                  <div className="invalid-password-message">The password must contain at least 8 characters including a number,
                    a lowercase letter and an uppercase letter.</div>
                )}
              </div>
              <div className={`input-group mb-3 ${doPasswordsMatch ? '' : 'invalid'}`}>
                <input
                  type={showVerificationPassword ? 'text' : 'password'}
                  className="form-control"
                  placeholder="Verify Password"
                  aria-label="Verify Password"
                  value={verifyPassword}
                  onChange={handleVerifyPasswordChange}
                />
                <button
                  type="button"
                  className="btn btn eye-icon"
                  onClick={PasswordVerificationVisibility}
                >
                  {showVerificationPassword ? <i className="bi bi-eye-slash"></i> : <i className="bi bi-eye"></i>}
                </button>
                {password !== verifyPassword && verifyPassword.length >= 0 && (
                  <div className="validation-message">Passwords do not match.</div>
                )}
              </div>
              <div>
                <input
                  type="text"
                  className="form-control"
                  placeholder="Choose Nickname"
                  aria-label="Nickname"
                  value={nickname}
                  onChange={(e) => setNickname(e.target.value)}
                />
              </div>
              <div>
                {"profile pic:"}
                <input
                  type="file"
                  onChange={handleThumbnailUpload}
                  accept="image/*"
                  ref={ThumbnailInputRef}
                />
              </div>
              <button
                type="button"
                className="btn btn-outline-danger"
                onClick={handleSubmit}
              >
                Sign Up
              </button>
              <Link to="/">
                <button type="button" className="btn btn-outline-danger list-group-item d-flex align-items-center">
                  Back to Home Page
                </button>
              </Link>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}

export default SignUp;
