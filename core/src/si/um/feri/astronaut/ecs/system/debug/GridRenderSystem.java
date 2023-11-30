package si.um.feri.astronaut.ecs.system.debug;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;

import si.um.feri.astronaut.ecs.system.debug.support.ViewportUtils;

public class GridRenderSystem extends EntitySystem {

    private final Viewport viewport;
    private final ShapeRenderer renderer;

    public GridRenderSystem(Viewport viewport, ShapeRenderer renderer) {
        this.viewport = viewport;
        this.renderer = renderer;
    }

    @Override
    public void update(float deltaTime) {
        viewport.apply();
        ViewportUtils.drawGrid(viewport, renderer);
    }
}
