package com.mygdx.fuegopeligro.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.fuegopeligro.FuegoPeligro;
import com.mygdx.fuegopeligro.GameOverOverlay;
import com.mygdx.fuegopeligro.LevelEndOverlay;
import com.mygdx.fuegopeligro.ai.fsm.FiremanState;
import com.mygdx.fuegopeligro.ai.msg.MessageType;
import com.mygdx.fuegopeligro.entity.Entity;
import com.mygdx.fuegopeligro.entity.Fireman;
import com.mygdx.fuegopeligro.graphics.hud.StatusBar;
import com.mygdx.fuegopeligro.graphics.minigames.FourPicsOneWord;
import com.mygdx.fuegopeligro.graphics.minigames.FourPicsOneWord2;
import com.mygdx.fuegopeligro.graphics.minigames.LetterPuzzle;
import com.mygdx.fuegopeligro.graphics.minigames.LetterPuzzle2;
import com.mygdx.fuegopeligro.graphics.minigames.MultipleChoice;
import com.mygdx.fuegopeligro.graphics.minigames.MultipleChoice2;
import com.mygdx.fuegopeligro.graphics.minigames.Workspaces;
import com.mygdx.fuegopeligro.graphics.minigames.Workspaces2;
import com.mygdx.fuegopeligro.input.FiremanInputProcessor;
import com.mygdx.fuegopeligro.map.LevelRenderer;
import com.mygdx.fuegopeligro.player.CurrentPlayerStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author JDEsguerra
 */
public class LevelGraphicsProcessor extends Table implements GraphicsProcessor, Telegraph {
    private final LevelRenderer mapRenderer;
    private final GameOverOverlay gameOver;
    private final LevelEndOverlay levelEnd;
    private MultipleChoice multipleChoice;
    private MultipleChoice2 multipleChoice2;
    private FourPicsOneWord fourPicsOneWord;
    private FourPicsOneWord2 fourPicsOneWord2;
    private LetterPuzzle letterPuzzle;
    private LetterPuzzle2 letterPuzzle2;
    private Workspaces workspaces;
    private Workspaces2 workspaces2;

    private boolean renderGameOver;
    private boolean renderLevelEnd;
    private boolean minicamSelection;
    private final CurrentPlayerStatus status;
    private List<String> line;
    private final InputMultiplexer im;
    private final Entity character;

