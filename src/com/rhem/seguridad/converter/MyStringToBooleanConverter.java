package com.rhem.seguridad.converter;

import com.vaadin.data.util.converter.StringToBooleanConverter;


@SuppressWarnings("serial")
public class MyStringToBooleanConverter extends StringToBooleanConverter{
	private String falseString;
	private String trueString;
	public MyStringToBooleanConverter(String falseString, String trueString){
		this.falseString = falseString;
		this.trueString = trueString;
	}
	@Override
	protected String getFalseString() {
		return falseString;
	}
	@Override
	protected String getTrueString() {
		return trueString;
	}
}
