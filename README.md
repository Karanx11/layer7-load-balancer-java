# Layer 7 Load Balancer in Java

A multi-threaded Layer 7 Load Balancer built using Core Java to efficiently distribute HTTP requests across multiple backend servers. The project implements Round Robin load balancing, health monitoring, fault tolerance, path-based routing, and request metrics collection while demonstrating core concepts of distributed systems and backend engineering.

---

## Features

### Round Robin Load Balancing

Distributes incoming HTTP requests evenly across multiple backend servers.

Example:

Request 1 → Backend 8081

Request 2 → Backend 8082

Request 3 → Backend 8083

Request 4 → Backend 8081

---

### Multi-Threaded Request Handling

Uses Java's ExecutorService to process multiple client requests concurrently.

Benefits:

* Improved throughput
* Reduced response latency
* Better scalability under load

---

### Health Monitoring

Continuously monitors backend server health using a dedicated health-check service.

Health Endpoint:

/health

Example:

http://localhost:8081/health

Response:

OK

---

### Fault Tolerance

If a backend server becomes unavailable:

Backend 8081 ✅

Backend 8082 ❌

Backend 8083 ✅

The load balancer automatically removes the failed server from rotation and routes traffic only to healthy servers.

---

### Automatic Recovery Detection

If a failed server comes back online, the health checker automatically detects it and adds it back into the load balancing pool.

---

### Metrics Collection

Tracks:

* Total Requests
* Requests Per Backend Server

Metrics Endpoint:

/metrics

Example:

http://localhost:8080/metrics

Output:

Total Requests: 120

Requests Per Backend:

http://localhost:8081 -> 40

http://localhost:8082 -> 40

http://localhost:8083 -> 40

---

### Path-Based Routing (Layer 7)

Routes requests based on HTTP URL paths.

Examples:

/users → Backend 8081

/orders → Backend 8082

/payments → Backend 8083

Unknown paths fall back to Round Robin distribution.

---

## System Architecture

Client
│
▼
Load Balancer (Port 8080)
│
├── /users ─────► Backend 8081
│
├── /orders ────► Backend 8082
│
├── /payments ──► Backend 8083
│
└── Other Routes
│
▼
Round Robin Routing
│
├── Backend 8081
├── Backend 8082
└── Backend 8083

---

## Tech Stack

* Java 17+
* Core Java
* HttpServer API
* ExecutorService
* ScheduledExecutorService
* AtomicInteger
* ConcurrentHashMap
* Socket/HTTP Networking

---

## Project Structure

Layer7LoadBalancer/

├── BackendServer.java

├── LoadBalancer.java

├── ServerInfo.java

├── HealthChecker.java

└── MetricsManager.java

---

## Core Components

### BackendServer.java

Responsibilities:

* Creates backend servers
* Handles incoming requests
* Exposes health endpoint
* Returns processed request information

Endpoints:

/

/health

---

### LoadBalancer.java

Responsibilities:

* Accepts client requests
* Performs path-based routing
* Executes Round Robin distribution
* Forwards requests to backend servers
* Tracks metrics

---

### ServerInfo.java

Stores backend metadata:

* Backend URL
* Health status

Used by:

* Load Balancer
* Health Checker

---

### HealthChecker.java

Runs periodically using ScheduledExecutorService.

Responsibilities:

* Checks backend health every 5 seconds
* Marks servers healthy/unhealthy
* Enables automatic recovery

---

### MetricsManager.java

Tracks:

* Total requests
* Requests per backend

Uses:

* AtomicInteger
* ConcurrentHashMap

for thread-safe operations.

---

## How to Run

### Step 1: Compile

javac *.java

---

### Step 2: Start Backend Servers

Terminal 1

java BackendServer 8081

Terminal 2

java BackendServer 8082

Terminal 3

java BackendServer 8083

---

### Step 3: Start Load Balancer

Terminal 4

java LoadBalancer

Output:

Load Balancer running on port 8080

---

## API Testing

### Round Robin

http://localhost:8080/test

Refresh multiple times to observe request distribution.

---

### Path-Based Routing

Users Service

http://localhost:8080/users

Orders Service

http://localhost:8080/orders

Payments Service

http://localhost:8080/payments

---

### Health Monitoring

http://localhost:8081/health

http://localhost:8082/health

http://localhost:8083/health

---

### Metrics

http://localhost:8080/metrics

---

## Sample Console Output

Health Checker:

http://localhost:8081 -> HEALTHY

http://localhost:8082 -> HEALTHY

http://localhost:8083 -> HEALTHY

---

Request Routing:

pool-1-thread-1 -> http://localhost:8081

pool-1-thread-2 -> http://localhost:8082

pool-1-thread-3 -> http://localhost:8083

---

## Key Concepts Demonstrated

* Layer 7 Load Balancing
* Distributed Systems Fundamentals
* Concurrent Programming
* Health Monitoring
* Fault Tolerance
* Automatic Recovery
* HTTP Request Routing
* Backend Scalability
* Thread Safety
* Performance Optimization

---

## Future Enhancements

* Least Connections Load Balancing
* Weighted Round Robin
* Dynamic Backend Registration
* Configuration File Support
* Docker Deployment
* Request Rate Limiting
* Response Time Analytics
* HTTPS Support