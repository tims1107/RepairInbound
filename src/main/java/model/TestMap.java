package model;


import java.io.Serializable;


public class TestMap {
	
	public TestMap(String protoncode,String testcode,String testdesc, String testtype, String containerabbrev, String containerdesc){
		this.protoncode = protoncode.trim();
		this.testcode = testcode.trim();
		this.testdesc = testdesc.trim();
		this.testtype = testtype.trim();
		this.containerabbrev = containerabbrev.trim();
		this.containerdesc = containerdesc.trim();
		
	}
		
	private String protoncode;
	private String testcode;
	private String testdesc;
	private String testtype;
	private String containerabbrev;
	private String containerdesc;
	
	
	public String getProtoncode() {
		return protoncode.trim();
	}
	private void setProtoncode(String protoncode) {
		this.protoncode = protoncode;
	}
	public String getTestcode() {
		return testcode.trim();
	}
	private void setTestcode(String testcode) {
		this.testcode = testcode;
	}
	public String getTestdesc() {
		return testdesc.trim();
	}
	private void setTestdesc(String testdesc) {
		this.testdesc = testdesc;
	}
	public String getTesttype() {
		return testtype.trim();
	}
	private void setTesttype(String testtype) {
		this.testtype = testtype;
	}
	public String getContainerabbrev() {
		return containerabbrev.trim();
	}
	private void setContainerabbrev(String containerabbrev) {
		this.containerabbrev = containerabbrev;
	}
	public String getContainerdesc() {
		return containerdesc.trim();
	}
	private void setContainerdesc(String containerdesc) {
		this.containerdesc = containerdesc;
	}
	
}
