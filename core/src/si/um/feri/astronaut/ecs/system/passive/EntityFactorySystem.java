package si.um.feri.astronaut.ecs.system.passive;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;

import si.um.feri.astronaut.assets.AssetDescriptors;
import si.um.feri.astronaut.assets.RegionNames;
import si.um.feri.astronaut.config.GameConfig;
import si.um.feri.astronaut.ecs.component.AmmoComponent;
import si.um.feri.astronaut.ecs.component.ParticleComponent2;
import si.um.feri.astronaut.ecs.component.RockComponent;
import si.um.feri.astronaut.ecs.component.TreasureComponent;
import si.um.feri.astronaut.ecs.component.BoundsComponent;
import si.um.feri.astronaut.ecs.component.CleanUpComponent;
import si.um.feri.astronaut.ecs.component.CleanUpComponent2;
import si.um.feri.astronaut.ecs.component.DimensionComponent;
import si.um.feri.astronaut.ecs.component.MovementComponentXYR;
import si.um.feri.astronaut.ecs.component.ParticleComponent;
import si.um.feri.astronaut.ecs.component.PositionComponent;
import si.um.feri.astronaut.ecs.component.ShipComponent;
import si.um.feri.astronaut.ecs.component.ShieldComponent;
import si.um.feri.astronaut.ecs.component.TextureComponent;
import si.um.feri.astronaut.ecs.component.WorldWrapComponent;
import si.um.feri.astronaut.ecs.component.ZOrderComponent;

public class EntityFactorySystem extends EntitySystem {

    private static final int BACKGROUND_Z_ORDER = 1;
    private static final int ROCK_Z_ORDER = 2;
    private static final int TREASURE_Z_ORDER = 3;
    private static final int SHIELD_Z_ORDER =4;


    private static final int PIRATESHIP_Z_ORDER = 5;

    private final AssetManager assetManager;

    private PooledEngine engine;
    private TextureAtlas gamePlayAtlas;
    private Entity shipEntity;
    private Entity treasureEntity;

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
    public void createBackground(){

        PositionComponent position = engine.createComponent(PositionComponent.class);
        position.x = 0;
        position.y = 0;

        DimensionComponent dimension = engine.createComponent(DimensionComponent.class);
        dimension.width = GameConfig.WIDTH;
        dimension.height = GameConfig.HEIGHT;

        TextureComponent texture = engine.createComponent(TextureComponent.class);
        texture.region = gamePlayAtlas.findRegion(RegionNames.BACKGROUND);

        ZOrderComponent zOrder = engine.createComponent(ZOrderComponent.class);
        zOrder.z = BACKGROUND_Z_ORDER;
        Entity entity = engine.createEntity();
        entity.add(position);
        entity.add(dimension);
        entity.add(texture);
        entity.add(zOrder);
        engine.addEntity(entity);

    }

    public void createShip() {
        PositionComponent position = engine.createComponent(PositionComponent.class);
        position.x = GameConfig.WIDTH / 2f - GameConfig.SHIP_WIDTH / 2f;
        position.y = 20;

        DimensionComponent dimension = engine.createComponent(DimensionComponent.class);
        dimension.width = GameConfig.SHIP_WIDTH;
        dimension.height = GameConfig.SHIP_HEIGHT;

        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        bounds.rectangle.setPosition(position.x, position.y);
        bounds.rectangle.setSize(dimension.width, dimension.height);

        MovementComponentXYR movement = engine.createComponent(MovementComponentXYR.class);

        ShipComponent rocket = engine.createComponent(ShipComponent.class);

        WorldWrapComponent worldWrap = engine.createComponent(WorldWrapComponent.class);

        TextureComponent texture = engine.createComponent(TextureComponent.class);
        texture.region = gamePlayAtlas.findRegion(RegionNames.PIRATE_SHIP);

        ZOrderComponent zOrder = engine.createComponent(ZOrderComponent.class);
        zOrder.z = PIRATESHIP_Z_ORDER;

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
        shipEntity = entity;
    }
    public void createParticleShip() {
        ParticleComponent particleComponent = engine.createComponent(ParticleComponent.class);
        particleComponent.particle = assetManager.get(AssetDescriptors.WATER_PARTICLE_EFFECT);

        if (particleComponent.particle != null) {
            DimensionComponent dimension = engine.createComponent(DimensionComponent.class);
            dimension.width = GameConfig.TREASURE_SIZE;
            dimension.height = GameConfig.TREASURE_SIZE;

            PositionComponent rocketPosition = engine.createComponent(PositionComponent.class);
            DimensionComponent rocketDimensions = engine.createComponent(DimensionComponent.class);


            PositionComponent position = engine.createComponent(PositionComponent.class);
            position.x = rocketPosition.x; //+ rocketDimensions.width / 2;
            position.y = rocketPosition.y; //+ 5;

            particleComponent.particle.setPosition(position.x, position.y);
            particleComponent.particle.flipY();

            ZOrderComponent zOrder = engine.createComponent(ZOrderComponent.class);
            zOrder.z = 7;

           /* BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
            bounds.rectangle.setPosition(position.x, position.y);
            bounds.rectangle.setSize(rocketDimensions.width, rocketDimensions.height);*/

            CleanUpComponent cleanUp = engine.createComponent(CleanUpComponent.class);

            Entity entity = engine.createEntity();
            entity.add(position);
            entity.add(dimension);
            entity.add(particleComponent);
            entity.add(zOrder);
            entity.add(cleanUp);
           // entity.add(bounds);
            engine.addEntity(entity);
        }
    }


