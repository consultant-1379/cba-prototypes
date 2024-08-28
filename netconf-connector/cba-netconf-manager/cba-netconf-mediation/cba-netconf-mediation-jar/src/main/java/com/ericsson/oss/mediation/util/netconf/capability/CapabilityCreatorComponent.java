/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ericsson.oss.mediation.util.netconf.capability;

import com.ericsson.oss.mediation.util.netconf.api.exception.NetconfManagerException;
import com.ericsson.oss.mediation.util.netconf.flow.FlowInput;
import com.ericsson.oss.mediation.util.netconf.flow.FlowOutput;
import com.ericsson.oss.mediation.util.netconf.flow.component.FlowComponent;

/**
 * 
 * @author xvaltda
 */
public class CapabilityCreatorComponent implements FlowComponent {

    private final FlowOutput output = new FlowOutput();

    @Override
    public FlowOutput execute(final FlowInput input) {
        try {
            final NetconfSessionCapabilities capabilities = new NetconfSessionCapabilities(input.getNetconfSession()
                    .getConfigProperties()); //To change body of generated methods, choose Tools | Templates.
            input.getNetconfSession().setNetconfSessionCapabilities(capabilities);
            output.setError(false);

        } catch (NetconfManagerException ex) {
            output.setError(true);
            output.setErrorMessage(ex.getMessage());
        }

        return output;
    }

}
