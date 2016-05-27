package app;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import model.ISource;
import model.Processor;
import model.SqlListener;
import model.SqlServer;
import model.TestMap;

import org.slf4j.LoggerFactory;

import config.Configure;
import connections.ItemURL;
import controller.Controller;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

public class Main {
	
	private final Controller controller;
	
	
	
	private final static ScheduledExecutorService scheduler = Executors
			.newScheduledThreadPool(1);
	
	
	final ScheduledFuture<?> exec;
	
	private static LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

	private static Logger logger = lc.getLogger("Main.class");

	private final Map<String,ItemURL> map;
	private final long delay;
	
	public Main(String db) throws JoranException {
		configureLogger("listener.xml");
		
		map = (Map<String, ItemURL>) Configure.JsonConfigure(
				getClass().getClassLoader().getResourceAsStream("url3.json"), ItemURL.class);
		
		delay = map.get("scheduler").getMinutes();
		
		
		
		 exec = scheduler.scheduleAtFixedRate(sched, 1,
					delay, TimeUnit.MINUTES);
		
		AddShutDownHook hook = new AddShutDownHook();
		hook.attachShutdownHook();
		
		controller = new Controller(map,db);
		
		
		
	}
	
	
	
	public static void main(String args []){
		
		if(args.length == 0) return;
		
		try {
			new Main(args[0]);
		} catch (JoranException e) {
			logger.error(e.getMessage());
		}
	}
	
	private void configureLogger(String logconfig) throws JoranException{
		JoranConfigurator configurator = new JoranConfigurator();
	    configurator.setContext(lc);
	    lc.reset();
	    
	    configurator.doConfigure(Main.class.getClassLoader().getResourceAsStream(logconfig));
		
	}
	
	final Runnable sched = new Runnable() {
		int count = 0;
		
		public void run() {
				try
				{
				Thread.currentThread().setName("Scheduler");
				controller.run();
				
				logger.debug(Integer.toString(count++));
				
				
				} catch (Throwable t) {
						t.printStackTrace();
						try {
							throw t;
						} catch (Throwable e) {
							
						}
					
					
				} 
			
		}
		
	};

	
	
	

}
