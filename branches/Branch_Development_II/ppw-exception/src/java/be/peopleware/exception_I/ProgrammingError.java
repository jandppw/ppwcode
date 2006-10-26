/*<license>
  Copyright 2005, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/


package be.peopleware.exception_I;


import org.apache.commons.logging.Log;


/**
 * <p>This error is thrown when a programming error is detected
 *   during execution. This happens when the coder notices branches
 *   that can never be reached (<code>else</code>-statements in deep
 *   decision trees, or unreachable <code>default</code>-clause
 *   in a <code>switch</code>-statement, or a <code>catch</code>-clause
 *   of an exception that can never occur, ...).</p>
 * <p>The <code>signal</code> methods can be used
 *   to create instances, throw them, and log the situation.</p>
 *
 * @note The <code>signal</code>-methods use
 *       <a href="http://jakarta.apache.org/commons/logging/">Apache Jakarta Commons Logging</a>
 *       {@link Log} arguments. This is a debatable dependency for a general
 *       package like this. Comments from users will be taken into consideration
 *       to possibly change this.
 *
 * @invar     getMessage() != null;
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public class ProgrammingError extends Error {

  /*<section name="Meta Information">*/
  //------------------------------------------------------------------

  /** {@value} */
  public static final String CVS_REVISION = "$Revision$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_DATE = "$Date$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_STATE = "$State$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_TAG = "$Name$"; //$NON-NLS-1$

  /*</section>*/



  /**
   * Throw a {@link ProgrammingError} instance, with the given <code>message</code>.
   * If <code>log</code> is not <code>null</code>, this occurence is also written
   * to that log.
   *
   * @post false;
   * @throws ProgrammingError
   *         true;
   *         ProgrammingError(message, unexpected);
   *         (log != null) ? log.fatal(defaultMessage(message, unexpected), unexpected);
   */
  public static void signal(String message, Throwable unexpected, Log log) {
    if (log != null) {
      log.fatal(defaultMessage(message, unexpected), unexpected);
    }
    throw new ProgrammingError(message, unexpected);
  }

  /**
   * Throw a {@link ProgrammingError} instance, with the given <code>message</code>.
   *
   * @post false;
   * @throws ProgrammingError
   *         true;
   *         ProgrammingError(message, unexpected);
   */
  public static void signal(String message, Throwable unexpected) {
    signal(message, unexpected, null);
  }

  /**
   * Throw a {@link ProgrammingError} instance, with the given <code>message</code>.
   * If <code>log</code> is not <code>null</code>, this occurence is also written
   * to that log.
   *
   * @post false;
   * @throws ProgrammingError
   *         true;
   *         ProgrammingError(message, null);
   *         (log != null) ? log.fatal(defaultMessage(message, unexpected), unexpected);
   */
  public static void signal(String message, Log log) {
    signal(message, null, log);
  }

  /**
   * Throw a {@link ProgrammingError} instance, with the given <code>message</code>.
   *
   * @post false;
   * @throws ProgrammingError
   *         true;
   *         ProgrammingError(message, null);
   */
  public static void signal(String message) {
    signal(message, null, null);
  }

  /**
   * Throw a {@link ProgrammingError} instance, with the given <code>unexpected</code>.
   * If <code>log</code> is not <code>null</code>, this occurence is also written
   * to that log.
   *
   * @post false;
   * @throws ProgrammingError
   *         true;
   *         ProgrammingError(null, unexpected);
   *         (log != null) ? log.fatal(defaultMessage(message, unexpected), unexpected);
   */
  public static void signal(Throwable unexpected, Log log) {
    signal(null, unexpected, log);
  }


  /**
   * Throw a {@link ProgrammingError} instance, with the given <code>unexpected</code>.
   * If <code>log</code> is not <code>null</code>, this occurence is also written
   * to that log.
   *
   * @post false;
   * @throws ProgrammingError
   *         true;
   *         ProgrammingError(null, unexpected);
   */
  public static void signal(Throwable unexpected) {
    signal(null, unexpected, null);
  }
  /**
   * Throw a {@link ProgrammingError} instance, with the given <code>unexpected</code>.
   * If <code>log</code> is not <code>null</code>, this occurence is also written
   * to that log.
   *
   * @post false;
   * @throws ProgrammingError
   *         true;
   *         ProgrammingError(null, null);
   *         (log != null) ? log.fatal(defaultMessage(message, unexpected), unexpected);
   */
  public static void signal(Log log) {
    signal(null, null, log);
  }

  /**
   * Throw a {@link ProgrammingError} instance, with the given <code>unexpected</code>.
   * If <code>log</code> is not <code>null</code>, this occurence is also written
   * to that log.
   *
   * @post false;
   * @throws ProgrammingError
   *         true;
   *         ProgrammingError(null, null);
   *         (log != null) ? log.fatal(COULD_NOT_CONTINUE_MESSAGE);
   */
  public static void signal() {
    signal(null, null, null);
  }



  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * @param     message
   *            The message that describes the programming error.
   * @param     unexpected
   *            The unexpected exception that occured, causing this
   *            error to be thrown, if that is the case.
   *
   * @post      new.getMessage().equals(defaultMessage(message, unexpected));
   * @post      new.getCause() == cause;
   */
  public ProgrammingError(final String message,
                          final Throwable unexpected) {
    super(defaultMessage(message, unexpected), unexpected);
  }

  public final static String COULD_NOT_CONTINUE_MESSAGE =
    "Could not continue due to an unspecified programming error.";

  public final static String UNEXPECTED_MESSAGE = "An unexpected exception occured.";

  /**
   * @return (message != null) ?
   *           message :
   *           ((unexpected != null) ?
   *                 UNEXPECTED_MESSAGE :
   *                 COULD_NOT_CONTINUE_MESSAGE);
   */
  public static String defaultMessage(String message, Throwable unexpected) {
    return (message != null) ?
             message :
             ((unexpected != null) ?
                UNEXPECTED_MESSAGE :
                COULD_NOT_CONTINUE_MESSAGE);
  }

  /*</construction>*/

}
