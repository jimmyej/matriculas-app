# Enrollment management services
This Microservice contains the following endpoints:

- Students
- Courses
- Enrollments

### Tools
- Java 17
- Spring boot 3.3.1
- Postgres DB
- Swagger documentation 2.9.2
- Jacoco code coverage

### Setup
1. Create an application-local.yml file and add the following configuration.
    
    ```sh
    spring:
      datasource:
        url: jdbc:postgresql://localhost:5432/postgres
        username: <your username>
        password: <your password>
    
      jpa:
        properties:
          hibernate:
            ddl-auto: create
            default_schema: enrollmentdb
    ```
2. add the following environment variable in your IDE run configuration window.

    ```sh
    spring.profiles.active=local
    ```
### Installation

To install the application dependencies run the following maven command:

```sh
mvn clean install
```

To collect the code coverage run the following Maven command:

```sh
mvn clean verify
```

To scan the code coverage with SonarQube run the following Maven command:

```sh
mvn clean verify sonar:sonar \
  -Dsonar.projectKey=enrollment-management-app \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=TOKEN_GENERATED
```
### Documentation
1. To see the api documentation open the following link:
```http
http://localhost:8080/api/enroll-management/swagger-ui/index.html
```