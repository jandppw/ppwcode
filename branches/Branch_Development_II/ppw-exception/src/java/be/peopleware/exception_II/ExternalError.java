/*<license>
  Copyright 2005, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/


package be.peopleware.exception_II;


/**
 * <p>This error is thrown when an external condition occurs,
 *   which we know can happen (however unlikely), which we do not
 *   want to deal with in our application. Most often, these are
 *   exceptional conditions of a technical nature.</p>
 *
 * @invar     getMessage() != null;
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public class ExternalError extends Error {

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
