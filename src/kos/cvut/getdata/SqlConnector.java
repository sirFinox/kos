package kos.cvut.getdata;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.LinkedList;

import net.qmsource.android.cvut.kos.auth.KosHttpClient;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.kos.R;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragment;
import com.kos.authentification.authenticator.AuthenticatorActivity;
import com.kos.cvut.Constants;
import com.kos.cvut.KosDBHelper;
import com.kos.cvut.fragments.MainActivity;

public class SqlConnector {
	private static SqlConnector connector = null;
	private static com.kos.cvut.KosDBHelper connection;
	private static SQLiteDatabase db;
	private LinkedList<String> coursesList = new LinkedList<String>();
	private LinkedList<String> codeList = new LinkedList<String>();
	private LinkedList<String> urlList = new LinkedList<String>();
	private static final String TAG = "kos.cvut";
	private HttpClient client;
	private static final String username = "kosandr",
			password = "nAtras9UpruCRuza";
	private String[] COURSES_SELECT = new String[] { "name", "code", "credits",
			"completion", "lang", "classesLang", "classesType", "description",
			"homepage", "keywords", "lecturesContents", "literature",
			"objectives", "range", "requirements", "season",
			"tutorialsContents", "department" };

	private SqlConnector(Context context) {
		client = new KosHttpClient(username, password);
	}

	public static SqlConnector getInstance(Context context) {
		if (connector == null) {
			connector = new SqlConnector(context);
			connection = new KosDBHelper(context);
			db = connection.getWritableDatabase();
		}
		return connector;
	}

	public void addUser() {
		db.beginTransaction();
		db.endTransaction();
	}

	public LinkedList<String> getCourses() {
		db.beginTransaction();
		Cursor c = db.query("COURSES", new String[] { "name" }, null, null,
				null, null, null, null);
		int count = c.getCount();
		if (count <= 0) {

			String url = "https://kosapi.feld.cvut.cz/api/3b/";
			if (getClassification().matches("students")) {
				url = url + getClassification() + "/" + getUsername()
						+ "/enrolledCourses/";
				Element root = GetFromUrl(url);
				coursesList = getElements(root, "course");
				urlList = getElements(root, "course", "href");
				codeList = new LinkedList<String>();
				Iterator<String> urls = urlList.iterator();
				while (urls.hasNext()) {
					String code = urls.next();
					codeList.add(code.substring(code.indexOf("/") + 1,
							code.length() - 1));
				}
			} else {
				url = url + getClassification() + "/" + getUsername()
						+ "/courses/?multilang=false";
				Element root = GetFromUrl(url);
				coursesList = getAtomElements(root, "name", new String[] {
						"entry", "content", "name" });
				urlList = getElements(root, "link", "href");
				codeList = getElements(root, "code");
			}

		}
		db.endTransaction();
		return coursesList;
	}

	public LinkedList<String> getCodes() {
		if (coursesList.isEmpty()) {
			getCourses();
		}
		return codeList;
	}

