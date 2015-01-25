package fr.ocelet.runtime.model;

import java.util.Collection;

public interface EnumParameter<T> extends Parameter<T> {
  public Collection<T> getValues();
  public boolean isExclusive();

  // Do we need this for non exclusive params :
  // public Collection<T> getDefaultValues();
}
