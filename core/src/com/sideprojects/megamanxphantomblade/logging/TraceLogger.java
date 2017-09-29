package com.sideprojects.megamanxphantomblade.logging;

/**
 * Created by buivuhoang on 30/09/17.
 */
public class TraceLogger implements Logger {
    @Override
    public void info(String log) {
        System.out.println(log);
    }

    @Override
    public void debug(String log) {
        System.out.println(log);
    }

    @Override
    public void warn(String log) {
        System.out.println(log);
    }

    @Override
    public void error(String log) {
        System.err.println(log);
    }

    @Override
    public boolean isInfoEnabled() {
        return true;
    }

    @Override
    public boolean isDebugEnabled() {
        return true;
    }

    @Override
    public boolean isWarnEnabled() {
        return true;
    }

    @Override
    public boolean isErrorEnabled() {
        return true;
    }
}
