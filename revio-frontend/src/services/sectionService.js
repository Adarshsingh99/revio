import api from './api';

const sectionService = {
  /**
   * Get all sections for current user
   */
  getSections: async () => {
    try {
      const response = await api.get('/sections');
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  /**
   * Get a specific section (alias)
   */
  getSectionById: async (sectionId) => {
    try {
      const response = await api.get(`/sections/${sectionId}`);
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  /**
   * Get a specific section (original name)
   */
  getSection: async (sectionId) => {
    try {
      const response = await api.get(`/sections/${sectionId}`);
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  /**
   * Create a new section
   */
  createSection: async (title, description) => {
    try {
      const response = await api.post('/sections', {
        title,
        description,
      });
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  /**
   * Update a section
   */
  updateSection: async (sectionId, title, description) => {
    try {
      const response = await api.put(`/sections/${sectionId}`, {
        title,
        description,
      });
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  /**
   * Delete a section
   */
  deleteSection: async (sectionId) => {
    try {
      await api.delete(`/sections/${sectionId}`);
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  /**
   * Get all subsections in a section
   */
  getSubsections: async (sectionId) => {
    try {
      const response = await api.get(`/sections/${sectionId}/subsections`);
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  /**
   * Create a new subsection
   */
  createSubsection: async (sectionId, subsectionData) => {
    try {
      const response = await api.post(
        `/sections/${sectionId}/subsections`,
        subsectionData
      );
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  /**
   * Get a specific subsection (with alias)
   */
  getSubsectionById: async (sectionId, subsectionId) => {
    try {
      const response = await api.get(
        `/sections/${sectionId}/subsections/${subsectionId}`
      );
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  /**
   * Update a subsection
   */
  updateSubsection: async (sectionId, subsectionId, title, description) => {
    try {
      const response = await api.put(
        `/sections/${sectionId}/subsections/${subsectionId}`,
        { title, description }
      );
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  /**
   * Delete a subsection
   */
  deleteSubsection: async (sectionId, subsectionId) => {
    try {
      await api.delete(
        `/sections/${sectionId}/subsections/${subsectionId}`
      );
    } catch (error) {
      throw error.response?.data || error;
    }
  },
};

export default sectionService;
