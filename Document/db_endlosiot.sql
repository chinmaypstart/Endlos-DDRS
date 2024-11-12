alter table file add column path varchar(5000) not null;

/*14/02/2023*/
alter table notification add column issms boolean not null default false;
create index index_notification_issms on notification(issms);

create table client(
	pkid bigserial not null,
  	lockversion bigint not null,
  	name varchar(500) not null,
  	fklogoid bigint default null,
  	address varchar(500) not null,
  	area varchar(500) default null,
  	fkstateid bigint not null,
  	fkcityid bigint not null,
	pincode varchar(6) not null,
	siteid varchar(40) default null,
    siteuid varchar(40) default null,
  	fkcreateby bigint default null,
  	datecreate bigint not null,
  	fkupdateby bigint default null,
  	dateupdate bigint default null,
  	isactive boolean not null default true,
  	fkactchangeby bigint default null,
  	dateactchange bigint default null,
	primary key(pkid),
  	unique(name),
  	constraint positive_pkid check(pkid >= 0),
  	constraint positive_lockversion check(lockversion >= 0),
  	constraint positive_fkcreateby check (fkcreateby >= 0),
  	constraint positive_fkupdateby check (fkupdateby >= 0),
  	constraint positive_fkactchangeby check (fkactchangeby >= 0),
  	constraint positive_fkstateid check (fkstateid >= 0),
  	constraint positive_fkcityid check (fkcityid >= 0),
  	constraint positive_fklogoid check (fklogoid > 0),
  	constraint foreign_fkcreateby foreign key(fkcreateby) references users(pkid),
  	constraint foreign_fkupdateby foreign key(fkupdateby) references users(pkid),
  	constraint foreign_fkactchangeby foreign key(fkactchangeby) references users(pkid),
  	constraint foreign_fkstateid foreign key(fkstateid) references state(pkid),
  	constraint foreign_fkcityid foreign key(fkcityid) references city(pkid),
  	constraint foreign_fklogoid foreign key(fklogoid) references file(pkid)
);
create index index_client_fkcreateby on client(fkcreateby);
create index index_client_fkupdateby on client(fkupdateby);
create index index_client_fkactchangeby on client(fkactchangeby);
create index index_client_isactive on client(isactive);
create index index_client_fkstateid on client(fkstateid);
create index index_client_fkcityid on client(fkcityid);
create index index_client_fklogoid on client(fklogoid);

 create table location(
 	pkid bigserial not null,
  	lockversion bigint not null,
  	name varchar(500) not null,
  	fkclientid bigint not null,
  	address varchar(500) not null,
  	area varchar(500) default null,
  	fkstateid bigint not null,
  	fkcityid bigint not null,
	pincode varchar(6) not null,
	latitude varchar(20) default null,
	longitude varchar(20) default null,
	altitude varchar(20) default null,
	locationid varchar(40) default null,
  	fkcreateby bigint default null,
  	datecreate bigint not null,
  	fkupdateby bigint default null,
  	dateupdate bigint default null,
  	isactive boolean not null default true,
  	fkactchangeby bigint default null,
  	dateactchange bigint default null,
	primary key(pkid),
  	unique(name),
  	constraint positive_pkid check(pkid >= 0),
  	constraint positive_lockversion check(lockversion >= 0),
  	constraint positive_fkcreateby check (fkcreateby >= 0),
  	constraint positive_fkupdateby check (fkupdateby >= 0),
  	constraint positive_fkactchangeby check (fkactchangeby >= 0),
  	constraint positive_fkstateid check (fkstateid >= 0),
  	constraint positive_fkcityid check (fkcityid >= 0),
  	constraint foreign_fkcreateby foreign key(fkcreateby) references users(pkid),
  	constraint foreign_fkupdateby foreign key(fkupdateby) references users(pkid),
  	constraint foreign_fkactchangeby foreign key(fkactchangeby) references users(pkid),
  	constraint foreign_fkstateid foreign key(fkstateid) references state(pkid),
  	constraint foreign_fkcityid foreign key(fkcityid) references city(pkid),
  	constraint foreign_fkclientid foreign key(fkclientid) references client(pkid)
);
create index index_location_fkcreateby on location(fkcreateby);
create index index_location_fkupdateby on location(fkupdateby);
create index index_location_fkactchangeby on location(fkactchangeby);
create index index_location_isactive on location(isactive);
create index index_location_fkstateid on location(fkstateid);
create index index_location_fkcityid on location(fkcityid);
create index index_location_fkclientid on location(fkclientid);

