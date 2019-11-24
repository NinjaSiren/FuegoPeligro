/**
 *
 */
package com.mygdx.fuegopeligro.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.mappings.Ouya;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.fuegopeligro.FuegoPeligro;
import com.mygdx.fuegopeligro.audio.AudioProcessor;
import com.mygdx.fuegopeligro.audio.CarrotAudioProcessor;
import com.mygdx.fuegopeligro.audio.LevelAudioProcessor;
import com.mygdx.fuegopeligro.audio.NinjaRabbitAudioProcessor;
import com.mygdx.fuegopeligro.graphics.CarrotGraphicsProcessor;
import com.mygdx.fuegopeligro.graphics.GraphicsProcessor;
import com.mygdx.fuegopeligro.graphics.LevelGraphicsProcessor;
import com.mygdx.fuegopeligro.graphics.NinjaRabbitGraphicsProcessor;
import com.mygdx.fuegopeligro.input.NinjaRabbitControllerProcessor;
import com.mygdx.fuegopeligro.input.NinjaRabbitInputProcessor;
import com.mygdx.fuegopeligro.map.LevelRenderer;
import com.mygdx.fuegopeligro.physics.BodyEditorLoader;
import com.mygdx.fuegopeligro.physics.BodyProcessor;
import com.mygdx.fuegopeligro.physics.CarrotPhysicsProcessor;
import com.mygdx.fuegopeligro.physics.ContactListenerMultiplexer;
import com.mygdx.fuegopeligro.physics.LevelPhysicsProcessor;
import com.mygdx.fuegopeligro.physics.NinjaRabbitBodyProcessor;
import com.mygdx.fuegopeligro.physics.NinjaRabbitPhysicsProcessor;
import com.mygdx.fuegopeligro.physics.PhysicsProcessor;
import com.mygdx.fuegopeligro.player.CurrentPlayerStatus;
import com.mygdx.fuegopeligro.player.LevelPlayerStatusProcessor;
import com.mygdx.fuegopeligro.player.NinjaRabbitPlayerStatusProcessor;
import com.mygdx.fuegopeligro.player.PlayerStatusObserver;
import com.mygdx.fuegopeligro.player.PlayerStatusProcessor;

/**
 * Handles creation of new {@link Entity} instances.
 *
 * @author JDEsguerra
 *
 */
public final class EntityFactory {
    private static final ContactListenerMultiplexer CONTACT_LISTENER = new ContactListenerMultiplexer();

    /**
     * This should be used as a static factory. No instances allowed.
     */
    private EntityFactory() {

    }

    /**
     * Creates a new instance of {@link Collectible}, defining its graphical, audio and physical
     * properties.
     *
     * @param world
     *            The Box2D {@link World} onto which to create the {@link Body} and {@link Fixture}
     *            of the {@link Entity}.
     * @param assets
     *            The {@link AssetManager} from where to extract the graphical and audio resources.
     *            Those resources should be loaded in the manager before calling this method.
     * @return A ready to use instance of a new {@link Collectible}.
     */
    public static Entity createCollectible(final World world, final AssetManager assets) {
        PhysicsProcessor physics = new CarrotPhysicsProcessor();
        CONTACT_LISTENER.add(physics);
        world.setContactListener(CONTACT_LISTENER);
        GraphicsProcessor graphics = new CarrotGraphicsProcessor(assets);
        AudioProcessor audio = new CarrotAudioProcessor(assets);
        return new Collectible(graphics, physics, audio);
    }

    /**
     * Creates a new instance of {@link NinjaRabbit}, defining its graphical, audio and physical
     * properties.
     *
     * @param world
     *            The Box2D {@link World} onto which to create the {@link Body} and {@link Fixture}
     *            of the {@link Entity}.
     * @param loader
     *            A {@link BodyEditorLoader} to handle creation of the Entity body and fixtures.
     * @param assets
     *            The {@link AssetManager} from where to extract the graphical and audio resources.
     *            Those resources should be loaded in the manager before calling this method and
     *            won't be disposed.
     * @param status
     *            A reference to the global status of the player to be updated from the changes in
     *            the returned entity inner state.
     * @param observers
     *            An array of event receivers. Events will fire when the active player status
     *            changes (such as losing lives, collecting items, etc.).
     * @return A ready to use instance of a new {@link NinjaRabbit}.
     */
    public static Entity createNinjaRabbit(final World world, final BodyEditorLoader loader, final AssetManager assets,
                                           final CurrentPlayerStatus status, final PlayerStatusObserver... observers) {
        PhysicsProcessor physics = new NinjaRabbitPhysicsProcessor();
        CONTACT_LISTENER.add(physics);
        world.setContactListener(CONTACT_LISTENER);
        GraphicsProcessor graphics = new NinjaRabbitGraphicsProcessor(assets);
        BodyProcessor bodyProcessor = new NinjaRabbitBodyProcessor(world, loader);
        AudioProcessor audio = new NinjaRabbitAudioProcessor(assets);
        PlayerStatusProcessor player = new NinjaRabbitPlayerStatusProcessor(status);
        if (observers != null) {
            for (PlayerStatusObserver o : observers) {
                player.addObserver(o);
            }
        }
        NinjaRabbit ninjaRabbit = new NinjaRabbit(player, bodyProcessor, graphics, physics, audio);

        if (Ouya.isRunningOnOuya()) {
            Controllers.clearListeners();
            Controllers.addListener(new NinjaRabbitControllerProcessor(ninjaRabbit));
        } else {
            Gdx.input.setInputProcessor(new NinjaRabbitInputProcessor(ninjaRabbit));
        }
        return ninjaRabbit;
    }

    /**
     * Creates and returns a new instance of {@link Environment}, settings its physical, graphical
     * and audio attributes.
     *
     * @param world
     * @param batch
     * @param renderer
     * @param assets
     * @param observers
     * @return A newly created {@link Environment}.
     */
    public static Entity createEnvironment(final FuegoPeligro game, final World world, final Batch batch, final LevelRenderer renderer, final AssetManager assets,
                                           final CurrentPlayerStatus status, final PlayerStatusObserver... observers) {
        PhysicsProcessor physics = new LevelPhysicsProcessor(world, renderer.getTiledMap(), renderer.getUnitScale());
        CONTACT_LISTENER.add(physics);
        world.setContactListener(CONTACT_LISTENER);
        GraphicsProcessor graphics = new LevelGraphicsProcessor(assets, batch, renderer, game);
        AudioProcessor audio = new LevelAudioProcessor(assets, renderer.getTiledMap().getProperties());
        PlayerStatusProcessor player = new LevelPlayerStatusProcessor(status);
        if (observers != null) {
            for (PlayerStatusObserver o : observers) {
                player.addObserver(o);
            }
        }
        return new Environment(graphics, physics, audio, player);
    }

    /**
     * Removes every {@link ContactListener} added to the {@link World} after the creation of each
     * {@link Entity}.
     *
     * Calling this method will force the Box2D {@link World} to stop sending collision events to
     * current listeners.
     */
    public static void clearContactListeners() {
        CONTACT_LISTENER.clear();
    }
}
