
## Stack Exchange Client

<!-- GETTING STARTED -->
## Getting Started
Simple Stack Exchange API client web-application, written on Java using Spring Boot.
Allows User to input string, uses that string as query for searching over stackoverflow by question title.

### How To Build:

1. Clone the repo
   ```sh
   git clone https://github.com/AlxStenshin/StackExchangeClient.git
   ```
2. Navigate to source dir
   ```sh
   cd StackExchangeClient/
   ```
3. Build and run tests using
   ```sh
   ./gradlew clean build
   ```
4. Run tests in verbose mode using 
   ```sh
   ./gradlew clean test --info
   ```


### How To Run:
```sh
./gradlew bootJar && java -jar build/libs/StackExchangeClient.jar
```

<!-- USAGE EXAMPLES -->
## Usage

This application provides access to its functionality in two ways:

1. via self-explaining Web-interface, available at http://localhost:8080/

2. via REST API at http://localhost:8080/api/questions
Only GET method available. At least two parameters allowed:
    - title, String, required value. Specifies query for searching over Stackoverflow questions.
    - page, Integer, optional value. Allows navigating over multi-page responses.
