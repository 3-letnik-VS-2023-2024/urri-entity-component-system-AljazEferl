package si.um.feri.astronaut.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;

import si.um.feri.astronaut.common.Mappers;
import si.um.feri.astronaut.ecs.component.BoundsComponent;
import si.um.feri.astronaut.ecs.component.DimensionComponent;
import si.um.feri.astronaut.ecs.component.ParticleComponent;
import si.um.feri.astronaut.ecs.component.ParticleComponent2;
import si.um.feri.astronaut.ecs.component.PositionComponent;
import si.um.feri.astronaut.ecs.component.ShipComponent;
import si.um.feri.astronaut.ecs.component.TreasureComponent;

public class ParticleSystem2 extends IteratingSystem {
    private static final Family TREASURE_FAMILY = Family.all(TreasureComponent.class, BoundsComponent.class).get();


    private static final Family FAMILY = Family.all(
            ParticleComponent2.class,
            PositionComponent.class
    ).get();

    public ParticleSystem2() {
        super(FAMILY);
    }
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = Mappers.POSITION.get(entity);
        DimensionComponent dimension = Mappers.DIMENSION.get(entity);

        // Get all entities that belong to the TREASURE_FAMILY
        ImmutableArray<Entity> treasureEntities = getEngine().getEntitiesFor(TREASURE_FAMILY);

        // Iterate over all treasure entities
        for (Entity treasureEntity : treasureEntities) {
            dimension.width = treasureEntity.getComponent(DimensionComponent.class).width;
            position.x = treasureEntity.getComponent(PositionComponent.class).x + dimension.width / 2;
            position.y = treasureEntity.getComponent(PositionComponent.class).y;

            entity.getComponent(ParticleComponent2.class).particle.setPosition(position.x, position.y);
            entity.getComponent(ParticleComponent2.class).particle.update(deltaTime);
            if(entity.getComponent(ParticleComponent2.class).particle.isComplete()){
                entity.getComponent(ParticleComponent2.class).particle.reset();
            }
        }
    }

}
