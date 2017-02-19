package com.sideprojects.megamanxphantomblade.player.x;

import com.sideprojects.megamanxphantomblade.KeyMap;
import com.sideprojects.megamanxphantomblade.animation.XAnimationFactory;
import com.sideprojects.megamanxphantomblade.player.PlayerBase;
import com.sideprojects.megamanxphantomblade.player.TraceColour;

/**
 * Created by buivuhoang on 04/02/17.
 */
public class PlayerX extends PlayerBase {
    protected PlayerX(float x, float y, KeyMap keyMap) {
        super(x, y, keyMap);
    }

    @Override
    public void createAnimations() {
        animations = new XAnimationFactory();
    }

    @Override
    public TraceColour getTraceColour() {
        return new TraceColour(0, 0, 1);
    }
}
