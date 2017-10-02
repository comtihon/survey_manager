# Survey service [![Build Status](https://travis-ci.org/comtihon/survey_manager.svg?branch=master)](https://travis-ci.org/comtihon/survey_manager)
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
GET __/survey/{id}__ get full survey config by id.  
Response:

    {
        "result" : true,
        "response" : 
        {
            "id" : "survey_id"
            "name" : "suvey_name",
            "country_code" : "CC",
            "questions" : 
            [
                {
                    "id" : "question_id",
                    "name" : "question_name",
                    "answers" :
                    [
                        {
                            "id" : "answer_id",
                            "name" : "answer_name"
                        }
                        ...
                    ]
                }
                ...
            ]
        }
    }
DELETE __/survey/{id}__ delete survey, all its questions and answers by id.  
Response:

    {
        "result" : true,
        "response" : "ok"
    }
POST __/survey__ create survey  
BODY:

    {
        "name" : "suvey_name",
        "country_code" : "CC",
        "questions" : 
        [
            {
                "name" : "question_name",
                "answers" : [{"name" : "answer_name"}...]
            }
            ...
        ]
    }
Response:

    {
        "result" : true,
        "response" : "<created_survey_id>"
    }
POST __/survey/load__ same as `/survey`. Load was planned to be method for creating whole survey 
while just POST to `/survey` for creating only survey headers (without questions and answers). 
But now there is now difference between them.  
PUT __/survey/{id}__ edit survey by id  
BODY:  
send only information to be updated.  
do not send questions as they are ignored. Use questions CRUD for editing questions.

    {
        "name" : "new_name"
    }
Response:

    {
        "result" : true,
        "response" : "ok"
    }

### CRUD Question
POST __/question__ create question  
BODY:  

    {
        "name" : "question_name",
        "answers" : [{"name" : "answer_name"}...]
    }
Response:

    {
        "result" : true,
        "response" : "<created_questsion_id>"
    }
PUT __/question/{id}__ edit question by id  
BODY:  
do not send answers as they are ignored. Use answers CRUD for editing answers.

    {
        "name" : "new_name",
    }
Response:

    {
        "result" : true,
        "response" : "ok"
    }
DELETE __/question/{id}__ delete question by id  
Response:

    {
        "result" : true,
        "response" : "ok"
    }
GET __/question/{id}__ get question by id  
Response:

    {
        "result" : true,
        "response" : 
        {
            "id" : "question_id",
            "name" : "question_name",
            "answers" :
            [
                {
                    "id" : "answer_id",
                    "name" : "answer_name"
                }
                ...
            ]
        }
    }
### CRUD Answer
POST __/answer__ create answer  
BODY:

    {
        "name" : "answer_name"
    }
Response:

    {
        "result" : true,
        "response" : "<created_answer_id>"
    }
PUT __/answer/{id}__ update answer by id  
BODY:

    {
        "name" : "new_name"
    }
Response:

    {
        "result" : true,
        "response" : "ok"
    }
DELETE __/answer/{id}__ delete answer by id
Response:

    {
        "result" : true,
        "response" : "ok"
    }
GET __answer/{id}__ get answer by id

    {
        "result" : true,
        "response" :
        {
            "id" : "answer_id"
            "name" : "answer_name"
        }
    }
### Interactive (for frontend)
1. Create Survey -> get survey_id  
See Survey's CRUD
2. Create Question -> get question_id  
See Question's CRUD
3. Attach question to survey  
GET __/attach/question?question_id=<question_id>&survey_id=<survey_id>__ attach question with `<question_id>` to
survey with `<survey_id>`  
Response:  


    {
        "result" : true,
        "response" : "ok"
    }

3. Create Answer -> get answer_id  
See Answer's CRUD
4. Attach answer to question
GET __/attach/answer?question_id=<question_id>&answer_id=<answer_id>__ attach answer with `<answer_id>` to
question with `<question_id>`  
Response:  


    {
        "result" : true,
        "response" : "ok"
    }