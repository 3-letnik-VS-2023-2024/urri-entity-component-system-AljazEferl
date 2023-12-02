package si.um.feri.astronaut.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import si.um.feri.astronaut.common.Mappers;
import si.um.feri.astronaut.ecs.component.BoundsComponent;
import si.um.feri.astronaut.ecs.component.DimensionComponent;
import si.um.feri.astronaut.ecs.component.ParticleComponent;
import si.um.feri.astronaut.ecs.component.PositionComponent;
import si.um.feri.astronaut.ecs.component.ShipComponent;

public class ParticleSystem extends IteratingSystem {
    private static final Family SHIP_FAMILY = Family.all(ShipComponent.class, BoundsComponent.class).get();


    private static final Family FAMILY = Family.all(
            ParticleComponent.class,
            PositionComponent.class
    ).get();

    public ParticleSystem() {
        super(FAMILY);
    }
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = Mappers.POSITION.get(entity);
        DimensionComponent dimension = Mappers.DIMENSION.get(entity);
        Entity ship = getEngine().getEntitiesFor(SHIP_FAMILY).first();
        dimension.width = ship.getComponent(DimensionComponent.class).width;
        position.x = ship.getComponent(PositionComponent.class).x + dimension.width / 2;
        position.y = ship.getComponent(PositionComponent.class).y+5;

        entity.getComponent(ParticleComponent.class).particle.setPosition(position.x, position.y);
        entity.getComponent(ParticleComponent.class).particle.update(deltaTime);
        if(entity.getComponent(ParticleComponent.class).particle.isComplete()){
            entity.getComponent(ParticleComponent.class).particle.reset();
        }
    }
}
