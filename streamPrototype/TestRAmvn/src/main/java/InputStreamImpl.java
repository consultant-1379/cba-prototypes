import java.io.IOException;

import com.ericsson.oss.mediation.adapter.ssh.api.SSHConnection;
import com.ericsson.oss.mediation.adapter.ssh.api.SSHSessionBytesResponse;

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

public class InputStreamImpl extends  java.io.InputStream {

    private final  SSHConnection connection ;
    private final String command;
    private boolean commndRun = false;
    int index = 0;
    byte[] buffer = null;
    private final String END_PATTERN = "]]>]]>";
    boolean END_FOUND = false;

    /**
     * @param connection
     */
    public InputStreamImpl(final SSHConnection connection, final String cmd) {
	super();
	this.connection = connection;
	this.command = cmd;
    }

    /**
     * @param args
     */
    public static void main(final String[] args) {
	// TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see java.io.InputStream#read()
     * only returns char for each time
     */
    @Override
    public int read() throws IOException  {
	byte[] buffer = getBuffer();
	int bufferInt = -1;
	if(buffer!= null){
	    if(!END_FOUND || (END_FOUND && index < buffer.length)){
		Byte byteObj = buffer[index];
		bufferInt = byteObj.intValue();
		index++;
	    }
	}
	return bufferInt;
    }


    /**
     * @return the buffer
     */
    private byte[] getBuffer() {
	if(!commndRun){
	    executeCommand();
	}
	else
	{
	    if(buffer!= null){
		int length = buffer.length;
		if(index >= length)
		{
		    readResponse();
		}
	    }
	}
	return buffer;
    }

    private void readResponse() {
	while(true){
	if(!END_FOUND){
	    final SSHSessionBytesResponse resp= connection.readResponseBytes();
	    final byte[] bufferTemp = resp.getResponseBytes();
	    if(bufferTemp != null){
		String replyTemp = new String(bufferTemp).trim();
		if(!replyTemp.isEmpty()){
		    if(replyTemp.endsWith(END_PATTERN))
		    {
			END_FOUND = true;
			System.out.println("endfound");
			replyTemp = replyTemp.replaceFirst(END_PATTERN, "");
		    }
		    buffer = replyTemp.getBytes();
		    index = 0;
		    break;
		}
	    }else
	    {
		System.out.println("null data found");
		buffer = bufferTemp;
		break;
	    }
	}
	else
	{
	    break;
	}
	}
    }

    private void executeCommand() {
	SSHSessionBytesResponse resp;
	resp = connection.executeCommandBytes(command);
	byte[] bufferTemp = resp.getResponseBytes();
	if(bufferTemp != null){
	String replyTemp = new String(bufferTemp).trim();
	if(replyTemp.endsWith(END_PATTERN))
	{
	    END_FOUND = true;
	    System.out.println("endfound");
	}
	buffer = replyTemp.getBytes();
	}else
	{
	buffer = bufferTemp;
	}
	commndRun = true;
    }



}
