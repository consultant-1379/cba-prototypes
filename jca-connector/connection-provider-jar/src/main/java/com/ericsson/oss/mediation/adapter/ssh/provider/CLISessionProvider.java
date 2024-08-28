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
package com.ericsson.oss.mediation.adapter.ssh.provider;

import java.io.*;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maverick.ssh.SshException;
import com.maverick.ssh.SshIOException;

public abstract class CLISessionProvider {

    protected final String ipAddress;
    protected final int port;
    protected final String username;
    protected final String password;
    private int waitTimeForResponse;
    private Pattern commandPrompt;
    protected final String subsystem;
    private AtomicBoolean askedSessionAbort = new AtomicBoolean();
    private boolean busy = true;
    private InputStream stdOut;
    private OutputStream stdIn;

    // protected static final PtyInfo PTY_INFO =
    // PropertyConfigurator.getInstance().getPtyInfo();

    protected String patternPrefix = CliConstants.SSR_PATTERN_PREFIX; // Any
    // character
    // for
    // zero
    // or
    // more
    // times
    protected String patterSuffix = CliConstants.SSR_PATTERN_SUFFIX;

    // protected static final Pattern PASSWORD_PATTERN =
    // Pattern.compile(".*[Pp]assword\\s*:\\s*$", Pattern.DOTALL);

    protected static final int INFINITE_WAIT_TIME_FOR_RESPONSE = 0;

    private final Logger LOG = LoggerFactory
            .getLogger(CLISessionProvider.class);

    protected CLISessionProvider(final String ipAddress, final int port,
            final String username, final String password,
            final int waitTimeForResponse, final String commandPromptPattern,
            final String subsystem, final String patternPrefix,
            final String patterSuffix) throws InvalidPatternException {
        this.ipAddress = ipAddress;
        this.port = port;
        this.username = username;
        this.password = password;
        this.waitTimeForResponse = waitTimeForResponse;
        this.subsystem = subsystem;
        if (patternPrefix != null) {
            this.patternPrefix = patternPrefix;
        }
        if (patterSuffix != null) {
            this.patterSuffix = patterSuffix;
        }
        try {
            this.commandPrompt = Pattern.compile(
                    this.patternPrefix.concat(commandPromptPattern
                            + this.patterSuffix), Pattern.DOTALL);
        } catch (final Exception exc) {
            throw new InvalidPatternException("Invalid command prompt pattern",
                    exc);
        }
    }

    public Pattern getCommandPrompt() {
        return this.commandPrompt;
    }

    public void setCommandPrompt(final String commandPromptPattern)
            throws InvalidPatternException {
        try {
            this.commandPrompt = Pattern.compile(
                    patternPrefix.concat(commandPromptPattern + patterSuffix),
                    Pattern.DOTALL);
        } catch (final Exception exc) {
            throw new InvalidPatternException("Invalid command prompt pattern",
                    exc);
        }
    }

    protected InputStream getStdOut() {
        return this.stdOut;
    }

    protected void setStdOut(final InputStream stdOut) {
        this.stdOut = new BufferedInputStream(stdOut);
    }

    protected OutputStream getStdIn() {
        return this.stdIn;
    }

    protected void setStdIn(final OutputStream stdIn) {
        this.stdIn = stdIn;
    }


    public final byte[] executeCommandwithBytes(final String command)
            throws TimeoutExpiredException, AbortRequestException,
            CLISessionException {
        if (this.askedSessionAbort.get()) {
            throw new AbortRequestException("CLI Session aborted on demand");
        }
        sendCommand(command);
        final byte[] output = readOutput(this.commandPrompt);
        return output;
    }

    public final byte[] readCommandResBytes()
            throws TimeoutExpiredException, AbortRequestException,
            CLISessionException {
        if (this.askedSessionAbort.get()) {
            throw new AbortRequestException("CLI Session aborted on demand");
        }
        final byte[] output = readOutput(this.commandPrompt);
        return output;
    }

