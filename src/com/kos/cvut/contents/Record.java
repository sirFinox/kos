package kos.cvut.getdata;

public class Record {
    private String TAG;
    private String DESC;
    
    public Record(String TAG, String DESC) {
        this.setTAG(TAG);
        this.setDESC(DESC);
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