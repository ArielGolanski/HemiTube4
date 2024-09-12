import React from 'react';
import './darkmode.css'
function DarkModeToggle({ isDarkMode, toggleDarkMode }) {
  return (
    <button onClick={toggleDarkMode} className="dark-mode-toggle">
      {isDarkMode ? "Light Mode" : "Dark Mode"}
    </button>
  );
}

export default DarkModeToggle;