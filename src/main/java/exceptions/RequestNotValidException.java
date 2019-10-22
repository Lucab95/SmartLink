package exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.GONE, reason="Request Not Valid") //410
public class RequestNotValidException extends RuntimeException {
	

	/**
	 * auto generated
	 */
	private static final long serialVersionUID = -3274215524009996656L;

	public RequestNotValidException(String id,String status){
		super("UUID '"+ id +"' "+ status);
	}
}