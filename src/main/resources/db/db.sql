create table customer (
    id bigint unsigned auto_increment primary key not null ,
    full_name varchar(100) not null,
    email varchar(100) not null,
    mobile varchar(20) not null,
    address varchar(255),
    create_at datetime not null ,
    update_at datetime not null
)

create unique index customer_email_uindex
	on customer (email);

create unique index customer_mobile_uindex
	on customer (mobile);