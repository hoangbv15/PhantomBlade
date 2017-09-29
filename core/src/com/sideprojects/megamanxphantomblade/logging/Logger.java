package com.sideprojects.megamanxphantomblade.logging;

/**
 * Created by buivuhoang on 30/09/17.
 */
public interface Logger {
    void info(String log);
    void debug(String log);
    void warn(String log);
    void error(String log);

    boolean isInfoEnabled();
    boolean isDebugEnabled();
    boolean isWarnEnabled();
    boolean isErrorEnabled();
}
