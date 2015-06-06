# Release

This chapter describes how a release of Eternity is build.

## How does the build work?

The build uses Maven to create every (sub) project. The master POM describes all modules and know all library versions
available for the project. So there is only one place where the version numbers are stored.

Eternity consists of several functional and technical projects with a strict separation of api and implementation. 
 Implementations and APIs are only allowed to reference other APIs and external libraries. This ensures that we avoid 
  cross references between implementation projects.
  
The ejb/war/ear project depend on the needed implementations and build the specific artifact.

## Release naming

**Version numbers**

|                  | Name           | Major       | Minor                        | Bugfix                            |
-------------------|----------------|-------------|------------------------------|-----------------------------------|
| Description      | Marketing name | API changes | features without API changes | only bugfixes without API changes |
| Example: 1.2.3.4 | 1              | 2           | 3                            | 4                                 |
 

Each features has to be implemented on a separate branch once the first version is released. The feature branch must not
be merged to the trunk as long as the feature is not finished and tested by QA. The name of the branch should be
**b&lt;ISSUE_NR&gt;** from https://github.com/ddrachau/eternity/issues e.g. b22 for issue number 22.
Feature branches will be used as soon as first version is tagged and released.

## How do you perform a release?

You can manually build a release or configure a ci build server.

* You have clone/update your local version of eternity, drop all uncommited changes
* Run `mvn versions:set -DnewVersion=RELEASE_VERSION` in order to set the correct version across the poms
* Run `mvn clean package [findbugs:findbugs checkstyle:checkstyle wildfly:deploy javadoc:aggregate] scm:tag` while `[]` 
 indicate that these parameter are optional. This will create an artifact and tag the code with the previously set 
 version

## Which artifacts exist, format and where to find them

Every project (except the pom projects) create a jar (ejb/war/ear) file in their target directory.

## Which release units are available?

The only release unit is `eternity/eternity-ear/target/eternity-ear.ear`
