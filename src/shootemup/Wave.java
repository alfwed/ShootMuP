package shootemup;

public class Wave {

	private int interval;
	private String[] content;
	
	public Wave(int interval, String[] content) {
		this.interval = interval;
		this.content = content;
	}
	
	public int getInterval() {
		return interval;
	}
	
	public String[] getContent() {
		return content;
	}
	
}
