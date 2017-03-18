package com.sideprojects.megamanxphantomblade.player.x;

import com.sideprojects.megamanxphantomblade.animation.Sprites;
import com.sideprojects.megamanxphantomblade.player.PlayerAnimation;

/**
 * Created by buivuhoang on 05/02/17.
 */
public class PlayerXAnimation extends PlayerAnimation {
    @Override
    protected String getTextureAtlas(Type type) {
        switch (type) {
            case Idle:
                return Sprites.XIdle;
            case Walljump:
                return Sprites.XWallSlide;
            case Run:
                return Sprites.XRun;
            case Jump:
            case Fall:
            case Touchdown:
                return Sprites.XJump;
            case Wallslide:
                return Sprites.XWallSlide;
            case Dashrocket:
                return Sprites.XDashRocket;
            case Dash:
            case Dashbreak:
                return Sprites.XDash;
            case Updash:
                return Sprites.XUpDash;
            case Updashrocket:
                return Sprites.XUpDashRocket;
            default:
                return null;
        }
    }

    @Override
    protected int[] getAnimationIndex(Type type) {
        switch (type) {
            case Idle:
                return new int[] {1, 0, 0, 0, 1, 2, 2, 2, 1, 0, 0, 0, 1, 2, 2, 2, 1, 0, 0, 0, 1, 3, 4, 3};
            case Walljump:
                return new int[] {4, 5, 6};
            case Run:
                return null;
            case Jump:
                return new int[] {0, 1, 2, 3};
            case Fall:
                return new int[] {3, 4, 5, 6, 7};
            case Touchdown:
                return new int[] {8, 9, 10};
            case Wallslide:
                return new int[] {0, 1, 2, 3};
            case Dashrocket:
                return null;
            case Dash:
                return new int[] {0, 1, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3};
            case Dashbreak:
                return new int[] {4, 5, 6, 7};
            case Updash:
            case Updashrocket:
                return null;
            default:
                return null;
        }
    }
}
