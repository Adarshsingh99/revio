import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import Navbar from './components/Navbar';
import ProtectedRoute from './components/ProtectedRoute';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import DashboardPage from './pages/DashboardPage';
import SectionPage from './pages/SectionPage';
import SubsectionPage from './pages/SubsectionPage';
import RevisionPage from './pages/RevisionPage';
import useAuth from './hooks/useAuth';

/**
 * Main App Component
 * 
 * Handles routing for the entire application:
 * - Public routes: Login, Register
 * - Protected routes: Dashboard, Sections, Revisions (require JWT token)
 * - Uses ProtectedRoute component to check authentication
 */
function App() {
  const { isAuthenticated, isLoading } = useAuth();

  if (isLoading) {
    return (
      <div className="flex items-center justify-center min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-indigo-600"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-50 via-blue-50 to-indigo-100">
      {isAuthenticated && <Navbar />}
      
      <Routes>
        {/* Public Routes */}
        <Route path="/login" element={isAuthenticated ? <Navigate to="/dashboard" /> : <LoginPage />} />
        <Route path="/register" element={isAuthenticated ? <Navigate to="/dashboard" /> : <RegisterPage />} />
        
        {/* Protected Routes */}
        <Route 
          path="/dashboard" 
          element={<ProtectedRoute component={DashboardPage} />} 
        />
        <Route path="/sections" element={<Navigate to="/dashboard" replace />} />
        <Route 
          path="/sections/:sectionId" 
          element={<ProtectedRoute component={SectionPage} />} 
        />
        <Route 
          path="/sections/:sectionId/subsections/:subsectionId" 
          element={<ProtectedRoute component={SubsectionPage} />} 
        />
        <Route 
          path="/revisions" 
          element={<ProtectedRoute component={RevisionPage} />} 
        />
        
        {/* Default redirect */}
        <Route path="/" element={<Navigate to={isAuthenticated ? "/dashboard" : "/login"} />} />
        <Route path="*" element={<Navigate to={isAuthenticated ? "/dashboard" : "/login"} />} />
      </Routes>

      {/* Global Toast Notifications */}
      <Toaster position="top-right" />
    </div>
  );
}

export default App;
