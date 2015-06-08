# Architecture

This chapter describes the architecture and some important architectural decisions.

## Which technologies are used?

* Java 8
* JavaEE 7 
* Arquillian
* JaCoCo
* JavaScript
* Bootstrap + JQuery
* AngularJS
* Neo4j
* Apache
* WildFly

## Architectural overview

The implementation of Eternity consists of a server and a client part.

### Server

The server implements the business logic and encapsulates the database against unmanaged changes. There are several 
technical core components as well as functional modules. This strict separation ensures easy future extensions with 
new functional as well as technical modules.
 
On top of the business services exist a REST interface which handles authentication and authorization by using 
provided business methods. 

### Client

The client implementation is written in AngularJS + Bootstrap. It allows the user to login and perform the required 
operations if its authenticated and authorized. Elements which the user is not allowed to call are invisible to the 
current user.

The client calls the servers REST interface and initiates a websocket connection. This connection is retried if it 
drops at some point. If the server calls changing methods like create / update / delete etc. on User, Project, 
Booking an event is sent to the client. If the client currently displays a page where this event is relevant, the ui 
gets reloaded.

### How does the security work?

If you want to use the REST interface, you have to authenticate yourself through the AuthenticationClientService 
via user name and password first. After a successful authentication, you have to send the issued session token in every 
request to authenticate yourself.

If you specify during the login procedure that the client should remember the login, the server also issues a one 
time login token for persistence. Note that this might be a security issue on insecure machines. If the session is 
not available but a remember token is, the client tries to authenticate itself with this token. If the token is still
 valid a new session and remember token get issued.
 
The access to functional operations is also protected through an authorization system. Every user has one role in the
 system (USER, MANAGER or ADMINISTRATOR). If an user is authenticated, the system checks if the user is also 
 authorized to call the requested operation and gets rejected if it is not. 
 

|                  | ADMIN OPERATIONS | MANAGER OPERATIONS | USER OPERATIONS |
-------------------|------------------|--------------------|-----------------|
| ADMINISTRATOR    | x                | x                  | x               |
| MANAGER          |                  | x                  | x               |
| USER             |                  |                    | x               |

### Password usage

