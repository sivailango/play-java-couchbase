/**
 * 
 */
package com.smartcoders.couchbase.plugin;

import com.smartcoders.couchbase.CouchbaseProvider;

import play.Plugin;

/**
 * @author siva
 *
 */
public class CouchbasePlugin extends Plugin {
	
	@Override
	public void onStart() {
		CouchbaseProvider.connect();
	}
	
	@Override
	public void onStop() {
		CouchbaseProvider.close();
	}
	
	@Override
	public boolean enabled() {
		return true;
	}
	
}
