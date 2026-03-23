# ============================================
# REVIO FRONTEND - DOCKERFILE
# ============================================
# Multi-stage build for optimized production image

# Stage 1: Build
# ============================================
FROM node:18-alpine AS builder

# Set working directory
WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm ci --only=production && npm ci --only=development

# Copy application code
COPY . .

# Build the application
RUN npm run build

# Stage 2: Production
# ============================================
FROM node:18-alpine

# Install serve to run the production build
RUN npm install -g serve

# Set working directory
WORKDIR /app

# Copy built application from builder stage
COPY --from=builder /app/build ./build

# Expose port
EXPOSE 3000

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD node -e "require('http').get('http://localhost:3000', (r) => {if (r.statusCode !== 200) throw new Error(r.statusCode)})"

# Start the application
CMD ["serve", "-s", "build", "-l", "3000"]
