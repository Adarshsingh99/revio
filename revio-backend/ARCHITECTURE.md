# Revio Backend - Quick Start Guide

## 🚀 Get Started in 5 Minutes

### Prerequisites Check
```bash
java -version          # Must be 17+
mvn -version          # Must be 3.6+
mongod --version      # Must be 4.0+
```

### Step 1: Start MongoDB (Choose One)

**Option A: Using Local MongoDB**
```bash
# macOS
brew services start mongodb-community

# Linux
sudo systemctl start mongod

# Windows
mongod
```

**Option B: Using Docker (Recommended)**
```bash
# Using docker-compose (easiest)
docker-compose up -d

# This starts:
# - MongoDB on port 27017
# - Mongo Express UI on port 8081
```

### Step 2: Build & Run Backend

```bash
# Build
mvn clean install

# Run
mvn spring-boot:run

# Or build JAR and run
mvn package
java -jar target/revio-backend-1.0.0.jar
```

### Step 3: Verify It's Running

```bash
curl http://localhost:8080/api/auth/health

# Expected response: "Revio API is running!"
```

---

## 📁 Project Structure Overview

```
revio-backend/
│
├── src/main/java/com/revio/
│   │
│   ├── RevioApplication.java          # ⭐ Main entry point
│   │                                   # Start here!
│   │
│   ├── model/                         # 📦 Database entities
│   │   ├── User.java                  # User document
│   │   ├── Section.java               # Section document
│   │   ├── Subsection.java            # Subsection document
│   │   └── Topic.java                 # Topic document (with revision system)
│   │
│   ├── repository/                    # 🗄️ MongoDB data access
│   │   ├── UserRepository.java
│   │   ├── SectionRepository.java
│   │   ├── SubsectionRepository.java
│   │   └── TopicRepository.java
│   │   # These provide database queries
│   │
│   ├── service/                       # 🧠 Business logic layer
│   │   ├── UserService.java           # Auth logic
│   │   ├── SectionService.java        # Section operations
│   │   ├── SubsectionService.java     # Subsection operations
│   │   └── TopicService.java          # ⭐ Core revision system logic
│   │   # This is where the magic happens!
│   │
│   ├── controller/                    # 🌐 REST API endpoints
│   │   ├── AuthController.java        # /auth/register, /auth/login
│   │   ├── SectionController.java     # /sections/*
│   │   ├── SubsectionController.java  # /sections/{id}/subsections/*
│   │   ├── TopicController.java       # /topics/*, /revision/today
│   │   └── DashboardController.java   # /dashboard
│   │   # Expose services as HTTP endpoints
│   │
│   ├── dto/                          # 📨 Data Transfer Objects
│   │   ├── AuthDto.java              # Auth request/response
│   │   ├── LoginRequest.java
│   │   ├── AuthResponse.java
│   │   ├── SectionRequest.java
│   │   ├── TopicRequest.java
│   │   ├── TopicResponse.java
│   │   ├── DashboardResponse.java
│   │   └── ...
│   │   # Define JSON request/response formats
│   │
│   ├── security/                     # 🔐 JWT & Authentication
│   │   ├── JwtTokenProvider.java      # Generate & validate JWT tokens
│   │   ├── JwtAuthenticationFilter.java # Intercept requests, validate tokens
│   │   └── SecurityConfig.java        # Spring Security configuration
│   │   # Handles user authentication
│   │
│   ├── exception/                    # ⚠️ Error handling
│   │   ├── Exceptions.java           # Custom exception classes
│   │   ├── ErrorResponse.java        # Standard error format
│   │   └── GlobalExceptionHandler.java # Catch all exceptions, return JSON
│   │   # Consistent error responses
│   │
│   └── scheduler/                    # ⏰ Scheduled tasks
│       └── RevisionScheduler.java    # Daily revision system tasks
│       # Runs at midnight, logs statistics
│
├── src/main/resources/
│   └── application.properties         # 🔧 Configuration
│                                      # Database URL, JWT secret, ports
│
├── pom.xml                           # 📦 Maven dependencies
├── docker-compose.yml                # 🐳 Docker MongoDB setup
├── README.md                         # 📖 Full documentation
├── API_TESTING.md                    # 🧪 cURL examples
├── ARCHITECTURE.md                   # 📐 This file
└── .gitignore                        # 🚫 Git ignore rules

```

---

## 🏗️ Architecture Overview

### Layered Architecture

