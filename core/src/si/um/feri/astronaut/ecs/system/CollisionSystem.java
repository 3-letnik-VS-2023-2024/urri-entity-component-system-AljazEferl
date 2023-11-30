package si.um.feri.astronaut.ecs.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Intersector;

import si.um.feri.astronaut.common.GameManager;
import si.um.feri.astronaut.common.Mappers;
import si.um.feri.astronaut.ecs.component.AsteroidComponent;
import si.um.feri.astronaut.ecs.component.AstronautComponent;
import si.um.feri.astronaut.ecs.component.BoundsComponent;
import si.um.feri.astronaut.ecs.component.RocketComponent;
import si.um.feri.astronaut.ecs.component.ShieldComponent;
import si.um.feri.astronaut.ecs.system.passive.SoundSystem;

public class CollisionSystem extends EntitySystem {

    private static final Family ROCKET_FAMILY = Family.all(RocketComponent.class, BoundsComponent.class).get();
    private static final Family ASTEROID_FAMILY = Family.all(AsteroidComponent.class, BoundsComponent.class).get();
    private static final Family ASTRONAUT_FAMILY = Family.all(AstronautComponent.class, BoundsComponent.class).get();

    private static final Family SHIELD_FAMILY = Family.all(ShieldComponent.class, BoundsComponent.class).get();
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

        for (Entity rocketEntity : rockets) {
            BoundsComponent rocketBounds = Mappers.BOUNDS.get(rocketEntity);

            // check collision with asteroids
            for (Entity asteroidEntity : asteroids) {
                AsteroidComponent asteroid = Mappers.ASTEROID.get(asteroidEntity);

                if (asteroid.hit) {
                    continue;
                }

                BoundsComponent asteroidBounds = Mappers.BOUNDS.get(asteroidEntity);
                if(GameManager.INSTANCE.getShield() == true){
                    if (Intersector.overlaps(rocketBounds.rectangle, asteroidBounds.rectangle)) {
                        asteroid.hit = true;
                        getEngine().removeEntity(asteroidEntity);
                    }
                }
                else if (!GameManager.INSTANCE.getShield() && Intersector.overlaps(rocketBounds.rectangle, asteroidBounds.rectangle)) {
                    asteroid.hit = true;
                    GameManager.INSTANCE.damage();
                    soundSystem.pickDamage();
                    getEngine().removeEntity(asteroidEntity);
                }
            }

            // check collision with astronauts
            for (Entity astronautEntity : astronauts) {
                AstronautComponent astronaut = Mappers.ASTRONAUTS.get(astronautEntity);

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
                    GameManager.INSTANCE.setShield(true);
                    shield.duration = shieldDuration;
                    getEngine().removeEntity(shieldEntity);
                }
                shield.duration -= deltaTime;
                if (shield.duration <= 0) {
                    GameManager.INSTANCE.setShield(false);
                }
            }



            }

        }
    }

