# Development

This chapter describes which steps are necessary and which rules apply for continue developing in Eternity.

## Source Code Repository - Where are the sources located?

GitHub is used for source code management. The repository can be found here:  
https://github.com/ddrachau/eternity.git

## Issue Tracking - How are issues handled?

Issues are tracked in GitHub:  
https://github.com/ddrachau/eternity/issues

## Which skills are needed if you want to continue the development?

* Java (EE) developer
* Java Script know-how
* GIT
* Cypher query language

## Which software is required to develop?

For setting up your development/test/production environment see 
[Deployment and environment](./deployment.md)

* You can use a development environment of your choice.
    * the base development was performed with IntelliJ
* Java 8 (1.8.0_40)
* Maven (3.2.5)
* Neo4J (2.2.0) 
* WildFly (8.2.0.Final) + with Neo4j JDBC Connector

## Definition of done

Your change to the project is **done** if you comply with the following rules:
* your code compiles
* your code has JavaDoc, no Checkstyle and FindBugs errors
* your have tests for your change
* all tests are green
* the build is possible
* the CI server does not complain

## How are the sources organized?

We use a Maven multi project layout with several function/technical modules and concrete wrapper projects for the jars, 
ejb, ear and war. In general there are api (without impl at the end) and implementations with and 'impl' at the end of
the name.

**common pom module (eternity-modules-common-pom):**  
This module consists of a common pom-project for the client and service api/impl projects. Here you define the
libraries which should be available in the kind of child projects.

        └── pom.xml

**common base module:**
These are the base projects with code which is technical and available for all modules of this kind.
* eternity-modules-common-client
* eternity-modules-common-client-impl
* eternity-modules-common-service
* eternity-modules-common-service-impl

        ├── pom.xml
        ├── src
        │   └── main
        │       ├── java
        │       │   └── com
        │       │       └── prodyna
        │       │           └── pac
        │       │               └── eternity
        │       │                   └── common
        │       │                       ├── client
        │       │                       │   └── impl
        │       │                       └── service
        │       │                           └── impl
        │       └── resources (optional)
        └── target


**common module (e.g. eternity-modules-common-helper):**  
These modules are technical components which can be used by every implementation. The implementations are only allowed
to reference the API projects.
        
        ├── pom.xml
        ├── src
        │   └── main
        │       ├── java
        │       │   └── com
        │       │       └── prodyna
        │       │           └── pac
        │       │               └── eternity
        │       │                   └── common
        │       │                       └── helper
        │       └── resources (optional)
        └── target
        
**functional module (e.g. eternity-modules-authentication):**  
These modules consists of client/server api and implementation projects. 
* eternity-modules-authentication-client
* eternity-modules-authentication-client-impl
* eternity-modules-authentication-service
* eternity-modules-authentication-service-impl

        ├── pom.xml
        ├── src
        │   └── main
        │       ├── java
        │       │   └── com
        │       │       └── prodyna
        │       │           └── pac
        │       │               └── eternity
        │       │                   └── authentication
        │       │                       ├── client
        │       │                       │   └── impl
        │       │                       └── service
        │       │                           └── impl
        │       └── resources (optional)
        └── target

**test module (e.g. eternity-test):**  
This module consists of all the tests for this project.

        ├── pom.xml
        ├── src
        │   └── test
        │       ├── java
        │       │   └── com
        │       │       └── prodyna
        │       │           └── pac
        │       │               └── eternity
        │       │                   └── test
        │       │                       ├── helper
        │       │                       └── same structure as in the modules to test
        │       └── resources
        └── target

**ejb / war / ear:**  
These projects just wrap other libraries and in case of the war project also provide the web client.

        ├── pom.xml
        ├── src
        │   └── main (optinal)
        │       ├── webapp (optional)
        │       └── resources (optional)
        └── target

## Default names and namespaces, encoding

* packages:
    * the default package name is `com.prodyna.pac.eternity`
    * the default package name for the test classes is `com.prodyna.pac.eternity.test`
    * the package names for the files is derived from the project name (eternity-modules-authentication-client-impl ->
    `com.prodyna.pac.eternity.authentication.client.impl`), (eternity-modules-common-helper -> 
    `com.prodyna.pac.eternity.common.helper`), ...
* classes / interfaces:
    * Interfaces have a normal name like `com.prodyna.pac.eternity.authentication.client.AuthenticationClientService` 
    whereas the implementation looks like 
    `com.prodyna.pac.eternity.authentication.client.impl.AuthenticationClintServiceImpl`
* exceptions:
    * Exceptions are named after the case like 
    `com.prodyna.pac.eternity.common.model.exception.functional.DuplicateTimeBookingException`. Functional exceptions 
     should be caught by the callers, while technical exceptions are runtime exceptions and usually do not have to be
     handled.
* nodes / associations
    * Nodes are named after their use case, e.g. a user -> User, booking -> Booking.
    * Associations are written in upper cases and may consists of underscores. They are verbs like, `ASSIGNED_TO` or 
     `PERFORMED_BY`
* encodin: `UTF-8`


## Defaults for tests

Tests are located in the `eternity-test` project. This project has no restrictions for the dependencies.

* Integration tests use Arquillian and require a running WildFly at localhost:8080 and an admin user `dmin:admin`
* Tests have the same package structure like the class under test but with the folowing prefix: 
`com.prodyna.pac.eternity.test`.
* Client tests require an own proxy definition like 
`com.prodyna.pac.eternity.test.authentication.client.impl.AuthenticationClientServiceProxy`
* Service tests start with the clearance and creation of test data for the test. Some tests clean or create test data
on their own.