alter table users
add column fkclientid bigint default null,
add column fklocationid bigint default null,
add constraint positive_fkclientid check (fkclientid > 0),
add constraint positive_fklocationid check (fklocationid > 0),
add constraint foreign_fkclientid foreign key(fkclientid) references client(pkid),
add constraint foreign_fklocationid foreign key(fklocationid) references location(pkid);

create table gateway(
	pkid bigserial not null,
  	lockversion bigint not null,
  	name varchar(500) not null,
  	gatewayid varchar(50) default null,
  	ipaddress varchar(50) default null,
  	macaddress varchar(50) default null,
  	fklocationid bigint not null,
  	longitude varchar(20) default null,
	latitude varchar(20) default null,
  	fkcreateby bigint default null,
  	datecreate bigint not null,
  	fkupdateby bigint default null,
  	dateupdate bigint default null,
  	isactive boolean not null default true,
  	fkactchangeby bigint default null,
  	dateactchange bigint default null,
  	isarchive boolean not null default false,
  	fkarchiveby bigint default null,
  	datearchive bigint default null,
	primary key(pkid),
  	constraint positive_pkid check(pkid >= 0),
  	constraint positive_lockversion check(lockversion >= 0),
  	constraint positive_fkcreateby check (fkcreateby >= 0),
  	constraint positive_fkarchiveby check (fkarchiveby >= 0),
  	constraint positive_fkupdateby check (fkupdateby >= 0),
  	constraint positive_fkactchangeby check (fkactchangeby >= 0),
  	constraint positive_fklocationid check (fklocationid > 0),
  	constraint foreign_fkcreateby foreign key(fkcreateby) references users(pkid),
  	constraint foreign_fkupdateby foreign key(fkupdateby) references users(pkid),
  	constraint foreign_fkactchangeby foreign key(fkactchangeby) references users(pkid),
    constraint foreign_fkarchiveby foreign key(fkarchiveby) references users(pkid),
  	constraint foreign_fklocationid foreign key(fklocationid) references location(pkid)
);
create index index_gateway_fkcreateby on gateway(fkcreateby);
create index index_gateway_fkarchiveby on gateway(fkarchiveby);
create index index_gateway_fkupdateby on gateway(fkupdateby);
create index index_gateway_fkactchangeby on gateway(fkactchangeby);
create index index_gateway_isactive on gateway(isactive);
create index index_gateway_fklocationid on gateway(fklocationid);


alter table gateway add column fkclientid bigint not null,
	add constraint positive_fkclientid check(fkclientid>0),
	add constraint foreign_fkclientid foreign key(fkclientid) references client(pkid);
create index index_gateway_fkclientid on gateway(fkclientid);


alter table location add column
isarchive boolean not null default false,
  add column	fkarchiveby bigint default null,
  add column	datearchive bigint default null,
  add	constraint positive_fkarchiveby check (fkarchiveby >= 0),
  add	constraint foreign_fkarchiveby foreign key(fkarchiveby) references users(pkid);
 create index index_location_fkarchiveby on location(fkarchiveby);

