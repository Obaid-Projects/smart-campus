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
In JAX-RS, a new instance of each resource class is created for every single request that comes in. This means if I stored my room or sensor data inside the resource class itself, it would be wiped out after every request because the object gets thrown away. To get around this, I created a separate DataStore class that holds all the data in static fields. Because they are static, the data belongs to the class itself and not to any particular instance, so every request reads from and writes to the same shared data regardless of how many resource objects get created. I also used ConcurrentHashMap instead of a regular HashMap because multiple requests can come in at the same time, and a regular HashMap can get corrupted if two requests try to write to it simultaneously. ConcurrentHashMap handles this safely.

### Part 1.2 – HATEOAS
HATEOAS stands for Hypermedia as the Engine of Application State. Basically it just means that your API responses include links that tell the client where they can go next, rather than the client having to already know all the URLs. For example my discovery endpoint returns links to /api/v1/rooms and /api/v1/sensors so a developer hitting the API for the first time can immediately see what is available. This is much better than static documentation because the API essentially documents itself. If the URLs ever change, the links in the responses change too, so clients do not break. It makes life easier for anyone building on top of the API.

### Part 2.1 – Returning IDs vs Full Objects
If you only return IDs when listing rooms, the response is much smaller which saves bandwidth. But then the client has to make a separate request for every single room to get its details, which means lots of extra HTTP calls. On the other hand, returning full room objects means a bigger response but the client gets everything in one go without needing extra requests. For this API I chose to return full objects because it is more practical for a campus management system where you typically need all the room details straight away rather than fetching them one by one.

### Part 2.2 – DELETE Idempotency
Yes, DELETE is idempotent in my implementation. Idempotent just means that doing the same thing multiple times gives the same end result as doing it once. So if you DELETE a room that exists, it gets deleted and you get a 200 response. If you then try to DELETE the same room again, it no longer exists so you get a 404. Either way the room is gone from the system, which is the same outcome. This is the correct behaviour for REST APIs and means clients do not need to worry about accidentally sending the same DELETE request twice.

### Part 3.1 – @Consumes Mismatch
The @Consumes(MediaType.APPLICATION_JSON) annotation tells JAX-RS to only accept JSON data on that endpoint. If a client sends a request with the wrong content type like text/plain or application/xml, JAX-RS automatically rejects it and sends back a 415 Unsupported Media Type error before it even gets to my code. I do not need to write any extra validation for this, JAX-RS handles it on its own. This is useful because it stops the API from receiving data in a format it cannot process.

### Part 3.2 – QueryParam vs PathParam for Filtering
Using a query parameter like GET /api/v1/sensors?type=CO2 is better than putting the type in the path like GET /api/v1/sensors/type/CO2 because query parameters are optional. If no type is provided, the endpoint just returns everything. If you put the type in the path it becomes a required part of the URL, so you would need a completely separate endpoint just to get all sensors. Query parameters are also the standard way of doing filtering and searching in REST APIs, so it is what developers expect to see. It keeps the URL structure clean and flexible.

### Part 4.1 – Sub-Resource Locator Pattern
Instead of putting all the logic for sensor readings inside SensorResource, I used the Sub-Resource Locator pattern to delegate that to a separate SensorReadingResource class. So when a request comes in for /{sensorId}/readings, SensorResource just hands it off to SensorReadingResource to deal with. This keeps each class focused on one thing rather than having one massive class trying to handle everything. In a large API this makes a huge difference because one giant class with hundreds of methods would be really hard to read, debug, and maintain. Splitting responsibilities across smaller classes makes the whole thing much more manageable.

### Part 5.2 – HTTP 422 vs 404
A 404 error means the URL the client requested does not exist on the server. A 422 error means the URL is fine but there is something wrong with the data in the request body. When someone tries to create a sensor with a roomId that does not exist, the endpoint /api/v1/sensors is perfectly valid, the problem is with the roomId they provided inside the JSON. Sending a 404 would be confusing because it suggests the endpoint itself cannot be found, which is not true. A 422 is more accurate because it tells the client their request was understood but the data they sent references something that does not exist.

### Part 5.4 – Security Risks of Exposing Stack Traces
If your API returns raw Java stack traces when something goes wrong, you are basically handing attackers a map of your application. Stack traces show class names, method names, file paths, and line numbers, which tells an attacker exactly what libraries and frameworks you are using and which versions. They can then look up known security vulnerabilities for those exact versions and exploit them. Stack traces can also reveal how your internal logic works, making it easier to find weak points. That is why I implemented a GlobalExceptionMapper that catches any unexpected error and just returns a simple 500 message with no internal details exposed.

### Part 5.5 – Filters vs Manual Logging
If I put Logger.info() statements inside every single resource method, I would have to update every method individually any time the logging behaviour needed to change. That would be really tedious and easy to mess up. Using a JAX-RS filter means the logging happens automatically for every request and response without touching the resource classes at all. The filter runs before and after every single request, so nothing gets missed. It also keeps the resource methods clean because they only contain the actual business logic rather than being cluttered with logging code. If I ever want to change what gets logged, I just update the filter in one place.
