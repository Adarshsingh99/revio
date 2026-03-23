# 🚀 REVIO Backend - Smart Study & Revision Tracker

A comprehensive Spring Boot application for managing study topics with an intelligent spaced-repetition revision system.

## 📋 Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Database Schema](#database-schema)
- [Revision System Explained](#revision-system-explained)
- [Troubleshooting](#troubleshooting)

---

## ✨ Features

### Core Features
- ✅ **User Authentication** - Register, login with JWT tokens
- ✅ **Section Management** - Create and organize study sections (e.g., DSA, Web Dev)
- ✅ **Subsection Management** - Organize topics within sections
- ✅ **Topic Management** - Add and track study topics
- ✅ **Smart Revision System** - Intelligent spaced-repetition scheduling
- ✅ **Dashboard** - Comprehensive statistics and progress tracking
- ✅ **Daily Scheduler** - Automated revision task runner

### Technical Features
- Clean layered architecture (Controller → Service → Repository → Model)
- JWT-based stateless authentication
- Global exception handling
- Input validation with Spring Validation
- MongoDB integration with automatic indexing
- CORS support for frontend communication
- Comprehensive logging
- Scheduler for automated tasks

---

## 🛠️ Tech Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| **Framework** | Spring Boot | 3.1.5 |
| **Language** | Java | 17+ |
| **Database** | MongoDB | 4.0+ |
| **Authentication** | JWT (JJWT) | 0.12.3 |
| **Build Tool** | Maven | 3.6+ |
| **Security** | Spring Security | 3.1.5 |
| **Validation** | Jakarta Validation | 3.0+ |

---

## 📦 Prerequisites

Before you begin, ensure you have the following installed:

```bash
# Check versions
java -version          # Should be 17 or higher
mvn -version          # Should be 3.6 or higher
mongod --version      # Should be 4.0 or higher
```

### Required Software
1. **Java 17+** - Download from [oracle.com](https://www.oracle.com/java/technologies/downloads/)
2. **Apache Maven 3.6+** - Download from [maven.apache.org](https://maven.apache.org/download.cgi)
3. **MongoDB 4.0+** - Download from [mongodb.com](https://www.mongodb.com/try/download/community)

### Optional but Recommended
- **MongoDB Compass** - GUI for MongoDB (Download from mongodb.com)
- **Postman** - API testing tool (Download from postman.com)
- **Git** - Version control (Download from git-scm.com)

---

## 💻 Installation & Setup

### Step 1: Clone or Download the Project

```bash
# If using git
git clone <repository-url>
cd revio-backend

# Or extract the provided project folder
```

### Step 2: Start MongoDB

#### Option A: Using MongoDB Community Edition

```bash
# On Windows
# MongoDB should be installed, start it from Services or:
mongod

# On macOS (with Homebrew)
brew services start mongodb-community

# On Linux
sudo systemctl start mongod
```

#### Option B: Using Docker (Recommended)

```bash
# Create and run MongoDB container
docker run -d \
  --name mongodb \
  -p 27017:27017 \
  -e MONGO_INITDB_ROOT_USERNAME=admin \
  -e MONGO_INITDB_ROOT_PASSWORD=password \
  mongo:latest

# Or use docker-compose (if docker-compose.yml is provided)
docker-compose up -d
```

#### Verify MongoDB is Running

```bash
# Open MongoDB Shell
mongosh

# Should show:
# MongoDB shell version v...
# connecting to: mongodb://localhost:27017/?directConnection=true

# Type 'exit' to quit
exit
```

### Step 3: Update Application Configuration

Edit `src/main/resources/application.properties`:

```properties
# MongoDB connection (local)
spring.data.mongodb.uri=mongodb://localhost:27017/revio_db

# Or with authentication (if MongoDB requires login)
spring.data.mongodb.uri=mongodb://admin:password@localhost:27017/revio_db?authSource=admin

# JWT Secret (Change this in production!)
jwt.secret=your-super-secret-jwt-key-change-this-in-production-environment-12345678901234567890
```

### Step 4: Build the Project

```bash
# Navigate to project root
cd revio-backend

# Clean and build with Maven
mvn clean install

# This will:
# - Download all dependencies
# - Compile the code
# - Run tests
# - Package the application
```

---

## 🚀 Running the Application

### Option 1: Run from Maven

```bash
# Navigate to project root
cd revio-backend

# Run the application
mvn spring-boot:run

# Output should show:
# 🚀 Revio Backend Started Successfully!
# 📚 Smart Study and Revision Tracker
# 🌐 Server running on http://localhost:8080/api
```

### Option 2: Run as JAR File

```bash
# Build JAR file
mvn package

# Run the JAR
java -jar target/revio-backend-1.0.0.jar

# Note: Adjust version number if different
```

### Option 3: Run from IDE

1. **IntelliJ IDEA**
   - Open project in IntelliJ
   - Right-click `RevioApplication.java`
   - Click "Run 'RevioApplication'"

2. **Eclipse**
   - Right-click project → Run As → Spring Boot App

3. **Visual Studio Code**
   - Install "Spring Boot Extension Pack"
   - Open `RevioApplication.java`
   - Click "Run" above the main method

### Verify Application is Running

```bash
# In a new terminal
curl http://localhost:8080/api/auth/health

# Should return:
# "Revio API is running!"
```

---

## 📚 API Documentation

### Base URL
```
http://localhost:8080/api
```

### Authentication
All endpoints except `/auth/**` require a JWT token in the Authorization header:

```
Authorization: Bearer <jwt_token>
```

---

### 🔐 Authentication Endpoints

#### Register User
```http
POST /auth/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "SecurePass123"
}
```

**Response (201 Created):**
```json
{
  "id": "507f1f77bcf86cd799439011",
  "name": "John Doe",
  "email": "john@example.com",
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "message": "User registered successfully"
}
```

#### Login User
```http
POST /auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "SecurePass123"
}
```

**Response (200 OK):**
```json
{
  "id": "507f1f77bcf86cd799439011",
  "name": "John Doe",
  "email": "john@example.com",
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "message": "Login successful"
}
```

---

### 📂 Section Endpoints

#### Get All Sections
```http
GET /sections
Authorization: Bearer <token>
```

**Response (200 OK):**
```json
[
  {
    "id": "507f1f77bcf86cd799439012",
    "userId": "507f1f77bcf86cd799439011",
    "title": "Data Structures & Algorithms",
    "description": "Learn DSA fundamentals",
    "subsectionCount": 5,
    "totalTopics": 23,
    "completedTopics": 10,
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  }
]
```

#### Create Section
```http
POST /sections
Content-Type: application/json
Authorization: Bearer <token>

{
  "title": "Web Development",
  "description": "Frontend and Backend technologies"
}
```

**Response (201 Created):**
```json
{
  "id": "507f1f77bcf86cd799439013",
  "userId": "507f1f77bcf86cd799439011",
  "title": "Web Development",
  "description": "Frontend and Backend technologies",
  "subsectionCount": 0,
  "totalTopics": 0,
  "completedTopics": 0,
  "createdAt": "2024-01-15T10:35:00",
  "updatedAt": "2024-01-15T10:35:00"
}
```

#### Update Section
```http
PUT /sections/{sectionId}
Content-Type: application/json
Authorization: Bearer <token>

{
  "title": "Advanced Web Development",
  "description": "Updated description"
}
```

#### Delete Section
```http
DELETE /sections/{sectionId}
Authorization: Bearer <token>
```

---

### 📑 Subsection Endpoints

#### Get All Subsections
```http
GET /sections/{sectionId}/subsections
Authorization: Bearer <token>
```

**Response (200 OK):**
```json
[
  {
    "id": "507f1f77bcf86cd799439014",
    "sectionId": "507f1f77bcf86cd799439012",
    "title": "Arrays",
    "description": "Array data structure",
    "topicCount": 5,
    "completedTopics": 2,
    "pendingTopics": 3,
    "progressPercentage": 40.0,
    "createdAt": "2024-01-15T10:40:00",
    "updatedAt": "2024-01-15T10:40:00"
  }
]
```

#### Create Subsection
```http
POST /sections/{sectionId}/subsections
Content-Type: application/json
Authorization: Bearer <token>

{
  "title": "Linked Lists",
  "description": "Linked list data structure and operations"
}
```

---

### 📖 Topic Endpoints

#### Get All Topics in Subsection
```http
GET /sections/{sectionId}/subsections/{subsectionId}/topics
Authorization: Bearer <token>
```

**Response (200 OK):**
```json
[
  {
    "id": "507f1f77bcf86cd799439015",
    "subsectionId": "507f1f77bcf86cd799439014",
    "title": "Array Creation and Access",
    "description": "Learn how to create and access arrays",
    "status": "PENDING",
    "completedAt": null,
    "revisionDates": null,
    "nextRevision": null,
    "isDueForRevisionToday": false,
    "createdAt": "2024-01-15T10:45:00",
    "updatedAt": "2024-01-15T10:45:00"
  }
]
```

#### Create Topic
```http
POST /sections/{sectionId}/subsections/{subsectionId}/topics
Content-Type: application/json
Authorization: Bearer <token>

{
  "title": "Binary Search",
  "description": "Learn binary search algorithm"
}
```

#### ⭐ Complete Topic & Schedule Revisions (IMPORTANT!)
```http
POST /sections/{sectionId}/topics/{topicId}/complete
Authorization: Bearer <token>
```

**Response (200 OK):**
```json
{
  "id": "507f1f77bcf86cd799439015",
  "subsectionId": "507f1f77bcf86cd799439014",
  "title": "Binary Search",
  "description": "Learn binary search algorithm",
  "status": "COMPLETED",
  "completedAt": "2024-01-15T11:00:00",
  "revisionDates": [
    "2024-01-15",
    "2024-01-16",
    "2024-01-19",
    "2024-01-26"
  ],
  "nextRevision": "2024-01-15",
  "isDueForRevisionToday": true,
  "createdAt": "2024-01-15T10:45:00",
  "updatedAt": "2024-01-15T11:00:00"
}
```

**Revision Schedule Explanation:**
- **Day 1 (Today):** 2024-01-15 - Review immediately after learning
- **Day 2 (Next Day):** 2024-01-16 - Review after 1 day
- **Day 3 (4 days later):** 2024-01-19 - Review after 3 more days
- **Day 4 (11 days later):** 2024-01-26 - Review after 7 more days

This follows the Spaced Repetition algorithm for optimal learning!

#### Complete Revision & Get Next Date
```http
POST /sections/{sectionId}/topics/{topicId}/revise
Authorization: Bearer <token>
```

**Response (200 OK):**
```json
{
  "id": "507f1f77bcf86cd799439015",
  "status": "COMPLETED",
  "nextRevision": "2024-01-16",
  "isDueForRevisionToday": false,
  ...
}
```

---

### 🔄 Revision Endpoints

#### Get Today's Revision Topics (IMPORTANT!)
```http
GET /revision/today
Authorization: Bearer <token>
```

**Response (200 OK):**
```json
[
  {
    "id": "507f1f77bcf86cd799439015",
    "title": "Binary Search",
    "subsectionTitle": "Searching Algorithms",
    "sectionTitle": "Data Structures & Algorithms",
    "revisionDate": "2024-01-15"
  },
  {
    "id": "507f1f77bcf86cd799439016",
    "title": "Merge Sort",
    "subsectionTitle": "Sorting Algorithms",
    "sectionTitle": "Data Structures & Algorithms",
    "revisionDate": "2024-01-15"
  }
]
```

---

### 📊 Dashboard Endpoint

#### Get Dashboard Stats
```http
GET /dashboard
Authorization: Bearer <token>
```

**Response (200 OK):**
```json
{
  "totalTopics": 50,
  "completedTopics": 20,
  "pendingTopics": 30,
  "completionPercentage": 40.0,
  "todayRevisionCount": 5,
  "todayRevisions": [
    {
      "id": "507f1f77bcf86cd799439015",
      "title": "Binary Search",
      "subsectionTitle": "Searching",
      "sectionTitle": "DSA",
      "revisionDate": "2024-01-15"
    }
  ],
  "totalSections": 3,
  "totalSubsections": 12
}
```

---

## 🗄️ Database Schema

### Collections in MongoDB

#### users
```javascript
{
  _id: ObjectId,
  name: String,
  email: String (unique),
  password: String (bcrypt hashed),
  createdAt: Date,
  updatedAt: Date
}
```

#### sections
```javascript
{
  _id: ObjectId,
  userId: ObjectId (ref: users),
  title: String,
  description: String,
  createdAt: Date,
  updatedAt: Date
}
```

#### subsections
```javascript
{
  _id: ObjectId,
  sectionId: ObjectId (ref: sections),
  title: String,
  description: String,
  createdAt: Date,
  updatedAt: Date
}
```

#### topics
```javascript
{
  _id: ObjectId,
  subsectionId: ObjectId (ref: subsections),
  title: String,
  description: String,
  status: String, // PENDING, COMPLETED, FULLY_REVISED
  completedAt: Date,
  revisionDates: Array[Date],
  nextRevision: Date,
  createdAt: Date,
  updatedAt: Date
}
```

---

## 🧠 Revision System Explained

### How It Works

When you mark a topic as **COMPLETED** using `/topics/{topicId}/complete`:

1. **Immediate Revision (Day 1):** Topic is available for revision today
2. **Next Day (Day 2):** System schedules revision 1 day later
3. **Later (Day 3):** System schedules revision 3 days after Day 2
4. **Much Later (Day 4):** System schedules revision 7 days after Day 3

### Spaced Repetition Benefits

This follows the **Spaced Repetition** learning technique:
- Fights the "forgetting curve"
- Optimal recall with minimal effort
- Long-term retention of knowledge
- Research-backed by psychological studies

### Example Timeline

```
Completed on: Jan 15, 2024

Revision Schedule:
├─ Day 1:  Jan 15 (Today)        ✓ Immediate review
├─ Day 2:  Jan 16 (Tomorrow)     ← Review after 1 day
├─ Day 3:  Jan 19 (4 days later) ← Review after 3 more days  
└─ Day 4:  Jan 26 (11 days later)← Review after 7 more days
```

### Using the Dashboard

The **Today's Revisions** list shows all topics where `nextRevision == today`:

1. View `/dashboard` to see today's revision count
2. View `/revision/today` to see detailed list
3. Click a topic to study
4. Use `/topics/{topicId}/revise` when done
5. This moves `nextRevision` to the next scheduled date

---

## 🔍 Testing the API

### Using Postman

1. **Import Collection**
   - Create a new request collection called "Revio"
   - Add the endpoints documented above

2. **Set Environment Variables**
   - Add variable: `base_url` = `http://localhost:8080/api`
   - Add variable: `token` = (leave empty initially)

3. **Register & Login**
   - Send POST to `/auth/register`
   - Copy the `token` from response
   - Set environment variable: `token` = (copied token)

4. **Test Other Endpoints**
   - Now you can test other endpoints with the token

### Using cURL

```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"John","email":"john@test.com","password":"pass123"}'

# Login (copy token from response)
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"john@test.com","password":"pass123"}'

# Get sections with token
curl -X GET http://localhost:8080/api/sections \
  -H "Authorization: Bearer <YOUR_TOKEN_HERE>"

# Get dashboard
curl -X GET http://localhost:8080/api/dashboard \
  -H "Authorization: Bearer <YOUR_TOKEN_HERE>"
```

---

## 🐛 Troubleshooting

### MongoDB Connection Error
```
ERROR: Connection refused to localhost:27017
```

**Solution:**
```bash
# Check if MongoDB is running
mongosh

# If not running, start it:
# Windows: mongod
# macOS: brew services start mongodb-community
# Linux: sudo systemctl start mongod

# Or use Docker:
docker run -d -p 27017:27017 mongo:latest
```

### Port 8080 Already in Use
```
ERROR: Address already in use
```

**Solution:**
```bash
# Find process using port 8080
lsof -i :8080          # macOS/Linux
netstat -ano | findstr :8080  # Windows

# Kill the process or change port in application.properties:
# server.port=8081
```

### JWT Token Errors
```
ERROR: Invalid or expired token
```

**Solution:**
- Ensure token is included in Authorization header as `Bearer <token>`
- Check token hasn't expired (default: 24 hours)
- Generate a new token using `/auth/login`

### Build Fails
```
ERROR: Failed to build
```

**Solution:**
```bash
# Clear Maven cache and rebuild
mvn clean install -U

# If still fails, check Java version
java -version  # Should be 17+
```

---

## 📝 Project Structure

```
revio-backend/
├── src/main/java/com/revio/
│   ├── RevioApplication.java          # Main entry point
│   ├── model/                         # Entity classes
│   │   ├── User.java
│   │   ├── Section.java
│   │   ├── Subsection.java
│   │   └── Topic.java
│   ├── repository/                    # MongoDB repositories
│   │   ├── UserRepository.java
│   │   ├── SectionRepository.java
│   │   ├── SubsectionRepository.java
│   │   └── TopicRepository.java
│   ├── service/                       # Business logic
│   │   ├── UserService.java
│   │   ├── SectionService.java
│   │   ├── SubsectionService.java
│   │   └── TopicService.java
│   ├── controller/                    # REST API endpoints
│   │   ├── AuthController.java
│   │   ├── SectionController.java
│   │   ├── SubsectionController.java
│   │   ├── TopicController.java
│   │   └── DashboardController.java
│   ├── dto/                          # Data Transfer Objects
│   ├── security/                     # JWT & Security
│   ├── exception/                    # Exception handling
│   └── scheduler/                    # Scheduled tasks
├── src/main/resources/
│   └── application.properties         # Configuration
├── pom.xml                           # Maven dependencies
└── README.md                         # This file
```

---

## 🚀 Next Steps

1. **Frontend Integration**
   - Build React frontend (next document)
   - Connect to these API endpoints

2. **Database Setup**
   - Consider data backup strategy
   - Set up MongoDB replication

3. **Deployment**
   - Deploy to cloud (AWS, Heroku, etc.)
   - Configure production JWT secret
   - Set up CI/CD pipeline

4. **Enhancements**
   - Add email notifications
   - Implement user analytics
   - Add topic notes and resources
   - Social sharing features

---

## 📞 Support

For issues or questions:
1. Check the [Troubleshooting](#troubleshooting) section
2. Review API responses (usually indicate the problem)
3. Check Spring Boot logs in console
4. Verify MongoDB is running

---

## 📄 License

This project is provided as-is for educational purposes.

---

**Happy Learning with Revio! 🎓**
