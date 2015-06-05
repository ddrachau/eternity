# Deployment and environment

This chapter describes the necessary steps to prepare a release environment and a deployment.


## Deployment artifact

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
        
## How does the deployment work?

<h4>Continous Integegration</h4>
Die Projekte können in beliebige CI-Umgebungen mit Mavenunterstützung integriert werden. Für die Entwicklung wird ein Jenkinsserver verwendet.

http://nozdormu.ffm.drachau.de:8888