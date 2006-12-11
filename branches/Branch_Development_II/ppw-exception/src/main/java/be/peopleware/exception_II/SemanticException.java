/*<license>
  Copyright 2006, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.exception_II;


import be.peopleware.metainfo.vcs.CvsInfo;


/**
 * <p>Supertype for exceptions related to semantics:
 *   the nominal effect of a method could not be reached,
 *   because doing so under the given circumstances would
 *   violate semantics (often type invariants).</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class SemanticException extends InternalException {

  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * @param     messageIdentifier
   *            The string that identifies a localized
   *            end user feedback message about the
   *            non-nominal behavior.
   * @param     cause
   *            The exception that occured, causing this
   *            exception to be thrown, if that is the case.
   * @pre       (messageIdentifier == null) ||
   *            EMPTY.equals(messageIdentifier) ||
   *            validMessageIdentifier(messageIdentifier);
   * @post      new.getMessage().equals((messageIdentifier == null) || (EMPTY.equals(messageIdentifier)) ?
   *                                       DEFAULT_MESSAGE_IDENTIFIER :
   *                                       messageIdentifier);
   * @post      new.getCause() == cause;
   */
  public SemanticException(final String messageIdentifier,
                           final Throwable cause) {
    super(messageIdentifier, cause);
  }

  /*</construction>*/

}

