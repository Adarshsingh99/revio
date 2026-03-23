# 🚀 REVIO - QUICK START GUIDE

Get Revio up and running in **5 minutes**!

## 📋 Prerequisites

```bash
node -v    # Must be 14+
npm -v     # Must be 6+
```

## ⚡ Quick Start (5 Minutes)

### 1. Backend Setup (2 minutes)

```bash
cd revio-backend

# Start MongoDB (choose one)
# Option A: Local MongoDB
mongod

# Option B: Docker
docker-compose up -d

# Build and run
mvn clean install
mvn spring-boot:run

# Backend running on: http://localhost:8080/api
```

### 2. Frontend Setup (2 minutes)

```bash
cd revio-frontend

# Install dependencies
npm install

# Copy environment
cp .env.example .env

# Start development server
npm start

# Frontend running on: http://localhost:3000
```

### 3. Test the App (1 minute)

```
1. Open http://localhost:3000
2. Click "Sign up"
3. Register with email: test@example.com
4. Create a section: "DSA"
5. Create subsection: "Arrays"
6. Create topic: "Binary Search"
7. Click "✓ Mark Complete"
8. View revision dates!
```

---

## 📁 Project Structure at a Glance

```
revio/
├── revio-backend/          # Spring Boot API
│   ├── src/main/java/      # Java code
│   ├── pom.xml             # Dependencies
│   └── README.md           # Backend docs
│
└── revio-frontend/         # React app
    ├── src/                # React code
    ├── package.json        # Dependencies
    └── README.md           # Frontend docs
```

---

## 🎯 Key Commands

### Backend

```bash
# Build
mvn clean install

# Run
mvn spring-boot:run

# Build JAR
mvn package
java -jar target/revio-backend-1.0.0.jar

# Run tests
mvn test

# View logs
tail -f logs/revio.log
```

### Frontend

```bash
# Install
npm install

# Start dev server
npm start

# Build for production
npm run build

# Run tests
npm test

# Eject (advanced - not reversible)
npm run eject
```

### MongoDB

```bash
# Start locally
mongod

# Or with Docker
docker run -d -p 27017:27017 mongo

# Or with docker-compose
docker-compose up -d
```

---

## 🧪 Test Workflow

### 1. Register User

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John",
    "email": "john@test.com",
    "password": "pass123"
  }'
```

### 2. Get Token (from response)

```
"token": "eyJhbGciOiJIUzUxMiJ9..."
```

### 3. Create Section

```bash
TOKEN="your_token_here"

curl -X POST http://localhost:8080/api/sections \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "DSA",
    "description": "Data Structures"
  }'
```

### 4. Complete Topic (Trigger Revisions)

```bash
curl -X POST http://localhost:8080/api/sections/SECTION_ID/topics/TOPIC_ID/complete \
  -H "Authorization: Bearer $TOKEN"
```

### 5. Get Today's Revisions

```bash
curl -X GET http://localhost:8080/api/revision/today \
  -H "Authorization: Bearer $TOKEN"
```

---

## 📱 Full Workflow in Frontend

```
Login/Register
    ↓
Dashboard (overview)
    ↓
Create Section
    ↓
Create Subsection
    ↓
Create Topics
    ↓
Mark Topic Complete (triggers revisions!)
    ↓
View Today's Revisions
    ↓
Complete Revisions
    ↓
Next Revision Scheduled!
```

---

## 🔧 Troubleshooting

### "Backend not responding"
```bash
# Check if running
curl http://localhost:8080/api/auth/health

# If not, start it
mvn spring-boot:run
```

### "Port already in use"
```bash
# Kill process
lsof -i :8080          # Find PID
kill -9 <PID>          # Kill it

# Or use different port
PORT=8081 mvn spring-boot:run
```

### "Database connection error"
```bash
# Check MongoDB
mongosh

# If not running, start it
mongod

# Or use Docker
docker run -d -p 27017:27017 mongo
```

### "Frontend won't load"
```bash
# Clear cache and reinstall
rm -rf node_modules package-lock.json
npm install
npm start
```

---

## 🔑 Default Credentials

After first run:
- **Email**: test@example.com
- **Password**: pass123

---

## 📊 Frontend Pages

| Page | URL | Purpose |
|------|-----|---------|
| Login | `/login` | User authentication |
| Register | `/register` | New user signup |
| Dashboard | `/dashboard` | Statistics & revisions |
| Sections | `/sections/:id` | View section |
| Subsections | `/sections/:id/subsections/:id` | Manage topics |
| Revisions | `/revisions` | Today's revisions |

---

## 🎨 Default Style

- **Colors**: Blue/Indigo (Tailwind)
- **Icons**: Lucide React
- **Layout**: Responsive grid
- **Fonts**: System fonts

---

## 🐛 Debug Mode

### Enable logging:

Edit `.env`:
```env
REACT_APP_DEBUG=true
REACT_APP_LOG_REQUESTS=true
```

Or in code:
```javascript
localStorage.setItem('debug', 'true');
```

---

## 💡 Pro Tips

1. **Test with Postman**: Import API docs
2. **Use Redux DevTools**: Install browser extension
3. **Check Network tab**: See API calls
4. **React DevTools**: Install browser extension
5. **Enable profiler**: Measure performance

---

## 🚀 Next Steps

1. ✅ Run locally
2. ✅ Test workflow
3. ✅ Read full documentation
4. 🔄 Customize styling
5. 📦 Deploy!

---

## 📚 Documentation

- [Backend README](../revio-backend/README.md)
- [Frontend README](./README.md)
- [API Testing](../revio-backend/API_TESTING.md)
- [Deployment Guide](./DEPLOYMENT.md)
- [Architecture](../revio-backend/ARCHITECTURE.md)

---

## ✅ Checklist

- [ ] Node.js 14+ installed
- [ ] MongoDB running
- [ ] Backend started (port 8080)
- [ ] Frontend started (port 3000)
- [ ] Can access http://localhost:3000
- [ ] Can register/login
- [ ] Can create section
- [ ] Can create subsection
- [ ] Can create topic
- [ ] Can mark topic complete
- [ ] Can see revision schedule
- [ ] Can view today's revisions

---

**Ready to go! Happy learning! 📚**

For detailed info, check the full documentation in each folder.
