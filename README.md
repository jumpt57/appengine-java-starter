# AppEngine Java Starter

The goal of this project is to showcase a java application 
hosted on Google Cloud Platform and running on Google AppEngine
Standard with everything needed.

## Why standard ?
To summarize Standard intent is to run for free or at very low cost, 
where you pay only for what you need and when you need it. For example, 
your application can scale to 0 instances when there is no traffic.

For more information visit : https://cloud.google.com/appengine/docs/the-appengine-environments

Also it provides the easiest way to transition from a standard servlet based application
on a server to the Java 8 App Engine serverless service which is also
servlet based.

## What does it contains ?

The project is developed in Java 8.

### Profiles

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
* Avoid direct constructor calls and factories and replace them 
with the standard @Inject annotation
* Remove the need for servlet XML configuration for Endpoints or filters through 
 a web.xml (but it can be added for specifics use cases)
* Create our own annotations to do AOP (for authentication of user or cron)
* Inject components easily and create beans with the @Provied annotation
* Use JPA with little configuration (still needs persistence.xml)
* Automatically register new Endpoints at startup
* Allow to switch between testing modules, local modules and deployed modules
* Allow to create singleton managed by the container
* and more

For more information : https://github.com/google/guice/wiki/Motivation

### E2E or Functional testing

In order to create functional testing (also called e2e testing) a goal of mine was 
to be able to start the server through a java test. After a lot of research through 
the official AppEngine documentation it became apparent that it was mandatory to use :

```
@RunWith(DevAppServerTestRunner.class)
@DevAppServerTest(BaseDevAppServerTestConfig.class)
``` 
with 
```
LocalServiceTestHelper.class
```

In order to have an example of an e2e test you can refer to :
```
com.github.jumpt57.endpoints.HelloWorldEndpointTest.class
```

To simplify the big part happens in :
```
com.github.jumpt57.endpoints.EndpointConfig.class
```
which extends BaseDevAppServerTestConfig.class and require
to override getSdkRoot(), getAppDir() and getClasspath() in order
for the runner to find the AppEngine Java SDK, the war to start and
the classpath to load all of the dependencies.

### AOP authentication and interceptors

In order to handle the authentication to the server there is two things. First of all
there is there is an Authenticator to validate the Oauth token of the user.

But I also wanted a way to validate the user rights like Spring Security, with an annotation.

Guice provides a way to do it by using AOP which stands for Aspect Oriented Programming.

https://github.com/google/guice/wiki/AOP

It is a way of adding behavior through custom annotation.

I have added two annotations :
* one to validate the domain the user @Secured
* the second to validate that the endpoint is called by a cron @Cron

The annotation is bind to his specific MethodInterceptor through the 
InterceptorsModule and the bindInterceptor method.

The MethodInterceptor contains a method called just before the actual
method, in can either proceed or throw an exception.

That way you can create whatever method you want to add behavior to your code.

### Guice Google module

In order for the application to be tested, launched locally and deployed. The
application is provided with different Guice modules. The ones that are
specific are for the Google Service. In the GCP we can use a lot of
services such as GCS, Datastore, BigQuery and so on. But depending on what
we want to achieve the way those services can be used in the code can
be different for example between the local run and in the GCP. In the GCP
credentials are handled by the container. Not locally which means that their
are not built the same way. Locally or during tests we might want to create
the service using a service account. 

Well in the GuiceListener we have GoogleModuleFactory.get(). Depending if it
is the local environment (see the environment section) it will inject the GoogleLocalModule 
or the GoogleDeployModule. The first one uses the builder with setCredentials and the other 
one the getDefaultInstance.

This system can be used to target different GCP project even in production or to use different 
quota.

Moreover the is also the TestingGoogleModule which can be used to do the same
or inject mock (with mockito).

### Bean validation

In order to validate bean I chose to use bean validation jsr 303 specification. Which allows to
validate Java Classes through annotations like Hibernate. In order to do so I use a 
specific dependency :
```
https://github.com/xvik/guice-validator
```

With the ImplicitValidationModule which allow to use annotations 
like @NotBlank (see : com.github.jumpt57.models.Message).

### JPA with Cloud SQL

In order for the application to communicate with Cloud SQL with need a couple of things.
First of all I chose to use Hibernate.

In order to configure Hibernate I need to create a persistence.xml with all the correct values for :
* hibernate.connection.url (different for local use and in production)
* hibernate.connection.username
* hibernate.connection.password

I also need to tell App Engine to load the correct Driver by putting this in
the appengine-web.xml :
```
<use-google-connector-j>true</use-google-connector-j>
```

For the database URL it is different if you want to contact the DB locally or in production :

#### Locally 

It uses the Cloud SQL proxy to avoid opening the database to the world :
```
jdbc:mysql://@/<DATABASE_NAME>?unix_socket=/cloudsql/<INSTANCE_CONNECTION_NAME>
```
Proxy : https://cloud.google.com/sql/docs/mysql/quickstart-proxy-test
You need to download the correct one for you OS and start it :
```
./cloud_sql_proxy -instances=<INSTANCE_CONNECTION_NAME>=tcp:3306
```

#### In App Engine :

In appengine the url is a little bit different because it uses the Google Java Driver :
```
jdbc:google:mysql://<INSTANCE_CONNECTION_NAME>/<DATABASE_NAME>?tcpKeepAlive=true
```

#### Use the EntityManager

To interact with the DB after that you only have to call :
```
@Inject
private EntityManager em;
```

### Environments

### Resources loading in AppEngine

## To do
* Add documentation about environment
* Add documentation about resources loading in AppEngine
* Add documentation to explain the dependencies

## Dependencies

The application is shipped with a lot of dependencies from testing to API and DI.

### Testing

* assertj
* testng
* mockito
* appengine-testing
* appengine-api-stubs
* appengine-tools-sdk
* junit
* json-path

### Logging
* slf4j-api
* slf4j-jdk14

### Utils
* Jackson 
* lombok
* mapstruct
* reflections
* feign
* guava
* guava-retrying

### Javax
* servlet-api
* javax.inject
* javax.el
* hibernate-validator
* hibernate-validator-annotation-processor
* guice-validator

### Google API
* google-cloud-storage

### AppEngine
* endpoints-framework V2
* endpoints-framework-guice
* appengine-api-1.0-sdk
