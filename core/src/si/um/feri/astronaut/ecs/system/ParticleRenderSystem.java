package si.um.feri.astronaut.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import si.um.feri.astronaut.common.Mappers;
import si.um.feri.astronaut.ecs.component.ParticleComponent;
import si.um.feri.astronaut.ecs.component.PositionComponent;
import si.um.feri.astronaut.ecs.component.ZOrderComparator;
import si.um.feri.astronaut.ecs.component.ZOrderComponent;

public class ParticleRenderSystem extends SortedIteratingSystem {
    private static final Family PARTICLE_FAMILY = Family.all(
            ParticleComponent.class,
            PositionComponent.class,
            ZOrderComponent.class
    ).get();

    private final SpriteBatch batch;

    public ParticleRenderSystem(SpriteBatch batch) {
        super(PARTICLE_FAMILY, ZOrderComparator.INSTANCE);
        this.batch = batch;
    }

    @Override
    public void update(float deltaTime) {
        batch.begin();

        super.update(deltaTime);

        batch.end();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ParticleComponent particleComponent = Mappers.PARTICLE.get(entity);

        if (particleComponent != null && particleComponent.particle != null) {
            particleComponent.particle.draw(batch);
        } else {
            System.out.println("Invalid Particle Component");
        }
    }
}
