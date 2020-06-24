### Java 11
-make sure you have Java 11

### add mongo connection to intellij VM options before running the app
-Dspring.profiles.active=local
-Dspring.data.mongodb.username=fake user name 
-Dspring.data.mongodb.password=fake password
-Dspring.data.mongodb.host=fake host 
-Dspring.data.mongodb.port=xxxxx 
-Dspring.data.mongodb.database=fake database 
-Dspring.data.mongodb.retryWrites=false

### add JWT secret to intellij VM options before running the app
-Dsecurity.jwt.token.secret-key=secret

### APIs documentation
- http://localhost:8282/pizzeria/api-docs
- http://localhost:8282/pizzeria/swagger-ui/index.html

### heroku deployment
- set profile? in Heroku setting add a property `SPRING_PROFILES_ACTIVE` and set value as `prod` and when deploy, it will take all properties from `application-prod.properties` file <br />
-```$ git push heroku master```<br />
-```$ heroku logs --tail ```

### how to enable all actuator endpoints?
- below will enable all, e.g will show environments: https://pizzera.herokuapp.com/pizzeria/actuator/env
- management.endpoints.web.exposure.include=*