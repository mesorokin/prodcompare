# Product Comparison Service
This is an implementation of Product Comparison Service
The aim of this service is to provide endpoint which helps the end customer to decide which website or retail shop
they can use to buy their product by comparing data from different providers.

## How to Run Service
The service is a standard Spring Boot application.
Java 12+ is required to run this service.
Main class is
```
com.sorokin.ProductComparisonApplication
```
Maven build is also deploying a docker image
```
mvn clean install
```

## Assumptions
During development of this service next assumptions were taken:
- there is no additional validation on the uniqueness of product names within category.
If somebody tries to create a product with the same name, the same category and the same seller multiple times, 
duplicated entries will be created.
- sellers with the same names are the same (Seller's name isn't case-sensitive)
- categories with the same name are the same (Category's name isn't case-sensitive)

Future development of this service could be introducing validation of the products entries.
Extracting columns 'Category' and 'Seller' to another tables  and introducing relations between tables.
