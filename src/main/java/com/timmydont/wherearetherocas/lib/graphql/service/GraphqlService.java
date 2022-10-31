package com.timmydont.wherearetherocas.lib.graphql.service;

import graphql.GraphQL;

public interface GraphqlService {

	/**
	 * Get the GraphQL context object
	 *
	 * @return the GraphQL object
	 */
	GraphQL getGraphQL();
}
