package com.mygdx.fuegopeligro.ai.msg;

/**
 * Every possible type of message delivered through a
 * {@link com.badlogic.gdx.ai.msg.MessageDispatcher}. Inner integer codes should be used as
 * identifiers. Avoid using the ordinal.
 *
 * @author JDEsguerra
 */
public enum MessageType {
    /**
     * Message dispatched when the character gathers a collectible
     */
    COLLECTED(1),
    /**
     * Message dispatched when the player runs out of lives or time.
     */
    GAME_OVER(2),
    /**
     * Message dispatched when the main character reaches the end of the level.
     */
    EXIT(3),
    /**
     * Message dispatched after FINISH_LEVEL but before starting next level.
     */
    BEGIN_LEVEL(4),
    /**
     * Message dispatched just before starting a new level (e.g.: after reaching the exit).
     */
    FINISH_LEVEL(5),
    /**
     * Message dispatched when the player loses a life.
     */
    DEAD(6),
    /**
     * Message dispatched when the game is reset and goes back to the title screen (e.g.: after game
     * over).
     */
    RESET(7),

    /**
     * Message dispatched when the game is reset and goes back to the title screen (e.g.: after game
     * over).
     */
    RESET_PLAYER_STATUS(8),
    RESET_CURRENT_LEVEL(9),
    LOAD_NEXT_LEVEL(10),
    LOAD_NEW_GAME(11),
    BACK_TO_MENU(12),
    LOAD_CURRENT_GAME(13),
    PREFERENCES_SCREEN(14),
    LEVEL_SELECTION(15),
    MOVE_LEFT(16),
    MOVE_RIGHT(17),
    MOVE_JUMP(18),
    CORRECT_ANSWER(19),
    WRONG_ANSWER(20),
    HINT_USED(21),
    SET_LEVEL(22);

    /**
     * The inner code of the message.
     */
    private int code;

    MessageType(final int code) {
        this.code = code;
    }

    /**
     * Returns the integer code of the message type.
     *
     * @return The message code.
     */
    public int code() {
        return code;
    }
}
