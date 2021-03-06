# Deployment and environment

This chapter describes the necessary steps to prepare an environment and a deployment.


## Environment test/quality/prod

* **JAVA**: Java 1.8.0_40
* **Application server**:	WildFly-8.2.0.Final
* **Database**:	Neo4j 2.2.0

The Eternity application is deployed to the WildFly server and requests a Datasource to the Neo4j database while 
starting. Therefore you have to setup your database and WildFly. The following configuration is **a** possible solution:

### Java

* Extract/install the java version and set your JAVA_HOME or modify the start script

### Database

* Install Neo4j, user the default port 7474, connect to the website `http://localhost:7474/browser/` and change the
password for the user neo4j
* Retrieve the Neo4j JDBC driver from `https://github.com/neo4j-contrib/neo4j-jdbc` - you might use 
`http://dist.neo4j.org/neo4j-jdbc/neo4j-jdbc-2.0.1-SNAPSHOT-jar-with-dependencies.jar`
* If you want to run the integration tests the application server has to provide an additional datasource 
`Neo4jDSTest`. You should create an additional database (extract a second instance of neo4j) and configure it for a 
different port (`NEO4J_HOME/conf/neo4j-server.properties` e.g. change 7474 to 7476 and 7473 to 7475). Remember to 
change the user password as well
* You can use the same database for dev and tests but please remember to execute the init script after running the tests

### Install base data

* Set `NEO4J_HOME` to your Neo4j database directory
* Run the [Initial Neo4j data script](../database/init-database.sh)  
* Please note, you want to delete constraints see [clear-constraints](../database/clear-constraints.cyp). If a 
constraint is not present the script fails, so uncomment the drops only if the constraint is available or run the 
commands manually in the web ui.
  
### Application server

* If you run a local WildFly / JDK set these parameters to ensure the same behavior
 `-Dfile.encoding=UTF8 -Duser.language=de -Duser.country=DE`
* Extract the WildFly to a directory of your choice
* Start the server with the standalone configuration
* Create user for administration and deployment, e.g. user:pw **admin:admin** with `wildfly-8.2.0.Final/bin/add-user.sh`
* Deploy the JDBC driver, you might use the `http://localhost:9990/console/App.html#deployments`
* Create a datasource to your Neo4j database 
`{name:'Neo4jDS', jndi-name:'java:jboss/datasources/Neo4jDS', connectionUrl:'jdbc:neo4j://localhost:7474/'}`
* Create a test datasource to your Neo4j database, used for the integration tests 
`{name:'Neo4jDSTest', jndi-name:'java:jboss/datasources/Neo4jDSTest', connectionUrl:'jdbc:neo4j://localhost:7476/'}`

* If you want to use a SSL connection you have to use certificates and modify the WildFly configuration.
* A keystore generated as described below can be found at `eternity/additional-files/eternity.jks` 
* A possible setup can be found here: (http://blog.eisele.net/2015/01/ssl-with-wildfly-8-and-undertow.html)
    * `keytool -genkey -alias mycert -keyalg RSA -sigalg MD5withRSA -keystore eternity.jks -storepass secret  -keypass 
    secret -validity 9999`
    * ` <management>
               <security-realms>
               <!-- ... -->
                  <security-realm name="UndertowRealm">
                       <server-identities>
                           <ssl>
                               <keystore path="eternity.jks" relative-to="jboss.server.config.dir" 
                               keystore-password="secret" alias="mycert" key-password="secret"/>
                           </ssl>
                       </server-identities>
                   </security-realm>`
    * `<subsystem xmlns="urn:jboss:domain:undertow:1.2">
           <!-- ... -->
           <server name="default-server">
                <!-- ... -->
                <https-listener name="https" socket-binding="https" security-realm="UndertowRealm"/>`
* This enables you to use a https connection with a self signed certificate. **NOTE** at lest Safari has problems 
with server push if you use such a certificate. Firefox is more tolerant. Certificates with a trusted root CA should 
work.

## Deployment

You have to create the `ear` with maven. You can use a build server, your IDE working with exploded versions, or just
manually copy/deploy the artifact to your application server.

### Deployment artifact

Content of the resulting ear file `eternity-ear.ear`:

    ├── eternity-war.war
    └── META-INF
        ├── application.xml
        ├── MANIFEST.MF
        ├── maven
        │   └── com.prodyna.pac.eternity
        │       └── eternity-ear
        │           ├── pom.properties
        │           └── pom.xml
        └── persistence.xml
        
### How does the deployment work?

Maven executed in the root project creates all sub projects and artifacts.
 
* `mvn clean package` - creates the projects with tests and build the ear
* `mvn clean package -Dmaven.test.skip=true` - creates the projects but skips the tests and build the ear
 
### Using a CI server

If you want to use a CI server you can use a configuration like this:

* `mvn versions:set -DnewVersion=$BUILD_NUMBER` - this sets the version number for the build correctly
* `mvn clean package findbugs:findbugs checkstyle:checkstyle wildfly:deploy javadoc:aggregate scm:tag` - this creates all
the projects run the tests, checkstyle, findbugs, deploy the artifact to the WildFly, create the JavaDoc and also 
creates a tag in the source code management system

* see also [Release](./release.md)  



### Accessing the application

After a successful deployment you can access the application at:  

* http://localhost:8080/eternity
* https://localhost:8443/eternity (if you configured the WildFly to use SSL)
* If you installed the initial data, you can login as admin via `admin:pw`
