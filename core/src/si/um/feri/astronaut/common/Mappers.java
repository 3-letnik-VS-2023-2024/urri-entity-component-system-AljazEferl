package si.um.feri.astronaut.common;

import com.badlogic.ashley.core.ComponentMapper;

import si.um.feri.astronaut.ecs.component.AsteroidComponent;
import si.um.feri.astronaut.ecs.component.AstronautComponent;
import si.um.feri.astronaut.ecs.component.BoundsComponent;
import si.um.feri.astronaut.ecs.component.DimensionComponent;
import si.um.feri.astronaut.ecs.component.MovementComponentXYR;
import si.um.feri.astronaut.ecs.component.PositionComponent;
import si.um.feri.astronaut.ecs.component.RocketComponent;
import si.um.feri.astronaut.ecs.component.ShieldComponent;
import si.um.feri.astronaut.ecs.component.TextureComponent;
import si.um.feri.astronaut.ecs.component.ZOrderComponent;

//TODO Explain how Mappers work (see ComponentMapper and Entity implementation)
public final class Mappers {

    public static final ComponentMapper<AsteroidComponent> ASTEROID =
            ComponentMapper.getFor(AsteroidComponent.class);

    public static final ComponentMapper<AstronautComponent> ASTRONAUTS =
            ComponentMapper.getFor(AstronautComponent.class);

    public static final ComponentMapper<ShieldComponent> SHIELDS =
            ComponentMapper.getFor(ShieldComponent.class);

    public static final ComponentMapper<BoundsComponent> BOUNDS =
            ComponentMapper.getFor(BoundsComponent.class);

    public static final ComponentMapper<DimensionComponent> DIMENSION =
            ComponentMapper.getFor(DimensionComponent.class);

    public static final ComponentMapper<MovementComponentXYR> MOVEMENT =
            ComponentMapper.getFor(MovementComponentXYR.class);

    public static final ComponentMapper<PositionComponent> POSITION =
            ComponentMapper.getFor(PositionComponent.class);

    public static final ComponentMapper<RocketComponent> ROCKET =
            ComponentMapper.getFor(RocketComponent.class);

    public static final ComponentMapper<TextureComponent> TEXTURE =
            ComponentMapper.getFor(TextureComponent.class);

    public static final ComponentMapper<ZOrderComponent> Z_ORDER =
            ComponentMapper.getFor(ZOrderComponent.class);

    private Mappers() {
    }
}
