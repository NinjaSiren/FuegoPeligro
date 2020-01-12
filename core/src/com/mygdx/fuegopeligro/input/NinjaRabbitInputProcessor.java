package com.mygdx.fuegopeligro.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.mygdx.fuegopeligro.ai.fsm.NinjaRabbitState;
import com.mygdx.fuegopeligro.ai.msg.MessageType;
import com.mygdx.fuegopeligro.entity.Entity;
import com.mygdx.fuegopeligro.entity.NinjaRabbit;

/**
 * Handles input from keyboard to change the inner state of a {@link NinjaRabbit}.
 *
 * @author JDEsguerra
 */
public class NinjaRabbitInputProcessor extends InputAdapter implements Telegraph, InputProcessor {
    private final static int JUMP_KEY = Keys.W;
    private final static int LEFT_KEY = Keys.A;
    private final static int RIGHT_KEY = Keys.D;
    private final static int RESET_KEY = Keys.BACKSPACE;

    private final Entity character;

    public NinjaRabbitInputProcessor(final NinjaRabbit ninjaRabbit) {
        if (ninjaRabbit == null) {
            throw new IllegalArgumentException("'character' cannot be null"); }
        this.character = ninjaRabbit;
        MessageManager.getInstance().addListener(this, MessageType.EXIT.code());
    }

    @Override
    public boolean keyDown(final int keycode) {
        switch (keycode) {
            case JUMP_KEY:
                character.changeState(NinjaRabbitState.JUMP);
                break;
            case LEFT_KEY:
                character.changeState(NinjaRabbitState.LEFT);
                break;
            case RIGHT_KEY:
                character.changeState(NinjaRabbitState.RIGHT);
                break;
            case RESET_KEY:
                MessageManager.getInstance().dispatchMessage(null, MessageType.DEAD.code(), character);
                break;
            default:
                break;
        }
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(final int keycode) {
        switch (keycode) {
            case JUMP_KEY:
                if (Gdx.input.isKeyPressed(RIGHT_KEY)) {
                    character.changeState(NinjaRabbitState.RIGHT);
                } else if (Gdx.input.isKeyPressed(LEFT_KEY)) {
                    character.changeState(NinjaRabbitState.LEFT);
                } else {
                    character.changeState(NinjaRabbitState.IDLE);
                }
                break;
            case LEFT_KEY:
                if (Gdx.input.isKeyPressed(RIGHT_KEY)) {
                    character.changeState(NinjaRabbitState.RIGHT);
                } else {
                    character.changeState(NinjaRabbitState.IDLE);
                }
                break;
            case RIGHT_KEY:
                if (Gdx.input.isKeyPressed(LEFT_KEY)) {
                    character.changeState(NinjaRabbitState.LEFT);
                } else {
                    character.changeState(NinjaRabbitState.IDLE);
                }
                break;
            default:
                break;
        }
        return super.keyUp(keycode);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        /*if(Gdx.input.isTouched()) {
            float pointerFirst = Gdx.input.getX(0);
            float pointerSecond = Gdx.input.getX(1);
            float pointerCenter = 0;

            if(pointerFirst < pointerCenter) {
                character.changeState(NinjaRabbitState.LEFT);
                if(pointerSecond < pointerCenter) {
                    character.changeState(NinjaRabbitState.LEFT);
                    if(Gdx.input.getAccelerometerX() > 10 || Gdx.input.getAccelerometerX() < 10) {
                        character.changeState(NinjaRabbitState.JUMP);
                    } else if(Gdx.input.getAccelerometerY() > 10 || Gdx.input.getAccelerometerY() < 10) {
                        character.changeState(NinjaRabbitState.JUMP);
                    } else if(Gdx.input.getAccelerometerZ() > 10 || Gdx.input.getAccelerometerZ() < 10) {
                        character.changeState(NinjaRabbitState.JUMP);
                    } else {
                        character.changeState(NinjaRabbitState.IDLE);
                    }
                } else if(pointerSecond > pointerCenter) {
                    character.changeState(NinjaRabbitState.RIGHT);
                    if(Gdx.input.getAccelerometerX() > 10 || Gdx.input.getAccelerometerX() < 10) {
                        character.changeState(NinjaRabbitState.JUMP);
                    } else if(Gdx.input.getAccelerometerY() > 10 || Gdx.input.getAccelerometerY() < 10) {
                        character.changeState(NinjaRabbitState.JUMP);
                    } else if(Gdx.input.getAccelerometerZ() > 10 || Gdx.input.getAccelerometerZ() < 10) {
                        character.changeState(NinjaRabbitState.JUMP);
                    } else {
                        character.changeState(NinjaRabbitState.IDLE);
                    }
                } else {
                    character.changeState(NinjaRabbitState.IDLE);
                }
            } else if(pointerSecond > pointerCenter) {
                character.changeState(NinjaRabbitState.RIGHT);
                if(pointerSecond < pointerCenter) {
                    character.changeState(NinjaRabbitState.LEFT);
                    if(Gdx.input.getAccelerometerX() > 10 || Gdx.input.getAccelerometerX() < 10) {
                        character.changeState(NinjaRabbitState.JUMP);
                    } else if(Gdx.input.getAccelerometerY() > 10 || Gdx.input.getAccelerometerY() < 10) {
                        character.changeState(NinjaRabbitState.JUMP);
                    } else if(Gdx.input.getAccelerometerZ() > 10 || Gdx.input.getAccelerometerZ() < 10) {
                        character.changeState(NinjaRabbitState.JUMP);
                    } else {
                        character.changeState(NinjaRabbitState.IDLE);
                    }
                } else if(pointerSecond > pointerCenter) {
                    character.changeState(NinjaRabbitState.RIGHT);
                    if(Gdx.input.getAccelerometerX() > 10 || Gdx.input.getAccelerometerX() < 10) {
                        character.changeState(NinjaRabbitState.JUMP);
                    } else if(Gdx.input.getAccelerometerY() > 10 || Gdx.input.getAccelerometerY() < 10) {
                        character.changeState(NinjaRabbitState.JUMP);
                    } else if(Gdx.input.getAccelerometerZ() > 10 || Gdx.input.getAccelerometerZ() < 10) {
                        character.changeState(NinjaRabbitState.JUMP);
                    } else {
                        character.changeState(NinjaRabbitState.IDLE);
                    }
                } else {
                    character.changeState(NinjaRabbitState.IDLE);
                }
            } else {
                character.changeState(NinjaRabbitState.IDLE);
            }
        }*/
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return true;
    }

    private void moveLeft() {
        addListeners();
        keyDown(LEFT_KEY);
        keyUp(LEFT_KEY);
    }

    private void moveRight() {
        addListeners();
        keyDown(RIGHT_KEY);
        keyUp(RIGHT_KEY);
    }

    private void moveJump() {
        addListeners();
        keyDown(JUMP_KEY);
        keyUp(JUMP_KEY);
    }

    private void addListeners() {
        MessageManager manager = MessageManager.getInstance();
        manager.clear();
        manager.addListeners(this, MessageType.MOVE_LEFT.code(),
                MessageType.MOVE_RIGHT.code(), MessageType.MOVE_JUMP.code());
    }

    @Override
    public boolean handleMessage(final Telegram msg) {
        if (msg.message == MessageType.MOVE_LEFT.code()) {
            moveLeft();
        } else if (msg.message == MessageType.MOVE_RIGHT.code()) {
            moveRight();
        } else if (msg.message == MessageType.MOVE_JUMP.code()) {
            moveJump();
        }
        Gdx.input.setInputProcessor(null);
        return false;
    }
}
