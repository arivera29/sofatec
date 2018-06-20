package com.are.sofatec;

public class PerfilesMenu {
	private String idperfil;
	private String idmenu;

	public String getIdperfil() {
		return idperfil;
	}

	public void setIdperfil(String idperfil) {
		this.idperfil = idperfil;
	}

	public String getIdmenu() {
		return idmenu;
	}

	public void setIdmenu(String idmenu) {
		this.idmenu = idmenu;
	}

	public PerfilesMenu(String idperfil, String idmenu) {
		super();
		this.idperfil = idperfil;
		this.idmenu = idmenu;
	}

	public PerfilesMenu() {
		super();
		this.idmenu = "";
		this.idperfil = "";
	}

}
