package com.ajjpj.asysmon.config.log;

import com.ajjpj.asysmon.util.AStringFunction;
import org.apache.log4j.Logger;

/**
 * @author arno
 */
public class ALog4JLogger implements ASysMonLogger {
    private static final Logger log = Logger.getLogger("com.ajjpj.asysmon");

    public static final ALog4JLogger INSTANCE = new ALog4JLogger();

    @Override public void debug(AStringFunction msg) {
        log.debug(msg.apply());
    }

    @Override
    public void info(String msg) {
        log.info(msg);
    }

    @Override public void warn(String msg) {
        log.warn(msg);
    }

    @Override public void warn(String msg, Exception exc) {
        log.warn(msg, exc);
    }

    @Override public void error(String msg) {
        log.error(msg);
    }

    @Override public void error(String msg, Exception exc) {
        log.error(msg, exc);
    }
}
