package be.peopleware.facebookdemo.jpa;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.ppwcode.vernacular.exception_III.handle.ExceptionHandler;
import org.ppwcode.vernacular.persistence_III.dao.jpa.JpaOutOfContainerStatelessCrudDao;

public class JpaRemoteAtomicStatelessCrudDao
		extends
		org.ppwcode.vernacular.persistence_III.dao.jpa.JpaRemoteAtomicStatelessCrudDao {

	public JpaRemoteAtomicStatelessCrudDao() {
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
