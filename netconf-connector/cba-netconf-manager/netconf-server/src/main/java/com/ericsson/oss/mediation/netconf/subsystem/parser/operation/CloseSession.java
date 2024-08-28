package com.ericsson.oss.mediation.netconf.subsystem.parser.operation;

import com.ericsson.oss.mediation.netconf.subsystem.CommandListener;

import java.io.PrintWriter;

public class CloseSession extends Rpc {

    public CloseSession(final Rpc rpc) {
        super(rpc);
    }

    @Override
    public Nop invoke(final CommandListener commandListener, final PrintWriter out) {
        commandListener.closeSession(messageId, out);
        return new Nop();
    }
}
