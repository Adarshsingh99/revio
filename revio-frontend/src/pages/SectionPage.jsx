import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { FiChevronLeft, FiPlus, FiTrash2, FiEdit2, FiBookOpen } from 'react-icons/fi';
import toast from 'react-hot-toast';
import sectionService from '../services/sectionService';
import ProgressBar from '../components/ProgressBar';
import Spinner from '../components/Spinner';
import ErrorMessage from '../components/ErrorMessage';

/**
 * Section Page - View and manage a specific section
 * 
 * Displays:
 * 1. Section title and description
 * 2. List of subsections with progress
 * 3. Option to create new subsection
 * 4. Quick navigation to subsection details
 */
function SectionPage() {
  const { sectionId } = useParams();
  const navigate = useNavigate();
  const [section, setSection] = useState(null);
  const [subsections, setSubsections] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showAddModal, setShowAddModal] = useState(false);
  const [newSubsectionTitle, setNewSubsectionTitle] = useState('');
  const [newSubsectionDesc, setNewSubsectionDesc] = useState('');

  useEffect(() => {
    fetchSectionData();
  }, [sectionId]);

  const fetchSectionData = async () => {
    try {
      setLoading(true);
      setError(null);

      // Fetch section details
      const sectionData = await sectionService.getSectionById(sectionId);
      setSection(sectionData);

      // Fetch subsections
      const subsectionsData = await sectionService.getSubsections(sectionId);
      setSubsections(subsectionsData);
    } catch (err) {
      console.error('Error fetching section:', err);
      setError(err.response?.data?.message || 'Failed to load section');
      toast.error('Failed to load section');
    } finally {
      setLoading(false);
    }
  };

  const handleAddSubsection = async (e) => {
    e.preventDefault();
    if (!newSubsectionTitle.trim()) {
      toast.error('Subsection title is required');
      return;
    }

    try {
      await sectionService.createSubsection(sectionId, {
        title: newSubsectionTitle,
        description: newSubsectionDesc
      });
      
      toast.success('Subsection created successfully!');
      setNewSubsectionTitle('');
      setNewSubsectionDesc('');
      setShowAddModal(false);
      fetchSectionData();
    } catch (err) {
      toast.error('Failed to create subsection');
    }
  };

  const handleDeleteSubsection = async (subsectionId) => {
    if (window.confirm('Are you sure you want to delete this subsection?')) {
      try {
        await sectionService.deleteSubsection(sectionId, subsectionId);
        toast.success('Subsection deleted successfully');
        fetchSectionData();
      } catch (err) {
        toast.error('Failed to delete subsection');
      }
    }
  };

  if (loading) return <Spinner />;
  if (error) return <ErrorMessage message={error} onRetry={fetchSectionData} />;
  if (!section) return <div>No section found</div>;

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-50 via-blue-50 to-indigo-100 p-4 md:p-8">
      <div className="max-w-6xl mx-auto">
        {/* Header with back button */}
        <div className="mb-8">
          <button
            onClick={() => navigate('/dashboard')}
            className="flex items-center gap-2 text-indigo-600 hover:text-indigo-700 font-medium mb-4"
          >
            <FiChevronLeft className="w-5 h-5" />
            Back to Dashboard
          </button>

          <div className="bg-white rounded-lg shadow-md p-8 mb-8">
            <h1 className="text-4xl font-bold text-gray-900 mb-2">{section.title}</h1>
            {section.description && (
              <p className="text-gray-600 text-lg">{section.description}</p>
            )}

            {/* Section Statistics */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mt-6 pt-6 border-t border-gray-200">
              <div>
                <p className="text-sm text-gray-600 font-medium">Total Subsections</p>
                <p className="text-2xl font-bold text-gray-900">{section.subsectionCount}</p>
              </div>
              <div>
                <p className="text-sm text-gray-600 font-medium">Total Topics</p>
                <p className="text-2xl font-bold text-gray-900">{section.totalTopics}</p>
              </div>
              <div>
                <p className="text-sm text-gray-600 font-medium">Completed Topics</p>
                <p className="text-2xl font-bold text-green-600">{section.completedTopics}</p>
              </div>
            </div>

            {/* Progress Bar */}
            {section.totalTopics > 0 && (
              <div className="mt-6">
                <div className="flex items-center justify-between mb-2">
                  <span className="text-sm font-medium text-gray-700">Overall Progress</span>
                  <span className="text-sm font-bold text-indigo-600">
                    {Math.round((section.completedTopics / section.totalTopics) * 100)}%
                  </span>
                </div>
                <ProgressBar
                  completed={section.completedTopics}
                  total={section.totalTopics}
                  height="h-3"
                />
              </div>
            )}
          </div>
        </div>

        {/* Subsections List */}
        <div className="bg-white rounded-lg shadow-md p-6 mb-8">
          <div className="flex items-center justify-between mb-6">
            <h2 className="text-2xl font-bold text-gray-900 flex items-center gap-2">
              <FiBookOpen className="w-6 h-6 text-blue-600" />
              Subsections ({subsections.length})
            </h2>
            <button
              onClick={() => setShowAddModal(true)}
              className="px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition-colors flex items-center gap-2"
            >
              <FiPlus className="w-5 h-5" />
              New Subsection
            </button>
          </div>

          {subsections.length > 0 ? (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {subsections.map((subsection) => (
                <div
                  key={subsection.id}
                  className="border border-gray-200 rounded-lg p-5 hover:shadow-md transition-shadow cursor-pointer"
                  onClick={() => navigate(`/sections/${sectionId}/subsections/${subsection.id}`)}
                >
                  <div className="flex items-start justify-between mb-3">
                    <h3 className="text-xl font-semibold text-gray-900">
                      {subsection.title}
                    </h3>
                    <div className="flex gap-2">
                      <button
                        onClick={(e) => {
                          e.stopPropagation();
                          // Edit functionality can be added here
                          toast.info('Edit feature coming soon');
                        }}
                        className="p-2 text-gray-400 hover:text-gray-600 transition-colors"
                      >
                        <FiEdit2 className="w-4 h-4" />
                      </button>
                      <button
                        onClick={(e) => {
                          e.stopPropagation();
                          handleDeleteSubsection(subsection.id);
                        }}
                        className="p-2 text-gray-400 hover:text-red-600 transition-colors"
                      >
                        <FiTrash2 className="w-4 h-4" />
                      </button>
                    </div>
                  </div>

                  {subsection.description && (
                    <p className="text-gray-600 text-sm mb-3">{subsection.description}</p>
                  )}

                  {/* Subsection Stats */}
                  <div className="flex items-center justify-between mb-3">
                    <div className="flex gap-4 text-sm">
                      <span className="text-gray-600">
                        📚 {subsection.topicCount} topics
                      </span>
                      <span className="text-green-600 font-medium">
                        ✓ {subsection.completedTopics} done
                      </span>
                    </div>
                  </div>

                  {/* Progress Bar */}
                  <ProgressBar
                    completed={subsection.completedTopics}
                    total={subsection.topicCount}
                    height="h-2"
                  />
                  <p className="text-xs text-gray-500 mt-2">
                    {Math.round(subsection.progressPercentage)}% complete
                  </p>
                </div>
              ))}
            </div>
          ) : (
            <div className="text-center py-12">
              <FiBookOpen className="w-16 h-16 text-gray-300 mx-auto mb-4" />
              <p className="text-gray-500 text-lg font-medium">No subsections yet</p>
              <p className="text-gray-400 mb-4">Create a subsection to start organizing your topics</p>
              <button
                onClick={() => setShowAddModal(true)}
                className="px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700"
              >
                Create First Subsection
              </button>
            </div>
          )}
        </div>

        {/* Add Subsection Modal */}
        {showAddModal && (
          <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
            <div className="bg-white rounded-lg shadow-lg max-w-md w-full p-6">
              <h3 className="text-2xl font-bold text-gray-900 mb-4">New Subsection</h3>
              <form onSubmit={handleAddSubsection} className="space-y-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Subsection Title *
                  </label>
                  <input
                    type="text"
                    value={newSubsectionTitle}
                    onChange={(e) => setNewSubsectionTitle(e.target.value)}
                    placeholder="e.g., Arrays, Linked Lists"
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Description (Optional)
                  </label>
                  <textarea
                    value={newSubsectionDesc}
                    onChange={(e) => setNewSubsectionDesc(e.target.value)}
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

export default SectionPage;
