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
import com.mygdx.fuegopeligro.ai.msg.MessageType;
import com.mygdx.fuegopeligro.entity.Entity;
import com.mygdx.fuegopeligro.entity.Fireman;
import com.mygdx.fuegopeligro.graphics.minigames.FourPicsOneWord;
import com.mygdx.fuegopeligro.graphics.minigames.FourPicsOneWord2;
import com.mygdx.fuegopeligro.graphics.minigames.LetterPuzzle;
import com.mygdx.fuegopeligro.graphics.minigames.LetterPuzzle2;
import com.mygdx.fuegopeligro.graphics.minigames.MultipleChoice;
import com.mygdx.fuegopeligro.graphics.minigames.MultipleChoice2;
import com.mygdx.fuegopeligro.graphics.minigames.Wordscapes;
import com.mygdx.fuegopeligro.graphics.minigames.Wordscapes2;
import com.mygdx.fuegopeligro.input.FiremanInputProcessor;
import com.mygdx.fuegopeligro.map.LevelRenderer;
import com.mygdx.fuegopeligro.player.CurrentPlayerStatus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author JDEsguerra
 */
public class LevelGraphicsProcessor implements GraphicsProcessor, Telegraph {
    private final LevelRenderer mapRenderer;
    private final GameOverOverlay gameOver;
    private final LevelEndOverlay levelEnd;
    private MultipleChoice multipleChoice;
    private MultipleChoice2 multipleChoice2;
    private FourPicsOneWord fourPicsOneWord;
    private FourPicsOneWord2 fourPicsOneWord2;
    private LetterPuzzle letterPuzzle;
    private LetterPuzzle2 letterPuzzle2;
    private Wordscapes wordscapes;
    private Wordscapes2 wordscapes2;

    private boolean renderGameOver;
    private boolean renderLevelEnd;
    private boolean minicamSelection;
    private final CurrentPlayerStatus status;
    private final Fireman fireman2;
    private final FuegoPeligro fargo;
    private final AssetManager assetManager;
    private List<String> line;

