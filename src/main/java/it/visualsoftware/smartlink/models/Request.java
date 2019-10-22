package it.visualsoftware.smartlink.models;
import java.util.Date;
import java.util.UUID;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 
 * @author luca9
 *
 */
public @Data class Request{
	public @Data class Auth{
		private String usr;
		private String pass;
	}

	public @Data class LmAuth{
		private String api;
		private String tenant;
	}
	@Id
	public String _id;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss") //dubbio
	private Date expiration;//TODO 2 ore avanti per utc
	private String body;
	private String uri;
	private String redirectURI;
	private String webMethod;
	private String clickableUrl;
	
	private String apiK;
	private Auth auth; 
	private LmAuth leadAuth;
	
	private String uUId;
	private Date creation;
	private Date clickDate;
	
	/**
	 * inizializza generando un UUID da 12 char, la data di creazione e imposta la data di click
	 */
	public Request () { 
		String fullUUId = (UUID.randomUUID().toString());
		this.uUId = fullUUId.substring(fullUUId.length()-12);
		this.clickDate = null;
		this.creation = new Date();
	}
	public String getuUId() { return uUId;}
}