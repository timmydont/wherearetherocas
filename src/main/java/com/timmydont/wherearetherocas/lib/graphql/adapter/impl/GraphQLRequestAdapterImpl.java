package com.timmydont.wherearetherocas.lib.graphql.adapter.impl;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.timmydont.wherearetherocas.lib.graphql.adapter.GraphqlRequestAdapter;
import com.timmydont.wherearetherocas.lib.graphql.model.GraphqlRequest;
import graphql.ExecutionInput;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static com.timmydont.wherearetherocas.lib.utils.LoggerUtils.error;

public class GraphQLRequestAdapterImpl implements GraphqlRequestAdapter {

    private final Logger logger = Logger.getLogger(getClass());

    @Override
    public ExecutionInput adapt(HttpServletRequest request) {
        try {
            GraphqlRequest graphqlRequest = new Gson().fromJson(request.getReader(), GraphqlRequest.class);
            // build and return the ExecutionInput
            if (graphqlRequest.hasVariables()) {
                return ExecutionInput.newExecutionInput()
                        .query(graphqlRequest.query)
                        .variables(graphqlRequest.variables)
                        .build();
            } else {
                return ExecutionInput.newExecutionInput()
                        .query(graphqlRequest.query)
                        .build();
            }
        } catch (JsonSyntaxException | JsonIOException | IOException e) {
            error(logger, "unable to parse request to graphql request object", e);
        }
        return null;
    }
}
