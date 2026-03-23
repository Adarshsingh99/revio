import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { FiChevronLeft, FiPlus, FiTrash2, FiCheckCircle, FiCircle } from 'react-icons/fi';
import toast from 'react-hot-toast';
import sectionService from '../services/sectionService';
import topicService from '../services/topicService';
import ProgressBar from '../components/ProgressBar';
import Spinner from '../components/Spinner';
import ErrorMessage from '../components/ErrorMessage';

/**
 * Subsection Page - Manage topics within a subsection
 * 
 * Displays:
 * 1. All topics in the subsection
 * 2. Topic status (pending, completed, revision due)
 * 3. Ability to mark topics as complete (triggers revision schedule)
 * 4. Revision date information
 * 5. Option to add new topics
 */
function SubsectionPage() {
  const { sectionId, subsectionId } = useParams();
  const navigate = useNavigate();
  const [subsection, setSubsection] = useState(null);
  const [topics, setTopics] = useState([]);
  const [sectionTitle, setSectionTitle] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showAddModal, setShowAddModal] = useState(false);
  const [newTopicTitle, setNewTopicTitle] = useState('');
  const [newTopicDesc, setNewTopicDesc] = useState('');
  const [expandedTopic, setExpandedTopic] = useState(null);

  useEffect(() => {
    fetchSubsectionData();
  }, [sectionId, subsectionId]);

  const fetchSubsectionData = async () => {
    try {
      setLoading(true);
      setError(null);

      // Fetch section (for title)
      const sectionData = await sectionService.getSectionById(sectionId);
      setSectionTitle(sectionData.title);

      // Fetch subsection
      const subsectionData = await sectionService.getSubsectionById(sectionId, subsectionId);
      setSubsection(subsectionData);

      // Fetch topics
      const topicsData = await topicService.getTopics(sectionId, subsectionId);
      setTopics(topicsData);
    } catch (err) {
      console.error('Error fetching subsection:', err);
      setError(err.response?.data?.message || 'Failed to load subsection');
      toast.error('Failed to load subsection');
    } finally {
      setLoading(false);
    }
  };

  const handleAddTopic = async (e) => {
    e.preventDefault();
    if (!newTopicTitle.trim()) {
      toast.error('Topic title is required');
      return;
    }

    try {
      await topicService.createTopic(sectionId, subsectionId, {
        title: newTopicTitle,
        description: newTopicDesc
      });
      
      toast.success('Topic created successfully!');
      setNewTopicTitle('');
      setNewTopicDesc('');
      setShowAddModal(false);
      fetchSubsectionData();
    } catch (err) {
      toast.error('Failed to create topic');
    }
  };

  const handleCompleteTopic = async (topicId) => {
    try {
      await topicService.completeTopic(sectionId, topicId);
      toast.success('✓ Topic completed! Revision schedule created.');
      fetchSubsectionData();
    } catch (err) {
      toast.error('Failed to complete topic');
    }
  };

  const handleDeleteTopic = async (topicId) => {
    if (window.confirm('Are you sure you want to delete this topic?')) {
      try {
        await topicService.deleteTopic(sectionId, subsectionId, topicId);
        toast.success('Topic deleted successfully');
        fetchSubsectionData();
      } catch (err) {
        toast.error('Failed to delete topic');
      }
    }
  };

  const getStatusBadge = (topic) => {
    if (topic.status === 'COMPLETED') {
      if (topic.isDueForRevisionToday) {
        return <span className="px-3 py-1 bg-purple-100 text-purple-700 text-xs font-semibold rounded-full">📅 Revision Due Today</span>;
      }
      if (topic.nextRevision) {
        const nextDate = new Date(topic.nextRevision).toLocaleDateString();
        return <span className="px-3 py-1 bg-blue-100 text-blue-700 text-xs font-semibold rounded-full">Next: {nextDate}</span>;
      }
      return <span className="px-3 py-1 bg-green-100 text-green-700 text-xs font-semibold rounded-full">✓ Completed</span>;
    }
    return <span className="px-3 py-1 bg-gray-100 text-gray-700 text-xs font-semibold rounded-full">⏳ Pending</span>;
  };

  if (loading) return <Spinner />;
  if (error) return <ErrorMessage message={error} onRetry={fetchSubsectionData} />;
  if (!subsection) return <div>No subsection found</div>;

  const completedTopics = topics.filter(t => t.status === 'COMPLETED').length;
  const pendingTopics = topics.length - completedTopics;

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-50 via-blue-50 to-indigo-100 p-4 md:p-8">
      <div className="max-w-6xl mx-auto">
        {/* Header */}
        <div className="mb-8">
          <button
            onClick={() => navigate(`/sections/${sectionId}`)}
            className="flex items-center gap-2 text-indigo-600 hover:text-indigo-700 font-medium mb-4"
          >
            <FiChevronLeft className="w-5 h-5" />
            Back to {sectionTitle}
          </button>

          <div className="bg-white rounded-lg shadow-md p-8">
            <h1 className="text-4xl font-bold text-gray-900 mb-2">{subsection.title}</h1>
            {subsection.description && (
              <p className="text-gray-600 text-lg mb-4">{subsection.description}</p>
            )}

            {/* Statistics */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4 pt-6 border-t border-gray-200">
              <div>
                <p className="text-sm text-gray-600 font-medium">Total Topics</p>
                <p className="text-2xl font-bold text-gray-900">{subsection.topicCount}</p>
              </div>
              <div>
                <p className="text-sm text-gray-600 font-medium">Completed</p>
                <p className="text-2xl font-bold text-green-600">{subsection.completedTopics}</p>
              </div>
              <div>
                <p className="text-sm text-gray-600 font-medium">Pending</p>
                <p className="text-2xl font-bold text-orange-600">{subsection.pendingTopics}</p>
              </div>
            </div>

            {/* Progress Bar */}
            {subsection.topicCount > 0 && (
              <div className="mt-6">
                <div className="flex items-center justify-between mb-2">
                  <span className="text-sm font-medium text-gray-700">Progress</span>
                  <span className="text-sm font-bold text-indigo-600">
                    {Math.round(subsection.progressPercentage)}%
                  </span>
                </div>
                <ProgressBar
                  completed={subsection.completedTopics}
                  total={subsection.topicCount}
                  height="h-3"
                />
              </div>
            )}
          </div>
        </div>

        {/* Topics List */}
        <div className="bg-white rounded-lg shadow-md p-6">
          <div className="flex items-center justify-between mb-6">
            <h2 className="text-2xl font-bold text-gray-900">
              Topics ({topics.length})
            </h2>
            <button
              onClick={() => setShowAddModal(true)}
              className="px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition-colors flex items-center gap-2"
            >
              <FiPlus className="w-5 h-5" />
              New Topic
            </button>
          </div>

          {topics.length > 0 ? (
            <div className="space-y-3">
              {topics.map((topic) => (
                <div
                  key={topic.id}
                  className="border border-gray-200 rounded-lg p-5 hover:shadow-md transition-shadow"
                >
                  <div className="flex items-start justify-between mb-3">
                    <div className="flex-1">
                      <div className="flex items-center gap-3">
                        {topic.status === 'COMPLETED' ? (
                          <FiCheckCircle className="w-6 h-6 text-green-500 flex-shrink-0" />
                        ) : (
                          <FiCircle className="w-6 h-6 text-gray-300 flex-shrink-0" />
                        )}
                        <h3 className="text-lg font-semibold text-gray-900">
                          {topic.title}
                        </h3>
                      </div>
                      {topic.description && (
                        <p className="text-gray-600 text-sm ml-9 mt-1">{topic.description}</p>
                      )}
                    </div>
                    <button
                      onClick={() => handleDeleteTopic(topic.id)}
                      className="p-2 text-gray-400 hover:text-red-600 transition-colors flex-shrink-0"
                    >
                      <FiTrash2 className="w-4 h-4" />
                    </button>
                  </div>

                  {/* Status Badge and Details */}
                  <div className="ml-9 flex items-center gap-3">
                    {getStatusBadge(topic)}
                  </div>

                  {/* Revision Dates (if completed) */}
                  {topic.status === 'COMPLETED' && topic.revisionDates && (
                    <div className="ml-9 mt-3 p-3 bg-blue-50 rounded-lg border border-blue-200">
                      <p className="text-xs font-semibold text-blue-900 mb-2">📅 Revision Schedule:</p>
                      <div className="grid grid-cols-2 md:grid-cols-4 gap-2">
                        {topic.revisionDates.map((date, idx) => (
                          <div
                            key={idx}
                            className={`p-2 rounded text-center text-xs font-medium ${
                              new Date(date).toDateString() === new Date().toDateString()
                                ? 'bg-purple-200 text-purple-900 border border-purple-400'
                                : 'bg-white text-gray-700 border border-blue-200'
                            }`}
                          >
                            <p className="text-xs opacity-75">Day {idx + 1}</p>
                            <p>{new Date(date).toLocaleDateString()}</p>
                          </div>
                        ))}
                      </div>
                    </div>
                  )}

                  {/* Action Buttons */}
                  <div className="ml-9 mt-3 flex gap-2">
                    {topic.status === 'PENDING' && (
                      <button
                        onClick={() => handleCompleteTopic(topic.id)}
                        className="px-4 py-2 bg-green-500 text-white rounded-lg hover:bg-green-600 transition-colors text-sm font-medium"
                      >
                        ✓ Mark Complete
                      </button>
                    )}
                    {topic.status === 'COMPLETED' && topic.isDueForRevisionToday && (
                      <button
                        onClick={() => navigate('/revisions')}
                        className="px-4 py-2 bg-purple-500 text-white rounded-lg hover:bg-purple-600 transition-colors text-sm font-medium"
                      >
                        📅 Revise Now
                      </button>
                    )}
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <div className="text-center py-12">
              <p className="text-gray-500 text-lg font-medium">No topics yet</p>
              <p className="text-gray-400 mb-4">Add your first topic to start learning and tracking revisions</p>
              <button
                onClick={() => setShowAddModal(true)}
                className="px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700"
              >
                Create First Topic
              </button>
            </div>
          )}
        </div>

        {/* Add Topic Modal */}
        {showAddModal && (
          <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
            <div className="bg-white rounded-lg shadow-lg max-w-md w-full p-6">
              <h3 className="text-2xl font-bold text-gray-900 mb-4">New Topic</h3>
              <form onSubmit={handleAddTopic} className="space-y-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Topic Title *
                  </label>
                  <input
                    type="text"
                    value={newTopicTitle}
                    onChange={(e) => setNewTopicTitle(e.target.value)}
                    placeholder="e.g., Binary Search"
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Description (Optional)
                  </label>
                  <textarea
                    value={newTopicDesc}
                    onChange={(e) => setNewTopicDesc(e.target.value)}
                    placeholder="Add notes about this topic..."
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
                    onClick={() => setShowAddModal(false)}
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
    </div>
  );
}

export default SubsectionPage;
