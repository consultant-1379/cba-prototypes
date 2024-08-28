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
package com.ericsson.oss.mediation.pm.scanneroperations.exception;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;

import com.ericsson.oss.mediation.pm.handlers.error.HandlerError;

public class PerformanceMonitoringExceptionTest {
	
    @Test
    public void test() {
        final PerformanceMonitoringException obj = new PerformanceMonitoringException("Netconf operation failed", HandlerError.NETCONF_OPERATION_FAILED);
        Assert.assertNotNull(obj);
        assertEquals(HandlerError.NETCONF_OPERATION_FAILED.getCode(), obj.getError().getCode());
        assertEquals("Netconf operation failed", obj.getMessage());
    }
}
