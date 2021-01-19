package com.example.cassandra;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.*;

import java.net.InetSocketAddress;

public class TestingConnections {
    public CqlSession session;
    
    public void connect() {
        System.setProperty("sun.security.krb5.debug", "true");
        System.setProperty("dse.sasl.service", "dse_automaton");
        System.setProperty("javax.security.sasl.qop", "auth");

        session = CqlSession.builder()
            .build();
        
        String keyspace = "keyspace1";
        String table = "names";
        this.getData(keyspace, table);
    }
    
    public void getData(String keyspace, String table) {
        ResultSet results = session.execute("select * from keyspace1.names"); 
        for (Row row : results) {
            String firstName = row.getString("first");
            String lastName = row.getString("last");
            System.out.printf("First: %s, Last: %s\n", firstName, lastName);
        }
    }
    public void close() {
            session.close();
    }
    public static void main(String[] args) {
        TestingConnections client = new TestingConnections();
        client.connect();
        client.close();            
    }
}