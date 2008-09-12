package org.ppwcode.maven.plugin.was;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Build an EJB (and optional client) from the current project.
 *
 * @author Ruben Vandeginste
 * @version 0.0.1
 * @goal ejbstubs
 * @phase package
 */
public class WasStubsMojo
extends AbstractMojo
{
  /**
   * Root install location of WebSphere 6.1
   *
   * @parameter expression="${was6.wasHome}" default-value="${env.WAS_HOME}"
   * @required
   */
  private File wasHome;

  /**
   * host on which to run createEJBStubs remotely
   *
   * @parameter expression="${was6.remotehost}"
   */
  private String remoteHost;

  /**
   * Remote host where ant tasks will be run
   * @parameter expression="${was6.remotehost.username}" default-value="root"
   * @required
   */
  private String remoteHostUserName;

  /**
   * directory on remote host in which our files will be put
   *
   * @parameter expression="${was6.remotehost.workingdirectory}" default-value="/tmp/WASStubs-maven-plugin"
   */
  private String remoteWorkingDirectory;

  /**
   * The directory for the generated jar.
   *
   * @parameter expression="${project.build.directory}"
   * @required
   * @readonly
   */
  private String basedir;

  /**
   * The name of the jar file to generate.
   *
   * @parameter expression="${project.build.finalName}"
   * @required
   */
  private String jarName;

  /**
   * The Maven project.
   *
   * @parameter expression="${project}"
   * @required
   * @readonly
   */
  private MavenProject project;

  /**
   * The Maven project's helper.
   *
   * @parameter expression="${component.org.apache.maven.project.MavenProjectHelper}"
   * @required
   * @readonly
   */
  private MavenProjectHelper projectHelper;

  /**
   * The compileclasspath on the remote host
   */
  private String remoteclasspath;

  /**
   * returns the remote host on which the command must be run
   * @return the remote host on which the command must be run
   */
  protected String getRemoteHost()
  {
    return remoteHost;
  }

  /**
   * get the user name that is used to connect to the remote host
   */
  protected String getRemoteHostUserName()
  {
    return remoteHostUserName;
  }

  /**
   * Plugin working directory.
   *
   * @return
   */
  protected String getRemoteWorkingDirectory()
  {
    return remoteWorkingDirectory;
  }

  /**
   * Returns the list of jar files that must be in the classpath on the remote
   * host for createEJBStubs to succeed.
   * @return the remote class path
   */
  protected String getRemoteClassPath() {
    return remoteclasspath;
  }

  /**
   * Runs the createEJBStubs.sh command from the WebSphere install
   *
   */
  public void execute()  throws MojoExecutionException
  {
    if (remoteHost == null) {
      executeLocally();
    } else {
      executeRemotely();
    }
  }

  private void executeLocally() {
    File originalJarFile = getEJBJarFile( basedir, jarName, "client" );
    File newJarFile = getEJBJarFile( basedir, jarName, "wsclient" );

    String originalJarName = originalJarFile.getAbsolutePath();
    String newJarName = newJarFile.getAbsolutePath();

    getLog().info("Original   : "+originalJarName);
    getLog().info("New        : "+newJarName);

    List compileClasspathElementsList = new ArrayList();
    try {
      compileClasspathElementsList = project.getCompileClasspathElements();
    } catch (DependencyResolutionRequiredException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    String compileClasspath = StringUtils.join( compileClasspathElementsList, File.pathSeparator );
    getLog().info("Classpath  : "+compileClasspath);

    File executableFile = new File( wasHome, "bin/createEJBStubs." + ( SystemUtils.IS_OS_UNIX ? "sh" : "bat" ) );
    getLog().info("Executable : "+executableFile.getAbsolutePath());

    Commandline cmd = new Commandline();
    cmd.setExecutable(executableFile.getAbsolutePath());
    cmd.setWorkingDirectory(project.getBuild().getDirectory());
    cmd.createArg().setValue(originalJarName);
    cmd.createArg().setLine("-newfile " + newJarName);
    cmd.createArg().setLine("-cp "+ compileClasspath);
    cmd.createArg().setValue("-verbose");

    getLog().info("Command    :"+cmd.toString());

    try {
      Process p = cmd.execute();
      int val = p.waitFor();
    } catch (CommandLineException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    projectHelper.attachArtifact( project, "ejb", "wsclient", newJarFile );
  }

  private void executeRemotely() throws MojoExecutionException {
    File originalJarFile = getEJBJarFile( basedir, jarName, "client" );
    File newJarFile = getEJBJarFile( basedir, jarName, "wsclient" );

    String originalJarName = originalJarFile.getName();
    String newJarName = newJarFile.getName();

    getLog().info("Original   : " + originalJarName);
    getLog().info("New        : " + newJarName);

    cleanRemoteEnvironment();
    getLog().info("remote environment cleaned");
    uploadLibrariesAndDetermineRemoteClassPath();
    getLog().info("libraries uploaded");
    uploadOriginalJar(originalJarFile);
    getLog().info("original jar uploaded");
    executeCreateStubsRemotely(originalJarName, newJarName);
    getLog().info("stubs created");
    downloadNewJar(newJarFile);
    getLog().info("new jar downloaded");

    projectHelper.attachArtifact( project, "ejb", "wsclient", newJarFile );
  }

  private void cleanRemoteEnvironment() throws MojoExecutionException {
    Commandline cleancmd = createRemoteCommandLine();
    cleancmd.createArg().setLine("rm -rf ");
    cleancmd.createArg().setValue(getRemoteWorkingDirectory());

    if (executeCmdLine(cleancmd) != 0) {
      throw new MojoExecutionException ("Could not clean remote working directory ("
          + getRemoteWorkingDirectory() + ")");
    }
  }

  private void uploadLibrariesAndDetermineRemoteClassPath() throws MojoExecutionException {

    //get the classpath on the local machine so we can determine what to upload to the remote host
    List compileClasspathElementsList = new ArrayList();
    try {
      compileClasspathElementsList = project.getCompileClasspathElements();
    } catch (DependencyResolutionRequiredException e) {
      throw new MojoExecutionException(e.getMessage(), e);
    }

    //setting some convenience variables
    String remotedirectory = getRemoteWorkingDirectory() + "/lib";
    String remotedestination = getRemoteHostUserName() + "@" + getRemoteHost() + ":" + remotedirectory;

    //make a lib directory in the remote working directory
    Commandline createcmd = createRemoteCommandLine();
    createcmd.createArg().setLine("mkdir -p");
    createcmd.createArg().setValue(getRemoteWorkingDirectory() + "/lib");

    if (executeCmdLine(createcmd) != 0 ) {
      throw new MojoExecutionException("Could not create remote working diretory: " + remotedirectory);
    }

    // copy all files to the remote host, to the remote lib directory
    Commandline cpcmd = new Commandline();
    cpcmd.setExecutable("scp");
    cpcmd.setWorkingDirectory( project.getBuild().getDirectory() );
    cpcmd.createArg().setValue("-r");
    String fileList = StringUtils.join(compileClasspathElementsList, ' ');
    cpcmd.createArg().setLine(fileList);
    cpcmd.createArg().setValue(remotedestination);

    if (executeCmdLine(cpcmd) != 0) {
      throw new MojoExecutionException("Could not execute " + cpcmd.toString());
    }

    // determine the classpath on the remote host
    remotedirectory += '/';
    List remoteClasspathElementsList = new ArrayList();
    for ( Iterator iter = compileClasspathElementsList.iterator(); iter.hasNext(); ) {
      String filename = (String)(iter.next());
      remoteClasspathElementsList.add(remotedirectory + (new File(filename)).getName());
    }
    remoteclasspath = StringUtils.join(remoteClasspathElementsList, ':');
    getLog().debug("Classpath  : " + remoteclasspath);
  }

  private void uploadOriginalJar(File originalJarFile) throws MojoExecutionException {
    Commandline cpcmd = new Commandline();
    cpcmd.setExecutable("scp");
    cpcmd.setWorkingDirectory( project.getBuild().getDirectory() );

    cpcmd.createArg().setValue(originalJarFile.getAbsolutePath());
    cpcmd.createArg().setLine(
        getRemoteHostUserName()
        + '@' + getRemoteHost()
        + ':' + getRemoteWorkingDirectory());

    if (executeCmdLine(cpcmd) != 0) {
      throw new MojoExecutionException("Could not copy " + originalJarFile.getAbsolutePath()
          + " to " + getRemoteHost());
    }
  }

  private void executeCreateStubsRemotely(String originalJarName, String newJarName) throws MojoExecutionException {
    String remoteexecutable = wasHome + "/bin/createEJBStubs.sh";
    Commandline stubscmd = createRemoteCommandLine();
    stubscmd.setExecutable("ssh");
    stubscmd.setWorkingDirectory(project.getBuild().getDirectory());

    stubscmd.createArg().setValue(remoteexecutable);
    stubscmd.createArg().setValue(getRemoteWorkingDirectory() + '/' + originalJarName);
    stubscmd.createArg().setLine("-newfile " + getRemoteWorkingDirectory() + '/' + newJarName);
    stubscmd.createArg().setLine("-cp "+ getRemoteClassPath());
    stubscmd.createArg().setValue("-verbose");

    if (executeCmdLine(stubscmd) != 0) {
      throw new MojoExecutionException("Could not create the stubs remotely");
    }
  }

  private void downloadNewJar(File newJarFile) throws MojoExecutionException {
    String remotename = getRemoteHostUserName() + '@' + getRemoteHost()
      + ':' + getRemoteWorkingDirectory() + '/' + newJarFile.getName();
    String localname = newJarFile.getAbsolutePath();

    Commandline cpcmd = new Commandline();
    cpcmd.setExecutable("scp");
    cpcmd.setWorkingDirectory( project.getBuild().getDirectory() );
    cpcmd.createArg().setLine(remotename);
    cpcmd.createArg().setValue(localname);

    if (executeCmdLine(cpcmd) != 0) {
      throw new MojoExecutionException("Could not copy " + remotename
          + " to " + localname);
    }
  }


  protected Commandline createRemoteCommandLine() {
    Commandline commandLine = new Commandline();
    commandLine.setExecutable("ssh");
    commandLine.setWorkingDirectory( project.getBuild().getDirectory() );

    commandLine.createArg().setLine("-l " + getRemoteHostUserName());
    commandLine.createArg().setValue(getRemoteHost());
    return commandLine;
  }

  protected int executeCmdLine( Commandline commandline )  throws MojoExecutionException  {
    try  {
      StreamConsumer errConsumer = getStreamConsumer( "error" );
      StreamConsumer infoConsumer = getStreamConsumer( "info" );

      getLog().debug( "Executing command line: " + StringUtils.join( commandline.getShellCommandline(), " " ) );

      return CommandLineUtils.executeCommandLine( commandline, infoConsumer, errConsumer );
    }
    catch ( CommandLineException e ) {
      throw new MojoExecutionException( e.getMessage() );
    }
  }

  /**
   * Creates an streamconsumer which log's to maven log.
   *
   * @param level the log level to log to (info or error)
   * @return a {@link StreamConsumer}
   */
  private StreamConsumer getStreamConsumer( final String level )
  {
    StreamConsumer consumer = new StreamConsumer() {
      public void consumeLine( String line ) {
        if ( level.equalsIgnoreCase( "info" ) ) {
          getLog().info( line );
        } else {
          getLog().error( line );
        }
      }
    };
    return consumer;
  }

  /**
   * Returns the EJB Jar file to generate, based on an optional classifier.
   *
   * @param basedir    the output directory
   * @param finalName  the name of the ear file
   * @param classifier an optional classifier
   * @return the EJB file to generate
   */
  private static File getEJBJarFile( String basedir, String finalName, String classifier )
  {
    if ( classifier == null )
    {
      classifier = "";
    }
    else if ( classifier.trim().length() > 0 && !classifier.startsWith( "-" ) )
    {
      classifier = "-" + classifier;
    }

    return new File( basedir, finalName + classifier + ".jar" );
  }

}
