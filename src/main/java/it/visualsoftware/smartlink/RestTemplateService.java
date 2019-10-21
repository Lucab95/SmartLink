package it.visualsoftware.smartlink;

import java.nio.charset.Charset;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import it.visualsoftware.smartlink.models.Request;
import it.visualsoftware.smartlink.repositories.RequestRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RestTemplateService {
 	
	
	private final RestTemplate template;
	
 	public RestTemplateService(RestTemplate template) {
 		this.template=template;
 	}
 	

 	/**
 	 * prende l'utente, gestisce la richiesta ed effettua la chiamata POST/GET
 	 * @param userReq
 	 * @param repository 
 	 */
 	public String sendRequest(Request userReq, RequestRepository repository) {
 		String user = null;
 		HttpHeaders headers = new HttpHeaders();
 		if (userReq.getApiK()!=null){
 			headers.set("Authorization","Bearer "+userReq.getApiK());
 		}else{
 			String auth = userReq.getAuth().getUsr() + ":" + userReq.getAuth().getPass();
	        byte[] encodedAuth = Base64.encodeBase64( auth.getBytes(Charset.forName("ASCII")) );
	        String authHeader = "Basic " + new String( encodedAuth );
	        headers.set( "Authorization", authHeader );
 		} 
 		String httpMethod =userReq.getWebMethod().toUpperCase();
 		headers.setContentType(MediaType.APPLICATION_JSON);
 		if (httpMethod.equals("POST")) {user = userReq.getBody().toString();}
 		String uri = userReq.getUri();
		HttpEntity<String> req = new HttpEntity<String>( user , headers);
		log.info("template exchange");
		ResponseEntity<String> response = template.exchange( uri , HttpMethod.valueOf(httpMethod) , req , String.class);
		//save mongo
		if(response.getStatusCode().value()==200) {
			log.info("success");
			userReq.setClickDate(new Date());
			repository.save(userReq);
		}
		//save e click
		return (response.getStatusCode()+" "+response.getBody());
 	}
 	
 	
 	
}
