# Spring Reactive WebClient /w WebFlux

### Overview

This sample shows how to use build high performance, asynchronous microservices using WebClient. The demo will use a reactive stream API to query an external service to retrieve a JSON document, extract a list of names at a particular JPath, and then call an external banner generating service to convert them into ASCII art before creating a consolidated concatenated view to the caller. 

### Run the Demo

* Import the root of the repo into your favorite Java IDE
* Run the application application
* Access http://localhost:8080 
 

### Things to try out 
* Examine the source code to see the use of fluent API exposed by the WebClient that is used to build up a reactive pipeline. Notice how the results of previous operations are piped into the next.
* Notice how new WebClient requests are made inside the pipeline that are converted into a Mono (analog to a Future), forming a collection events (Flux).
* Trying inserting a new stage to convert text to uppercase  
 
### Resources to Learn More:
* TBD