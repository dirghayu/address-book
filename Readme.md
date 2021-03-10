# Address Book

## Assumption:
All the names are case sensitive.
For simplicity, Not using any authentication and authorization

## Run:

To run the test cases run
> mvn test

To run the application run 

> mvn install
> mvn spring-boot:run

## URLS
Swagger: http://localhost:8080/swagger-ui.html

### H2 dB
http://localhost:8080/h2-console/login.jsp?jsessionid=4db8130c0495835f10a6dfed5a943dcb
JDBC url: jdbc:h2:mem:testdb
username: sa
password: password

I was planing to add docker containerization and deploy to ec2. Due to time constraint not doing it.