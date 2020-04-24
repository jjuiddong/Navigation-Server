package navi.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "journey_date")
public class JourneyDate implements Serializable {

	private static final long serialVersionUID = -8234385345840160197L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Long id;
	
	@Column(name = "time_id")
	public Long timeId;	

	@Column(name = "date", columnDefinition = "DATE")
	public LocalDate date;
	
	@Column(name = "user_id")
	public Long userId;

	@Column(name = "distance")
	public float distance;
	
	@Column(name = "journey_time")
	public float journeyTime;
	
}
