/*<license>
  Copyright 2006, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.exception_II;


import be.peopleware.metainfo.vcs.CvsInfo;


/**
 * <p>This error is thrown when an external condition occurs,
 *   which we know can happen (however unlikely), which we do not
 *   want to deal with in our application. Most often, these are
 *   exceptional conditions of a technical nature.</p>
 * <p>The {@link #getMessage() message} should express the error as
 *   closely as possible. If you cannot pinpoint the exact nature of
 *   the error, the message should say so explicitly. If you become
 *   aware of the external condition you do not want to deal with through
 *   an {@link Exception} or an {@link Error}, it should be carried
 *   by an instance of this class as its {@link #getCause() cause}.</p>
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
public class ExternalError extends Error {

  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * @param     message
   *            The message that describes the external exceptional condition.
   * @param     unexpected
   *            The unexpected exception that occured, causing this
   *            error to be thrown, if that is the case.
   *
   * @post      new.getMessage().equals(defaultMessage(message, unexpected));
   * @post      new.getCause() == unexpected;
   */
  public ExternalError(final String message,
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
  public ExternalError(final Throwable unexpected) {
    this(null, unexpected);
  }

  /**
   * @param     message
   *            The message that describes the external exceptional condition.
   *
   * @post      new.getMessage().equals(defaultMessage(message, null));
   * @post      new.getCause() == null;
   */
  public ExternalError(final String message) {
    this(message, null);
  }

  /**
   * @post      new.getMessage().equals(defaultMessage(null, null));
   * @post      new.getCause() == null;
   */
  public ExternalError() {
    this(defaultMessage(null, null), null);
  }

  public final static String COULD_NOT_CONTINUE_MESSAGE =
    "Could not continue due to an unspecified external error.";

  public final static String UNEXPECTED_MESSAGE = "An unexpected external exception occured.";

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
