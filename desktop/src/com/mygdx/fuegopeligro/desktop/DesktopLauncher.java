
package com.mygdx.fuegopeligro.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.fuegopeligro.FuegoPeligro;

/**
 * @author JDEsguerra
 */
public class DesktopLauncher {

    public static void main(final String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1280;
        config.height = 720;
        config.vSyncEnabled = true;
        new LwjglApplication(new FuegoPeligro(), config);
    }
}
