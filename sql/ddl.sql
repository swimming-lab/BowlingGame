-- MySQL Database Create Table
create database bowling;
use bowling;

drop table if exists account;
drop table if exists frame;
drop table if exists game;
drop table if exists hibernate_sequence;
drop table if exists scoreBoard;

-- MySQL Database Create Table
create table account (
    accountId bigint not null auto_increment,
    accountName varchar(255) not null,
    accountRole varchar(255),
    password varchar(255) not null,
    primary key (accountId)
);

create table frame (frameId bigint not null,
    scoreData varchar(255),
    scoreBoardId bigint,
    primary key (frameId)
);

create table game (gameId bigint not null auto_increment,
    regTime varchar(255),
    updateTime varchar(255),
    createdBy varchar(255),
    modifiedBy varchar(255),
    gameStatus varchar(255),
    totalScore integer,
    accountId bigint,
    primary key (gameId)
);

create table hibernate_sequence (next_val bigint);
insert into hibernate_sequence values ( 1 );

create table scoreBoard (
    scoreBoardId bigint not null auto_increment,
    curFrameNum integer not null,
    curPhaseNum integer not null,
    lastCalcFrameNum integer not null,
    gameId bigint,
    primary key (scoreBoardId)
);

alter table account add constraint UK_348j55o810wppirqq1km365nl unique (accountName);
alter table frame add constraint FK7euhtrmx9i2hboyw0de2d0ecs foreign key (scoreBoardId) references scoreBoard (scoreBoardId);
alter table game add constraint FKsbxix9lsh5winhmr0r8ktapdl foreign key (accountId) references account (accountId);
alter table scoreBoard add constraint FKk20ta3m8nsojt6rbq2fo2yhux foreign key (gameId) references game (gameId);