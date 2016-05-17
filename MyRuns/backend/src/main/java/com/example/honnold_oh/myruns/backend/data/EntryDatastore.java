package com.example.honnold_oh.myruns.backend.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Transaction;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EntryDatastore {

	private static final Logger mLogger = Logger
			.getLogger(EntryDatastore.class.getName());
	private static final DatastoreService mDatastore = DatastoreServiceFactory
			.getDatastoreService();

	private static Key getKey() {
		return KeyFactory.createKey(Entry.CONTACT_PARENT_ENTITY_NAME,
				Entry.CONTACT_PARENT_KEY_NAME);
	}

	private static void createParentEntity() {
		Entity entity = new Entity(getKey());

		mDatastore.put(entity);
	}

	public static boolean add(Entry entry) {
		if (getContactByName(entry.mName, null) != null) {
			mLogger.log(Level.INFO, "entry exists");
			return false;
		}

		Key parentKey = getKey();


		Entity entity = new Entity(Entry.CONTACT_ENTITY_NAME, entry.mName,
				parentKey);
		entity.setProperty(Entry.FIELD_NAME_NAME, entry.mName);
		entity.setProperty(Entry.FIELD_NAME_ADDR, entry.mAddress);
		entity.setProperty(Entry.FIELD_NAME_PHONENUM, entry.mPhoneNumber);

		mDatastore.put(entity);

		return true;
	}

	public static boolean update(Entry entry) {
		Entity result = null;
		try {
			result = mDatastore.get(KeyFactory.createKey(getKey(),
					Entry.CONTACT_ENTITY_NAME, entry.mName));
			result.setProperty(Entry.FIELD_NAME_ADDR, entry.mAddress);
			result.setProperty(Entry.FIELD_NAME_PHONENUM,
					entry.mPhoneNumber);

			mDatastore.put(result);
		} catch (Exception ex) {

		}
		return false;
	}

	public static boolean delete(String name) {
		// you can also use name to get key, then use the key to delete the
		// entity from datastore directly
		// because name is also the entity's key

		// query
		Filter filter = new FilterPredicate(Entry.FIELD_NAME_NAME,
				FilterOperator.EQUAL, name);

		Query query = new Query(Entry.CONTACT_ENTITY_NAME);
		query.setFilter(filter);

		// Use PreparedQuery interface to retrieve results
		PreparedQuery pq = mDatastore.prepare(query);

		Entity result = pq.asSingleEntity();
		boolean ret = false;
		if (result != null) {
			// delete
			mDatastore.delete(result.getKey());
			ret = true;
		}

		return ret;
	}

	public static ArrayList<Entry> query(String name) {
		ArrayList<Entry> resultList = new ArrayList<Entry>();
		if (name != null && !name.equals("")) {
			Entry entry = getContactByName(name, null);
			if (entry != null) {
				resultList.add(entry);
			}
		} else {
			Query query = new Query(Entry.CONTACT_ENTITY_NAME);
			// get every record from datastore, no filter
			query.setFilter(null);
			// set query's ancestor to get strong consistency
			query.setAncestor(getKey());

			PreparedQuery pq = mDatastore.prepare(query);

			for (Entity entity : pq.asIterable()) {
				Entry entry = getContactFromEntity(entity);
				if (entry != null) {
					resultList.add(entry);
				}
			}
		}
		return resultList;
	}

	public static Entry getContactByName(String name, Transaction txn) {
		Entity result = null;
		try {
			result = mDatastore.get(KeyFactory.createKey(getKey(),
					Entry.CONTACT_ENTITY_NAME, name));
		} catch (Exception ex) {

		}

		return getContactFromEntity(result);
	}

	private static Entry getContactFromEntity(Entity entity) {
		if (entity == null) {
			return null;
		}

		return new Entry(
				(String) entity.getProperty(Entry.FIELD_NAME_NAME),
				(String) entity.getProperty(Entry.FIELD_NAME_ADDR),
				(String) entity.getProperty(Entry.FIELD_NAME_PHONENUM));
	}
}