alter table client add column
isarchive boolean not null default false,
  add column	fkarchiveby bigint default null,
  add column	datearchive bigint default null,
  add	constraint positive_fkarchiveby check (fkarchiveby >= 0),
  add	constraint foreign_fkarchiveby foreign key(fkarchiveby) references users(pkid);
 create index index_client_fkarchiveby on client(fkarchiveby);

 create table devicedata(
 	pkid bigserial not null,
   	lockversion bigint not null,
   	plus decimal(14,2) default null,
   	analog1 decimal(14,2) default null,
   	analog2 decimal(14,2) default null,
   	turbodity decimal(14,2) default null,
   	chlorine decimal(14,2) default null,
   	datecreate bigint not null,
 	primary key(pkid),
   	constraint positive_pkid check(pkid >= 0),
   	constraint positive_lockversion check(lockversion >= 0)
 );

 create table devicedata(
  	pkid bigserial not null,
    	plus decimal(14,2) default null,
    	analog1 decimal(14,2) default null,
    	analog2 decimal(14,2) default null,
    	turbodity decimal(14,2) default null,
    	chlorine decimal(14,2) default null,
    	datecreate bigint not null,
  	    primary key(pkid),
    	constraint positive_pkid check(pkid >= 0)
   );

  ALTER TABLE users
  ADD COLUMN isplus boolean not null default true,
  ADD COLUMN isanalog1 boolean not null default true,
  ADD COLUMN isanalog2 boolean not null default true,
  ADD COLUMN ismodbus boolean not null default true;

  ALTER TABLE devicedata
    ADD COLUMN type bigint default null,

create table rawdata(
	pkid bigserial not null,
	fkdeviceid bigint not null,
	flow decimal(14,2) not null,
	turbidity decimal(14,2) not null,
    chlorine decimal(14,2) not null,
    pressuretransmitter decimal(14,2) not null,
    levelsensor decimal(14,2) not null,
    createdate bigint not null,
	primary key(pkid),
	constraint positive_pkid check (pkid > 0),
	constraint positive_fkdeviceid check (fkdeviceid > 0),
	constraint positive_createdate check(createdate >= 0),
  	constraint foreign_fkdeviceid foreign key(fkdeviceid) references device(pkid)
);
create index index_rawdata_fkdeviceid on rawdata(fkdeviceid);

alter table rawdata add column digitaloutput decimal(14,2) default null;


ALTER TABLE location ADD COLUMN	block varchar(255) default null;
ALTER TABLE location ADD COLUMN	panchayat varchar(255) default null;
ALTER TABLE location ADD COLUMN	village varchar(255) default null;









/* --26-6-24 - All Latest Tables */





create table smsaccount (
  pkid bigserial not null,
  lockversion bigint not null,
  mobile varchar(15) not null,
  password  varchar(100)  not null,
  senderid varchar(6)  not null,
  peid varchar(20) default null,
  fkcreateby bigint not null,
  datecreate bigint not null,
  fkupdateby bigint default null,
  dateupdate bigint default null,
  primary key (pkid),
  constraint positive_pkid check(pkid > 0),
  constraint positive_lockversion check(lockversion >= 0),
  constraint positive_fkcreateby check (fkcreateby > 0),
  constraint positive_fkupdateby check (fkupdateby > 0),
  constraint foreign_fkcreateby foreign key(fkcreateby) references users(pkid),
  constraint foreign_fkupdateby foreign key(fkupdateby) references users(pkid)
  );

create index index_smsaccount_fkcreateby on smsaccount(fkcreateby);
create index index_smsaccount_fkupdateby on smsaccount(fkupdateby);

create table smscontent (
  pkid bigserial not null,
  lockversion bigint  not null,
  fksmsaccountid bigint  not null,
  name varchar(100) not null,
  content text not null,
  fknotificationid smallint not null,
  templateid varchar(20) default null,
  fkcreateby bigint default null,
  datecreate bigint not null,
  fkupdateby bigint default null,
  dateupdate bigint default null,

  primary key (pkid),
  constraint positive_pkid check(pkid > 0),
  constraint positive_lockversion check(lockversion >= 0),
  constraint positive_fksmsaccountid check(fksmsaccountid > 0),
  constraint positive_fkcreateby check (fkcreateby > 0),
  constraint positive_fkupdateby check (fkupdateby > 0),
  constraint foreign_fkcreateby foreign key(fkcreateby) references users(pkid),
  constraint foreign_fkupdateby foreign key(fkupdateby) references users(pkid),
  constraint foreign_fknotificationid foreign key(fknotificationid) references notification(pkid),
  constraint foreign_fksmsaccountid foreign key(fksmsaccountid) references smsaccount(pkid)
);
create index index_smscontent_fkcreateby on smscontent(fkcreateby);
create index index_smscontent_fkupdateby on smscontent(fkupdateby);
create index index_smscontent_fknotificationid on smscontent(fknotificationid);
create index index_smscontent_fksmsaccountid on smscontent(fksmsaccountid);

