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
package com.ericsson.oss.mediation.util.netconf.server;

import com.ericsson.oss.mediation.util.netconf.context.NetconfSessionTest;
import com.ericsson.oss.mediation.util.netconf.manger.NetconfTestConstants;
import com.ericsson.oss.mediation.util.transport.api.TransportData;
import com.ericsson.oss.mediation.util.transport.api.TransportManager;
import com.ericsson.oss.mediation.util.transport.api.exception.TransportException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.doAnswer;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * 
 * @author xperpau
 */
public class NetconfServerTestUtility {

    public static void prepareSendRpcOk(final TransportManager transportManager) {
        try {
            doAnswer(new Answer<Void>() {

                @Override
                public Void answer(final InvocationOnMock invocation) throws Throwable {

                    final Object[] arguments = invocation.getArguments();
                    if ((arguments != null) && (arguments.length > 0) && (arguments[0] != null)) {
                        final TransportData transportData = (TransportData) arguments[0];
                        transportData.setData(new StringBuilder(NetconfTestConstants.RPC_OK));
                    }
                    return null;
                }
            }).when(transportManager).readData(any(TransportData.class), anyBoolean());
        } catch (final TransportException ex) {
            Logger.getLogger(NetconfSessionTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void prepareSendCloseSession(final TransportManager transportManager) {
        try {
            doAnswer(new Answer<Void>() {

                @Override
                public Void answer(final InvocationOnMock invocation) throws Throwable {

                    final Object[] arguments = invocation.getArguments();
                    if (arguments != null && arguments.length > 0 && arguments[0] != null) {
                        TransportData transportData = (TransportData) arguments[0];
                        transportData.setData(new StringBuilder(NetconfTestConstants.RPC_OK));
                    }
                    return null;
                }
            }).when(transportManager).readData(any(TransportData.class), anyBoolean());
        } catch (TransportException ex) {
            Logger.getLogger(NetconfSessionTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void prepareSendHelloWithoutSessionID(final TransportManager transportManager) {
        try {
            doAnswer(new Answer<Void>() {

                @Override
                public Void answer(final InvocationOnMock invocation) throws Throwable {

                    final Object[] arguments = invocation.getArguments();
                    if (arguments != null && arguments.length > 0 && arguments[0] != null) {
                        TransportData transportData = (TransportData) arguments[0];
                        transportData.setData(new StringBuilder(NetconfTestConstants.HELLO_WITHOUT_SESSION_ID));
                    }
                    return null;
                }
            }).when(transportManager).readData(any(TransportData.class), anyBoolean());
        } catch (TransportException ex) {
            Logger.getLogger(NetconfSessionTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void prepareSendHello(final TransportManager transportManager) {
        try {
            doAnswer(new Answer<Void>() {

                @Override
                public Void answer(final InvocationOnMock invocation) throws Throwable {

                    final Object[] arguments = invocation.getArguments();
                    if (arguments != null && arguments.length > 0 && arguments[0] != null) {
                        TransportData transportData = (TransportData) arguments[0];
                        transportData.setData(new StringBuilder(NetconfTestConstants.HELLO));
                    }
                    return null;
                }
            }).when(transportManager).readData(any(TransportData.class), anyBoolean());
        } catch (TransportException ex) {
            Logger.getLogger(NetconfSessionTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void prepareSendQuery(final TransportManager transportManager, final String GET_ANSWER) {
        try {
            doAnswer(new Answer<Void>() {

                @Override
                public Void answer(final InvocationOnMock invocation) throws Throwable {

                    final Object[] arguments = invocation.getArguments();
                    if (arguments != null && arguments.length > 0 && arguments[0] != null) {
                        TransportData transportData = (TransportData) arguments[0];
                        transportData.setData(new StringBuilder(GET_ANSWER));
                    }
                    return null;
                }
            }).when(transportManager).readData(any(TransportData.class), anyBoolean());
        } catch (TransportException ex) {
            Logger.getLogger(NetconfSessionTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
