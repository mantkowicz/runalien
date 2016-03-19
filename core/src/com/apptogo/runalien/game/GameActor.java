package com.apptogo.runalien.game;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.apptogo.runalien.exception.PluginException;
import com.apptogo.runalien.physics.UserData;
import com.apptogo.runalien.plugin.AbstractPlugin;
import com.apptogo.runalien.screen.GameScreen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool.Poolable;

public class GameActor extends AbstractActor implements Poolable, Serializable{
	private static final long serialVersionUID = -2249455026755833379L;
	
	private Body body;
	private float customOffsetX, customOffsetY;
	
	public GameActor(String name) {
		super(name);
	}

	Map<String, AbstractPlugin> plugins = new HashMap<String, AbstractPlugin>();

	@Override
	public void act(float delta) {
		super.act(delta);

		setCurrentAnimation();

		//we add customOffset to adjust animation position (and actor) with body to make game enjoyable
		//we add animation deltaOffset and few lines below we subtracting it. Thanks that actor and graphic is always in the same position.
		//more information about deltaOffset in AnimationActor

		setPosition(body.getPosition().x + customOffsetX + currentAnimation.getDeltaOffset().x, body.getPosition().y + customOffsetY + currentAnimation.getDeltaOffset().y);
		setSize(currentAnimation.getWidth(), currentAnimation.getHeight());

		currentAnimation.position(getX() - currentAnimation.getDeltaOffset().x, getY() - currentAnimation.getDeltaOffset().y);
		currentAnimation.act(delta);

		plugins.forEach((k, v) -> v.run());
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		currentAnimation.draw(batch, parentAlpha);
	}

	public void addPlugin(AbstractPlugin plugin) {
		plugin.setActor(this);
		plugins.put(plugin.getClass().getSimpleName(), plugin);
	}

	public void modifyCustomOffsets(float deltaX, float deltaY) {
		customOffsetX += deltaX;
		customOffsetY += deltaY;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;

		customOffsetX = -((UserData) body.getUserData()).width / 2f;
		customOffsetY = -((UserData) body.getUserData()).height / 2f;
	}
	
	/**
	 * @param plugin name. Always getSimpleName() of plugin class
	 * @return plugin
	 * @throws PluginException 
	 */
	public <T extends AbstractPlugin> T getPlugin(String name) throws PluginException{
		@SuppressWarnings("unchecked")
		T plugin = (T) plugins.get(name);
		if(plugin == null)
			throw new PluginException("Actor: '" + getName() + "' doesn't have plugin: '" + name);
		return plugin;
	}
	

	/* ----------- POOL STUFF ----------- */
	private boolean alive;
	
	@Override
	public void reset() {
		body.setTransform(new Vector2(-100, 0), 0);
		remove();
	}

	public void init(Vector2 position) {
		body.setTransform(position, 0);
		GameScreen.getGameworldStage().addActor(this);
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}
}
