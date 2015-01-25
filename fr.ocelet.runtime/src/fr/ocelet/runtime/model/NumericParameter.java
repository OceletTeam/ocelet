package fr.ocelet.runtime.model;

public interface NumericParameter<T extends Number> extends Parameter<T> {

	T getMinValue();

	T getMaxValue();

}
