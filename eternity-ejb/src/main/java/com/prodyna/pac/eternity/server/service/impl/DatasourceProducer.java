package com.prodyna.pac.eternity.server.service.impl;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Produces the neo4j datasource.
 */
public class DatasourceProducer {

    /**
     * Produces the neo4j datasource
     *
     * @param ip the InjectionPoint of the requesting class
     * @return the neo4j datasource
     * @throws NamingException if the ds cannot be looked up
     */
    @Produces
    public DataSource datasourceProducer(final InjectionPoint ip) throws NamingException {

        InitialContext ctx = new InitialContext();
        return (DataSource) ctx.lookup("java:jboss/datasources/Neo4jDS");

    }

}
