+----------------------------+
YMER MEDIATION EXAMPLE
+----------------------------+

CONTENT
================================

1. PROJECT STRUCTURE
2. WHAT IT DEMONSTRATES
3. HOW TO RUN


PROJECT STRUCTURE .
================================

This is sample mediation project that can be genereated using mediation-set maven archetype.
Project is multimodule maven project with several maven modules:

|---------->Mediation Example
			|------> DPS Utility
			|------> Hello World Handler
			|------> Hello World Flow
			|------> Event based example

	1. DPS Utility is simple util project offering ejb interface that can be used 
	   to insert record into Versant database, since all versant cli tools are read only.
	   It is used in the event based example test to insert record into database that will be
	   the target of the hello world flow.
	
	2. Hello World Handler is multi-module maven project with one module for handler model,
	   another for handler code and ear packaging.
	
	3. Hello World Flow is multi-module project containing only one module for flow model. Flow model is located in
	   etc/model/fbp_flow and mediation configuration for this flow is located in etc/model/med_configuration
	   
	4. Event based example is project with arquillian test that will bind all this together. It will deploy all mediation components,
	   deploy hello world handler and hello world flow & mediation configuration for it. It will then insert test record into database
	   using DPS created from dps army test model. In this case we are inserting instance of General, into namespace Army, where general's
	   name will be generated using UUID, and it's last name will be first four characters of the UUID. 
	   Once the record is inserted into database, it will invoked mediation using event based interface by means of sending
	   the Mediation Task Request.
	   In order to run example you need to have Versant database installed on your machine (expects it as localhost) and database called
	   med_example created on it. Host and database name is configurable through the main pom file.
	   To download Versant and create database see: https://arm1s11-eiffel004.eiffel.gic.ericsson.se:8443/nexus/content/sites/tor/data-persistence-service-api/getting_started/install_versant.html	       


WHAT IT DEMONSTRATES
================================

Project demonstrates full event based mediation example including test handler and flow. It also verifies event driven interface of mediation.
All components are versioned using ymer-sdk project, single version hiding the complexity and inter-layer dependencies. All mediation components and
mediation dependencies are versioned in ymer-sdk-bom pom file.
Running this example will demonstrate full vertical slice of mediation, unrelated to any use-case and can be used as start from scratch project.
For the incoming request HelloWorld handler will create HelloWorld.txt file inside target directory
and the test will assert that file exists.




HOW TO RUN
================================

 1. Install versant database according to: https://eselivm2v238l.lmera.ericsson.se/nexus/content/sites/tor/data-persistence-service-api/getting_started/install_versant.html
 2. Create med_example database using: https://eselivm2v238l.lmera.ericsson.se/nexus/content/sites/tor/data-persistence-service-api/getting_started/install_versant.html
 3. Run mvn clean install from within the project
 4. Logs can be found in the eventbased-mediation-example/target/jboss-eap-6.1/standalone/log/server.log


