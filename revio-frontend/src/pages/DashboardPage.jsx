import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  FiBookOpen,
  FiCheckCircle,
  FiClock,
  FiTrendingUp,
  FiCalendar,
  FiAlertCircle,
} from 'react-icons/fi';
import toast from 'react-hot-toast';
import dashboardService from '../services/dashboardService';
import sectionService from '../services/sectionService';
import topicService from '../services/topicService';
import ProgressBar from '../components/ProgressBar';
import Spinner from '../components/Spinner';
import ErrorMessage from '../components/ErrorMessage';

const getErrorMessage = (error, fallback) => (
  error?.response?.data?.message ||
  error?.message ||
  fallback
);

/**
 * Dashboard Page - Main hub for user
 * 
 * Displays:
 * 1. Overall statistics (total, completed, pending topics)
 * 2. Progress bar with completion percentage
 * 3. Today's revision list
 * 4. Quick access to sections
 * 5. Action buttons for key tasks
 */
function DashboardPage() {
  const navigate = useNavigate();
  const [dashboard, setDashboard] = useState(null);
  const [sections, setSections] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showCreateSectionModal, setShowCreateSectionModal] = useState(false);
  const [newSectionTitle, setNewSectionTitle] = useState('');
  const [newSectionDesc, setNewSectionDesc] = useState('');

  // Fetch dashboard data on component mount
  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    try {
      setLoading(true);
      setError(null);

      // Fetch dashboard stats
      const dashboardData = await dashboardService.getDashboard();
      setDashboard(dashboardData);

      try {
        // Sections are supplementary. Keep the dashboard usable if this call fails.
        const sectionsData = await sectionService.getSections();
        setSections(sectionsData);
      } catch (sectionError) {
        console.error('Error fetching sections:', sectionError);
        setSections([]);
        toast.error(getErrorMessage(sectionError, 'Failed to load sections'));
      }
    } catch (err) {
      console.error('Error fetching dashboard:', err);
      const message = getErrorMessage(err, 'Failed to load dashboard');
      setError(message);
      toast.error(message);
    } finally {
      setLoading(false);
    }
  };

  const handleCompleteRevision = async (topicId, sectionId) => {
    try {
      await topicService.completeRevision(sectionId, topicId);
      toast.success('Revision marked as complete! Next revision scheduled.');
      fetchDashboardData(); // Refresh data
    } catch (err) {
      toast.error('Failed to complete revision');
    }
  };

  const handleCreateSection = async (e) => {
    e.preventDefault();

    if (!newSectionTitle.trim()) {
      toast.error('Section title is required');
      return;
    }

    try {
      const createdSection = await sectionService.createSection(
        newSectionTitle.trim(),
        newSectionDesc.trim()
      );

      toast.success('Section created successfully');
      setShowCreateSectionModal(false);
      setNewSectionTitle('');
      setNewSectionDesc('');
      await fetchDashboardData();
      navigate(`/sections/${createdSection.id}`);
    } catch (err) {
      console.error('Error creating section:', err);
      toast.error('Failed to create section');
    }
  };

  if (loading) return <Spinner />;
  if (error) return <ErrorMessage message={error} onRetry={fetchDashboardData} />;
  if (!dashboard) return <div>No data available</div>;

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-50 via-blue-50 to-indigo-100 p-4 md:p-8">
      <div className="max-w-7xl mx-auto">
        {/* Header */}
        <div className="mb-8">
          <h1 className="text-4xl font-bold text-gray-900 mb-2">📚 Welcome Back!</h1>
          <p className="text-gray-600">Here's your study progress and today's revision list</p>
        </div>

        {/* Statistics Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
          {/* Total Topics Card */}
          <div className="bg-white rounded-lg shadow-md p-6 border-l-4 border-blue-500">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-gray-600 text-sm font-medium">Total Topics</p>
                <p className="text-3xl font-bold text-gray-900">{dashboard.totalTopics}</p>
              </div>
              <FiBookOpen className="w-12 h-12 text-blue-500 opacity-20" />
            </div>
          </div>

          {/* Completed Topics Card */}
          <div className="bg-white rounded-lg shadow-md p-6 border-l-4 border-green-500">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-gray-600 text-sm font-medium">Completed</p>
                <p className="text-3xl font-bold text-gray-900">{dashboard.completedTopics}</p>
              </div>
              <FiCheckCircle className="w-12 h-12 text-green-500 opacity-20" />
            </div>
          </div>

          {/* Pending Topics Card */}
          <div className="bg-white rounded-lg shadow-md p-6 border-l-4 border-orange-500">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-gray-600 text-sm font-medium">Pending</p>
                <p className="text-3xl font-bold text-gray-900">{dashboard.pendingTopics}</p>
              </div>
              <FiClock className="w-12 h-12 text-orange-500 opacity-20" />
            </div>
          </div>

          {/* Today's Revisions Card */}
          <div className="bg-white rounded-lg shadow-md p-6 border-l-4 border-purple-500">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-gray-600 text-sm font-medium">Today's Revisions</p>
                <p className="text-3xl font-bold text-gray-900">{dashboard.todayRevisionCount}</p>
              </div>
              <FiCalendar className="w-12 h-12 text-purple-500 opacity-20" />
            </div>
          </div>
        </div>

        {/* Progress Section */}
        <div className="bg-white rounded-lg shadow-md p-6 mb-8">
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-2xl font-bold text-gray-900 flex items-center gap-2">
              <FiTrendingUp className="w-6 h-6 text-indigo-600" />
              Overall Progress
            </h2>
            <span className="text-2xl font-bold text-indigo-600">
              {Math.round(dashboard.completionPercentage)}%
            </span>
          </div>
          <ProgressBar 
            completed={dashboard.completedTopics} 
            total={dashboard.totalTopics}
            height="h-4"
          />
          <p className="text-gray-600 text-sm mt-2">
            {dashboard.completedTopics} of {dashboard.totalTopics} topics completed
          </p>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Today's Revisions Section */}
          <div className="lg:col-span-2">
            <div className="bg-white rounded-lg shadow-md p-6">
              <h2 className="text-2xl font-bold text-gray-900 mb-4 flex items-center gap-2">
                <FiCalendar className="w-6 h-6 text-purple-600" />
                Today's Revision List
              </h2>

              {dashboard.todayRevisions && dashboard.todayRevisions.length > 0 ? (
                <div className="space-y-3">
                  {dashboard.todayRevisions.map((topic) => (
                    <div 
                      key={topic.id}
                      className="p-4 border border-gray-200 rounded-lg hover:shadow-md transition-shadow"
                    >
                      <div className="flex items-start justify-between">
                        <div className="flex-1">
                          <h3 className="font-semibold text-gray-900">{topic.title}</h3>
                          <p className="text-sm text-gray-600">
                            {topic.sectionTitle} → {topic.subsectionTitle}
                          </p>
                          <p className="text-xs text-gray-500 mt-1">
                            📅 Revision Date: {new Date(topic.revisionDate).toLocaleDateString()}
                          </p>
                        </div>
                        <button
                          onClick={() => handleCompleteRevision(topic.id, topic.sectionId)}
                          className="ml-4 px-4 py-2 bg-green-500 text-white rounded-lg hover:bg-green-600 transition-colors whitespace-nowrap"
                        >
                          ✓ Done
                        </button>
                      </div>
                    </div>
                  ))}
                </div>
              ) : (
                <div className="text-center py-12">
                  <FiAlertCircle className="w-12 h-12 text-gray-400 mx-auto mb-2 opacity-50" />
                  <p className="text-gray-500 text-lg">No revisions due today! 🎉</p>
                  <p className="text-gray-400 text-sm mt-1">Great job staying on top of your studies.</p>
                </div>
              )}
            </div>
          </div>

          {/* Quick Access Sections */}
          <div className="lg:col-span-1">
            <div className="bg-white rounded-lg shadow-md p-6">
              <h2 className="text-2xl font-bold text-gray-900 mb-4 flex items-center gap-2">
                <FiBookOpen className="w-6 h-6 text-blue-600" />
                My Sections
              </h2>

              {sections && sections.length > 0 ? (
                <div className="space-y-2">
                  {sections.slice(0, 5).map((section) => (
                    <button
                      key={section.id}
                      onClick={() => navigate(`/sections/${section.id}`)}
                      className="w-full text-left p-3 rounded-lg bg-gradient-to-r from-blue-50 to-indigo-50 hover:from-blue-100 hover:to-indigo-100 transition-colors border border-blue-200"
                    >
                      <p className="font-semibold text-gray-900">{section.title}</p>
                      <p className="text-xs text-gray-600">
                        {section.totalTopics} topics
                      </p>
                    </button>
                  ))}
                  
                  {sections.length > 5 && (
                    <p className="text-xs text-gray-500 text-center mt-2">
                      +{sections.length - 5} more sections
                    </p>
                  )}
                </div>
              ) : (
                <p className="text-gray-500">No sections yet. Create one to get started!</p>
              )}

              <button
                onClick={() => setShowCreateSectionModal(true)}
                className="w-full mt-4 px-4 py-3 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition-colors font-medium"
              >
                + New Section
              </button>
            </div>

            {/* Quick Actions */}
            <div className="bg-white rounded-lg shadow-md p-6 mt-4">
              <h3 className="font-bold text-gray-900 mb-3">Quick Actions</h3>
              <div className="space-y-2">
                <button
                  onClick={() => navigate('/revisions')}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50 transition-colors text-gray-700 font-medium"
                >
                  📋 All Revisions
                </button>
                <button
                  onClick={fetchDashboardData}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50 transition-colors text-gray-700 font-medium"
                >
                  🔄 Refresh
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      {showCreateSectionModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
          <div className="bg-white rounded-lg shadow-lg max-w-md w-full p-6">
            <h3 className="text-2xl font-bold text-gray-900 mb-4">New Section</h3>
            <form onSubmit={handleCreateSection} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Section Title *
                </label>
                <input
                  type="text"
                  value={newSectionTitle}
                  onChange={(e) => setNewSectionTitle(e.target.value)}
                  placeholder="e.g., Data Structures"
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Description (Optional)
                </label>
                <textarea
                  value={newSectionDesc}
                  onChange={(e) => setNewSectionDesc(e.target.value)}
                  placeholder="Add a description..."
                  rows="3"
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500"
                />
              </div>
              <div className="flex gap-2">
                <button
                  type="submit"
                  className="flex-1 px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 font-medium"
                >
                  Create
                </button>
                <button
                  type="button"
                  onClick={() => {
                    setShowCreateSectionModal(false);
                    setNewSectionTitle('');
                    setNewSectionDesc('');
                  }}
                  className="flex-1 px-4 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 font-medium"
                >
                  Cancel
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}

export default DashboardPage;
