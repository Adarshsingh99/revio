# Revio API Testing Guide

This file contains cURL commands to test the Revio API. You can copy and paste these commands into your terminal.

## Prerequisites

- Ensure the backend is running on `http://localhost:8080/api`
- MongoDB should be running
- Use `bash` or equivalent shell

## Step 1: Register a New User

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Developer",
    "email": "john.dev@example.com",
    "password": "SecurePass123"
  }'
```

**Expected Response (201 Created):**
```json
{
  "id": "507f1f77bcf86cd799439011",
  "name": "John Developer",
  "email": "john.dev@example.com",
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "message": "User registered successfully"
}
```

**Save the token for next steps!**

---

## Step 2: Login with Email and Password

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.dev@example.com",
    "password": "SecurePass123"
  }'
```

---

## Step 3: Create a Section

Replace `YOUR_TOKEN_HERE` with the actual token from registration:

```bash
curl -X POST http://localhost:8080/api/sections \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "title": "Data Structures & Algorithms",
    "description": "Master DSA concepts for interviews"
  }'
```

**Save the section ID from response!**

---

## Step 4: Create a Subsection

Replace `SECTION_ID` with the ID from Step 3:

```bash
curl -X POST http://localhost:8080/api/sections/SECTION_ID/subsections \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "title": "Arrays",
    "description": "Array data structure and algorithms"
  }'
```

**Save the subsection ID!**

---

## Step 5: Create Topics

Replace `SECTION_ID` and `SUBSECTION_ID`:

```bash
curl -X POST http://localhost:8080/api/sections/SECTION_ID/subsections/SUBSECTION_ID/topics \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "title": "Binary Search",
    "description": "Learn the binary search algorithm"
  }'
```

Create more topics:

```bash
curl -X POST http://localhost:8080/api/sections/SECTION_ID/subsections/SUBSECTION_ID/topics \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "title": "Merge Sort",
    "description": "Learn the merge sort algorithm"
  }'

curl -X POST http://localhost:8080/api/sections/SECTION_ID/subsections/SUBSECTION_ID/topics \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "title": "Quick Sort",
    "description": "Learn the quick sort algorithm"
  }'
```

**Save at least one topic ID!**

---

## Step 6: Get All Topics in Subsection

```bash
curl -X GET http://localhost:8080/api/sections/SECTION_ID/subsections/SUBSECTION_ID/topics \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

---

## Step 7: ⭐ Mark Topic as Complete (Trigger Revision System)

Replace `TOPIC_ID` with actual topic ID:

```bash
curl -X POST http://localhost:8080/api/sections/SECTION_ID/topics/TOPIC_ID/complete \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

**Important Response:**
```json
{
  "status": "COMPLETED",
  "revisionDates": [
    "2024-01-15",
    "2024-01-16",
    "2024-01-19",
    "2024-01-26"
  ],
  "nextRevision": "2024-01-15",
  "isDueForRevisionToday": true
}
```

This shows:
- Topic is marked COMPLETED
- Revision schedule is created
- Next revision is TODAY!

---

## Step 8: Get Today's Revision Topics

```bash
curl -X GET http://localhost:8080/api/revision/today \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

**Response:**
```json
[
  {
    "id": "TOPIC_ID",
    "title": "Binary Search",
    "subsectionTitle": "Arrays",
    "sectionTitle": "Data Structures & Algorithms",
    "revisionDate": "2024-01-15"
  }
]
```

---

## Step 9: Get Dashboard Statistics

```bash
curl -X GET http://localhost:8080/api/dashboard \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

**Response:**
```json
{
  "totalTopics": 3,
  "completedTopics": 1,
  "pendingTopics": 2,
  "completionPercentage": 33.33,
  "todayRevisionCount": 1,
  "todayRevisions": [...],
  "totalSections": 1,
  "totalSubsections": 1
}
```

---

## Step 10: Complete a Revision

Mark a revision as done (moves to next scheduled date):

```bash
curl -X POST http://localhost:8080/api/sections/SECTION_ID/topics/TOPIC_ID/revise \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

**Response:**
```json
{
  "status": "COMPLETED",
  "nextRevision": "2024-01-16",
  "isDueForRevisionToday": false
}
```

Now the next revision is scheduled for tomorrow!

---

## Additional Useful Endpoints

### Get All Sections
```bash
curl -X GET http://localhost:8080/api/sections \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### Get Specific Section
```bash
curl -X GET http://localhost:8080/api/sections/SECTION_ID \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### Update Section
```bash
curl -X PUT http://localhost:8080/api/sections/SECTION_ID \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "title": "Updated DSA Title",
    "description": "Updated description"
  }'
