package com.timmydont.wherearetherocas.servlet;

import com.timmydont.wherearetherocas.lib.graphql.service.GraphqlWiringService;
import com.timmydont.wherearetherocas.lib.servlet.GraphqlEndpointServlet;
import com.timmydont.wherearetherocas.services.impl.WiringService;

public class EndpointServlet extends GraphqlEndpointServlet {

    @Override
    protected GraphqlWiringService getWiringService() {
        return new WiringService();
    }
}
