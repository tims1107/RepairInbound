package model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;

import utils.ParseUtil;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;

import connections.ConcreteDAO;
import connections.IDao;

public class SqlServer  {
	
	private Connection con;
	
	private static LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

	private static Logger logger = lc.getLogger("SQL DAO");
	
	
	private PreparedStatement getinbound;
	
	private CallableStatement insertinbound;
	
	private ISource listener;
	
	private final IDao dao;
	
	private final Map<String,LabSources> refsource = new HashMap<String, LabSources>();
	
	private final Calendar cal = Calendar.getInstance();
	


	public SqlServer(IDao dao) {
		
		this.dao = dao;
		
		addRefSource(15, "HLAB-SOARIAN-EAST", 5);
		addRefSource(16, "HLAB-SOARIAN-WEST", 7);
	}
	
	private void addRefSource(int id,String source,int labid){
		refsource.put(source, new LabSources(id, source, labid));
	}
	
	
	public void setListener(ISource listener){
		this.listener = listener;
	}
	
	public void connect(){
		if(con == null){
			con = (Connection) dao.getConnection();
		}
		
		prepareStatements();
	}
	
	public void disconnect(){
		List<Object> resources = new ArrayList<Object>();
		
		
		resources.add(getinbound);
		resources.add(insertinbound);
		resources.add(con);
		
		
		
		for(Object o : resources){
			if(o != null){
				closeResource(o);
				
			}
		}
		
		dao.disconnect();
		con = null;
		getinbound = null;
		insertinbound = null;
		
		
		
		
	}
	
	private void closeResource(Object obj)  {
		
		try
		{
		if(obj instanceof Connection){
			
			((Connection) obj).close();
			
			logger.info("Connection closed: " + obj);
		} else if(obj instanceof PreparedStatement){
			
			((PreparedStatement) obj).close();
			logger.info("Prepared closed: " + obj);
			
		} else if(obj instanceof CallableStatement){
			
			((CallableStatement) obj).close();
			logger.info("Callable closed: " + obj);
		}
		
		} catch (SQLException se){
			logger.error(se.getMessage());
		}
	}
	
	private  String filename () {
		
		cal.add(Calendar.MILLISECOND, 10);
		   
	    String s = new String(Long.toHexString(cal.getTimeInMillis()).toUpperCase());
	    
	    s =  s + ".TXT";
	    return s;
	}
	
	
public int insertInbound(String source,int fileid){
		
		@SuppressWarnings("unused")
		int retcode = 0;
		int result = 0;
		ResultSet rst = null;
		
		
		try	{
  	
			
              
			insertinbound.setInt(1,0);
			insertinbound.setString(2,filename());
			insertinbound.setString(3, source);
			insertinbound.setInt(4, fileid);
			insertinbound.setInt(5, refsource.get(source).getId());
			insertinbound.setInt(6, refsource.get(source).getLabid());
					
			
		
			result = insertinbound.executeUpdate();
			
			
			retcode = insertinbound.getInt(1);
			
			logger.info("Result: " + retcode);
			
			
			
			
			
			return result;
		
			    	
		} catch (SQLException re){
			logger.error("Inbound Error returned");
			logger.error(re.getMessage());
			//rollbackTran();
		} finally {
			if(rst != null) try {rst.close();} catch(SQLException se){}
			
		}
		
		return 0;
	
	}


	
	
	private void closeStatement(ResultSet rst){
		if(rst != null){
			try {
				rst.close();
			} catch (SQLException e) {}
		}
	}
	
	public void getInbound(){
		ResultSet rst = null;
		
		try {
			rst = getinbound.executeQuery();
			while(rst.next()){
				logger.info("Fileid: " + rst.getInt(1));
				if(listener != null){
					listener.getSource(rst.getString(2), "", rst.getInt(1));
				}
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		} finally {
			closeStatement(rst);
		}
		
	}
	
	
	
	
	public void prepareStatements(){
		
		try
		{
			if(con == null){
				
				throw new SQLException("Connection not available");
			}
			
			logger.debug(con.toString());
			
			
			
			getinbound = con.prepareStatement("select r.fileid,r.filesource from inbound_labid l " +
					"full outer join rcg_files r ON r.fileid = l.fileid " + 
					"where l.fileid IS null " +
					"and r.create_time > '2016-05-04 16:00'");
			
			insertinbound = con.prepareCall("{? = call rcg_loginbound_new(?,?,?,?,?)}");
			insertinbound.registerOutParameter(1, Types.INTEGER);
			
			
			
		} catch (SQLException se){
			
			se.printStackTrace();
			
			logger.error("Can't prepare statements");
		}
	}
	
	
	
	

}