create table transactionalsms (
  	pkid bigserial not null,
  	lockversion bigint not null,
  	fksmsaccountid bigint  not null,
  	smsto text not null,
  	content text not null,
  	datesend bigint not null,
  	datesent bigint default null,
  	enumstatus smallint not null default 0,
  	numberretrycount bigint not null default 0,
  	error text,
  	messageid varchar(34) default null,
	templateid varchar(20) default null,
  	primary key (pkid),
  	constraint positive_pkid check(pkid > 0),
  	constraint positive_lockversion check(lockversion >= 0),
  	constraint positive_fksmsaccountid check(fksmsaccountid > 0),
  	constraint foreign_fksmsaccountid foreign key(fksmsaccountid) references smsaccount(pkid)
);
create index index_transactionalsms_datesend on transactionalsms(datesend);
create index index_transactionalsms_enumstatus on transactionalsms(enumstatus);
create index index_transactionalsms_numberretrycount on transactionalsms(numberretrycount);
create index index_transactionalsms_fksmsaccountid on transactionalsms(fksmsaccountid);

create table usersession(
	pkid bigserial not null,
	fkuserid bigint not null,
	browser varchar(100) default null,
	operatingsystem varchar(500) default null,
	ipaddress varchar(50) default null,
	logindate bigint not null,
	datedevicecookie bigint not null,
    devicecookie varchar(100) not null,
    datelastlogin bigint default null,
	primary key(pkid),
	constraint positive_pkid check (pkid > 0),
	constraint positive_fkuserid check (fkuserid > 0),
  	constraint foreign_fkuserid foreign key(fkuserid) references users(pkid)
);
create index index_usersession_fkuserid on usersession(fkuserid);


CREATE TABLE public.device (
	pkid bigserial NOT NULL,
	lockversion int8 NOT NULL,
	"name" varchar(500) NOT NULL,
	deviceid varchar(50) DEFAULT NULL::character varying NULL,
	fkcreateby int8 NULL,
	datecreate int8 NOT NULL,
	fkupdateby int8 NULL,
	dateupdate int8 NULL,
	isactive bool DEFAULT true NOT NULL,
	fkactchangeby int8 NULL,
	dateactchange int8 NULL,
	isarchive bool DEFAULT false NOT NULL,
	fkarchiveby int8 NULL,
	datearchive int8 NULL,
	imei varchar(100) NULL,
	CONSTRAINT device_pkey PRIMARY KEY (pkid),
	CONSTRAINT positive_fkactchangeby CHECK ((fkactchangeby >= 0)),
	CONSTRAINT positive_fkarchiveby CHECK ((fkarchiveby >= 0)),
	CONSTRAINT positive_fkcreateby CHECK ((fkcreateby >= 0)),
	CONSTRAINT positive_fkupdateby CHECK ((fkupdateby >= 0)),
	CONSTRAINT positive_lockversion CHECK ((lockversion >= 0)),
	CONSTRAINT positive_pkid CHECK ((pkid >= 0)),
	CONSTRAINT foreign_fkactchangeby FOREIGN KEY (fkactchangeby) REFERENCES public.users(pkid),
	CONSTRAINT foreign_fkarchiveby FOREIGN KEY (fkarchiveby) REFERENCES public.users(pkid),
	CONSTRAINT foreign_fkcreateby FOREIGN KEY (fkcreateby) REFERENCES public.users(pkid),
	CONSTRAINT foreign_fkupdateby FOREIGN KEY (fkupdateby) REFERENCES public.users(pkid)
);
CREATE INDEX index_device_fkactchangeby ON public.device USING btree (fkactchangeby);
CREATE INDEX index_device_fkarchiveby ON public.device USING btree (fkarchiveby);
CREATE INDEX index_device_fkcreateby ON public.device USING btree (fkcreateby);
CREATE INDEX index_device_fkupdateby ON public.device USING btree (fkupdateby);
CREATE INDEX index_device_isactive ON public.device USING btree (isactive);


