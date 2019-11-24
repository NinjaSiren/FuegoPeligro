package com.mygdx.fuegopeligro.player;

import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.mygdx.fuegopeligro.ai.msg.MessageType;
import com.mygdx.fuegopeligro.entity.Entity;

/**
 * @author JDEsguerra
 */
public class LevelPlayerStatusProcessor extends PlayerStatusProcessor implements Telegraph {
    private boolean gameOverSignaled;

    public LevelPlayerStatusProcessor(final CurrentPlayerStatus status) {
        super(status);
        MessageManager.getInstance().addListener(this, MessageType.FINISH_LEVEL.code());
    }

    @Override
    protected void doUpdate(final Entity entity) {
        // No time or lives left: game over
        if (getStatus().getTime() < 0 || getStatus().getLives() < 1 && !gameOverSignaled) {
            MessageManager.getInstance().dispatchMessage(this, MessageType.GAME_OVER.code());
            gameOverSignaled = true;
        }

        /**
         *
         * Resets lives and time for the given checkpointed level.
         *
         */
        if (getStatus().getTime() > 0 && gameOverSignaled) {
            short time = 400;
            short lives = 3;
            getStatus().setTime(time);
            getStatus().setLives(lives);
            gameOverSignaled = false;
        }
    }

    @Override
    public boolean handleMessage(final Telegram msg) {
        getStatus().setLevel((byte) (getStatus().getLevel() + 1));
        if (getStatus().getLevel() > 2) {
            getStatus().setLevel((byte) (getStatus().getLevel() - 2));
            getStatus().setWorld((byte) (getStatus().getWorld() + 1));
        } else if (getStatus().getWorld() > 5) {
            getStatus().setLevel((byte) 2);
            getStatus().setWorld((byte) 5);
        }
        if (getStatus().getWorld() == 1) {
            getStatus().setCurrentWorld((byte) 1);
            if (getStatus().getLevel() == 1) {
                getStatus().setCurrentLevel((byte) 1);
            } else if (getStatus().getLevel() == 2) {
                getStatus().setCurrentLevel((byte) 2);
            }
        } else if (getStatus().getWorld() == 2) {
            getStatus().setCurrentWorld((byte) 2);
            if (getStatus().getLevel() == 1) {
                getStatus().setCurrentLevel((byte) 1);
            } else if (getStatus().getLevel() == 2) {
                getStatus().setCurrentLevel((byte) 2);
            }
        } else if (getStatus().getWorld() == 3) {
            getStatus().setCurrentWorld((byte) 2);
            if (getStatus().getLevel() == 1) {
                getStatus().setCurrentLevel((byte) 1);
            } else if (getStatus().getLevel() == 2) {
                getStatus().setCurrentLevel((byte) 2);
            }
        } else if (getStatus().getWorld() == 4) {
            getStatus().setCurrentWorld((byte) 2);
            if (getStatus().getLevel() == 1) {
                getStatus().setCurrentLevel((byte) 1);
            } else if (getStatus().getLevel() == 2) {
                getStatus().setCurrentLevel((byte) 2);
            }
        } else if (getStatus().getWorld() == 5) {
            getStatus().setCurrentWorld((byte) 2);
            if (getStatus().getLevel() == 1) {
                getStatus().setCurrentLevel((byte) 1);
            } else if (getStatus().getLevel() == 2) {
                getStatus().setCurrentLevel((byte) 2);
            }
        }
        getStatus().setTime(PlayerStatus.DEFAULT_TIME);
        MessageManager.getInstance().dispatchMessage(this, MessageType.BEGIN_LEVEL.code());
        return true;
    }
}
