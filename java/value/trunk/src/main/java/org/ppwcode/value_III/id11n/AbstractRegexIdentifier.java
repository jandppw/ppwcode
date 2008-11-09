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
 * @author    Jan Dockx
 * @author    PeopleWare NV
 */
@Copyright("2008 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
@Invars({
  @Expression("isConstant(getClass(), REGEX_PATTERN_NAME)")
//  @Expression("isConstant(getClass(), GROUP_NAMES_NAME)")
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
  protected AbstractRegexIdentifier(String identifier)
      throws IdentifierWellformednessException {
    super(identifier);
    Pattern regex = getRegexPattern();
    Matcher m = regex.matcher(identifier);
    boolean matches = m.matches();
    if (! matches) {
      throw new IdentifierWellformednessException(getClass(), getIdentifier(), "NOT_CONSISTENT_WITH_GREP_PATTERN", null);
    }
/*
 * The code below looks interesting, but is untested and not needed (yet)
 */
//    else {
//      $matchGroups = new MatchGroup[m.groupCount()];
//      for (int i = 0; i < $matchGroups.length; i++) {
//        int groupIndex = i + 1;
//        $matchGroups[i] = new MatchGroup(m.start(groupIndex), m.end(groupIndex));
//      }
//    }
  }

  /**
   * The name of the required constant in each concrete subclass that
   * holds the regex pattern.
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


/*
 * The code below looks interesting, but is untested and not needed (yet)
 */
//  private class MatchGroup {
//
//    public MatchGroup(int s, int e) {
//      start = s;
//      end = e;
//    }
//
//    public final int start;
//
//    public final int end;
//
//    public final String get() {
//      return getIdentifier().substring(start, end);
//    }
//
//  }
//
//  // IDEA let's make this transient, and recalculate on deserialization
//  private final MatchGroup[] $matchGroups;
//
//
//
//  /**
//   * The name of the required constant in each concrete subclass that
//   * holds the names for groups defined in the regex expression (the
//   * constant with name {@link #REGEX_PATTERN_NAME}..
//   */
//  public final static String GROUP_NAMES_NAME = "GROUP_NAMES";
//
//  /**
//   * This method uses reflection to get the value of a constant (static final)
//   * using dynamic binding.
//   */
//  @MethodContract(
//    post = @Expression("constant(getClass(), GROUP_NAMES_NAME)")
//  )
//  public final String[] groupNames() {
//    return constant(getClass(), GROUP_NAMES_NAME);
//  }
//
//  @MethodContract(
//    pre  = {
//      @Expression("_groupName != null"),
//      @Expression("_groupName != EMPTY"),
//      @Expression("groupNames().contains(_groupName)")
//    },
//    post = {
//      @Expression("regexPattern.matcher(identifier).group(groupIndex(_groupName))")
//    }
//  )
//  public final String getGroup(String groupName) {
//    int groupIndex = groupIndex(groupName);
//    return $matchGroups[groupIndex].get();
//  }
//
//
//  @MethodContract(
//    pre  = {
//      @Expression("_groupName != null"),
//      @Expression("_groupName != EMPTY"),
//      @Expression("groupNames().contains(_groupName)")
//    },
//    post = {
//      @Expression("groupNames()[result] == groupName")
//    }
//  )
//  public int groupIndex(String groupName) {
//    assert preArgumentNotEmpty(groupName, "groupName");
//    String[] groupNames = groupNames();
//    for (int i = 0; i < groupNames.length; i++) {
//      if (groupNames[i].equals(groupName)) {
//        return i;
//      }
//    }
//    deadBranch("\"" + groupName + "\" is not defined as a group for instances of " + getClass());
//    return -1; // keep compiler happy
//  }

}
