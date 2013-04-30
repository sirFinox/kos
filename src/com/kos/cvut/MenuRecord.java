package com.kos.cvut;

public class MenuRecord {
	private String icon;
    private String menu;
       
    public MenuRecord(String icon, String menu) {
        this.setIcon(icon);
        this.setMenu(menu);
    }

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}
}
