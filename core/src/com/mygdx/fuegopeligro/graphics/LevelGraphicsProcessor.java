package com.mygdx.fuegopeligro.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.fuegopeligro.FuegoPeligro;
import com.mygdx.fuegopeligro.GameOverOverlay;
import com.mygdx.fuegopeligro.LevelEndOverlay;
import com.mygdx.fuegopeligro.ai.msg.MessageType;
import com.mygdx.fuegopeligro.entity.Entity;
import com.mygdx.fuegopeligro.entity.EntityFactory;
import com.mygdx.fuegopeligro.map.LevelRenderer;
import com.mygdx.fuegopeligro.minigames.FourPicsOneWord;
import com.mygdx.fuegopeligro.minigames.LetterPuzzle;
import com.mygdx.fuegopeligro.minigames.MultipleChoice;
import com.mygdx.fuegopeligro.minigames.Wordscapes;
import com.mygdx.fuegopeligro.player.CurrentPlayerStatus;

/**
 * @author JDEsguerra
 */
public class LevelGraphicsProcessor implements GraphicsProcessor, Telegraph {
    private final LevelRenderer mapRenderer;
    private final GameOverOverlay gameOver;
    private final LevelEndOverlay levelEnd;
    private final MultipleChoice multipleChoice;
    private final FourPicsOneWord fourPicsOneWord;
    private final LetterPuzzle letterPuzzle;
    private final Wordscapes wordscapes;

    private boolean renderGameOver;
    private boolean renderLevelEnd;
    private boolean minicamSelection;
    private boolean minigameEnd;
    private final CurrentPlayerStatus status;

    public LevelGraphicsProcessor(final AssetManager assets, final Batch batch, final LevelRenderer mapRenderer, final FuegoPeligro game) {
        status = new CurrentPlayerStatus();
        gameOver = new GameOverOverlay(batch, assets, game);
        levelEnd = new LevelEndOverlay(batch, assets, game);
        multipleChoice = new MultipleChoice(batch, assets, game, this);
        fourPicsOneWord = new FourPicsOneWord(batch, assets, game, this);
        letterPuzzle = new LetterPuzzle(batch, assets, game, this);
        wordscapes = new Wordscapes(batch, assets, game, this);
        this.mapRenderer = mapRenderer;
        MessageManager.getInstance().addListeners(this, MessageType.GAME_OVER.code());
        MessageManager.getInstance().addListeners(this, MessageType.FINISH_LEVEL.code());
        MessageManager.getInstance().addListeners(this, MessageType.COLLECTED.code());
        MessageManager.getInstance().addListeners(this, MessageType.END_MINIGAME_OVERLAY.code());
    }

    @Override
    public void update(final Entity character, final Camera camera) {
        mapRenderer.render((OrthographicCamera) camera);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mygdx.fuegopeligro.graphics.GraphicsProcessor#draw(com.mygdx.fuegopeligro.entity.Entity,
     * com.badlogic.gdx.graphics.g2d.Batch)
     */
    @Override
    public void draw(final Entity entity, final Batch batch) {
        mapRenderer.update();

        if (renderGameOver) {
            gameOver.render(Gdx.graphics.getDeltaTime());
        } else if (renderLevelEnd) {
            levelEnd.render(Gdx.graphics.getDeltaTime());
        } else if (minicamSelection) {
            short worldValue = status.getCurrentWorld();
            //short levelValue = status.getCurrentLevel();
            short mgValue = status.getMGValue();

            if (worldValue == 1) {
                //short easyValue = status.getEqaValue();
                switch (mgValue) {
                    case 1:
                        multipleChoice.render(Gdx.graphics.getDeltaTime());
                        if (minigameEnd) {
                            multipleChoice.dispose();
                        }
                        break;
                    case 2:
                        wordscapes.render(Gdx.graphics.getDeltaTime());
                        if (minigameEnd) {
                            wordscapes.dispose();
                        }
                        break;
                    case 3:
                        letterPuzzle.render(Gdx.graphics.getDeltaTime());
                        if (minigameEnd) {
                            letterPuzzle.dispose();
                        }
                        break;
                    case 4:
                        fourPicsOneWord.render(Gdx.graphics.getDeltaTime());
                        if (minigameEnd) {
                            fourPicsOneWord.dispose();
                        }
                        break;
                }
                new EntityFactory();
            } else if (worldValue == 2) {
                //short hardValue = status.getHqaValue();
                switch (mgValue) {
                    case 1:
                        multipleChoice.render(Gdx.graphics.getDeltaTime());
                        if (minigameEnd) {
                            multipleChoice.dispose();
                        }
                        break;
                    case 2:
                        wordscapes.render(Gdx.graphics.getDeltaTime());
                        if (minigameEnd) {
                            wordscapes.dispose();
                        }
                        break;
                    case 3:
                        letterPuzzle.render(Gdx.graphics.getDeltaTime());
                        if (minigameEnd) {
                            letterPuzzle.dispose();
                        }
                        break;
                    case 4:
                        fourPicsOneWord.render(Gdx.graphics.getDeltaTime());
                        if (minigameEnd) {
                            fourPicsOneWord.dispose();
                        }
                        break;
                }
                new EntityFactory();
            }
        }
    }

    @Override
    public boolean handleMessage(final Telegram msg) {
        renderGameOver = msg.message == MessageType.GAME_OVER.code();
        renderLevelEnd = msg.message == MessageType.FINISH_LEVEL.code();
        minicamSelection = msg.message == MessageType.COLLECTED.code();
        minigameEnd = msg.message == MessageType.END_MINIGAME_OVERLAY.code();
        return true;
    }

    @Override
    public void resize(final int width, final int height) {
        gameOver.resize(width, height);
        levelEnd.resize(width, height);
        multipleChoice.resize(width, height);
        wordscapes.resize(width, height);
        letterPuzzle.resize(width, height);
        fourPicsOneWord.resize(width, height);
    }

    @Override
    public void dispose() {
        gameOver.dispose();
        levelEnd.dispose();
        multipleChoice.dispose();
        wordscapes.dispose();
        letterPuzzle.dispose();
        fourPicsOneWord.dispose();
    }
}
