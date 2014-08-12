package com.rhem.seguridad.util;

@SuppressWarnings("serial")
public class MyFilterDecorador extends MyDefaultFilterDecorador {

	@Override
	public boolean usePopupForNumericProperty(Object propertyId) {
		return true;
	}
	
	@Override
    public String getBooleanFilterDisplayName(Object propertyId, boolean value) {
        if ("enabled".equals(propertyId)) {
            return value ? "ACTIVO" : "INACTIVO";
        }
        
        return null;
    }
}

