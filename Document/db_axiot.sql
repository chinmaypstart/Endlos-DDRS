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

alter table file add column path varchar(5000) not null;

/*14/02/2023*/
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

create table device(
	pkid bigserial not null,
  	lockversion bigint not null,
  	name varchar(500) not null,
  	deviceid varchar(50) default null,
  	modelnumber varchar(100) default null,
  	fkgatewayid bigint not null,
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
  	constraint positive_fkupdateby check (fkupdateby >= 0),
  	constraint positive_fkactchangeby check (fkactchangeby >= 0),
  	constraint positive_fkgatewayid check (fkgatewayid > 0),
  	constraint positive_fkarchiveby check (fkarchiveby >= 0),
  	constraint foreign_fkcreateby foreign key(fkcreateby) references users(pkid),
  	constraint foreign_fkupdateby foreign key(fkupdateby) references users(pkid),
  	constraint foreign_fkactchangeby foreign key(fkactchangeby) references users(pkid),
    constraint foreign_fkarchiveby foreign key(fkarchiveby) references users(pkid),
  	constraint foreign_fkgatewayid foreign key(fkgatewayid) references gateway(pkid)
);
create index index_device_fkcreateby on device(fkcreateby);
create index index_device_fkarchiveby on device(fkarchiveby);
create index index_device_fkupdateby on device(fkupdateby);
create index index_device_fkactchangeby on device(fkactchangeby);
create index index_device_isactive on device(isactive);
create index index_device_fkgatewayid on device(fkgatewayid);

alter table gateway add column fkclientid bigint not null,
	add constraint positive_fkclientid check(fkclientid>0),
	add constraint foreign_fkclientid foreign key(fkclientid) references client(pkid);
create index index_gateway_fkclientid on gateway(fkclientid);

alter table device add column fkclientid bigint not null,
	add constraint positive_fkclientid check(fkclientid>0),
	add constraint foreign_fkclientid foreign key(fkclientid) references client(pkid);
create index index_device_fkclientid on device(fkclientid);

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

  ALTER TABLE device
    ADD COLUMN minvalue decimal(14,2) default null,
    ADD COLUMN maxvalue decimal(14,2) default null,
    ADD COLUMN type int default null;

create table parametermapping(
	pkid bigserial not null,
  	lockversion bigint not null,
  	name varchar(500) not null,
  	jsoncode varchar(100) not null,
  	parameterunit varchar(100) not null,
  	fkcreateby bigint default null,
  	createdate bigint not null,
  	fkupdateby bigint default null,
  	updatedate bigint default null,
  	isarchive boolean not null default false,
  	fkarchiveby bigint default null,
  	datearchive bigint default null,
  	primary key(pkid),
  	constraint positive_pkid check(pkid > 0),
  	constraint positive_lockversion check(lockversion >= 0),
  	constraint positive_fkcreateby check (fkcreateby > 0),
  	constraint positive_fkupdateby check (fkupdateby > 0),
  	constraint positive_fkarchiveby check (fkarchiveby > 0),
  	constraint foreign_fkcreateby foreign key(fkcreateby) references users(pkid),
  	constraint foreign_fkupdateby foreign key(fkupdateby) references users(pkid),
  	constraint foreign_fkarchiveby foreign key(fkarchiveby) references users(pkid)
);
create index index_parametermapping_fkcreateby on parametermapping(fkcreateby);
create index index_parametermapping_fkupdateby on parametermapping(fkupdateby);
create index index_parametermapping_fkarchiveby on parametermapping(fkarchiveby);
create index index_parametermapping_isarchive on parametermapping(isarchive);

create table deviceparameters(
	pkid bigserial not null,
	fkdeviceid bigint not null,
	fkparameterid bigint not null,
	displayname text not null,
	primary key(pkid),
	constraint positive_pkid check (pkid > 0),
	constraint positive_fkdeviceid check (fkdeviceid > 0),
	constraint positive_fkparameterid check (fkparameterid > 0),
  	constraint foreign_fkdeviceid foreign key(fkdeviceid) references device(pkid),
  	constraint foreign_fkparameterid foreign key(fkparameterid) references parametermapping(pkid)
);
create index index_deviceparameters_fkdeviceid on deviceparameters(fkdeviceid);
create index index_deviceparameters_fkparameterid on deviceparameters(fkparameterid);

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

alter table device add column ispump boolean default false;
alter table device add column publishtopic text default null;
alter table device add column publishtopictolowlevel text default null;
alter table device add column publishtopictohighlevel text default null;

ALTER TABLE IF EXISTS public.device DROP COLUMN IF EXISTS fkgatewayid;

ALTER TABLE IF EXISTS public.device DROP COLUMN IF EXISTS minvalue;

ALTER TABLE IF EXISTS public.device DROP COLUMN IF EXISTS maxvalue;

ALTER TABLE IF EXISTS public.device DROP COLUMN IF EXISTS type;
ALTER TABLE IF EXISTS public.device DROP CONSTRAINT IF EXISTS foreign_fkgatewayid;
ALTER TABLE IF EXISTS public.device DROP CONSTRAINT IF EXISTS positive_fkgatewayid;

ALTER TABLE IF EXISTS public.device ADD COLUMN fklocationid bigint default null,
ADD constraint positive_fklocationid check (fklocationid > 0),
ADD constraint foreign_fklocationid foreign key(fklocationid) references location(pkid);
create index index_device_fklocationid on device(fklocationid);

ALTER TABLE deviceparameters ADD COLUMN	longitude varchar(20) default null;
ALTER TABLE deviceparameters ADD COLUMN	latitude varchar(20) default null;

ALTER TABLE location ADD COLUMN	block varchar(255) default null;
ALTER TABLE location ADD COLUMN	panchayat varchar(255) default null;
ALTER TABLE location ADD COLUMN	village varchar(255) default null;

ALTER TABLE device ADD COLUMN communicationbreaktime bigint default null;

ALTER TABLE device ADD COLUMN name varchar(500) not null;