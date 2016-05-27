package model;

public interface SqlListener {
	public TestMap getMapping(int fileid,String testcode);
	public void insertPost(int fileid,String reqnum,String rawdata,String filename);
}
