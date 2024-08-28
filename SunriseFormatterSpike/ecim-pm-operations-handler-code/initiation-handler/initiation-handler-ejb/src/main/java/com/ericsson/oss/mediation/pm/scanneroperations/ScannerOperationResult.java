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
package com.ericsson.oss.mediation.pm.scanneroperations;

public class ScannerOperationResult {

    private final String scannerID;
    private final String scannerStatus;
    private short errorCode = -1;

    public ScannerOperationResult(final String scanner_Id, final String scanner_Status) {
        this.scannerID = scanner_Id;
        this.scannerStatus = scanner_Status;
    }

    public ScannerOperationResult(final String scanner_Id, final String scanner_Status, final short error_Code) {
        this(scanner_Id, scanner_Status);
        this.errorCode = error_Code;

    }

    /**
     * @return the scannerID
     */
    public String getScannerID() {
        return scannerID;
    }

    /**
     * @return the scannerStatus
     */
    public String getScannerStatus() {
        return scannerStatus;
    }

    public short getErrorCode() {
        return errorCode;
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ScannerOperationResult [scannerID=" + scannerID
				+ ", scannerStatus=" + scannerStatus + ", errorCode="
				+ errorCode + "]";
	}    

}
