# Build
mvn clean package

# Run
mvn spring-boot:run

# -----------------------------
# Basic API Calls (No endpoint)
# -----------------------------

# Single request
curl "http://localhost:8080/api/space/accept?id=1"

# Multiple different IDs (same minute)
curl "http://localhost:8080/api/space/accept?id=2"
curl "http://localhost:8080/api/space/accept?id=3"
curl "http://localhost:8080/api/space/accept?id=4"

# Duplicate ID (should be counted once per minute)
curl "http://localhost:8080/api/space/accept?id=5"
curl "http://localhost:8080/api/space/accept?id=5"

# -----------------------------------------
# API Calls (With endpoint – Deduplication)
# -----------------------------------------

# Same ID multiple times (same minute)
curl "http://localhost:8080/api/space/accept?id=10&endpoint=https://httpbin.org/get"
curl "http://localhost:8080/api/space/accept?id=10&endpoint=https://httpbin.org/get"

# Different IDs (same minute)
curl "http://localhost:8080/api/space/accept?id=20&endpoint=https://httpbin.org/get"
curl "http://localhost:8080/api/space/accept?id=30&endpoint=https://httpbin.org/get"

# -----------------------------------------
# Wait 1 minute for aggregation
# (Scheduler runs every calendar minute)
# -----------------------------------------

# -----------------------------
# Open H2 Database Console
# -----------------------------
http://localhost:8080/h2-console

# H2 Login Details
JDBC URL : jdbc:h2:mem:testdb
Username : sa
Password : (leave empty)

# -----------------------------
# Database Queries
# -----------------------------
SELECT * FROM MINUTE_STATS;
SELECT COUNT(*) FROM MINUTE_STATS;
