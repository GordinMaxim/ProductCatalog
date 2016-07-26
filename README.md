# Product Catalog

## Необходимые интсрументы

Убедитесь, что у вас установлены:
 - Tomcat 8 
 - MySQL или MariaDB полседних версий
 - maven 3.3.9
 - Java 1.8

Также перед запуском сервлета требуется создать базу данных __testdb__ с пользователем __root__ и паролем __test__ и таблицы _category_ и _product_ (вы можете поменять настройки БД в файле _src/main/resources/META-INF/persistence.xml_) :

``` sql 
CREATE TABLE category
( id INT(11) NOT NULL AUTO_INCREMENT,
name VARCHAR(255) NOT NULL,
PRIMARY KEY (id)
);
ALTER TABLE category ADD CONSTRAINT uq_category_name UNIQUE (name);

CREATE TABLE product
( id INT(11) NOT NULL AUTO_INCREMENT,
cat_id INT(11) NOT NULL,
name VARCHAR(255) NOT NULL,
price DECIMAL(16,2) NOT NULL,
PRIMARY KEY (id),
INDEX cat_id (cat_id),
FOREIGN KEY (cat_id) REFERENCES category(id) ON DELETE CASCADE
); 
```

Заполните таблицы некоторыми значениями:

``` sql 
insert into category (name) values ("category 1");
insert into category (name) values ("category 2");
insert into category (name) values ("category 3");
insert into category (name) values ("category 4");

insert into product (cat_id, name, price) values (1, "cat1_pname1", 11.11);
insert into product (cat_id, name, price) values (1, "cat1_pname2", 121.322);
insert into product (cat_id, name, price) values (2, "cat2_pname1", 2211.1222);
insert into product (cat_id, name, price) values (2, "cat2_pname2", 324256);
insert into product (cat_id, name, price) values (3, "cat3_pname1", 0.567);
insert into product (cat_id, name, price) values (3, "cat3_pname2", 54.21);
insert into product (cat_id, name, price) values (4, "cat4_pname1", 99999.1);
insert into product (cat_id, name, price) values (4, "cat4_pname2", 222.22);
```

Примечание: наполнить таблицу _product_ можно после запуска сервлета из веб-интерфейса

## Установка

1. Клонируйте репозиторий https://github.com/GordinMaxim/ProductCatalog.git или скачайте исходный код архивом и распакуйте 1
2. В корневом каталоге __ProductCatalog__ выполните команду _mvn clean install_ 2
3. Полученный .war-файл  в каталоге __ProductCatalog/target__  разверните в контейнере сервлетов Tomcat 3
4. Перейдите по ссылке http://localhost:8080/Catalog/ 4

Вместо сборки сервлета в консоли, можно импортировать проект в какую-либо IDE (Eclipse или Intellij IDEA) и развернуть сервлет в интегрированном в IDE контейнере сервлетов