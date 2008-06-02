/*<license>
  Copyright 2006, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.exception_II;


import be.peopleware.metainfo.vcs.CvsInfo;


/**
 * <p>A {@link SemanticException} thrown while working with a
 *   JavaBean. The JavaBean is of type {@link #getBeanType()}.
 *   If the exception is not thrown by a constructor of the
 *   bean, {@link #getBean()} contains a reference to the
 *   bean we were working on. If the exception is thrown
 *   while constructing the bean, {@link #getBean()} is
 *   {@code null}.</p>
 *
 * @invar getBeanType() != null;
 * @invar getBean() != null ? getBeanType() == getBean().getClass();
 * @invar ! (getBean() instanceof Class);
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class BeanException extends InternalException {

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
  public BeanException(Object bean,
                       final String messageIdentifier,
                       final Throwable cause) {
    super(messageIdentifier, cause);
    assert bean != null;
    assert ! (bean instanceof Class);
    $beanType = bean.getClass();
    $bean = bean;
  }

  public BeanException(Class<?> beanType,
                       final String messageIdentifier,
                       final Throwable cause) {
    super(messageIdentifier, cause);
    assert beanType != null;
    $beanType = beanType;
  }

  /*</construction>*/



  /*<property name="bean">*/
  //------------------------------------------------------------------

  public final Object getBean() {
    return $bean;
  }

  private Object $bean;

  /*</property>*/



  /*<property name="beanType">*/
  //------------------------------------------------------------------

  public final Class<?> getBeanType() {
    return $beanType;
  }

  /**
   * @invar $beanType != null;
   * @invar $bean != null ? $beanType = $bean.getClass();
   */
  private Class<?> $beanType;

  /*</property>*/

}

