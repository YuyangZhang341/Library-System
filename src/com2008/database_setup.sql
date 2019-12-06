create table journals
(
    issn             varchar(9) default '' not null
        primary key,
    name             tinytext              null,
    chiefEditorEmail varchar(100)          null
);

create table editions
(
    number int        default 0   not null,
    issn   varchar(9) default '0' not null,
    vol    int        default 0   not null,
    primary key (number, issn, vol),
    constraint editions_journals_issn_fk
        foreign key (issn) references journals (issn)
);

create index editions_volumes_vol_fk
    on editions (vol);

create table selectedArticles
(
    issn varchar(9) not null
        primary key,
    name tinytext   null
);

create table submissions
(
    submissionID     int auto_increment
        primary key,
    title            tinytext      null,
    abstract         text          null,
    pdf              mediumblob    null,
    mainAuthorsEmail varchar(100)  null,
    issn             varchar(9)    null,
    reviewCount      int default 0 null,
    constraint submissions_journals_issn_fk
        foreign key (issn) references journals (issn)
);

create table consideredSubmissions
(
    submissionID int default 0 not null
        primary key,
    decision     varchar(15)   null,
    constraint consideredSubmissions_submissions_submissionID_fk
        foreign key (submissionID) references submissions (submissionID)
);

create table publishedArticles
(
    submissionID int default 0 not null
        primary key,
    vol          int           null,
    number       int           null,
    startPage    int           null,
    endPage      int           null,
    constraint publishedArticles2_submissions_submissionID_fk
        foreign key (submissionID) references submissions (submissionID)
);

create table reviews
(
    reviewerID          int           not null,
    summary             text          null,
    typographicalErrors text          null,
    initialVerdict      varchar(20)   null,
    submissionID        int default 0 not null,
    finalVerdict        varchar(20)   null,
    primary key (reviewerID, submissionID),
    constraint reviews_submissions_submissionID_fk
        foreign key (submissionID) references submissions (submissionID)
);

create table criticisms
(
    criticismID  int auto_increment
        primary key,
    submissionID int  null,
    reviewerID   int  null,
    criticism    text null,
    response     text null,
    constraint criticisms_reviews_submissionID_reviewerID_fk
        foreign key (submissionID, reviewerID) references reviews (submissionID, reviewerID)
);

create table revisedSubmissions
(
    submissionID int default 0 not null
        primary key,
    title        text          null,
    abstract     mediumtext    null,
    pdf          mediumblob    null,
    constraint revisedSubmissions_submissions_submissionID_fk
        foreign key (submissionID) references submissions (submissionID)
);

create table users
(
    email                 varchar(100) default '' not null
        primary key,
    title                 varchar(50)             null,
    forenames             varchar(50)             null,
    surname               varchar(50)             null,
    universityAffiliation varchar(50)             null,
    password              varchar(100)            null
);

create table authors
(
    submissionID int          default 0  not null,
    email        varchar(100) default '' not null,
    primary key (submissionID, email),
    constraint authors_submissions_submissionID_fk
        foreign key (submissionID) references submissions (submissionID),
    constraint authors_users_email_fk
        foreign key (email) references users (email)
);

create table editors
(
    issn  varchar(9)   default '' not null,
    email varchar(100) default '' not null,
    primary key (issn, email),
    constraint editors_journals_issn_fk
        foreign key (issn) references journals (issn),
    constraint editors_users_email_fk
        foreign key (email) references users (email)
);

create table reviewers
(
    email        varchar(100) default '' not null,
    submissionID int          default 0  not null,
    reviewerID   int                     null,
    primary key (email, submissionID),
    constraint reviewers_submissions_submissionID_fk
        foreign key (submissionID) references submissions (submissionID),
    constraint reviewers_users_email_fk
        foreign key (email) references users (email)
);

create table volumes
(
    vol  int        default 0  not null,
    year int                   null,
    issn varchar(9) default '' not null,
    primary key (vol, issn),
    constraint volumes_journals_issn_fk
        foreign key (issn) references journals (issn)
);


