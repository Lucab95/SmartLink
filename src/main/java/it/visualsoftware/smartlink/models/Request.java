package it.visualsoftware.smartlink.models;
import java.util.Date;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Request extends FactoryRequest{
	@Getter @Setter
	public class Auth{
		private String usr;
		private String pass;
	}
	@Id
	public ObjectId _id;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss") //dubbio
	private Date expiration;//TODO 2 ore avanti per utc
	private String body;
	private String uri;
	private String redirectURI;
	private String webMethod;
	private String apiK;
	private Auth auth;
	private String clickableUrl;

	
	
	public Request () { 
//		//UUID uUId = UUID.randomUUID();//getrandom da 8 UUID factory
//		this.uUId = RandomStringUtils.randomAlphanumeric(8);
//		this.creation = new Date();
//		this.clickDate = null;
	}
	
	/*public String getDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
							return(dateFormat.format(creation));
							}*/
}