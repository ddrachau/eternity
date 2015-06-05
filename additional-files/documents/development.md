# Development

This chapter describes which steps are necessary and which rules apply for continue developing in Eternity.

## Source Code Repository - Where are the sources located?

GitHub is used for code management. The repository can be found here:
https://github.com/ddrachau/eternity.git

## Issue Tracking - How are issues handled?

Issues are tracked in GitHub:  
https://github.com/ddrachau/eternity/issues

## Which skills are needed if you want to continue the development

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

## How are the sources organized?

We use a Maven multi project layout with several function/technical modules and concrete wrapper projects for the jars, 
ejb, ear and war. In general there are api (without impl at the end) and implementations with and 'impl' at the end of
the name.

**common pom module (eternity-modules-common-pom):**
This module consists of a common pom-project for the client and service api/impl projects. Here you define the
libraries which should be available in the kind of child projects.

        └── pom.xml

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

**ejb / war / ear**:
These projects just wrap other libraries and in case of the war project also provide the web client.

        ├── pom.xml
        ├── src
        │   └── main (optinal)
        │       ├── webapp (optional)
        │       └── resources (optional)
        └── target

## Standards für Names und Namespaces (z.B. Packages, Klassen, Exceptions, Interfaces, Services, Artefakte)

## Standards für Code Style

* File encoding is `UTF-8`


## Standards für Tests und automatisierte Tests
