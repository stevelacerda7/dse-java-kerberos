package com.example.cassandra;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Metrics;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.policies.DCAwareRoundRobinPolicy;
import com.datastax.driver.core.policies.LatencyAwarePolicy;
import com.datastax.driver.core.policies.RoundRobinPolicy;
//import com.datastax.driver.core.policies.DefaultRetryPolicy;
import com.datastax.driver.core.QueryOptions;
import com.datastax.driver.core.ConsistencyLevel;
//import com.datastax.driver.core.policies.RetryPolicy;
import com.datastax.driver.core.policies.DowngradingConsistencyRetryPolicy;
import com.datastax.driver.core.policies.WhiteListPolicy;
import java.util.Collection;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.SimpleStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.BoundStatement;


public class TestingConnections {
    private Cluster cluster;
    private Session session;

    public void connect(String nodeIp, String user, String pwd) {
            System.out.printf("User: %s", user);
            System.out.printf("Password: %s", pwd);
            
            Collection<InetSocketAddress> whiteList = new ArrayList<InetSocketAddress>();
//            whiteList.add(new InetSocketAddress("10.101.32.110", 9042));
//            whiteList.add(new InetSocketAddress("10.101.33.200", 9042));

            cluster = Cluster.builder()
                    .addContactPoint(nodeIp)
//                    .withSSL()
//                    ssl options to add to runtime env:
//                    -Djavax.net.ssl.trustStore=/Users/stevelacerda/.ssh/lacerda_sa_5_1_truststore.jks
//                    -Djavax.net.ssl.trustStorePassword=cassandra
                    
                    
                    
                    .withQueryOptions(new QueryOptions()
                        .setConsistencyLevel(ConsistencyLevel.LOCAL_QUORUM)
                        .setSerialConsistencyLevel(ConsistencyLevel.LOCAL_SERIAL))
//                    .withRetryPolicy(new CustomRetryPolicy(1,1,1))
//                    .withLoadBalancingPolicy(new WhiteListPolicy(DCAwareRoundRobinPolicy.builder().withLocalDc("SearchAnalytics").withUsedHostsPerRemoteDc(1).allowRemoteDCsForLocalConsistencyLevel().build(), whiteList))
//                    .withLoadBalancingPolicy(new WhiteListPolicy(new RoundRobinPolicy(), whiteList))

                    .withCredentials("cassandra", "cassandra")
                    .build();

            Metadata metadata = cluster.getMetadata();
            Metrics metrics = cluster.getMetrics();
            System.out.printf("Connected hosts: %s\n", metrics.getKnownHosts().getValue());

            System.out.printf("Connected to cluster: %s\n", metadata.getClusterName());

            for (Host host : metadata.getAllHosts()) {
                    System.out.printf("Datacenter: %s, Host: %s, Rack: %s\n", host.getDatacenter(), host.getAddress(), host.getRack());
            }

            session = cluster.connect();
                        
            String keyspace = "keyspace1";
            String table = "names";
            this.getData(keyspace, table);
//            this.writeData(keyspace, table);
            
        
    
    
    }
    
    public void getData(String keyspace, String table) {
//        ResultSet results = session.execute("select first,last from " + keyspace + "." + table + " where solr_query='{\"q\":\"first:steve\"}'");
        ResultSet results = session.execute("select * from keyspace1.names"); 
        for (Row row : results) {
            String firstName = row.getString("first");
            String lastName = row.getString("last");
            System.out.printf("First: %s, Last: %s\n", firstName, lastName);
        }
    }
    
//    public void writeData(String keyspace, String table) {
//        Integer i = 0;
//        
//        BatchStatement batch = new BatchStatement(BatchStatement.Type.UNLOGGED);
//        PreparedStatement preparedInsert = session.prepare("INSERT INTO " + keyspace + "." + table + " (partition_key) " + "VALUES (:partition_key)");
//                
//        while (i < 5000) {
//            try{
////                ResultSet results = session.execute("insert into " + keyspace + "." + table + " (partition_key) values ('" + i.toString() + "')");
//                batch.add(preparedInsert.bind(i.toString()));
////                Integer resultsLength = results.toString().length();
////                String row = results.all().toString();
////                    System.out.printf("Results: %s\n", row);
////                    RetryPolicy.RetryDecision.tryNextHost(ConsistencyLevel.LOCAL_ONE);
//            } catch(Exception E) {
////                     do nothing
//                System.out.printf("Exception: %s", E.getLocalizedMessage());
//            }
//
//            i = i + 1;
//        }
//        
//        session.execute(batch);
//    }    
    
    public void writeData(String keyspace, String table) {
        Integer i = 0;
        String first = "Steve";
        
        PreparedStatement ps = session.prepare("INSERT INTO keyspace1.testnames (first,last,middle) VALUES (:first, :last, :middle)");
        
        while (i < 10000000) {
            try{
                BoundStatement bs = ps.bind().setString("first", first).setString("last", i.toString() + "54564654165416541654654165465416546541654").setString("middle", i.toString() + "02554641231315454541516541654165");
                session.execute(bs);

//                String query = String.format("insert into %s.%s (first) values (%s)", keyspace, table, first);
//                ResultSet results = session.execute(query);
//                Integer resultsLength = results.toString().length();
//                String row = results.all().toString();
//                System.out.printf("Results: %s\n", row);
//                RetryPolicy.RetryDecision.tryNextHost(ConsistencyLevel.LOCAL_ONE);
            } catch(Exception E) {
//                     do nothing
                System.out.printf("Exception: %s", E.getLocalizedMessage());
            }

            i = i + 1;
        }
    }

    public void close() {
            cluster.close();
    }

    public static void main(String[] args) {
            if (args.length > 0) {
                    System.out.printf("IP is: %s\nUsername: %s\nPassword: %s\n", args[0], args[1], args[2]);
                    String ip = args[0];
                    String username = args[1];
                    String password = args[2];

                    System.out.printf("Connecting to %s\n", ip);
                    TestingConnections client = new TestingConnections();
                    client.connect(ip, username, ip);
                    client.close();			
            }
    }
}