package fr.ocelet.runtime.model;

public class NumericParameterImpl<T extends Number> extends ParameterImpl<T>
		implements NumericParameter<T> {

	protected T minv;
	protected T maxv;

	public NumericParameterImpl() {
		super();
	}

	public NumericParameterImpl(String pname, String pdesc, boolean opt,
			T defval, T minvalue, T maxvalue, String unit) {
		super(pname, pdesc, opt, defval, unit);
		this.minv = minvalue;
		this.maxv = maxvalue;
	}

	@Override
	public T getMinValue() {
		return minv;
	}

	@Override
	public T getMaxValue() {
		return maxv;
	}

}
