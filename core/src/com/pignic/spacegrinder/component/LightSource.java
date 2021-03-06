package com.pignic.spacegrinder.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.pignic.spacegrinder.screen.AbstractScreen;

import box2dLight.ConeLight;
import box2dLight.PositionalLight;
import box2dLight.RayHandler;

public class LightSource extends Configurable implements SerializableComponent {

	private float angle;

	private float arc;

	private Body body;

	private boolean built = false;

	private final Color color = new Color();

	private ConeLight light;

	private float maxArc;

	private float maxRange;

	private final Vector2 position = new Vector2();

	private float range;

	public LightSource() {

	}

	public LightSource(final Color color, final Body body, final float range, final float arc, final float maxRange,
			final float maxArc) {
		this(color, range, maxRange, maxArc);
		this.body = body;
		this.arc = arc;
	}

	private LightSource(final Color color, final float range, final float maxRange, final float maxArc) {
		this.color.set(color);
		this.range = range;
		this.maxArc = maxArc;
		this.maxRange = maxRange;
	}

	public LightSource(final Color color, final float range, final Vector2 position, final float angle, final float arc,
			final float maxRange, final float maxArc) {
		this(color, range, maxRange, maxArc);
		this.angle = angle;
		this.position.set(position);
		this.arc = arc;
	}

	public PositionalLight build(final RayHandler rayHandler) {
		if (built) {
			return light;
		}
		if (body != null) {
			light = new ConeLight(rayHandler, (int) Math.ceil((float) (arc / (Math.PI / 48d))), color, range,
					body.getWorldCenter().x, body.getWorldCenter().y, (float) Math.toDegrees(body.getAngle()),
					(float) Math.toDegrees(arc));
			light.attachToBody(body);
		} else {
			light = new ConeLight(rayHandler, (int) Math.ceil((float) (arc / (Math.PI / 48d))), color, range,
					position.x, position.y, (float) Math.toDegrees(angle), (float) Math.toDegrees(arc));
		}
		built = true;
		return light;
	}

	@Override
	public void deserialize(final Json json, final JsonValue jsonData) {
		angle = jsonData.getFloat("angle");
		arc = jsonData.getFloat("arc");
		maxArc = jsonData.getFloat("maxArc");
		maxRange = jsonData.getFloat("maxRange");
		range = jsonData.getFloat("range");
		position.x = jsonData.getFloat("x");
		position.y = jsonData.getFloat("y");
		color.r = jsonData.getFloat("r");
		color.g = jsonData.getFloat("g");
		color.b = jsonData.getFloat("b");
	}

	@Override
	public Table getConfiguration(final Table table) {
		final Label title = new Label("Light", AbstractScreen.style.skin, "light");
		table.add(title).row();
		final Label powerLabel = new Label("Power", AbstractScreen.style.skin, "light");
		final Slider powerSlider = new Slider(0f, maxRange, maxRange / 100f, false, AbstractScreen.style.skin);
		powerSlider.setValue(light.getDistance());
		powerSlider.addListener(new EventListener() {
			@Override
			public boolean handle(final Event event) {
				light.setDistance(powerSlider.getValue());
				return true;
			}
		});
		table.add(powerLabel);
		table.add(powerSlider).row();

		final Label redLabel = new Label("Red", AbstractScreen.style.skin, "light");
		final Slider redSlider = new Slider(0f, 1, 0.01f, false, AbstractScreen.style.skin);
		redSlider.setValue(light.getColor().r);
		redSlider.addListener(new EventListener() {
			@Override
			public boolean handle(final Event event) {
				light.getColor().r = redSlider.getValue();
				light.setColor(light.getColor());
				return true;
			}
		});
		table.add(redLabel);
		table.add(redSlider).row();

		final Label greenLabel = new Label("Green", AbstractScreen.style.skin, "light");
		final Slider greenSlider = new Slider(0f, 1, 0.01f, false, AbstractScreen.style.skin);
		greenSlider.setValue(light.getColor().g);
		greenSlider.addListener(new EventListener() {
			@Override
			public boolean handle(final Event event) {
				light.getColor().g = greenSlider.getValue();
				light.setColor(light.getColor());
				return true;
			}
		});
		table.add(greenLabel);
		table.add(greenSlider).row();

		final Label blueLabel = new Label("Blue", AbstractScreen.style.skin, "light");
		final Slider blueSlider = new Slider(0f, 1, 0.01f, false, AbstractScreen.style.skin);
		blueSlider.setValue(light.getColor().b);
		blueSlider.addListener(new EventListener() {
			@Override
			public boolean handle(final Event event) {
				light.getColor().b = blueSlider.getValue();
				light.setColor(light.getColor());
				return true;
			}
		});
		table.add(blueLabel);
		table.add(blueSlider).row();

		final Label arcLabel = new Label("Arc", AbstractScreen.style.skin, "light");
		final Slider arcSlider = new Slider(0f, maxArc, maxArc / 100f, false, AbstractScreen.style.skin);
		arcSlider.setValue(arc);
		arcSlider.addListener(new EventListener() {
			@Override
			public boolean handle(final Event event) {
				light.setConeDegree((float) Math.toDegrees(arcSlider.getValue()));
				arc = arcSlider.getValue();
				return true;
			}
		});
		table.add(arcLabel);
		table.add(arcSlider).row();
		return table;
	}

	public PositionalLight getLight() {
		return light;
	}

	public boolean isBuilt() {
		return built;
	}

	@Override
	public void serialize(final Json json) {
		json.writeValue("angle", angle);
		json.writeValue("arc", arc);
		json.writeValue("r", color.r);
		json.writeValue("g", color.g);
		json.writeValue("b", color.b);
		json.writeValue("maxArc", maxArc);
		json.writeValue("maxRange", maxRange);
		json.writeValue("x", position.x);
		json.writeValue("y", position.y);
		json.writeValue("range", range);
	}

}
