import React from 'react';
import { FiAlertCircle, FiX } from 'react-icons/fi';

/**
 * Error message component
 */
const ErrorMessage = ({ message, onClose, type = 'error' }) => {
  const typeColors = {
    error: 'bg-red-100 text-red-800 border-red-300',
    warning: 'bg-yellow-100 text-yellow-800 border-yellow-300',
    info: 'bg-blue-100 text-blue-800 border-blue-300',
  };

  if (!message) return null;

  return (
    <div className={`border border-solid rounded-lg p-4 flex items-start gap-3 ${typeColors[type]}`}>
      <FiAlertCircle className="flex-shrink-0 mt-0.5" />
      <div className="flex-1">
        <p className="text-sm font-medium">{message}</p>
      </div>
      {onClose && (
        <button
          onClick={onClose}
          className="flex-shrink-0 hover:opacity-70 transition"
          aria-label="Close"
        >
          <FiX />
        </button>
      )}
    </div>
  );
};

export default ErrorMessage;
