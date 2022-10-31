package com.timmydont.wherearetherocas.lib.graphql.service;

import graphql.schema.idl.RuntimeWiring;

public interface GraphqlWiringService {

	/**
	 * Get GraphQL wiring
	 * @return GraphQL wiring
	 */
	RuntimeWiring getWiring();
}
