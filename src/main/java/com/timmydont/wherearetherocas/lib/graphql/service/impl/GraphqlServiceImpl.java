package com.timmydont.wherearetherocas.lib.graphql.service.impl;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.timmydont.wherearetherocas.lib.graphql.service.GraphqlService;
import com.timmydont.wherearetherocas.lib.graphql.service.GraphqlWiringService;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import graphql.schema.idl.errors.SchemaProblem;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.timmydont.wherearetherocas.lib.utils.LoggerUtils.error;

public class GraphqlServiceImpl implements GraphqlService {

    private static final String SCHEMA_ACTIVE = "/schema.graphqls";

    private final Logger logger = Logger.getLogger(getClass());

    private GraphQL graphQL;

    public GraphqlServiceImpl(GraphqlWiringService wiringService) {
        // get graphQL schema
        String schema = getRawSchema();
        if (StringUtils.isBlank(schema)) {
            error(logger, "unable to read schema, check errors");
            return;
        }
        // validate wiring service
        if(wiringService == null) {
            error(logger, "the wiring service is null, everything will fail");
            return;
        }
        // build graphQL schema
        TypeDefinitionRegistry typeRegistry;
        try {
            typeRegistry = new SchemaParser().parse(schema);
        } catch (SchemaProblem e) {
            error(logger, e, "unable to compile schema, check errors");
            return;
        }
        try {
            GraphQLSchema graphQLSchema = new SchemaGenerator().makeExecutableSchema(typeRegistry,
                    wiringService.getWiring());
            graphQL = GraphQL.newGraphQL(graphQLSchema).build();
        } catch (Exception e) {
            error(logger, e, "unexpected error while creating GraphQL from schema %s", SCHEMA_ACTIVE);
        }
    }

    /**
     * Read schema and return it as a String
     *
     * @return the schema as a String value
     */
    private String getRawSchema() {
        try {
            InputStream is = getClass().getResourceAsStream(SCHEMA_ACTIVE);
            if (null == is) {
                error(logger, "unable to read graphql schema from file %s, service not initialized", SCHEMA_ACTIVE);
            } else {
                return CharStreams.toString(new InputStreamReader(is, Charsets.UTF_8));
            }
        } catch (IOException e) {
            error(logger, "an I/O error occur while reading graphql schema from file %s, service not initialized",
                    SCHEMA_ACTIVE, e);
        }
        return null;
    }

    @Override
    public GraphQL getGraphQL() {
        return graphQL;
    }
}
