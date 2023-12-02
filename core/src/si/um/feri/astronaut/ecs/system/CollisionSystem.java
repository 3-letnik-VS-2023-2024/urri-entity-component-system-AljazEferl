package si.um.feri.astronaut.ecs.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Intersector;

import si.um.feri.astronaut.common.GameManager;
import si.um.feri.astronaut.common.Mappers;
import si.um.feri.astronaut.ecs.component.AmmoComponent;
import si.um.feri.astronaut.ecs.component.RockComponent;
import si.um.feri.astronaut.ecs.component.TreasureComponent;
import si.um.feri.astronaut.ecs.component.BoundsComponent;
import si.um.feri.astronaut.ecs.component.ShipComponent;
import si.um.feri.astronaut.ecs.component.ShieldComponent;
import si.um.feri.astronaut.ecs.system.passive.SoundSystem;

public class CollisionSystem extends EntitySystem {

    private static final Family ROCKET_FAMILY = Family.all(ShipComponent.class, BoundsComponent.class).get();
    private static final Family ASTEROID_FAMILY = Family.all(RockComponent.class, BoundsComponent.class).get();
    private static final Family ASTRONAUT_FAMILY = Family.all(TreasureComponent.class, BoundsComponent.class).get();

    private static final Family SHIELD_FAMILY = Family.all(ShieldComponent.class, BoundsComponent.class).get();

    private static final Family AMMO_FAMILY = Family.all(AmmoComponent.class,BoundsComponent.class).get();
    private SoundSystem soundSystem;

    public CollisionSystem() {
    }

    @Override
    public void addedToEngine(Engine engine) {
        soundSystem = engine.getSystem(SoundSystem.class);
    }

    @Override
    public void update(float deltaTime) {
        if (GameManager.INSTANCE.isGameOver()) return;

        ImmutableArray<Entity> rockets = getEngine().getEntitiesFor(ROCKET_FAMILY);
        ImmutableArray<Entity> asteroids = getEngine().getEntitiesFor(ASTEROID_FAMILY);
        ImmutableArray<Entity> astronauts = getEngine().getEntitiesFor(ASTRONAUT_FAMILY);
        ImmutableArray<Entity> shields = getEngine().getEntitiesFor(SHIELD_FAMILY);
        ImmutableArray<Entity> ammos = getEngine().getEntitiesFor(AMMO_FAMILY);

        for (Entity rocketEntity : rockets) {
            BoundsComponent rocketBounds = Mappers.BOUNDS.get(rocketEntity);

            // check collision with asteroids
            for (Entity asteroidEntity : asteroids) {
                RockComponent asteroid = Mappers.ASTEROID.get(asteroidEntity);

                if (asteroid.hit) {
                    continue;
                }

                BoundsComponent asteroidBounds = Mappers.BOUNDS.get(asteroidEntity);
                for (Entity ammoEntity : ammos) {
                    AmmoComponent ammo = Mappers.AMMOS.get(ammoEntity);
                    BoundsComponent ammoBounds = Mappers.BOUNDS.get(ammoEntity);
                    if (Intersector.overlaps(asteroidBounds.rectangle, ammoBounds.rectangle)) {

                        asteroid.hit = true;
                        ammo.hit = true;
                        GameManager.INSTANCE.setHits(GameManager.INSTANCE.getHits()+1);
                        getEngine().removeEntity(asteroidEntity);
                        getEngine().removeEntity(ammoEntity);
                    }
                }


                    if(GameManager.INSTANCE.getShieldDuration() > 0){
                    if (Intersector.overlaps(rocketBounds.rectangle, asteroidBounds.rectangle)) {
                        asteroid.hit = true;
                        getEngine().removeEntity(asteroidEntity);
                    }
                }
                else if (GameManager.INSTANCE.getShieldDuration() <= 0 &&  Intersector.overlaps(rocketBounds.rectangle, asteroidBounds.rectangle)) {
                    asteroid.hit = true;
                    GameManager.INSTANCE.damage();
                    soundSystem.pickDamage();
                    getEngine().removeEntity(asteroidEntity);
                }
            }

            // check collision with astronauts
            for (Entity astronautEntity : astronauts) {
                TreasureComponent astronaut = Mappers.ASTRONAUTS.get(astronautEntity);

                if (astronaut.hit) {
                    continue;
                }

                BoundsComponent astronautBounds = Mappers.BOUNDS.get(astronautEntity);

                if (Intersector.overlaps(rocketBounds.rectangle, astronautBounds.rectangle)) {
                    astronaut.hit = true;
                    GameManager.INSTANCE.incResult();
                    soundSystem.pickLaugh();
                    getEngine().removeEntity(astronautEntity);
                }
            }
            float shieldDuration = 5.0f;

            //check collision with shield
            for (Entity shieldEntity : shields) {
                ShieldComponent shield = Mappers.SHIELDS.get(shieldEntity);
                if (shield.hit) {
                    continue;
                }
                BoundsComponent shieldBounds = Mappers.BOUNDS.get(shieldEntity);
                if (Intersector.overlaps(rocketBounds.rectangle, shieldBounds.rectangle)) {
                    shield.hit = true;
                    GameManager.INSTANCE.setDuration(shieldDuration);
                    getEngine().removeEntity(shieldEntity);
                }

                if(GameManager.INSTANCE.getShieldDuration() <= 0){
                    GameManager.INSTANCE.setDuration(0f);

                }
              //  System.out.println("Shield Duration: " + GameManager.INSTANCE.getShieldDuration());

            }

            }

        }
    }

