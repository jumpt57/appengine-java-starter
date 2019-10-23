# AppEngine Java Starter

The goal of this project is to showcase a java application 
hosted on Google Cloud Platform and running on Google AppEngine
Standard with everything needed.

#### Why standard ?
To summarize Standard intent is to run for free or at very low cost, 
where you pay only for what you need and when you need it. For example, 
your application can scale to 0 instances when there is no traffic.

For more information visit : https://cloud.google.com/appengine/docs/the-appengine-environments

### What does it contains ?

The project is developed in Java 8.

#### Profiles

It uses Maven 3 as his build automation tool.

There is 8 different profiles :
* local : intended to be used for local development
* dev : intended to be used with a dev GCP project
* staging : intended to be used with a staging GCP project
* prod  : intended to be used with a production GCP project
* debug : intended to be used with the local profile, allows
to debug the application in local by using a JMX connection listening on port
5005 with a IntelliJ Remote configuration
* deployment-manager : intended to deploy the conf-deployment.yaml in the WEB-INF directory
* windows : intended to be used with de deployment-manager to launch the correct command
* unix : intended to be used with de deployment-manager to launch the correct command

Example of use : 
```
mvn clean install appengine:run -DskipTests -Plocal,debug
```

This command, will clean compile install dependencies, then with start the application
locally with the local profile and also with debug activated.

```
mvn clean install -Pdev,deployment-manager
```
This command will launch the deployment of GCP resources define in the conf-deployment.yaml 
but targeting the dev GCP project

#### Dependencies

The application is shipped with a lot of dependencies from testing to API and DI.

#### Testing

* assertj
* testng
* mockito
* appengine-testing
* appengine-api-stubs
* appengine-tools-sdk
* junit
* json-path

#### Logging
* slf4j-api
* slf4j-jdk14

#### Utils
* Jackson 
* lombok
* mapstruct
* reflections
* feign
* guava
* guava-retrying

#### Javax
* servlet-api
* javax.inject
* javax.el
* hibernate-validator
* hibernate-validator-annotation-processor
* guice-validator

#### Google API
* google-cloud-storage

#### AppEngine
* endpoints-framework V2
* endpoints-framework-guice
* appengine-api-1.0-sdk

### Useful commands 

Start with :
```
mvn clean appengine:run -Plocal
```

Access API Explorer with :
```
http://localhost:8080/_ah/api/explorer
```

Start debug with :
```
mvn clean appengine:run -Plocal,debug
```

Deploy :
```
mvn clean appengine:deployAll -Pdev
```

Deploy GCP resources :
```
mvn -Pdeployment-manager -Pdev
```

Launch tests :
```
mvn test -Plocal
```

### Google Endpoints with Guice

Why use Google Endpoints V2 and not Spring Boot ?
* Full integration with Google OAuth2 authentication
* Full integration with Google Stackdriver monitoring service
* Integrate the OpenAPI specification
* Annotation based declaration of endpoints
* No XML configuration needed
* Really cheap (0-2M calls to the API = $0.00)

Full documentation here : https://cloud.google.com/endpoints/

In order for the application to be as easy as possible to work on it was obvious
to integrate Google Endpoints V2 framework with the Dependency Injection Framework 
Guice. Guice combined with Endpoints allow to :
* Avoid direct constructor calls and factories and replace same 
with the standard @Inject annotation
* Remove the need for servlet XML configuration for Endpoints or filters
* Create our own annotations to do AOP (for authentication of user or cron)
* Inject components easily and create beans with the @Provied annotation
* Use JPA with little configuration (still needs persistence.xml)
* Automatically register new Endpoints at startup
* Allow to switch between testing modules, local modules and deployed modules
* Allow to create singleton managed by the container
* and more

For more information : https://github.com/google/guice/wiki/Motivation