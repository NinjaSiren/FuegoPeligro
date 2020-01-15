package com.mygdx.fuegopeligro.map;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.fuegopeligro.physics.BodyEditorLoader;

/**
 * @author JDEsguerra
 */
public final class LevelFactory {
    private static final String CHECKPOINT_LAYER = "checkpoints";
    private static final String COINS_LAYER = "coins";
    public static String LEVEL_MAP_FILE = "";

    public LevelFactory() {

    }

    /**
     * Loads a tiled map described by the given level number. Creates {@link CollectibleRenderer}
     * for collectible layers (every layer which name starts with "collectible").
     *
     * @param world     The Box2D {@link World} used to create bodies and fixtures from objects layers in
     *                  the map.
     * @param loader
     * @param batch     A {@link Batch} to use while rendering tiles.
     * @param assets    {@link AssetManager} from where to get audio and graphics resources for the
     *                  collectibles.
     * @param stage     Number of the stage the returned render should draw.
     * @param level     Number of the level the returned render should draw.
     * @param unitScale Pixels per unit.
     * @return A new {@link LevelRenderer}, ready to render the map, its bodies and collectibles.
     */
    public static LevelRenderer create(final World world, final BodyEditorLoader loader, final Batch batch,
                                       final AssetManager assets, final byte stage, final byte level, final float unitScale) {
        LEVEL_MAP_FILE = String.format("map/stage.%s.%s.tmx", stage, level);
        TiledMap tiledMap = new TmxMapLoader().load(LEVEL_MAP_FILE);
        LevelRenderer renderer = new LevelRenderer(tiledMap, assets, batch, unitScale);

        for (MapLayer ml : tiledMap.getLayers()) {
            if (ml.getName().toLowerCase().startsWith(CHECKPOINT_LAYER)) {
                CollectibleRenderer carrots = new CollectibleRenderer(unitScale);
                carrots.load(world, loader, assets, ml);
                renderer.addCollectibleRenderer(carrots);
            } /*else if (ml.getName().toLowerCase().startsWith(COINS_LAYER)) {
                CollectibleRenderer coins = new CollectibleRenderer(unitScale);
                coins.load(world, loader, assets, ml);
                renderer.addCollectibleRenderer(coins);
            }*/
        }
        return renderer;
    }
}
