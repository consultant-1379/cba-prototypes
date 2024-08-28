/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2014
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.mediation.util.netconf.vo.error;

import com.ericsson.oss.mediation.util.netconf.vo.NetconfVo;
import java.util.List;

/**
 * @author xdeevas
 * 
 */
public class RpcErrorVo extends NetconfVo {

    List<Error> errors;

    /**
     * @return the errors
     */
    public List<Error> getErrors() {
        return errors;
    }

    /**
     * @param errors
     *            the errors to set
     */
    public void setErrors(final List<Error> errors) {
        this.errors = errors;
    }

}
