# Journal de Bord RESTful API
This is a RESTful API implemented using Spring Boot. The API is used by the [Journal de bord web application](https://github.com/Lofaloa/journal-de-bord-spa). 
The documentation can be found at this [location](https://farci-logan.gitbook.io/journal-de-bord/backend/overview).

## Prerequisites
Before starting developing, you should install: Maven and the Java SE 11 Open JDK. If you are working on Ubuntu, you can
install them as follows:

```bash
> sudo apt install openjdk-11-jdk
> sudo apt install maven
```

## Configuration
Here is a minimal configuration file. It is the content of the `src/main/resources/application.yml` file.
```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: <authorization server url>
          jwk-set-uri: <authorization server url>/protocol/openid-connect/certs
  datasource:
    url: <database path>
    driverClassName: <driver>
    username: <database username>
    password: <database username password>
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
  h2:
    console:
      enabled: true

```

## Install and run
You build the application using maven (check the prerequisites).
```bash
> mvn clean install
> java -jar target/<output archive name>.jar
```