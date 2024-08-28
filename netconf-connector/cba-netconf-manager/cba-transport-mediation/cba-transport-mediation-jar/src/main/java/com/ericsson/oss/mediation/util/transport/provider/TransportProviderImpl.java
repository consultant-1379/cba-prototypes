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

package com.ericsson.oss.mediation.util.transport.provider;

import com.ericsson.oss.mediation.util.transport.api.factory.TransportFactory;

/**
 * 
 * @author xvaltda
 */
public abstract class TransportProviderImpl {

    private static final String FACTORY_NAME = "com.ericsson.oss.mediation.util.transport.%s.manager.%STransportFactory";

    public static TransportFactory getFactory(final String provider) {

        final String myFactoryWithPath = String.format(FACTORY_NAME, provider.toLowerCase(), provider.toLowerCase());
        return createFactory(myFactoryWithPath);
    }

    private static TransportFactory createFactory(final String aClassName) {

        try {
            final Class myClazz = Class.forName(aClassName);
            return (TransportFactory) myClazz.newInstance();
        } catch (final ClassNotFoundException ex) {
        } catch (final InstantiationException ex) {
        } catch (final IllegalAccessException ex) {
        }

        return null;
    }

}
