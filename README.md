## Requirements

1. Glassfish 4
2. JDK 7

## Deploying the Services

1.  Generate a WAR file for your project by building the project.
    Ensure that a war has been generated at `/Services/dist/WSCat-Services.war`

2.  Open `/Services/Makefile`
    - update the `GF4_ADMIN_PORT` variable on line 10 to
        be your Glassfish 4 Admin Port
    - update the `ASAADMIN` variable on line 9 to be
        the path to your Glassfish 4 asaadmin executable

3.  Run `make deploy`