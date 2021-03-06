package com.pignic.spacegrinder.factory.complex;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.pignic.spacegrinder.AssetManager;
import com.pignic.spacegrinder.Constants;
import com.pignic.spacegrinder.component.Physical;
import com.pignic.spacegrinder.component.Position;
import com.pignic.spacegrinder.component.Projectile;
import com.pignic.spacegrinder.component.Renderable;
import com.pignic.spacegrinder.pojo.Weapon.Damage;

public class ProjectileFactory {

	public static Entity buildProjectile(final World world, final Body emitter, final float speed, final float range,
			final float damage, final float projectileSize, final Damage[] damages) {
		final Entity entity = new Entity();
		final BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position
				.add(new Vector2(emitter.getWorldCenter()).add(emitter.getWorldVector(new Vector2(1, 0).scl(2))));
		bodyDef.angle = emitter.getAngle();
		bodyDef.bullet = true;
		final CircleShape shape = new CircleShape();
		shape.setRadius(projectileSize);
		final FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = 1;
		fixtureDef.shape = shape;
		final Physical physical = new Physical(world, entity, bodyDef, fixtureDef);
		physical.getBody().applyLinearImpulse(physical.getBody().getWorldVector(new Vector2(1, 0).scl(speed)),
				physical.getBody().getWorldCenter(), true);
		emitter.applyLinearImpulse(emitter.getWorldVector(new Vector2(-1, 0)).scl(speed), emitter.getWorldCenter(),
				true);
		physical.getBody().getFixtureList().get(0).setUserData(emitter);
		entity.add(physical);
		entity.add(new Projectile((Entity) emitter.getUserData(), range, speed, damage, damages));
		entity.add(new Position());
		entity.add(new Renderable(AssetManager.getInstance().getTexture(Constants.TEXTURE_PATH + "bullet.png")));
		return entity;
	}
}
