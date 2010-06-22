package be.peopleware.facebookdemo.persistence.jpa;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.ppwcode.vernacular.exception_III.handle.ExceptionHandler;
import org.ppwcode.vernacular.persistence_III.dao.jpa.JpaOutOfContainerStatelessCrudDao;

public class FacebookDemoCrudDao
		extends
		org.ppwcode.vernacular.persistence_III.dao.jpa.JpaRemoteAtomicStatelessCrudDao {

	private static final String ANONYMOUS_ROLE_NAME = "anonymous";

	public FacebookDemoCrudDao() {
		setExceptionHandler(new ExceptionHandler());
		setStatelessCrudJoinTransactionDao(new JpaOutOfContainerStatelessCrudDao(){

			/**
			 * This allows every user to perform a CRUD operation (the anonymous role
			 * is defined for create and update on the Movie object.) 
			 * 
			 * TODO: a facebook connect session should result (preferrably transparently)
			 * in the creation of a JAAS compatible security context.  This would enable
			 * us to define the roles declaratively (in web.xml) instead of hard coding
			 * them into the application.
			 */
			@Override
			protected boolean isCallerInRole(String role) {
				return ANONYMOUS_ROLE_NAME.equals(role);
			}
			
		});
	}
	
	EntityManagerFactory $entityManagerFactory = null;
	
	@Override
	protected EntityManagerFactory getEntityManagerFactory() {
		if ($entityManagerFactory == null) {
			$entityManagerFactory = Persistence.createEntityManagerFactory("FaceBookDemo");
		}
		return $entityManagerFactory;
	}

}
