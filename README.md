WSCat
========

# Structure

_/soen487/wscat/marfcat_

Connor's marfcat API

Dependencies:
 * /WebContent/WEB-INF/lib/marfcat.jar
 * /WebContent/WEB-INF/lib/marf.jar

_/soen487/wscat/parser_

Web crawler's parsers used to gather WSDL and generate the 
MARFCAT-IN

Dependencies:
 * /WebContent/WEB-INF/lib/rexcrawler.jar

_/soen487/wscat/service_

SOAP services. They may contain main entry point for testing or local execution

Dependecies:
 * /lib/wsdlretriever-service.jar (Classes generated from WSDLRetriever service)

# Notes

WSCat uses an internal service (WSDLRetriever), this confuses Glassfish if we include
the interface for WSDLRetrieve as a library. Therefore those are placed in /lib which
is not imported in the WAR file.

We may need to consider to split the project, having one WAR file per service but it
depends on the requiremnts for BPEL.