```
┌─────────────────────────────────────────┐
│         Client (React Frontend)          │ HTTP Request
├─────────────────────────────────────────┤
│  Controller Layer (REST Endpoints)      │
│  - AuthController, SectionController... │
├─────────────────────────────────────────┤
│  Service Layer (Business Logic)         │
│  - UserService, TopicService...         │
├─────────────────────────────────────────┤
│  Repository Layer (Data Access)         │
│  - UserRepository, TopicRepository...   │
├─────────────────────────────────────────┤
│        MongoDB Database                 │
│  - users, sections, topics collection   │
└─────────────────────────────────────────┘
```

### Request Flow Example

```
1. User clicks "Complete Topic" in frontend
   ↓
2. Frontend sends HTTP POST request:
   POST /sections/{id}/topics/{id}/complete
   Header: Authorization: Bearer <JWT_TOKEN>
   ↓
3. JwtAuthenticationFilter intercepts request
   - Validates JWT token
   - Sets user context
   ↓
4. TopicController.completeTopic() receives request
   ↓
5. TopicService.completeTopicAndScheduleRevision()
   - Fetches topic from database
   - Generates revision dates using spaced repetition
   - Updates nextRevision field
   - Saves to MongoDB
   ↓
6. Response returned as JSON:
   {
     "id": "...",
     "status": "COMPLETED",
     "revisionDates": ["2024-01-15", "2024-01-16", ...],
     "nextRevision": "2024-01-15"
   }
   ↓
7. Frontend receives response and updates UI
```

---

## ⭐ Key Components Explained

### 1. JWT Authentication (Security)

**File:** `security/JwtTokenProvider.java`

```
User Login → Generate JWT Token → Include in every request
  ↓
JwtAuthenticationFilter intercepts request
  ↓
Extract token from Authorization header
  ↓
Validate signature and expiration
  ↓
Set user context (userId) in Spring Security
  ↓
Allow request to proceed
```

### 2. Smart Revision System (Core Feature)

**File:** `service/TopicService.java`

```
Mark Topic Complete
  ↓
Generate 4 Revision Dates:
  - Day 1: Today
  - Day 2: +1 day
  - Day 3: +4 days total
  - Day 4: +11 days total
  ↓
Set nextRevision = Today
  ↓
Topic appears in /revision/today
  ↓
User studies and clicks "Complete Revision"
  ↓
nextRevision moves to next scheduled date
```

### 3. Data Persistence (MongoDB)

**Files:** `repository/*.java`

MongoDB Collections:
- **users** - User accounts with hashed passwords
- **sections** - Study sections (DSA, Web Dev, etc.)
- **subsections** - Topics grouped (Arrays, Graphs, etc.)
- **topics** - Individual study topics with revision dates

### 4. REST API Endpoints

**Files:** `controller/*.java`

```
Authentication:
POST   /auth/register                → Register new user
POST   /auth/login                   → Login and get JWT token

Sections:
GET    /sections                     → Get all sections
POST   /sections                     → Create section
PUT    /sections/{id}                → Update section
DELETE /sections/{id}                → Delete section

Subsections:
GET    /sections/{id}/subsections    → Get subsections
POST   /sections/{id}/subsections    → Create subsection

Topics:
GET    /sections/{id}/subsections/{id}/topics → Get topics
POST   /sections/{id}/subsections/{id}/topics → Create topic
POST   /sections/{id}/topics/{id}/complete    → Complete topic (⭐ TRIGGERS REVISIONS)
POST   /sections/{id}/topics/{id}/revise      → Complete revision

Revisions:
GET    /revision/today               → Get today's revisions (⭐ CORE)

Dashboard:
GET    /dashboard                    → Get statistics
```

---

## 🔄 Data Flow: Marking a Topic Complete

```
Frontend Action: User clicks "Mark Complete"
    ↓
API Call: POST /sections/SEC123/topics/TOP456/complete
    ↓
Authorization: Check JWT token is valid ✓
    ↓
TopicService.completeTopicAndScheduleRevision()
    {
        // Find topic
        topic = topicRepository.findById("TOP456")
        
        // Mark as completed
        topic.setStatus("COMPLETED")
        topic.setCompletedAt(LocalDateTime.now())
        
        // Generate revision schedule (spaced repetition)
        today = LocalDate.now()  // 2024-01-15
        revisionDates = [
            today,              // 2024-01-15
            today.plusDays(1),  // 2024-01-16
            today.plusDays(4),  // 2024-01-19
            today.plusDays(11)  // 2024-01-26
        ]
        
        topic.setRevisionDates(revisionDates)
        topic.setNextRevision(today)  // First revision is today!
        
        // Save to MongoDB
        topicRepository.save(topic)
    }
    ↓
Response: TopicResponse with revision schedule
    {
        "status": "COMPLETED",
        "revisionDates": ["2024-01-15", "2024-01-16", "2024-01-19", "2024-01-26"],
        "nextRevision": "2024-01-15",
        "isDueForRevisionToday": true
    }
    ↓
Frontend: Updates UI, topic now in "Today's Revisions"
```

