package be.peopleware.facebookdemo.semantics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.ppwcode.vernacular.persistence_III.jpa.AbstractIntegerIdIntegerVersionedPersistentBean;

@Entity
@Table(name="movie")
public class Movie extends AbstractIntegerIdIntegerVersionedPersistentBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4545746267436384281L;

	
  /*<property name="facebook user">
  -------------------------------------------------------------------------*/
	public void setFacebookUserId(long id) {
		$facebookUserId = id;
	}
	
	public long getFacebookUserId() {
		return $facebookUserId;
	}
	
	private long $facebookUserId = 0;
	/*</property*/
	
  /*<property name="name">
  -------------------------------------------------------------------------*/
  public String getTitle() {
    return $title;
  }

  public void setTitle(String title) {
    $title = title;
  }

  @Column(name="title",nullable=false)
  private String $title = null;

  /*</property>*/

}