    public LevelGraphicsProcessor(final AssetManager assets, final LevelRenderer mapRenderer,
                                  final FuegoPeligro game, final Fireman fireman,
                                  final CurrentPlayerStatus player, final Batch batch) {
        if (fireman == null) {
            throw new IllegalArgumentException("'character' cannot be null");
        }

        assetManager = assets;
        status = player;
        fireman2 = fireman;
        fargo = game;

        gameOver = new GameOverOverlay(batch, assets, game);
        levelEnd = new LevelEndOverlay(batch, assets, game);
        multipleChoice = new MultipleChoice(assets, batch);
        letterPuzzle = new LetterPuzzle(assets, batch);
        wordscapes = new Wordscapes(assets, batch);
        fourPicsOneWord = new FourPicsOneWord(assets, batch);
        multipleChoice2 = new MultipleChoice2(assets, batch);
        letterPuzzle2 = new LetterPuzzle2(assets, batch);
        wordscapes2 = new Wordscapes2(assets, batch);
        fourPicsOneWord2 = new FourPicsOneWord2(assets, batch);

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

    private void minicamSelection(final Fireman fireman, final FuegoPeligro game, final AssetManager assets,
                                  final CurrentPlayerStatus status) {
        final byte lives = status.getLives();

        if (status.getCurrentWorld() == 1) {
            final String easyQA = "minigames/easyQA.csv";
            final String easyWords = "minigames/easywords.csv";
            final String easyDir = "minigames/easypics/";

            switch (status.getMGValue()) {
                case 1:
                    QAReader(easyQA, status.getEqaValue());
                    multiChoice2(fireman, game, assets, lives, status);
                    break;
                case 2:
                    QAReader(easyQA, status.getEqaValue());
                    multiChoice(fireman, game, assets, lives, status);
                    break;
                case 3:
                    QAReader(easyQA, status.getEqaValue());
                    wordScales2(fireman, game, assets, lives, status);
                    break;
                case 4:
                    QAReader(easyQA, status.getEqaValue());
                    wordScales(fireman, game, assets, lives, status);
                    break;
                case 5:
                    QAReader(easyWords, status.getEqaValue());
                    letterPutz2(fireman, game, assets, lives, status);
                case 6:
                    QAReader(easyWords, status.getEqaValue());
                    letterPutz(fireman, game, assets, lives, status);
                    break;
                case 7:
                    QAReader(easyWords, status.getEqaValue());
                    fPicsOWord2(fireman, game, assets, lives, easyDir, status);
                case 8:
                    QAReader(easyWords, status.getEqaValue());
                    fPicsOWord(fireman, game, assets, lives, easyDir, status);
                    break;
            }
        } else if (status.getCurrentWorld() == 2) {
            final String hardQA = "minigames/hardQA.csv";
            final String hardWords = "minigames/hardwords.csv";
            final String hardDir = "minigames/hardpics/";

            switch (status.getMGValue()) {
                case 1:
                    QAReader(hardQA, status.getEqaValue());
                    multiChoice2(fireman, game, assets, lives, status);
                    break;
                case 2:
                    QAReader(hardQA, status.getEqaValue());
                    multiChoice(fireman, game, assets, lives, status);
                    break;
                case 3:
                    QAReader(hardQA, status.getEqaValue());
                    wordScales2(fireman, game, assets, lives, status);
                    break;
                case 4:
                    QAReader(hardQA, status.getEqaValue());
                    wordScales(fireman, game, assets, lives, status);
                    break;
                case 5:
                    QAReader(hardWords, status.getEqaValue());
                    letterPutz2(fireman, game, assets, lives, status);
                case 6:
                    QAReader(hardWords, status.getEqaValue());
                    letterPutz(fireman, game, assets, lives, status);
                    break;
                case 7:
                    QAReader(hardWords, status.getEqaValue());
                    fPicsOWord2(fireman, game, assets, lives, hardDir, status);
                case 8:
                    QAReader(hardWords, status.getEqaValue());
                    fPicsOWord(fireman, game, assets, lives, hardDir, status);
                    break;
            }
        }
    }

    private void multiChoice2(final Fireman fireman, final FuegoPeligro game, final AssetManager asset,
                             final byte lives, final CurrentPlayerStatus status) {
        multipleChoice2.questionText.setText(line.get(6));
        multipleChoice2.answer1.setText(line.get(1));
        multipleChoice2.answer2.setText(line.get(2));
        multipleChoice2.answer3.setText(line.get(3));
        multipleChoice2.answer4.setText(line.get(4));

        multipleChoice2.render(Gdx.graphics.getDeltaTime());
        multipleChoice2.table.setVisible(true);
        Gdx.input.setInputProcessor(multipleChoice2.stage);

        if (multipleChoice2.answer1.isPressed()) {
            Gdx.input.setInputProcessor(new FiremanInputProcessor(fireman, game.batch, asset).im);
            multipleChoice2.table.setVisible(false);
        }

        if (multipleChoice2.answer2.isPressed()) { status.setLives((byte)(lives - 1)); }

        if (multipleChoice2.answer3.isPressed()) { status.setLives((byte)(lives - 1)); }

        if (multipleChoice2.answer4.isPressed()) { status.setLives((byte)(lives - 1)); }

        if (multipleChoice2.enterHints.isPressed()) {
        }
    }
    
    private void multiChoice(final Fireman fireman, final FuegoPeligro game, final AssetManager asset,
                             final byte lives, final CurrentPlayerStatus status) {
        multipleChoice.questionText.setText(line.get(6));
        multipleChoice.answer1.setText(line.get(1));
        multipleChoice.answer2.setText(line.get(2));
        multipleChoice.answer3.setText(line.get(3));
        multipleChoice.answer4.setText(line.get(4));

        multipleChoice.render(Gdx.graphics.getDeltaTime());
        multipleChoice.table.setVisible(true);
        Gdx.input.setInputProcessor(multipleChoice.stage);

        if (multipleChoice.answer1.isPressed()) {
            Gdx.input.setInputProcessor(new FiremanInputProcessor(fireman, game.batch, asset).im);
            multipleChoice.table.setVisible(false);
        }

        if (multipleChoice.answer2.isPressed()) { status.setLives((byte)(lives - 1)); }

        if (multipleChoice.answer3.isPressed()) { status.setLives((byte)(lives - 1)); }

        if (multipleChoice.answer4.isPressed()) { status.setLives((byte)(lives - 1)); }

        if (multipleChoice.enterHints.isPressed()) {
        }
    }

    private void wordScales2(final Fireman fireman, final FuegoPeligro game, final AssetManager asset,
                            final byte lives, final CurrentPlayerStatus status) {
        wordscapes2.questionText.setText(line.get(6));
        wordscapes2.answer1.setText(line.get(1));
        wordscapes2.answer2.setText(line.get(2));
        wordscapes2.answer3.setText(line.get(3));
        wordscapes2.answer4.setText(line.get(4));

        wordscapes2.render(Gdx.graphics.getDeltaTime());
        wordscapes2.table.setVisible(true);
        Gdx.input.setInputProcessor(wordscapes2.stage);

        if (wordscapes2.answer1.isPressed()) {
            Gdx.input.setInputProcessor(new FiremanInputProcessor(fireman, game.batch, asset).im);
            wordscapes2.table.setVisible(false);
        }

        if (wordscapes2.answer2.isPressed()) { status.setLives((byte)(lives - 1)); }

        if (wordscapes2.answer3.isPressed()) { status.setLives((byte)(lives - 1)); }

        if (wordscapes2.answer4.isPressed()) { status.setLives((byte)(lives - 1)); }

        if (wordscapes2.enterHints.isPressed()) {
        }
    }
    
    private void wordScales(final Fireman fireman, final FuegoPeligro game, final AssetManager asset,
                            final byte lives, final CurrentPlayerStatus status) {
        wordscapes.questionText.setText(line.get(6));
        wordscapes.answer1.setText(line.get(1));
        wordscapes.answer2.setText(line.get(2));
        wordscapes.answer3.setText(line.get(3));
        wordscapes.answer4.setText(line.get(4));

        wordscapes.render(Gdx.graphics.getDeltaTime());
        wordscapes.table.setVisible(true);
        Gdx.input.setInputProcessor(wordscapes.stage);

        if (wordscapes.answer1.isPressed()) {
            Gdx.input.setInputProcessor(new FiremanInputProcessor(fireman, game.batch, asset).im);
            wordscapes.table.setVisible(false);
        }

        if (wordscapes.answer2.isPressed()) { status.setLives((byte)(lives - 1)); }

        if (wordscapes.answer3.isPressed()) { status.setLives((byte)(lives - 1)); }

        if (wordscapes.answer4.isPressed()) { status.setLives((byte)(lives - 1)); }

        if (wordscapes.enterHints.isPressed()) {
        }
    }

    private void letterPutz2(final Fireman fireman, final FuegoPeligro game, final AssetManager asset,
                            final byte lives, final CurrentPlayerStatus status) {
        letterPuzzle2.questionText.setText(line.get(1).substring(1));
        letterPuzzle2.answer1.setText(line.get(4));
        letterPuzzle2.answer2.setText(line.get(3));
        letterPuzzle2.answer3.setText(line.get(2));
        letterPuzzle2.answer4.setText(line.get(1));

        letterPuzzle2.render(Gdx.graphics.getDeltaTime());
        letterPuzzle2.table.setVisible(true);
        Gdx.input.setInputProcessor(letterPuzzle2.stage);

        if (letterPuzzle2.answer1.isPressed()) { status.setLives((byte)(lives - 1)); }

        if (letterPuzzle2.answer2.isPressed()) { status.setLives((byte)(lives - 1)); }

        if (letterPuzzle2.answer3.isPressed()) { status.setLives((byte)(lives - 1)); }

        if (letterPuzzle2.answer4.isPressed()) {
            Gdx.input.setInputProcessor(new FiremanInputProcessor(fireman, game.batch, asset).im);
            letterPuzzle2.table.setVisible(false);
        }

        if (letterPuzzle2.enterHints.isPressed()) {
        }
    }
    
    private void letterPutz(final Fireman fireman, final FuegoPeligro game, final AssetManager asset,
                            final byte lives, final CurrentPlayerStatus status) {
        letterPuzzle.questionText.setText(line.get(1).substring(2));
        letterPuzzle.answer1.setText(line.get(4));
        letterPuzzle.answer2.setText(line.get(3));
        letterPuzzle.answer3.setText(line.get(2));
        letterPuzzle.answer4.setText(line.get(1));

        letterPuzzle.render(Gdx.graphics.getDeltaTime());
        letterPuzzle.table.setVisible(true);
        Gdx.input.setInputProcessor(letterPuzzle.stage);

        if (letterPuzzle.answer1.isPressed()) { status.setLives((byte)(lives - 1)); }

        if (letterPuzzle.answer2.isPressed()) { status.setLives((byte)(lives - 1)); }

        if (letterPuzzle.answer3.isPressed()) { status.setLives((byte)(lives - 1)); }

        if (letterPuzzle.answer4.isPressed()) {
            Gdx.input.setInputProcessor(new FiremanInputProcessor(fireman, game.batch, asset).im);
            letterPuzzle.table.setVisible(false);
        }

        if (letterPuzzle.enterHints.isPressed()) {

        }
    }

    private void fPicsOWord2(final Fireman fireman, final FuegoPeligro game, final AssetManager asset,
                            final byte lives, final String fileName, final CurrentPlayerStatus status) {
        Texture texture_1 = new Texture(Gdx.files.internal(fileName +
                line.get(0).substring(1) + "." + 1 + ".jpg"));
        Texture texture_2 = new Texture(Gdx.files.internal(fileName +
                line.get(0).substring(1) + "." + 2 + ".jpg"));
        Texture texture_3 = new Texture(Gdx.files.internal(fileName +
                line.get(0).substring(1) + "." + 3 + ".jpg"));
        Texture texture_4 = new Texture(Gdx.files.internal(fileName +
                line.get(0).substring(1) + "." + 4 + ".jpg"));

        TextureRegion myTextureRegion_1 = new TextureRegion(texture_1);
        TextureRegion myTextureRegion_2 = new TextureRegion(texture_2);
        TextureRegion myTextureRegion_3 = new TextureRegion(texture_3);
        TextureRegion myTextureRegion_4 = new TextureRegion(texture_4);

        TextureRegionDrawable myTexRegionDrawable_1 = new TextureRegionDrawable(myTextureRegion_1);
        TextureRegionDrawable myTexRegionDrawable_2 = new TextureRegionDrawable(myTextureRegion_2);
        TextureRegionDrawable myTexRegionDrawable_3 = new TextureRegionDrawable(myTextureRegion_3);
        TextureRegionDrawable myTexRegionDrawable_4 = new TextureRegionDrawable(myTextureRegion_4);

        fourPicsOneWord2.answer1.setDrawable(myTexRegionDrawable_1);
        fourPicsOneWord2.answer2.setDrawable(myTexRegionDrawable_2);
        fourPicsOneWord2.answer3.setDrawable(myTexRegionDrawable_3);
        fourPicsOneWord2.answer4.setDrawable(myTexRegionDrawable_4);

        fourPicsOneWord2.render(Gdx.graphics.getDeltaTime());
        fourPicsOneWord2.table.setVisible(true);
        Gdx.input.setInputProcessor(fourPicsOneWord2.stage);

        if (fourPicsOneWord2.enterAnswer.isPressed()) {
            if (fourPicsOneWord2.input.getText().equalsIgnoreCase(line.get(1))) {
                Gdx.input.setInputProcessor(new FiremanInputProcessor(fireman, game.batch, asset).im);
                fourPicsOneWord2.table.setVisible(false);
            } else if (!fourPicsOneWord2.input.getText().equalsIgnoreCase(line.get(1))) {
                status.setLives((byte)(lives - 1)); }
            texture_1.dispose();
            texture_2.dispose();
            texture_3.dispose();
            texture_4.dispose();
        }

        if (fourPicsOneWord2.enterHints.isPressed()) {
            Gdx.input.setInputProcessor(new FiremanInputProcessor(fireman, game.batch, asset).im);
            fourPicsOneWord2.table.setVisible(false);
            texture_1.dispose();
            texture_2.dispose();
            texture_3.dispose();
            texture_4.dispose();
        }
    }
    
    private void fPicsOWord(final Fireman fireman, final FuegoPeligro game, final AssetManager asset,
                            final byte lives, final String fileName, final CurrentPlayerStatus status) {
        Texture texture_1 = new Texture(Gdx.files.internal(fileName +
                line.get(0).substring(1) + "." + 1 + ".jpg"));
        Texture texture_2 = new Texture(Gdx.files.internal(fileName +
                line.get(0).substring(1) + "." + 2 + ".jpg"));
        Texture texture_3 = new Texture(Gdx.files.internal(fileName +
                line.get(0).substring(1) + "." + 3 + ".jpg"));
        Texture texture_4 = new Texture(Gdx.files.internal(fileName +
                line.get(0).substring(1) + "." + 4 + ".jpg"));

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
        fourPicsOneWord.table.setVisible(true);
        Gdx.input.setInputProcessor(fourPicsOneWord.stage);

        if (fourPicsOneWord.enterAnswer.isPressed()) {
            if (fourPicsOneWord.input.getText().equalsIgnoreCase(line.get(1))) {
                Gdx.input.setInputProcessor(new FiremanInputProcessor(fireman, game.batch, asset).im);
                fourPicsOneWord.table.setVisible(false);
            } else if (!fourPicsOneWord.input.getText().equalsIgnoreCase(line.get(1))) {
                status.setLives((byte)(lives - 1)); }
            texture_1.dispose();
            texture_2.dispose();
            texture_3.dispose();
            texture_4.dispose();
        }

        if (fourPicsOneWord.enterHints.isPressed()) {
            Gdx.input.setInputProcessor(new FiremanInputProcessor(fireman, game.batch, asset).im);
            fourPicsOneWord.table.setVisible(false);
            texture_1.dispose();
            texture_2.dispose();
            texture_3.dispose();
            texture_4.dispose();
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
            minicamSelection(fireman2, fargo, assetManager, status);
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

    private void QAReader(String fileName, byte qaNumber1) {
        try (BufferedReader br = new BufferedReader(new FileReader(new File(fileName)))) {
            List<String> lines = br.lines().skip(qaNumber1).limit(1).collect(Collectors.toList());
            String lines2 = String.valueOf(lines);
            line = Arrays.asList(lines2.split(","));
        } catch (IOException e) { e.printStackTrace(); }
    }
}
