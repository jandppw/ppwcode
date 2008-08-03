package org.ppwcode.util.reflect_I;



/**
 * Signals that a class with {@link #getFullyQualifiedClassName()}
 * could not be loaded. The {@link #getCause() cause} gives more detail.
 *
 * @author Jan Dockx
 *
 * @note partial copy from toryt_II_dev
 */
public class _CannotGetClassException extends _AbstractFqcnException {

  /**
   * @pre fqcn != null;
   * @post new.getFullyQualifiedClassName().equals(fqcn);
   * @post new.getMessage() == null;
   * @post new.getCause() == cause;
   */
  public _CannotGetClassException(String fqcn, Throwable cause) {
    super(fqcn, cause);
  }

}