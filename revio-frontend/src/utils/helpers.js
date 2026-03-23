import { formatDistanceToNow, format } from 'date-fns';

/**
 * Format date for display
 */
export const formatDate = (dateString) => {
  if (!dateString) return 'N/A';
  try {
    const date = new Date(dateString);
    return format(date, 'MMM dd, yyyy');
  } catch (error) {
    return 'Invalid date';
  }
};

/**
 * Format date for time-based display (e.g., "2 hours ago")
 */
export const formatDateDistance = (dateString) => {
  if (!dateString) return 'N/A';
  try {
    const date = new Date(dateString);
    return formatDistanceToNow(date, { addSuffix: true });
  } catch (error) {
    return 'Invalid date';
  }
};

/**
 * Format percentage with one decimal
 */
export const formatPercentage = (value) => {
  if (typeof value !== 'number') return '0%';
  return `${(value || 0).toFixed(1)}%`;
};

/**
 * Validate email format
 */
export const isValidEmail = (email) => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
};

/**
 * Validate password strength
 */
export const validatePassword = (password) => {
  return {
    isValid: password.length >= 6,
    errors: password.length < 6 ? ['Password must be at least 6 characters'] : [],
  };
};

/**
 * Get status badge color
 */
export const getStatusColor = (status) => {
  const colors = {
    PENDING: 'bg-yellow-100 text-yellow-800',
    COMPLETED: 'bg-green-100 text-green-800',
    FULLY_REVISED: 'bg-blue-100 text-blue-800',
    REVISION: 'bg-orange-100 text-orange-800',
  };
  return colors[status] || 'bg-gray-100 text-gray-800';
};

/**
 * Get progress bar color based on percentage
 */
export const getProgressColor = (percentage) => {
  if (percentage < 30) return 'bg-red-500';
  if (percentage < 60) return 'bg-yellow-500';
  if (percentage < 90) return 'bg-blue-500';
  return 'bg-green-500';
};

/**
 * Truncate text to specified length
 */
export const truncateText = (text, length = 50) => {
  if (!text || text.length <= length) return text;
  return `${text.substring(0, length)}...`;
};

/**
 * Check if date is today
 */
export const isToday = (dateString) => {
  if (!dateString) return false;
  const date = new Date(dateString).toDateString();
  const today = new Date().toDateString();
  return date === today;
};

/**
 * Check if date is in the past
 */
export const isPastDate = (dateString) => {
  if (!dateString) return false;
  const date = new Date(dateString);
  const today = new Date();
  today.setHours(0, 0, 0, 0);
  return date < today;
};

/**
 * Check if date is in the future
 */
export const isFutureDate = (dateString) => {
  if (!dateString) return false;
  const date = new Date(dateString);
  const today = new Date();
  today.setHours(23, 59, 59, 999);
  return date > today;
};

/**
 * Get days until date
 */
export const getDaysUntil = (dateString) => {
  if (!dateString) return 0;
  const date = new Date(dateString);
  const today = new Date();
  const diffTime = date.getTime() - today.getTime();
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
  return diffDays;
};
