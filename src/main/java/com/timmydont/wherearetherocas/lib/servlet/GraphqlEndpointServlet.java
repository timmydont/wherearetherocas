package com.timmydont.wherearetherocas.lib.servlet;

import com.google.gson.Gson;
import com.timmydont.wherearetherocas.lib.graphql.adapter.GraphqlRequestAdapter;
import com.timmydont.wherearetherocas.lib.graphql.adapter.impl.GraphQLRequestAdapterImpl;
import com.timmydont.wherearetherocas.lib.graphql.service.GraphqlService;
import com.timmydont.wherearetherocas.lib.graphql.service.GraphqlWiringService;
import com.timmydont.wherearetherocas.lib.graphql.service.impl.GraphqlServiceImpl;
import com.timmydont.wherearetherocas.lib.utils.HttpUtils;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.timmydont.wherearetherocas.lib.utils.HttpUtils.setResponse;
import static com.timmydont.wherearetherocas.lib.utils.LoggerUtils.error;

public abstract class GraphqlEndpointServlet extends HttpServlet {

    private final Logger logger = Logger.getLogger(getClass());

    private final Gson gson;
    private final GraphqlService service;
    private final GraphqlRequestAdapter requestAdapter;

    public GraphqlEndpointServlet() {
        gson = new Gson();
        service = new GraphqlServiceImpl(getWiringService());
        requestAdapter = new GraphQLRequestAdapterImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // graphql execution input from request
        ExecutionInput input = requestAdapter.adapt(request);
        if (null == input) {
            error(logger, "while creating ExecutionInput from POST request");
            setResponse(response, HttpServletResponse.SC_BAD_REQUEST, HttpUtils.HTTP_ERROR_PARSE_REQUEST);
            return;
        }
        // execute input
        ExecutionResult executionResult = service.getGraphQL().execute(input);
        // write results to response
        if (executionResult.isDataPresent() && executionResult.getErrors().isEmpty()) {
            setResponse(response, HttpServletResponse.SC_OK, gson.toJson(executionResult.toSpecification()));
        } else {
            error(logger, "[graphql internal] " + executionResult.getErrors().get(0).getMessage());
            setResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, HttpUtils.HTTP_ERROR_GRAPHQL_INTERNAL);
        }
    }

    protected abstract GraphqlWiringService getWiringService();
}