CREATE TABLE public.deviceparameters (
	pkid bigserial NOT NULL,
	fkdeviceid int8 NOT NULL,
	fkparameterid int8 NOT NULL,
	registername text NOT NULL,
	unit varchar(20) DEFAULT NULL::character varying NULL,
	address varchar(20) DEFAULT NULL::character varying NULL,
	"function" varchar(100) NULL,
	CONSTRAINT deviceparameters_pkey PRIMARY KEY (pkid),
	CONSTRAINT positive_fkdeviceid CHECK ((fkdeviceid > 0)),
	CONSTRAINT positive_fkparameterid CHECK ((fkparameterid > 0)),
	CONSTRAINT positive_pkid CHECK ((pkid > 0)),
	CONSTRAINT foreign_fkdeviceid FOREIGN KEY (fkdeviceid) REFERENCES public.device(pkid),
	CONSTRAINT foreign_fkparameterid FOREIGN KEY (fkparameterid) REFERENCES public.parametermapping(pkid)
);
CREATE INDEX index_deviceparameters_fkdeviceid ON public.deviceparameters USING btree (fkdeviceid);
CREATE INDEX index_deviceparameters_fkparameterid ON public.deviceparameters USING btree (fkparameterid);

CREATE TABLE public.parametermapping (
	pkid bigserial NOT NULL,
	lockversion int8 NOT NULL,
	"name" varchar(500) NOT NULL,
	jsoncode varchar(100) NOT NULL,
	parameterunit varchar(100) NOT NULL,
	fkcreateby int8 NULL,
	datecreate int8 NOT NULL,
	fkupdateby int8 NULL,
	dateupdate int8 NULL,
	isarchive bool DEFAULT false NOT NULL,
	fkarchiveby int8 NULL,
	datearchive int8 NULL,
	CONSTRAINT parametermapping_pkey PRIMARY KEY (pkid),
	CONSTRAINT positive_fkarchiveby CHECK ((fkarchiveby > 0)),
	CONSTRAINT positive_fkcreateby CHECK ((fkcreateby > 0)),
	CONSTRAINT positive_fkupdateby CHECK ((fkupdateby > 0)),
	CONSTRAINT positive_lockversion CHECK ((lockversion >= 0)),
	CONSTRAINT positive_pkid CHECK ((pkid > 0)),
	CONSTRAINT foreign_fkarchiveby FOREIGN KEY (fkarchiveby) REFERENCES public.users(pkid),
	CONSTRAINT foreign_fkcreateby FOREIGN KEY (fkcreateby) REFERENCES public.users(pkid),
	CONSTRAINT foreign_fkupdateby FOREIGN KEY (fkupdateby) REFERENCES public.users(pkid)
);
CREATE INDEX index_parametermapping_fkarchiveby ON public.parametermapping USING btree (fkarchiveby);
CREATE INDEX index_parametermapping_fkcreateby ON public.parametermapping USING btree (fkcreateby);
CREATE INDEX index_parametermapping_fkupdateby ON public.parametermapping USING btree (fkupdateby);
CREATE INDEX index_parametermapping_isarchive ON public.parametermapping USING btree (isarchive);


