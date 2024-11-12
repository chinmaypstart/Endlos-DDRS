create table module(
 	pkid smallint not null,
 	name varchar(100) not null,
 	primary key (pkid),
 	unique (name),
 	constraint positive_pkid check(pkid > 0)
);
insert into module values(1,'User');
insert into module values(2,'Role');
insert into module values(3,'Device');
insert into module values(4,'Screen');
insert into module values(5,'Device Diagnosis');
insert into module values(6,'Response Message');
insert into module values(7,'Dashboard');
insert into module values(8,'Report');
insert into module values(9,'System Setting');

create table rights(
 	pkid smallint not null,
 	name varchar(100) not null,
 	primary key (pkid),
 	unique (name),
 	constraint positive_pkid check(pkid > 0)
);
insert into rights values(1,'Add');
insert into rights values(2,'Update');
insert into rights values(3,'View');
insert into rights values(4,'Delete');
insert into rights values(5,'Activation');
insert into rights values(6,'List');
insert into rights values(7,'Download');
insert into rights values(8,'File Upload');
insert into rights values(9,'Import');

create table file (
 	pkid bigserial not null,
 	fileid VARCHAR(64) not null,
 	name VARCHAR(200) not null,
 	fkmoduleid smallint not null,
 	dateupload bigint not null,
 	ispublic boolean not null default false,
	originalname varchar(200) default null,
	compressname varchar(200) default null,
 	primary key(pkid),
 	unique (fileid),
 	constraint positive_fkmoduleid check (fkmoduleid > 0),
 	constraint positive_pkid check(pkid > 0),
 	constraint file_fkmoduleid foreign key(fkmoduleid) references module(pkid)
);
create index index_file_dateupload on file(dateupload);
create index index_file_ispublic on file(ispublic);
create index index_file_fkmoduleid on file(fkmoduleid);

create table users(
	pkid bigserial not null,
  	lockversion bigint not null,
  	name varchar(100) not null,
  	email varchar(100) default null,
  	mobile varchar(15) default null,
  	verificationtoken varchar(64) not null,
  	isverificationtokenused boolean not null default false,
  	resetpasswordtoken varchar(64) default null,
  	isresetpasswordtokenused boolean not null default false,
  	dateresetpassword bigint default null,
  	twofactortoken varchar(16) default null,
  	istwofactortokenused boolean not null default false,
  	datetwofactortoken bigint default null,
  	hasloggedin boolean not null default false,
  	verificationotp varchar(6) not null,
  	isverificationotpused boolean not null default false,
	isclientadmin boolean not null default false,
	fkprofilepicid bigint default null,
	address varchar(1000) default null,
	landmark varchar(1000) default null,
	pincode varchar(6) default null,
	fkcountryid bigint default null,
  	fkstateid bigint default null,
  	fkcityid bigint default null,
	istemppassword boolean not null default false,
	temppassword varchar(10) default null,
	uniquetoken varchar(100) default null,
  	fkcreateby bigint default null,
  	datecreate bigint not null,
  	fkupdateby bigint default null,
  	dateupdate bigint default null,
  	isactive boolean not null default false,
  	fkactchangeby bigint default null,
  	dateactchange bigint default null,
  	isarchive boolean not null default false,
  	fkarchiveby bigint default null,
  	datearchive bigint default null,
  	primary key(pkid),
  	unique(email),
  	unique(mobile),
  	unique(email, mobile),
  	unique(verificationtoken),
  	unique(resetpasswordtoken),
  	constraint positive_pkid check(pkid > 0),
  	constraint positive_lockversion check(lockversion >= 0),
  	constraint positive_fkcreateby check (fkcreateby > 0),
  	constraint positive_fkupdateby check (fkupdateby > 0),
  	constraint positive_fkactchangeby check (fkactchangeby > 0),
  	constraint positive_fkarchiveby check (fkarchiveby > 0),
  	constraint positive_fkcountryid check (fkcountryid > 0),
  	constraint positive_fkstateid check (fkstateid > 0),
  	constraint positive_fkcityid check (fkcityid > 0),
  	constraint positive_fkprofilepicid check (fkprofilepicid > 0),
  	constraint foreign_fkcreateby foreign key(fkcreateby) references users(pkid),
  	constraint foreign_fkupdateby foreign key(fkupdateby) references users(pkid),
  	constraint foreign_fkactchangeby foreign key(fkactchangeby) references users(pkid),
  	constraint foreign_fkarchiveby foreign key(fkarchiveby) references users(pkid),
  	constraint foreign_fkcountryid foreign key(fkcountryid) references country(pkid),
  	constraint foreign_fkstateid foreign key(fkstateid) references state(pkid),
  	constraint foreign_fkcityid foreign key(fkcityid) references city(pkid),
  	constraint foreign_fkprofilepicid foreign key(fkprofilepicid) references file(pkid)
);

