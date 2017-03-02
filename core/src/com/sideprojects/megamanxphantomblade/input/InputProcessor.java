package com.sideprojects.megamanxphantomblade.input;

/**
 * Created by buivuhoang on 22/02/17.
 */
public interface InputProcessor {
    boolean isCommandPressed(Command command);
    boolean isCommandJustPressed(Command command);
}
