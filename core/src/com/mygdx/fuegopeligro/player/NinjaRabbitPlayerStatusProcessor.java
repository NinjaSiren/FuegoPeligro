/**
 *
 */
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
public class NinjaRabbitPlayerStatusProcessor extends PlayerStatusProcessor implements Telegraph {
    /**
     * Points earned by gathering a collectible.
     */
    private static final int COLLECTIBLE_POINTS = 200;

    public NinjaRabbitPlayerStatusProcessor(final CurrentPlayerStatus status) {
        super(status);
        MessageManager.getInstance().addListeners(this, MessageType.COLLECTED.code(), MessageType.DEAD.code());
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
            getStatus().setCollectibles((short) (getStatus().getCollectibles() + 1));
            getStatus().setScore(getStatus().getScore() + COLLECTIBLE_POINTS);
        } else if (msg.message == MessageType.DEAD.code()) {
            if (getStatus().getLives() > 0) {
                getStatus().setLives((short) (getStatus().getLives() - 1));
            }
            System.out.println("Dead received!");
        }
        return true;
    }

}
