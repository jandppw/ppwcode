package org.codehaus.mojo.was6;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;
import org.dom4j.Document;

/**
 * Installs an EAR into WebSphere Application Server.
 * 
 * @goal installApp
 * @author <a href="mailto:david@codehaus.org">David J. M. Karlsen</a>
 */
public class WsInstallAppMojo
    extends AbstractAppMojo
{
    /**
     * Flag for updating existing application or installing a brand new.
     * 
     * @parameter expression="${was6.updateExisting}" default-value="true"
     */
    private boolean updateExisting;

    /**
     * Name of target cluster to deploy to.
     * 
     * @parameter expression="${was6.targetCluster}"
     */
    private String targetCluster;

    /**
     * EAR archive to deploy.
     * 
     * @parameter expression="${was6.earFile}" default-value="${project.artifact.file}"
     */
    private File earFile;

    /**
     * {@inheritDoc}
     */
    protected String getTaskName()
    {
        return "wsInstallApp";
    }

    protected void prepareRemoteEnvironment() throws MojoFailureException {
    	// first create the remote working directory and upload the build script 
    	super.prepareRemoteEnvironment();
    	
    	//then upload the EAR file
    	Commandline uploadearcmdl = new Commandline();
    	uploadearcmdl.setExecutable("scp");
    	uploadearcmdl.createArg().setValue(earFile.getAbsolutePath());
    	uploadearcmdl.createArg().setValue(getRemoteHostUserName() + "@" + getRemoteHost() + ":" + getRemoteWorkingDirectory()); 
    	executeCmdLine(uploadearcmdl);
    }

    /**
     * {@inheritDoc}
     */
    protected void configureBuildScript( Document document )
        throws MojoExecutionException
    {
        super.configureBuildScript( document );

        if ( !earFile.canRead() )
        {
            throw new MojoExecutionException( "Bad archive: " + earFile.getAbsolutePath() );
        }

        if( getRemoteHost() == null) {
        	//we're running locally, so configure the ant task with the path to the 
        	//ear file on the local file system
        	configureTaskAttribute( document, "ear", earFile.getAbsolutePath() );
        } else {
        	//the command is executed remotely, the earfile will be in the remote working directory
        	configureTaskAttribute ( document, "ear", getRemoteWorkingDirectory() + '/' + earFile.getName());
        }

        StringBuffer options = new StringBuffer();

        options.append( "-appname " ).append( applicationName );

        if ( updateExisting )
        {
            options.append( " -update" );
        }

        if ( targetCluster != null )
        {
            options.append( " -cluster " ).append( targetCluster );
        }

        configureTaskAttribute( document, "options", options );
    }

}