---

## 🧪 Testing Endpoints

All examples below use these IDs (update with real ones):

```bash
TOKEN="eyJhbGciOiJIUzUxMiJ9..."
SECTION_ID="507f1f77bcf86cd799439012"
SUBSECTION_ID="507f1f77bcf86cd799439013"
TOPIC_ID="507f1f77bcf86cd799439014"
```

### Test Complete Revision Flow

```bash
# 1. Create a topic
curl -X POST http://localhost:8080/api/sections/$SECTION_ID/subsections/$SUBSECTION_ID/topics \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"title":"Binary Search","description":"Learn it"}'

# Save TOPIC_ID from response

# 2. Complete it (trigger revisions)
curl -X POST http://localhost:8080/api/sections/$SECTION_ID/topics/$TOPIC_ID/complete \
  -H "Authorization: Bearer $TOKEN"

# 3. Check today's revisions
curl -X GET http://localhost:8080/api/revision/today \
  -H "Authorization: Bearer $TOKEN"

# Should show the topic!

# 4. Complete the revision
curl -X POST http://localhost:8080/api/sections/$SECTION_ID/topics/$TOPIC_ID/revise \
  -H "Authorization: Bearer $TOKEN"

# nextRevision should now be tomorrow!
```

---

## 🐛 Debugging Tips

### Enable Debug Logging

Edit `application.properties`:
```properties
logging.level.com.revio=DEBUG
logging.level.org.springframework.security=DEBUG
logging.level.org.mongodb=DEBUG
```

### Check MongoDB Data

```bash
# Connect to MongoDB
mongosh

# Switch to database
use revio_db

# View collections
show collections

# Query data
db.topics.find()
db.topics.find({ status: "COMPLETED" })
db.topics.find({ nextRevision: ISODate("2024-01-15") })
```

### Common Errors & Solutions

| Error | Cause | Solution |
|-------|-------|----------|
| Connection refused | MongoDB not running | Start MongoDB with `mongod` or `docker-compose up` |
| Unauthorized | Invalid JWT token | Register/login to get new token |
| Not found | Wrong ID | Copy ID correctly from API response |
| Build error | Java version | `java -version` should be 17+ |
| Port in use | Another app on 8080 | Change port in `application.properties` |

---

## 📖 Important Files to Study

### For Understanding the System

1. **RevioApplication.java** - Entry point
   - Shows what gets enabled (@EnableScheduling)

2. **Topic.java** - Data model
   - Fields like `revisionDates`, `nextRevision`

3. **TopicService.java** - Core logic
   - `completeTopicAndScheduleRevision()` method
   - Implements spaced repetition algorithm

4. **TopicController.java** - API endpoints
   - Shows how requests are routed to service

5. **JwtTokenProvider.java** - Security
   - Shows how tokens are created and validated

### For Extending the System

1. Add new fields to models
2. Create new repository methods
3. Add service logic
4. Create controller endpoints
5. Update DTOs

---

## 🚀 Next Steps

1. **Test the API** - Use `API_TESTING.md` commands
2. **Study the code** - Start with `RevioApplication.java`
3. **Review the schema** - Look at entity classes
4. **Understand the revision system** - Study `TopicService.java`
5. **Build the frontend** - Connect React to these APIs

---

## 📞 Quick Reference

| File | Purpose |
|------|---------|
| `RevioApplication.java` | Spring Boot entry point |
| `Topic.java` | Revision scheduling model |
| `TopicService.java` | Revision logic |
| `JwtTokenProvider.java` | Token generation |
| `GlobalExceptionHandler.java` | Error handling |
| `application.properties` | Configuration |

---

## ✅ Checklist

- [ ] Java 17+ installed
- [ ] Maven 3.6+ installed
- [ ] MongoDB running (local or Docker)
- [ ] Backend starts without errors
- [ ] Health endpoint responds
- [ ] Can register a user
- [ ] Can create sections/topics
- [ ] Can complete a topic (revision dates generated)
- [ ] Can see today's revisions

---

Happy Coding! 🚀

For detailed documentation, see `README.md`.
For API testing examples, see `API_TESTING.md`.