    /**
     * @param commandPrompt2
     * @return
     * @throws CLISessionException 
     */
    private byte[] readOutput(final Pattern expectedPattern) throws CLISessionException {
        LOG.debug("Reading of output command in progress...");
        byte[] buffer = new byte[1024];
        final StringBuilder outputBuilder = new StringBuilder();
        int readBytesNum = 0;
        final byte[] output = null;
        try {
            readBytesNum = this.stdOut.read(buffer);

            outputBuilder.append(new String(buffer, 0, readBytesNum,
                    StandardCharsets.UTF_8));

            /* If the expected string is found in stdout buffer */
            if (findPattern(outputBuilder, expectedPattern)) {
                try {
                    this.changeSocketTimeout(INFINITE_WAIT_TIME_FOR_RESPONSE); // Infinite
                    // wait
                    // time
                    // for
                    // response
                    // setting
                } catch (final SocketTimeoutChangeException exc) {
                    exc.setLastCommandOuput(output .toString());
                    throw exc;
                }
                if (LOG.isInfoEnabled()) {
                    LOG.info("The \"" + expectedPattern
                            + "\" pattern found in the output command");
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Reading of output command completed");
                    }
                }
//                buffer = null;
                return buffer;
                //                }
            }
        } catch (final SshIOException exc) // Handling Timeout error for SSH
        // connection
        {
            final SshException sshException = exc.getRealException();
            if (sshException != null) {
                if ((sshException.getReason() == SshException.SOCKET_TIMEOUT)
                        || (sshException.getReason() == SshException.PROMPT_TIMEOUT)) {
                    throw new TimeoutExpiredException(
                            "Timeout expired and the \""
                                    + expectedPattern
                                    + "\" pattern not found in the output command: \""
                                    + output + "\"", sshException,
                                    output.toString());
                } else {
                    throw new CLISessionException(
                            "An I/O error is occured during standard output command reading: "
                                    + sshException.getMessage() + " (Reason: "
                                    + sshException.getReason()
                                    + ") - output: \"" + output + "\"",
                                    output.toString());
                }
            }
            throw new CLISessionException(
                    "An I/O error is occured during standard output command reading - output: \""
                            + output + "\"", exc, output.toString());
        } catch (final SocketTimeoutException exc) // Handling Timeout error for
        // Telnet connection
        {
            throw new TimeoutExpiredException("Timeout expired and the \""
                    + expectedPattern
                    + "\" pattern not found in the output command: \"" + output
                    + "\"", exc, output.toString());
        } catch (final IOException exc) // Handling Generic I/O error
        {
            throw new CLISessionException(
                    "An I/O error is occured during standard output command reading - output: \""
                            + output + "\"", exc, output.toString());
        }
        return buffer;
    }



    protected final void sendCommand(final String command)
            throws SocketTimeoutChangeException, CLISessionException {
        if (isConnected()) {
            try {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Writing command \"" + command
                            + "\" in standard input in progress...");
                }
                this.stdIn.write(command.concat("\n").getBytes());
                this.stdIn.flush();
                this.changeSocketTimeout(this.waitTimeForResponse); // Wait time
                // for
                // response
                // setting
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Writing command \"" + command
                            + "\" in standard input completed");
                }
            } catch (final IOException exc) {
                throw new CLISessionException("Sending command \"" + command
                        + "\" failed (last chars read by standard output: \""
                        + this.readLastCharsByStdOut() + "\")", exc);
            }
        } else {
            throw new CLISessionException(
                    "Cannot send command \""
                            + command
                            + "\": connection loss (last chars read by standard output: \""
                            + this.readLastCharsByStdOut() + "\")");
        }
    }

    private String readLastCharsByStdOut() {
        try {
            if (this.stdOut.available() != 0) {
                final StringBuilder output = new StringBuilder();
                final byte[] buffer = new byte[1024];
                int readBytesNum = 0;
                while (true) {
                    readBytesNum = this.stdOut.read(buffer);
                    if (readBytesNum == -1) {
                        break;
                    } else {
                        output.append(new String(buffer, 0, readBytesNum,
                                StandardCharsets.UTF_8));
                    }
                }
                return output.toString();
            }
        } catch (final IOException exc) {
            LOG.error("Reading of last chars in standard output failed", exc);
        }
        return "";
    }

    protected final String readOutputCommand(final Pattern expectedPattern)
            throws TimeoutExpiredException, SocketTimeoutChangeException,
            CLISessionException {
        LOG.debug("Reading of output command in progress...");
        final StringBuilder output = new StringBuilder();
        final byte[] buffer = new byte[1024];
        int readBytesNum = 0;
        try {
            while (true) {
                readBytesNum = this.stdOut.read(buffer);
                if (readBytesNum == -1) {
                    throw new CLISessionException(
                            "The stdout stream is closed - output: \"" + output
                            + "\"", output.toString());
                }
                output.append(new String(buffer, 0, readBytesNum,
                        StandardCharsets.UTF_8));

                /* If the expected string is found in stdout buffer */
                if (findPattern(output, expectedPattern)) {
                    try {
                        this.changeSocketTimeout(INFINITE_WAIT_TIME_FOR_RESPONSE); // Infinite
                        // wait
                        // time
                        // for
                        // response
                        // setting
                    } catch (final SocketTimeoutChangeException exc) {
                        exc.setLastCommandOuput(output.toString());
                        throw exc;
                    }
                    if (LOG.isInfoEnabled()) {
                        LOG.info("The \"" + expectedPattern
                                + "\" pattern found in the output command");
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Reading of output command completed");
                        }
                    }
                    return output.toString();
                }
            }
        } catch (final SshIOException exc) // Handling Timeout error for SSH
        // connection
        {
            final SshException sshException = exc.getRealException();
            if (sshException != null) {
                if ((sshException.getReason() == SshException.SOCKET_TIMEOUT)
                        || (sshException.getReason() == SshException.PROMPT_TIMEOUT)) {
                    throw new TimeoutExpiredException(
                            "Timeout expired and the \""
                                    + expectedPattern
                                    + "\" pattern not found in the output command: \""
                                    + output + "\"", sshException,
                                    output.toString());
                } else {
                    throw new CLISessionException(
                            "An I/O error is occured during standard output command reading: "
                                    + sshException.getMessage() + " (Reason: "
                                    + sshException.getReason()
                                    + ") - output: \"" + output + "\"",
                                    output.toString());
                }
            }
            throw new CLISessionException(
                    "An I/O error is occured during standard output command reading - output: \""
                            + output + "\"", exc, output.toString());
        } catch (final SocketTimeoutException exc) // Handling Timeout error for
        // Telnet connection
        {
            throw new TimeoutExpiredException("Timeout expired and the \""
                    + expectedPattern
                    + "\" pattern not found in the output command: \"" + output
                    + "\"", exc, output.toString());
        } catch (final IOException exc) // Handling Generic I/O error
        {
            throw new CLISessionException(
                    "An I/O error is occured during standard output command reading - output: \""
                            + output + "\"", exc, output.toString());
        }
    }

    protected final String readOutputCommand(final Pattern expectedPattern,
            final Pattern errorPattern) throws TimeoutExpiredException,
            ErrorPatternFoundException, SocketTimeoutChangeException,
            CLISessionException {
        LOG.debug("Reading of output command in progress...");
        final StringBuilder output = new StringBuilder();
        final byte[] buffer = new byte[1024];
        int readBytesNum = 0;
        try {
            while (true) {
                readBytesNum = this.stdOut.read(buffer);
                if (readBytesNum == -1) {
                    throw new CLISessionException(
                            "The stdout stream is closed - output: \"" + output
                            + "\"", output.toString());
                }
                output.append(new String(buffer, 0, readBytesNum,
                        StandardCharsets.UTF_8));
                // If the expected string is found in stdout buffer
                if (findPattern(output, expectedPattern)) {
                    try {
                        this.changeSocketTimeout(INFINITE_WAIT_TIME_FOR_RESPONSE); // Infinite
                        // wait
                        // time
                        // for
                        // response
                        // setting
                    } catch (final SocketTimeoutChangeException exc) {
                        exc.setLastCommandOuput(output.toString());
                        throw exc;
                    }
                    if (LOG.isInfoEnabled()) {
                        LOG.info("The \"" + expectedPattern
                                + "\" pattern found in the output command.");
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Reading of output command completed");
                        }
                    }
                    return output.toString();
                } else if ((errorPattern != null)
                        && findPattern(output, errorPattern)) {
                    if (LOG.isInfoEnabled()) {
                        LOG.info("The \""
                                + errorPattern
                                + "\" error pattern found in the output command");
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("output: \"" + output + "\"");
                            LOG.debug("Reading of output command completed");
                        }
                    }
                    throw new ErrorPatternFoundException("The \""
                            + errorPattern
                            + "\" error pattern found in the output command",
                            output.toString());
                }
            }
        } catch (final SshIOException exc) // Handling Timeout error for SSH
        // connection
        {
            final SshException sshException = exc.getRealException();
            if (sshException != null) {
                if ((sshException.getReason() == SshException.SOCKET_TIMEOUT)
                        || (sshException.getReason() == SshException.PROMPT_TIMEOUT)) {
                    throw new TimeoutExpiredException(
                            "Timeout expired and the \""
                                    + expectedPattern
                                    + "\" pattern not found in the output command: \""
                                    + output + "\"", sshException,
                                    output.toString());
                } else {
                    throw new CLISessionException(
                            "An I/O error is occured during standard output command reading: "
                                    + sshException.getMessage() + " (Reason: "
                                    + sshException.getReason()
                                    + ") - output: \"" + output + "\"",
                                    output.toString());
                }
            }
            throw new CLISessionException(
                    "An I/O error is occured during standard output command reading - output: \""
                            + output + "\"", exc, output.toString());
        } catch (final SocketTimeoutException exc) // Handling Timeout error for
        // Telnet connection
        {
            throw new TimeoutExpiredException("Timeout expired and the \""
                    + expectedPattern
                    + "\" pattern not found in the output command: \"" + output
                    + "\"", exc, output.toString());
        } catch (final IOException exc) // Handling Generic I/O error
        {
            throw new CLISessionException(
                    "An I/O error is occured during standard output command reading - output: \""
                            + output + "\"", exc, output.toString());
        }
    }

    protected final boolean findPattern(final StringBuilder commadOutput,
            final Pattern pattern) {
        return pattern.matcher(commadOutput).find();
    }

    protected final Pattern validateMaxSessionAuthReachedMsgPattern(
            final String maxSessionAuthReachedMsgPattern)
                    throws InvalidPatternException {
        if (maxSessionAuthReachedMsgPattern != null) {
            try {
                return Pattern.compile(
                        patternPrefix.concat(maxSessionAuthReachedMsgPattern
                                + patterSuffix), Pattern.DOTALL);
            } catch (final Exception exc) {
                throw new InvalidPatternException(
                        "Invalid maxSessionAuthReached pattern", exc);
            }
        }
        return null;
    }

    public final int getWaitTimeForResponse() {
        return this.waitTimeForResponse;
    }

    public final void setWaitTimeForResponse(final int waitTimeForResponse) {
        this.waitTimeForResponse = waitTimeForResponse * 1000;
    }

    protected final void changeSocketTimeout(final int timeout)
            throws SocketTimeoutChangeException {
        try {
            this.setSocketTimeout(timeout);
        } catch (final SocketException exc) {
            throw new SocketTimeoutChangeException(
                    "Updating of waitTimeForResponse value failed", exc);
        }
    }

    protected abstract void setSocketTimeout(int timeout)
            throws SocketException;

    public boolean isBusy() {
        return this.busy;
    }

    public void setBusy(final boolean newState) {
        this.busy = newState;
    }

    public void setAbortRequestState(final AtomicBoolean askedSessionAbort) {
        this.askedSessionAbort = askedSessionAbort;
    }

    @Override
    public String toString() {
        return "CLISessionHandler details:\n\tIP Address: " + this.ipAddress
                + "\n\tPort: " + this.port + "\n\tUsername: " + this.username
                + "\n\tWait Time For Response: " + this.waitTimeForResponse
                + "\n\tCommand Prompt: " + this.commandPrompt
                + "\n\tAsked Session Abort Flag: " + this.askedSessionAbort
                + "\n\tBusy Flag: " + this.busy;
    }

    public abstract void openSession(String maxSessionAuthReachedMsgPattern)
            throws OpenConnectionException, TimeoutExpiredException,
            AuthenticationErrorException, ErrorPatternFoundException,
            SocketTimeoutChangeException, InvalidPatternException,
            CLISessionException;

    public abstract boolean isConnected();

    public abstract void closeSession();
}