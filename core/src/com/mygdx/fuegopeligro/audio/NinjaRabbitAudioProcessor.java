package com.mygdx.fuegopeligro.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.mygdx.fuegopeligro.AppPreferences;
import com.mygdx.fuegopeligro.Assets;
import com.mygdx.fuegopeligro.ai.fsm.NinjaRabbitState;
import com.mygdx.fuegopeligro.ai.msg.MessageType;
import com.mygdx.fuegopeligro.entity.Entity;
import com.mygdx.fuegopeligro.entity.NinjaRabbit;

/**
 * Handles audios played by actions taken by a {@link NinjaRabbit} entity.
 *
 * @author JDEsguerra
 */
public class NinjaRabbitAudioProcessor extends AppPreferences implements AudioProcessor, Telegraph {
    private static final int MAX_JUMP_TIMEOUT = 30;

    private final AssetManager assets;
    private int jumpTimeout;
    private long jumpFxId;
    private long lostLife;

    public NinjaRabbitAudioProcessor(final AssetManager assets) {
        this.assets = assets;
        MessageManager.getInstance().addListener(this, MessageType.DEAD.code());
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mygdx.fuegopeligro.audio.AudioProcessor#update(com.mygdx.fuegopeligro.entity.Entity)
     */
    @Override
    public void update(final Entity character) {
        if (character.isInState(NinjaRabbitState.JUMP) && character.getBody().getLinearVelocity().y > 0) {
            if (jumpTimeout <= 0) {
                Sound jumpFx = assets.get(Assets.JUMP_FX);
                jumpFx.stop(jumpFxId);
                jumpFxId = jumpFx.play();
                float SOUND_VOLUME = this.getSoundVolume();
                jumpFx.setVolume(jumpFxId, SOUND_VOLUME);
                jumpTimeout = MAX_JUMP_TIMEOUT;
            } else {
                jumpTimeout -= Gdx.graphics.getDeltaTime();
            }
        } else {
            jumpTimeout = 0;
        }
    }

    @Override
    public boolean handleMessage(final Telegram msg) {
        Sound minusLife = assets.get(Assets.LIFE_LOST_FX);
        float SOUND_VOLUME = this.getSoundVolume();
        lostLife = minusLife.play();
        minusLife.setVolume(lostLife, SOUND_VOLUME);
        return true;
    }

    @Override
    public void dispose() {

    }
}
