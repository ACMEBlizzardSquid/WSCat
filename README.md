## Requirements

1. Glassfish 4
2. JDK 7


## Deploying the Services

1.  Add the `/Services` project to Netbeans

2.  Generate a WAR file for your project by building the project.
    Ensure that a war has been generated at `/Services/dist/WSCat-Services.war`

3.  Open `/Services/Makefile` in a test editor
    - update the `GF4_ADMIN_PORT` variable on line 10 to
        be your Glassfish 4 Admin Port
    - update the `ASAADMIN` variable on line 9 to be
        the path to your Glassfish 4 asaadmin executable

4.  `cd` to the `/Services` directory in a terminal

5.  Run `make deploy`


## Deploying the Composite Application

1.  Add the `/Processes` and `/Applications` projects to Netbeans

2.  Modify the port bindings to reflect your Glassfish 4 Server port
    (the server port, NOT THE ADMIN PORT) in the following files:

      - `/Processes/src/RESTService.wsdl`
      - `/Processes/src/WADLService.wsdl`
      - `/Processes/src/WSDLService.wsdl`

