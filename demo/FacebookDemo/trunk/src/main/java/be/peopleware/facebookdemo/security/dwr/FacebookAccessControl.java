package be.peopleware.facebookdemo.security.dwr;

import java.lang.reflect.Method;

import org.directwebremoting.WebContextFactory;
import org.directwebremoting.extend.Creator;
import org.directwebremoting.impl.DefaultAccessControl;
import org.directwebremoting.util.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class FacebookAccessControl extends DefaultAccessControl {

	@Override
	public void assertExecutionIsPossible(Creator creator, String className, Method method) throws SecurityException {
		super.assertExecutionIsPossible(creator, className, method);
		log.info("FacebookAccessControl::assertExecutionIsPossible for " + method.toString());
		HttpServletRequest req = WebContextFactory.get().getHttpServletRequest();
		Cookie[] cookies = req.getCookies();
		System.out.println("Cookies are:");
		for (int i = 0; i < cookies.length; i++) {
			System.out.println(cookies[i].getName() + ": " + cookies[i].getPath() + ": " + cookies[i].getValue());
		}
	}

  /**
   * The log stream
   */
  private static final Logger log = Logger.getLogger(FacebookAccessControl.class);

	
}
