import React from 'react';
import { getProgressColor, formatPercentage } from '../utils/helpers';

/**
 * Progress bar component
 */
const ProgressBar = ({ completed, total, showLabel = true, height = 'h-2' }) => {
  const percentage = total > 0 ? (completed / total) * 100 : 0;
  const color = getProgressColor(percentage);

  return (
    <div className="w-full">
      <div className={`w-full bg-gray-200 rounded-full overflow-hidden ${height}`}>
        <div
          className={`${color} transition-all duration-500 ease-out ${height}`}
          style={{ width: `${percentage}%` }}
        />
      </div>
      {showLabel && (
        <p className="text-sm text-gray-600 mt-1">
          {completed}/{total} completed • {formatPercentage(percentage)}
        </p>
      )}
    </div>
  );
};

export default ProgressBar;
