package navi.model;

import java.io.Serializable;

// path show/hide check data

public class CheckData implements Serializable {
	private static final long serialVersionUID = 6432212969734253902L;
	
	public Long journeyId; // journey id
	public Long journeyTimeId; // journey time id
	public Integer checked;

}
