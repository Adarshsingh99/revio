# 🚀 REVIO - DEPLOYMENT GUIDE

Complete guide to deploy Revio (Backend + Frontend) to production.

## 📋 Table of Contents

- [Pre-Deployment Checklist](#pre-deployment-checklist)
- [Deployment Options](#deployment-options)
- [Option 1: Vercel (Recommended)](#option-1-vercel-recommended)
- [Option 2: Heroku](#option-2-heroku)
- [Option 3: Docker + VPS](#option-3-docker--vps)
- [Option 4: AWS](#option-4-aws)
- [Option 5: DigitalOcean](#option-5-digitalocean)
- [Environment Variables](#environment-variables)
- [Post-Deployment](#post-deployment)
- [Troubleshooting](#troubleshooting)

---

## ✅ Pre-Deployment Checklist

### Code Quality
- [ ] All tests passing
- [ ] No console errors or warnings
- [ ] No API keys in code (use .env)
- [ ] All features tested locally
- [ ] Code committed to git

### Backend (.env)
- [ ] JWT_SECRET changed to secure value
- [ ] MONGODB_URI points to production database
- [ ] All environment variables set
- [ ] CORS configured for frontend domain

### Frontend (.env)
- [ ] REACT_APP_API_URL points to production backend
- [ ] REACT_APP_ENV set to "production"
- [ ] All API calls working
- [ ] No hardcoded localhost URLs

### Database
- [ ] MongoDB backup created
- [ ] Database user credentials secure
- [ ] Indexes created for performance
- [ ] Backup strategy in place

### Security
- [ ] HTTPS enabled
- [ ] CORS properly configured
- [ ] Sensitive data not exposed
- [ ] Rate limiting configured
- [ ] Authentication tested

---

## 🎯 Deployment Options

| Option | Ease | Cost | Performance | Best For |
|--------|------|------|-------------|----------|
| **Vercel** | ⭐⭐⭐⭐⭐ | Free-$20 | Excellent | React + Serverless |
| **Heroku** | ⭐⭐⭐⭐ | $7-50 | Good | Simple apps |
| **Docker + VPS** | ⭐⭐⭐ | $5-20 | Excellent | Full control |
| **AWS** | ⭐⭐ | $5-100+ | Excellent | Enterprise |
| **DigitalOcean** | ⭐⭐⭐ | $5-40 | Excellent | Developers |

---

## 🌐 Option 1: Vercel (Recommended)

### Why Vercel?
- ✅ Optimized for React
- ✅ Free tier available
- ✅ Automatic deployments from Git
- ✅ Built-in analytics
- ✅ Zero-config

### Step 1: Create Vercel Account

```bash
# Visit https://vercel.com/signup
# Sign up with GitHub account
```

### Step 2: Deploy Frontend

#### Method A: Via GitHub (Recommended)

1. Push frontend code to GitHub
2. Go to [Vercel Dashboard](https://vercel.com/dashboard)
3. Click "Add New..." → "Project"
4. Select your GitHub repository
5. Configure:
   - **Framework**: Next.js (or React)
   - **Root Directory**: `revio-frontend`
   - **Build Command**: `npm run build`
   - **Output Directory**: `build`

6. Add Environment Variables:
   ```
   REACT_APP_API_URL: https://your-backend-url.com/api
   REACT_APP_ENV: production
   ```

7. Click "Deploy"

#### Method B: Via CLI

```bash
# Install Vercel CLI
npm i -g vercel

# Login
vercel login

# Deploy
cd revio-frontend
vercel --prod

# Follow prompts and set environment variables
```

### Step 3: Configure Custom Domain

1. Go to Project Settings
2. Domains → Add Domain
3. Point DNS to Vercel
4. SSL automatically enabled

### Step 4: Redeploy on Backend Changes

```bash
# Backend URL changes? Redeploy frontend:
vercel --prod

# Or push to GitHub for automatic deployment
```

---

## 🔴 Option 2: Heroku

### Backend Deployment

#### Step 1: Prepare for Heroku

```bash
cd revio-backend

# Create Heroku app
heroku create revio-api

# Add MongoDB URI
heroku config:set SPRING_DATA_MONGODB_URI=mongodb+srv://user:pass@cluster.mongodb.net/revio_db

# Add other environment variables
heroku config:set JWT_SECRET=your-secure-secret-key
heroku config:set JWT_EXPIRATION=86400000
```

#### Step 2: Deploy

```bash
# Push to Heroku
git push heroku main

# View logs
heroku logs --tail
```

#### Step 3: Get Backend URL

```bash
# Backend will be at:
https://revio-api.herokuapp.com/api
```

### Frontend Deployment

```bash
# Create app
heroku create revio-frontend

# Set environment variable
heroku config:set REACT_APP_API_URL=https://revio-api.herokuapp.com/api

# Deploy
git push heroku main
```

### Note
- Free Heroku tier is limited (sleeps after 30 mins)
- Use paid tier ($7+) for production
- Consider alternatives for better performance

---

## 🐳 Option 3: Docker + VPS

### Best for: Full control, custom setup

### Prerequisites
- VPS (DigitalOcean, AWS, Linode)
- SSH access to server
- Domain name
- Docker installed on VPS

### Step 1: Prepare Docker Images

```bash
# Backend
cd revio-backend
docker build -t revio-backend:1.0 .

# Frontend
cd revio-frontend
docker build -t revio-frontend:1.0 .
```

### Step 2: Push to Docker Registry

```bash
# Login to Docker Hub
docker login

# Tag images
docker tag revio-backend:1.0 yourusername/revio-backend:latest
docker tag revio-frontend:1.0 yourusername/revio-frontend:latest

# Push
docker push yourusername/revio-backend:latest
docker push yourusername/revio-frontend:latest
```

### Step 3: Deploy on VPS

```bash
# SSH into your VPS
ssh root@your-vps-ip

# Install Docker if not present
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# Create docker-compose.yml (copy from repo)
# Update MongoDB URI and environment variables

# Start services
docker-compose up -d

# View logs
docker-compose logs -f
```

### Step 4: Setup Reverse Proxy (Nginx)

```bash
# Install Nginx
sudo apt-get install nginx

# Configure (example)
sudo nano /etc/nginx/sites-available/revio

# Add:
server {
    listen 80;
    server_name your-domain.com;

    location / {
        proxy_pass http://localhost:3000;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_cache_bypass $http_upgrade;
    }

    location /api {
        proxy_pass http://localhost:8080/api;
    }
}

# Enable site
sudo ln -s /etc/nginx/sites-available/revio /etc/nginx/sites-enabled/

# Test and reload
sudo nginx -t
sudo systemctl reload nginx
```

### Step 5: Setup SSL (Let's Encrypt)

```bash
# Install Certbot
sudo apt-get install certbot python3-certbot-nginx

# Get certificate
sudo certbot --nginx -d your-domain.com

# Auto-renew
sudo systemctl enable certbot.timer
```

---

## ☁️ Option 4: AWS

### Using AWS Elastic Beanstalk (Recommended for AWS)

#### Backend on Elastic Beanstalk

```bash
# Install AWS CLI and EB CLI
pip install awsebcli

# Initialize
eb init -p "Docker running on 64bit Amazon Linux 2"

# Create environment
eb create revio-api

# Deploy
eb deploy

# Get URL
eb open
```

#### Frontend on S3 + CloudFront

```bash
# Build
npm run build

# Create S3 bucket
aws s3 mb s3://revio-frontend

# Upload build
aws s3 sync build/ s3://revio-frontend/ --delete

# Create CloudFront distribution (use AWS Console)
```

---

## 💧 Option 5: DigitalOcean

### Simple App Platform (No Docker needed)

#### Step 1: Create DigitalOcean Account

Visit [digitalocean.com](https://www.digitalocean.com)

#### Step 2: Create Database

1. Create MongoDB cluster
2. Save connection string

#### Step 3: Deploy Backend

1. Go to App Platform
2. Create App → GitHub → Select revio-backend
3. Configure:
   - **Runtime**: Docker
   - **Environment**: 
     ```
     SPRING_DATA_MONGODB_URI=your-mongo-url
     JWT_SECRET=your-secret
     ```

4. Deploy

#### Step 4: Deploy Frontend

1. Create App → GitHub → Select revio-frontend
2. Configure:
   - **Environment**:
     ```
     REACT_APP_API_URL=https://your-backend-url/api
     ```

3. Deploy

#### Step 5: Connect Domain

1. Go to App settings
2. Add custom domain
3. Point DNS records

---

## 🔐 Environment Variables

### Backend Production

```env
# Database
SPRING_DATA_MONGODB_URI=mongodb+srv://user:pass@cluster.mongodb.net/revio_db

# JWT
JWT_SECRET=very-long-random-secret-key-at-least-32-chars
JWT_EXPIRATION=86400000

# CORS
CORS_ORIGINS=https://your-frontend-domain.com

# Server
server.port=8080
spring.application.name=revio-api
```

### Frontend Production

```env
REACT_APP_API_URL=https://api.your-domain.com
REACT_APP_ENV=production
REACT_APP_DEBUG=false
```

---

## ✅ Post-Deployment

### Verification Checklist

- [ ] Backend API is responding
- [ ] Frontend loads without errors
- [ ] Can register a new account
- [ ] Can login successfully
- [ ] Can create sections/topics
- [ ] Can complete topics and schedule revisions
- [ ] Can view today's revisions
- [ ] Dashboard shows correct statistics
- [ ] No console errors
- [ ] HTTPS working
- [ ] Performance acceptable

### Testing

```bash
# Test backend
curl https://api.your-domain.com/api/auth/health

# Test frontend
# Visit https://your-domain.com in browser

# Full workflow test
# 1. Register
# 2. Create section
# 3. Create subsection
# 4. Create topic
# 5. Mark complete
# 6. Check revisions
```

### Monitoring

- [ ] Set up error logging (Sentry)
- [ ] Set up performance monitoring (New Relic)
- [ ] Set up uptime monitoring
- [ ] Set up database backups
- [ ] Set up email alerts

---

## 🐛 Troubleshooting

### Frontend won't load

**Error**: Blank page or 404

**Solution**:
```bash
# Check build
npm run build

# Verify deployment folder exists
# Check build/index.html present
```

### API not responding

**Error**: Network error, CORS error

**Solution**:
```bash
# Check backend is running
curl https://api.your-domain.com/api/auth/health

# Verify CORS is configured in backend
# Check API_URL in frontend .env
```

### Database connection error

**Error**: Cannot connect to MongoDB

**Solution**:
```bash
# Check connection string
# Verify IP whitelist in MongoDB Atlas
# Check credentials are correct
# Verify database exists
```

### Login fails

**Error**: 401 Unauthorized

**Solution**:
- Check JWT_SECRET is same in backend
- Clear localStorage on frontend
- Verify token format
- Check token expiration

### Performance issues

**Solution**:
- Enable caching headers
- Optimize images
- Use CDN for static files
- Database indexes
- Connection pooling

---

## 📊 Scaling Considerations

### For Growth

1. **Database**: Upgrade MongoDB tier
2. **Backend**: Auto-scaling groups
3. **Frontend**: CDN distribution
4. **Caching**: Redis for sessions
5. **Load Balancing**: Multiple instances

### Cost Optimization

- Use free tiers while learning
- Upgrade gradually as users grow
- Monitor usage and optimize
- Use serverless where possible

---

## 🔄 Continuous Deployment

### GitHub Actions Example

Create `.github/workflows/deploy.yml`:

```yaml
name: Deploy

on:
  push:
    branches: [main]

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - uses: actions/setup-node@v2
        with:
          node-version: '18'
      - run: npm install
      - run: npm run build
      - uses: actions/upload-artifact@v2
        with:
          name: build
          path: build/
```

---

## 📞 Support

For deployment issues:
1. Check service status pages
2. Review logs
3. Contact provider support
4. Use monitoring tools

---

**Deployment completed! Your Revio app is now live! 🎉**

Monitor performance and user feedback to continuously improve.
