package com.mygdx.fuegopeligro.audio;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.mygdx.fuegopeligro.AppPreferences;
import com.mygdx.fuegopeligro.Assets;
import com.mygdx.fuegopeligro.entity.Collectible;
import com.mygdx.fuegopeligro.entity.Entity;

/**
 * Plays a sound when a {@link Collectible} is collected by the player.
 *
 * @author JDEsguerra
 */
public class CoinsAudioProcessor extends AppPreferences implements AudioProcessor {
    private final Sound collected;

    public CoinsAudioProcessor(final AssetManager manager) {
        collected = manager.get(Assets.CRUNCH_FX);
    }

    @Override
    public void update(final Entity character) {
        if (((Collectible) character).isCollected()) {
            long capturedCarrot = collected.play();
            float SOUND_VOLUME = this.getSoundVolume();
            collected.setVolume(capturedCarrot, SOUND_VOLUME);
        }
    }

    @Override
    public void dispose() {

    }
}
