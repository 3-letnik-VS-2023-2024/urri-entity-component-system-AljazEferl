package si.um.feri.astronaut.ecs.system.passive;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;

public class StartUpSystem extends EntitySystem {

    private EntityFactorySystem factory;

    public StartUpSystem() {
        setProcessing(false);   // it doesn't call update
    }

    @Override
    public void addedToEngine(Engine engine) {
        factory = engine.getSystem(EntityFactorySystem.class);
        onStartUp();
    }

    private void onStartUp() {
        factory.createAsteroid();
        factory.createRocket();
        factory.createShield();
    }
}
