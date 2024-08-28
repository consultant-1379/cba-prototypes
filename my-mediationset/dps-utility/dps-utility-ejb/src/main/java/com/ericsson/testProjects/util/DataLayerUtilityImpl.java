package com.ericsson.testProjects.util;

import javax.ejb.*;

import com.ericsson.oss.itpf.datalayer.dps.DataBucket;
import com.ericsson.oss.itpf.datalayer.dps.DataPersistenceService;
import com.ericsson.oss.itpf.datalayer.dps.persistence.PersistenceObject;

/**
 * @author ecaoodo
 *
 */
@Stateless
@Remote(DataLayerUtility.class)
@EJB(name = "java:/datalayer/DataLayerUtility", beanInterface = DataLayerUtility.class)
public class DataLayerUtilityImpl implements DataLayerUtility {

    /**
     * 
     */
    private static final long serialVersionUID = -8647570252889054171L;
    @EJB(lookup = "java:/datalayer/DataPersistenceService")
    private DataPersistenceService dps;

    /** 
     * @param fdn identifier 
     * @return the poID
     */
    @Override
    public long insertGeneralWithFdn(final String fdn) {
        final DataBucket theBucket = dps.getLiveBucket();
        final PersistenceObject po = theBucket.getMibRootBuilder().namespace("Army").type("General").name(fdn)
                .addAttribute("surname", fdn.substring(0, 4)).addAttribute("isAlive", true).create();
        
        return po.getPoId();
       
    }

	/* (non-Javadoc)
	 * @see com.ericsson.testProjects.util.DataLayerUtility#poIDExists(long)
	 */
    @Override
    public boolean poIDExists(long poID) {
        final DataBucket theBucket = dps.getLiveBucket();
        final PersistenceObject po = theBucket.findPoById(poID);
        
        if (po == null) {
            return false;
        } 
        else
        {
            return true;
        }

    }



}
