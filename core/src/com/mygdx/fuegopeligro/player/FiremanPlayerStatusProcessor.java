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
    private static final int CHECKPOINT_SCORE = 100;
    private static final int ADDITIONAL_SCORE = 250;
    private static final int MINUS_LIVES = 1;
    private static final int MINUS_HINTS = 1;

    public FiremanPlayerStatusProcessor(final CurrentPlayerStatus status) {
        super(status);
        MessageManager.getInstance().addListeners(this,
                MessageType.COLLECTED.code(), MessageType.DEAD.code(),
                MessageType.LOAD_NEXT_LEVEL.code(), MessageType.LOAD_NEW_GAME.code(),
                MessageType.CORRECT_ANSWER.code(), MessageType.WRONG_ANSWER.code(),
                MessageType.HINT_USED.code(), MessageType.COINS_COLLECTED.code());
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
        getStatus().setCollectibles(getStatus().getCollectibles() + MINUS_HINTS);
        getStatus().setScore(getStatus().getScore() + CHECKPOINT_SCORE);
        LoadEasy(getStatus().getCurrentLevel());
        LoadHard(getStatus().getCurrentLevel());
    }

    private void doIfDead() {
        final int currentLives = getStatus().getLives() - MINUS_LIVES;
        if (getStatus().getLives() > 0) {
            getStatus().setLives(currentLives);
        }
    }


    /*
     * Some scoring and hints/lives controls gets wonky and problematic
     * this will be fixed next time. Code will be left out for the meantime.
     */
    private void coinsCaptured() {
        // getStatus().setScore(getStatus().getScore() + COLLECTIBLE_POINTS);
    }

    private void doIfHintUsed() {
        /* final int currentHints = getStatus().getCollectibles() - MINUS_HINTS;
        getStatus().setCollectibles(currentHints); */
    }

    private void doIfCorrect() {
        /* final int currentScore = getStatus().getScore() + ADDITIONAL_SCORE;
        getStatus().setScore(getStatus().getScore() + currentScore); */
    }

    private void doIfWrong() {
        /* final int currentLives = getStatus().getLives() - MINUS_LIVES;
        if (getStatus().getLives() > 0) {
            getStatus().setLives(currentLives);
        } */
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
        if (msg.message == MessageType.COLLECTED.code() ||
                msg.message == MessageType.LOAD_NEW_GAME.code() ||
                msg.message == MessageType.LOAD_CURRENT_GAME.code()) {
            Randomize();
        } else if (msg.message == MessageType.DEAD.code()) {
            doIfDead();
        } else if (msg.message == MessageType.CORRECT_ANSWER.code()) {
            doIfCorrect();
        } else if (msg.message == MessageType.WRONG_ANSWER.code()) {
            doIfWrong();
        } else if (msg.message == MessageType.HINT_USED.code()) {
            doIfHintUsed();
        } else if (msg.message == MessageType.COINS_COLLECTED.code()) {
            coinsCaptured();
        }
        return true;
    }
}
