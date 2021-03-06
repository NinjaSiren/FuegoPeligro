package com.mygdx.fuegopeligro;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.io.File;

/**
 * A convenient place to store and access every {@link AssetDescriptor}.
 *
 * @author JDEsguerra
 */
public final class Assets {

    public static final AssetDescriptor<Sound> CRUNCH_FX = new AssetDescriptor<Sound>("sfx" + File.separator + "crunch.ogg",
            Sound.class);
    public static final AssetDescriptor<TextureAtlas> NINJA_RABBIT_ATLAS = new AssetDescriptor<TextureAtlas>("ninja-rabbit.pack",
            TextureAtlas.class);
    public static final AssetDescriptor<Music> NINJA_RABBIT_THEME = new AssetDescriptor<Music>("sfx" + File.separator + "theme.ogg",
            Music.class);
    public static final AssetDescriptor<Sound> JUMP_FX = new AssetDescriptor<Sound>("sfx" + File.separator + "jump.ogg",
            Sound.class);
    public static final AssetDescriptor<Sound> LIFE_LOST_FX = new AssetDescriptor<Sound>("sfx" + File.separator + "life-lost.ogg",
            Sound.class);
    public static final AssetDescriptor<Music> VICTORY_FX = new AssetDescriptor<Music>("sfx" + File.separator + "victory.ogg",
            Music.class);
    public static final AssetDescriptor<BitmapFont> HUD_FONT = new AssetDescriptor<BitmapFont>("font" + File.separator + "last-ninja.fnt",
            BitmapFont.class);
    public static final AssetDescriptor<Texture> DEFAULT_BACKGROUND = new AssetDescriptor<Texture>("bg.png",
            Texture.class);
    public static final AssetDescriptor<Texture> GRASSLAND_BACKGROUND = new AssetDescriptor<Texture>("bg_grasslands.png",
            Texture.class);
    public static final AssetDescriptor<Texture> SWORD = new AssetDescriptor<Texture>("sword.png",
            Texture.class);
    public static final AssetDescriptor<Music> GAME_OVER_FX = new AssetDescriptor<Music>("sfx" + File.separator + "game-over.ogg",
            Music.class);
    public static final AssetDescriptor<Skin> GAME_UI_SKIN = new AssetDescriptor<Skin>("skins" + File.separator + "comic-ui.json",
            Skin.class);
    public static final AssetDescriptor<Texture> MENU_BG = new AssetDescriptor<Texture>("menubg.png",
            Texture.class);
    public static final AssetDescriptor<Texture> SPLASH_IMAGE = new AssetDescriptor<Texture>("delta_logo.png",
            Texture.class);
    public static final AssetDescriptor<Texture> SPLASH_IMAGE_2 = new AssetDescriptor<Texture>("libgdx_logo.png",
            Texture.class);
    public static final AssetDescriptor<Texture> MENU_BACKGROUND = new AssetDescriptor<Texture>("menubg.png",
            Texture.class);

    private Assets() {
    }
}