    public void createRock() {
        PositionComponent position = engine.createComponent(PositionComponent.class);

        position.x = MathUtils.random(0, GameConfig.WIDTH - GameConfig.ROCK_WIDTH);
        position.y = GameConfig.HEIGHT;

        MovementComponentXYR movementComponent = engine.createComponent(MovementComponentXYR.class);
        movementComponent.xSpeed = -GameConfig.ROCK_SPEED_X_MIN * MathUtils.random(-1f, 1f);
        movementComponent.ySpeed = -GameConfig.ROCK_SPEED_X_MIN * MathUtils.random(1f, 2f);
        movementComponent.rSpeed = MathUtils.random(-1f, 1f);

        float randFactor = MathUtils.random(1f, 4f);
        DimensionComponent dimension = engine.createComponent(DimensionComponent.class);
        dimension.width = GameConfig.ROCK_WIDTH * randFactor;
        dimension.height = GameConfig.ROCK_HEIGHT * randFactor;

        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        bounds.rectangle.setPosition(position.x, position.y);
        bounds.rectangle.setSize(dimension.width, dimension.height);

        RockComponent rockComponent = engine.createComponent(RockComponent.class);

        TextureComponent texture = engine.createComponent(TextureComponent.class);
        texture.region = gamePlayAtlas.findRegion(RegionNames.ROCK);

        ZOrderComponent zOrder = engine.createComponent(ZOrderComponent.class);
        zOrder.z = ROCK_Z_ORDER;

         WorldWrapComponent worldWrap = engine.createComponent(WorldWrapComponent.class);

        CleanUpComponent cleanUp = engine.createComponent(CleanUpComponent.class);

        Entity entity = engine.createEntity();
        entity.add(position);
        entity.add(dimension);
        entity.add(bounds);
        entity.add(movementComponent);
        entity.add(rockComponent);
        entity.add(texture);
        entity.add(zOrder);
        entity.add(worldWrap);
        entity.add(cleanUp);
        engine.addEntity(entity);
    }

    public void createTreasure() {
        PositionComponent position = engine.createComponent(PositionComponent.class);
        position.x = MathUtils.random(0, GameConfig.WIDTH - GameConfig.TREASURE_SIZE);
        position.y = GameConfig.HEIGHT;

        MovementComponentXYR movementComponent = engine.createComponent(MovementComponentXYR.class);
        movementComponent.xSpeed = GameConfig.ROCK_SPEED_X_MIN * MathUtils.random(-0.1f, 0.1f);
        movementComponent.ySpeed = -GameConfig.ROCK_SPEED_X_MIN * MathUtils.random(1f, 2f);
        movementComponent.rSpeed = MathUtils.random(-1f, 1f);

        TreasureComponent astronaut = engine.createComponent(TreasureComponent.class);

        DimensionComponent dimension = engine.createComponent(DimensionComponent.class);
        dimension.width = GameConfig.TREASURE_SIZE;
        dimension.height = GameConfig.TREASURE_SIZE;

        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        bounds.rectangle.setPosition(position.x, position.y);
        bounds.rectangle.setSize(dimension.width, dimension.height);

        TextureComponent texture = engine.createComponent(TextureComponent.class);
        texture.region = gamePlayAtlas.findRegion(RegionNames.TREASURE);

        ZOrderComponent zOrder = engine.createComponent(ZOrderComponent.class);
        zOrder.z = TREASURE_Z_ORDER;

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
        treasureEntity = entity;
    }

    public void createParticleTreasure() {
        ParticleComponent2 particleComponent = engine.createComponent(ParticleComponent2.class);
        particleComponent.particle = assetManager.get(AssetDescriptors.FIRE_PARTICLE_EFFECT);

        DimensionComponent dimension = engine.createComponent(DimensionComponent.class);
        dimension.width = GameConfig.TREASURE_SIZE;
        dimension.height = GameConfig.TREASURE_SIZE;

        PositionComponent treasurePosition = engine.createComponent(PositionComponent.class);
        DimensionComponent treasureDimensions = engine.createComponent(DimensionComponent.class);


        PositionComponent position = engine.createComponent(PositionComponent.class);
        position.x = treasurePosition.x; //+ rocketDimensions.width / 2;
        position.y = treasurePosition.y; //+ 5;

        particleComponent.particle.setPosition(0,0);//(position.x, position.y);


        ZOrderComponent zOrder = engine.createComponent(ZOrderComponent.class);
        zOrder.z = 7;


        CleanUpComponent cleanUp = engine.createComponent(CleanUpComponent.class);

        Entity entity = engine.createEntity();
        entity.add(position);
        entity.add(dimension);
        entity.add(particleComponent);
        entity.add(zOrder);
        entity.add(cleanUp);
        // entity.add(bounds);
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
        PositionComponent rocketPosition = shipEntity.getComponent(PositionComponent.class);
        DimensionComponent rocketDimensions = shipEntity.getComponent(DimensionComponent.class);


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
        zOrder.z = PIRATESHIP_Z_ORDER;

        CleanUpComponent2 cleanUp2 = engine.createComponent(CleanUpComponent2.class);


        Entity entity = engine.createEntity();
        entity.add(position);
        entity.add(dimension);
        entity.add(bounds);
        entity.add(movementComponent);
        entity.add(ammo);
        entity.add(texture);
        entity.add(zOrder);
        entity.add(cleanUp2);
        engine.addEntity(entity);


    }

}


