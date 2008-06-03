/*<license>
Copyright 2008 - $Date: 2008/04/03 22:19:23 $ by Jan Dockx.

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


package org.ppwcode.metainfo_I.reader;


import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * <p>Utility methods for reflection. We do not use a utility library, because
 *   we do not want dependencies on this library.</p>
 *
 * @author Jan Dockx
 */
@SvnInfo(revision = "$Revision: 857 $",
         date     = "$Date: 2007-05-08 16:33:08 +0200 (Tue, 08 May 2007) $")
class _Classes {

  private _Classes() {
    // NOP
  }

  /**
   * {@link Class#forName(String)} that has a simpler exception model, also
   * works for primitive types, and has an embedded &quot;import&quot; for
   * the package {@code java.lang}. This method also handles member types
   * with the dotnotation (where {@link Class#forName(String)} requires
   * &quot;$&quot; separation for member types).
   *
   * @result result != null
   * @result "boolean".equals(fqn) ?? result == Boolean.TYPE;
   * @result "byte".equals(fqn) ?? result == Byte.TYPE;
   * @result "char".equals(fqn) ?? result == Character.TYPE;
   * @result "short".equals(fqn) ?? result == Short.TYPE;
   * @result "int".equals(fqn) ?? result == Integer.TYPE;
   * @result "long".equals(fqn) ?? result == Long.TYPE;
   * @result "float".equals(fqn) ?? result == Float.TYPE;
   * @result "double".equals(fqn) ?? result == Double.TYPE;
   * @result (! "boolean".equals(fqn)) && (! "byte".equals(fqn)) &&
   *           (! "char".equals(fqn)) && (! "short".equals(fqn)) &&
   *           (! "int".equals(fqn)) && (! "long".equals(fqn)) &&
   *           (! "float".equals(fqn)) && (! "double".equals(fqn)) ?
   *         (result = Class.forName(fqn)) || (result == Class.forName("java.lang." + fqn);
   * @throws CannotGetClassException
   *         fqn == null;
   * @throws CannotGetClassException
   *         (! "boolean".equals(fqn)) && (! "byte".equals(fqn)) &&
   *           (! "char".equals(fqn)) && (! "short".equals(fqn)) &&
   *           (! "int".equals(fqn)) && (! "long".equals(fqn)) &&
   *           (! "float".equals(fqn)) && (! "double".equals(fqn)) ?
   *         Class.forName(fqn) throws && Class.forName("java.lang." + fqn) throws;
   */
  public static Class<?> loadForName(String fqn) throws CannotGetClassException {
    if (fqn == null) {
      throw new CannotGetClassException(fqn, new NullPointerException("fqn is null"));
    }
    else if ("boolean".equals(fqn)) {
      return Boolean.TYPE;
    }
    else if ("byte".equals(fqn)) {
      return Byte.TYPE;
    }
    else if ("char".equals(fqn)) {
      return Character.TYPE;
    }
    else if ("short".equals(fqn)) {
      return Short.TYPE;
    }
    else if ("int".equals(fqn)) {
     return Integer.TYPE;
    }
    else if ("long".equals(fqn)) {
      return Long.TYPE;
    }
    else if ("float".equals(fqn)) {
      return Float.TYPE;
    }
    else if ("double".equals(fqn)) {
      return Double.TYPE;
    }
    else {
      try {
        try {
          return Class.forName(fqn);
        }
        catch (ClassNotFoundException cnfExc) {
          if (! fqn.contains(".")) {
            // there are no member classes in java.lang, are there?
            try {
              return Class.forName("java.lang." + fqn);
            }
            catch (ClassNotFoundException cnfExc2) {
              throw new CannotGetClassException(fqn, cnfExc2);
            }
          }
          else { // let's try for member classes
            // from right to left, replace "." with "$"
            String[] names = fqn.split("\\."); // regex
            for (int i = names.length - 2; i >= 0; i--) {
              StringBuffer build = new StringBuffer();
              for (int j = 0; j < names.length; j++) {
                build.append(names[j]);
                if (j < names.length - 1) {
                  build.append((j < i) ? "." : "$");
                }
              }
              String tryThis = build.toString();
              try {
                return Class.forName(tryThis);
              }
              catch (ClassNotFoundException cnfExc2) {
                // NOP; try with i--
              }
            }
            // if we get here, we finally give up
            throw new CannotGetClassException(fqn, null);
          }
        }
      }
      catch (LinkageError lErr) {
        // also catches ExceptionInInitializerError
        throw new CannotGetClassException(fqn, lErr);
      }
    }
  }

}