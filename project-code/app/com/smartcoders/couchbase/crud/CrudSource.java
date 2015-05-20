package com.smartcoders.couchbase.crud;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import net.spy.memcached.PersistTo;
import net.spy.memcached.ReplicateTo;

import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.protocol.views.Query;
import com.couchbase.client.protocol.views.View;
import com.couchbase.client.protocol.views.ViewResponse;
import com.couchbase.client.protocol.views.ViewRow;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.google.gson.Gson;
import com.smartcoders.couchbase.CouchbaseProvider;
import com.smartcoders.couchbase.util.provider.GsonProvider;

/**
 * @author siva
 *
 */
public class CrudSource<T> {

	public final Class<T> clazz;
	public String id;
	
	private static CouchbaseClient bucket = CouchbaseProvider.get();
	private static Gson gson = GsonProvider.get();
	
	public CrudSource(Class<T> clazz) {
		this.clazz = clazz;
	}
	
	public void save(String key, T value) {
		bucket.set(key, gson.toJson(value), PersistTo.ZERO, ReplicateTo.ZERO);
	}
	
	public void save(T value) {
		bucket.set(UUID.randomUUID().toString(), gson.toJson(value), PersistTo.ZERO, ReplicateTo.ZERO);
	} 
	
	public T get(String key, Class<T> clazz) {
		return gson.fromJson((String) bucket.get(key), clazz);
	}
	
	public Collection<T> findByKeys(Collection<String> keys, Class<T> clazz) {
		Collection<T> docs = new ArrayList<T>();
		for(String key : keys) {
			docs.add(gson.fromJson((String) bucket.get(key), clazz));
		}
		return docs;
	}
	
	public ViewResponse getViewResponse(String designName, String viewName, Query query, Class<T> clazz) {
		View v = bucket.getView(designName, viewName);
		ViewResponse response = bucket.query(v, query);
		return response;
	}
	
	public Collection<T> find(String designName, String viewName, Query query, Class<T> clazz) {
		ViewResponse response = getViewResponse(designName, viewName, query, clazz);
		Collection<T> docs = new ArrayList<T>();
		for(ViewRow row : response) {
			docs.add(gson.fromJson((String) bucket.get(row.getId()), clazz));
		}
		return docs;
	}
	
	public void update(String id, T updated, Class<T> clazz) {
		T existing = get(id, clazz);
		T merged = null;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			merged = objectMapper.readValue(gson.toJson(existing), clazz);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ObjectReader updater = objectMapper.readerForUpdating(existing);
		try {
			merged = updater.readValue(gson.toJson(updated));
		} catch (IOException e) {
			e.printStackTrace();
		}
        bucket.replace(id, gson.toJson(merged));
	}
	
	public Integer count(String designName, String viewName, Query query, Class<T> clazz) {
		ViewResponse response = getViewResponse(designName, viewName, query, clazz);
		return response.size();
	}
	
	public Boolean isExist(String designName, String viewName, Query query, Class<T> clazz) {
		View v = bucket.getView(designName, viewName);
		ViewResponse response = bucket.query(v, query);
		if(response.size() > 0) {
			return true;
		}	
		return false;
	}
	
	public void delete(String key) {
		bucket.delete(key, PersistTo.ZERO, ReplicateTo.ZERO);
	}
	
}
