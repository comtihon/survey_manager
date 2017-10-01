# Survey service
Survey service manager backend. Works with surveys, questions and answers. For more info see #Protocol.

## Run
Ensure that [PostgreSql](https://www.postgresql.org/) is accessible before running the service.  
Postrges access url is specified in application.properties for `spring.datasource.url`

### In docker

    sudo ./gradlew build buildDocker
    sudo docker run -p 8080:8080 -t com.surveyor.manager

### In OS

    ./gradlew bootRun

## Protocol
### CRUD Survey
### CRUD Question
### CRUD Answer
### Interactive (for frontend)
1. Create Survey -> get survey_id
2. Create Question -> get question_id
3. Attach question to survey
3. Create Answer -> get answer_id
4. Attach answer to question
