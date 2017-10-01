insert into survey (survey_id, country_code, name) values('survey_id1', 'EN', 'test_survey1');

insert into question (question_id, survey_id, name) values('question_id1', 'survey_id1', 'question1');
insert into question (question_id, survey_id, name) values('question_id2', 'survey_id1', 'question2');
insert into question (question_id, survey_id, name) values('question_id3', 'survey_id1', 'question3');

insert into answer (answer_id, question_id, name) values('answer_id1', 'question_id1', 'true');
insert into answer (answer_id, question_id, name) values('answer_id2', 'question_id1', 'false');
insert into answer (answer_id, question_id, name) values('answer_id3', 'question_id1', 'unknown');

insert into answer (answer_id, question_id, name) values('answer_id4', 'question_id2', 'true');
insert into answer (answer_id, question_id, name) values('answer_id5', 'question_id2', 'false');
insert into answer (answer_id, question_id, name) values('answer_id6', 'question_id2', 'unknown');

insert into answer (answer_id, question_id, name) values('answer_id7', 'question_id3', 'true');
insert into answer (answer_id, question_id, name) values('answer_id8', 'question_id3', 'false');
insert into answer (answer_id, question_id, name) values('answer_id9', 'question_id3', 'unknown');

