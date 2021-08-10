package com.unistrong.api.services.poisearch;

import com.unistrong.api.services.poisearch.PoiSearch.Query;
import com.unistrong.api.services.poisearch.PoiSearch.SearchBound;

class QueryInternal {
	public String config;
	public String resType;
	public String enc;
	public String a_k;
	public Query mQuery;
	public SearchBound mBound;

	public QueryInternal(Query q, SearchBound bnd) {
		mQuery = q;
		mBound = bnd;
	}
}
