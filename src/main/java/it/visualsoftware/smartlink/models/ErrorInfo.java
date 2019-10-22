package it.visualsoftware.smartlink.models;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class ErrorInfo {
	private String errorType;
	
	public ErrorInfo(String eType) {
		this.errorType=eType;
	}
}

