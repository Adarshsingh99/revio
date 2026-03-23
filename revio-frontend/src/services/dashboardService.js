import api from './api';

const dashboardService = {
  /**
   * Get dashboard statistics and today's revisions
   */
  getDashboard: async () => {
    try {
      const response = await api.get('/dashboard');
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },
};

export default dashboardService;
