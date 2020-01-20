package com.mygdx.fuegopeligro.player;

import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.mygdx.fuegopeligro.ai.msg.MessageType;
import com.mygdx.fuegopeligro.entity.Entity;

/**
 * @author JDEsguerra
 *
 */
public class FiremanPlayerStatusProcessor extends PlayerStatusProcessor implements Telegraph {
    /**
     * Points earned by gathering a collectible.
     */
    private static final int COLLECTIBLE_POINTS = 500;

    public FiremanPlayerStatusProcessor(final CurrentPlayerStatus status) {
        super(status);
        MessageManager.getInstance().addListeners(this,
                MessageType.COLLECTED.code(), MessageType.DEAD.code(),
                MessageType.LOAD_NEXT_LEVEL.code(), MessageType.LOAD_NEW_GAME.code());
    }

    private void LoadEasy(byte levelValue) {
        if(levelValue == 1) {
            getStatus().setEqavalue((byte)((Math.random() * 31) + 1));
        } else if(levelValue == 2) {
            getStatus().setEqavalue((byte)((Math.random() * 31) + 7));
        } else if(levelValue == 3) {
            getStatus().setEqavalue((byte)((Math.random() * 31) + 13));
        } else if(levelValue == 4) {
            getStatus().setEqavalue((byte)((Math.random() * 31) + 19));
        } else if(levelValue == 5) {
            getStatus().setEqavalue((byte)((Math.random() * 31) + 25));
        }
    }

    private void LoadHard(byte levelValue) {
        if(levelValue == 1) {
            getStatus().setHqaValue((byte)((Math.random() * 41) + 1));
        } else if(levelValue == 2) {
            getStatus().setHqaValue((byte)((Math.random() * 41) + 9));
        } else if(levelValue == 3) {
            getStatus().setHqaValue((byte)((Math.random() * 41) + 17));
        } else if(levelValue == 4) {
            getStatus().setHqaValue((byte)((Math.random() * 41) + 25));
        } else if(levelValue == 5) {
            getStatus().setHqaValue((byte)((Math.random() * 41) + 33));
        }
    }

    private void Randomize() {
        getStatus().setMGValue((byte)((Math.random() * 4) + 1));
        getStatus().setCollectibles((short)(getStatus().getCollectibles() + 1));
        getStatus().setScore(getStatus().getScore() + COLLECTIBLE_POINTS);

        if (getStatus().getCurrentWorld() == 1) {
            LoadEasy(getStatus().getCurrentLevel());
        } else if (getStatus().getCurrentWorld() == 2) {
            LoadHard(getStatus().getCurrentLevel());
        }
    }

    private void doIfDead() {
        if (getStatus().getLives() > 0) {
            getStatus().setLives((byte)(getStatus().getLives() - 1));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mygdx.fuegopeligro.player.PlayerStatusProcessor#doUpdate(com.mygdx.fuegopeligro.entity.Entity)
     */
    @Override
    protected void doUpdate(final Entity character) {

    }

    @Override
    public boolean handleMessage(final Telegram msg) {
        if (msg.message == MessageType.COLLECTED.code()) {
            Randomize();
        } else if (msg.message == MessageType.LOAD_NEW_GAME.code()) {
            Randomize();
        } else if (msg.message == MessageType.LOAD_CURRENT_GAME.code()) {
            Randomize();
        } else if (msg.message == MessageType.DEAD.code()) {
            doIfDead();
        }
        return true;
    }
}
