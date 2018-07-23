/*
*  Ocelet spatial modelling language.   www.ocelet.org
*  Copyright Cirad 2010-2018
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
package fr.ocelet.lang.ui;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.xtext.generator.IOutputConfigurationProvider;
import org.eclipse.xtext.resource.clustering.DynamicResourceClusteringPolicy;
import org.eclipse.xtext.resource.clustering.IResourceClusteringPolicy;
import org.eclipse.xtext.ui.resource.IResourceSetProvider;
import org.eclipse.xtext.ui.resource.SimpleResourceSetProvider;
import org.eclipse.xtext.ui.resource.XtextResourceSetProvider;

import com.google.inject.Binder;
import com.google.inject.Singleton;

import fr.ocelet.lang.OcltOutputConfigurationProvider;

/**
 * Use this class to register components to be used within the IDE.
 * 
 * @author Pascal Degenne - Initial contribution
 */
public class OceletUiModule extends fr.ocelet.lang.ui.AbstractOceletUiModule {
	
	public OceletUiModule(AbstractUIPlugin plugin) {
		super(plugin);
	}

	@Override
	public void configure(Binder binder) {
		super.configure(binder);
		binder.bind(IOutputConfigurationProvider.class).to(OcltOutputConfigurationProvider.class).in(Singleton.class);
	}
	
	
	public Class<? extends IResourceClusteringPolicy> bindIResourceClusteringPolicy() {
		return DynamicResourceClusteringPolicy.class;
	}
	/* @Override
	    public Class<? extends IResourceSetProvider> bindIResourceSetProvider() {
	        return  SimpleResourceSetProvider.class;
	    }*/
	    
	   /* @org.eclipse.xtext.service.SingletonBinding(eager=false)
	    public Class<? extends PlatformURIMapCache> bindPlatformURIMapCache() {
	        return PlatformURIMapCache.class;
	    }*/

}
