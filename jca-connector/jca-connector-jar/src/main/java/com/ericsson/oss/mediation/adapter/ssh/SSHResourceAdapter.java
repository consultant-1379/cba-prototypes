package com.ericsson.oss.mediation.adapter.ssh;

import javax.resource.ResourceException;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.BootstrapContext;
import javax.resource.spi.ConfigProperty;
import javax.resource.spi.Connector;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.ResourceAdapterInternalException;
import javax.resource.spi.TransactionSupport;
import javax.resource.spi.XATerminator;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.xa.XAResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * SSHResourceAdapter
 * 
 * @version $Revision: $
 */
@Connector(reauthenticationSupport = false, transactionSupport = TransactionSupport.TransactionSupportLevel.NoTransaction)
public class SSHResourceAdapter implements ResourceAdapter, java.io.Serializable {

    /** The serial version UID */
    private static final long serialVersionUID = 1L;

    /** The logger */
    private static Logger log = LoggerFactory.getLogger(SSHResourceAdapter.class);

    /**
     * Resource adapter specific settings
     */
    /** name */
    @ConfigProperty(defaultValue = "SSH Resource Adapter")
    private String name;

    /** version */
    @ConfigProperty(defaultValue = "1.0")
    private String version;

    
    /**
     * TxSyncRegistry
     */
    private TransactionSynchronizationRegistry txRegistry;

    /**
     * XATerminator
     */
    private XATerminator xaTerminator;

    /**
     * @return the xaTerminator
     */
    public XATerminator getXaTerminator() {
        return xaTerminator;
    }

    /**
     * @param xaTerminator
     *            the xaTerminator to set
     */
    public void setXaTerminator(final XATerminator xaTerminator) {
        this.xaTerminator = xaTerminator;
    }

    /**
     * @return the txRegistry
     */
    public TransactionSynchronizationRegistry getTxRegistry() {
        return txRegistry;
    }

    /**
     * @param txRegistry
     *            the txRegistry to set
     */
    public void setTxRegistry(final TransactionSynchronizationRegistry txRegistry) {
        this.txRegistry = txRegistry;
    }


    /**
     * This is called during the activation of a message endpoint.
     * 
     * @param endpointFactory
     *            A message endpoint factory instance.
     * @param spec
     *            An activation spec JavaBean instance.
     * @throws ResourceException
     *             generic exception
     */
    @Override
    public void endpointActivation(final MessageEndpointFactory endpointFactory, final ActivationSpec spec) throws ResourceException {

        log.trace("endpointActivation()");

    }

    /**
     * This is called when a message endpoint is deactivated.
     * 
     * @param endpointFactory
     *            A message endpoint factory instance.
     * @param spec
     *            An activation spec JavaBean instance.
     */
    @Override
    public void endpointDeactivation(final MessageEndpointFactory endpointFactory, final ActivationSpec spec) {

        log.trace("endpointDeactivation()");

    }

    /**
     * This is called when a resource adapter instance is bootstrapped.
     * 
     * @param ctx
     *            A bootstrap context containing references
     * @throws ResourceAdapterInternalException
     *             indicates bootstrap failure.
     */
    @Override
    public void start(final BootstrapContext ctx) throws ResourceAdapterInternalException {
        log.info("Starting resorce adapter ...");
        this.txRegistry = ctx.getTransactionSynchronizationRegistry();
        this.xaTerminator = ctx.getXATerminator();

    }

    /**
     * This is called when a resource adapter instance is undeployed or during application server shutdown.
     */
    @Override
    public void stop() {
        log.debug("Stopping resource adapter...");

    }

    /**
     * This method is called by the application server during crash recovery.
     * 
     * @param specs
     *            An array of ActivationSpec JavaBeans
     * @throws ResourceException
     *             generic exception
     * @return An array of XAResource objects
     */
    @Override
    public XAResource[] getXAResources(final ActivationSpec[] specs) throws ResourceException {
        log.trace("getXAResources()");
        return new XAResource[] {};

    }
    
    /**
     * Set name
     * 
     * @param name
     *            The value
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Get name
     * 
     * @return The value
     */
    public String getName() {
        return name;
    }

    /**
     * Set version
     * 
     * @param version
     *            The value
     */
    public void setVersion(final String version) {
        this.version = version;
    }

    /**
     * Get version
     * 
     * @return The value
     */
    public String getVersion() {
        return version;
    }

    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SSHResourceAdapter other = (SSHResourceAdapter) obj;
        return equalsSSHResourceAdapter(other);
    }

    /*
     * used to reduce cyclomatic complexity and clear PMD warnings for equals method above
     */
    private boolean equalsSSHResourceAdapter(final SSHResourceAdapter other) {
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (version == null) {
            if (other.version != null) {
                return false;
            }
        } else if (!version.equals(other.version)) {
            return false;
        }
        return true;
    }
 
}
