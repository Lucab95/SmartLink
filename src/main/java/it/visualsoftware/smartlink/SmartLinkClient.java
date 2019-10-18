//package it.visualsoftware.smartlink;
//
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//
//import com.mongodb.BasicDBObject;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Component
//public class SmartLinkClient {
//	private RestTemplate rest;
//	public SmartLinkClient(RestTemplate rest) {
//		this.rest = rest;
//	}
//	/**
//	 *prende i dati del cliente, li mette in un oggetto e fa una POST al server
//	 */
//	public void postCustomer() {
//		
//		BasicDBObject customerBody = new BasicDBObject();
//		customerBody.put("body", "Luca");
//		customerBody.put("lastName", "sada");
//		customerBody.put("method", "Get");
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_JSON);
//		headers.set("apikey","a123b");
//		HttpEntity<BasicDBObject> request = new HttpEntity<BasicDBObject>(customerBody, headers);
//		String url=rest.postForObject("http://localhost:8088/api/request2", request, String.class);
//		log.info("url generato "+url);
//		//return url;
//	}
//	
//}
//
