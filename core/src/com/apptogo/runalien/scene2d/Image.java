package com.apptogo.runalien.scene2d;

import com.apptogo.runalien.manager.ResourcesManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Scaling;

public class Image extends com.badlogic.gdx.scenes.scene2d.ui.Image {

    private TextureRegion region;

    public static Image get(String regionName) {
        return new Image(ResourcesManager.getInstance().getAtlasRegion(regionName));
    }

    public static Image get(Texture texture) {
        return new Image(texture);
    }

    public Image(TextureRegion region) {
        super(region);
    }

    public Image(Texture region) {
        super(region);
        this.region = new TextureRegion(region);
    }

    public Image position(float x, float y)
    {
        this.setPosition(x, y);
        return this;
    }

    public Image size(float width, float height)
    {System.out.println(width + " " + height);
        this.setSize(width, height);

        return this;
    }

    public Image centerX()
    {
        this.setPosition(-this.getWidth() / 2f, this.getY());
        return this;
    }

    public Image centerX(float offset)
    {
        this.setPosition(-this.getWidth() / 2f + offset, this.getY());
        return this;
    }

    public Image centerY()
    {
        this.setPosition(this.getX(), -this.getHeight() / 2f);
        return this;
    }

    public Image centerY(float offset)
    {
        this.setPosition(this.getX(), -this.getHeight() / 2f + offset);
        return this;
    }

    public Image visible(boolean visible)
    {
        setVisible(visible);
        return this;
    }
    
    public Image scale(float scale)
    {
    	setSize(getWidth()*scale, getHeight()*scale);
    	return this;
    }

    public TextureRegion getRegion() {
        return region;
    }
}