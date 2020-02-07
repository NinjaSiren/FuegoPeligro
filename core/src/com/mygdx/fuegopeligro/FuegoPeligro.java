package com.mygdx.fuegopeligro;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.fuegopeligro.ai.msg.MessageType;
import com.mygdx.fuegopeligro.player.CurrentPlayerStatus;
import com.mygdx.fuegopeligro.player.PlayerStatus;

/**
 * @author JDEsguerra
 */
public class FuegoPeligro extends Game implements Telegraph {
    public static final float PPM = 92.0f;
    private static final String GAME_TITLE = "Fuego Peligro Beta v0.7 test [fps: %s]";
    /**
     * The state of the current player. Placed here for convenient use and access.
     */
    private final CurrentPlayerStatus status;
    public Batch batch;
    private AssetManager assetsManager;
    private AppPreferences preferences;

    public FuegoPeligro() {
        status = new CurrentPlayerStatus();
    }

    @Override
    public void create() {
        preferences = new AppPreferences();
        batch = new SpriteBatch();
        assetsManager = new AssetManager();
        assetsManager.load(Assets.NINJA_RABBIT_ATLAS);
        assetsManager.load(Assets.NINJA_RABBIT_THEME);
        assetsManager.load(Assets.JUMP_FX);
        assetsManager.load(Assets.LIFE_LOST_FX);
        assetsManager.load(Assets.GAME_OVER_FX);
        assetsManager.load(Assets.CRUNCH_FX);
        assetsManager.load(Assets.VICTORY_FX);
        assetsManager.load(Assets.HUD_FONT);
        assetsManager.load(Assets.DEFAULT_BACKGROUND);
        assetsManager.load(Assets.GRASSLAND_BACKGROUND);
        assetsManager.load(Assets.SWORD);
        assetsManager.load(Assets.GAME_UI_SKIN);
        assetsManager.load(Assets.MENU_BG);
        assetsManager.load(Assets.SPLASH_IMAGE);
        assetsManager.load(Assets.SPLASH_IMAGE_2);
        assetsManager.load(Assets.MENU_BACKGROUND);
        assetsManager.finishLoading();
        setScreen(new SplashScreen_1(FuegoPeligro.this));
        final long splash_start_time = System.currentTimeMillis();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        long splash_elapsed_time = System.currentTimeMillis() - splash_start_time;
                        if(splash_elapsed_time < 3000L) {
                            Timer.schedule(new Timer.Task() {
                                @Override
                                public void run() {
                                    setScreen(new SplashScreen_2(FuegoPeligro.this));
                                    if(splash_elapsed_time < 2000L) {
                                        Timer.schedule(new Timer.Task() {
                                            @Override
                                            public void run() {
                                                setScreen(new TitleScreen(FuegoPeligro.this));
                                            }
                                        }, (float) (3000L - splash_elapsed_time) / 2000f);
                                    } else {
                                        FuegoPeligro.this.setScreen(new TitleScreen(FuegoPeligro.this));
                                    }
                                }
                            }, (float) (3000L - splash_elapsed_time) / 2000f);
                        }
                    }
                });
            }
        }).start();
    }

    AppPreferences getPreferences() {
        return this.preferences;
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.graphics.setTitle(String.format(GAME_TITLE, Gdx.graphics.getFramesPerSecond()));
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        assetsManager.dispose();
        batch.dispose();
        getScreen().dispose();
    }

    /**
     * Sets the state of the game to what it was when it first began. Player status is reset and the
     * {@link TitleScreen} is shown.
     */
    private void reset() {
        getPlayerStatus().reset();
        addListeners();
        setScreen(new TitleScreen(this));
    }

    /**
     * Disposes of the current {@link Screen} and replaces it with a new {@link LevelStartScreen}.
     */
    private void resetLevel() {
        addListeners();
        getPlayerStatus().setLevel(1);
        getPlayerStatus().setWorld(1);
    }

    /**
     * Disposes of the current {@link Screen} and replaces it with a new {@link LevelStartScreen}.
     */
    void setLevel(int worldNumber, int levelNumber) {
        addListeners();
        setScreen(new LevelStartScreen(this, worldNumber, levelNumber));
    }

    /**
     * Disposes of the current {@link Screen} and replaces it with a new {@link LevelStartScreen}.
     */
    private void nextLevel() {
        addListeners();
        getPlayerStatus().levelEndOver();
        int levelNumber = getPlayerStatus().getLevel();
        int worldNumber = getPlayerStatus().getWorld();
        setScreen(new LevelStartScreen(this, worldNumber, levelNumber));
    }

    /**
     * Disposes of the current {@link Screen} and replaces it with a new {@link LevelStartScreen}.
     */
    private void newGame() {
        addListeners();
        getPlayerStatus().reset();
        setScreen(new LevelStartScreen(this, 1, 1));
    }

    /**
     * Sets the state of the game to what it was when it first began. Player status is reset and the
     * {@link TitleScreen} is shown.
     */
    private void backToMenu() {
        addListeners();
        setScreen(new TitleScreen(this));
    }

    private void loadGame() {
        addListeners();
        getPlayerStatus().resetGameOver();
        int levelNumber = getPlayerStatus().getCurrentLevel();
        int worldNumber = getPlayerStatus().getCurrentWorld();
        setScreen(new LevelStartScreen(this, worldNumber, levelNumber));
    }

    private void preferenceS() {
        addListeners();
        setScreen(new PreferencesScreen(this));
    }

    private void levelSelection() {
        addListeners();
        setScreen(new LevelSelectionScreen(this));
    }

    private void respawnAtCheckpoint() {
        addListeners();
    }

    /**
     * Clears all remaining messages and listeners in the {@link MessageManager} instance and
     * re-adds the ones from related to this {@link Telegraph}.
     */
    private void addListeners() {
        MessageManager manager = MessageManager.getInstance();
        manager.clear();
        manager.addListeners(this, MessageType.RESET_PLAYER_STATUS.code(),
                MessageType.RESET_CURRENT_LEVEL.code(), MessageType.LOAD_NEXT_LEVEL.code(),
                MessageType.LOAD_NEW_GAME.code(), MessageType.BACK_TO_MENU.code(),
                MessageType.SET_LEVEL.code());
    }

    AssetManager getAssetsManager() {
        return assetsManager;
    }

    public Batch getBatch() { return batch; }

    /**
     * Provides a way of polling the latest {@link PlayerStatus}.
     *
     * @return The current status of the player.
     */
    CurrentPlayerStatus getPlayerStatus() {
        return status;
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        if (msg.message == MessageType.RESET_PLAYER_STATUS.code()) {
            reset();
        } else if (msg.message == MessageType.RESET_CURRENT_LEVEL.code()) {
            loadGame();
        } else if (msg.message == MessageType.LOAD_NEXT_LEVEL.code()) {
            nextLevel();
        } else if (msg.message == MessageType.LOAD_NEW_GAME.code()) {
            newGame();
        } else if (msg.message == MessageType.LOAD_CURRENT_GAME.code()) {
            loadGame();
        } else if (msg.message == MessageType.BACK_TO_MENU.code()) {
            backToMenu();
        } else if (msg.message == MessageType.PREFERENCES_SCREEN.code()) {
            preferenceS();
        } else if (msg.message == MessageType.LEVEL_SELECTION.code()) {
            levelSelection();
        }
        return false;
    }
}
