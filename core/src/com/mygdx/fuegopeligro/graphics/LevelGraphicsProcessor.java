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
import com.mygdx.fuegopeligro.entity.Fireman;
import com.mygdx.fuegopeligro.graphics.minigames.FourPicsOneWord;
import com.mygdx.fuegopeligro.graphics.minigames.LetterPuzzle;
import com.mygdx.fuegopeligro.graphics.minigames.MultipleChoice;
import com.mygdx.fuegopeligro.graphics.minigames.Wordscapes;
import com.mygdx.fuegopeligro.input.FiremanInputProcessor;
import com.mygdx.fuegopeligro.map.LevelRenderer;
import com.mygdx.fuegopeligro.player.CurrentPlayerStatus;

/**
 * @author JDEsguerra
 */
public class LevelGraphicsProcessor implements GraphicsProcessor, Telegraph {
    private final LevelRenderer mapRenderer;
    private final GameOverOverlay gameOver;
    private final LevelEndOverlay levelEnd;
    private MultipleChoice multipleChoice;
    private FourPicsOneWord fourPicsOneWord;
    private LetterPuzzle letterPuzzle;
    private Wordscapes wordscapes;

    private boolean renderGameOver;
    private boolean renderLevelEnd;
    private boolean minicamSelection;
    private final CurrentPlayerStatus status;
    private final Fireman fireman2;

    public LevelGraphicsProcessor(final AssetManager assets, final LevelRenderer mapRenderer,
                                  final FuegoPeligro game, final Fireman fireman,
                                  final CurrentPlayerStatus player) {
        if (fireman == null) {
            throw new IllegalArgumentException("'character' cannot be null");
        }

        status = player;
        fireman2 = fireman;
        gameOver = new GameOverOverlay(game.getBatch(), assets, game);
        levelEnd = new LevelEndOverlay(game.getBatch(), assets, game);
        multipleChoice = new MultipleChoice(assets, game, player);
        fourPicsOneWord = new FourPicsOneWord(assets, game, player);
        letterPuzzle = new LetterPuzzle(assets, game, player);
        wordscapes = new Wordscapes(assets, game, player);
        this.mapRenderer = mapRenderer;
        MessageManager.getInstance().addListeners(this, MessageType.GAME_OVER.code());
        MessageManager.getInstance().addListeners(this, MessageType.FINISH_LEVEL.code());
        MessageManager.getInstance().addListeners(this, MessageType.COLLECTED.code());
    }

    @Override
    public void update(final Entity character, final Camera camera) {
        mapRenderer.render((OrthographicCamera) camera);
    }

    private void gameOver() {
        gameOver.render(Gdx.graphics.getDeltaTime());
    }

    private void levelEnd() {
        levelEnd.render(Gdx.graphics.getDeltaTime());
    }

    private void minigameSelection(final Fireman fireman) {
        if (status.getCurrentWorld() == 1 || status.getCurrentWorld() == 2) {
            //byte levelValue = status.getCurrentLevel();
            //byte easyValue = status.getEqaValue();
            //byte hardValue = status.getHqaValue();

            switch (status.getMGValue()) {
                case 1:
                    multipleChoice.render(Gdx.graphics.getDeltaTime());
                    Gdx.input.setInputProcessor(multipleChoice.stage);
                    if (multipleChoice.enterAnswer.isPressed()) {
                        Gdx.input.setInputProcessor(new FiremanInputProcessor(fireman));
                        multipleChoice.setVisible(false);
                    }
                    break;
                case 2:
                    wordscapes.render(Gdx.graphics.getDeltaTime());
                    Gdx.input.setInputProcessor(wordscapes.stage);
                    if (wordscapes.enterAnswer.isPressed()) {
                        Gdx.input.setInputProcessor(new FiremanInputProcessor(fireman));
                        wordscapes.setVisible(false);
                    }
                    break;
                case 3:
                    letterPuzzle.render(Gdx.graphics.getDeltaTime());
                    Gdx.input.setInputProcessor(letterPuzzle.stage);
                    if (letterPuzzle.enterAnswer.isPressed()) {
                        Gdx.input.setInputProcessor(new FiremanInputProcessor(fireman));
                        letterPuzzle.setVisible(false);
                    }
                    break;
                case 4:
                    fourPicsOneWord.render(Gdx.graphics.getDeltaTime());
                    Gdx.input.setInputProcessor(fourPicsOneWord.stage);
                    if (fourPicsOneWord.enterAnswer.isPressed()) {
                        Gdx.input.setInputProcessor(new FiremanInputProcessor(fireman));
                        fourPicsOneWord.setVisible(false);
                    }
                    break;
            }
        }
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
            gameOver();
        } else if (renderLevelEnd) {
            levelEnd();
        } else if (minicamSelection) {
            minigameSelection(fireman2);
        }
    }

    @Override
    public boolean handleMessage(final Telegram msg) {
        renderGameOver = msg.message == MessageType.GAME_OVER.code();
        renderLevelEnd = msg.message == MessageType.FINISH_LEVEL.code();
        minicamSelection = msg.message == MessageType.COLLECTED.code();
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
