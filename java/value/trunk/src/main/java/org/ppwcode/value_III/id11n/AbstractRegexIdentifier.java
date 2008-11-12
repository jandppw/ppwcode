/*<license>
Copyright 2004 - $Date$ by PeopleWare n.v..

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</license>*/

package org.ppwcode.value_III.id11n;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.util.reflect_I.ConstantHelpers.constant;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.pre;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.unexpectedException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;
import org.toryt.annotations_I.MethodContract;
import org.toryt.annotations_I.Throw;


/**
 * <p>General support for identifiers in which the identifier is constrained by a grep
 *  pattern. Groups in the pattern can be given a name and used as properties.</p>
 *
 * @protected
 * <p>Subclasses must provide a String constant with name {@link #REGEX_PATTERN_NAME} of type
 *   {@link Pattern}, containing the regex pattern that describes the identifier Strings that are acceptable.
 *   The pattern should be defined using {@link Pattern#compile(String)} or {@link Pattern#compile(String, int)}.
 *   In this pattern, regex groups can be defined, which can be retrieved using {@link #patternGroup(int)}.
 *   If these groups, or derivations thereof, are of interest to users of the identifier,
 *   they should be exposed with meaningful names in the subclass. Remember that, in selecting
 *   a pattern group, group {@code 0} is the complete identifier, and actual groups start counting
 *   at {@code 1}.</p>
 * <p>A subclass should always provide a constructor that takes a candidate String as argument, and
 *   calls the super constructor of this class with that candidate String. The constructor in this class
 *   verifies the given candidate String against the pattern specified in the String constant with name
 *   {@link #REGEX_PATTERN_NAME} defined in the subclass. After this call, the subclass may do further
 *   verification if applicable.</p>
 * <p>It is in general a bad idea for classes like this, which are serialized and send
 *   over the wire often, which are persisted in databases often, to choose storing extra
 *   data over small recalculations. Therfor, {@link #patternGroup(int)} recalculates the match each time
 *   it is used.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare NV
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
@Invars({
  @Expression("isConstant(getClass(), REGEX_PATTERN_NAME)")
})
public abstract class AbstractRegexIdentifier extends AbstractIdentifier {

  @MethodContract(
    pre  = {
      @Expression("_identifier != null"),
      @Expression("_identifier != EMPTY")
    },
    post = {
      @Expression("identifier == _identifier")
    },
    exc  = {
      @Throw(type = IdentifierWellformednessException.class, cond = @Expression("true")),
      @Throw(type = IdentifierWellformednessException.class,
             cond = @Expression("! regexPattern.matcher(identifier).matches()"))
    }
  )
  protected AbstractRegexIdentifier(String identifier) throws IdentifierWellformednessException {
    super(identifier);
    Pattern regex = getRegexPattern();
    Matcher m = regex.matcher(identifier);
    boolean matches = m.matches();
    if (! matches) {
      throw new IdentifierWellformednessException(getClass(), getIdentifier(), "NOT_CONSISTENT_WITH_GREP_PATTERN", null);
    }
  }

  /**
   * <p>The name of the required constant of type {@link Pattern} in each concrete subclass that
   *   holds the regex pattern.</p>
   * <p><code>REGEX_PATTERN_NAME == <strong>{@value}</strong></code>.</p>
   */
  public final static String REGEX_PATTERN_NAME = "REGEX_PATTERN";

  /**
   * This method uses reflection to get the value of a constant (static final)
   * using dynamic binding. This is done so that we do not need to load
   * what is essentially type-level information into each instance.
   * Since this is needed in the constructor, though, a regular abstract
   * dynamic instance method leaves open the door to instantiation problems.
   * This is a bit safer.
   */
  @MethodContract(
    post = @Expression("constant(getClass(), REGEX_PATTERN_NAME)")
  )
  public final Pattern getRegexPattern() {
    return constant(getClass(), REGEX_PATTERN_NAME);
  }

  /**
   * The capturing group with index {@code patternGroupIndex} of {@link #getIdentifier()}
   * defined in pattern {@link #getRegexPattern()}.
   */
  @MethodContract(
    pre  = {
      @Expression("_patternGroupIndex >= 0"),
      @Expression("_patternGroupIndex <= regexPattern.matcher(identifier).groupCount()")
    },
    post = {
      @Expression("regexPattern.matcher(identifier).group(_patternGroupIndex)")
    }
  )
  public final String patternGroup(int patternGroupIndex) {
    pre(patternGroupIndex >= 0, "patternGroupIndex >= 0");
    Matcher m = getRegexPattern().matcher(getIdentifier());
    boolean matchOk = m.matches();
    pre(patternGroupIndex <= m.groupCount(), "patternGroupIndex <= m.groupCount()");
    assert matchOk;
    String result = null;
    try {
      result = m.group(patternGroupIndex);
    }
    catch (IllegalStateException isExc) {
      unexpectedException(isExc, "we just did a match");
    }
    catch (IndexOutOfBoundsException ioobExc) {
      unexpectedException(ioobExc, "there is no pattern group with index " +  patternGroupIndex +
                                   " in regex patter " +  getRegexPattern().pattern());
    }
    return result;
  }

}
