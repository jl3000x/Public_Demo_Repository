#### PPHOLIC Travel Agency ####

This is the website as the demonstration server of ZhenHao Li.
As a demo version of a travel agency website, it's implemented with following features:

1. Product Function: Browsing, searching, sorting, management.

2. External Client Utility: Registration, information update, dedicated shopping cart edition and payment, shopping record. 

3. Internal User: Full management utility of product, external client member, and internal user.

4. RESTful API and Client Application: RESTful API design and client application implemented based on RESTful API.


For further detail of features and implementation, please refer the summary below and following detail.

* [Website](http://travel.ppholic.tw)

* [Website Introduction](http://travel.ppholic.tw/website-introdcution)

* [RESTful API Introduction](http://travel.ppholic.tw/rest-api-introdcution)



### Technical Background

* [Language]    Java 8
* [Project]     Spring Boot 2.4.5, Maven
* [Framework]   Spring MVC
* [Database]    MySQL 8.0.23, Hibernate
* [Viewer]      Thymeleaf, Bootstrap 5
* [Security]    Spring Security, bcrypt, JWT
* [Aspect]      Aspect J
* [Server]      AWS Elastic Beanstalk, RDS



### Implementation Features

* [Structure] <Viewer> <=> <Controller> <=> <Service> <=> <DAO> <=> <Hibernate> <=> <MySQL on AWS RDS>

* [Security_Privilege_Hierarchy] Protected password data by bcrypt and strictly layerized privilege hierarchy by Spring Security.

* [RESTful_API_with_JWT_Authentication] RESTful API with public information and private stateless communication secured by JWT.

* [Aspect_Utility] Logger for the function call information and exception handle by Aspect J.

* [Product_Recommendation_Algo] Latest-Clicked-First to recommend popular items.

* [Table_Display_Sorting_Utility] Implemented by HQL sorting commend.



### Future Update Plan

1. Product Comment Utility

2. External Payment API Implementation

3. Client-Preference-Oriented Product Recommendation Algo



### Website Map
![image](../static/images/SQL_Scheme.png)



### Website Map

-- Home --  Member Register
        |
        --  Product Center  --  Product Page
        |
        --  Member Center   --  Shopping Cart
                            |
                            --  Shopping Record
                            |
                            --  User Center     --  Product Management System
                                                |
                                                --  Member Management System
                                                |
                                                --  Manager Management Area     --  User Management System
                                                |
                                                --  System Management Area



### Management Privilege Summary

                Member          Member                      Member          Product
                Information     Shopping Cart/Record        Privilege       Information
                C/R/U/D         C/R/U/D                     C/R/U/D         C/R/U/D
------------------------------------------------------------------------------------------------------------------------
1. Member       O/O/O/O         N/O/O/X                     N/O/X/X         X/O/X/X
2. Employee     O/O/O/O         N/O/O/O                     N/O/O/O         O/O/O/O
3. Manager      O/O/O/O         N/O/O/O                     N/O/O/O         O/O/O/O
4. Admin        O/O/O/O         N/O/O/O                     N/O/O/O         O/O/O/O
5. Others       O/X/X/X         N/X/X/X                     N/X/X/X         X/O/X/X


                User            User                        User
                Information     Shopping Cart/Record        Privilege
                C/R/U/D         C/R/U/D                     C/R/U/D
------------------------------------------------------------------------------------------------------------------------
1. Member       X/X/X/X         N/X/X/X                     N/X/X/X
2. Employee     X/O/O/O         N/O/O/X                     N/O/X/X
3. Manager      O/O/O/O         O/O/O/O                     N/O/O/O
4. Admin        O/O/O/O         O/O/O/O                     N/O/O/O
5. Others       X/X/X/X         N/X/X/X                     N/X/X/X