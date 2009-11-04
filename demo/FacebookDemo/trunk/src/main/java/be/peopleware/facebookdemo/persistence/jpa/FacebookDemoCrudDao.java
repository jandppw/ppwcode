package be.peopleware.facebookdemo.persistence.jpa;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.ppwcode.vernacular.exception_III.handle.ExceptionHandler;
import org.ppwcode.vernacular.persistence_III.dao.jpa.JpaOutOfContainerStatelessCrudDao;

public class FacebookDemoCrudDao
		extends
		org.ppwcode.vernacular.persistence_III.dao.jpa.JpaRemoteAtomicStatelessCrudDao {

	public FacebookDemoCrudDao() {
		setExceptionHandler(new ExceptionHandler());
		setStatelessCrudJoinTransactionDao(new JpaOutOfContainerStatelessCrudDao());
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