The password handling uses an advanced hashing and salting for every password of every user. Base implementation is 
used from: (https://crackstation.net/hashing-security.htm#normalhashing)

### How does the logging component work?

The logging component provides a provider for injecting a logger in your components. Additional an annotation Logging
 is provided. You can annotate classes or methods with `Logging` and an interceptor logs in debug mode the call in 
 and out of the method(s).  

### How does the profiling component work?

The profiling component provides an annotation for marking classes or methods for being monitored. An interceptor 
monitors the annotated methods and stores the execution duration in a profiling JMX Bean. This bean can be integrated
 in an monitoring server. The bean provides duration minimum, maximum and the average as well.
 
### Persistence

All nodes have a technical `id` (UUID) and an optional functional identifier.

* get methods return the searched object or null if the search was empty
* delete methods throw an exception if the instance to be deleted does not exists. Currently if you delete a node, 
all relations and otherwise inconsistent objects are deleted as well. E.g. if you delete an User, all Sessions, 
RememberMes, Bookings for this user and relations are delete as well. This might be dangerous and delete more than 
you intended. You might want to change the deletion to an deactivation in the future.
      
## Database 

### Nodes

There are five nodes:
* User - a concrete user which can log in the system
* Project - a project users can book time for
* Booking - a concrete booking of an user for a project
* Session - a session for a logged in user
* RememberMe - a remember me token for a user

![Neo4j Model](./images/neo-model.png)

### Writing Neo4j services

Services accessing the database call the CypherService to issue the cypher query. The jdbc driver implementation is a
 wrapper around the Neo4j REST-interface.

### Multilevel Booking Index Tree

In the current model we skipped the idea of introducing a multi level indexing structure for booking entries.

To keep the complexity low for the moment we have to monitor the performance for booking operations for many bookings 
over time. After creating several thousands of bookings for an user, the creation time (most time consuming with the 
overlapping checks) increases from about 16 ms to about 30.

If the overall performance should suffer, the following construct could be introduced. The booking service would have 
to be adjusted:

```
CREATE    
    (BRoot:BookingRoot {name:'root'}),
    (BY2014:BookingYear {name: 'Y2014'}),
    (BY2015:BookingYear {name: 'Y2015'}),
    (BYM201412:BookingMonth {name:'Y14M01'}),
    (BYM201501:BookingMonth {name:'Y15M12'}),
    (BYMD20141231:BookingDay {name:'Y14M12D31'}),
    (BYMD20150101:BookingDay {name:'Y15M01D01'}),
    (BYMD20150102:BookingDay {name:'Y15M01D02'}),
    (BYMD20150103:BookingDay {name:'Y15M01D03'}),
    (BRoot)-[:Y2014]->(BY2014),
    (BY2014)-[:M12]->(BYM201412),
    (BYM201412)-[:D31]->(BYMD20141231),
    (BRoot)-[:Y2015]->(BY2015),
    (BY2015)-[:M01]->(BYM201501),
    (BYM201501)-[:D01]->(BYMD20150101),
    (BYM201501)-[:D02]->(BYMD20150102),
    (BYM201501)-[:D03]->(BYMD20150103),
    (BYMD20141231)-[:NEXT]->(BYMD20150101),
    (BYMD20150101)-[:NEXT]->(BYMD20150102),
    (BYMD20150102)-[:NEXT]->(BYMD20150103),
    (BYMD20141231)-[:VALUE]->(Booking1),
    (BYMD20141231)-[:VALUE]->(Booking2),
    (BYMD20141231)-[:VALUE]->(Booking3),
    (BYMD20141231)-[:VALUE]->(Booking4),
    (BYMD20141231)-[:VALUE]->(Booking5),
    (BYMD20141231)-[:VALUE]->(Booking6),
    (BYMD20141231)-[:VALUE]->(Booking7),
    (BYMD20141231)-[:VALUE]->(Booking8),
    (BYMD20141231)-[:VALUE]->(Booking9),
    (BYMD20141231)-[:VALUE]->(Booking10),
    (BYMD20150101)-[:VALUE]->(Booking11),
    (BYMD20150101)-[:VALUE]->(Booking12),
    (BYMD20150101)-[:VALUE]->(Booking13),
    (BYMD20150101)-[:VALUE]->(Booking14),
    (BYMD20150101)-[:VALUE]->(Booking15),
    (BYMD20150102)-[:VALUE]->(Booking15),
    (BYMD20150102)-[:VALUE]->(Booking16),
    (BYMD20150102)-[:VALUE]->(Booking17),
    (BYMD20150102)-[:VALUE]->(Booking18),
    (BYMD20150102)-[:VALUE]->(Booking19),
    (BYMD20150102)-[:VALUE]->(Booking20),
    (BYMD20150102)-[:VALUE]->(Booking21),
    (BYMD20150102)-[:VALUE]->(Booking22),
    (BYMD20150102)-[:VALUE]->(Booking23),
    (BYMD20150102)-[:VALUE]->(Booking24),
    (BYMD20150102)-[:VALUE]->(Booking25),
    (BYMD20150103)-[:VALUE]->(Booking26),
    (BYMD20150103)-[:VALUE]->(Booking27)
    
// whole Range for a specific user
MATCH (root:BookingRoot), (u:User {id:'uuser'})
MATCH startPath=root-[:Y2014]->()-[:M12]->()-[:D31]->startLeaf,
      endPath=root-[:Y2015]->()-[:M01]->()-[:D03]->endLeaf,
      valuePath=startLeaf-[:NEXT*0..]->middle-[:NEXT*0..]->endLeaf,
      values=middle-[:VALUE]->booking-[:PERFORMED_BY]->(u)
RETURN booking,u 

MATCH (root:BookingRoot), (u:User {id:'uuser'})
MATCH commonPath=root-[:Y2015]->()-[:M01]->commonRootEnd,
      startPath=commonRootEnd-[:D01]->startLeaf,
      endPath=commonRootEnd-[:D02]->endLeaf,
      valuePath=startLeaf-[:NEXT*0..]->middle-[:NEXT*0..]->endLeaf,
      values=middle-[:VALUE]->booking-[:PERFORMED_BY]->(u)
RETURN booking,u 

MATCH commonPath=root-[:`2011`]->()-[:`01`]->commonRootEnd,
      startPath=commonRootEnd-[:`01`]->startLeaf,
      endPath=commonRootEnd-[:`03`]->endLeaf,
      valuePath=startLeaf-[:NEXT*0..]->middle-[:NEXT*0..]->endLeaf,
      values=middle-[:VALUE]->event
```