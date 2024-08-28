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
package com.ericsson.oss.mediation.util.netconf.api;

/**
 * 
 * @author xvaltda
 */

public class Capability implements java.io.Serializable {

    private static final long serialVersionUID = -2402343446798784505L;
    // RFC4741-8
    public static final String BASE_CAPABILITY = "urn:ietf:params:netconf:base:1.0";
    public static final Capability BASE = new Capability(BASE_CAPABILITY, "base", "ietf:params:netconf", "1.0");
    public static final String WRITABLE_RUNNING = "urn:ietf:params:xml:ns:netconf:capability:writable-running";
    public static final String CANDIDATE = "urn:ietf:params:xml:ns:netconf:capability:candidate";
    public static final String CONFIRMED_COMMIT = "urn:ietf:params:xml:ns:netconf:capability:confirmed-commit";
    public static final String ROLLBACK_ON_ERROR = "urn:ietf:params:xml:ns:netconf:capability:rollback-on-error";
    public static final String VALIDATE = "urn:ietf:params:xml:ns:netconf:capability:validate";
    public static final String DISTINCT_STARTUP = "urn:ietf:params:xml:ns:netconf:capability:distinc-startup";
    public static final String URL = "urn:ietf:params:xml:ns:netconf:capability:url";
    public static final String XPATH = "urn:ietf:params:xml:ns:netconf:capability:xpath";
    public static final String CUSTOM = "http://example.com/schema/1.2/stats";

    private final String urn;
    private final String version;
    private final String name;
    private final String namespace;

    public Capability(final String urn, final String name, final String namespace, final String version) {
        this.urn = urn;
        this.name = name;
        this.namespace = namespace;
        this.version = version;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the namespace
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    public String getURN() {
        return urn;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return urn;
    }

    @Override
    public boolean equals(final Object obj) {
        return urn.equals(((Capability) obj).getURN());
    }

    @Override
    public int hashCode() {
        return urn.hashCode();
    }

}
