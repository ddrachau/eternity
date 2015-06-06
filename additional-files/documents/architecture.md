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
* Neo4J
* Apache
* WildFly

## Architectural overview

The implementation of Eternity consists of a server and a client part.

### Server

The server implements the business logic and encapsulates the database against unmanaged changes. There are several 
technical core components as well as functional modules. This strict separation ensures easy future extensions with 
new functional as well as technical modules.
 
On top of the business services exist a REST interface which handles authentication and authorization by using 
provides business methods. 

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

### How does logging work?

The logging component provides a provider for injecting a logger in your components. Additional an annotation Logging
 is provided. You can annotate classes or methods with `Logging` and an interceptor logs in debug mode the call in 
 and out of the method(s).  

### How does profiling work?

The profiling component provides an annotation for marking classes or methods for being monitored. An interceptor 
monitors the annotated methods and stores the execution duration in a profiling JMX Bean. This bean can be integrated
 in an monitoring server. The bean provides duration minimum, maximum and the average as well.

## Database nodes

There are five nodes:
* User - a concrete user which can log in the system
* Project - a project users can book time for
* Booking - a concrete booking of an user for a project
* Session - a session for a logged in user
* RememberMe - a remember me token for a user

![Neo4j Model](./images/neo-model.png)


