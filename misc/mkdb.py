import PackageLoader

import org.neo4j.graphdb.Direction
import org.neo4j.graphdb.GraphDatabaseService
import org.neo4j.graphdb.Node
import org.neo4j.graphdb.Relationship
import org.neo4j.graphdb.RelationshipType
import org.neo4j.graphdb.Transaction as Transaction
import org.neo4j.graphdb.factory.GraphDatabaseFactory as GDBFactory

from WordGraph import RelTypes, DB_PATH

wordsdb = GDBFactory().newEmbeddedDatabase( DB_PATH )

