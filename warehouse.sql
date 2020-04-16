drop database if exists warehouse;
create database if not exists warehouse;
use warehouse;
SET GLOBAL time_zone = '+3:00';
create table if not exists customer(
id int  unique auto_increment,
nameClient varchar(20),
adress varchar(20),
primary key(id));
create table if not exists orders(
id int unique auto_increment,
nameClient varchar(20),
nameProdus varchar(20),
quantity int,
primary key(id));
create table if not exists products(
id int unique auto_increment,
nameProdus varchar(20),
quantity float,
price float, 
primary key(id));
create table if not exists bills(
id int unique auto_increment,
nameProdus varchar(20),
nameClient varchar(20),
totalPrice float, 
primary key(id));
