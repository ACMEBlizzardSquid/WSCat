#
# Makefile for content generation and packaging
#
# Rodrigo

HOSTNAME=`hostname`
WSCAT_DOC="doc/wscat"

doc:
	javadoc -d ${WSCAT_DOC} -classpath WebContent/WEB-INF/lib/rexcrawler.jar -sourcepath "src:WebContent/WEB-INF/lib/rexcrawler.jar" -subpackages "org.rexcrawler:soen487.wscat"

# Script used to generate the Jar from the WDSL
# The service imported must be deployed in glassfish
#

WSDLRetriever:
	wsimport -clientjar wsdlretriever-service.jar -d WebContent/WEB-INF/lib "http://${HOSTNAME}:8080/WSCat/WSDLRetrieverService?wsdl"
	rm -fr WebContent/WEB-INF/lib/soen487

clean:
	rm -fr ${WSCAT_DOC}

.PHONY: clean doc
