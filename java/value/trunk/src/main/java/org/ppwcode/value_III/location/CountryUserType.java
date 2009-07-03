package org.ppwcode.value_III.location;

import org.ppwcode.value_III.id11n.EnumerationUserType;

public class CountryUserType extends EnumerationUserType {
  /**
   * Which type we return is defined by a parameter set on the definition of this user type. This should be a subtype of
   * {@link Country}.
   */
  @Override
  public Class<? extends Country> returnedClass() {
    return Country.class;
  }
}
