import api from './api';

const topicService = {
  /**
   * Get all topics in a subsection
   */
  getTopicsBySubsection: async (sectionId, subsectionId) => {
    try {
      const response = await api.get(
        `/sections/${sectionId}/subsections/${subsectionId}/topics`
      );
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  /**
   * Get a specific topic
   */
  getTopic: async (sectionId, subsectionId, topicId) => {
    try {
      const response = await api.get(
        `/sections/${sectionId}/subsections/${subsectionId}/topics/${topicId}`
      );
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  /**
   * Create a new topic
   */
  createTopic: async (sectionId, subsectionId, topicData) => {
    try {
      const response = await api.post(
        `/sections/${sectionId}/subsections/${subsectionId}/topics`,
        topicData
      );
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  /**
   * Get topics in a subsection (alias)
   */
  getTopics: async (sectionId, subsectionId) => {
    try {
      const response = await api.get(
        `/sections/${sectionId}/subsections/${subsectionId}/topics`
      );
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  /**
   * Update a topic
   */
  updateTopic: async (sectionId, subsectionId, topicId, title, description) => {
    try {
      const response = await api.put(
        `/sections/${sectionId}/subsections/${subsectionId}/topics/${topicId}`,
        { title, description }
      );
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  /**
   * Delete a topic
   */
  deleteTopic: async (sectionId, subsectionId, topicId) => {
    try {
      await api.delete(
        `/sections/${sectionId}/subsections/${subsectionId}/topics/${topicId}`
      );
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  /**
   * ⭐ Complete a topic and schedule revisions
   */
  completeTopic: async (sectionId, topicId) => {
    try {
      const response = await api.post(
        `/sections/${sectionId}/topics/${topicId}/complete`
      );
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  /**
   * Complete a revision for a topic
   */
  completeRevision: async (sectionId, topicId) => {
    try {
      const response = await api.post(
        `/sections/${sectionId}/topics/${topicId}/revise`
      );
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  /**
   * ⭐ Get today's revision topics
   */
  getTodayRevisions: async () => {
    try {
      const response = await api.get('/revision/today');
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },
};

export default topicService;
