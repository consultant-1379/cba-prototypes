/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2013
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.cba.prototype.protocol.sshclient.model.query;

public class Query {

    private String query = "";
    private QueryType queryType = QueryType.NETCONF_QUERY;
    private int responseHandlingTimeoutInMillis = 150000;

    public Query() {

    }

    /**
     * @param query
     */
    public Query(final String query) {
        this.query = query;
    }

    /**
     * @param query
     * @param responseHandlingTimeoutInMillis
     */
    public Query(final String query, final int responseHandlingTimeoutInMillis) {
        this.query = query;
        this.responseHandlingTimeoutInMillis = responseHandlingTimeoutInMillis;
    }

    /**
     * @return
     */
    public String getQuery() {
        return query;
    }

    /**
     * @return
     */
    public QueryType getQueryType() {
        return queryType;
    }

    /**
     * @return
     */
    public int getResponseHandlingTimeoutInMillis() {
        return responseHandlingTimeoutInMillis;
    }

    /**
     * @param query
     */
    public void setQuery(final String query) {
        this.query = query;
    }

    /**
     * @param queryType
     */
    public void setQueryType(final QueryType queryType) {
        this.queryType = queryType;
    }

    /**
     * @param responseHandlingTimeoutInMillis
     */
    public void setResponseHandlingTimeoutInMillis(final int responseHandlingTimeoutInMillis) {
        this.responseHandlingTimeoutInMillis = responseHandlingTimeoutInMillis;
    }

}
