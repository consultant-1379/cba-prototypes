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
package com.ericsson.oss.mediation.pm.handlers.exceptions;

import org.junit.Assert;
import org.junit.Test;

public class NetconfManagerNotFoundExceptionTest {

    @Test
    public void test() {
        final NetconfManagerNotFoundException obj = new NetconfManagerNotFoundException("");
        Assert.assertNotNull(obj);
    }
}
