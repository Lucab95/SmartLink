package it.visualsoftware.smartlink.models;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
/**
 * DTO di risposta quando si controlla la validità di una richiesta
 * @author luca9
 *
 */
public class DTOResponse {
	public String body;
	public String status;
	public String clickDate;
	public String creationDate;
	public String expirationDate;
	
	public DTOResponse(String body, String creation, String expiration, String click, String status) {
		this.body = body;
		this.creationDate = creation;
		this.expirationDate = expiration;
		this.clickDate=click;
		this.status = status;
	}
}
