package org.ppwcode.util.reflect_I;



/**
 * Signals that a bean initialisation for class with {@link #getFullyQualifiedClassName()}
 * could not be loaded. The {@link #getCause() cause} gives more detail.
 *
 * @author Jan Dockx
 *
 * @invar getFullyQualifiedClassName() != null;
 *
 * @note partial copy from toryt_II_dev
 */
public abstract class _AbstractFqcnException extends _ReflectionException {

  /**
   * @pre fqcn != null;
   * @post new.getFullyQualifiedClassName().equals(fqcn);
   * @post new.getMessage() == null;
   * @post new.getCause() == cause;
   */
  public _AbstractFqcnException(String fqcn, Throwable cause) {
    super(cause);
    assert fqcn != null;
    $fqcn = fqcn;
  }

  /**
   * @basic
   */
  public String getFullyQualifiedClassName() {
    return $fqcn;
  }

  /**
   * @invar $fqcn != null;
   */
  private final String $fqcn;

}