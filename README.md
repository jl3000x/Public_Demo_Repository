# PPHOLIC Travel Agency #

This is the source code for the demonstration server of ZhenHao Li.

Two applications are implemented:

* [ppholic_server_demo] server side application with RESTful API implemented.

* [ppholic_server_demo_client] Client application based on RESTful API.


As a demo version of a travel agency website, it's implemented with following features:

1. **Product Function:** Browsing, searching, sorting, management.

2. **External Client Utility:** Registration, information update, dedicated shopping cart edition and payment, shopping record. 

3. **Internal User:** Full management utility of product, external client member, and internal user.

4. **RESTful API and Client Application:** RESTful API design and client application implemented based on RESTful API.


For further detail of features and implementation, please refer the summary below and following detail.

* [Website](http://travel.ppholic.tw)

* [Restful Client Website ](http://restful.travel.ppholic.tw/)

* [Server Website Introduction](http://travel.ppholic.tw/website-introdcution)

* [RESTful API Introduction](http://travel.ppholic.tw/rest-api-introdcution)

---

## Technical Background

* [Language]    Java 8
* [Project]     Spring Boot 2.4.5, Maven
* [Server]      AWS Elastic Beanstalk, RDS
* [Framework]   Spring MVC
* [Database]    MySQL 8.0.23, Hibernate
* [Viewer]      Thymeleaf, Bootstrap 5
* [Security]    Spring Security, bcrypt, JWT
* [Aspect]      Aspect J
* [Testing]     Junit 5, Mockito

---

## Implementation Features

* [Structure] Viewer <=> Controller <=> Service <=> DAO <=> Hibernate <=> MySQL on AWS RDS

* [Security_Privilege_Hierarchy] Protected password data by bcrypt and strictly layerized privilege hierarchy by Spring Security.

* [RESTful_API_with_JWT_Authentication] RESTful API with public information and private stateless communication secured by JWT.

* [Aspect_Utility] Logger for the function call information and exception handle by Aspect J.

* [Product_Recommendation_Algo] Latest-Clicked-First to recommend popular items.

* [Table_Display_Sorting_Utility] Implemented by HQL sorting commend.

---

## Future Update Plan

1. Product Comment Utility

2. External Payment API Implementation

3. Client-Preference-Oriented Product Recommendation Algo

---

## DataBase Scheme
![image](https://github.com/jl3000x/Public_Demo_Repository/blob/master/ppholic_server_demo/src/main/resources/static/images/SQL_Scheme.png)
