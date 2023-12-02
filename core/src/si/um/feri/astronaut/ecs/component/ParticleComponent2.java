package si.um.feri.astronaut.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.utils.Pool;

public class ParticleComponent2 implements Component, Pool.Poolable {
    public ParticleEffect particle;
    @Override
    public void reset() {
        if (particle != null) {
            particle.dispose();
        }
        particle = null;
    }
}

