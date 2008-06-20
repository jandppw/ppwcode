package org.ppwcode.util.reflect_I;



/**
 * Signals problems accessing member with name
 * {@link #getMemberName()} of class {@link #getClazz()}.
 * The {@link #getCause() cause} gives more detail.
 *
 * @author Jan Dockx
 *
 * @invar getClazz() != null;
 * @invar getMemberName() != null;
 */
public class NamedMemberException extends ReflectionException {

  /**
   * @pre clazz != null;
   * @pre memberName != null;
   * @post new.getClazz() == clazz;
   * @post new.getMemberName().equals(fqcn);
   * @post new.getMessage() == null;
   * @post new.getCause() == cause;
   */
  public NamedMemberException(Class<?> clazz, String memberName, Throwable cause) {
    super(cause);
    $clazz = clazz;
    $memberName = memberName;
  }



  /**
   * @basic
   */
  public Class<?> getClazz() {
    return $clazz;
  }

  /**
   * @invar $clazz != null;
   */
  private final Class<?> $clazz;



  /**
   * @basic
   */
  public String getMemberName() {
    return $memberName;
  }

  /**
   * @invar $memberName != null;
   */
  private final String $memberName;

}