/*
*  Ocelet spatial modelling language.   www.ocelet.org
*  Copyright Cirad 2010-2016
*
*  This software is a domain specific programming language dedicated to writing
*  spatially explicit models and performing spatial dynamics simulations.
*
*  This software is governed by the CeCILL license under French law and
*  abiding by the rules of distribution of free software.  You can  use,
*  modify and/ or redistribute the software under the terms of the CeCILL
*  license as circulated by CEA, CNRS and INRIA at the following URL
*  "http://www.cecill.info".
*  As a counterpart to the access to the source code and  rights to copy,
*  modify and redistribute granted by the license, users are provided only
*  with a limited warranty  and the software's author,  the holder of the
*  economic rights,  and the successive licensors  have only limited
*  liability.
*  The fact that you are presently reading this means that you have had
*  knowledge of the CeCILL license and that you accept its terms.
*/

package fr.ocelet.runtime.relation;

import java.util.Collection;


/**
 * A RoleSet is a kind of intermediate filter between an interaction graph and a set of entities.
 * A graph refers to sets of entities (that play a specific role) but to make sure that a given
 * entity can be referred to by more than one graph, we separate the set that concretely hold
 * the entities and the way we access those entities in a graph.
 * The RoleSet points to a concrete entity set and maintains a subset view that is being used by
 * the graph.
 * 
 * A RoleSet must be synchronized with the content of an entity store. It should be notified if
 * the content of the entity store changes.
 * 
 * Implementations of the RoleSet interface should provide at least two constructors :
 *  one empty constructor and one that takes an Iterable<R> for argument.
 * 
 * If the empty constructor is used, the setEntityStore(Iterable<R>) can be called
 *  later because we need a reference to a set of entities.
 * 
 * @author Pascal Degenne, Initial contribution
 *
 */
public interface RoleSet<R extends OcltRole> extends Iterable<R>{

  public void setEntityStore(Collection<R> eset);
  
  /**
   * Attempts to add a new entity to the set used by a graph.
   * If the given role is already present in the entity store, then this
   * RoleSet will include that role in the positive side of the filter;
   * if the given role is not already present in the entity store, the
   * RoleSet will try to add that role to the store (some stores may not
   * accept such modification). 
   * 
   * @param r The role to be added
   */
  public void addRole(R r);
  
  /**
   * Removes a role from the set viewed by a graph. It means that the given role
   * will be placed on the negative side of the filter, but it will not be
   * concretely removed from the entity store.
   * 
   * @param r The role to be removed
   */
  public void removeRole(R r);

  /**
   * Same as addRole but working with a whole set of Roles at a time
   * @param roles A set of roles to be added
   */
  public void addRoles(Iterable<R> roles);
  
  /**
   * Same as removeRole but working with a set of Roles at a time
   * @param roles A set of Roles to be removed
   */
  public void removeRoles(Iterable<R> roles);

  public boolean hasRemovedRoles();
  public Collection<R> getRecentlyRemovedRoles();
  public void resetRemovedRoles();
  public boolean contains(R role);
}
