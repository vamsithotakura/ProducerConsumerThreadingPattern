package my.ParallelFileProcessing;

/* AUTHOR : VAMSI THOTAKURA */

public class CountsMetaData {

	private int countsA = 0;
	private int countsT = 0;
	private int countsG = 0;
	private int countsC = 0;

	private String threadName;

	private boolean isEndOfFile;

	public int getCountsA() {
		return countsA;
	}
	public void setCountsA(int countsA) {
		this.countsA = countsA;
	}
	public int getCountsT() {
		return countsT;
	}
	public void setCountsT(int countsT) {
		this.countsT = countsT;
	}
	public int getCountsG() {
		return countsG;
	}
	public void setCountsG(int countsG) {
		this.countsG = countsG;
	}
	public int getCountsC() {
		return countsC;
	}
	public void setCountsC(int countsC) {
		this.countsC = countsC;
	}
	public String getThreadName() {
		return threadName;
	}
	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}
	public boolean isEndOfFile() {
		return isEndOfFile;
	}
	public void setEndOfFile(boolean isEndOfFile) {
		this.isEndOfFile = isEndOfFile;
	}

}
