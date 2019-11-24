package com.mygdx.fuegopeligro;

import com.badlogic.gdx.Screen;

/**
 * Base class for every {@link Screen} in the game. Holds a reference to the main game instance.
 *
 * @author JDEsguerra
 */
public abstract class AbstractScreen implements Screen {
    protected final FuegoPeligro game;

    public AbstractScreen(final FuegoPeligro game) {
        this.game = game;
    }

    @Override
    public void hide() {
        dispose();
    }
}
