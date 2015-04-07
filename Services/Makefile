#
# Makefile for content generation and packaging
#
# Rodrigo

HOSTNAME=`hostname`
WSCAT_DOC="doc/wscat"

ASADMIN=asadmin
GF4_ADMIN_PORT=48480
WAR_FILE=/home/shake0/WSCat.war

doc:
	javadoc -d ${WSCAT_DOC} -classpath WebContent/WEB-INF/lib/rexcrawler.jar -sourcepath "src:WebContent/WEB-INF/lib/rexcrawler.jar" -subpackages "org.rexcrawler:soen487.wscat"

# Script used to generate the Jar from the WDSL
# The service imported must be deployed in glassfish
#

deploy:
	cp ${WAR_FILE} $(basename ${WAR_FILE})-WSDL.war
	cp ${WAR_FILE} $(basename ${WAR_FILE})-REST.war
	cp ${WAR_FILE} $(basename ${WAR_FILE})-WADL.war
	mkdir WEB-INF
	sed "s|<param-value>WSDL</param-value>|<param-value>REST</param-value>|g" WebContent/WEB-INF/web.xml > WEB-INF/web.xml
	jar -uf $(basename ${WAR_FILE})-REST.war WEB-INF/web.xml
	sed "s|<param-value>REST</param-value>|<param-value>WADL</param-value>|g" WebContent/WEB-INF/web.xml > WEB-INF/web.xml
	jar -uf $(basename ${WAR_FILE})-WADL.war WEB-INF/web.xml
	sed "s|<param-value>WADL</param-value>|<param-value>WSDL</param-value>|g" WebContent/WEB-INF/web.xml > WEB-INF/web.xml
	jar -uf $(basename ${WAR_FILE})-WSDL.war WEB-INF/web.xml
	rm -fr WEB-INF
	${ASADMIN} --port ${GF4_ADMIN_PORT} deploy --contextroot WSCat-WSDL $(basename ${WAR_FILE})-WSDL.war
	${ASADMIN} --port ${GF4_ADMIN_PORT} deploy --contextroot WSCat-REST $(basename ${WAR_FILE})-REST.war
	${ASADMIN} --port ${GF4_ADMIN_PORT} deploy --contextroot WSCat-WADL $(basename ${WAR_FILE})-WADL.war

undeploy:
	${ASADMIN} --port ${GF4_ADMIN_PORT} undeploy WSCat-WSDL
	${ASADMIN} --port ${GF4_ADMIN_PORT} undeploy WSCat-REST
	${ASADMIN} --port ${GF4_ADMIN_PORT} undeploy WSCat-WADL

clean:
	rm -fr ${WSCAT_DOC}

.PHONY: clean doc
