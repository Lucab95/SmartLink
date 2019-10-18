package it.visualsoftware.smartlink.models;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DTOLink {
	private String url;

	public DTOLink(String url) {
		this.url = url;
	}	
	
}
