create database imageStored;
use imageStored;

create table user(
id_user int primary key auto_increment,
name varchar(50),
email varchar(50) unique,
password varchar(50)
);

create table gallery(
id_gallery int primary key auto_increment,
id_user int,
galery_name varchar (50),
foreign key (id_user) references user(id_user)
);

create table image(
id int primary key auto_increment,
alt varchar(100),
id_gallery int,
url varchar(255),
foreign key (id_gallery) references gallery(id_gallery)
);