create index index_users_fkcreateby on users(fkcreateby);
create index index_users_fkupdateby on users(fkupdateby);
create index index_users_fkactchangeby on users(fkactchangeby);
create index index_users_fkarchiveby on users(fkarchiveby);
create index index_users_isarchive on users(isarchive);
create index index_users_isactive on users(isactive);
create index index_users_isverificationotpused on users(isverificationotpused);
create index index_users_hasloggedin on users(hasloggedin);
create index index_users_fkcountryid on users(fkcountryid);
create index index_users_fkstateid on users(fkstateid);
create index index_users_fkcityid on users(fkcityid);
create index index_users_isclientadmin on users(isclientadmin);
create index index_users_fkprofilepicid on users(fkprofilepicid);
create index index_users_uniquetoken on users(uniquetoken);

insert into users (
pkid, lockversion, name, email, mobile, verificationtoken, isverificationtokenused,
hasloggedin, verificationotp, isverificationotpused, fkcreateby, datecreate,
isactive, isarchive,uniquetoken) VALUES (1, 1, 'Master Admin', 'admin@endlosiot.com','7149253237', 'verificationtoken',
true, false,'123456',true, 1, (select extract(epoch from now())::bigint), true, false,'NTfwZvev');

alter SEQUENCE users_pkid_seq restart with 2;

create table usersearch(
	fkuserid bigserial not null,
  	searchparam text default null,
  	primary key(fkuserid),
  	constraint positive_pkid check(fkuserid > 0),
  	constraint foreign_fkuserid foreign key(fkuserid) references users(pkid)
);
create index index_usersearch_searchparam on usersearch(searchparam);

insert into usersearch values (1, (select to_tsvector('simple',coalesce(tu.name,'')
|| ' ' || coalesce(tu.email,'') || ' ' || coalesce(tu.mobile,''))
from users tu where tu.pkid = 1));

create table role(
 	pkid bigserial not null,
  	lockversion bigint not null,
  	name varchar(30) not null,
  	description varchar(256) default null,
	enumtype smallint default null,
  	fkcreateby bigint default null,
  	datecreate bigint not null,
  	fkupdateby bigint default null,
  	dateupdate bigint default null,
	primary key(pkid),
  	constraint positive_pkid check(pkid > 0),
  	constraint positive_lockversion check(lockversion >= 0),
  	constraint positive_fkcreateby check (fkcreateby > 0),
  	constraint positive_fkupdateby check (fkupdateby > 0),
  	constraint foreign_fkcreateby foreign key(fkcreateby) references users(pkid),
  	constraint foreign_fkupdateby foreign key(fkupdateby) references users(pkid)
);
create index index_role_fkcreateby on role(fkcreateby);
create index index_role_fkupdateby on role(fkupdateby);
create index index_role_enumtype on role(enumtype);

insert into role (pkid, lockversion, name, enumtype, fkcreateby, datecreate) values (
1, 0, 'Master Admin', 1, 1, (select extract(epoch from now())::bigint));
insert into role (pkid, lockversion, name, enumtype, fkcreateby, datecreate) values (
2, 0, 'Client Admin', 2, 1, (select extract(epoch from now())::bigint));
insert into role (pkid, lockversion, name, enumtype, fkcreateby, datecreate) values (
3, 0, 'Client users', 3, 1, (select extract(epoch from now())::bigint));

