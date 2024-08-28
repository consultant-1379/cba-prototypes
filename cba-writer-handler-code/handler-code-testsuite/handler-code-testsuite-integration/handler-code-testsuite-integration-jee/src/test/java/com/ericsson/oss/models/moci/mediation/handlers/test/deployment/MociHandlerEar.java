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
package com.ericsson.oss.models.moci.mediation.handlers.test.deployment;

import static com.ericsson.oss.models.moci.mediation.handlers.test.deployment.Artifact.CBA_WRITER_HANDLER_EAR;
import static com.ericsson.oss.models.moci.mediation.handlers.test.deployment.Artifact.CBA_WRITER_HANDLER_JAR;
import static com.ericsson.oss.models.moci.mediation.handlers.test.deployment.Artifact.MOCKITO_ALL;

import java.io.File;

import org.jboss.shrinkwrap.api.Node;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

import com.ericsson.oss.models.moci.mediation.handlers.api.NodeConnectionProvider;
import com.ericsson.oss.models.moci.mediation.handlers.test.mocks.MockMociCMConnectionProvider;
import com.ericsson.oss.models.moci.mediation.handlers.test.mocks.MockNetworkElementConnection;
import com.ericsson.oss.models.moci.mediation.handlers.test.util.ConnectionProviderVerifier;

public class MociHandlerEar {
    
    public static EnterpriseArchive createEar() {
        final JavaArchive mociJar = Deployments.createJavaArchive(CBA_WRITER_HANDLER_JAR);
        mociJar.addClass(ConnectionProviderVerifier.class);
        mociJar.addClass(MockNetworkElementConnection.class);
        mociJar.addClass(MockMociCMConnectionProvider.class);
        mociJar.addClass(NodeConnectionProvider.class);
        mociJar.delete("META-INF/beans.xml");
        final File beansXML = new File("src/test/resources/META-INF/beans.xml");
        mociJar.addAsManifestResource(beansXML);
        final EnterpriseArchive ear = Deployments.createEnterpriseArchiveDeployment(CBA_WRITER_HANDLER_EAR);
        final Node node = ear.get(mociJar.getName());
        ear.delete(node.getPath());
        ear.addAsModule(mociJar);
        ear.addAsLibrary(Artifact.resolveArtifactWithoutDependencies(MOCKITO_ALL));
        return ear;

    }
    
    

}
