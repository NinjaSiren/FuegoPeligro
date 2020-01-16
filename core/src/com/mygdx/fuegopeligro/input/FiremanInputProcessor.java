package com.mygdx.fuegopeligro.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.input.GestureDetector;
import com.mygdx.fuegopeligro.ai.fsm.FiremanState;
import com.mygdx.fuegopeligro.ai.msg.MessageType;
import com.mygdx.fuegopeligro.entity.Entity;
import com.mygdx.fuegopeligro.entity.Fireman;

/**
 * Handles input from keyboard to change the inner state of a {@link Fireman}.
 *
 * @author JDEsguerra
 */
public class FiremanInputProcessor extends GestureDetector.GestureAdapter implements Telegraph, InputProcessor, GestureDetector.GestureListener {
    private final static int JUMP_KEY = Keys.W;
    private final static int LEFT_KEY = Keys.A;
    private final static int RIGHT_KEY = Keys.D;
    private final static int RESET_KEY = Keys.BACKSPACE;

    private final Entity character;

    public FiremanInputProcessor(final Fireman fireman) {
        if (fireman == null) {
            throw new IllegalArgumentException("'character' cannot be null"); }
        character = fireman;
        MessageManager.getInstance().addListener(this, MessageType.EXIT.code());
        MessageManager.getInstance().addListener(this, MessageType.MOVE_LEFT.code());
        MessageManager.getInstance().addListener(this, MessageType.MOVE_RIGHT.code());
        MessageManager.getInstance().addListener(this, MessageType.MOVE_JUMP.code());
    }

    @Override
    public boolean keyDown(final int keycode) {
        switch (keycode) {
            case JUMP_KEY:
                character.changeState(FiremanState.JUMP);
                break;
            case LEFT_KEY:
                character.changeState(FiremanState.LEFT);
                break;
            case RIGHT_KEY:
                character.changeState(FiremanState.RIGHT);
                break;
            case RESET_KEY:
                MessageManager.getInstance().dispatchMessage(null, MessageType.DEAD.code(), character);
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(final int keycode) {
        switch (keycode) {
            case JUMP_KEY:
                if (Gdx.input.isKeyPressed(RIGHT_KEY)) {
                    character.changeState(FiremanState.RIGHT);
                } else if (Gdx.input.isKeyPressed(LEFT_KEY)) {
                    character.changeState(FiremanState.LEFT);
                } else {
                    character.changeState(FiremanState.IDLE);
                }
                break;
            case LEFT_KEY:
                if (Gdx.input.isKeyPressed(RIGHT_KEY)) {
                    character.changeState(FiremanState.RIGHT);
                } else {
                    character.changeState(FiremanState.IDLE);
                }
                break;
            case RIGHT_KEY:
                if (Gdx.input.isKeyPressed(LEFT_KEY)) {
                    character.changeState(FiremanState.LEFT);
                } else {
                    character.changeState(FiremanState.IDLE);
                }
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    // Move left or right
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        int leftRight;
        if (Gdx.input.isTouched()) {
            if (Gdx.input.getX() < Gdx.graphics.getWidth() / 2){
                //left
                leftRight = 1;
                moveLeftRight(leftRight);
            } else {
                //right
                leftRight = 2;
                moveLeftRight(leftRight);
            }
        }
        return false;
    }

    private void moveLeftRight(int value) {
        switch (value) {
            case 1:
                character.changeState(FiremanState.LEFT);
                break;
            case 2:
                character.changeState(FiremanState.RIGHT);
                break;
        }
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        if (count == 2) {
            character.changeState(FiremanState.JUMP);
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public boolean handleMessage(final Telegram msg) {
        if (msg.message == MessageType.MOVE_LEFT.code()) {
            keyUp(LEFT_KEY);
            keyDown(LEFT_KEY);
        } else if (msg.message == MessageType.MOVE_RIGHT.code()) {
            keyUp(RIGHT_KEY);
            keyDown(RIGHT_KEY);
        } else if (msg.message == MessageType.MOVE_JUMP.code()) {
            keyUp(JUMP_KEY);
            keyDown(JUMP_KEY);
        }
        Gdx.input.setInputProcessor(null);
        return false;
    }
}