```

### Delete Section
```bash
curl -X DELETE http://localhost:8080/api/sections/SECTION_ID \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### Get All Subsections
```bash
curl -X GET http://localhost:8080/api/sections/SECTION_ID/subsections \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### Update Topic
```bash
curl -X PUT http://localhost:8080/api/sections/SECTION_ID/subsections/SUBSECTION_ID/topics/TOPIC_ID \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "title": "Updated Topic Title",
    "description": "Updated description"
  }'
```

### Delete Topic
```bash
curl -X DELETE http://localhost:8080/api/sections/SECTION_ID/subsections/SUBSECTION_ID/topics/TOPIC_ID \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

---

## Bash Script for Quick Testing

Create a file named `test-revio.sh`:

```bash
#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

BASE_URL="http://localhost:8080/api"

echo -e "${BLUE}=== Revio API Testing ===${NC}\n"

# Step 1: Register
echo -e "${GREEN}Step 1: Registering user...${NC}"
RESPONSE=$(curl -s -X POST $BASE_URL/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test'$(date +%s)'@example.com",
    "password": "TestPass123"
  }')

TOKEN=$(echo $RESPONSE | grep -o '"token":"[^"]*' | cut -d'"' -f4)
USER_ID=$(echo $RESPONSE | grep -o '"id":"[^"]*' | cut -d'"' -f4)

echo "Token: $TOKEN"
echo "User ID: $USER_ID\n"

# Step 2: Create Section
echo -e "${GREEN}Step 2: Creating section...${NC}"
SECTION=$(curl -s -X POST $BASE_URL/sections \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "title": "Test Section",
    "description": "Testing revision system"
  }')

SECTION_ID=$(echo $SECTION | grep -o '"id":"[^"]*' | head -1 | cut -d'"' -f4)
echo "Section ID: $SECTION_ID\n"

# Step 3: Create Subsection
echo -e "${GREEN}Step 3: Creating subsection...${NC}"
SUBSECTION=$(curl -s -X POST $BASE_URL/sections/$SECTION_ID/subsections \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "title": "Test Subsection",
    "description": "Testing topics"
  }')

SUBSECTION_ID=$(echo $SUBSECTION | grep -o '"id":"[^"]*' | head -1 | cut -d'"' -f4)
echo "Subsection ID: $SUBSECTION_ID\n"

# Step 4: Create Topic
echo -e "${GREEN}Step 4: Creating topic...${NC}"
TOPIC=$(curl -s -X POST $BASE_URL/sections/$SECTION_ID/subsections/$SUBSECTION_ID/topics \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "title": "Test Topic",
    "description": "Learning this topic"
  }')

TOPIC_ID=$(echo $TOPIC | grep -o '"id":"[^"]*' | head -1 | cut -d'"' -f4)
echo "Topic ID: $TOPIC_ID\n"

# Step 5: Complete Topic
echo -e "${GREEN}Step 5: Completing topic and scheduling revisions...${NC}"
COMPLETED=$(curl -s -X POST $BASE_URL/sections/$SECTION_ID/topics/$TOPIC_ID/complete \
  -H "Authorization: Bearer $TOKEN")

echo "Completed Topic Response:"
echo $COMPLETED | grep -o '"revisionDates":\[.*\]' | head -1
echo ""

# Step 6: Get Today's Revisions
echo -e "${GREEN}Step 6: Fetching today's revisions...${NC}"
curl -s -X GET $BASE_URL/revision/today \
  -H "Authorization: Bearer $TOKEN" | grep -o '"title":"[^"]*'
echo ""

# Step 7: Get Dashboard
echo -e "${GREEN}Step 7: Getting dashboard statistics...${NC}"
curl -s -X GET $BASE_URL/dashboard \
  -H "Authorization: Bearer $TOKEN" | grep -o '"[^"]*Percentage":"[^"]*'
echo ""

echo -e "${GREEN}=== Testing Complete ===${NC}"
```

Make it executable and run:
```bash
chmod +x test-revio.sh
./test-revio.sh
```

---

## Error Handling Examples

### Unauthorized (Missing Token)
```bash
curl -X GET http://localhost:8080/api/dashboard

# Response: 403 Forbidden
```

### Invalid Token
```bash
curl -X GET http://localhost:8080/api/dashboard \
  -H "Authorization: Bearer invalid_token"

# Response: 401 Unauthorized
```

### Resource Not Found
```bash
curl -X GET http://localhost:8080/api/sections/invalid_id \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"

# Response: 404 Not Found with error message
```

### Validation Error
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "J",
    "email": "invalid-email",
    "password": "short"
  }'

# Response: 400 Bad Request with validation errors
```

---

## Tips for Testing

1. **Store IDs in variables** for easier reuse
2. **Use Postman** for a GUI-based approach
3. **Check response status codes** (201 = Created, 200 = OK, 400 = Bad Request, 401 = Unauthorized, 404 = Not Found)
4. **Verify database** using MongoDB Compass
5. **Check logs** in terminal where backend is running

---

Happy Testing! 🚀
