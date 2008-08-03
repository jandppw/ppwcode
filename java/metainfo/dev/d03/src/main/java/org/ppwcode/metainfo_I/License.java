/*<license>
Copyright 2007 - $Date: 2008-03-15 18:07:05 +0100 (Sat, 15 Mar 2008) $ by PeopleWare n.v.

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

package org.ppwcode.metainfo_I;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.MalformedURLException;
import java.net.URL;

import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * Annotation for license information. By using this annotation,
 * the license information is available in the code.
 *
 * Usage pattern:
 * <pre>
 * ATLicense(APACHE_V2)
 * public class ... {
 *  ...
 * }
 * </pre>
 *
 * @author    Jan Dockx
 */
@Copyright("2007 - $Date: 2008-03-15 18:07:05 +0100 (Sat, 15 Mar 2008) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 1082 $",
         date     = "$Date: 2008-03-15 18:07:05 +0100 (Sat, 15 Mar 2008) $")
@Documented
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface License {

  /**
   * Supported license types.
   *
   * @note This list will be extended as necessary.
   */
  public enum Type {
    APACHE_V2    ("http://www.apache.org/licenses/LICENSE-2.0.html"),
    GPL_v2       ("http://www.gnu.org/licenses/old-licenses/gpl-2.0.html"),
    GPL_v3       ("http://www.fsf.org/licensing/licenses/gpl.html"),
    LGPL_v2_1    ("http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html"),
    LGPL_v3      ("http://www.fsf.org/licensing/licenses/lgpl.html"),
    MPL_v1_1     ("http://www.mozilla.org/MPL/MPL-1.1.html");

    Type(String url) {
      try {
        $url = new URL(url);
      }
      catch (MalformedURLException exc) {
        assert false : "MalformedURLException in definition of Type instance";
      }
    }

    public URL getUrl() {
      return $url;
    }

    private URL $url;

    private static Class<?>[] contentTypes = {InputStream.class};

    public final Reader getReader() throws Exception {
      try {
        InputStream result = (InputStream)getUrl().getContent(contentTypes);
        return new InputStreamReader(result);
      }
      catch (IOException exc) {
        throw new Exception(); // MUDO good exception
      }
    }

  }

  /**
   * Name of the license.
   *
   * @note This list will be extended as necessary.
   */
  Type value();

}