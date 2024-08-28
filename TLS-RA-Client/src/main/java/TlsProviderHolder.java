import com.ericsson.oss.mediation.adapter.tls.exception.TlsChannelException;
import com.ericsson.oss.mediation.adapter.tls.exception.TlsException;
import com.ericsson.oss.mediation.transport.api.TransportManagerCI;
import com.ericsson.oss.mediation.transport.tls.provider.TLSTransportProviderImpl;

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

public final class TlsProviderHolder {
	
	private static TLSTransportProviderImpl provider;

	public static TLSTransportProviderImpl getTlsProvider(TransportManagerCI transportManagerCI) {
		if(provider == null){
			provider =  new TLSTransportProviderImpl(transportManagerCI);
		}
		return provider;
	}

}
