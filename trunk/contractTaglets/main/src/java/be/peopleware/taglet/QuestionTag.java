package be.peopleware.taglet;

import java.util.Map;

/**
 * Taglet for custom tag <code>@question</code>
 * @author Peopleware n.v.
 */
public class QuestionTag extends TagletRegistrar {

	private final String name = "question"; //$NON-NLS-1$
  private final String header = "Question:"; //$NON-NLS-1$

  /**
   * Register this taglet
   * 
   * @param tagletMap
   * 										the map to register this tag to.
   */
  public static void register(Map tagletMap) {
    QuestionTag tag = new QuestionTag();
    TagletRegistrar.registerTaglet(tagletMap, tag);
  }

	protected void setTagScopes() {
	   bInField 			= true;
	   bInConstructor = true;
	   bInMethod 			= true;
	   bInOverview 		= true;
	   bInPackage 		= true;
	   bInType 				= true;
	   bInLine 				= false;
	}

}
