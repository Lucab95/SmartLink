package it.visualsoftware.smartlink.models;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
/**
 * classe contente solo il link da cliccare una volta inserita la richiesta nel DB
 * @author luca9
 *
 */
public class DTOLink {
	private String url;

	public DTOLink(String url) {
		this.url = url;
	}	
	
}
