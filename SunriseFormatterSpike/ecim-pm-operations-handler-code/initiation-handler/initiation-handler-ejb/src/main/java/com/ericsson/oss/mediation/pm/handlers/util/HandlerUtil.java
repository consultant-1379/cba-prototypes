package com.ericsson.oss.mediation.pm.handlers.util;

import static com.ericsson.oss.mediation.pm.handlers.constants.HandlerConstants.*;

import java.util.Map;

public class HandlerUtil {
	private HandlerUtil() {
    }    

    public static void prepareEventForNextHandler(final Map<String, Object> eventHeaders, final String nodeScannerID,
    		final short errorCode, final String scannerStatus) {

    	// populate headers
    	eventHeaders.put(NODE_SCANNER_ID, nodeScannerID);
    	eventHeaders.put(ERROR_CODE, errorCode);
    	eventHeaders.put(SCANNER_STATUS, scannerStatus);
    }
}
