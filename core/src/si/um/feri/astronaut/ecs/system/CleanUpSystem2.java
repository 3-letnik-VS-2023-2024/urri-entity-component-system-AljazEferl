package si.um.feri.astronaut.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import si.um.feri.astronaut.common.Mappers;
import si.um.feri.astronaut.config.GameConfig;
import si.um.feri.astronaut.ecs.component.CleanUpComponent;
import si.um.feri.astronaut.ecs.component.CleanUpComponent2;
import si.um.feri.astronaut.ecs.component.DimensionComponent;
import si.um.feri.astronaut.ecs.component.PositionComponent;

public class CleanUpSystem2 extends IteratingSystem {
    private static final Family FAMILY = Family.all(
            CleanUpComponent2.class,
            PositionComponent.class,
            DimensionComponent.class
    ).get();

    public CleanUpSystem2() {
        super(FAMILY);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = Mappers.POSITION.get(entity);
        DimensionComponent dimension = Mappers.DIMENSION.get(entity);

        if (position.y > GameConfig.HEIGHT) {
            getEngine().removeEntity(entity);
        }
    }
}
