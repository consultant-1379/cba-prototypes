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

package com.ericsson.oss.mediation.util.netconf.io;

import com.ericsson.oss.mediation.util.netconf.context.NetconfSession;
import com.ericsson.oss.mediation.util.netconf.operation.Hello;
import com.ericsson.oss.mediation.util.netconf.operation.Operation;
import com.ericsson.oss.mediation.util.netconf.operation.OperationFactory;
import com.ericsson.oss.mediation.util.netconf.operation.OperationType;
import com.ericsson.oss.mediation.util.netconf.parser.NetconfParser;
import com.ericsson.oss.mediation.util.netconf.parser.ParserException;
import com.ericsson.oss.mediation.util.netconf.parser.ParserFactory;
import com.ericsson.oss.mediation.util.netconf.rpc.Rpc;
import com.ericsson.oss.mediation.util.netconf.vo.NetconfVo;
import com.ericsson.oss.mediation.util.transport.api.TransportData;
import com.ericsson.oss.mediation.util.transport.api.TransportManager;
import com.ericsson.oss.mediation.util.transport.api.exception.TransportException;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author xvaltda
 */
public class TransportManagerHelper {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(TransportManagerHelper.class);
    private final TransportManager transportManager;
    private TransportData transportData;
    private Operation operation;
    private OperationType operationType = null;

    public TransportManagerHelper(final TransportManager transportManager) {
        this.transportManager = transportManager;
    }

    public boolean sendNetconfOperation(final OperationType operationType, final StringBuffer data,
            final NetconfSession session) throws TransportException {

        final boolean isDataSend = true;
        try {
            this.operationType = operationType;
            operation = OperationFactory.create(operationType, data, session);
            if (operationType != OperationType.HELLO) {
                final Rpc rpc = new Rpc.RpcBuilder(operation).build();
                transportManager.sendData(rpc.getTransportData());
            } else {
                final Hello hello = (Hello) operation;
                transportManager.sendData(hello.getTransportData());
            }

        } catch (final TransportException ex) {
            logger.debug("Exception Sending operation: " + operation.getOperationType());
            ex.printStackTrace();
            throw ex;

        } catch (final Exception e) {
            logger.debug("Exception Sending operation : " + operation.getOperationType());
            e.printStackTrace();
            throw e;
        }
        return isDataSend;
    }

    public NetconfVo getNetconfResponse(final boolean close) throws TransportException, ParserException {

        transportData = new TransportData();
        transportData.setEndData(']');
        try {

            transportManager.readData(transportData, close);
            final NetconfParser parser = ParserFactory.create(operationType);

            return parser.parse(transportData);

        } catch (final TransportException ex) {
            logger.debug("Exception Reading the operation sent by the node, expected: " + operation.getOperationType());
            throw ex;

        } catch (final ParserException ex) {
            logger.debug("Exception Reading the operation sent by the node, expected: " + operation.getOperationType());
            throw ex;
        } catch (final Exception e) {
            logger.debug("Exception Reading the operation sent by the node, expected: " + operation.getOperationType());
            throw e;
        }
    }

}