CREATE TABLE public.screen (
	pkid bigserial NOT NULL,
	lockversion int8 NOT NULL,
	screenname varchar(100) NOT NULL,
	titletext varchar(100) NOT null,
	screendesc varchar(500),
	rowcount int null,
	columncount int null,
	fkcreateby int8 NULL,
	datecreate int8 NOT NULL,
	fkupdateby int8 NULL,
	dateupdate int8 NULL,
	isactive bool DEFAULT true NOT NULL,
	fkactchangeby int8 NULL,
	dateactchange int8 NULL,
	isarchive bool DEFAULT false NOT NULL,
	fkarchiveby int8 NULL,
	datearchive int8 NULL,
	CONSTRAINT screen_pkey PRIMARY KEY (pkid),
	CONSTRAINT positive_fkactchangeby CHECK ((fkactchangeby >= 0)),
	CONSTRAINT positive_fkarchiveby CHECK ((fkarchiveby >= 0)),
	CONSTRAINT positive_fkcreateby CHECK ((fkcreateby >= 0)),
	CONSTRAINT positive_fkupdateby CHECK ((fkupdateby >= 0)),
	CONSTRAINT positive_pkid CHECK ((pkid >= 0)),
	CONSTRAINT foreign_fkactchangeby FOREIGN KEY (fkactchangeby) REFERENCES public.users(pkid),
	CONSTRAINT foreign_fkarchiveby FOREIGN KEY (fkarchiveby) REFERENCES public.users(pkid),
	CONSTRAINT foreign_fkcreateby FOREIGN KEY (fkcreateby) REFERENCES public.users(pkid),
	CONSTRAINT foreign_fkupdateby FOREIGN KEY (fkupdateby) REFERENCES public.users(pkid)
);
CREATE INDEX index_screen_fkactchangeby ON public.screen USING btree (fkactchangeby);
CREATE INDEX index_screen_fkarchiveby ON public.screen USING btree (fkarchiveby);
CREATE INDEX index_screen_fkcreateby ON public.screen USING btree (fkcreateby);
CREATE INDEX index_screen_fkupdateby ON public.screen USING btree (fkupdateby);
CREATE INDEX index_screen_isactive ON public.screen USING btree (isactive);


create table rowmaster(
 	pkid smallint not null,
 	rowname varchar(100) not null,
 	fkscreenid int8 NOT NULL,
 	primary key (pkid),
 	constraint positive_pkid check(pkid > 0),
 	CONSTRAINT positive_fkscreenid CHECK ((fkscreenid > 0)),
 	CONSTRAINT foreign_fkscreenid FOREIGN KEY (fkscreenid) REFERENCES public.screen(pkid)
);
CREATE INDEX index_rowmaster_fkscreenid ON public.rowmaster(fkscreenid);
DROP SEQUENCE IF EXISTS rowmaster_pkid_seq;
CREATE SEQUENCE rowmaster_pkid_seq;
ALTER TABLE rowmaster
ALTER COLUMN pkid SET DEFAULT nextval('rowmaster_pkid_seq');


create table columnmaster(
 	pkid smallint not null,
 	columnname varchar(100) not null,
 	fkscreenid int8 NOT NULL,
 	primary key (pkid),
 	constraint positive_pkid check(pkid > 0),
 	CONSTRAINT positive_fkscreenid CHECK ((fkscreenid > 0)),
 	CONSTRAINT foreign_fkscreenid FOREIGN KEY (fkscreenid) REFERENCES public.screen(pkid)
);
CREATE INDEX index_columnmaster_fkscreenid ON public.columnmaster(fkscreenid);
DROP SEQUENCE IF EXISTS columnmaster_pkid_seq;
CREATE SEQUENCE columnmaster_pkid_seq;
ALTER TABLE columnmaster
ALTER COLUMN pkid SET DEFAULT nextval('columnmaster_pkid_seq');


create table cellmaster(
 	pkid smallint not null,
 	cellvalue varchar(100) not null,
 	fkrowid int8 NOT NULL,
 	fkcolumnid int8 NOT NULL,
 	primary key (pkid),
 	constraint positive_pkid check(pkid > 0),
 	CONSTRAINT positive_fkrowid CHECK ((fkrowid > 0)),
 	CONSTRAINT positive_fkcolumnid CHECK ((fkcolumnid > 0)),
 	CONSTRAINT foreign_fkrowid FOREIGN KEY (fkrowid) REFERENCES public.rowmaster(pkid),
 	CONSTRAINT foreign_fkcolumnid FOREIGN KEY (fkcolumnid) REFERENCES public.columnmaster(pkid)
);
CREATE INDEX index_cellmaster_fkrowid ON public.cellmaster USING btree (fkrowid);
CREATE INDEX index_cellmaster_fkcolumnid ON public.cellmaster USING btree (fkcolumnid);

DROP SEQUENCE IF EXISTS columnmaster_pkid_seq;
CREATE SEQUENCE cellmaster_pkid_seq;
ALTER TABLE cellmaster
ALTER COLUMN pkid SET DEFAULT nextval('cellmaster_pkid_seq');