alter SEQUENCE role_pkid_seq restart with 4;

create table rolemoduleright(
	fkroleid bigint not null,
	fkmoduleid bigint not null,
	fkrightsid bigint not null,
	primary key(fkroleid, fkmoduleid, fkrightsid),
	constraint positive_fkroleid check (fkroleid > 0),
	constraint positive_fkmoduleid check (fkmoduleid > 0),
	constraint positive_fkrightsid check (fkrightsid > 0),
  	constraint foreign_fkroleid foreign key(fkroleid) references role(pkid),
  	constraint foreign_fkmoduleid foreign key(fkmoduleid) references module(pkid),
  	constraint foreign_fkrightsid foreign key(fkrightsid) references rights(pkid)
);
create index index_rolemoduleright_fkroleid on rolemoduleright(fkroleid);
create index index_rolemoduleright_fkmoduleid on rolemoduleright(fkmoduleid);
create index index_rolemoduleright_fkrightsid on rolemoduleright(fkrightsid);

create table userrole(
	fkuserid bigint not null,
	fkroleid bigint not null,
  	primary key(fkuserid, fkroleid),
  	constraint positive_fkuserid check (fkuserid > 0),
  	constraint positive_fkroleid check (fkroleid > 0),
  	constraint foreign_fkuserid foreign key(fkuserid) references users(pkid),
  	constraint foreign_fkroleid foreign key(fkroleid) references role(pkid)
);

insert into userrole values(1,1);

create table userpassword(
	pkid bigserial not null,
	fkuserid bigint not null,
	password varchar(1000) not null,
	datecreate bigint not null,
	primary key(pkid),
	constraint positive_pkid check (pkid > 0),
	constraint positive_fkuserid check (fkuserid > 0),
  	constraint foreign_fkuserid foreign key(fkuserid) references users(pkid)
);
create index index_userpassword_fkuserid on userpassword(fkuserid);
create index index_userpassword_datecreate on userpassword(datecreate);
insert into userpassword(fkuserid,password,datecreate)
values(1,'$2a$11$1U4g5cVp8RDC5/KDQqGwwe7XlT8jVw40wXKQVSlZaLWMzFUm3QH2G',(select extract(epoch from now())::bigint));

create table emailaccount (
  pkid bigserial not null,
  lockversion bigint not null,
  name varchar(100) not null,
  host varchar(500) not null,
  intport bigint null default 25,
  username varchar(100) not null,
  password varchar(500)  not null,
  replytoemail varchar(100)  not null,
  emailfrom varchar(500)  not null,
  intrateperhour bigint  default null,
  intupdaterateperhour bigint default null,
  intrateperday bigint  default null,
  intupdaterateperday bigint  default null,
  enumauthmethod smallint not null default 0/*0=plain, 1=demo, 2=cram md5*/,
  enumauthsecurity smallint not null default 0/*0=none, 1=use ssl, 2=use tls*/,
  inttimeout bigint not null default 60000,
  fkcreateby bigint not null,
  datecreate bigint not null,
  fkupdateby bigint default null,
  dateupdate bigint default null,
  isactive boolean not null default true,
  fkactchangeby bigint default null,
  dateactchange bigint default null,
  isarchive boolean not null default false,
  fkarchiveby bigint default null,
  datearchive bigint default null,
  primary key (pkid),
  constraint positive_pkid check(pkid > 0),
  constraint positive_lockversion check(lockversion >= 0),
  constraint positive_fkcreateby check (fkcreateby > 0),
  constraint positive_fkupdateby check (fkupdateby > 0),
  constraint positive_fkactchangeby check (fkactchangeby > 0),
  constraint positive_fkarchiveby check (fkarchiveby > 0),
  constraint positive_enumauthmethod check (enumauthmethod >= 0),
  constraint positive_enumauthsecurity check (enumauthsecurity >= 0),
  constraint foreign_fkcreateby foreign key(fkcreateby) references users(pkid),
  constraint foreign_fkupdateby foreign key(fkupdateby) references users(pkid),
  constraint foreign_fkactchangeby foreign key(fkactchangeby) references users(pkid),
  constraint foreign_fkarchiveby foreign key(fkarchiveby) references users(pkid)
);

