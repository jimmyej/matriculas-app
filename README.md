# Enrollment management services
This Microservice contains the following endpoints:

- Students
- Courses
- Enrolments

### Tools
- Java 11
- Spring boot 2.7.0
- Postgres DB
- Swagger documentation 2.9.2
- Jacoco code coverage

### Setup
1. Create a application-local.yml file and add the following configuration.
    
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

