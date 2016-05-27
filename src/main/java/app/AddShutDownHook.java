package app;



import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

public class AddShutDownHook {
	
	private static LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

	private static Logger logger = lc.getLogger("Inbound Validation Shutdown Hook");
	
	AddShutDownHook(){
		logger.setLevel(Level.ERROR);
		logger.debug("Shutdown Hook Attached");
	}
	
	public void attachShutdownHook() {
		
		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run(){
				
				logger.error("Inside Hook");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
	}
}
