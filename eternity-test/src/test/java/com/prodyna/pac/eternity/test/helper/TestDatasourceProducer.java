package com.prodyna.pac.eternity.test.helper;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Produces the neo4j test datasource.
 */
public class TestDatasourceProducer {

    /**
     * Produces the neo4j test datasource
     *
     * @param ip the InjectionPoint of the requesting class
     * @return the neo4j test datasource
     * @throws NamingException if the ds cannot be looked up
     */
    @Produces
    public DataSource loggerProducer(final InjectionPoint ip) throws NamingException {

        InitialContext ctx = new InitialContext();
        return (DataSource) ctx.lookup("java:jboss/datasources/Neo4jDSTest");

    }

}
