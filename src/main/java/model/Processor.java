package model;

import java.util.List;

import org.slf4j.LoggerFactory;

import utils.ParseUtil;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import handlers.ISourceHandler;
import handlers.Source;
import handlers.SourceFactory;

public class Processor {
	
	private static LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

	private static Logger logger = lc.getLogger("Processor");
	
	
	private final ISourceHandler handler;
	
	private SqlListener sqllistener;
	
	private String reqnum;
	private String filename;
	private int fileid;
	

	public Processor() {
		
		handler = new SourceFactory().getHandler("HL7");
	}
	
	public void setSqlListener(SqlListener sqllistener){
		this.sqllistener = sqllistener;
	}
	
	public void setSource(String source,String reqnum,String filename,int fileid){
		this.reqnum = reqnum;
		this.filename = filename;
		this.fileid = fileid;
		
		handler.registerHandler(source);
		
		if(sqllistener != null){
			sqllistener.insertPost(fileid, reqnum, handler.getHandler().getMshsource(), filename);
		}
	
	}
	
		
		

}
