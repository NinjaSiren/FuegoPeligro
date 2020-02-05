package com.mygdx.fuegopeligro.player;

import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.mygdx.fuegopeligro.RandomNumberGenerator;
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
    private static final int ADDITIONAL_SCORE = 250;

    public FiremanPlayerStatusProcessor(final CurrentPlayerStatus status) {
        super(status);
        MessageManager.getInstance().addListeners(this,
                MessageType.COLLECTED.code(), MessageType.DEAD.code(),
                MessageType.LOAD_NEXT_LEVEL.code(), MessageType.LOAD_NEW_GAME.code(),
                MessageType.CORRECT_ANSWER.code(), MessageType.WRONG_ANSWER.code(),
                MessageType.HINT_USED.code());
    }

    private void LoadEasy(int levelValue) {
        switch (levelValue) {
            case 1:
                getStatus().setEqavalue(new RandomNumberGenerator(1, 30).getGeneratedNumber());
                break;
            case 2:
                getStatus().setEqavalue(new RandomNumberGenerator(7, 30).getGeneratedNumber());
                break;
            case 3:
                getStatus().setEqavalue(new RandomNumberGenerator(13, 30).getGeneratedNumber());
                break;
            case 4:
                getStatus().setEqavalue(new RandomNumberGenerator(19, 30).getGeneratedNumber());
                break;
            case 5:
                getStatus().setEqavalue(new RandomNumberGenerator(25, 30).getGeneratedNumber());
                break;
        }
    }

    private void LoadHard(int levelValue) {
        switch (levelValue) {
            case 1:
                getStatus().setHqaValue(new RandomNumberGenerator(1, 40).getGeneratedNumber());
                break;
            case 2:
                getStatus().setHqaValue(new RandomNumberGenerator(9, 40).getGeneratedNumber());
                break;
            case 3:
                getStatus().setHqaValue(new RandomNumberGenerator(17, 40).getGeneratedNumber());
                break;
            case 4:
                getStatus().setHqaValue(new RandomNumberGenerator(25, 40).getGeneratedNumber());
                break;
            case 5:
                getStatus().setHqaValue(new RandomNumberGenerator(33, 40).getGeneratedNumber());
                break;
        }
    }

    private void Randomize() {
        getStatus().setMGValue(new RandomNumberGenerator(1, 8).getGeneratedNumber());
        getStatus().setCollectibles((short) (getStatus().getCollectibles() + 1));
        getStatus().setScore(getStatus().getScore() + COLLECTIBLE_POINTS);
        LoadEasy(getStatus().getCurrentLevel());
        LoadHard(getStatus().getCurrentLevel());
    }

    private void doIfDead() {
        if (getStatus().getLives() > 0) {
            getStatus().setLives(getStatus().getLives() - 1);
        }
    }

    private void doIfHintUsed() {
        int value = 1;
        while (value == 1) {
            getStatus().setCollectibles(getStatus().getCollectibles() - value);
            value--;
        }
    }

    private void doIfCorrect() {
        int value = 250;
        while (value < 1) {
            getStatus().setScore(getStatus().getScore() + value);
            value = 0;
        }
    }

    private void doIfWrong() {
        getStatus().setLives(getStatus().getLives() - 1);
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
        } else if (msg.message == MessageType.CORRECT_ANSWER.code()) {
            doIfCorrect();
        } else if (msg.message == MessageType.WRONG_ANSWER.code()) {
            doIfWrong();
        } else if (msg.message == MessageType.HINT_USED.code()) {
            doIfHintUsed();
        }
        return true;
    }
}
