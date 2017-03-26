package com.sideprojects.megamanxphantomblade.input;

import com.badlogic.gdx.Gdx;
import com.sideprojects.megamanxphantomblade.KeyMap;

/**
 * Created by buivuhoang on 22/02/17.
 */
public class PlayerInputProcessor implements InputProcessor {
    private KeyMap keyMap;

    public PlayerInputProcessor(KeyMap keyMap) {
        this.keyMap = keyMap;
    }

    @Override
    public boolean isCommandPressed(Command command) {
        switch (command) {
            case LEFT:
                return Gdx.input.isKeyPressed(keyMap.left);
            case RIGHT:
                return Gdx.input.isKeyPressed(keyMap.right);
            case UP:
                return Gdx.input.isKeyPressed(keyMap.up);
            case JUMP:
                return Gdx.input.isKeyPressed(keyMap.jump);
            case DASH:
                return Gdx.input.isKeyPressed(keyMap.dash);
            case ATTACK:
                return Gdx.input.isKeyPressed(keyMap.attack);
            default:
                return false;
        }
    }

    @Override
    public boolean isCommandJustPressed(Command command) {
        switch (command) {
            case LEFT:
                return Gdx.input.isKeyJustPressed(keyMap.left);
            case RIGHT:
                return Gdx.input.isKeyJustPressed(keyMap.right);
            case UP:
                return Gdx.input.isKeyJustPressed(keyMap.up);
            case JUMP:
                return Gdx.input.isKeyJustPressed(keyMap.jump);
            case DASH:
                return Gdx.input.isKeyJustPressed(keyMap.dash);
            case ATTACK:
                return Gdx.input.isKeyJustPressed(keyMap.attack);
            default:
                return false;
        }
    }
}
