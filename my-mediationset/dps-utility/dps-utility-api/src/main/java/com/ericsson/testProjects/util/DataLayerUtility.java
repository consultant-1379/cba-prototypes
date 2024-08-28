package com.ericsson.testProjects.util;

import java.io.Serializable;

/**
 * @author ecaoodo
 *
 */
public interface DataLayerUtility extends Serializable {

    /**
     * @param fdn identifier
     * @return poID 
     */
    long insertGeneralWithFdn(final String fdn);
    
    /**
     * @param poID ID of the PO we are looking for
     * @return true or false depending on whether the po exists or not
     */
    boolean poIDExists(long poID); 
}
