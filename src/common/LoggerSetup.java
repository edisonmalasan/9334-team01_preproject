package common;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;
/**
 * Sets the logger
 */
public class LoggerSetup {
    public static Logger setupLogger(String loggerName, String filePath) {
        Logger logger = Logger.getLogger(loggerName);
        logger.setUseParentHandlers(false);

//        // avoid duplicate
//        if (logger.getHandlers().length > 0) {
//            return logger;
//        }

        try {
            File logFile = new File(filePath);
            File logDir = logFile.getParentFile();

            if (logDir != null && !logDir.exists()) {
                logDir.mkdirs();
            }

            FileHandler fileHandler = new FileHandler(filePath, true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return logger;
    }
}