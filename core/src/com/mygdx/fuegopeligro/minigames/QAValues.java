package com.mygdx.fuegopeligro.minigames;

import com.badlogic.gdx.ai.msg.Telegram;
import com.mygdx.fuegopeligro.ai.msg.QAMessageType;

public class QAValues {

    //@Override
    public boolean handleMessage(Telegram msg) {
        /**
         * Easy QA
         */
        if (msg.message == QAMessageType.E1.code()) {

        } else if (msg.message == QAMessageType.E2.code()) {

        } else if (msg.message == QAMessageType.E3.code()) {

        }

        /**
         * Hard QA
         */


        return false;
    }
}