TRUNCATE TABLE devicediagnosis RESTART IDENTITY;


create table userscreen(
 	pkid smallint not null,
 	fkuserid int8 NOT NULL,
 	fkscreenid int8 NOT NULL,
 	primary key (pkid),
 	CONSTRAINT positive_fkuserid CHECK ((fkuserid > 0)),
 	CONSTRAINT positive_fkscreenid CHECK ((fkscreenid > 0)),
 	CONSTRAINT foreign_fkuserid FOREIGN KEY (fkuserid) REFERENCES public.users(pkid),
 	CONSTRAINT foreign_fkscreenid FOREIGN KEY (fkscreenid) REFERENCES public.screen(pkid)
);
CREATE INDEX index_userscreen_fkscreenid ON public.userscreen(fkscreenid);
CREATE INDEX index_userscreen_fkuserid ON public.userscreen(fkuserid);
DROP SEQUENCE IF EXISTS userscreen_pkid_seq;
CREATE SEQUENCE userscreen_pkid_seq;
ALTER TABLE userscreen
ALTER COLUMN pkid SET DEFAULT nextval('userscreen_pkid_seq');


ALTER TABLE deviceparameters
ADD column min bigint DEFAULT NULL,
ADD column max bigint DEFAULT null,
ADD CONSTRAINT positive_min CHECK ((min > 0)),
ADD CONSTRAINT positive_max CHECK ((max > 0));

ALTER TABLE public.cellmaster ADD column decimal int8 DEFAULT NULL;

ALTER TABLE public.deviceparameters DROP CONSTRAINT positive_max;
ALTER TABLE public.deviceparameters DROP COLUMN max;
ALTER TABLE public.deviceparameters DROP CONSTRAINT positive_min;
ALTER TABLE public.deviceparameters DROP COLUMN min;

ALTER TABLE cellmaster
ADD column min bigint DEFAULT NULL,
ADD column max bigint DEFAULT null,
ADD CONSTRAINT positive_min CHECK ((min > 0)),
ADD CONSTRAINT positive_max CHECK ((max > 0));

ALTER TABLE public.deviceparameters DROP COLUMN "function";
ALTER TABLE public.deviceparameters DROP COLUMN unit;

ALTER TABLE cellmaster
ADD COLUMN unit varchar(20) DEFAULT NULL::character varying NULL,
ADD	COLUMN function varchar(100) NULL;

ALTER TABLE cellmaster
ADD COLUMN zerobuttontext varchar(100) DEFAULT NULL::character varying NULL,
ADD	COLUMN onebuttontext varchar(100) NULL;

ALTER TABLE cellmaster
ADD COLUMN showvaluemessage boolean not null default false;

ALTER TABLE devicediagnosis ADD COLUMN isalarmcalculated boolean not null default false;

ALTER TABLE deviceparameters ADD COLUMN isneedtoalarmlogged boolean not null default false;

create table alaramhistory(
 	pkid bigserial not null,
 	fkdeviceid bigint NOT NULL,
 	fkdeviceparameterid bigint NOT NULL,
 	createdate bigint NOT NULL,
 	resolvedate bigint DEFAULT NULL,
 	noofcount bigint DEFAULT null,
 	primary key (pkid),
 	CONSTRAINT positive_pkid check(pkid >= 0),
 	CONSTRAINT positive_fkdeviceid CHECK ((fkdeviceid > 0)),
 	CONSTRAINT positive_fkdeviceparameterid CHECK ((fkdeviceparameterid > 0)),
 	CONSTRAINT foreign_fkdeviceid FOREIGN KEY (fkdeviceid) REFERENCES public.device(pkid),
 	CONSTRAINT foreign_fkdeviceparameterid FOREIGN KEY (fkdeviceparameterid) REFERENCES public.deviceparameters(pkid)
);
CREATE INDEX index_alaramhistory_fkdeviceid ON public.alaramhistory(fkdeviceid);
CREATE INDEX index_alaramhistory_fkdeviceparameterid ON public.alaramhistory(fkdeviceparameterid);







