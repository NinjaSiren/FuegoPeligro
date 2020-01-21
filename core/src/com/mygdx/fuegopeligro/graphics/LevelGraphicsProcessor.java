package com.mygdx.fuegopeligro.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.fuegopeligro.FuegoPeligro;
import com.mygdx.fuegopeligro.GameOverOverlay;
import com.mygdx.fuegopeligro.LevelEndOverlay;
import com.mygdx.fuegopeligro.QAReader;
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
    private final FuegoPeligro fuego;
    private final Entity entity;

    public LevelGraphicsProcessor(final AssetManager assets, final LevelRenderer mapRenderer,
                                  final FuegoPeligro game, final Fireman fireman,
                                  final CurrentPlayerStatus player) {
        if (fireman == null) {
            throw new IllegalArgumentException("'character' cannot be null");
        }
        entity = fireman;

        status = player;
        fireman2 = fireman;
        fuego = game;
        gameOver = new GameOverOverlay(game.getBatch(), assets, game);
        levelEnd = new LevelEndOverlay(game.getBatch(), assets, game);
        multipleChoice = new MultipleChoice(assets, game, player);
        letterPuzzle = new LetterPuzzle(assets, game, player);
        wordscapes = new Wordscapes(assets, game, player);
        fourPicsOneWord = new FourPicsOneWord(assets, game, player);
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
        byte value;
        String fileName;
        QAReader qaReader;
        Texture texture_1, texture_2, texture_3, texture_4;
        int i = 0;
        
        if (status.getCurrentWorld() == 1) {
            value = status.getEqaValue();
            fileName = "minigames/easyQA.csv";
            qaReader = new QAReader(fileName, (int)value);

            switch (status.getMGValue()) {
                case 1:
                    multipleChoice.questionText.setText(qaReader.getQaQuestion());
                    multipleChoice.answer1.setText(qaReader.getQaAnswerT());
                    multipleChoice.answer2.setText(qaReader.getQaAnswerF1());
                    multipleChoice.answer3.setText(qaReader.getQaAnswerF2());
                    multipleChoice.answer4.setText(qaReader.getQaAnswerF3());
                    multipleChoice.render(Gdx.graphics.getDeltaTime());
                    multipleChoice.setVisible(true);
                    Gdx.input.setInputProcessor(multipleChoice.stage);
                    if (multipleChoice.answer1.isPressed()) {
                        Gdx.input.setInputProcessor(new FiremanInputProcessor(fireman));
                        multipleChoice.setVisible(false);
                    } else if (multipleChoice.answer2.isPressed()) {
                        if (status.getLives() > 0) {
                            status.setLives((byte)(status.getLives() - 1));
                        } else {
                            MessageManager.getInstance().dispatchMessage(null,
                                    MessageType.DEAD.code(), entity);
                        }
                    } else if (multipleChoice.answer3.isPressed()) {
                        MessageManager.getInstance().dispatchMessage(null,
                                MessageType.DEAD.code(), entity);
                    } else if (multipleChoice.answer4.isPressed()) {
                        MessageManager.getInstance().dispatchMessage(null,
                                MessageType.DEAD.code(), entity);
                    } else if (multipleChoice.enterHints.isPressed()) {

                    }
                    break;

                case 2:
                    wordscapes.questionText.setText(qaReader.getQaQuestion());
                    wordscapes.answer1.setText(qaReader.getQaAnswerT());
                    wordscapes.answer2.setText(qaReader.getQaAnswerF1());
                    wordscapes.answer3.setText(qaReader.getQaAnswerF2());
                    wordscapes.answer4.setText(qaReader.getQaAnswerF3());
                    wordscapes.render(Gdx.graphics.getDeltaTime());
                    wordscapes.setVisible(true);
                    Gdx.input.setInputProcessor(wordscapes.stage);
                    if (wordscapes.answer1.isPressed()) {
                        Gdx.input.setInputProcessor(new FiremanInputProcessor(fireman));
                        wordscapes.setVisible(false);
                    }
                    break;

                case 3:
                    letterPuzzle.questionText.setText(qaReader.getQaQuestion());
                    letterPuzzle.answer1.setText(qaReader.getQaAnswerT());
                    letterPuzzle.answer2.setText(qaReader.getQaAnswerF1());
                    letterPuzzle.answer3.setText(qaReader.getQaAnswerF2());
                    letterPuzzle.answer4.setText(qaReader.getQaAnswerF3());
                    letterPuzzle.render(Gdx.graphics.getDeltaTime());
                    letterPuzzle.setVisible(true);
                    Gdx.input.setInputProcessor(letterPuzzle.stage);
                    if (letterPuzzle.answer1.isPressed()) {
                        Gdx.input.setInputProcessor(new FiremanInputProcessor(fireman));
                        letterPuzzle.setVisible(false);
                    }
                    break;

                case 4:
                    fileName = "minigame/easywords.csv";
                    qaReader = new QAReader(fileName, (int)value);

                    if (qaReader.getQaNumber() < 1) {
                        qaReader.setQaNumber(1);
                        texture_1 = new Texture(Gdx.files.internal("minigames/easypics/" +
                                qaReader.getQaNumber() + "." + 1 + ".jpg"));
                        texture_2 = new Texture(Gdx.files.internal("minigames/easypics/" +
                                qaReader.getQaNumber() + "." + 2 + ".jpg"));
                        texture_3 = new Texture(Gdx.files.internal("minigames/easypics/" +
                                qaReader.getQaNumber() + "." + 3 + ".jpg"));
                        texture_4 = new Texture(Gdx.files.internal("minigames/easypics/" +
                                qaReader.getQaNumber() + "." + 4 + ".jpg"));
                    } else {
                        texture_1 = new Texture(Gdx.files.internal("minigames/easypics/" +
                                qaReader.getQaNumber() + "." + 1 + ".jpg"));
                        texture_2 = new Texture(Gdx.files.internal("minigames/easypics/" +
                                qaReader.getQaNumber() + "." + 2 + ".jpg"));
                        texture_3 = new Texture(Gdx.files.internal("minigames/easypics/" +
                                qaReader.getQaNumber() + "." + 3 + ".jpg"));
                        texture_4 = new Texture(Gdx.files.internal("minigames/easypics/" +
                                qaReader.getQaNumber() + "." + 4 + ".jpg"));
                    }

                    TextureRegion myTextureRegion_1 = new TextureRegion(texture_1);
                    TextureRegion myTextureRegion_2 = new TextureRegion(texture_2);
                    TextureRegion myTextureRegion_3 = new TextureRegion(texture_3);
                    TextureRegion myTextureRegion_4 = new TextureRegion(texture_4);

                    TextureRegionDrawable myTexRegionDrawable_1 = new TextureRegionDrawable(myTextureRegion_1);
                    TextureRegionDrawable myTexRegionDrawable_2 = new TextureRegionDrawable(myTextureRegion_2);
                    TextureRegionDrawable myTexRegionDrawable_3 = new TextureRegionDrawable(myTextureRegion_3);
                    TextureRegionDrawable myTexRegionDrawable_4 = new TextureRegionDrawable(myTextureRegion_4);

                    fourPicsOneWord.answer1.setDrawable(myTexRegionDrawable_1);
                    fourPicsOneWord.answer2.setDrawable(myTexRegionDrawable_2);
                    fourPicsOneWord.answer3.setDrawable(myTexRegionDrawable_3);
                    fourPicsOneWord.answer4.setDrawable(myTexRegionDrawable_4);

                    fourPicsOneWord.render(Gdx.graphics.getDeltaTime());
                    fourPicsOneWord.setVisible(true);
                    Gdx.input.setInputProcessor(fourPicsOneWord.stage);
                    if (fourPicsOneWord.enterHints.isPressed()) {
                        Gdx.input.setInputProcessor(new FiremanInputProcessor(fireman));
                        fourPicsOneWord.setVisible(false);
                    }
                    break;
            }
        } else if (status.getCurrentWorld() == 2) {
            value = status.getHqaValue();
            fileName = "minigames/hardQA.csv";
            qaReader = new QAReader(fileName, (int)value);

            switch (status.getMGValue()) {
                case 1:
                    multipleChoice.questionText.setText(qaReader.getQaQuestion());
                    multipleChoice.answer1.setText(qaReader.getQaAnswerT());
                    multipleChoice.answer2.setText(qaReader.getQaAnswerF1());
                    multipleChoice.answer3.setText(qaReader.getQaAnswerF2());
                    multipleChoice.answer4.setText(qaReader.getQaAnswerF3());
                    multipleChoice.render(Gdx.graphics.getDeltaTime());
                    multipleChoice.setVisible(true);
                    Gdx.input.setInputProcessor(multipleChoice.stage);
                    if (multipleChoice.answer1.isPressed()) {
                        Gdx.input.setInputProcessor(new FiremanInputProcessor(fireman));
                        multipleChoice.setVisible(false);
                    } else if (multipleChoice.answer2.isPressed()) {
                        if (status.getLives() > 0) {
                            status.setLives((byte)(status.getLives() - 1));
                        } else {
                            MessageManager.getInstance().dispatchMessage(null,
                                    MessageType.DEAD.code(), entity);
                        }
                    } else if (multipleChoice.answer3.isPressed()) {
                        MessageManager.getInstance().dispatchMessage(null,
                                MessageType.DEAD.code(), entity);
                    } else if (multipleChoice.answer4.isPressed()) {
                        MessageManager.getInstance().dispatchMessage(null,
                                MessageType.DEAD.code(), entity);
                    } else if (multipleChoice.enterHints.isPressed()) {

                    }
                    break;

                case 2:
                    wordscapes.questionText.setText(qaReader.getQaQuestion());
                    wordscapes.answer1.setText(qaReader.getQaAnswerT());
                    wordscapes.answer2.setText(qaReader.getQaAnswerF1());
                    wordscapes.answer3.setText(qaReader.getQaAnswerF2());
                    wordscapes.answer4.setText(qaReader.getQaAnswerF3());
                    wordscapes.render(Gdx.graphics.getDeltaTime());
                    wordscapes.setVisible(true);
                    Gdx.input.setInputProcessor(wordscapes.stage);
                    if (wordscapes.answer1.isPressed()) {
                        Gdx.input.setInputProcessor(new FiremanInputProcessor(fireman));
                        wordscapes.setVisible(false);
                    }
                    break;

                case 3:
                    letterPuzzle.questionText.setText(qaReader.getQaQuestion());
                    letterPuzzle.answer1.setText(qaReader.getQaAnswerT());
                    letterPuzzle.answer2.setText(qaReader.getQaAnswerF1());
                    letterPuzzle.answer3.setText(qaReader.getQaAnswerF2());
                    letterPuzzle.answer4.setText(qaReader.getQaAnswerF3());
                    letterPuzzle.render(Gdx.graphics.getDeltaTime());
                    letterPuzzle.setVisible(true);
                    Gdx.input.setInputProcessor(letterPuzzle.stage);
                    if (letterPuzzle.answer1.isPressed()) {
                        Gdx.input.setInputProcessor(new FiremanInputProcessor(fireman));
                        letterPuzzle.setVisible(false);
                    }
                    break;

                case 4:
                    if (qaReader.getQaNumber() < 1) {
                        qaReader.setQaNumber(1);
                        texture_1 = new Texture(Gdx.files.internal("minigames/hardpics/" +
                                qaReader.getQaNumber() + "." + 1 + ".jpg"));
                        texture_2 = new Texture(Gdx.files.internal("minigames/hardpics/" +
                                qaReader.getQaNumber() + "." + 2 + ".jpg"));
                        texture_3 = new Texture(Gdx.files.internal("minigames/hardpics/" +
                                qaReader.getQaNumber() + "." + 3 + ".jpg"));
                        texture_4 = new Texture(Gdx.files.internal("minigames/hardpics/" +
                                qaReader.getQaNumber() + "." + 4 + ".jpg"));
                    } else {
                        texture_1 = new Texture(Gdx.files.internal("minigames/hardpics/" +
                                qaReader.getQaNumber() + "." + 1 + ".jpg"));
                        texture_2 = new Texture(Gdx.files.internal("minigames/hardpics/" +
                                qaReader.getQaNumber() + "." + 2 + ".jpg"));
                        texture_3 = new Texture(Gdx.files.internal("minigames/hardpics/" +
                                qaReader.getQaNumber() + "." + 3 + ".jpg"));
                        texture_4 = new Texture(Gdx.files.internal("minigames/hardpics/" +
                                qaReader.getQaNumber() + "." + 4 + ".jpg"));
                    }

                    TextureRegion myTextureRegion_1 = new TextureRegion(texture_1);
                    TextureRegion myTextureRegion_2 = new TextureRegion(texture_2);
                    TextureRegion myTextureRegion_3 = new TextureRegion(texture_3);
                    TextureRegion myTextureRegion_4 = new TextureRegion(texture_4);

                    TextureRegionDrawable myTexRegionDrawable_1 = new TextureRegionDrawable(myTextureRegion_1);
                    TextureRegionDrawable myTexRegionDrawable_2 = new TextureRegionDrawable(myTextureRegion_2);
                    TextureRegionDrawable myTexRegionDrawable_3 = new TextureRegionDrawable(myTextureRegion_3);
                    TextureRegionDrawable myTexRegionDrawable_4 = new TextureRegionDrawable(myTextureRegion_4);

                    fourPicsOneWord.answer1.setDrawable(myTexRegionDrawable_1);
                    fourPicsOneWord.answer2.setDrawable(myTexRegionDrawable_2);
                    fourPicsOneWord.answer3.setDrawable(myTexRegionDrawable_3);
                    fourPicsOneWord.answer4.setDrawable(myTexRegionDrawable_4);

                    fourPicsOneWord.render(Gdx.graphics.getDeltaTime());
                    fourPicsOneWord.setVisible(true);
                    Gdx.input.setInputProcessor(fourPicsOneWord.stage);
                    if (fourPicsOneWord.enterHints.isPressed()) {
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
        /*multipleChoice.resize(width, height);
        wordscapes.resize(width, height);
        letterPuzzle.resize(width, height);
        fourPicsOneWord.resize(width, height);*/
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
