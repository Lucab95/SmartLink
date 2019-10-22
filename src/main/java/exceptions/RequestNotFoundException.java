package exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Request Not Found") //404
public class RequestNotFoundException extends RuntimeException {
	
	/**
	 * auto generated
	 */
	private static final long serialVersionUID = 8301466938136605254L;

	public RequestNotFoundException(String id){
		super( "UUID '"+id +"' not found");
	}
}
