package com.ericsson.oss.mediation.util.netconf.api;

public enum Datastore {
    RUNNING("running"), CANDIDATE("candidate"), STARTUP("startup");

    private String parameterName;

    Datastore(final String parameterName) {
        this.parameterName = parameterName;
    }

    public String asParameter() {
        return this.parameterName;
    }
}
