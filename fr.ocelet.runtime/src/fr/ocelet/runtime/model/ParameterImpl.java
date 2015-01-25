package fr.ocelet.runtime.model;

public class ParameterImpl<T> implements Parameter<T> {

	protected String pname;
	protected String pdesc;
	protected String punit;
	protected boolean optionnal;
	protected T defval;

	public ParameterImpl() {
		this.pname = "?";
		this.pdesc = "";
		this.optionnal = true;
	}

	public ParameterImpl(String pname, String pdesc, boolean opt, T defval, String unit) {
		this.pname = pname;
		this.pdesc = pdesc;
		this.optionnal = opt;
		this.defval = defval;
		this.punit =unit;
	}

	@Override
	public String getName() {
		return pname;
	}

	@Override
	public String getDescription() {
		return pdesc;
	}

	@Override
	public boolean isOptionnal() {
		return optionnal;
	}

	@Override
	public T getDefaultValue() {
		return defval;
	}

	@Override
	public String getUnit() {
		return punit;
	}

}
