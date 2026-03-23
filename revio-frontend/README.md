# 🚀 Revio Frontend - React Application

A modern, responsive React application for the **Revio Smart Study & Revision Tracker**. Features a clean UI with Tailwind CSS, real-time API integration, and intelligent spaced-repetition tracking.

## 📋 Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [Project Structure](#project-structure)
- [Usage Guide](#usage-guide)
- [API Integration](#api-integration)
- [Troubleshooting](#troubleshooting)
- [Deployment](#deployment)

---

## ✨ Features

### User Experience
- ✅ **Responsive Design** - Works on mobile, tablet, and desktop
- ✅ **Modern UI** - Built with Tailwind CSS for stunning visuals
- ✅ **Real-time Updates** - Seamless API integration with Axios
- ✅ **Toast Notifications** - User feedback for all actions
- ✅ **Loading States** - Spinners and skeleton screens
- ✅ **Error Handling** - User-friendly error messages

### Study Management
- ✅ **Dashboard** - Overview of progress and today's revisions
- ✅ **Section Management** - Organize study material by sections
- ✅ **Subsection Management** - Further organize into subsections
- ✅ **Topic Tracking** - Create and track individual topics
- ✅ **Progress Bars** - Visual progress tracking at all levels
- ✅ **Statistics** - Comprehensive learning metrics

### Revision System
- ✅ **Smart Scheduling** - Spaced-repetition based revision dates
- ✅ **Today's Revisions** - Quick access to topics due today
- ✅ **Revision Tracking** - Visual progress during revision sessions
- ✅ **Schedule Visualization** - See all revision dates at a glance
- ✅ **Session Progress** - Track completion during revision

### Authentication
- ✅ **JWT Tokens** - Secure API authentication
- ✅ **Auto Login** - Remember login state
- ✅ **Protected Routes** - Secure page access
- ✅ **Logout** - Clean session termination

---

## 🛠️ Tech Stack

| Layer | Technology | Version |
|-------|-----------|---------|
| **Framework** | React | 18.2.0 |
| **Routing** | React Router | 6.19.0 |
| **HTTP Client** | Axios | 1.6.2 |
| **Styling** | Tailwind CSS | 3.3.5 |
| **Icons** | Lucide React | 0.292.0 |
| **Notifications** | React Hot Toast | 2.4.1 |
| **Date Handling** | date-fns | 2.30.0 |
| **Build Tool** | Create React App | 5.0.1 |

---

## 📦 Prerequisites

Ensure you have the following installed:

```bash
node --version      # Should be v14+ (v16+ recommended)
npm --version       # Should be v6+
```

### System Requirements
- **Node.js**: 14.0 or higher (16+ recommended)
- **npm**: 6.0 or higher
- **Memory**: 2GB minimum
- **Disk Space**: 500MB for node_modules

### Backend Requirement
The **Revio Backend** must be running on `http://localhost:8080/api`

See [Backend Setup Guide](../revio-backend/README.md) for instructions.

---

## 💻 Installation

### Step 1: Prerequisites Check

```bash
# Verify Node.js and npm
node -v     # v16.13.0 or higher
npm -v      # 7.20.0 or higher

# Update npm if needed
npm install -g npm@latest
```

### Step 2: Clone or Download Project

```bash
# If using git
git clone <repository-url>
cd revio-frontend

# Or extract the project folder
```

### Step 3: Install Dependencies

```bash
# Install all required packages
npm install

# This installs:
# - React and React DOM
# - React Router for navigation
# - Axios for API calls
# - Tailwind CSS for styling
# - Lucide React for icons
# - React Hot Toast for notifications
# - date-fns for date formatting
```

**Expected output:**
```
added 1234 packages in 45s
```

### Step 4: Verify Installation

```bash
# Check if all dependencies installed correctly
npm list --depth=0

# Should show:
# ├── axios@1.6.2
# ├── date-fns@2.30.0
# ├── react@18.2.0
# ├── react-dom@18.2.0
# ├── react-hot-toast@2.4.1
# ├── react-router-dom@6.19.0
# ├── tailwindcss@3.3.5
# └── ...
```

---

## ⚙️ Configuration

### Step 1: Create .env File

Create a `.env` file in the project root:

```bash
cp .env.example .env
```

Or manually create `.env`:

```env
# Backend API Configuration
REACT_APP_API_URL=http://localhost:8080/api

# Environment
REACT_APP_ENV=development

# Feature Flags (optional)
REACT_APP_DEBUG=false
```

### Step 2: Environment Variables Explained

| Variable | Description | Default |
|----------|-----------|---------|
| `REACT_APP_API_URL` | Backend API base URL | `http://localhost:8080/api` |
| `REACT_APP_ENV` | Environment (development/production) | `development` |
| `REACT_APP_DEBUG` | Enable debug logging | `false` |

### Step 3: Update API Service (if needed)

Edit `src/services/api.js`:

```javascript
const api = axios.create({
  baseURL: process.env.REACT_APP_API_URL || 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
});
```

---

## 🚀 Running the Application

### Option 1: Development Server

```bash
# Start development server with hot reload
npm start

# This:
# - Starts on http://localhost:3000
# - Opens in default browser
# - Enables hot module reloading
# - Shows build errors in console
```

**Expected output:**
```
Compiled successfully!

You can now view revio-frontend in the browser.

  Local:            http://localhost:3000
  On Your Network:  http://192.168.x.x:3000

Note that the development build is not optimized.
To create a production build, use npm run build.
```

### Option 2: Build for Production

```bash
# Create optimized production build
npm run build

# This:
# - Minifies code
# - Optimizes assets
# - Creates ./build folder
# - Ready for deployment
```

### Option 3: Test the Application

```bash
# Run tests (if any)
npm test

# Watch mode
npm test -- --watch
```

---

## 📁 Project Structure

```
revio-frontend/
├── public/
│   └── index.html                 # Main HTML file
│
├── src/
│   ├── pages/                     # Page components
│   │   ├── DashboardPage.jsx      # Main dashboard
│   │   ├── SectionPage.jsx        # Section view
│   │   ├── SubsectionPage.jsx     # Topics management
│   │   ├── RevisionPage.jsx       # Today's revisions
│   │   ├── LoginPage.jsx          # Login form
│   │   └── RegisterPage.jsx       # Registration form
│   │
│   ├── components/                # Reusable components
│   │   ├── Navbar.jsx             # Navigation bar
│   │   ├── ProtectedRoute.jsx     # Route protection
│   │   ├── ProgressBar.jsx        # Progress visualization
│   │   ├── Spinner.jsx            # Loading spinner
│   │   └── ErrorMessage.jsx       # Error display
│   │
│   ├── services/                  # API services
│   │   ├── api.js                 # Axios configuration
│   │   ├── authService.js         # Auth API calls
│   │   ├── sectionService.js      # Section API calls
│   │   ├── topicService.js        # Topic API calls
│   │   └── dashboardService.js    # Dashboard API calls
│   │
│   ├── hooks/                     # Custom React hooks
│   │   └── useAuth.js             # Auth state management
│   │
│   ├── utils/                     # Utility functions
│   │   └── helpers.js             # Helper functions
│   │
│   ├── App.jsx                    # Main app component
│   ├── index.jsx                  # Entry point
│   └── index.css                  # Global styles
│
├── .env.example                   # Example environment variables
├── .env                           # Your environment variables (git ignored)
├── .gitignore                     # Git ignore rules
├── package.json                   # Dependencies and scripts
├── tailwind.config.js             # Tailwind configuration
├── postcss.config.js              # PostCSS configuration
└── README.md                      # This file
```

---

## 📖 Usage Guide

### 1. Getting Started

#### First Time User Flow:
```
1. Visit http://localhost:3000
   ↓
2. Click "Sign up" on login page
   ↓
3. Enter name, email, password
   ↓
4. Account created → Dashboard
   ↓
5. Create first section (e.g., "Data Structures")
   ↓
6. Add subsections (e.g., "Arrays", "Linked Lists")
   ↓
7. Add topics to subsections
   ↓
8. Mark topics as complete
   ↓
9. View today's revisions
```

### 2. Dashboard Overview

**Main Statistics:**
- Total Topics: All topics you've created
- Completed Topics: Topics marked as done
- Pending Topics: Topics not yet started
- Today's Revisions: Topics due for revision today

**Quick Actions:**
- View today's revisions
- Create new section
- Refresh data
- Navigate to all sections

### 3. Managing Sections

**Create a Section:**
1. Dashboard → "+ New Section"
2. Enter section title and description
3. Click "Create"

**View Section:**
1. Click on a section from Dashboard
2. See subsections and their progress
3. Create new subsections

**Delete Section:**
1. Navigate to Section page
2. Hover over subsection
3. Click delete icon

### 4. Managing Subsections

**Create a Subsection:**
1. In Section page → "+ New Subsection"
2. Enter title and description
3. Click "Create"

**View Subsection:**
1. Click on a subsection
2. See all topics and their status
3. Create new topics

### 5. Managing Topics

**Create a Topic:**
1. In Subsection page → "+ New Topic"
2. Enter title and description
3. Click "Create"

**Mark Topic Complete (⭐ IMPORTANT):**
1. In Subsection page, find topic
2. Click "✓ Mark Complete"
3. Topic is marked COMPLETED
4. **Revision schedule is automatically created!**

**View Revision Schedule:**
1. Topic is marked complete
2. Scroll to see revision dates
3. Revision dates show:
   - Day 1: Today
   - Day 2: +1 day
   - Day 3: +4 days
   - Day 4: +11 days

### 6. Using the Revision System

**View Today's Revisions:**
1. Dashboard → "Today's Revision List"
2. Or Navbar → "📋 Revisions"

**Complete a Revision:**
1. Open Revision page
2. Topics due today are listed
3. Read/study the topic
4. Click "✓ Mark Revision Complete"
5. Next revision date is scheduled

**Revision Progress:**
- Progress bar shows session completion
- Example: 2/5 means 2 of 5 revisions done
- Completion message appears when all done

---

## 🔌 API Integration

### Services Overview

All API calls are handled through service files:

#### Authentication Service
```javascript
// src/services/authService.js
authService.login(email, password)
authService.register(name, email, password)
authService.logout()
```

#### Section Service
```javascript
// src/services/sectionService.js
sectionService.getSections()
sectionService.getSectionById(sectionId)
sectionService.createSection(sectionData)
sectionService.updateSection(sectionId, sectionData)
sectionService.deleteSection(sectionId)

// Subsections
sectionService.getSubsections(sectionId)
sectionService.getSubsectionById(sectionId, subsectionId)
sectionService.createSubsection(sectionId, subsectionData)
sectionService.deleteSubsection(sectionId, subsectionId)
```

#### Topic Service
```javascript
// src/services/topicService.js
topicService.getTopics(sectionId, subsectionId)
topicService.createTopic(sectionId, subsectionId, topicData)
topicService.deleteTopic(sectionId, subsectionId, topicId)

// Revision System (⭐ CORE)
topicService.completeTopic(sectionId, topicId)
topicService.completeRevision(sectionId, topicId)
topicService.getTodayRevisions()
```

#### Dashboard Service
```javascript
// src/services/dashboardService.js
dashboardService.getDashboard()
```

### API Interceptors

The Axios instance automatically:
1. Adds JWT token to every request header
2. Handles 401 errors (token expired)
3. Redirects to login if unauthorized

---

## 🐛 Troubleshooting

### Common Issues

#### 1. Port 3000 Already in Use
```bash
# macOS/Linux
lsof -i :3000
kill -9 <PID>

# Windows
netstat -ano | findstr :3000
taskkill /PID <PID> /F

# Or use different port
PORT=3001 npm start
```

#### 2. Backend Not Responding
```
Error: Failed to load dashboard
```

**Solution:**
- Check if backend is running: `http://localhost:8080/api/auth/health`
- Verify REACT_APP_API_URL in .env
- Check CORS configuration in backend

#### 3. Authentication Issues
```
Error: Unauthorized (401)
```

**Solution:**
- Clear localStorage: `localStorage.clear()`
- Logout and login again
- Check token expiration (24 hours default)

#### 4. Blank Page on Load
```
Nothing visible on http://localhost:3000
```

**Solution:**
```bash
# Clear cache and reinstall
rm -rf node_modules package-lock.json
npm install
npm start
```

#### 5. Build Fails
```
npm ERR! code ERESOLVE
```

**Solution:**
```bash
# Force legacy dependency resolution
npm install --legacy-peer-deps

# Or update npm
npm install -g npm@latest
```

---

## 🌐 Deployment

### Before Deployment Checklist

- [ ] Backend is deployed and running
- [ ] .env variables are set correctly
- [ ] All pages tested in production mode
- [ ] Build completes without errors
- [ ] No console errors in browser devtools

### Build for Production

```bash
# Create optimized build
npm run build

# Output:
# The build folder is ready to be deployed.
# Size:
#   119.46 kb    build/static/js/main.xxxxx.js
#   38.12 kb    build/static/css/main.xxxxx.css
```

### Deploy to Vercel (Recommended)

#### Option 1: Using Vercel CLI

```bash
# Install Vercel CLI
npm i -g vercel

# Login to Vercel
vercel login

# Deploy
vercel

# Follow prompts and set environment variables
```

#### Option 2: Using GitHub Integration

1. Push code to GitHub
2. Connect repository to Vercel
3. Set `REACT_APP_API_URL` in Environment Variables
4. Deploy

### Deploy to Netlify

```bash
# Install Netlify CLI
npm i -g netlify-cli

# Login
netlify login

# Deploy
netlify deploy --prod

# Or use Netlify UI and connect GitHub repository
```

### Deploy to AWS Amplify

```bash
# Install Amplify CLI
npm i -g @aws-amplify/cli

# Configure
amplify configure

# Initialize and deploy
amplify init
amplify publish
```

### Environment Variables for Production

```env
# .env.production
REACT_APP_API_URL=https://api.revio.com
REACT_APP_ENV=production
REACT_APP_DEBUG=false
```

---

## 📱 Browser Support

- ✅ Chrome/Edge (latest)
- ✅ Firefox (latest)
- ✅ Safari (latest)
- ✅ Mobile browsers (iOS Safari, Chrome Mobile)

---

## 🎨 Customization

### Change Theme Colors

Edit `tailwind.config.js`:

```javascript
module.exports = {
  theme: {
    extend: {
      colors: {
        primary: '#3B82F6',
        secondary: '#10B981',
        // Add more colors
      },
    },
  },
};
```

### Update Logo/Branding

Edit components and change:
- Logo in `LoginPage.jsx` and `RegisterPage.jsx`
- Title in `index.html`
- Favicon in `public/`

---

## 📝 Scripts Reference

```bash
npm start              # Start development server
npm run build          # Build for production
npm test              # Run tests
npm run eject         # Eject from Create React App (⚠️ irreversible)
```

---

## 🤝 Contributing

To contribute improvements:

1. Create a feature branch
2. Make your changes
3. Test thoroughly
4. Submit a pull request

---

## 📄 License

This project is provided as-is for educational purposes.

---

## 📞 Support & Resources

### Documentation
- [Backend API Documentation](../revio-backend/README.md)
- [API Testing Guide](../revio-backend/API_TESTING.md)
- [Architecture Overview](../revio-backend/ARCHITECTURE.md)

### Learning Resources
- [React Documentation](https://react.dev)
- [Tailwind CSS](https://tailwindcss.com)
- [Axios](https://axios-http.com)
- [React Router](https://reactrouter.com)

### Troubleshooting Resources
- [React Errors](https://react.dev/reference/react/memo#my-custom-function)
- [Tailwind Issues](https://tailwindcss.com/docs/installation)
- [Axios Guide](https://axios-http.com/docs/intro)

---

## ✅ Quick Reference Checklist

- [ ] Node.js 16+ installed
- [ ] npm dependencies installed (`npm install`)
- [ ] .env file created with REACT_APP_API_URL
- [ ] Backend running on http://localhost:8080/api
- [ ] Development server started (`npm start`)
- [ ] Can access http://localhost:3000
- [ ] Can register/login
- [ ] Can create sections and topics
- [ ] Can mark topics as complete
- [ ] Can view today's revisions

---

**Happy Learning with Revio! 📚✨**

For issues or questions, check the troubleshooting section or contact support.
