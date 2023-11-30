package si.um.feri.astronaut.ecs.system.passive;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;

import si.um.feri.astronaut.assets.AssetDescriptors;
import si.um.feri.astronaut.assets.RegionNames;
import si.um.feri.astronaut.config.GameConfig;
import si.um.feri.astronaut.ecs.component.AmmoComponent;
import si.um.feri.astronaut.ecs.component.AsteroidComponent;
import si.um.feri.astronaut.ecs.component.AstronautComponent;
import si.um.feri.astronaut.ecs.component.BoundsComponent;
import si.um.feri.astronaut.ecs.component.CleanUpComponent;
import si.um.feri.astronaut.ecs.component.DimensionComponent;
import si.um.feri.astronaut.ecs.component.MovementComponentXYR;
import si.um.feri.astronaut.ecs.component.PositionComponent;
import si.um.feri.astronaut.ecs.component.RocketComponent;
import si.um.feri.astronaut.ecs.component.ShieldComponent;
import si.um.feri.astronaut.ecs.component.TextureComponent;
import si.um.feri.astronaut.ecs.component.WorldWrapComponent;
import si.um.feri.astronaut.ecs.component.ZOrderComponent;

public class EntityFactorySystem extends EntitySystem {

    private static final int ASTEROID_Z_ORDER = 1;
    private static final int ASTRONAUT_Z_ORDER = 2;
    private static final int SHIELD_Z_ORDER = 3;


    private static final int ROCKET_Z_ORDER = 4;

    private final AssetManager assetManager;

    private PooledEngine engine;
    private TextureAtlas gamePlayAtlas;
    private Entity rocketEntity;

    public EntityFactorySystem(AssetManager assetManager) {
        this.assetManager = assetManager;
        setProcessing(false);   // passive system
        init();
    }

    private void init() {
        gamePlayAtlas = assetManager.get(AssetDescriptors.GAMEPLAY);
    }

    @Override
    public void addedToEngine(Engine engine) {
        this.engine = (PooledEngine) engine;
    }

    public void createRocket() {
        PositionComponent position = engine.createComponent(PositionComponent.class);
        position.x = GameConfig.WIDTH / 2f - GameConfig.ROCKET_WIDTH / 2f;
        position.y = 20;

        DimensionComponent dimension = engine.createComponent(DimensionComponent.class);
        dimension.width = GameConfig.ROCKET_WIDTH;
        dimension.height = GameConfig.ROCKET_HEIGHT;

        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        bounds.rectangle.setPosition(position.x, position.y);
        bounds.rectangle.setSize(dimension.width, dimension.height);

        MovementComponentXYR movement = engine.createComponent(MovementComponentXYR.class);

        RocketComponent rocket = engine.createComponent(RocketComponent.class);

        WorldWrapComponent worldWrap = engine.createComponent(WorldWrapComponent.class);

        TextureComponent texture = engine.createComponent(TextureComponent.class);
        texture.region = gamePlayAtlas.findRegion(RegionNames.PIRATE_SHIP);

        ZOrderComponent zOrder = engine.createComponent(ZOrderComponent.class);
        zOrder.z = ROCKET_Z_ORDER;

        Entity entity = engine.createEntity();
        entity.add(position);
        entity.add(dimension);
        entity.add(bounds);
        entity.add(movement);
        entity.add(rocket);
        entity.add(worldWrap);
        entity.add(texture);
        entity.add(zOrder);
        engine.addEntity(entity);
        rocketEntity = entity;
    }

    public void createAsteroid() {
        PositionComponent position = engine.createComponent(PositionComponent.class);

        position.x = MathUtils.random(0, GameConfig.WIDTH - GameConfig.ASTEROID_WIDTH);
        position.y = GameConfig.HEIGHT;

        MovementComponentXYR movementComponent = engine.createComponent(MovementComponentXYR.class);
        movementComponent.xSpeed = -GameConfig.ASTEROID_SPEED_X_MIN * MathUtils.random(-1f, 1f);
        movementComponent.ySpeed = -GameConfig.ASTEROID_SPEED_X_MIN * MathUtils.random(1f, 2f);
        movementComponent.rSpeed = MathUtils.random(-1f, 1f);

        float randFactor = MathUtils.random(1f, 4f);
        DimensionComponent dimension = engine.createComponent(DimensionComponent.class);
        dimension.width = GameConfig.ASTEROID_WIDTH * randFactor;
        dimension.height = GameConfig.ASTEROID_HEIGHT * randFactor;

        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        bounds.rectangle.setPosition(position.x, position.y);
        bounds.rectangle.setSize(dimension.width, dimension.height);

        AsteroidComponent asteroidComponent = engine.createComponent(AsteroidComponent.class);

        TextureComponent texture = engine.createComponent(TextureComponent.class);
        texture.region = gamePlayAtlas.findRegion(RegionNames.ROCK);

        ZOrderComponent zOrder = engine.createComponent(ZOrderComponent.class);
        zOrder.z = ASTEROID_Z_ORDER;

         WorldWrapComponent worldWrap = engine.createComponent(WorldWrapComponent.class);

        CleanUpComponent cleanUp = engine.createComponent(CleanUpComponent.class);

        Entity entity = engine.createEntity();
        entity.add(position);
        entity.add(dimension);
        entity.add(bounds);
        entity.add(movementComponent);
        entity.add(asteroidComponent);
        entity.add(texture);
        entity.add(zOrder);
         entity.add(worldWrap);
        entity.add(cleanUp);
        engine.addEntity(entity);
    }

