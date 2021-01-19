package com.example.cassandra;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.*;

import java.net.InetSocketAddress;

public class TestingConnections {
    public CqlSession session;
    
    public void connect(String nodeIp, String user, String pwd) {
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
        String ip = "10.101.32.187";
        String username = "username";
        String password = "password";
        System.out.printf("Connecting to %s\n", ip);
        TestingConnections client = new TestingConnections();
        client.connect(ip, username, password);
        client.close();            
    }
}