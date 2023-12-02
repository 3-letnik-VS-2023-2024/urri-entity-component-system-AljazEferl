package si.um.feri.astronaut.ecs.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import si.um.feri.astronaut.common.Mappers;
import si.um.feri.astronaut.config.GameConfig;
import si.um.feri.astronaut.ecs.component.MovementComponentXYR;
import si.um.feri.astronaut.ecs.component.ShipComponent;
import si.um.feri.astronaut.ecs.system.passive.EntityFactorySystem;


public class ShipInputSystem extends IteratingSystem {

    private EntityFactorySystem entityFactorySystem;

    private static final Family FAMILY = Family.all(
            ShipComponent.class,
            MovementComponentXYR.class
    ).get();

    public ShipInputSystem() {
        super(FAMILY);
    }

    // we don't need to override the update Iterating system method because there is no batch begin/end

    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        entityFactorySystem = engine.getSystem(EntityFactorySystem.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MovementComponentXYR movement = Mappers.MOVEMENT.get(entity);
        movement.xSpeed = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            movement.xSpeed = GameConfig.MAX_SHIP_X_SPEED * deltaTime;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            movement.xSpeed = -GameConfig.MAX_SHIP_X_SPEED * deltaTime;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            entityFactorySystem.createAmmo();
        }

    }
}