    public void createAstronaut() {
        PositionComponent position = engine.createComponent(PositionComponent.class);
        position.x = MathUtils.random(0, GameConfig.WIDTH - GameConfig.ASTRONAUT_SIZE);
        position.y = GameConfig.HEIGHT;

        MovementComponentXYR movementComponent = engine.createComponent(MovementComponentXYR.class);
        movementComponent.xSpeed = GameConfig.ASTEROID_SPEED_X_MIN * MathUtils.random(-0.1f, 0.1f);
        movementComponent.ySpeed = -GameConfig.ASTEROID_SPEED_X_MIN * MathUtils.random(1f, 2f);
        movementComponent.rSpeed = MathUtils.random(-1f, 1f);

        AstronautComponent astronaut = engine.createComponent(AstronautComponent.class);

        DimensionComponent dimension = engine.createComponent(DimensionComponent.class);
        dimension.width = GameConfig.ASTRONAUT_SIZE;
        dimension.height = GameConfig.ASTRONAUT_SIZE;

        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        bounds.rectangle.setPosition(position.x, position.y);
        bounds.rectangle.setSize(dimension.width, dimension.height);

        TextureComponent texture = engine.createComponent(TextureComponent.class);
        texture.region = gamePlayAtlas.findRegion(RegionNames.TREASURE);

        ZOrderComponent zOrder = engine.createComponent(ZOrderComponent.class);
        zOrder.z = ASTRONAUT_Z_ORDER;

        CleanUpComponent cleanUp = engine.createComponent(CleanUpComponent.class);
        WorldWrapComponent worldWrap = engine.createComponent(WorldWrapComponent.class);

        Entity entity = engine.createEntity();
        entity.add(position);
        entity.add(dimension);
        entity.add(bounds);
        entity.add(movementComponent);
        entity.add(astronaut);
        entity.add(texture);
        entity.add(zOrder);
        entity.add(cleanUp);
        entity.add(worldWrap);
        engine.addEntity(entity);
    }
    public void createShield() {
        PositionComponent position = engine.createComponent(PositionComponent.class);
        position.x = MathUtils.random(0, GameConfig.WIDTH - GameConfig.SHIELD_SIZE);
        position.y = GameConfig.HEIGHT;

        MovementComponentXYR movementComponent = engine.createComponent(MovementComponentXYR.class);
        movementComponent.xSpeed = GameConfig.SHIELD_SPEED_X_MIN * MathUtils.random(-0.1f, 0.1f);
        movementComponent.ySpeed = -GameConfig.SHIELD_SPEED_X_MIN * MathUtils.random(1f, 2f);
        movementComponent.rSpeed = MathUtils.random(-1f, 1f);

        ShieldComponent shield = engine.createComponent(ShieldComponent.class);

        DimensionComponent dimension = engine.createComponent(DimensionComponent.class);
        dimension.width = GameConfig.SHIELD_SIZE;
        dimension.height = GameConfig.SHIELD_SIZE;

        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        bounds.rectangle.setPosition(position.x, position.y);
        bounds.rectangle.setSize(dimension.width, dimension.height);

        TextureComponent texture = engine.createComponent(TextureComponent.class);
        texture.region = gamePlayAtlas.findRegion(RegionNames.SHIELD);

        ZOrderComponent zOrder = engine.createComponent(ZOrderComponent.class);
        zOrder.z = SHIELD_Z_ORDER;

        CleanUpComponent cleanUp = engine.createComponent(CleanUpComponent.class);
        WorldWrapComponent worldWrap = engine.createComponent(WorldWrapComponent.class);

        Entity entity = engine.createEntity();
        entity.add(position);
        entity.add(dimension);
        entity.add(bounds);
        entity.add(movementComponent);
        entity.add(shield);
        entity.add(texture);
        entity.add(zOrder);
        entity.add(cleanUp);
        entity.add(worldWrap);
        engine.addEntity(entity);


    }
    public void createAmmo() {
        AmmoComponent ammo = engine.createComponent(AmmoComponent.class);

        DimensionComponent dimension = engine.createComponent(DimensionComponent.class);
        dimension.width = GameConfig.AMMO_SIZE;
        dimension.height = GameConfig.AMMO_SIZE;

        MovementComponentXYR movementComponent = engine.createComponent(MovementComponentXYR.class);
        movementComponent.ySpeed = GameConfig.AMMO_SPEED_X_MIN;

        //PIRATES SHIP MEASURES
        PositionComponent rocketPosition = rocketEntity.getComponent(PositionComponent.class);
        DimensionComponent rocketDimensions = rocketEntity.getComponent(DimensionComponent.class);


        PositionComponent position = engine.createComponent(PositionComponent.class);
        position.x = rocketPosition.x + rocketDimensions.width /2 - dimension.width/2;
        position.y = rocketPosition.y + rocketDimensions.height;
        //END PIRATES SHIP MEASURES
        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        bounds.rectangle.setPosition(position.x, position.y);
        bounds.rectangle.setSize(dimension.width, dimension.height);

        TextureComponent texture = engine.createComponent(TextureComponent.class);
        texture.region = gamePlayAtlas.findRegion(RegionNames.AMMO);

        ZOrderComponent zOrder = engine.createComponent(ZOrderComponent.class);
        zOrder.z = ROCKET_Z_ORDER;

        CleanUpComponent cleanUp = engine.createComponent(CleanUpComponent.class);


        Entity entity = engine.createEntity();
        entity.add(position);
        entity.add(dimension);
        entity.add(bounds);
        entity.add(movementComponent);
        entity.add(ammo);
        entity.add(texture);
        entity.add(zOrder);
        entity.add(cleanUp);
        engine.addEntity(entity);


    }

}


