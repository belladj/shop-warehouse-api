# Shop Warehouse Management System with Java Spring Boot
```Java 17, Spring Boot 3.5.3, Maven Builder, H2 Database```

### Features:
- Basic Create, Read, Update, and Delete (CRUD) functions for warehouse items
- Find items by their name, size, or color
- Bulk item creation
- Bulk item amount change
- Checkout item function (Removes items in the warehouse equal to items being purchased and calculates the total price)
- Exception handling for unavailable item cases
- RESTful API implementation
- Persistent local database with H2 database console

## Running the Application
1. Obtain application folder through ```git clone``` or extracted .zip folder
```
git clone https://github.com/belladj/shop-warehouse-api.git
```
2. Redirect into repository
```
cd shop-warehouse-api
```
3. Install packages with ```mvn clean install```
4. Run package with ```mvn spring-boot:run```
5. If no errors detected, application should work in ```localhost:8080```
### Accessing Database
1. To access database console, go to ```localhost:8080/h2-console```
2. Enter these credentials, then click 'Connect' after ensuring it was correct from 'Test Connection'
```
JDBC URL: jdbc:h2:~/itemdb
User Name: test
Password: test
```
### API Testing
The main API platform used for this project is Postman, so it is assumed as the default for testing purposes.

# Application Design
## 1. Data Models 
Warehouse inventory is classified as ```Item.java``` in the ```modal``` folder, with these attributes:
```
id - Item ID (Long, automatically generated for each new Item created)
name - Item Name (String)
size - Item Size (String)
color - Item Color (String)
price - Item Price (String)
stock - Amount of item currently available (Integer)
```
Checkout functionality has its own DTO as its input parameter and its own response result.
> ```ItemCheckoutDTO.java```
```
id - Item ID (Long)
amount -  Amount of item with the associated ID (Integer)
```
> ```ItemCheckoutResponse.java```
```
itemList - List of items purchased (List<Item>)
amount -  Total amount of item purchased (Integer)
totalPrice - Total price of the items (String)
```
Price is classified as String for ease of Update function to modify only certain attributes. Stock/Amount is classified as Integer under assumption it is always entered.
## 2. Repository - ```ItemRepository.java```
Using JPARepository, filtering for specific categories (name/size/color) are done in backend rather than frontend for performance purposes. Functions added:
- ```findByNameContainingIgnoreCase```: Search items containing certain string (case insensitive)
- ```findBySize```: Search items based on size
- ```findByColor```: Search items based on color
## 3. Services - ```WarehouseService.java```
Primary functions:
 - Get Items: ```getAll, getItemById, getItemsByName, getItemsBySize, getItemsByColor```
 - Add Items: ```createItem (single), createItems (multiple)```
 - Update Item: ```updateItem```
 - Add Item Amount: ```stock```
 - Checkout Item: ```checkout```
 - Delete Item: ```deleteItem```

For non-create functionalities with batch item edits such as ```stock``` and ```checkout```, inserting the modified items into a list then performing ```saveAll()``` is used instead of individual ```save()``` for better performance.
## 4. Controller - ```WarehouseController.java```
Uses ```/items``` as Request Mapping.
### Endpoints
1. GET
- ```/items```: get all items
- ```/items/search/{name}```: get items containing a specific string
- ```/items/size/{size}```: get items of a certain size
- ```/items/color/{color}```: get items of a certain color
2. POST
- ```/items```: create new item (requires Item request body)
- ```/items/batch```: create multiple new items (requires Item list request body)
- ```/items/stock```: update multiple items' stock (requires ItemCheckoutDTO list request body)
- ```/items/checkout```: checkout items (requires ItemCheckoutDTO list request body)
3. PUT
- ```/items/{id}```: update an item with a certain ID (requires Item request body)
4. DELETE
- ```/items/{id}```: delete an item with a certain ID
### cURL Examples
cURL example to get all items containing "dress" in its name:
```
curl --location 'localhost:8080/items/search/dress'
```
cURL example to add a red, M-sized dress shirt item costing 200 and 10 in stock:
```
curl --location 'localhost:8080/items' \
--header 'Content-Type: application/json' \
--data '{ "name": "Dress Shirt", "size": "M", "color": "red", "price": "200", "stock": 10}'
```
## 5. Exception Handling
Exception is handled on cases of Item ID being not found, or invalid amount during checkout process (the amount exceeds the remaining item in stock).
- ```InvalidItemException.java``` class in ```exception``` folder
- ```ExceptionHandler``` for InvalidItemException class in ```WarehouseController.java```
## 6. Database
The database used for this application is stored as a local file to maintain persistent data storage that is updated whenever the project is run, instead of a temporary in-memory storage that resets the database for each rerun. This file format is specified in the ```spring.datasource.url``` and setting the ```spring.jpa.hibernate.ddl-auto``` to ```update```.
> ```application.properties```
```
# H2 database init
spring.datasource.url=jdbc:h2:file:~/itemdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;AUTO_RECONNECT=TRUE
spring.datasource.driver-class-name=org.h2.Driver
...
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
```
Explanations for ```spring.datasource.url``` additional properties:
- ```DB_CLOSE_DELAY=-1```: Prevents database automatically closing when connection is lost
- ```DB_CLOSE_ON_EXIT=FALSE```: Prevents database automatically closing when application is shut down
- ```AUTO_RECONNECT=TRUE```: Database will attempt to immediately reconnect when connection is lost
