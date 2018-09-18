Crypto Robot - application for crypto trading and crypto robot ![Build Status](https://travis-ci.org/shekhargulati/spring-boot-maven-angular-starter.svg?branch=master)
-----

This project uses following libraries:

1. Spring Boot v1.5.9
2. Angular v5.0.4
3. Node v8.9.0
4. Yarn v1.3.2

## Running the backend for development mode

for start server execute:
<img src="https://i.gyazo.com/1dd7fbbc87b405a1f1e072f3e4a09a40.png"></img>

in browser open http://localhost:8080/api/cars
## Running the frontend for development mode
install "npm" https://nodejs.org/en/download/

only first time execute:

<img src="https://i.gyazo.com/122e511c14d52fffc96f6e266564a4cb.png"></img>

for start Angular server:

<img src="https://gyazo.com/fa55379cc57d3c4e456f497a2eb37e67.png"></img>

OR run with Production environment:
<img src="https://i.gyazo.com/b95a40491ed281a6ea47a2139c3991a0.png"></img>

in browser open http://localhost:4200
## Hot reloading

Both the front-end and back-end modules support hot reloading.

## Running the full application in Production

You can build the package as a single artifact by running the `./mvnw clean install`.
Next, you can run the application by executing:

```bash
$ java -jar backend/target/ngboot-app.jar
```

The application will be accessible at `http://localhost:8080`.