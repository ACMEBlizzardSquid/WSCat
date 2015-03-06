# 
# Script used to generate the Jar from the WDSL
# The service imported must be deployed in glassfish
#

HOSTNAME=`hostname`

WSDLRetriever:
	wsimport -clientjar wsdlretriever-service.jar -d WebContent/WEB-INF/lib "http://${HOSTNAME}:8080/WSCat/WSDLRetrieverService?wsdl"
	rm -fr WebContent/WEB-INF/lib/soen487
