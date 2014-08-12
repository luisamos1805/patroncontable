package com.rhem.seguridad.converter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.vaadin.data.util.converter.StringToDateConverter;

@SuppressWarnings("serial")
public class MyStringToDateConverter extends StringToDateConverter {
	@Override
	protected DateFormat getFormat(Locale locale) {
		return new SimpleDateFormat("dd/MM/yyyy");
	}
}
