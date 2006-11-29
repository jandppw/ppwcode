/*<license>
  Copyright 2005, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/


package be.peopleware.exception_II;


/**
 * <p>This error is thrown when a programming error is detected
 *   during execution. This happens when the coder notices branches
 *   that can never be reached (<code>else</code>-statements in deep
 *   decision trees, or unreachable <code>default</code>-clause
 *   in a <code>switch</code>-statement, or a <code>catch</code>-clause
 *   of an exception that can never occur, ...).</p>
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
   * @post      new.getCause() == unexpected;
   */
  public ProgrammingError(final String message,
                          final Throwable unexpected) {
    super(defaultMessage(message, unexpected), unexpected);
  }

  /**
   * @param     unexpected
   *            The unexpected exception that occured, causing this
   *            error to be thrown, if that is the case.
   *
   * @post      new.getMessage().equals(defaultMessage(null, unexpected));
   * @post      new.getCause() == unexpected;
   */
  public ProgrammingError(final Throwable unexpected) {
    this(null, unexpected);
  }

  /**
   * @param     message
   *            The message that describes the programming error.
   *
   * @post      new.getMessage().equals(defaultMessage(message, null));
   * @post      new.getCause() == null;
   */
  public ProgrammingError(final String message) {
    this(message, null);
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
