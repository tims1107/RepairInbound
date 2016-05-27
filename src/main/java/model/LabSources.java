package model;

public class LabSources {
	
	private final int id;
	private final String source;
	private final int labid;
	
	
	public LabSources(int id, String source, int labid) {
		
		this.id = id;
		this.source = source;
		this.labid = labid;
	}


	public int getId() {
		return id;
	}


	public String getSource() {
		return source.trim();
	}


	public int getLabid() {
		return labid;
	}
	
	

	

}
