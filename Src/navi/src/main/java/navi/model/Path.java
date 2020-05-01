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
@Table(name = "path")
public class Path implements Serializable {

	private static final long serialVersionUID = 4558593505205835947L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Long id;
	
	@Column(name = "date_time", columnDefinition = "DATETIME")
	public LocalDateTime dateTime;
	
	@Column(name = "user_id")
	public Long userId;
	
	@Column(name = "journey_time_id")
	public Long journeyTimeId;
	
	@Column(name = "lon")
	public double lon;
	
	@Column(name = "lat")
	public double lat;

	@Column(name = "speed")
	public float speed;

	@Column(name = "altitude")
	public float altitude;
	
}
