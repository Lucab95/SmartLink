//package it.visualsoftware.smartlink;
//
//import org.json.JSONObject;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//
//import it.visualsoftware.smartlink.models.Request;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Component
//public class SmartLinkClient {
//	private RestTemplate rest;
//	public SmartLinkClient() {
//	}
//	/**
//	 *prende i dati del cliente, li mette in un oggetto e fa una POST al server
//	 */
//	public void postCustomer() {
//		
//		JSONObject customerBody = new JSONObject();
//		Request z = new Request();
//		customerBody.put("body", z.toString());
//		customerBody.put("uri","http://localhost:8088/api/checkValidity/fnKIJAE2");
//		customerBody.put("webMethod", "Get");
//		customerBody.put("apiK", "Bearer awdadzaw");
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_JSON);
//		HttpEntity<JSONObject> request = new HttpEntity<JSONObject>(customerBody, headers);
//		String url=rest.postForObject("http://localhost:8088/api/request2", request, String.class);
//		log.info("url generato "+url);
//		//return url;
//	}
//	
//}