	public Element GetFromUrl(String url) {
		HttpUriRequest request;
		db.beginTransaction();
		Element root = null;
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		request = new HttpGet(url);
		Log.i("GetKosData", url);
		request.setHeader("Content-Type", "application/atom+xml");
		InputStream data = null;
		try {
			HttpResponse response = client.execute(request);
			data = response.getEntity().getContent();
			// r = new InputStreamReader(data);
			java.util.Scanner s = new java.util.Scanner(data)
					.useDelimiter("\\A");
			String str;
			if (s.hasNext()) {
				str = s.next();
			} else
				str = "";
			Document document = DocumentHelper.parseText(str);
			root = document.getRootElement();
		} catch (ClientProtocolException e) {
			Log.e(TAG, "todo error handlig", e);
		} catch (IOException e) {
			Log.e(TAG, "todo error handlig", e);
		} catch (NullPointerException e) {
			Log.e(TAG, "todo error handlig", e);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				data.close();
				db.endTransaction();
			} catch (IOException e) {
				Log.e(TAG, "todo error handlig", e);
			}

		}
		return root;
	}

	public LinkedList<String[]> getInfo(String course, String course_code,
			Context ctx) {
		db.beginTransaction();
		getCourses(); // pro jistotu, ze LinkedList nejsou prazdne
		LinkedList<String[]> coursesInfoList = new LinkedList<String[]>();
		// Cursor c = db.query("COURSES", COURSES_SELECT, "name LIKE ?",
		// new String[] { "%" + course + "%" }, null, null, null, null);
		Cursor cu = db.rawQuery("SELECT * FROM COURSES", null);
		int pocet = cu.getCount();
		// String kod = cu.getString(1);

		// Cursor c = db.query(true, "COURSES", COURSES_SELECT, "name = ?", new
		// String[] { course }, null, null, null, null);
		Cursor c = db.rawQuery("SELECT * FROM COURSES WHERE name like '%"
				+ course + "%'", null);
		int count = c.getCount();
		if (count <= 0) {
			String[] link = new String[] { "", "" };

			String url = "https://kosapi.feld.cvut.cz/api/3b/";

			if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
						.permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}
			Iterator<String> i = coursesList.iterator();
			Iterator<String> j = codeList.iterator();
			Iterator<String> k = urlList.iterator();
			while (i.hasNext() && j.hasNext()) {
				String name = i.next();
				String code = j.next();
				String course_url = k.next();
				if (name.contains(course) && code.contains(course_code)) {
					link[0] = name;
					link[1] = course_url;
				}
			}
			Element root;
			url = "https://kosapi.feld.cvut.cz/api/3b/";
			url = url + link[1] + "?detail=1";
			root = GetFromUrl(url);
			String code = getElement(root, "code");
			String season = getElement(root, "season");
			String note = getElement(root, "note");
			String completion = getElement(root, "completion");
			String credits = getElement(root, "credits");
			String range = getElement(root, "range");
			String department = getElement(root, "department");
			String description = getElement(root, "desciption");
			String lecturesContents = getElement(root, "lecturesContents");
			String tutorialsContents = getElement(root, "tutorialsContents");
			String literature = getElement(root, "literature");
			String objectives = getElement(root, "objectives");
			String requirements = getElement(root, "requirements");
			String classesLang = getElement(root, "classesLang");
			String studyForm = getElement(root, "studyForm");
			coursesInfoList.add(new String[] { "Code", code });
			coursesInfoList.add(new String[] { "Season", season });
			coursesInfoList.add(new String[] { "Note", note });
			coursesInfoList.add(new String[] { "Completion", completion });
			coursesInfoList.add(new String[] { "Credits", credits });
			coursesInfoList.add(new String[] { "Range", range });
			coursesInfoList.add(new String[] { "Department", department });
			coursesInfoList.add(new String[] { "Description", description });
			coursesInfoList.add(new String[] { "Lectures contents",
					lecturesContents });
			coursesInfoList.add(new String[] { "Tutorials contents",
					tutorialsContents });
			coursesInfoList.add(new String[] { "Requirements", requirements });
			coursesInfoList.add(new String[] { "Objectives", objectives });
			coursesInfoList.add(new String[] { "Literature", literature });
			coursesInfoList
					.add(new String[] { "Classes language", classesLang });
			coursesInfoList.add(new String[] { "Study form", studyForm });
			ContentValues content = new ContentValues();
			content.put("code", code);
			content.put("season", season);
			content.put("note", note);
			content.put("completion", completion);
			content.put("credits", credits);
			content.put("range", range);
			content.put("department", department);
			content.put("name", course);
			content.put("description", description);
			content.put("lecturesContents", lecturesContents);
			content.put("tutorialsContents", tutorialsContents);
			content.put("requirements", requirements);
			content.put("objectives", objectives);
			content.put("literature", literature);
			content.put("classesLang", classesLang);
			content.put("studyForm", getStudyForm(studyForm, ctx));
			long row = db.insert("COURSES", null, content);
			db.setTransactionSuccessful();
		} else {
			c.moveToFirst();
			coursesInfoList.add(new String[] { "Code",
					c.getString(c.getColumnIndex("code")) });
			coursesInfoList.add(new String[] { "Season",
					c.getString(c.getColumnIndex("season")) });
			coursesInfoList.add(new String[] { "Note",
					c.getString(c.getColumnIndex("note")) });
			coursesInfoList.add(new String[] { "Completion",
					c.getString(c.getColumnIndex("completion")) });
			coursesInfoList.add(new String[] { "Credits",
					c.getString(c.getColumnIndex("credits")) });
			coursesInfoList.add(new String[] { "Range",
					c.getString(c.getColumnIndex("range")) });
			coursesInfoList.add(new String[] { "Department",
					c.getString(c.getColumnIndex("department")) });
			coursesInfoList.add(new String[] { "Description",
					c.getString(c.getColumnIndex("description")) });
			coursesInfoList.add(new String[] { "Lectures contents",
					c.getString(c.getColumnIndex("lecturesContents")) });
			coursesInfoList.add(new String[] { "Tutorials contents",
					c.getString(c.getColumnIndex("tutorialsContents")) });
			coursesInfoList.add(new String[] { "Requirements",
					c.getString(c.getColumnIndex("requirements")) });
			coursesInfoList.add(new String[] { "Objectives",
					c.getString(c.getColumnIndex("objectives")) });
			coursesInfoList.add(new String[] { "Literature",
					c.getString(c.getColumnIndex("literature")) });
			coursesInfoList.add(new String[] { "Classes language",
					c.getString(c.getColumnIndex("classesLang")) });
			coursesInfoList.add(new String[] { "Study form",
					c.getString(c.getColumnIndex("studyForm")) });
		}
		db.endTransaction();
		return coursesInfoList;
	}

	private String getStudyForm(String studyForm, Context ctx) {
		// TODO Auto-generated method stub
		if (studyForm.equalsIgnoreCase("FULLTIME")) {
			ctx.getResources().getString(R.string.fulltime);
		} else if (studyForm.equalsIgnoreCase("PARTTIME")) {
			ctx.getResources().getString(R.string.parttime);
		} else if (studyForm.equalsIgnoreCase("DISTANCE")) {
			ctx.getResources().getString(R.string.distance);
		} else if (studyForm.equalsIgnoreCase("SELF_PAYER")) {
			ctx.getResources().getString(R.string.self_payer);
		} else if (studyForm.equalsIgnoreCase("UNDEFINED")) {
			ctx.getResources().getString(R.string.undefined);
		}
		return null;
	}

	public LinkedList<String[]> getInfoFromUrl(String course_url) {
		LinkedList<String[]> coursesInfoList = new LinkedList<String[]>();
		String[] link = new String[] { "", "" };

		String url_full = "https://kosapi.feld.cvut.cz/api/3b/";
		url_full = url_full.concat(course_url);
		url_full = url_full.concat("?detail=1");
		Element root = GetFromUrl(url_full);
		String code = getElement(root, "code");
		String season = getElement(root, "season");
		String note = getElement(root, "note");
		String completion = getElement(root, "completion");
		String credits = getElement(root, "credits");
		String range = getElement(root, "range");
		String department = getElement(root, "department");
		coursesInfoList.add(new String[] { "Code", code });
		coursesInfoList.add(new String[] { "Season", season });
		coursesInfoList.add(new String[] { "Note", note });
		coursesInfoList.add(new String[] { "Completion", completion });
		coursesInfoList.add(new String[] { "Credits", credits });
		coursesInfoList.add(new String[] { "Range", range });
		coursesInfoList.add(new String[] { "Department", department });
		return coursesInfoList;
	}

	public int getCoursesCount() {
		if (coursesList.size() < 1) {
			getCourses();
		}
		int count = coursesList.size();
		return count;
	}

	public void saveUsername(String username, Context context) {
		db.beginTransaction();
		Cursor c = db.query("KOS", new String[] { "username" }, null, null,
				null, null, null, null);
		int count = c.getCount();
		if (count < 1) {
			ContentValues content = new ContentValues();
			// content.put("student", false);
			// content.put("teacher", false);
			content.put("username", username);
			long error = db.insert("KOS", null, content);
			if (error < 0) {
				Toast toast = Toast.makeText(context,
						"Fail in inserting username", Toast.LENGTH_SHORT);
				toast.show();
			} else {
				Toast toast = Toast.makeText(context, "Insert username ok: "
						+ Long.toString(error), Toast.LENGTH_SHORT);
				toast.show();
			}
		} else {
			ContentValues content = new ContentValues();
			content.put("username", username);
			db.update("KOS", content, null, null);
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public void setClassification(boolean student, boolean teacher) {
		db.beginTransaction();
		Cursor c = db.query("KOS", new String[] { "username" }, null, null,
				null, null, null, null);
		int count = c.getCount();
		if (count < 1) {
			ContentValues content = new ContentValues();
			content.put("student", student);
			content.put("teacher", teacher);
			db.insert("KOS", null, content);
		} else {
			ContentValues content = new ContentValues();
			content.put("student", student);
			content.put("teacher", teacher);
			db.update("KOS", content, null, null);
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public String getClassification() {
		String classification = "";
		db.beginTransaction();
		// Cursor c = db.query("KOS", new String[] { "username" }, null, null,
		// null, null, null, null);
		Cursor c = db.rawQuery("SELECT * FROM KOS", null);
		int count = c.getCount();
		if (count >= 1) {
			c.moveToFirst();

			int studentIndex = c.getColumnIndex("student");
			int teacherIndex = c.getColumnIndex("teacher");
			int student = c.getInt(studentIndex);
			int teacher = c.getInt(teacherIndex);

			if (student > 0) {
				classification = "students";
			} else if (teacher > 0) {
				classification = "teachers";
			} else{
				getStudentOrTeacher();
				return getClassification();
			}
		} else {
			getStudentOrTeacher();
			return getClassification();
		}
		db.endTransaction();
		return classification;
	}

	public String getUsername() {
		db.beginTransaction();
		String sqlUsername = "";
		// Cursor c = db.query("KOS", new String[] { "username" }, null, null,
		// null, null, null, null);
		Cursor c = db.rawQuery("SELECT * FROM KOS", null);
		int count = c.getCount();
		if (count > 0) {
			c.moveToFirst();
			int username_col = c.getColumnIndex("username");
			sqlUsername = c.getString(username_col);
		}
		db.endTransaction();
		return sqlUsername;
	}

	public String parseXml(Element el) {
		Iterator i = el.elements().iterator();
		String ent = "";
		while (i.hasNext()) {
			Element elem = (Element) i.next();
			if (elem.getName().matches("semester")
					|| elem.getName().matches("course")) {
				ent = ent + elem.getName();
				ent = ent + " " + elem.getData() + "\n";
				ent = ent + " " + elem.attributeValue("href") + "\n";
			}
			if (elem.elements().size() > 0) {
				ent = ent + parseXml(elem);
			}

		}
		return ent;
	}
	
	public String xmlToString(Element el) {
		Iterator i = el.elements().iterator();
		String ent = "";
		while (i.hasNext()) {
			Element elem = (Element) i.next();
			ent = ent + elem.getName();
			ent = ent + " " + elem.getData() + "\n";
			if (elem.elements().size() > 0) {
				ent = ent + parseXml(elem);
			}

		}
		return ent;
	}

	public LinkedList<String[]> getRoles(Element el) {
		Iterator i = el.elements().iterator();
		LinkedList<String[]> results = new LinkedList<String[]>();
		String[] result = new String[] { "", "" };
		while (i.hasNext()) {
			Element elem = (Element) i.next();
			if (elem.getName().matches("roles")) {
				Iterator j = elem.elements().iterator();
				while (j.hasNext()) {
					Element role = (Element) j.next();
					result[0] = role.getName();
					result[1] = role.attributeValue("href");
					results.add(result);
				}
				return results;
			}
			if (elem.elements().size() > 0) {
				results = getRoles(elem);
				if (results.size() > 0) {
					return results;
				}
			}

		}
		return results;
	}

	public String[] getElement(Element el, String name, String attrib,
			String filter) {
		Iterator i = el.elements().iterator();
		String[] result = new String[] { "", "" };
		while (i.hasNext()) {
			Element elem = (Element) i.next();
			if (name == elem.getName()) {
				if (((String) elem.getData()).contains(filter)) {
					result[0] = elem.getData().toString();
					result[1] = elem.attributeValue(attrib);
					break;
				}
			}
			if (elem.elements().size() > 0) {
				String[] res = getElement(elem, name, attrib, filter);
				if (res[1].length() > 0) {
					result = res;
					return result;
				}
			}

		}
		return result;
	}

	public LinkedList<String> getElements(Element el, String name, String attrib) {
		Iterator i = el.elements().iterator();
		LinkedList<String> results = new LinkedList<String>();
		String result;
		while (i.hasNext()) {
			Element elem = (Element) i.next();

			if (name == elem.getName()) {
				// result[0] = elem.getData().toString();
				result = elem.attributeValue(attrib);
				results.add(result);
			}
			if (elem.elements().size() > 0) {

				LinkedList<String> res = getElements(elem, name, attrib);
				Iterator j = res.iterator();
				while (j.hasNext()) {
					results.add((String) j.next());
				}

			}

		}
		return results;
	}

	public LinkedList<String> getElements(Element el, String name) {
		Iterator i = el.elements().iterator();
		LinkedList<String> results = new LinkedList<String>();
		while (i.hasNext()) {
			Element elem = (Element) i.next();
			if (name == elem.getName()) {
				results.add(elem.getData().toString());
			}
			if (elem.elements().size() > 0) {
				LinkedList<String> res = getElements(elem, name);
				Iterator j = res.iterator();
				while (j.hasNext()) {
					results.add((String) j.next());
				}
			}

		}
		return results;
	}

	public LinkedList<String> getAtomElements(Element el, String name,
			String[] qnames) {
		String qname = "";
		for (int i = 0; i < qnames.length; i++) {
			qname = qname.concat(qnames[i] + " ");
		}
		Iterator i = el.elements().iterator();
		LinkedList<String> results = new LinkedList<String>();
		while (i.hasNext()) {
			Element elem = (Element) i.next();
			if (qname.contains(elem.getQName().getName())) {
				if (name == elem.getName()) {
					results.add(elem.getData().toString());
				}

				if (elem.elements().size() > 0) {
					LinkedList<String> res = getAtomElements(elem, name, qnames);
					Iterator j = res.iterator();
					while (j.hasNext()) {
						results.add((String) j.next());
					}
				}
			}
		}
		return results;
	}

	public String getElement(Element el, String name) {
		Iterator i = el.elements().iterator();
		String result = "";
		while (i.hasNext()) {
			Element elem = (Element) i.next();
			if (name == elem.getName()) {
				result = (String) elem.getData();
				return result;
			}
			if (elem.elements().size() > 0) {
				result = getElement(elem, name);
				if (result.length() > 0) {
					return result;
				}
			}

		}
		return result;
	}

	private boolean connect() {
		// TODO Auto-generated method stub
		if (getUsername() != null && getUsername().length() > 0) {
			MainActivity.setOnline();
			client = new KosHttpClient(Constants.getUsername(),
					Constants.getPassword());
			return true;
		}
		return false;

	}

	public void getStudentOrTeacher() {
		if (connect()) {
			HttpUriRequest request;

			String url = "https://kosapi.feld.cvut.cz/api/3b/";
			if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
						.permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}
			url = url + "students/" + getUsername();
			request = new HttpGet(url);
			// Log.i("GetKosData", url);
			request.setHeader("Content-Type", "application/atom+xml");
			InputStream data = null;
			Reader r = null;
			try {
				Log.i("testing student/teacher", "starting request...");
				HttpResponse response;
				response = client.execute(request);
				data = response.getEntity().getContent();
				java.util.Scanner s = new java.util.Scanner(data)
						.useDelimiter("\\A");
				String str;
				if (s.hasNext()) {
					str = s.next();
				} else
					str = "";
				Document document = DocumentHelper.parseText(str);
				Element root = document.getRootElement();
				String clasification = xmlToString(root);
				if (clasification.contains("exception")) {
					setClassification(false, true);
				} else if (clasification.contains("entry")
						|| clasification.contains("title")
						|| clasification.contains("atom")) {
					setClassification(true, false);
				}
				data.close();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
