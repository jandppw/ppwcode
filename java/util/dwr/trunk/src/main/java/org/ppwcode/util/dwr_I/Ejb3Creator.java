/*<license>
Copyright 2004 - $Date: 2008-11-16 14:33:57 +0100 (Sun, 16 Nov 2008) $ by PeopleWare n.v..

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

package org.ppwcode.util.dwr_I;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.directwebremoting.create.AbstractCreator;
import org.directwebremoting.extend.Creator;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Messages;

/**
 * A Creator that works against EJB3 beans
 * @author Squishy [Squishy_I at gmx dot net]
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Ejb3Creator extends AbstractCreator implements Creator
{
    /**
     * The common interface of the Bean.
     * <b>If you don't have a common interface from which local and remote are
     * derived, you have to set the bean name manually!</b>
     * The BeanName is fetched from the part of the String behind the last '.'
     * @param className The fully qualified class name of the Bean's interface
     */
    public void setInterface(String className)
    {
        this.className = className;
    }

    /**
     * Set the name by which the session bean is known on the directory server.
     * The initial context parameters for the directory server must be configured
     * in a file /jndi.properties that must be located on your classpath.
     * @param name the JNDI name of the session bean
     */
    public void setJndiName(String name) {
    	this.jndiName = name;
    }
    

    /* (non-Javadoc)
     * @see org.directwebremoting.Creator#getType()
     */
    public Class getType()
    {
        try
        {
            return LocalUtil.classForName(className);
        }
        catch (ClassNotFoundException ex)
        {
            throw new IllegalArgumentException(Messages.getString("Creator.BeanClassNotFound", className));
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Creator#getInstance()
     */
    public Object getInstance() throws InstantiationException
    {

        try
        {
            Properties props = new Properties();

            props.load(getClass().getResourceAsStream("/jndi.properties"));
//            props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.cosnaming.CNCtxFactory");
//            props.put(Context.PROVIDER_URL, "corbaloc:iiop:mars.gast.arista.be:9100/NameServiceServerRoot");

            Context jndi = new InitialContext(props);
            	
            Object retval = jndi.lookup(jndiName);
            return retval;
        }
        catch (Exception ex)
        {
            throw new InstantiationException(jndiName + " not bound: " + ex.getMessage());
        }
    }

    /**
     * The name by which the bean is known in the directory server
     */
    private String jndiName;
    
    /**
     * The type of the bean
     */
    private String className;

}
