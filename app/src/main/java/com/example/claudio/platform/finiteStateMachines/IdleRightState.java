package com.example.claudio.platform.finiteStateMachines;

import com.example.claudio.platform.entities.Player;
import com.example.claudio.platform.time.Time;
import com.example.claudio.platform.toolBox.Input;
import com.example.claudio.platform.toolBox.Util;
import com.example.claudio.platform.toolBox.Vector2f;

/**
 * Created by Claudio on 18/05/2017.
 */

public class IdleRightState extends PlayerState {

    @Override
    public void enter(Player player) {
        idleRightAnimation.start(Time.getCurrentTime());
        player.getPhysicModel().setTargetSpeed(new Vector2f(0,0));
    }

    @Override
    public PlayerState handleInput(Player player) {
        if(Input.isKeyDown(Util.BUTTON_LEFT)){
            exit();
            return runningLeftState;
        }
        if(Input.isKeyDown(Util.BUTTON_RIGHT)){
            exit();
            return runningRightState;
        }
        if(Input.isKeyDown(Util.BUTTON_UP)){
            exit();
            return jumpingRightState;
        }
        if(Input.isKeyUp(Util.BUTTON_RIGHT) && Input.isKeyUp(Util.BUTTON_LEFT) && Input.isKeyUp(Util.BUTTON_UP)){
            return this;
        }
        return null;
    }

    @Override
    public void update(Player player) {
        idleRightAnimation.update(Time.getCurrentTime());
        player.getPhysicModel().setTargetSpeed(new Vector2f(0,0));
    }

    @Override
    public void exit() {
        idleRightAnimation.stop();
    }

    @Override
    public int getAnimationID() {
        return idleRightAnimation.getCurrentID();
    }
}
