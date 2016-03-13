package com.apptogo.runalien.plugin;

import com.apptogo.runalien.screen.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

public class TouchSteering extends AbstractPlugin {

	protected boolean jumping = false;
	protected boolean doubleJumping = false;
	protected boolean sliding = false;
	
	protected SequenceAction standUpAction = Actions.sequence(); 
	private SoundHandler soundHandler;
	private Running running;

	protected void jump()
	{
		sliding = false;
		
		if(!jumping)
		{	
			this.body.setLinearVelocity(new Vector2(this.body.getLinearVelocity().x, 30));
			jumping = true;
			actor.changeAnimation("jump");

			soundHandler.pauseSound("scream");
			soundHandler.playSound("jump");
			
		}
		else
		{
			doubleJump();
		}
	}
	
	public void doubleJump()
	{
		if(!doubleJumping)
		{
			this.body.setLinearVelocity(new Vector2(this.body.getLinearVelocity().x, 25));
			doubleJumping = true;
			soundHandler.playSound("doubleJump");
		}
	}
	
	public void chargeDown()
	{
		if(jumping)
		{
			this.body.setLinearVelocity(body.getLinearVelocity().x, -35);
			soundHandler.playSound("chargeDown");
		}
		else
		{
			slide();
		}
	}
	
	public void slide()
	{		
		if(!sliding){
			actor.changeAnimation("slide");
			soundHandler.playSound("slide");
			soundHandler.pauseSound("scream");
		}
		
		sliding = true;
		
		actor.removeAction(standUpAction);
		
		standUpAction = Actions.sequence(Actions.delay(0.4f), Actions.run(new Runnable() {
							@Override
							public void run() {
								if(sliding)
									standUp();
						}}));
		
		actor.addAction(standUpAction);
	}
	
	public void standUp()
	{
		sliding = false;
		actor.changeAnimation("standup");
		actor.queueAnimation("run");
		
		soundHandler.playSound("scream");
	}
	
	public void land()
	{
		if(jumping || doubleJumping)
		{
			jumping = false;
			doubleJumping = false;
			actor.changeAnimation("land");
			actor.queueAnimation("run");
			
			soundHandler.playSound("land");
			soundHandler.playSound("scream");
		}
	}
	
	public void startRunning(){		
		running.setStarted(true);
	}
	
	@Override
	public void run() {
		if(soundHandler == null || running == null){
			soundHandler = actor.getPlugin(SoundHandler.class.getSimpleName());
			running = actor.getPlugin(Running.class.getSimpleName());
		}
		
		if(GameScreen.contactsSnapshot.containsKey("player") && GameScreen.contactsSnapshot.get("player").equals("ground"))
			land();
		
		if(running.isStarted()){
			if(Gdx.input.isKeyJustPressed(Keys.A))
				jump();
			if(Gdx.input.isKeyJustPressed(Keys.S))
				chargeDown();
		}
		if(Gdx.input.isKeyJustPressed(Keys.SPACE))
			startRunning();
			
	}

}
