# lagom-template
Building Reactive Java 8 application with Lagom framework. This is a reference mini banking application which persist events in Cassandra Db. Here we are using embedded Cassandra to persist events and embedded kafka for publishing and subscribing between microservices.

# Prerequisites
1. Java 1.8
2. Maven 4.0

# Getting the Project
https://github.com/vimalmishrra/lagom-java-mbank-master.git

####Create executable jar: 
`mvn package -Dmaven.skip.test=true`

####Command to start the project

`mvn lagom:runAll`

####Testing the project

`Open banking.html in internet explorer

## Json Formats for different Rest services are mentioned below :

#### 1. Create New Netbanking User:

Route(Method - POST) : `localhost:9000/api/netbanking`

Rawdata(json): 
    {
	"id": "1",
	"name": "Vimlendu",
	"genre": "9767394478"
    }


#### 2. Update Netbanking User:

Route(Method - PUT) : `localhost:9000/api/update-netbanking/:id`
    

#### 3. Delete Netbanking User:

Route(Method - DELETE) : `localhost:9000/api/delete-netbanking/:id`
    

#### 4. Get Netbanking User details:

Route(Method - GET) : `localhost:9000/api/netbanking/:id`

