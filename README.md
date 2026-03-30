# Social Network CLI — Java

A console-based social networking application built in Java using a layered architecture.

## Features
- Sign up and log in
- View and manage friendships
- Friend recommendations using friends-of-friends algorithm
- View top users ranked by friend count
- Calculate degree of separation between users using BFS (Breadth-First Search)

## Project Structure
```
src/socialnetwork/
├── model/
│   ├── User.java
│   └── FriendshipGraph.java
├── repository/
│   └── UserRepository.java
├── service/
│   └── SocialNetworkService.java
├── exception/
│   ├── UserNotFoundException.java
│   └── AlreadyFriendsException.java
├── util/
│   └── InputValidator.java
└── Main.java
```

## Design
- **Layered architecture** — UI → Service → Repository → Model, each layer only talks to the one below
- **BFS algorithm** for calculating degree of separation between users
- **Friends-of-friends scoring** for ranked friend recommendations
- **Custom exceptions** for clean error handling
- **Input validation** to handle all invalid user inputs gracefully

## How to Run
```bash
javac -d out src/socialnetwork/**/*.java src/socialnetwork/Main.java
java -cp out socialnetwork.Main
```

## Tech Stack
- Java (Core)
- Data Structures: HashMap, HashSet, Queue (for BFS)
- No external libraries — pure Java
