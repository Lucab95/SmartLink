package it.visualsoftware.smartlink;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import exceptions.RequestNotFoundException;
import exceptions.RequestNotValidException;
import it.visualsoftware.smartlink.models.DTOLink;
import it.visualsoftware.smartlink.models.DTOResponse;
import it.visualsoftware.smartlink.models.ErrorInfo;
import it.visualsoftware.smartlink.models.Request;
import it.visualsoftware.smartlink.repositories.RequestRepository;
import lombok.extern.slf4j.Slf4j;
/**
 * 
 * @author luca9
 *Controller SmartLink
 *Prende in input un insieme di parametri, salva tutto sul DB (mongoDB) e ritorna un link unico cliccabile
 *una volta cliccato il link se è ancora valido, SmartLink ne esegue la richiesta contenuta e lo consuma.
 *è possibile anche verificare se una richiesta è ancora raggiungibile
 *in tal caso bisogna effettuare una chiamata get all'indirizzo xxxxx/api/checkValidity/{uUId}
 *
 */
@Slf4j
@RestController
@RequestMapping("/api")
class SmartLinkController {
	private final RequestRepository repository;
	private RestTemplateService proxy;
	String url;
	
	
	public SmartLinkController(RequestRepository repository, RestTemplateService proxy, @Value("${click.domain}") String urlBase) {
		this.proxy = proxy;
		this.repository = repository;
		this.url= urlBase;
	}
	
	/**
	 * Gestisce gli errori relativi alla ricerca, nel caso in cui non viene trovata e nel caso in cui sia scaduta
	 * @param req
	 * @param ex
	 * @return ErrorInfo
	 */
	@ExceptionHandler (value = {RequestNotValidException.class, RequestNotFoundException.class})
	@ResponseBody 
	public ErrorInfo handleErr(HttpServletRequest req ,Exception ex){
		String code = (UUID.randomUUID().toString());
		code = code.substring(code.length()-8);
		ErrorInfo e = new ErrorInfo(ex.toString());
		log.error("errorCode:{}  ExceptionType: {} url: {}",code,ex,req.getRequestURL());
		return e;
	}
	@ExceptionHandler (value = {RuntimeException.class,Exception.class })
	@ResponseBody 
	public void handleUnknownError(HttpServletRequest req ,Exception ex){
		String code = (UUID.randomUUID().toString());
		code = code.substring(code.length()-8);
		log.error("eccezione generica",ex);	
	}
	
	
	/**
	 * prende in input una data controlla se non è null ritorna la stringa leggibile
	 * @param time 
	 * @return (String) time
	 */
	public String convertTime(Date time) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
		if (time!=null) {
			return dateFormat.format(time);
		}else {
			return null;
		}
	}
	
	/**
	 * se non viene inserita una data di scadenza, la scadenza viene settata a 2 giorni
	 * @param start
	 * @param day
	 * @return
	 */
	private Date setExpirationDefault(Date start,int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(start);
		cal.add(Calendar.DATE, day);
		return cal.getTime();
	}
	/**
	 * riceve la richiesta in input, controlla e ritorna il suo stato cliccato||attivo||scaduto
	 * @param userReq
	 * @return status
	 */
	public String getStatus(Request userReq) {
		Long end = userReq.getExpiration().getTime();
		Long curr= new Date().getTime();
		String status = "clicked";
		if (userReq.getClickDate()==null) {
			if (curr.compareTo(end)<0) {
				status="valid";
			}else {
				status="expired";
			}
		}
		return status;
	}
	
	
/**
 * cerca tramite UUId la richiesta, se ancora valida (getStatus()) ne esegue il contenuto
 * una volta eseguita la richiesta SE è presente un uri per il reindirizzamento lo effettua.
 * @param uUId
 * @param response
 * @throws IOException
 * @throws RequestNotFoundException 
 * @throws RequestExpiredException 
 */
	@GetMapping("{uUId}")
	public void getRequestByUUId(@PathVariable String uUId, HttpServletResponse response) throws IOException, RequestNotFoundException, RequestNotValidException {
		log.info("Request UUID: "+uUId);
		Request userReq = repository.findByUUId(uUId);
		if (userReq==null) {
			throw new RequestNotFoundException(uUId);
		}
		log.info("Request found: "+userReq);
		if (getStatus(userReq)!="valid") {
			throw new RequestNotValidException(uUId,getStatus(userReq));
		}
		//else
		String resp = proxy.sendRequest(userReq,repository);	
		if (resp!=null) {
			log.info("redirect to "+ resp);
			response.sendRedirect(resp);
			response.flushBuffer();
		}
	}

/** preso l'uuid cerca la richiesta e ritorna le informazioni su di essa
 * data di creazione, scadenza e momento del click nel caso la richiesta fosse stata consumata
 * @param uUId
 * @return DTOResponse
 * @throws RequestNotFoundException 
 */
	@GetMapping("checkValidity/{uUId}")
	public DTOResponse checkValidity(@PathVariable String uUId) throws RequestNotFoundException {
		log.info("UUID da verificare:"+uUId);
		Request userReq = repository.findByUUId(uUId);
		if (userReq==null) {
			throw new RequestNotFoundException(uUId);//error handler per evitare runtimeerror e genrico
		}
		//response body -> codice errore scritto log exception.. con random string log.error(param ecc per identificarla)
		String creation = convertTime(userReq.getCreation());
		String expiration = convertTime(userReq.getExpiration());
		String click = convertTime(userReq.getClickDate());	
		return (new DTOResponse(userReq.getBody() , creation, expiration , click , getStatus(userReq)));
	}
	
	/**
	 * funzione di test, stampa il body e gli headers della richiesta
	 * @param body
	 * @param headers
	 */
	@PostMapping("/test")
	public void testPost(@RequestBody String body, @RequestHeader Map<String, String> headers ){
		log.info("/test stampa body e headers: {}{}", body , headers);
	}
	
	
/**
 *  intercetta la post da cui riceve la richiesta come body,
 *  la salva su mongo, viene generato uUId e ritorna il DTO contenente il link da cliccare 
 * @param userReq
 * @return DTOLink
 */
	@PostMapping("/createLink")
	public Object createLinkDTO(@RequestBody Request userReq){
		//redirecturi se valorizzato mi rimanda ad un link specificato altrimenti nessuna response
		log.info("oggetto ricevuto dalla POST "+ userReq.toString());
		String finalUrl = url +userReq.getuUId();
		if (userReq.getExpiration()==null) {
			userReq.setExpiration(setExpirationDefault(userReq.getCreation(),2));
		}
		userReq.setClickableUrl(finalUrl);
		repository.save(userReq);
		return (new DTOLink(finalUrl));
	}


	
}