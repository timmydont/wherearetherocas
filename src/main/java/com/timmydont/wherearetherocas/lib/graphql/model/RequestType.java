package com.timmydont.wherearetherocas.lib.graphql.model;

import org.apache.commons.lang3.StringUtils;

public enum RequestType {

	QUERY("Query"), 
	MUTATION("Mutation"), 
	UNKNOWN("Unknown");

	public final String id;

	RequestType(String id) {
		this.id = id;
	}

	public static RequestType ofValue(String value) {
		if (StringUtils.isBlank(value))
			return UNKNOWN;
		for (RequestType requestType : RequestType.values()) {
			if (requestType.id.equalsIgnoreCase(value)) {
				return requestType;
			}
		}
		return UNKNOWN;
	}
}
