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

package com.ericsson.oss.mediation.util.transport.ssh;
import com.ericsson.oss.mediation.util.transport.api.TransportManagerCI;
import com.ericsson.oss.mediation.util.transport.api.TransportData;
import com.ericsson.oss.mediation.util.transport.ssh.exception.ExceptionReason;
import com.ericsson.oss.mediation.util.transport.ssh.exception.SshException;
import com.ericsson.oss.mediation.util.transport.ssh.exception.SshIllegalStateException;
import com.jcraft.jsch.ChannelSubsystem;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author xvaltda
 */
public class SshContext {
    
   private final TransportManagerCI transportManagerCI;
   
   private SshConnection sshConnection = null;
   private SshSession sshSession = null;
   private TransportData transportData;
   private JSch sshLibrary = null;
   
   private SshTransportFSM sshTransportFSM;
   private static final Logger logger = LoggerFactory.getLogger(SshContext.class);
   
   
   public SshTransportFSM getState() {
       return sshTransportFSM;
   }
   
   public void setState (final SshTransportFSM state) {
       sshTransportFSM= state;
   }
   
   protected void setsshLibrary (final JSch sshLibraryparam) {
       sshLibrary= sshLibraryparam;
   }
   
   public SshContext (final TransportManagerCI transportManagerCI) {
       this.transportManagerCI = transportManagerCI;
       sshTransportFSM = SshTransportFSM.IDLE;
        sshLibrary = new JSch();
   }
   
   public void openChannel() throws SshException {
       try {
           sshTransportFSM.openchannel(this);
       } catch (SshIllegalStateException ex) {
           logger.error("Error openning ssh's channel");
           throw new SshException(ex);
       }
   }
   
   public void closeConnection() throws SshException  {
       try {
           sshTransportFSM.disconnect(this);
       } catch (SshIllegalStateException ex) {
           logger.error("Error closing the ssh's connection");
           throw new SshException(ex);
       }
   }
   
   public void connect () throws SshException  {
       try {
           sshTransportFSM.connect(this);
       } 
       catch (SshIllegalStateException ex) {
           logger.error("Error creating the ssh's connection");
           throw new SshException(ex);
       }
   }
    
   public void sendRequest (TransportData request) throws SshException {
	   if (request == null) {
           throw new SshException(ExceptionReason.INVALID_INPUT_NULL_BUFFER);
       }
       try {
    	   this.transportData = request;
           sshTransportFSM.write(this);
       }
       catch (SshIllegalStateException ex) {
           logger.error("Error sending the ssh's request");
           throw new SshException(ex);
       }
   } 
   

   public void getResponse (TransportData buffer) throws SshException {
       try {
           if (buffer == null) {
               throw new SshException(ExceptionReason.INVALID_INPUT_NULL_BUFFER);
           }
           transportData = buffer;
           sshTransportFSM.read(this);
           
       } catch (SshIllegalStateException ex) {
           logger.error("Error reading the ssh's connection");
           throw new SshException(ex);
       }
         
   }

   public boolean disconnect() throws SshException {
        try {
             
             ChannelSubsystem channel = (ChannelSubsystem) sshSession.getSession();
             channel.disconnect();
             
             Session session = (Session) sshConnection.getConnection();
             session.disconnect();
             
        } catch (final Exception e) {
            throw new SshException(ExceptionReason.SESSION_DISCONNECTION_FAILURE, e.getMessage());
        }
        return true;
    }

   private void write(final TransportData request) throws IOException {
        logger.debug("sending the data's buffer to ssh connection. \n It contains: \n"+request.getData().toString());
        sshSession.getOutputStream().write(request.getBytes());
        sshSession.getOutputStream().flush();  
   }
   
   public void doRead () {
       try {
		readSocket ();
		sshTransportFSM.onSuccess(this);
	} catch (IOException e) {
		sshTransportFSM.onFailure(this);
	}
   }
   
   public void doWrite() {
       try {
           write (transportData);
           sshTransportFSM.onSuccess(this);
       } 
       catch (IOException ex) {
           logger.error("Error writing in the ssh's socket");
           sshTransportFSM.onFailure(this);
       }
   }
   
   public void doCreateSshConnection() throws SshException {
        sshConnection = new SshConnection();

//        final JSch sshLibrary = new JSch();
        
        try {          
            

            final Session session = sshLibrary.getSession(transportManagerCI.getUsername(),
            transportManagerCI.getHostname(),transportManagerCI.getPort());
            
            session.setPassword(transportManagerCI.getPassword());
            session.setTimeout(transportManagerCI.getSocketConnectionTimeoutInMillis());
            
//            //drazen
//            session.setServerAliveInterval(1000 * 5);
            
            final java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
                     
            session.setConfig(config);
            session.connect();
            
            sshConnection.setConnection(session);
            
            Thread.sleep(100l);
            sshTransportFSM.onSuccess(this);
        } 
        catch (final Exception e) {
            
            sshTransportFSM.onFailure(this);
            throw new SshException(ExceptionReason.CONNECTION_ESTABLISHMENT_FAILURE, 
                    " hostname : " + transportManagerCI.getHostname()+ 
                    " port : " + transportManagerCI.getPort()+ 
                    " username : " + transportManagerCI.getUsername() + 
                    " password : " + "" + 
                    " exception : " + e.getMessage());
        }
    }
    
   public void doCreateSshSession() throws SshException {
       try { 
           crateSshSession ();
           sshTransportFSM.onSuccess(this);
       } catch (SshException ex) {
           logger.error("Error creating the ssh's connection");
           sshTransportFSM.onFailure(this);
           throw new SshException(ex);
       }
    }
   
   private void crateSshSession () throws SshException {
        sshSession = new SshSession();
        
        try {      
            Session session = (com.jcraft.jsch.Session) sshConnection.getConnection();
            
            ChannelSubsystem sshChannel = (ChannelSubsystem) session.openChannel(transportManagerCI.getSessionType().getValue());
            
            sshChannel.setSubsystem(transportManagerCI.getSessionTypeValue());
            
            
            sshSession.setOutputStream(sshChannel.getOutputStream());
            sshSession.setInputStream(sshChannel.getInputStream());
            
            sshChannel.connect();
            sshSession.setSession(sshChannel);
        } 
        catch (final Exception e) {
            throw new SshException(ExceptionReason.SUBSYTEM_CONNECTION_FAILURE, e.getMessage());
        }
   }
   
   public void doDisconnect () throws SshException {
       try {
           disconnect ();
           sshTransportFSM.onSuccess(this);
       } catch (SshException ex) {
            logger.error("Error in ssh's disconnect");
            throw new SshException(ex);
       }
   }
   
  private void readSocket() throws IOException {
       
       try {
       final BufferedReader reader = new BufferedReader(new InputStreamReader(sshSession.getInputStream()));
       
        StringBuffer buffer = new StringBuffer();
        StringBuffer response = new StringBuffer();
        int  ascii = 0;
        char character;
        
           ascii = reader.read();
           response.append(""+(char)ascii);
           
           while (ascii != transportData.getEndData() ) {

           ascii = reader.read();
           character = (char)ascii;
           response.append(""+character);

        }
           
           buffer.append(response);
           transportData.setData(buffer);
           logger.debug("reading the ssh connection, data received:\n "+response.toString());
       } catch (IOException ex) {
            logger.error("Error reading the ssh socket");
           throw ex;
       }
       
   }
}
