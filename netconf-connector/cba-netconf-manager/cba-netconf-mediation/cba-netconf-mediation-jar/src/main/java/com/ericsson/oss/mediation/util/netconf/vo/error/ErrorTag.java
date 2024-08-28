/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2012
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.mediation.util.netconf.vo.error;

public enum ErrorTag {
    IN_USE("in-use", new ErrorType[] { ErrorType.protocol, ErrorType.application }, ErrorSeverity.error), INVALID_VALUE(
            "invalid-value", new ErrorType[] { ErrorType.protocol, ErrorType.application }, ErrorSeverity.error), TOO_BIG(
            "too-big",
            new ErrorType[] { ErrorType.protocol, ErrorType.application, ErrorType.rpc, ErrorType.transport },
            ErrorSeverity.error), MISSING_ATTRIBUTE("missing-attribute", new ErrorType[] { ErrorType.protocol,
            ErrorType.application, ErrorType.rpc }, ErrorSeverity.error), BAD_ATTRIBUTE("bad-attribute",
            new ErrorType[] { ErrorType.protocol, ErrorType.application, ErrorType.rpc }, ErrorSeverity.error), UNKNOWN_ATTRIBUTE(
            "unknown-attribute", new ErrorType[] { ErrorType.protocol, ErrorType.application, ErrorType.rpc },
            ErrorSeverity.error), MISSING_ELEMENT("missing-element", new ErrorType[] { ErrorType.protocol,
            ErrorType.application }, ErrorSeverity.error), BAD_ELEMENT("bad-element", new ErrorType[] {
            ErrorType.protocol, ErrorType.application }, ErrorSeverity.error), UNKNOWN_ELEMENT("unknown-element",
            new ErrorType[] { ErrorType.protocol, ErrorType.application }, ErrorSeverity.error), unknown_namespace(
            "unknown-namespace", new ErrorType[] { ErrorType.protocol, ErrorType.application }, ErrorSeverity.error), ACCESS_DENIED(
            "access-denied", new ErrorType[] { ErrorType.protocol, ErrorType.application }, ErrorSeverity.error), LOCK_DENIED(
            "lock-denied", new ErrorType[] { ErrorType.protocol }, ErrorSeverity.error), RESOURCE_DENIED(
            "resource-denied", new ErrorType[] { ErrorType.protocol, ErrorType.application, ErrorType.rpc,
                    ErrorType.transport }, ErrorSeverity.error), ROLLBACK_FAILED("rollback-failed", new ErrorType[] {
            ErrorType.protocol, ErrorType.application }, ErrorSeverity.error), DATA_EXISTS("data-exists",
            new ErrorType[] { ErrorType.application }, ErrorSeverity.error), DATA_MISSING("data-missing",
            new ErrorType[] { ErrorType.application }, ErrorSeverity.error), OPERATION_NOT_SUPPORTED(
            "operation-not-supported", new ErrorType[] { ErrorType.protocol, ErrorType.application },
            ErrorSeverity.error), OPERATION_FAILED("operation-failed", new ErrorType[] { ErrorType.protocol,
            ErrorType.application, ErrorType.rpc }, ErrorSeverity.error), PARTIAL_OPERATION("partial-operation",
            new ErrorType[] { ErrorType.application }, ErrorSeverity.error), MALFORMED_MESSAGE("malformed-message",
            new ErrorType[] { ErrorType.rpc }, ErrorSeverity.error);
    String value;
    ErrorType[] types;
    ErrorSeverity severity;

    /**
     * @param value
     * @param type
     * @param severity
     */
    private ErrorTag(final String value, final ErrorType[] types, final ErrorSeverity severity) {
        this.value = value;
        this.types = types;
        this.severity = severity;
    }

    public static ErrorTag getErrorTagbyValue(final String name) {
        final ErrorTag[] values = ErrorTag.values();
        for (final ErrorTag errorTag : values) {
            if (errorTag.value.equals(name)) {
                return errorTag;
            }
        }
        return null;
    }

}
