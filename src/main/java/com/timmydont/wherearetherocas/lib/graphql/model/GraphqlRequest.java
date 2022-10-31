package com.timmydont.wherearetherocas.lib.graphql.model;

import java.util.Map;

public class GraphqlRequest {

    public String query;

    public Map<String, Object> variables;

    public boolean hasVariables() {
        return variables != null && !variables.isEmpty();
    }
}
