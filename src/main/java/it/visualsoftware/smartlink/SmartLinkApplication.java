package it.visualsoftware.smartlink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SmartLinkApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(SmartLinkApplication.class, args);
		
		/*SmartLinkClient sLClient = new SmartLinkClient();
		sLClient.postCustomer();*/
	}
	

}
