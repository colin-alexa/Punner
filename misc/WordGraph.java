import java.io.File;
import java.io.IOException;
import java.util.TreeSet;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.PathExpanders;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.tooling.GlobalGraphOperations;
import org.neo4j.kernel.impl.util.FileUtils;

public class WordGraph
{

/// Contains some static data for use in jython code relating to instantiating 
///  the words database, as well as a skeleton for a class wrapping a database session in Java
    public static String DB_PATH = "db/my-test-db";

    private GraphDatabaseService graphDb;
    private PathFinder<Path> allSimplePaths =
	GraphAlgoFactory.allSimplePaths( PathExpanders.forType(RelTypes.OCCURS_WITH), 2 );
	
    private PathFinder<Path> redundantRelationships =
        GraphAlgoFactory.allSimplePaths( PathExpanders.forType(RelTypes.OCCURS_WITH), 1 );
    
    private Node end;

    public static enum RelTypes implements RelationshipType
    { OCCURS_WITH; }
    
    public static enum Labels implements Label 
    { WORD, END; }
  
    public void createDb()
    {   
	System.out.println("Clearing old db");
        clearDb();
        
        System.out.println("allocating new db");
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
        
        
        try ( Transaction tx = graphDb.beginTx() ){
          System.out.println("Making db schema");
          graphDb.schema().constraintFor(Labels.WORD).assertPropertyIsUnique("LEX");
          end = graphDb.createNode(Labels.END);
          
          tx.success();
        }
        registerShutdownHook( graphDb );
    }
    
    public void openDb(){
      graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
      registerShutdownHook( graphDb );
      end = findEndNode();
    }

    public Node findEndNode(){
     Node n;
     try ( Transaction tx = graphDb.beginTx() ){
       ResourceIterator<Node> existing = GlobalGraphOperations.at(graphDb).getAllNodesWithLabel(Labels.END).iterator();
       if ( existing.hasNext() )
         n = existing.next();
       else
         n = graphDb.createNode(Labels.END);
       
       tx.success();
     }
     return n;
    
    }
    
    public Node findOrCreateWordNode( String lex ){
     Node n;
     try ( Transaction tx = graphDb.beginTx() ){
       ResourceIterator<Node> existing = graphDb.findNodesByLabelAndProperty( Labels.WORD, "LEX", lex ).iterator();
       if ( existing.hasNext() )
         n = existing.next();
       else {
         n = graphDb.createNode( Labels.WORD );
         n.setProperty("LEX", lex);
         n.createRelationshipTo(end, RelTypes.OCCURS_WITH).setProperty("WEIGHT", 0.0);
       }
       
       tx.success();
     }
     return n;
    
    }
    
    private static Double addWeights( Double prev, Double tba ){
      prev = Math.ceil(1.0 / prev);
      return 1.00/ (prev + tba);
    }
    
    public void createWordAssociation(String a, String b){
      Node lhs = findOrCreateWordNode(a);
      Node rhs = findOrCreateWordNode(b);
      try (Transaction tx = graphDb.beginTx() ){
        lhs.createRelationshipTo(rhs, RelTypes.OCCURS_WITH);
        tx.success();
      }
    }
    
    public void updateWordAssociation(String a, String b, Double occurences){
    
      try ( Transaction tx = graphDb.beginTx() ){
      
	Node lhs = findOrCreateWordNode(a);
	Node rhs = findOrCreateWordNode(b);
      
	for (Relationship rel : lhs.getRelationships(RelTypes.OCCURS_WITH)){
	  if ( rel.getOtherNode(lhs).equals(rhs) ) {
	    rel.setProperty("WEIGHT",
			    addWeights((Double) rel.getProperty("WEIGHT"), occurences));
			    
	    tx.success();
	    return;
	  }
	}     
	lhs.createRelationshipTo(rhs, RelTypes.OCCURS_WITH).setProperty("WEIGHT", 1 / occurences);
	
	tx.success();
      }
      return;
    }
    
    private void updateWeight(Node lhs, Node rhs){
      Double oldWeight = 0.0;
      Double unweighted = 0.0;
      for (Relationship r : lhs.getRelationships()){
        if (r.getOtherNode(lhs).equals(rhs)){
          if (r.hasProperty("WEIGHT")) {
            oldWeight += (Double) r.getProperty("WEIGHT");
            System.out.format("%d, %d: weighted is %d", lhs.getId(), rhs.getId(), r.getId());
          }
          else { 
            unweighted += 1.0;
          }
          r.delete();
        }
      }
      Relationship weighted = lhs.createRelationshipTo(rhs, RelTypes.OCCURS_WITH);
      if (oldWeight == 0.0)
        weighted.setProperty("WEIGHT", 1.0 / unweighted );
      else
        weighted.setProperty("WEIGHT", addWeights( oldWeight, unweighted));
    }
    
    public TreeSet<String> wordsLike( String startWord ){
      TreeSet<String> closeWords = new TreeSet<String>();
      
      try ( Transaction tx = graphDb.beginTx() ){
	Node start = findOrCreateWordNode( startWord );
	for (Path p : allSimplePaths.findAllPaths(start, end) ){
	  for (Node n : p.nodes()){
	    if (! n.equals(end) ) 
	      closeWords.add((String) n.getProperty("LEX"));
	  }
	}
	
	tx.success();
      }
      return closeWords;
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

    public void shutDown()
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