    public LevelGraphicsProcessor(final AssetManager assets, final LevelRenderer mapRenderer,
                                  final FuegoPeligro game, final Fireman fireman,
                                  final CurrentPlayerStatus player, final StatusBar lvlScrn) {
        if (fireman == null) {
            throw new IllegalArgumentException("'character' cannot be null");
        }
        status = player;
        character = fireman;

        gameOver = new GameOverOverlay(assets, game);
        levelEnd = new LevelEndOverlay(assets, game);

        multipleChoice = new MultipleChoice(assets, game);
        letterPuzzle = new LetterPuzzle(assets, game);
        workspaces = new Workspaces(assets, game);
        fourPicsOneWord = new FourPicsOneWord(assets, game);

        multipleChoice2 = new MultipleChoice2(assets, game);
        letterPuzzle2 = new LetterPuzzle2(assets, game);
        workspaces2 = new Workspaces2(assets, game);
        fourPicsOneWord2 = new FourPicsOneWord2(assets, game);

        this.mapRenderer = mapRenderer;
        MessageManager.getInstance().addListeners(this, MessageType.GAME_OVER.code());
        MessageManager.getInstance().addListeners(this, MessageType.FINISH_LEVEL.code());
        MessageManager.getInstance().addListeners(this, MessageType.COLLECTED.code());
        
        im = new InputMultiplexer();
        GestureDetector gd = new GestureDetector(new FiremanInputProcessor(fireman, lvlScrn));
        im.addProcessor(new FiremanInputProcessor(fireman, lvlScrn));
        im.addProcessor(gd);
        im.addProcessor(lvlScrn);
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

    private void minicamSelection(final CurrentPlayerStatus status) {
        if (status.getCurrentWorld() == 1) {
            final String easyQA = "minigames/easyQA.csv";
            final String easyWords = "minigames/easywords.csv";
            final String easyDir = "minigames/easypics/";

            switch (status.getMGValue()) {
                case 1:
                    QAReader(easyQA, status.getEqaValue());
                    multiChoice2(status);
                    break;
                case 2:
                    QAReader(easyQA, status.getEqaValue());
                    multiChoice(status);
                    break;
                case 3:
                    QAReader(easyQA, status.getEqaValue());
                    wordScales2(status);
                    break;
                case 4:
                    QAReader(easyQA, status.getEqaValue());
                    wordScales(status);
                    break;
                case 5:
                    QAReader(easyWords, status.getEqaValue());
                    letterPutz2(status);
                    break;
                case 6:
                    QAReader(easyWords, status.getEqaValue());
                    letterPutz(status);
                    break;
                case 7:
                    QAReader(easyWords, status.getEqaValue());
                    fPicsOWord2(easyDir, status);
                    break;
                case 8:
                    QAReader(easyWords, status.getEqaValue());
                    fPicsOWord(easyDir, status);
                    break;
            }
        } else if (status.getCurrentWorld() == 2) {
            final String hardQA = "minigames/hardQA.csv";
            final String hardWords = "minigames/hardwords.csv";
            final String hardDir = "minigames/hardpics/";

            switch (status.getMGValue()) {
                case 1:
                    QAReader(hardQA, status.getEqaValue());
                    multiChoice2(status);
                    break;
                case 2:
                    QAReader(hardQA, status.getEqaValue());
                    multiChoice(status);
                    break;
                case 3:
                    QAReader(hardQA, status.getEqaValue());
                    wordScales2(status);
                    break;
                case 4:
                    QAReader(hardQA, status.getEqaValue());
                    wordScales(status);
                    break;
                case 5:
                    QAReader(hardWords, status.getEqaValue());
                    letterPutz2(status);
                    break;
                case 6:
                    QAReader(hardWords, status.getEqaValue());
                    letterPutz(status);
                    break;
                case 7:
                    QAReader(hardWords, status.getEqaValue());
                    fPicsOWord2(hardDir, status);
                    break;
                case 8:
                    QAReader(hardWords, status.getEqaValue());
                    fPicsOWord(hardDir, status);
                    break;
            }
        }
    }

    private void multiChoice2(final CurrentPlayerStatus status) {
        multipleChoice2.getQuestionText().setText(getLine().get(6));
        multipleChoice2.getAnswer1().setText(getLine().get(1));
        multipleChoice2.getAnswer2().setText(getLine().get(2));
        multipleChoice2.getAnswer3().setText(getLine().get(3));
        multipleChoice2.getAnswer4().setText(getLine().get(4));

        multipleChoice2.render(Gdx.graphics.getDeltaTime());
        multipleChoice2.getTable().setVisible(true);
        Gdx.input.setInputProcessor(multipleChoice2.getStage());

        if (multipleChoice2.getAnswer1().isPressed()) {
            MessageManager.getInstance().dispatchMessage(MessageType.CORRECT_ANSWER.code());
            Gdx.input.setInputProcessor(getIm());
            multipleChoice2.getTable().setVisible(false);
        } else if (multipleChoice2.getAnswer2().isPressed()) {
            MessageManager.getInstance().dispatchMessage(MessageType.WRONG_ANSWER.code());
        } else if (multipleChoice2.getAnswer3().isPressed()) {
            MessageManager.getInstance().dispatchMessage(MessageType.WRONG_ANSWER.code());
        } else if (multipleChoice2.getAnswer4().isPressed()) {
            MessageManager.getInstance().dispatchMessage(MessageType.WRONG_ANSWER.code());
        } else if (multipleChoice2.getEnterHints().isPressed()) {
            if (status.getCollectibles() > 0) {
                MessageManager.getInstance().dispatchMessage(MessageType.HINT_USED.code());
                multipleChoice2.getAnswer2().setVisible(false);
                multipleChoice2.getAnswer3().setVisible(false);
            }
        }
    }
    
    private void multiChoice(final CurrentPlayerStatus status) {
        multipleChoice.getQuestionText().setText(getLine().get(6));
        multipleChoice.getAnswer1().setText(getLine().get(1));
        multipleChoice.getAnswer2().setText(getLine().get(2));
        multipleChoice.getAnswer3().setText(getLine().get(3));
        multipleChoice.getAnswer4().setText(getLine().get(4));

        multipleChoice.render(Gdx.graphics.getDeltaTime());
        multipleChoice.getTable().setVisible(true);
        Gdx.input.setInputProcessor(multipleChoice.getStage());

        if (multipleChoice.getAnswer1().isPressed()) {
            MessageManager.getInstance().dispatchMessage(MessageType.CORRECT_ANSWER.code());
            Gdx.input.setInputProcessor(getIm());
            multipleChoice.getTable().setVisible(false);
        } else if (multipleChoice.getAnswer2().isPressed()) {
            MessageManager.getInstance().dispatchMessage(MessageType.WRONG_ANSWER.code());
        } else if (multipleChoice.getAnswer3().isPressed()) {
            MessageManager.getInstance().dispatchMessage(MessageType.WRONG_ANSWER.code());
        } else if (multipleChoice.getAnswer4().isPressed()) {
            MessageManager.getInstance().dispatchMessage(MessageType.WRONG_ANSWER.code());
        } else if (multipleChoice.getEnterHints().isPressed()) {
            if (status.getCollectibles() > 0) {
                MessageManager.getInstance().dispatchMessage(MessageType.HINT_USED.code());
                multipleChoice.getAnswer2().setVisible(false);
                multipleChoice.getAnswer4().setVisible(false);
            }
        }
    }

    private void wordScales2(final CurrentPlayerStatus status) {
        workspaces2.getQuestionText().setText(getLine().get(6));
        workspaces2.getAnswer1().setText(getLine().get(1));
        workspaces2.getAnswer2().setText(getLine().get(2));
        workspaces2.getAnswer3().setText(getLine().get(3));
        workspaces2.getAnswer4().setText(getLine().get(4));

        workspaces2.render(Gdx.graphics.getDeltaTime());
        workspaces2.getTable().setVisible(true);
        Gdx.input.setInputProcessor(workspaces2.getStage());

        if (workspaces2.getAnswer1().isPressed()) {
            MessageManager.getInstance().dispatchMessage(MessageType.CORRECT_ANSWER.code());
            Gdx.input.setInputProcessor(getIm());
            workspaces2.getTable().setVisible(false);
        } else if (workspaces2.getAnswer2().isPressed()) {
            MessageManager.getInstance().dispatchMessage(MessageType.WRONG_ANSWER.code());
        } else if (workspaces2.getAnswer3().isPressed()) {
            MessageManager.getInstance().dispatchMessage(MessageType.WRONG_ANSWER.code());
        } else if (workspaces2.getAnswer4().isPressed()) {
            MessageManager.getInstance().dispatchMessage(MessageType.WRONG_ANSWER.code());
        } else if (workspaces2.getEnterHints().isPressed()) {
            if (status.getCollectibles() > 0) {
                MessageManager.getInstance().dispatchMessage(MessageType.HINT_USED.code());
                workspaces2.getAnswer2().setVisible(false);
                workspaces2.getAnswer4().setVisible(false);
            }
        }
    }
    
    private void wordScales(final CurrentPlayerStatus status) {
        workspaces.getQuestionText().setText(getLine().get(6));
        workspaces.getAnswer1().setText(getLine().get(1));
        workspaces.getAnswer2().setText(getLine().get(2));
        workspaces.getAnswer3().setText(getLine().get(3));
        workspaces.getAnswer4().setText(getLine().get(4));

        workspaces.render(Gdx.graphics.getDeltaTime());
        workspaces.getTable().setVisible(true);
        Gdx.input.setInputProcessor(workspaces.getStage());

        if (workspaces.getAnswer1().isPressed()) {
            MessageManager.getInstance().dispatchMessage(MessageType.CORRECT_ANSWER.code());
            Gdx.input.setInputProcessor(getIm());
            workspaces.getTable().setVisible(false);
        } else if (workspaces.getAnswer2().isPressed()) {
            MessageManager.getInstance().dispatchMessage(MessageType.WRONG_ANSWER.code());
        } else if (workspaces.getAnswer3().isPressed()) {
            MessageManager.getInstance().dispatchMessage(MessageType.WRONG_ANSWER.code());
        } else if (workspaces.getAnswer4().isPressed()) {
            MessageManager.getInstance().dispatchMessage(MessageType.WRONG_ANSWER.code());
        } else if (workspaces.getEnterHints().isPressed()) {
            if (status.getCollectibles() > 0) {
                MessageManager.getInstance().dispatchMessage(MessageType.HINT_USED.code());
                workspaces.getAnswer2().setVisible(false);
                workspaces.getAnswer3().setVisible(false);
            }
        }
    }

    private void letterPutz2(final CurrentPlayerStatus status) {
        if (status.getCurrentWorld() == 1) {
            letterPuzzle2.getQuestionText().setText(getLine()
                    .get(1)
                    .replace('i',' ')
                    .replace('o', ' ')
                    .replace('u', ' ')
                    .substring(1));
        } else if (status.getCurrentWorld() == 2) {
            letterPuzzle2.getQuestionText().setText(getLine()
                    .get(1)
                    .replace('e',' ')
                    .replace('i', ' ')
                    .replace('o', ' ')
                    .replace('u', ' ')
                    .substring(1));
        }

        letterPuzzle2.getAnswer1().setText(getLine().get(4));
        letterPuzzle2.getAnswer2().setText(getLine().get(3));
        letterPuzzle2.getAnswer3().setText(getLine().get(2));
        letterPuzzle2.getAnswer4().setText(getLine().get(1));

        letterPuzzle2.render(Gdx.graphics.getDeltaTime());
        letterPuzzle2.getTable().setVisible(true);
        Gdx.input.setInputProcessor(letterPuzzle2.getStage());

        if (letterPuzzle2.getAnswer1().isPressed()) {
            MessageManager.getInstance().dispatchMessage(MessageType.WRONG_ANSWER.code());
        } else if (letterPuzzle2.getAnswer2().isPressed()) {
            MessageManager.getInstance().dispatchMessage(MessageType.WRONG_ANSWER.code());
        } else if (letterPuzzle2.getAnswer3().isPressed()) {
            MessageManager.getInstance().dispatchMessage(MessageType.WRONG_ANSWER.code());
        } else if (letterPuzzle2.getAnswer4().isPressed()) {
            MessageManager.getInstance().dispatchMessage(MessageType.CORRECT_ANSWER.code());
            Gdx.input.setInputProcessor(getIm());
            letterPuzzle2.getTable().setVisible(false);
        } else if (letterPuzzle2.getEnterHints().isPressed()) {
            if (status.getCollectibles() > 0) {
                MessageManager.getInstance().dispatchMessage(MessageType.HINT_USED.code());
                letterPuzzle2.getAnswer2().setVisible(false);
                letterPuzzle2.getAnswer1().setVisible(false);
            }
        }
    }
    
    private void letterPutz(final CurrentPlayerStatus status) {
        if (status.getCurrentWorld() == 1) {
            letterPuzzle.getQuestionText().setText(getLine()
                    .get(1)
                    .replace('i',' ')
                    .replace('o', ' ')
                    .replace('u', ' ')
                    .substring(1));
        } else if (status.getCurrentWorld() == 2) {
            letterPuzzle.getQuestionText().setText(getLine()
                    .get(1)
                    .replace('a',' ')
                    .replace('i', ' ')
                    .replace('o', ' ')
                    .replace('u', ' ')
                    .substring(1));
        }

        letterPuzzle.getAnswer1().setText(getLine().get(4));
        letterPuzzle.getAnswer2().setText(getLine().get(3));
        letterPuzzle.getAnswer3().setText(getLine().get(2));
        letterPuzzle.getAnswer4().setText(getLine().get(1));

        letterPuzzle.render(Gdx.graphics.getDeltaTime());
        letterPuzzle.getTable().setVisible(true);
        Gdx.input.setInputProcessor(letterPuzzle.getStage());
        
        if (letterPuzzle.getAnswer1().isPressed()) {
            MessageManager.getInstance().dispatchMessage(MessageType.WRONG_ANSWER.code());
        } else if (letterPuzzle.getAnswer2().isPressed()) {
            MessageManager.getInstance().dispatchMessage(MessageType.WRONG_ANSWER.code());
        } else if (letterPuzzle.getAnswer3().isPressed()) {
            MessageManager.getInstance().dispatchMessage(MessageType.WRONG_ANSWER.code());
        } else if (letterPuzzle.getAnswer4().isPressed()) {
            MessageManager.getInstance().dispatchMessage(MessageType.CORRECT_ANSWER.code());
            Gdx.input.setInputProcessor(getIm());
            letterPuzzle.getTable().setVisible(false);
        } else if (letterPuzzle.getEnterHints().isPressed()) {
            if (status.getCollectibles() > 0) {
                MessageManager.getInstance().dispatchMessage(MessageType.HINT_USED.code());
                letterPuzzle.getAnswer3().setVisible(false);
                letterPuzzle.getAnswer1().setVisible(false);
            }
        }
    }

    private void fPicsOWord2(final String fileName, final CurrentPlayerStatus status) {
        Texture texture_1 = new Texture(Gdx.files.internal(fileName +
                getLine().get(0).substring(1) + "." + 1 + ".jpg"));
        Texture texture_2 = new Texture(Gdx.files.internal(fileName +
                getLine().get(0).substring(1) + "." + 2 + ".jpg"));
        Texture texture_3 = new Texture(Gdx.files.internal(fileName +
                getLine().get(0).substring(1) + "." + 3 + ".jpg"));
        Texture texture_4 = new Texture(Gdx.files.internal(fileName +
                getLine().get(0).substring(1) + "." + 4 + ".jpg"));

        TextureRegion myTextureRegion_1 = new TextureRegion(texture_1);
        TextureRegion myTextureRegion_2 = new TextureRegion(texture_2);
        TextureRegion myTextureRegion_3 = new TextureRegion(texture_3);
        TextureRegion myTextureRegion_4 = new TextureRegion(texture_4);

        TextureRegionDrawable myTexRegionDrawable_1 = new TextureRegionDrawable(myTextureRegion_1);
        TextureRegionDrawable myTexRegionDrawable_2 = new TextureRegionDrawable(myTextureRegion_2);
        TextureRegionDrawable myTexRegionDrawable_3 = new TextureRegionDrawable(myTextureRegion_3);
        TextureRegionDrawable myTexRegionDrawable_4 = new TextureRegionDrawable(myTextureRegion_4);

        fourPicsOneWord2.getAnswer1().setDrawable(myTexRegionDrawable_1);
        fourPicsOneWord2.getAnswer2().setDrawable(myTexRegionDrawable_2);
        fourPicsOneWord2.getAnswer3().setDrawable(myTexRegionDrawable_3);
        fourPicsOneWord2.getAnswer4().setDrawable(myTexRegionDrawable_4);

        fourPicsOneWord2.render(Gdx.graphics.getDeltaTime());
        fourPicsOneWord2.getTable().setVisible(true);
        Gdx.input.setInputProcessor(fourPicsOneWord2.getStage());

        if (fourPicsOneWord2.getEnterAnswer().isPressed()) {
            if (fourPicsOneWord2.getInput().getText().equalsIgnoreCase(getLine().get(1))) {
                MessageManager.getInstance().dispatchMessage(MessageType.CORRECT_ANSWER.code());
                Gdx.input.setInputProcessor(getIm());
                fourPicsOneWord2.getTable().setVisible(false);
            } else if (!fourPicsOneWord2.getInput().getText().equalsIgnoreCase(getLine().get(1))) {
                MessageManager.getInstance().dispatchMessage(MessageType.WRONG_ANSWER.code()); }
            texture_1.dispose();
            texture_2.dispose();
            texture_3.dispose();
            texture_4.dispose();
        } else if (fourPicsOneWord2.getEnterHints().isPressed()) {
            if (status.getCollectibles() > 0) {
                MessageManager.getInstance().dispatchMessage(MessageType.HINT_USED.code());
                fourPicsOneWord2.getInput().setText(getLine().get(1).substring(0, getLine().get(1).length() / 2));
            }
        }
    }
    
    private void fPicsOWord(final String fileName, final CurrentPlayerStatus status) {
        Texture texture_1 = new Texture(Gdx.files.internal(fileName +
                getLine().get(0).substring(1) + "." + 1 + ".jpg"));
        Texture texture_2 = new Texture(Gdx.files.internal(fileName +
                getLine().get(0).substring(1) + "." + 2 + ".jpg"));
        Texture texture_3 = new Texture(Gdx.files.internal(fileName +
                getLine().get(0).substring(1) + "." + 3 + ".jpg"));
        Texture texture_4 = new Texture(Gdx.files.internal(fileName +
                getLine().get(0).substring(1) + "." + 4 + ".jpg"));

        TextureRegion myTextureRegion_1 = new TextureRegion(texture_1);
        TextureRegion myTextureRegion_2 = new TextureRegion(texture_2);
        TextureRegion myTextureRegion_3 = new TextureRegion(texture_3);
        TextureRegion myTextureRegion_4 = new TextureRegion(texture_4);

        TextureRegionDrawable myTexRegionDrawable_1 = new TextureRegionDrawable(myTextureRegion_1);
        TextureRegionDrawable myTexRegionDrawable_2 = new TextureRegionDrawable(myTextureRegion_2);
        TextureRegionDrawable myTexRegionDrawable_3 = new TextureRegionDrawable(myTextureRegion_3);
        TextureRegionDrawable myTexRegionDrawable_4 = new TextureRegionDrawable(myTextureRegion_4);

        fourPicsOneWord.getAnswer1().setDrawable(myTexRegionDrawable_1);
        fourPicsOneWord.getAnswer2().setDrawable(myTexRegionDrawable_2);
        fourPicsOneWord.getAnswer3().setDrawable(myTexRegionDrawable_3);
        fourPicsOneWord.getAnswer4().setDrawable(myTexRegionDrawable_4);

        character.changeState(FiremanState.IDLE);

        fourPicsOneWord.render(Gdx.graphics.getDeltaTime());
        fourPicsOneWord.getTable().setVisible(true);
        Gdx.input.setInputProcessor(fourPicsOneWord.getStage());

        if (fourPicsOneWord.getEnterAnswer().isPressed()) {
            if (fourPicsOneWord.getInput().getText().equalsIgnoreCase(getLine().get(1))) {
                MessageManager.getInstance().dispatchMessage(MessageType.CORRECT_ANSWER.code());
                Gdx.input.setInputProcessor(getIm());
                fourPicsOneWord.getTable().setVisible(false);
            } else if (!fourPicsOneWord.getInput().getText().equalsIgnoreCase(getLine().get(1))) {
                MessageManager.getInstance().dispatchMessage(MessageType.WRONG_ANSWER.code()); }
            texture_1.dispose();
            texture_2.dispose();
            texture_3.dispose();
            texture_4.dispose();
        } else if (fourPicsOneWord.getEnterHints().isPressed()) {
            if (status.getCollectibles() > 0) {
                MessageManager.getInstance().dispatchMessage(MessageType.HINT_USED.code());
                fourPicsOneWord.getInput().setText(getLine().get(1).substring(0, getLine().get(1).length() / 2));
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
            minicamSelection(status);
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
        workspaces.resize(width, height);
        letterPuzzle.resize(width, height);
        fourPicsOneWord.resize(width, height);
    }

    @Override
    public void dispose() {
        gameOver.dispose();
        levelEnd.dispose();
        multipleChoice.dispose();
        workspaces.dispose();
        letterPuzzle.dispose();
        fourPicsOneWord.dispose();
    }

    private void QAReader(String fileName, int qaNumber1) {
        FileHandle handle = Gdx.files.internal(fileName);
        if(handle.exists()) {
            try (BufferedReader br = new BufferedReader(handle.reader())) {
                List<String> lines = br.lines().skip(qaNumber1).limit(1).collect(Collectors.toList());
                String lines2 = String.valueOf(lines);
                setLine(Arrays.asList(lines2.split(",")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private List<String> getLine() { return line; }
    
    private void setLine(List<String> value) { line = value; }
    
    private InputMultiplexer getIm() { return im; }
}
