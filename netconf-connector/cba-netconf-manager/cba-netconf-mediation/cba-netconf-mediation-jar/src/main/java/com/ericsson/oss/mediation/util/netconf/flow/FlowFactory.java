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

package com.ericsson.oss.mediation.util.netconf.flow;

import com.ericsson.oss.mediation.util.netconf.api.Datastore;
import com.ericsson.oss.mediation.util.netconf.api.Filter;
import com.ericsson.oss.mediation.util.netconf.capability.CapabilityCreatorComponent;
import com.ericsson.oss.mediation.util.netconf.context.NetconfSession;
import com.ericsson.oss.mediation.util.netconf.io.HelloWriterComponent;
import com.ericsson.oss.mediation.util.netconf.io.ReaderComponent;
import com.ericsson.oss.mediation.util.netconf.io.RpcWriterComponent;
import com.ericsson.oss.mediation.util.netconf.operation.OperationComponent;
import com.ericsson.oss.mediation.util.netconf.operation.OperationType;
import com.ericsson.oss.mediation.util.netconf.parser.ParserComponent;
import com.ericsson.oss.mediation.util.netconf.validator.HelloVoValidator;
import com.ericsson.oss.mediation.util.netconf.validator.KillSessionValidator;

/**
 * 
 * @author xvaltda
 */
public abstract class FlowFactory {

    public static FlowComposite createHandshakeFlow(final NetconfSession netconfSession) {
        final FlowComposite flowComposite = new FlowComposite(netconfSession, OperationType.HELLO);
        flowComposite.addComponent(new CapabilityCreatorComponent());
        flowComposite.addComponent(new OperationComponent());
        flowComposite.addComponent(new HelloWriterComponent());
        flowComposite.addComponent(new ReaderComponent());
        flowComposite.addComponent(new ParserComponent());
        flowComposite.addComponent(new HelloVoValidator());
        return flowComposite;

    }

    public static FlowComposite createGetConfigFlow(final NetconfSession netconfSession, final Datastore datastore,
            final Filter filter) {
        final FlowComposite flowComposite = new FlowComposite(netconfSession, OperationType.GET_CONFIG);
        flowComposite.addComponent(new OperationComponent(new Object[] { datastore, filter }));
        flowComposite.addComponent(new RpcWriterComponent());
        flowComposite.addComponent(new ReaderComponent());
        flowComposite.addComponent(new ParserComponent());
        return flowComposite;

    }

    public static FlowComposite createGetFlow(final NetconfSession netconfSession, final Filter filter) {
        final FlowComposite flowComposite = new FlowComposite(netconfSession, OperationType.GET);

        flowComposite.addComponent(new OperationComponent(filter));
        flowComposite.addComponent(new RpcWriterComponent());
        flowComposite.addComponent(new ReaderComponent());
        flowComposite.addComponent(new ParserComponent());
        return flowComposite;

    }

    public static FlowComposite createCloseFlow(final NetconfSession netconfSession) {
        final FlowComposite flowComposite = new FlowComposite(netconfSession, OperationType.CLOSE_SESSION);

        flowComposite.addComponent(new OperationComponent());
        flowComposite.addComponent(new RpcWriterComponent());
        flowComposite.addComponent(new ReaderComponent(true));
        flowComposite.addComponent(new ParserComponent());
        return flowComposite;

    }

    public static FlowComposite createKillSessionFlow(final String sessionId, final NetconfSession netconfSession) {
        final FlowComposite flowComposite = new FlowComposite(netconfSession, OperationType.KILL_SESSION);
        flowComposite.addComponent(new KillSessionValidator(sessionId));
        flowComposite.addComponent(new OperationComponent(sessionId));
        flowComposite.addComponent(new RpcWriterComponent());
        flowComposite.addComponent(new ReaderComponent());
        flowComposite.addComponent(new ParserComponent());
        return flowComposite;

    }
}
