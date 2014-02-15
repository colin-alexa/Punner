import java.io.File;
import java.io.IOException;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.impl.util.FileUtils;

public class WordGraph
{

/// Contains some static data for use in jython code relating to instantiating 
///  the words database, as well as a skeleton for a class wrapping a database session in Java
    public static String DB_PATH = "db/my-test-db";

    GraphDatabaseService graphDb;

    public static enum RelTypes implements RelationshipType
    {
        OCCURS_WITH
    }

    void createDb()
    {
        clearDb();
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
        registerShutdownHook( graphDb );

        // START SNIPPET: transaction
        try ( Transaction tx = graphDb.beginTx() )
        {
            // Database operations go here
            


            tx.success();
        }
    }

    private void clearDb()
    {
        try
        {
            FileUtils.deleteRecursively( new File( DB_PATH ) );
        }
        catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
    }

    void removeData()
    {
        try ( Transaction tx = graphDb.beginTx() )
        {
            

            tx.success();
        }
    }

    void shutDown()
    {
        System.out.println();
        System.out.println( "Shutting down database ..." );
        graphDb.shutdown();
    }

    private static void registerShutdownHook( final GraphDatabaseService graphDb )
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                graphDb.shutdown();
            }
        } );
    }
}
