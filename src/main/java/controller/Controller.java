package controller;

import java.util.Map;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;

import model.ISource;
import model.Processor;
import model.SqlListener;
import model.SqlServer;
import model.TestMap;

import connections.ConcreteDAO;
import connections.ItemURL;

public class Controller {
	
	private final Map<String,ItemURL> map;
	
	private final SqlServer sqlserver;
	private final Processor process;
	
	private static LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

	private static Logger logger = lc.getLogger("Controller");
	

	public Controller(Map<String,ItemURL> map,String db) {
		this.map = map;
		process = new Processor();
		
		sqlserver = new SqlServer(new ConcreteDAO(map.get(db)));
		
		setListeners();
	}
	
	public void run(){
		
		
		sqlserver.connect();
		
		sqlserver.getInbound();
		
		sqlserver.disconnect();
		
	}
	
	private void setListeners(){
		process.setSqlListener(new SqlListener() {
			
			public TestMap getMapping(int fileid, String testcode) {
				return null;
			}

			public void insertPost(int fileid, String reqnum, String rawdata,
					String filename) {
			
				
				sqlserver.insertInbound(rawdata, fileid);
				
				logger.info(rawdata);
				
			}
		});
		
		sqlserver.setListener(new ISource(){

			public void getSource(String source, String reqnum,int fileid) {
				process.setSource(source, reqnum,"",fileid);
				
			}
	
		});
	}

}