create index index_emailaccount_fkcreateby on emailaccount(fkcreateby);
create index index_emailaccount_fkupdateby on emailaccount(fkupdateby);
create index index_emailaccount_fkactchangeby on emailaccount(fkactchangeby);
create index index_emailaccount_fkarchiveby on emailaccount(fkarchiveby);
create index index_emailaccount_isarchive on emailaccount(isarchive);
create index index_emailaccount_isactive on emailaccount(isactive);

create table notification(
	pkid bigserial not null,
	name varchar(100) not null,
	isemail boolean not null default false,
	ispush boolean not null default false,
	primary key(pkid),
	constraint positive_pkid check(pkid > 0)
);
create index index_notification_isemail on notification(isemail);
create index index_notification_ispush on notification(ispush);

create table emailcontent (
  pkid bigserial not null,
  lockversion bigint  not null,
  fkemailaccountid bigint not null,
  subject varchar(1000) not null,
  content text not null,
  emailcc text default null,
  emailBcc text default null,
  fknotificationid smallint default  null,
  fkcreateby bigint default null,
  datecreate bigint not null,
  fkupdateby bigint default null,
  dateupdate bigint default null,
  primary key (pkid),
  unique(fknotificationid),
  constraint positive_pkid check(pkid > 0),
  constraint positive_lockversion check(lockversion >= 0),
  constraint positive_fkcreateby check (fkcreateby > 0),
  constraint positive_fkupdateby check (fkupdateby > 0),
  constraint positive_fkemailaccountid check(fkemailaccountid > 0),
  constraint foreign_fkcreateby foreign key(fkcreateby) references users(pkid),
  constraint foreign_fkupdateby foreign key(fkupdateby) references users(pkid),
  constraint foreign_fkemailaccountid foreign key(fkemailaccountid) references emailaccount(pkid),
  constraint foreign_fknotificationid foreign key(fknotificationid) references notification(pkid)
);

create index index_emailcontent_fkcreateby on emailcontent(fkcreateby);
create index index_emailcontent_fkupdateby on emailcontent(fkupdateby);
create index index_emailcontent_fkemailaccountid on emailcontent(fkemailaccountid);
create index index_emailcontent_fknotificationid on emailcontent(fknotificationid);

create table transactionemail (
  	pkid bigserial not null,
  	lockversion bigint not null,
  	fkemailaccountid bigint  not null,
  	emailto text not null,
  	emailcc text,
  	emailbcc text,
  	subject varchar(1000) not null,
  	body text not null,
  	enumstatus smallint not null default 0/*0=new, 1=inprocess, 2=failed, 3=sent*/,
  	numberretrycount bigint not null default 0,
  	attachmentpath text,
  	error text,
  	datesend bigint default null,
  	datesent bigint default null,
  	primary key (pkid),
  	constraint positive_pkid check(pkid > 0),
  	constraint positive_lockversion check(lockversion >= 0),
  	constraint positive_enumstatus check (enumstatus >= 0),
  	constraint positive_fkemailaccountid check(fkemailaccountid > 0),
  	constraint foreign_fkemailaccountid foreign key(fkemailaccountid) references emailaccount(pkid)
);

create index index_transactionemail_fkemailaccountid on transactionemail(fkemailaccountid);
create index index_transactionemail_enumstatus on transactionemail(enumstatus);
create index index_transactionemail_numberretrycount on transactionemail(numberretrycount);
create index index_transactionemail_datesend on transactionemail(datesend);

