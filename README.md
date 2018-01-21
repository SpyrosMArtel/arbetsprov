# arbetsprov
A bookstore REST API implementation as a work assignment.

## Getting started
To get a copy of the project up and running on your local machine follow these instructions:
### Prerequisites
If you are not using any IDE which has Maven bundled, you will have to install it. To do so, you can find more information at: [Installing Maven](https://maven.apache.org/install.html)
### Installing
Open a terminal and follow along... :)  
*All commands listed from here on are for Linux platforms.*
1. Clone the repository:
```shell
git clone https://github.com/SpyrosMArtel/arbetsprov.git
```
2. Create the database schema at your database:
```shell
CREATE SCHEMA bookstore;
```
3. Change the database URL in the bookstore.yml:
Navigate to the ```arbetsprov``` directory, edit the file ```bookstore.yml``` and change the **IP address**(192.168.56.100) and the **PORT**(3306) at line 10:
```url: jdbc:mysql://192.168.56.100:3306/bookstore```, to point to your database.
4. Run the project
```shell
cd arbetsprov
mvn clean package
cp target/bookstore-0.0.1.jar .
java -jar bookstore-0.0.1.jar server bookstore.yml
```

At this moment, the REST API should be running on your machine at:
```shell
http://localhost:8080
```
## Running the tests
Should you wish to run the unit tests, type the following:
```shell
mvn test
```
## Api endpoints
>Books:
```shell
    GET     /api/books
    GET     /api/books/search/{searchString}
    GET     /api/books/{bookId}
    POST    /api/books/buy
    POST    /api/books/{quantity}
    DELETE  /api/books/{bookId}
    PUT     /api/books/{bookId}
```
>Cart:
```shell
    GET     /api/cart
    POST    /api/cart
    DELETE  /api/cart/{bookId}
```
>Swagger:
```shell
    GET     /api/swagger
    GET     /api/swagger.json
    GET     /api/swagger.yaml
```
By visiting the Swagger endpoint you can interact the REST API in a simple and agnostic way.
## Built with
* [Dropwizard](http://www.dropwizard.io/0.9.3/docs/) - The web framework used
* [Dropwizard-swagger](https://github.com/federecio/dropwizard-swagger) - Swagger support for Dropwizard
* [Maven](https://maven.apache.org/) - Dependency Management
