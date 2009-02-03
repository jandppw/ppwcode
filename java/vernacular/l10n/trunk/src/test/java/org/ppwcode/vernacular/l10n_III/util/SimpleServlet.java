/*<license>
Copyright 2009 - $Date: 2008-10-15 18:22:22 +0200 (Wed, 15 Oct 2008) $ by PeopleWare n.v..

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
package org.ppwcode.vernacular.l10n_III.util;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  Servlet used for testing.
 *
 * @author rvdginste
 */
public class SimpleServlet extends HttpServlet {

  private String contentBody = "";
  private String contentType = "text/html";

  public SimpleServlet(String type, String body) {
    super();
    contentType = type;
    contentBody = body;
  }

  private void doService(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    response.setContentType(contentType);
    PrintWriter out = response.getWriter();
    out.append(contentBody);
    out.close();
    response.setContentLength(contentBody.length());
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    doService(request, response);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    doService(request, response);
  }
}
