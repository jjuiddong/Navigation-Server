package navi.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class JourneyInfo implements Serializable {
	
	private static final long serialVersionUID = -5579579024485267696L;

	public Long id;
	public Long timeId;	
	public LocalDate date;
	public Long userId;
	public float distance;
	public float journeyTime;
	public LocalDateTime startTime;
	public LocalDateTime endTime;
	public int updateCount;
}
