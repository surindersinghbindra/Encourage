//package database_helper;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import OwnWrittenQuotes;
//
//public class DatabaseHandler extends SQLiteOpenHelper {
//
//	// All Static variables
//	// Database Version
//	private static final int DATABASE_VERSION = 1;
//
//	// Database Name
//	private static final String DATABASE_NAME = "Qoutes1";
//
//	// Contacts table name
//	private static final String OWN_WRITTEN_QUOTES = "own_wtitten_qoutes";
//
//	// Contacts Table Columns names
//	private static final String KEY_ID = "id";
//	private static final String SUB_CATEGORY = "subcategory";
//	private static final String QOUTE = "qoute";
//
//	public DatabaseHandler(Context context) {
//		super(context, DATABASE_NAME, null, DATABASE_VERSION);
//	}
//
//	@Override
//	public void onCreate(SQLiteDatabase db) {
//		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + OWN_WRITTEN_QUOTES + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
//				+ SUB_CATEGORY + " TEXT," + QOUTE + " TEXT" + ")";
//		db.execSQL(CREATE_CONTACTS_TABLE);
//
//	}
//
//	@Override
//	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		// Drop older table if existed
//		db.execSQL("DROP TABLE IF EXISTS " + OWN_WRITTEN_QUOTES);
//
//		// Create tables again
//		onCreate(db);
//
//	}
//
//	/**
//	 * All CRUD(Create, Read, Update, Delete) Operations
//	 */
//	// Adding new contact
//	public void addContact(OwnWrittenQuotes contact) {
//		SQLiteDatabase db = this.getWritableDatabase();
//
//		ContentValues values = new ContentValues();
//		values.put(SUB_CATEGORY, contact.getSubCategory());
//		values.put(QOUTE, contact.getQoute());
//
//		// Inserting Row
//		db.insert(OWN_WRITTEN_QUOTES, null, values);
//		db.close(); // Closing database connection
//	}
//
//	// Getting single contact
//	public OwnWrittenQuotes getContact(int id) {
//		SQLiteDatabase db = this.getReadableDatabase();
//
//		Cursor cursor = db.query(OWN_WRITTEN_QUOTES, new String[] { KEY_ID, SUB_CATEGORY, QOUTE }, KEY_ID + "=?",
//				new String[] { String.valueOf(id) }, null, null, null, null);
//		if (cursor != null)
//			cursor.moveToFirst();
//
//		OwnWrittenQuotes contact = new OwnWrittenQuotes(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
//				cursor.getString(2));
//		// return contact
//		return contact;
//	}
//
//	// Getting All Contacts
//	public List<OwnWrittenQuotes> getAllContacts() {
//
//		List<OwnWrittenQuotes> contactList = new ArrayList<OwnWrittenQuotes>();
//		// Select All Query
//		String selectQuery = "SELECT  * FROM " + OWN_WRITTEN_QUOTES + " ORDER BY " + KEY_ID + " DESC";
//
//		SQLiteDatabase db = this.getWritableDatabase();
//		Cursor cursor = db.rawQuery(selectQuery, null);
//
//		// looping through all rows and adding to list
//		if (cursor.moveToFirst()) {
//			do {
//				OwnWrittenQuotes contact = new OwnWrittenQuotes();
//				contact.setId(Integer.parseInt(cursor.getString(0)));
//				contact.setSubCategory(cursor.getString(1));
//				contact.setQoute(cursor.getString(2));
//				// Adding contact to list
//				contactList.add(contact);
//			} while (cursor.moveToNext());
//		}
//
//		// return contact list
//		return contactList;
//	}
//
//	// Updating single contact
//	public int updateContact(OwnWrittenQuotes contact) {
//		SQLiteDatabase db = this.getWritableDatabase();
//
//		ContentValues values = new ContentValues();
//		values.put(SUB_CATEGORY, contact.getSubCategory());
//		values.put(QOUTE, contact.getQoute());
//
//		// updating row
//		return db.update(OWN_WRITTEN_QUOTES, values, KEY_ID + " = ?", new String[] { String.valueOf(contact.getId()) });
//	}
//
//	// Deleting single contact
//	public void deleteContact(OwnWrittenQuotes contact) {
//		SQLiteDatabase db = this.getWritableDatabase();
//		db.delete(OWN_WRITTEN_QUOTES, KEY_ID + " = ?", new String[] { String.valueOf(contact.getId()) });
//		db.close();
//	}
//
//	// Getting contacts Count
//	public int getContactsCount() {
//		String countQuery = "SELECT  * FROM " + OWN_WRITTEN_QUOTES;
//		SQLiteDatabase db = this.getReadableDatabase();
//		Cursor cursor = db.rawQuery(countQuery, null);
//		cursor.close();
//
//		return cursor.getCount();
//	}
//
//}
