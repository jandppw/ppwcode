/*<license>
  Copyright 2006, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.exception_II;


import be.peopleware.metainfo.vcs.CvsInfo;


/**
 * <p>This error is thrown when a programming error is detected
 *   during execution. This happens when the coder notices branches
 *   that can never be reached (<code>else</code>-statements in deep
 *   decision trees, or unreachable <code>default</code>-clause
 *   in a <code>switch</code>-statement, or a <code>catch</code>-clause
 *   of an exception that can never occur, ...).</p>
 * <p>The {@link #getMessage() message} should describe the programming
 *   error as closely as possible. If you cannot pinpoint the exact
 *   nature of the programming error, you should say so explicitly.
 *   If you become aware of the programming error by catching an
 *   {@link Exception} or an {@link Error}, it should be carried by
 *   instances of this class as the {@link #getCause() cause}.</p>
 *
 * @invar     getMessage() != null;
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class ProgrammingError extends Error {

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
