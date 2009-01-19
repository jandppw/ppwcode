package org.ppwcode.util.dwr_I;

import javax.ejb.EJBException;

import org.directwebremoting.extend.Call;
import org.directwebremoting.extend.Reply;

public class EjbRemoter extends org.directwebremoting.impl.DefaultRemoter {

	@Override
	public Reply execute(Call c) {
		Reply r = super.execute(c);
		Reply answer = null;
		Throwable th = r.getThrowable();
		if (null == th) {
			//normal reply
			answer = r;
		} else if (!(th instanceof EJBException)) {
			// if throwable is not an EJBException, use original reply 
			answer = r;
		} else { //exception is EJBException, strip off EJBException
			answer = new Reply(r.getCallId(), null, ((EJBException) th).getCausedByException());
		}
		return answer;
	}
}
