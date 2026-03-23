import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { FiLogOut, FiHome, FiBook, FiTarget } from 'react-icons/fi';
import authService from '../services/authService';

/**
 * Navbar component
 */
const Navbar = () => {
  const navigate = useNavigate();
  const user = authService.getCurrentUser();

  const handleLogout = () => {
    authService.logout();
    navigate('/login');
  };

  return (
    <nav className="bg-gradient-to-r from-blue-600 to-blue-700 text-white shadow-lg">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between items-center h-16">
          {/* Logo */}
          <Link to="/dashboard" className="flex items-center gap-2 font-bold text-2xl">
            <FiBook className="text-2xl" />
            <span>Revio</span>
          </Link>

          {/* Center Navigation */}
          <div className="flex gap-6">
            <Link
              to="/dashboard"
              className="flex items-center gap-2 hover:bg-blue-800 px-3 py-2 rounded-lg transition"
            >
              <FiHome />
              Dashboard
            </Link>
            <Link
              to="/sections"
              className="flex items-center gap-2 hover:bg-blue-800 px-3 py-2 rounded-lg transition"
            >
              <FiBook />
              Sections
            </Link>
            <Link
              to="/revisions"
              className="flex items-center gap-2 hover:bg-blue-800 px-3 py-2 rounded-lg transition"
            >
              <FiTarget />
              Today's Revisions
            </Link>
          </div>

          {/* User Info & Logout */}
          <div className="flex items-center gap-4">
            <div className="hidden md:block">
              <p className="text-sm font-medium">{user?.name}</p>
              <p className="text-xs text-blue-100">{user?.email}</p>
            </div>
            <button
              onClick={handleLogout}
              className="flex items-center gap-2 hover:bg-blue-800 px-3 py-2 rounded-lg transition"
              title="Logout"
            >
              <FiLogOut />
              <span className="hidden sm:inline">Logout</span>
            </button>
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
