# BusBuddy

BusBuddy is a Spring Boot application designed to manage and facilitate bus transit services.

# Features

1. User authentication and authorization
2. Bus route management
3. Scheduling and real-time tracking
4. Booking and ticketing system
5. Admin dashboard for managing routes and schedules

# API Endpoints

### AdminController

| Endpoint          | Method |    Description    | Access |
| :---------------- | :----: | :---------------: | :----: |
| `/admin/register` |  POST  | Register as admin | Public |

### AdminLoginController

| Endpoint        | Method | Description  | Access |
| :-------------- | :----: | :----------: | :----: |
| `/admin/login`  |  POST  | Admin login  | Public |
| `/admin/logout` |  POST  | Admin logout | Public |

### BusController

| Endpoint              | Method |      Description      | Access |
| :-------------------- | :----: | :-------------------: | :----: |
| `/bus/admin`          |  POST  |      Add new bus      | Admin  |
| `/bus/admin`          |  PUT   |  Update bus details   | Admin  |
| `/bus/admin/{busId}`  | DELETE |      Delete bus       | Admin  |
| `/bus/all`            |  GET   |     Get all buses     | Public |
| `/bus/all/{busId}`    |  GET   | Get bus details by ID | Public |
| `/bus/type/{busType}` |  GET   |   Get buses by type   | Public |

### ReservationController

| Endpoint            | Method |    Description     |    Access     |
| :------------------ | :----: | :----------------: | :-----------: |
| `/user/reservation` |  POST  |  Add reservation   | Authenticated |
| `/user/reservation` | DELETE | Cancel reservation | Authenticated |

### RouteController

| Endpoint       | Method |                Description                | Access |
| :------------- | :----: | :---------------------------------------: | :----: |
| `/route/admin` |  POST  |               Add new route               | Admin  |
| `/route/all`   |  GET   | Get all routes from source to destination | Public |

### UserController

| Endpoint       | Method | Description  |    Access     |
| :------------- | :----: | :----------: | :-----------: |
| `/user/login`  |  POST  |  User login  |    Public     |
| `/user/logout` |  GET   | User logout  | Authenticated |
| `/user/signup` |  POST  | User sign up |    Public     |

# Project Structure

```
BusBuddy/
├── .mvn/
│   └── wrapper/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── busbuddy/
│   │   └── resources/
│   └── test/
│       ├── java/
│       └── resources/
├── .gitignore
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

# Getting Started

## Prerequisites

Java 17 or higher

## Installation

Clone the repository:

```
git clone https://github.com/dee-coder01/BusBuddy.git
```

## Navigate to the project directory:

```
cd BusBuddy
```

## Build the project:

```
mvn clean install
```

## Running the Application

Run the Spring Boot application:

```
mvn spring-boot:run
```

## Access the application at:

```
http://localhost:8080
```

## Contributing

1. Fork the repository
2. Create a new branch

```
git checkout -b feature-name
```

3. Commit your changes

```
git commit -m "Add some feature"
```

4. Push to the branch

```
git push origin feature-name
```

5. Create a new Pull Request
