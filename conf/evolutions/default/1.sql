# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table question (
  id                        integer not null,
  text                      varchar(255),
  constraint pk_question primary key (id))
;

create table user (
  id                        integer not null,
  username                  varchar(255),
  password_hash             varchar(255),
  constraint pk_user primary key (id))
;


create table user_to_question (
  user_id                        integer not null,
  question_id                    integer not null,
  constraint pk_user_to_question primary key (user_id, question_id))
;
create sequence question_seq;

create sequence user_seq;




alter table user_to_question add constraint fk_user_to_question_user_01 foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table user_to_question add constraint fk_user_to_question_question_02 foreign key (question_id) references question (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists question;

drop table if exists user_to_question;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists question_seq;

drop sequence if exists user_seq;

