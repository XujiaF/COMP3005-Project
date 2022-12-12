drop table if exists Publisher;
CREATE TABLE Publisher
(
    pub_id    integer PRIMARY KEY,
    pub_name   varchar(300),
    bank_account  varchar(32),
    email     varchar(32),
    phone     varchar(32)
);
insert into Publisher values(1,'Dundurn Press','622200010001','dundurn@gmail.com','89631234');
insert into Publisher values(2,'Taylor & Francis Publishing','622200010002','taylor@gmail.com','89633541');
insert into Publisher values(3,'Saga Press','622200010003','saga@gmail.com','89631222');
insert into Publisher values(4,'Comma Press','622200010004','comma@gmail.com','89631255');


drop table if exists UserInfo;
CREATE TABLE UserInfo
(
    user_id    integer PRIMARY KEY,
    username   varchar(64),
    password  varchar(32),
    email     varchar(32),
    is_owner     varchar(8)
);
insert into UserInfo values(1,'admin','1234qwer','Thomas@gmail.com','Y');
insert into UserInfo values(2,'Steve','1234qwer','Steve@gmail.com','N');

drop table if exists Warehouse;
CREATE TABLE Warehouse
(
    warehouse_id    integer PRIMARY KEY,
    warehouse_address   varchar(128)
);
insert into Warehouse values(1,' 6000 W Van Buren St, Phoenix, AZ 85043');

drop table if exists Genre;
CREATE TABLE Genre
(
    genre_id    integer PRIMARY KEY,
    genre_name   varchar(128)
);
insert into Genre values(1,'Classics');
insert into Genre values(2,'Comic');
insert into Genre values(3,'Detective');
insert into Genre values(4,'Computer');
insert into Genre values(5,'Fantasy');
insert into Genre values(6,'Horror');

drop table if exists Author;
CREATE TABLE Author
(
    author_id    integer PRIMARY KEY,
    author_name   varchar(128)
);
insert into Author values(1,'Michael Connelly');
insert into Author values(2,'Sir Arthur Conan Doyle');
insert into Author values(3,'Ta-Nehisi Coates');
insert into Author values(4,'Leigh Bardugo');
insert into Author values(5,'Arthur Golden');
insert into Author values(6,'Josh Malerman');

drop table if exists Writen_By;
CREATE TABLE Writen_By
(
    author_id    integer,
    ISBN   varchar(128),
    CONSTRAINT wb_pk primary key  (author_id,ISBN),
    foreign key (author_id) references Author(author_id),
    foreign key (ISBN) references Book(ISBN)
);

drop table if exists Book;
CREATE TABLE Book
(
    ISBN      varchar(64) PRIMARY KEY,
    title     character varying(200),
    number_of_pages   integer,
    price     numeric(5, 2),
    warehouse_id     integer,
    pub_id integer,
    genre_id integer,
    foreign key (warehouse_id) references Warehouse(warehouse_id),
    foreign key (pub_id) references Publisher(pub_id),
    foreign key (genre_id) references Genre(genre_id)
);
INSERT INTO book VALUES('978-0307743664','Carrie Mass Market Paperback', 304, 26, 1, 1, 6);
INSERT INTO book VALUES('978-0399155345','The Help Hardcover', 464, 15, 1, 1, 5);
INSERT INTO book VALUES('978-0316485616','CIRCE', 221, 15, 1, 1, 3);




drop table if exists Basket;
CREATE TABLE Basket
(
    basket_id    integer PRIMARY KEY,
    user_id integer,
    status integer,
    foreign key (user_id) references UserInfo(user_id)
);

drop table if exists Basket_books;
CREATE TABLE Basket_books
(
    basket_id    integer not null ,
    ISBN varchar(64) not null,
    CONSTRAINT basket_books_pk primary key  (basket_id,ISBN),
    foreign key (basket_id) references Basket(basket_id),
    foreign key (ISBN) references Book(ISBN)
);

drop table if exists OrderInfo;
CREATE TABLE OrderInfo
(
    order_id    integer not null primary key ,
    user_id integer not null,
    basket_id    integer not null ,
    ship_id    integer not null ,
    ship_status integer not null,
    address_id integer not null,
    create_time timestamp,
    price numeric(5, 2),
    foreign key (user_id) references UserInfo(user_id),
    foreign key (basket_id) references Basket(basket_id),
    foreign key (ship_id) references ShippingService(ship_id),
    foreign key (address_id) references Address(address_id)
);

drop table if exists Address;
CREATE TABLE Address
(
    address_id    integer not null primary key ,
    full_address varchar(64) not null,
    user_id integer,
    foreign key (user_id) references UserInfo(user_id)
);
insert into Address values(1,'8673 W. Walnut Ave. Westmont, IL 60559',2);
insert into Address values(2,'681 Nichols Street Port Charlotte, FL 33952',2);

drop table if exists ShippingService;
CREATE TABLE ShippingService
(
    ship_id    integer not null primary key ,
    company_name varchar(64) not null
);
insert into ShippingService values(1,'Mediterranean Shipping Company');
insert into ShippingService values(2,'Maersk');
insert into ShippingService values(3,'COSCO Shipping Lines');
insert into ShippingService values(4,'Yang Ming Marine Transport Corporation');

drop table if exists store_report;
CREATE TABLE store_report
(
    report_id    integer not null primary key ,
    year  integer,
    month integer,
    quantity integer,
    total_revenue float,
    total_cost float,
    total_profit float
);
insert into store_report values(1,2021,1,23,2224,-2123,101);
insert into store_report values(2,2021,2,33,3224,-2123,1101);
insert into store_report values(3,2021,3,43,2224,-2023,201);


CREATE SEQUENCE bookstore_id START 100;