create table tokenblacklist(
	pkid bigserial not null,
	fkuserid bigint not null,
	jwttoken varchar(500) not null,
	primary key(pkid),
	constraint positive_pkid check (pkid > 0),
	constraint positive_fkuserid check (fkuserid > 0),
  	constraint foreign_fkuserid foreign key(fkuserid) references users(pkid)
);
create index index_tokenblacklist_fkuserid on tokenblacklist(fkuserid);
create index index_tokenblacklist_jwttoken on tokenblacklist(jwttoken);

create table systemsetting(
	pkid bigserial not null,
	key varchar(100) not null,
	value varchar(100) not null,
	enumdatatype int not null,
	primary key(pkid),
	unique(key),
	constraint positive_pkid check(pkid > 0),
	constraint positive_enumdatatype check(enumdatatype > 0)
);

insert into systemsetting(key, value, enumdatatype) values
('MAX_PASSWORD_STORE_COUNT_PER_USER','5', 1),
('LOCK_USER_ACCOUNT_ON_FAILED_ATTEMPTS','1',8),
('FAILED_LOGIN_ATTEMPT_COUNT','3', 1),
('UNLOCK_ACCOUNT_TIME_IN_HOURS','1', 1),
('TWO_FACTOR_AUTHENTICATION_ENABLED','0', 8),
('DEFAULT_FILE_PATH','/endlos-iot-api', 2),
('RESET_PASSWORD_TOKEN_VALID_MINUTES','1440', 1),
('PASSWORD_GENERATION_SYNTAX_CHECKING_ENABLED','1', 8),
('PASSWORD_GENERATION_MIN_LOWER_CASE_ALPHABETS','0', 1),
('PASSWORD_GENERATION_MIN_UPPER_CASE_ALPHABETS','1', 1),
('PASSWORD_GENERATION_MIN_SPECIAL_CHARACTERS','1', 1),
('PASSWORD_GENERATION_MIN_NUMERICS','1', 1),
('PASSWORD_GENERATION_MIN_LENGTH','8', 1),
('PASSWORD_GENERATION_MAX_LENGTH','16', 1),
('DEFAULT_PASSWORD_CHANGE_REQUIRED','0', 8),
('PASSWORD_USED_VALIDATION_ENABLED','1', 8),
('PASSWORD_EXPIRATION_MAX_AGE_NEEDED','0', 1),
('PASSWORD_EXPIRATION_MAX_AGE_DAYS','365', 1),
('SECRET_KEY_FOR_GENERATE_JWT_TOKEN','c8b28e2fcf5f4f8a88f808d8cbfffeebc8b28e2fcf5f4f8a88f808d8cbfffeebc8b28e2fcf5f4f8a88f808d8cbfffeeb',2),
('GENERATE_REFRESH_TOKEN_TIME_IN_MINUTES','10',1),
('REGISTRATION_JWT_TOKEN_EXPIRY_TIME_IN_MINUTES','60',1),
('RESET_PASSWORD_JWT_TOKEN_EXPIRY_TIME_IN_MINUTES','30',1),
('CAPTCHA_JWT_TOKEN_EXPIRY_TIME_IN_MINUTES','5',1),
('ACTIVATION_JWT_TOKEN_EXPIRY_TIME_IN_MINUTES','15',1),
('REFRESH_JWT_TOKEN_EXPIRY_TIME_IN_DAY','5',1),
('ACCESS_JWT_TOKEN_EXPIRY_TIME_IN_MINUTES','1440',1),
('TWO_FACTOR_TOKEN_EXPIRY_TIME_IN_MINUTES','10',1),
('FIRST_LOGIN_TOKEN_EXPIRY_TIME_IN_MINUTES','5',1),
('RESET_PASSWORD_RESEND_LIMIT','3', 1),
('URL','https://renestlife.com/',2),
('MAX_ALLOWED_DEVICE','2', 6),
('DEVICE_COOKIE_TIME_IN_SECONDS','120',1),
('OTP_VERIFICATION_VALID_MINUTE',15, 1),
('RESET_PASSWORD_DAY_WISE_LIMIT','3', 1);
