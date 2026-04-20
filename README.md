# Smart Campus REST API

## Overview
This project is a RESTful API built using JAX-RS (Jersey 2.32) and deployed on Apache Tomcat.
It manages university rooms and sensors as part of the Smart Campus initiative.
The API allows campus facilities managers to create, retrieve, and delete rooms and sensors, record sensor readings, and monitor the campus infrastructure.

## How to Build and Run
### Prerequisites
- Java JDK 8 or higher
- Apache Maven
- Apache Tomcat 9

### Steps
1. Clone the repository: git clone https://github.com/Obaid-Projects/smart-campus.git .
2. Open the project in NetBeans or any IDE of your choice (the following instructions have not been tested on other IDEs besides Netbeans).
3. Right click the project and select **Clean and Build**
4. Right click the project and select **Run** (NetBeans will deploy it to Tomcat automatically).
5. The API is available at : `http://localhost:8080/smart-campus/api/v1`.

## Sample curl Commands
### 1. Get all rooms
curl -X GET http://localhost:8080/smart-campus/api/v1/rooms
### 2. Create a new room
curl -X POST http://localhost:8080/smart-campus/api/v1/rooms -H "Content-Type: application/json" -d "{"id":"HALL-101","name":"Main Hall","capacity":200}"
### 3. Get all sensors filtered by type
curl -X GET "http://localhost:8080/smart-campus/api/v1/sensors?type=CO2"
### 4. Post a reading to a sensor
curl -X POST http://localhost:8080/smart-campus/api/v1/sensors/TEMP-001/readings -H "Content-Type: application/json" -d "{"value":23.5}"
### 5.Delete a room
curl -X DELETE http://localhost:8080/smart-campus/api/v1/rooms/HALL-101
### 6. Get a sensor readings history
curl -X GET http://localhost:8080/smart-campus/api/v1/sensors/TEMP-001/readings
---
## Report: Answers to Coursework Questions
### Part 1.1 - JAX-RS Resource Lifecycle
By default, JAX-RS creates a new instance of each resource class for every incoming HTTP request.
