package si.um.feri.astronaut.common;

import com.badlogic.ashley.core.ComponentMapper;

import si.um.feri.astronaut.ecs.component.AmmoComponent;
import si.um.feri.astronaut.ecs.component.RockComponent;
import si.um.feri.astronaut.ecs.component.TreasureComponent;
import si.um.feri.astronaut.ecs.component.BoundsComponent;
import si.um.feri.astronaut.ecs.component.DimensionComponent;
import si.um.feri.astronaut.ecs.component.MovementComponentXYR;
import si.um.feri.astronaut.ecs.component.ParticleComponent;
import si.um.feri.astronaut.ecs.component.PositionComponent;
import si.um.feri.astronaut.ecs.component.ShipComponent;
import si.um.feri.astronaut.ecs.component.ShieldComponent;
import si.um.feri.astronaut.ecs.component.TextureComponent;
import si.um.feri.astronaut.ecs.component.ZOrderComponent;

//TODO Explain how Mappers work (see ComponentMapper and Entity implementation)
public final class Mappers {

    public static final ComponentMapper<RockComponent> ASTEROID =
            ComponentMapper.getFor(RockComponent.class);

    public static final ComponentMapper<TreasureComponent> ASTRONAUTS =
            ComponentMapper.getFor(TreasureComponent.class);

    public static final ComponentMapper<ShieldComponent> SHIELDS =
            ComponentMapper.getFor(ShieldComponent.class);

    public static final ComponentMapper<AmmoComponent> AMMOS =
            ComponentMapper.getFor(AmmoComponent.class);


    public static final ComponentMapper<BoundsComponent> BOUNDS =
            ComponentMapper.getFor(BoundsComponent.class);

    public static final ComponentMapper<DimensionComponent> DIMENSION =
            ComponentMapper.getFor(DimensionComponent.class);

    public static final ComponentMapper<MovementComponentXYR> MOVEMENT =
            ComponentMapper.getFor(MovementComponentXYR.class);

    public static final ComponentMapper<PositionComponent> POSITION =
            ComponentMapper.getFor(PositionComponent.class);

    public static final ComponentMapper<ShipComponent> ROCKET =
            ComponentMapper.getFor(ShipComponent.class);

    public static final ComponentMapper<TextureComponent> TEXTURE =
            ComponentMapper.getFor(TextureComponent.class);

    public static final ComponentMapper<ZOrderComponent> Z_ORDER =
            ComponentMapper.getFor(ZOrderComponent.class);

    public static final ComponentMapper<ParticleComponent> PARTICLE =
            ComponentMapper.getFor(ParticleComponent.class);
    private Mappers() {
    }
}
