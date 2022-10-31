package com.timmydont.wherearetherocas.lib.graphql.adapter;

import graphql.ExecutionInput;

import javax.servlet.http.HttpServletRequest;

public interface GraphqlRequestAdapter {

    ExecutionInput adapt(HttpServletRequest request);
}
