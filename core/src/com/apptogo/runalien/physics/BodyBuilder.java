package com.apptogo.runalien.physics;

import com.apptogo.runalien.main.Main;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class BodyBuilder {

	World world;
	
	BodyDef bodyDef;
	Array<FixtureDef> fixtureDefs = new Array<FixtureDef>();
	Array<UserData> fixtureDatas = new Array<UserData>();
	
	public static BodyBuilder get()
	{
		return new BodyBuilder(Main.getInstance().getGameScreen().getWorld());
	}
	
	public BodyBuilder(World world)
	{
		this.world = world;
		this.bodyDef = new BodyDef();
	}
		
	public BodyBuilder type(BodyType type)
	{
		bodyDef.type = type;
		return this;
	}
	
	public BodyBuilder position(float x, float y)
	{
		bodyDef.position.set(x, y);
		return this;
	}
		
	public BodyBuilder addFixture(String name)
	{
		fixtureDatas.add( new UserData() );
		fixtureDatas.peek().key = name;
		
		fixtureDefs.add(new FixtureDef());
		
		return this;
	}
	
	public BodyBuilder circle(float radius)
	{
		CircleShape circle = new CircleShape();
		circle.setRadius(radius);
		
		fixtureDefs.peek().shape = circle;
		
		fixtureDatas.peek().width = radius;
		fixtureDatas.peek().height = radius;
		
		return this;
	}
	
	public BodyBuilder box(float width, float height)
	{
		return box(width, height, 0, 0);
	}
	
	public BodyBuilder box(float width, float height, float offsetX, float offsetY)
	{
		PolygonShape polygon = new PolygonShape();
		polygon.setAsBox(width/2f, height/2f, new Vector2(offsetX, offsetY), 0);
		
		fixtureDefs.peek().shape = polygon;
		
		fixtureDatas.peek().width = width;
		fixtureDatas.peek().height = height;
		
		return this;
	}
	
	public BodyBuilder loop(float[] vertices)
	{
		ChainShape chain = new ChainShape();
		chain.createLoop(vertices);
		
		fixtureDefs.peek().shape = chain;
		
		//Calculating width/height (looking for min/max x and y in the vertices array)
		float min_x = vertices[0], max_x = vertices[0];
		float min_y = vertices[1], max_y = vertices[1];
		
		for(int i = 0; i < vertices.length/2; i++)
		{
			if(vertices[i] < min_x) min_x = vertices[i];
			if(vertices[i] > max_x) max_x = vertices[i];
			if(vertices[i + 1] < min_y) min_y = vertices[i];
			if(vertices[i + 1] > max_y) max_y = vertices[i];				
		}
			
		fixtureDatas.peek().width = max_x - min_x;
		fixtureDatas.peek().height = max_y - min_y;
		
		return this;
	}
	
	public BodyBuilder friction(float friction)
	{
		fixtureDefs.peek().friction = friction;
		return this;
	}
	
	public BodyBuilder restitution(float restitution)
	{
		fixtureDefs.peek().restitution = restitution;
		return this;
	}
	
	public BodyBuilder density(float density)
	{
		fixtureDefs.peek().density = density;
		return this;
	}
	
	public BodyBuilder sensor(boolean sensor)
	{
		fixtureDefs.peek().isSensor = sensor;
		return this;
	}
	
	public BodyBuilder ignore(boolean ignore)
	{
		fixtureDatas.peek().ignore = ignore;
		return this;
	}
	
	public BodyBuilder origin(float x, float y)
	{
		//set fixture position
		return this;
	}
		
	public Body create()
	{
		Body body = world.createBody(bodyDef);
		body.setUserData( fixtureDatas.first() );
		
		for(int i = 0; i < fixtureDefs.size; i++) {
			body.createFixture(fixtureDefs.get(i)).setUserData(fixtureDatas.get(i));
		}
		
		return body;
	}
}
