package com.example.cassandra;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.*;

import java.net.InetSocketAddress;

public class TestingConnections {
    public CqlSession session;

    public void connect(String nodeIp, String user, String pwd) {
            System.out.printf("User: %s", user);
            System.out.printf("Password: %s", pwd);
            
            session = CqlSession.builder()
                    .addContactPoint(new InetSocketAddress("1.2.3.4", 9042))
                    
                    .build();
            
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
            session.close();
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