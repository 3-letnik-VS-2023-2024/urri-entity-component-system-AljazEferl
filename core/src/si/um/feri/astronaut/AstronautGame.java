package si.um.feri.astronaut;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import si.um.feri.astronaut.assets.AssetDescriptors;
import si.um.feri.astronaut.common.GameManager;
import si.um.feri.astronaut.config.GameConfig;
import si.um.feri.astronaut.ecs.system.ParticleRenderSystem;
import si.um.feri.astronaut.ecs.system.ParticleSystem2;
import si.um.feri.astronaut.ecs.system.RockSpawnSystem;
import si.um.feri.astronaut.ecs.system.TreasureSpawnSystem;
import si.um.feri.astronaut.ecs.system.BoundsSystem;
import si.um.feri.astronaut.ecs.system.CleanUpSystem;
import si.um.feri.astronaut.ecs.system.CleanUpSystem2;
import si.um.feri.astronaut.ecs.system.CollisionSystem;
import si.um.feri.astronaut.ecs.system.HudRenderSystem;
import si.um.feri.astronaut.ecs.system.MovementSystem;
import si.um.feri.astronaut.ecs.system.ParticleSystem;
import si.um.feri.astronaut.ecs.system.RenderSystem;
import si.um.feri.astronaut.ecs.system.ShipInputSystem;
import si.um.feri.astronaut.ecs.system.ShieldSpawnSystem;
import si.um.feri.astronaut.ecs.system.WorldWrapSystem;
import si.um.feri.astronaut.ecs.system.debug.DebugCameraSystem;
import si.um.feri.astronaut.ecs.system.debug.DebugInputSystem;
import si.um.feri.astronaut.ecs.system.debug.DebugRenderSystem;
import si.um.feri.astronaut.ecs.system.debug.GridRenderSystem;
import si.um.feri.astronaut.ecs.system.debug.support.ViewportUtils;
import si.um.feri.astronaut.ecs.system.passive.EntityFactorySystem;
import si.um.feri.astronaut.ecs.system.passive.SoundSystem;
import si.um.feri.astronaut.ecs.system.passive.StartUpSystem;

/**
 * Artwork from https://goodstuffnononsense.com/about/
 * https://goodstuffnononsense.com/hand-drawn-icons/space-icons/
 */
public class AstronautGame extends ApplicationAdapter {

    private static final Logger log = new Logger(AstronautGame.class.getSimpleName(), Logger.DEBUG);

    private AssetManager assetManager;
    private SpriteBatch batch;

    private OrthographicCamera camera;
    private Viewport viewport;
    private Viewport hudViewport;
    private ShapeRenderer renderer;
    private PooledEngine engine;
    private BitmapFont font;
    private boolean debug = true;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WIDTH, GameConfig.HEIGHT, camera);
        hudViewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        renderer = new ShapeRenderer();

        assetManager = new AssetManager();
        assetManager.getLogger().setLevel(Logger.DEBUG);

        assetManager.load(AssetDescriptors.GAMEPLAY);
        assetManager.load(AssetDescriptors.DAMAGE_SOUNDS);
        assetManager.load(AssetDescriptors.WATER_PARTICLE_EFFECT);
        assetManager.load(AssetDescriptors.FIRE_PARTICLE_EFFECT);
        assetManager.load(AssetDescriptors.LAUGH_SOUNDS);
        assetManager.load(AssetDescriptors.UI_FONT);
        assetManager.finishLoading();

        font = assetManager.get(AssetDescriptors.UI_FONT);

        engine = new PooledEngine();

        // 'passive' systems
        engine.addSystem(new EntityFactorySystem(assetManager));
        engine.addSystem(new SoundSystem(assetManager));
        engine.addSystem(new StartUpSystem());  // called only at the start, to generate first entities

        // 'active' systems
        engine.addSystem(new ShipInputSystem());
        engine.addSystem(new MovementSystem());
        engine.addSystem(new WorldWrapSystem());
        engine.addSystem(new BoundsSystem());
        engine.addSystem(new RockSpawnSystem());
        engine.addSystem(new TreasureSpawnSystem());
        engine.addSystem(new ShieldSpawnSystem());
        engine.addSystem(new CleanUpSystem());
        engine.addSystem(new CollisionSystem());
        engine.addSystem(new CleanUpSystem2());
        engine.addSystem(new ParticleSystem());
        engine.addSystem(new ParticleSystem2());
        engine.addSystem(new RenderSystem(batch, viewport));
        engine.addSystem(new ParticleRenderSystem(batch));


        engine.addSystem(new HudRenderSystem(batch, hudViewport, font));



        // debug systems
        if (debug) {
            ViewportUtils.DEFAULT_CELL_SIZE = 32;
            engine.addSystem(new GridRenderSystem(viewport, renderer));
            engine.addSystem(new DebugRenderSystem(viewport, renderer));
            engine.addSystem(new DebugCameraSystem(
                    GameConfig.WIDTH / 2, GameConfig.HEIGHT / 2,
                    camera
            ));
            engine.addSystem(new DebugInputSystem());   // show/hide grid on 'F5'
        }

        GameManager.INSTANCE.resetResult();
        logAllSystemsInEngine();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hudViewport.update(width, height, true);
    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.BLACK);

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) Gdx.app.exit();

        if (GameManager.INSTANCE.isGameOver()) {
            engine.update(0);
        } else {
            engine.update(Gdx.graphics.getDeltaTime());

            if(GameManager.INSTANCE.getShieldDuration() > 0){
            GameManager.INSTANCE.setDuration((GameManager.INSTANCE.getShieldDuration())-Gdx.graphics.getDeltaTime());
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        renderer.dispose();
        assetManager.dispose();
        engine.removeAllEntities();
    }

    public void logAllSystemsInEngine() {
        for (EntitySystem system : engine.getSystems()) {
            log.debug(system.getClass().getSimpleName());
        }
    }
}
