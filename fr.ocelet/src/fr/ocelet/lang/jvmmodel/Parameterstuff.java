package fr.ocelet.lang.jvmmodel;

import org.eclipse.xtext.common.types.JvmTypeReference;

class Parameterstuff {

	String name;
	String description;
	JvmTypeReference type;
	Object dvalue;
	Object minvalue;
	Object maxvalue;
	String unit;
	Boolean optionnal;

	public Parameterstuff(String name, JvmTypeReference type) {
		this.name = name;
		this.type = type;
		this.description = "";
		this.optionnal = true;
	}

	public void setDefvalue(Object dv) {
		this.dvalue = dv;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public void setMin(Object minv) {
		this.minvalue = minv;
	}

	public void setMax(Object maxv) {
		this.maxvalue = maxv;
	}

	public String getName() {
		return name;
	}

	public JvmTypeReference getType() {
		return type;
	}

	public Object getDvalue() {
		return dvalue;
	}

	public Object getMinvalue() {
		return minvalue;
	}

	public Object getMaxvalue() {
		return maxvalue;
	}

	public String getUnit() {
		return unit;
	}
	
	public boolean isNumericType() {
		return ((type.getSimpleName().compareTo("Integer") == 0) ||
				(type.getSimpleName().compareTo("Long") == 0) ||
				(type.getSimpleName().compareTo("Float") == 0) ||
				(type.getSimpleName().compareTo("Double") == 0) ||
				(type.getSimpleName().compareTo("Short") == 0) ||
				(type.getSimpleName().compareTo("Byte") == 0));
	}
	
	public boolean isStringType() {
		return (type.getSimpleName().compareTo("String") == 0);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getOptionnal() {
		return optionnal;
	}

	public void setOptionnal(Boolean optionnal) {
		this.optionnal = optionnal;
	}
}