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
package com.ericsson.oss.mediation.cba.handlers.netconf;

import com.ericsson.oss.itpf.common.config.Configuration;
import com.ericsson.oss.itpf.common.event.ComponentEvent;
import com.ericsson.oss.itpf.common.event.handler.EventHandlerContext;
import com.ericsson.oss.mediation.util.netconf.api.NetconManagerConstants;
import com.ericsson.oss.mediation.util.netconf.api.NetconfConnectionStatus;
import com.ericsson.oss.mediation.util.netconf.api.NetconfManager;
import com.ericsson.oss.mediation.util.netconf.api.exception.NetconfManagerException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class AbstractNetconfHandlerTest {

    @Mock
    private Logger logger;

    @Mock
    protected NetconfManager netconfManager;

    @InjectMocks
    private NetconfHandler handler;

    @Before
    public void setUp() {
        handler = new NetconfHandler();
        initMocks(this);
    }

    @Test
    public void initTest() {
        final Map<String, Object> headerMap = new HashMap<>();
        final EventHandlerContext ctx = mock(EventHandlerContext.class);
        final Configuration config = mock(Configuration.class);
        when(ctx.getEventHandlerConfiguration()).thenReturn(config);
        when(config.getAllProperties()).thenReturn(headerMap);

        handler.init(ctx);

        verify(ctx).getEventHandlerConfiguration();
        verify(config).getAllProperties();
        verify(logger, times(3)).debug(anyString());
    }

    @Test(expected = Exception.class)
    public void initThrowsException() throws NetconfManagerException {
        final EventHandlerContext ctx = mock(EventHandlerContext.class);
        when(ctx.getEventHandlerConfiguration()).thenReturn(null);
        when(netconfManager.getStatus()).thenReturn(NetconfConnectionStatus.CONNECTED);

        handler.init(ctx);

        verify(ctx).getEventHandlerConfiguration();
        verify(logger, times(3)).debug(anyString());
        verify(logger).error(anyString());
        verify(netconfManager).getStatus();
        verify(netconfManager).disconnect();
    }

    @Test
    public void onEventTest() {
        final Map<String, Object> headerMap = new HashMap<>();
        headerMap.put(NetconManagerConstants.NETCONF_MANAGER_ATTR, netconfManager);
        final ComponentEvent inputEvent = mock(ComponentEvent.class);
        when(inputEvent.getHeaders()).thenReturn(headerMap);

        handler.onEvent(inputEvent);

        verify(inputEvent).getHeaders();
        verify(logger, times(3)).debug(anyString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void onEventWithNullNetconfManager() {
        final Map<String, Object> headerMap = new HashMap<>();
        final ComponentEvent inputEvent = mock(ComponentEvent.class);
        when(inputEvent.getHeaders()).thenReturn(headerMap);

        handler.onEvent(inputEvent);

        verify(inputEvent).getHeaders();
        verify(logger, times(3)).debug(anyString());
        verify(logger).error(anyString());
        verify(netconfManager).getStatus();
        verify(logger).warn(anyString());
    }

    @Test
    public void destroyTest() {
        handler.destroy();

        verify(logger, times(2)).debug(anyString());
    }
}

class NetconfHandler extends AbstractNetconfHandler {
}
