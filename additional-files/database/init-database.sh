#! /bin/bash
set -e

if [[ -z "$NEO4J_HOME" ]]; then
	echo "Make sure you set \$NEO4J_HOME before running this script"
	echo "e.g. export NEO4J_HOME=\"/path/to/neo4j-community-2.2.0\""
	exit 1
fi

${NEO4J_HOME}/bin/neo4j status
if [ $? -ne 0 ]; then
	echo "Neo4j not started. Run ${NEO4J_HOME}/bin/neo4j start before running this script"
fi

${NEO4J_HOME}/bin/neo4j-shell --file clear-constraints.cyp
${NEO4J_HOME}/bin/neo4j-shell --file clear.cyp
${NEO4J_HOME}/bin/neo4j-shell --file constraints.cyp
${NEO4J_HOME}/bin/neo4j-shell --file nodes.cyp
