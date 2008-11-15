/*<license>
Copyright 2004 - $Date$ by PeopleWare n.v.

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

package org.ppwcode.vernacular.semantics_VI.bean;

import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.vernacular.semantics_VI.exception.CompoundPropertyException;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;
import org.toryt.annotations_I.Throw;



/**
 * <p>A type that formalizes how to work with JavaBeans that represent
 *   real-world objects during only a part of there life cycle.</p>
 * <a href="http://en.wikipedia.org/wiki/Jean-Jacques_Rousseau">
 * <img src="doc-files/180px-Jean-Jacques_Rousseau_(painted_portrait).jpg"
 *      align="right" /></a>
 * <p>Since JavaBeans are required to have a default constructor, often
 *   some properties cannot be given a semantically acceptable value
 *   at instantiation. This triggers a pattern where instances of the
 *   bean class can exist in a state where it does not represent a
 *   real-world object of the type the class is intended for, i.e.,
 *   the instances do not conform to type invariants that would apply
 *   direct representations of the real-world objects. Such beans
 *   are created in a <dfn>wild</dfn> state, then
 *   go through a setup phase where a number of properties are set,
 *   and then enter a lifecycle phase where they do represent a
 *   real-world object of the type the class is intended for (they become
 *   <dfn>civilized</dfn>).
 *   Typically, by changing one or more properties, such objects
 *   can also leave the civilized state, which typically happens before
 *   the object is terminated.</p>
 * <p>This type offers a number of methods to support this pattern.</p>
 * <p>Normally, invariants are specified and enforced as much as possible.
 *   This is possible for all properties for which there exists a
 *   <dfn>civilized</dfn> default value that can be set in the default
 *   constructor. Typically, this is at least not possible with
 *   associations, if the association is mandatory.</p>
 * <p>The extra rules that should apply in a <dfn>civilized</dfn> state
 *   can be checked by calling {@link #wildExceptions()}.
 *   {@link #civilized()} gives a simple boolean answer about the state
 *   of the Rousseau bean. It is possible that certain subtypes are able
 *   to change their properties from a <dfn>wild</dfn> state to a
 *   <dfn>civilized</dfn> state, without actually changing the semantics
 *   of the stored information. This is called <dfn>normalization</dfn>
 *   ({@link #normalize()}). Normalization keeps doesn't change the nominal
 *   state of a {@code RousseauBean)}.</p>
 *
 * @note since VI, this is split into {@link SemanticBean} and this class.
 * @note In a previous version, we had a method {@code hasSameValues(RousseaBean rb)},
 *       meant to express the difference of having the samen values, {@link #equals(Object)}
 *       {@code ==}, and being a representative of the same real-world object.
 *       Yet, in practice it proved that this method was difficult to maintain in all
 *       subtypes, and was actually never used. It is removed in this version.
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public interface RousseauBean extends SemanticBean {

  /**
   * <p>Build a set of {@link CompoundPropertyException}s that tell what is
   *   wrong with this instance, with respect to <dfn>being civilized</dfn>.
   *   The result comes in the form of an <strong>unclosed</strong>
   *   {@link CompoundPropertyException}, of
   *   which the set of element exceptions might be empty.</p>
   * <p>This method should work in any state of the object,
   *   also if it is not yet {@link #normalize() normalized}.</p>
   * <p>This method is public instead of
   *   protected to make it more easy to describe to users what the business
   *   rules for this type are.</p>
   */
  @MethodContract(
    post = {
      @Expression("result != null"),
      @Expression("! result.closed"),
      @Expression("result.origin == this"),
      @Expression("result.propertyName == null"),
      @Expression("result.message == InternalException.DEFAULT_MESSAGE_KEY"),
      @Expression("result.cause == null")
    }
  )
  CompoundPropertyException wildExceptions();

  @MethodContract(post = @Expression("wildExceptions.empty"))
  boolean civilized();

  /**
   * This method does nothing, but will throw the wild exceptions
   * if this bean is not civilized.
   */
  @MethodContract(
    post = @Expression(value = "'civilized",
                       description = "if this bean is not civilized before the call, " +
                           "nothing can make this postcondition true, and thus an " +
                           "exception must be thrown"),
    exc = @Throw(type = CompoundPropertyException.class,
                 cond = {
                   @Expression("! 'civilized"),
                   @Expression("thrown.like(wildExceptions)"),
                   @Expression("throw.closed")
                 })
  )
  void checkCivility() throws CompoundPropertyException;

  /**
   * <p>Change the value of properties or the internal state of this
   *   object without changing its semantics, with the intent of
   *   making the instance more <dfn>civilized</dfn>. This method
   *   should never change the nominal state of this bean.
   * <p>This method is typically called immediately prior to
   *   checking civilization with {@link #wildExceptions()}.</p>
   * <p>This method can be called at any time, so it should never
   *   make unexpected changes to the state.</p>
   */
  @MethodContract(post = @Expression("true"))
  void normalize();

}
