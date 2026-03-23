import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { FiCalendar, FiCheckCircle, FiChevronLeft, FiAlertCircle } from 'react-icons/fi';
import toast from 'react-hot-toast';
import topicService from '../services/topicService';
import Spinner from '../components/Spinner';
import ErrorMessage from '../components/ErrorMessage';

/**
 * Revision Page - Main revision tracking interface
 * 
 * Displays:
 * 1. All topics due for revision today
 * 2. Section and subsection context
 * 3. Ability to mark revisions as complete
 * 4. Progress tracking for revision sessions
 */
function RevisionPage() {
  const navigate = useNavigate();
  const [revisions, setRevisions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [completedCount, setCompletedCount] = useState(0);

  useEffect(() => {
    fetchTodayRevisions();
  }, []);

  const fetchTodayRevisions = async () => {
    try {
      setLoading(true);
      setError(null);
      const revisionsData = await topicService.getTodayRevisions();
      setRevisions(revisionsData);
      setCompletedCount(0);
    } catch (err) {
      console.error('Error fetching revisions:', err);
      setError(err.response?.data?.message || 'Failed to load revisions');
      toast.error('Failed to load revisions');
    } finally {
      setLoading(false);
    }
  };

  const handleCompleteRevision = async (topicId, sectionId) => {
    try {
      await topicService.completeRevision(sectionId, topicId);
      
      toast.success('✓ Revision completed! Next revision scheduled.');
      setCompletedCount(prev => prev + 1);
      
      // Refresh revisions
      fetchTodayRevisions();
    } catch (err) {
      console.error('Error completing revision:', err);
      toast.error('Failed to complete revision');
    }
  };

  if (loading) return <Spinner />;
  if (error) return <ErrorMessage message={error} onRetry={fetchTodayRevisions} />;

  const totalRevisions = revisions.length;
  const progressPercentage = totalRevisions > 0 ? (completedCount / totalRevisions) * 100 : 0;

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-50 via-blue-50 to-indigo-100 p-4 md:p-8">
      <div className="max-w-4xl mx-auto">
        {/* Header */}
        <div className="mb-8">
          <button
            onClick={() => navigate('/dashboard')}
            className="flex items-center gap-2 text-indigo-600 hover:text-indigo-700 font-medium mb-4"
          >
            <FiChevronLeft className="w-5 h-5" />
            Back to Dashboard
          </button>

          <div className="bg-white rounded-lg shadow-md p-8">
            <h1 className="text-4xl font-bold text-gray-900 mb-2 flex items-center gap-3">
              <FiCalendar className="w-10 h-10 text-purple-600" />
              Today's Revisions
            </h1>
            <p className="text-gray-600 text-lg">
              📅 {new Date().toLocaleDateString('en-US', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' })}
            </p>

            {/* Progress Section */}
            {totalRevisions > 0 && (
              <div className="mt-6 pt-6 border-t border-gray-200">
                <div className="flex items-center justify-between mb-3">
                  <span className="text-lg font-semibold text-gray-900">Session Progress</span>
                  <span className="text-2xl font-bold text-purple-600">{completedCount}/{totalRevisions}</span>
                </div>
                <div className="w-full bg-gray-200 rounded-full h-4 overflow-hidden">
                  <div
                    className="bg-gradient-to-r from-green-500 to-purple-600 h-full transition-all duration-300"
                    style={{ width: `${progressPercentage}%` }}
                  ></div>
                </div>
                <p className="text-sm text-gray-600 mt-2">
                  {completedCount === totalRevisions 
                    ? '🎉 All revisions completed!' 
                    : `${totalRevisions - completedCount} revisions remaining`}
                </p>
              </div>
            )}
          </div>
        </div>

        {/* Revisions List */}
        <div className="bg-white rounded-lg shadow-md p-6">
          {revisions.length > 0 ? (
            <div className="space-y-4">
              {revisions.map((revision, index) => (
                <div
                  key={revision.id}
                  className="border border-gray-200 rounded-lg p-6 hover:shadow-md transition-shadow"
                >
                  <div className="flex items-start justify-between mb-3">
                    <div className="flex-1">
                      {/* Topic Number */}
                      <div className="inline-block px-3 py-1 bg-purple-100 text-purple-700 rounded-full text-xs font-bold mb-2">
                        Topic {index + 1} of {revisions.length}
                      </div>

                      {/* Topic Title */}
                      <h3 className="text-2xl font-bold text-gray-900 mb-1">
                        {revision.title}
                      </h3>

                      {/* Breadcrumb */}
                      <p className="text-sm text-gray-600 mb-3">
                        📚 {revision.sectionTitle} → {revision.subsectionTitle}
                      </p>

                      {/* Revision Date */}
                      <div className="inline-block px-3 py-1 bg-blue-50 text-blue-700 rounded-lg text-xs font-medium border border-blue-200">
                        📅 Revision Date: {new Date(revision.revisionDate).toLocaleDateString()}
                      </div>
                    </div>

                    {/* Status Indicator */}
                    <div className="flex-shrink-0">
                      <div className="w-12 h-12 rounded-full bg-gradient-to-br from-purple-400 to-blue-500 flex items-center justify-center text-white font-bold text-lg">
                        {index + 1}
                      </div>
                    </div>
                  </div>

                  {/* Action Button */}
                  <div className="mt-4">
                    <button
                      onClick={() => handleCompleteRevision(revision.id, revision.sectionId)}
                      className="px-6 py-3 bg-green-500 hover:bg-green-600 text-white rounded-lg font-semibold transition-colors flex items-center gap-2 w-full justify-center"
                    >
                      <FiCheckCircle className="w-5 h-5" />
                      Mark Revision Complete
                    </button>
                  </div>
                </div>
              ))}

              {/* Completion Message */}
              {completedCount === totalRevisions && totalRevisions > 0 && (
                <div className="bg-gradient-to-r from-green-50 to-emerald-50 border-2 border-green-500 rounded-lg p-8 text-center">
                  <div className="text-5xl mb-4">🎉</div>
                  <h3 className="text-2xl font-bold text-green-900 mb-2">
                    Excellent Work!
                  </h3>
                  <p className="text-green-700 mb-4">
                    You've completed all {totalRevisions} revisions for today. 
                    Keep up this consistent practice for optimal learning!
                  </p>
                  <button
                    onClick={() => navigate('/dashboard')}
                    className="px-6 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 font-medium"
                  >
                    Back to Dashboard
                  </button>
                </div>
              )}
            </div>
          ) : (
            <div className="text-center py-16">
              <FiAlertCircle className="w-16 h-16 text-gray-300 mx-auto mb-4" />
              <h3 className="text-2xl font-bold text-gray-900 mb-2">
                No Revisions Due Today
              </h3>
              <p className="text-gray-600 mb-6 text-lg">
                Great job! You're all caught up with your revisions. 
                Complete more topics to schedule future revisions.
              </p>
              <div className="flex gap-2 justify-center">
                <button
                  onClick={() => navigate('/dashboard')}
                  className="px-6 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 font-medium"
                >
                  Back to Dashboard
                </button>
                <button
                  onClick={() => {
                    navigate('/dashboard');
                    // This will redirect to dashboard where user can create more topics
                  }}
                  className="px-6 py-2 border border-indigo-600 text-indigo-600 rounded-lg hover:bg-indigo-50 font-medium"
                >
                  Create Topics
                </button>
              </div>
            </div>
          )}
        </div>

        {/* Tips Section */}
        <div className="mt-8 bg-blue-50 border-l-4 border-blue-500 rounded-lg p-6">
          <h3 className="font-bold text-gray-900 mb-3 flex items-center gap-2">
            💡 Revision Tips
          </h3>
          <ul className="text-gray-700 space-y-2">
            <li>• Complete revisions on the scheduled dates for optimal memory retention</li>
            <li>• Use spaced repetition to fight the forgetting curve</li>
            <li>• Revise consistently to build long-term retention</li>
            <li>• Each completed revision unlocks the next scheduled review date</li>
          </ul>
        </div>
      </div>
    </div>
  );
}

export default RevisionPage;
