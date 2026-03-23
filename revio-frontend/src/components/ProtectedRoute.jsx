import React from 'react';
import { Navigate } from 'react-router-dom';
import authService from '../services/authService';

/**
 * ProtectedRoute component that requires authentication
 * Redirects to login if user is not authenticated
 */
const ProtectedRoute = ({ component: Component, children }) => {
  const isAuthenticated = authService.isAuthenticated();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  if (Component) {
    return <Component />;
  }

  return children;
};

export default ProtectedRoute;
