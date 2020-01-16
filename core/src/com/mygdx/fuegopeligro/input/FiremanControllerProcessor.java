package com.mygdx.fuegopeligro.input;

import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.mappings.Ouya;
import com.mygdx.fuegopeligro.ai.fsm.FiremanState;
import com.mygdx.fuegopeligro.ai.msg.MessageType;
import com.mygdx.fuegopeligro.entity.Entity;
import com.mygdx.fuegopeligro.entity.Fireman;

/**
 * Handles input from an Ouya controller to change the inner state of a {@link Fireman}.
 *
 * @author JDEsguerra
 */
public class FiremanControllerProcessor extends ControllerAdapter {
    private final static int JUMP_KEY = Ouya.BUTTON_O;
    private final static int MOVE_AXIS = Ouya.AXIS_LEFT_X;
    private static final int RESET_KEY = Ouya.BUTTON_R2;

    private final Entity character;

    public FiremanControllerProcessor(final Fireman fireman) {
        if (fireman == null) {
            throw new IllegalArgumentException("'character' cannot be null");
        }
        this.character = fireman;
    }

    @Override
    public boolean buttonDown(final Controller controller, final int buttonCode) {
        if (buttonCode == JUMP_KEY) {
            character.changeState(FiremanState.JUMP);
        } else if (buttonCode == RESET_KEY) {
            MessageManager.getInstance().dispatchMessage(null, MessageType.DEAD.code(), character);
        }
        return super.buttonDown(controller, buttonCode);
    }

    @Override
    public boolean buttonUp(final Controller controller, final int buttonCode) {
        if (buttonCode == JUMP_KEY) {
            character.changeState(FiremanState.IDLE);
        }
        return super.buttonUp(controller, buttonCode);
    }

    @Override
    public boolean axisMoved(final Controller controller, final int axisIndex, final float value) {
        if (axisIndex == MOVE_AXIS) {
            float axisValue = 0.5f * value;
            if (Math.abs(axisValue) > Ouya.STICK_DEADZONE) {
                if (axisValue > 0) {
                    character.changeState(FiremanState.RIGHT);
                } else {
                    character.changeState(FiremanState.LEFT);
                }
            } else {
                character.changeState(FiremanState.IDLE);
            }
        }
        return super.axisMoved(controller, axisIndex, value);
    }
}
