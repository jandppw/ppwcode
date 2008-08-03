// MUDO missing license
package org.ppwcode.util.reflect_I;




/**
 * Signals problems with reflection.
 *
 * @author Jan Dockx
 *
 * @note partial copy from toryt_II_dev
 */
public abstract class _ReflectionException extends Exception {

  /**
   * @post new.getMessage() == null;
   * @post new.getCause() == cause;
   */
  protected _ReflectionException(Throwable cause) {
    super(null, cause);
  }

  /**
   * @post equalsWithNull(getMessage(), message);
   * @post new.getCause() == null;
   */
  protected _ReflectionException(String message) {
    super(message, null);
  }

}
