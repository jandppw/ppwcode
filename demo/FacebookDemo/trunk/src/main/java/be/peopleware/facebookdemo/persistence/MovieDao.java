/*<license>
  Copyright 2008, PeopleWare
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.facebookdemo.persistence;


import java.util.List;

import org.ppwcode.vernacular.persistence_III.dao.Dao;

import be.peopleware.facebookdemo.semantics.Movie;


/**
 * This {@link Dao} offers access to {@link Enterprise} related functionality, apart from the standard CRUD functionality
 * provided by {@link org.ppwcode.vernacular.persistence_III.dao.StatelessCrudDao}.
 *
 * @mudo For now, the general functionality is still in here. Will be refactored shortly.
 * @mudo contracts
 */

public interface MovieDao extends Dao {

  /**
   * Return a list of enterprises in our persistent storage &quot;the Google way&quot;. The parameter String
   * is compared to the {@link Enterprise#getIdentifiers() identifiers}, the {@link Enterprise#getNames() enterprise name}
   * and the {@link Enterprise#getAddress() enterprise address}. // MUDO only postal code and city
   * If {@code filterInactive} is {@code true}, only objects for which the {@link Enterprise#getTerminationDate()} is
   * {@code null} are returned.
   */
  public List<Movie> findByFacebookUser(long fbUserId);


}
