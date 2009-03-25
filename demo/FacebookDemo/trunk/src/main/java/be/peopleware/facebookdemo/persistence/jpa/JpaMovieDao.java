package be.peopleware.facebookdemo.persistence.jpa;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.ppwcode.vernacular.persistence_III.dao.jpa.AbstractJpaDao;

import be.peopleware.facebookdemo.persistence.MovieDao;
import be.peopleware.facebookdemo.semantics.Movie;

public class JpaMovieDao extends AbstractJpaDao implements MovieDao {

	EntityManagerFactory $emf = null;
	@Override
	public EntityManager getEntityManager() {
		if ($emf == null) {
			$emf = Persistence.createEntityManagerFactory("FaceBookDemo");
		}
		return $emf.createEntityManager();
	}

	public List<Movie> findByFacebookUser(long fbUserId) {
		EntityManager em = getEntityManager();
		EntityTransaction et = em.getTransaction();
		et.begin();
		@SuppressWarnings("unchecked")
		List<Movie> rs = em.createNamedQuery("findMoviesByFacebookUser")
                            .setParameter("fbuid", fbUserId)
                            .getResultList();
		et.commit();
		em.close();
		return rs;
	}
	
	@Override
	protected void finalize() throws Throwable {
		try {
			if ($emf != null) {
				$emf.close();
			}
		} finally {
			super.finalize();
		}
	}
}
