package kos.cvut.getdata;

public class SearchCourse {
	private String TAG;
	private String DESC;
	private String url;

	public SearchCourse(String TAG, String DESC, String url) {
		this.setTAG(TAG);
		this.setDESC(DESC);
		this.setUrl(url);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getDESC() {
		return DESC;
	}

	public void setDESC(String dESC) {
		DESC = dESC;
	}

	public String getTAG() {
		return TAG;
	}

	public void setTAG(String tAG) {
		TAG = tAG;
	}
}