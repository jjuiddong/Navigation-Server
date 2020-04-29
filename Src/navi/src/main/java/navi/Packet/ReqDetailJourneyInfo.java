//
// request show journey detail information
//

package navi.Packet;

import java.io.Serializable;


public class ReqDetailJourneyInfo implements Serializable {
	private static final long serialVersionUID = 1990645211393068239L;
	public Long journeyId;
	public Long journeyTimeId;
}
