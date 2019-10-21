package it.visualsoftware.smartlink;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.visualsoftware.smartlink.models.DTOLink;
import it.visualsoftware.smartlink.models.DTOResponse;
import it.visualsoftware.smartlink.models.Request;
import it.visualsoftware.smartlink.repositories.RequestRepository;
import lombok.extern.slf4j.Slf4j;
/**
 * 
 * @author luca9
 *
 */
@Slf4j
@RestController
@RequestMapping("/api")
class SmartLinkController {
	private final RequestRepository repository;
	private RestTemplateService proxy;
	@Value("${click.domain}")
	private String url;
	
	
	public SmartLinkController(RequestRepository repository, RestTemplateService proxy) {
		this.proxy = proxy;
		this.repository = repository;
	}
	
	/**
	 * prende in input una data controlla se è null e ritorna la Stringa 
	 * @param time
	 * @return String time
	 */
	public String convertTime(Date time) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
		if (time!=null) {
			return dateFormat.format(time);
		}else {
			return null;
		}
	}
	public String getStatus(Request userReq) {
		Long end = userReq.getExpiration().getTime();
		Long curr= new Date().getTime();
		String status = "cliccato";
		if (userReq.getClickDate()==null) {
			if (curr.compareTo(end)<0) {
				status="attivo";
			}else {
				status="scaduto";
			}
		}
		return status;
	}
	
	/**
	 * cerca tramite uUId, se ancora valido esegue la richiesta contenuta in esso
	 * @param uUId
	 * @return userReq
	 */
	@GetMapping("{uUId}")
	public String getRequestByUUId(@PathVariable String uUId) {
		Request userReq = repository.findByUUId(uUId);
		if (userReq==null) {
			throw new RuntimeException("Impossibile trovare la richiesta");
		}
		log.info("Request trovata: "+userReq);
		if (getStatus(userReq)!="attivo") {
			throw new RuntimeException("il link è scaduto");
		}
		//else
		String print = proxy.sendRequest(userReq,repository);//silent return
		log.info("il link era valido, ritornato al controller e clicked aggiornato "+userReq);
		return (print);
	}
	
	/**
	 * preso l'uuid controlla se il link  è stato cliccato o no, ritorna un oggetto con le informazioni sul link
	 * @param body 
	 * @return DTOResponse
	 */
	@GetMapping("checkValidity/{uUId}")
	public DTOResponse checkValidity(@PathVariable String uUId) {
		Request userReq = repository.findByUUId(uUId);
		if (userReq==null) {
			throw new RuntimeException("req not found");
		}
		String creation = convertTime(userReq.getCreation());
		String expiration = convertTime(userReq.getExpiration());
		String click = convertTime(userReq.getClickDate());	
		DTOResponse dTO = new DTOResponse(userReq.getBody() , creation, expiration , click , getStatus(userReq));
		//log.info("creation:" +userReq.getCreation().getTime()+" expiration: "+userReq.getExpiration().getTime());
		return dTO;
	}
	
	/**
	 * provvisoria prendebody e stampa il contenuto
	 * @param body
	 * @param headers
	 */
	@PostMapping("/test")
	public void testPost(@RequestBody String body, @RequestHeader Map<String, String> headers ){
		log.info("/test stampa body e headers" + body + headers);
	}
	
	/**
	 * intercetta la post da cui prende la richiesta e la salva su mongo, viene generato uUId e ritorna l'url univoco da cliccare
	 * @param userReq
	 * @param headers
	 * @return String url
	 */
	
	@PostMapping("/createLink")
	public Object createLinkDTO(@RequestBody Request userReq){
		//TODO redirect uri
		//redirecturi se valorizzato mi rimanda ad un link specificato altrimenti nessuna response
		log.info("oggetto ricevuto dalla POST "+ userReq.toString());
		String finalUrl = url +userReq.getuUId(); 
		userReq.setClickableUrl(finalUrl);
		repository.save(userReq);
		return (new DTOLink(finalUrl));
	}
	
}