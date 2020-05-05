package navi.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "landmark")
public class LandMark implements Serializable {
	
	private static final long serialVersionUID = -7588446630688132456L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Long id;
	
	@Column(name = "date_time", columnDefinition = "DATETIME")
	public LocalDateTime dateTime;
	
	@Column(name = "user_id")
	public Long userId;
	
	@Column(name = "lon")
	public double lon;
	
	@Column(name = "lat")
	public double lat;

	@Column(name = "address")
	public String address;

	@Column(name = "comment")
	public String comment;
	
}
