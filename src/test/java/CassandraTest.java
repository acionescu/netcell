import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;


public class CassandraTest {
    public static void main(String[] args) {
	
	String node = "localhost";
	
	Cluster cluster = Cluster.builder()
	         .addContactPoint(node)
	         .build();
	   Metadata metadata = cluster.getMetadata();
	   System.out.printf("Connected to cluster: %s\n", 
	         metadata.getClusterName());
	   for ( Host host : metadata.getAllHosts() ) {
	      System.out.printf("Datacenter: %s; Host: %s; Rack: %s\n",
	         host.getDatacenter(), host.getAddress(), host.getRack());
	   }
	   
	   
    }
}
