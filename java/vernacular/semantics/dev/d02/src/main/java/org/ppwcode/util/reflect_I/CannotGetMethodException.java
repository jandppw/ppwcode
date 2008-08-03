package org.ppwcode.util.reflect_I;



/**
 * Signals that we could not get the value of a constant with name
 * {@link #getMemberName()} from class {@link #getClazz()}.
 * The {@link #getCause() cause} gives more detail.
 *
 * @author Jan Dockx
 *
 * @invar getConstantName() != null;
 * @note partial copy from toryt_II_dev
 */
public class CannotGetMethodException extends NamedMemberException {

  /**
   * @pre clazz != null;
   * @pre constantName != null;
   * @post new.getClazz() == clazz;
   * @post new.getConstantName().equals(fqcn);
   * @post new.getMessage() == null;
   * @post new.getCause() == cause;
   */
  public CannotGetMethodException(Class<?> clazz, String memberName, Throwable cause) {
    super(clazz, memberName, cause);
  }

}