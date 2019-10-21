package it.visualsoftware.smartlink.models;
import java.util.Date;
import org.apache.commons.lang3.RandomStringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
abstract  class FactoryRequest {

	private String uUId;
	private Date creation;
	private Date clickDate;
	
	//TODO correggere ora
	public FactoryRequest () { 
		this.uUId = RandomStringUtils.randomAlphanumeric(8);
		this.creation = new Date();
		this.clickDate = null;
	}
	public String getuUId() { return uUId;}
	/*public String getDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
							return(dateFormat.format(creation));
							}*/
}
