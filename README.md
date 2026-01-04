# =============================
# Build & Run Application
# =============================

# Build
mvn clean package

# Run
mvn spring-boot:run


# =================================
# Basic API Calls (No endpoint)
# =================================

# Single request
curl "http://localhost:8080/api/space/accept?id=1"

# Multiple different IDs (same minute)
curl "http://localhost:8080/api/space/accept?id=2"
curl "http://localhost:8080/api/space/accept?id=3"
curl "http://localhost:8080/api/space/accept?id=4"

# Duplicate ID (should be counted once per minute)
curl "http://localhost:8080/api/space/accept?id=5"
curl "http://localhost:8080/api/space/accept?id=5"


# =============================================
# API Calls (With endpoint – Deduplication Test)
# =============================================

# Same ID multiple times (same minute)
curl "http://localhost:8080/api/space/accept?id=10&endpoint=https://httpbin.org/get"
curl "http://localhost:8080/api/space/accept?id=10&endpoint=https://httpbin.org/get"

# Different IDs (same minute)
curl "http://localhost:8080/api/space/accept?id=20&endpoint=https://httpbin.org/get"
curl "http://localhost:8080/api/space/accept?id=30&endpoint=https://httpbin.org/get"


# =============================================
# Wait for Aggregation
# =============================================
# Wait 1 full minute (scheduler runs every calendar minute)
# Aggregation and POST happen for the previous minute


# =============================================
# Extension-1: POST Aggregation (AUTOMATIC)
# =============================================
# This POST request is sent automatically by the scheduler.
# No manual trigger is required.

# POST Target
# https://httpbin.org/post

# Payload sent automatically:
# {
#   "minuteStart": "YYYY-MM-DDTHH:MM:00Z",
#   "uniqueIdCount": <count>
# }

# Expected console output:
# YYYY-MM-DDTHH:MM:00Z -> <count> unique ids
# POST success to https://httpbin.org/post


# =============================================
# Optional: Manual POST Test (for verification)
# =============================================
curl -X POST https://httpbin.org/post \
  -H "Content-Type: application/json" \
  -d '{"minuteStart":"2026-01-04T12:22:00Z","uniqueIdCount":2}'


# =============================================
# Open H2 Database Console
# =============================================
# Open in browser:
# http://localhost:8080/h2-console

# Login details:
# JDBC URL : jdbc:h2:mem:testdb
# Username : sa
# Password : (leave empty)


# =============================================
# Database Queries
# =============================================
SELECT * FROM MINUTE_STATS;
SELECT COUNT(*) FROM MINUTE_STATS;
