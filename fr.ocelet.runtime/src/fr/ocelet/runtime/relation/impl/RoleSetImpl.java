package fr.ocelet.runtime.relation.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import fr.ocelet.runtime.relation.OcltRole;
import fr.ocelet.runtime.relation.RoleSet;
import fr.ocelet.runtime.util.Filter;

public class RoleSetImpl<R extends OcltRole> implements RoleSet<R> {

	protected Collection<R> store;
	protected HashSet<Object> excludedRoleIds;
	protected ArrayList<R> recentlyRemovedRoles;

	public RoleSetImpl() {
		excludedRoleIds = new HashSet<Object>();
		recentlyRemovedRoles = new ArrayList<R>();
	}

	public RoleSetImpl(Collection<R> roleStore) {
		excludedRoleIds = new HashSet<Object>();
		recentlyRemovedRoles = new ArrayList<R>();
		this.store = roleStore;
	}

	public boolean contains(R role) {
		if (store == null)
			return false;
		else
			return (store.contains(role) && (!excludedRoleIds.contains(role
					.getGraph_Element_Identifier()) && (!recentlyRemovedRoles
					.contains(role))));
	}

	@Override
	public Iterator<R> iterator() {
		return excludedFilter.filter(store).iterator();
	}

	@Override
	public void setEntityStore(Collection<R> eset) {
		this.store = eset;
	}

	@Override
	public void addRole(R r) {
		if (excludedRoleIds.contains(r.getGraph_Element_Identifier()))
			excludedRoleIds.remove(r.getGraph_Element_Identifier());
		else
			store.add(r);
	}

	@Override
	public void removeRole(R r) {
		recentlyRemovedRoles.add(r);
		excludedRoleIds.add(r.getGraph_Element_Identifier());
	}

	@Override
	public void addRoles(Iterable<R> roles) {
		for (R r : roles)
			addRole(r);
	}

	@Override
	public void removeRoles(Iterable<R> roles) {
		for (R r : roles)
			removeRole(r);

	}

	protected final Filter<R> excludedFilter = new Filter<R>() {
		public boolean passes(R role) {
			return !(excludedRoleIds.contains(role
					.getGraph_Element_Identifier()));
		}
	};

	public Collection<R> getRecentlyRemovedRoles() {
		return recentlyRemovedRoles;
	}

	public void resetRemovedRoles() {
		recentlyRemovedRoles.clear();
	}
	
	public boolean hasRemovedRoles() {
		return !recentlyRemovedRoles.isEmpty();
	}

}
