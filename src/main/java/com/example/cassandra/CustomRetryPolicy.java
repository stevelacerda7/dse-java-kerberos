/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.cassandra;
import com.datastax.driver.core.policies.RetryPolicy;
import com.datastax.driver.core.policies.RetryPolicy.RetryDecision;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.WriteType;
import com.datastax.driver.core.Cluster;
/**
 *
 * @author stevelacerda
 */

public class CustomRetryPolicy implements RetryPolicy {

    private final int readAttempts;
    private final int writeAttempts;
    private final int unavailableAttempts;
    
    public void init(Cluster cluster) {
        return;
    }
    
    public void close() {
        return;
    }

    public CustomRetryPolicy(int readAttempts, int writeAttempts, int unavailableAttempts) {
        this.readAttempts = readAttempts;
        this.writeAttempts = writeAttempts;
        this.unavailableAttempts = unavailableAttempts;
    }
    

    @Override
    public RetryDecision onReadTimeout(Statement stmnt, ConsistencyLevel cl, int requiredResponses, int receivedResponses, boolean dataReceived, int rTime) {
        if (dataReceived) {
            return RetryDecision.ignore();
        } else if (rTime < readAttempts) {
            return RetryDecision.retry(cl);
        } else {
            return RetryDecision.rethrow();
        }
    }

    @Override
    public RetryDecision onWriteTimeout(Statement stmnt, ConsistencyLevel cl, WriteType wt, int requiredResponses, int receivedResponses, int wTime) {
        if (wTime < writeAttempts) {
            return RetryDecision.retry(cl);
        }
        return RetryDecision.rethrow();
    }

    @Override
    public RetryDecision onUnavailable(Statement stmnt, ConsistencyLevel cl, int requiredResponses, int receivedResponses, int uTime) {
        System.out.print("OnUnavailable");
                if (uTime < unavailableAttempts) {
            return RetryDecision.retry(ConsistencyLevel.ONE);
        }
        return RetryDecision.rethrow();
    }
    
    public RetryDecision onRequestError(Statement stmnt, ConsistencyLevel cl, Exception e, int nbRetry) {
        System.out.print("OnUnavailable");
                if (nbRetry < unavailableAttempts) {
            return RetryDecision.retry(ConsistencyLevel.ONE);
        }
        return RetryDecision.rethrow();
    }
}
