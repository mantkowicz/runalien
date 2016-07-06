package com.apptogo.runalien.manager;

import com.apptogo.runalien.exception.ResourcesManagerException;
import com.apptogo.runalien.scene2d.AtlasRegionComparator;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

public class ResourcesManager {
	private static ResourcesManager INSTANCE;

	public static void create() {
		INSTANCE = new ResourcesManager();
	}

	public static void destroy() {
		INSTANCE.manager.clear();
		INSTANCE = null;
	}

	public static ResourcesManager getInstance() {
		return INSTANCE;
	}

	public AssetManager manager;
	public Skin skin;

	private ResourcesManager() {
		manager = new AssetManager();

		manager.load("logo.png", Texture.class);
		manager.finishLoading();
	}

	public void loadResources() {
		//textures
		manager.load("menu_atlas.pack", TextureAtlas.class);
		manager.load("intro_atlas.pack", TextureAtlas.class);
		manager.load("game_atlas.pack", TextureAtlas.class);
		manager.load("anim_atlas0.pack", TextureAtlas.class);
		manager.load("anim_atlas1.pack", TextureAtlas.class);
		manager.load("anim_atlas2.pack", TextureAtlas.class);
		manager.load("anim_atlas3.pack", TextureAtlas.class);
		manager.load("anim_atlas4.pack", TextureAtlas.class);
		manager.load("anim_atlas5.pack", TextureAtlas.class);
		manager.load("anim_atlas6.pack", TextureAtlas.class);
		manager.load("anim_atlas7.pack", TextureAtlas.class);
		manager.load("anim_atlas8.pack", TextureAtlas.class);
		manager.load("anim_atlas9.pack", TextureAtlas.class);
		manager.load("anim_atlas10.pack", TextureAtlas.class);
		manager.load("anim_atlas11.pack", TextureAtlas.class);
		manager.load("anim_atlas12.pack", TextureAtlas.class);
		manager.load("anim_atlas13.pack", TextureAtlas.class);
		manager.load("anim_atlas14.pack", TextureAtlas.class);
		manager.load("anim_atlas15.pack", TextureAtlas.class);
		manager.load("anim_atlas16.pack", TextureAtlas.class);
		manager.load("anim_atlas17.pack", TextureAtlas.class);
		
		manager.load("rozmycie.png", Texture.class);

		//sounds
		//TODO muzyka w podfolderze i funkcja do zwracania instancji (jak przy regionach)
		manager.load("scream.ogg", Sound.class);
		manager.load("slide.ogg", Sound.class);
		manager.load("jump.ogg", Sound.class);
		manager.load("doubleJump.ogg", Sound.class);
		manager.load("land.ogg", Sound.class);
		manager.load("chargeDown.ogg", Sound.class);
		manager.load("run.ogg", Sound.class);
		manager.load("bell.ogg", Sound.class);
		manager.load("die.ogg", Sound.class);
		manager.load("whoosh.ogg", Sound.class);
		manager.load("click.ogg", Sound.class);
		manager.load("rocket.ogg", Sound.class);
	}

	public void loadSkin() {
		manager.finishLoading();
		skin = new Skin(Gdx.files.internal("skin.json"), manager.get("menu_atlas.pack", TextureAtlas.class));
	}

	public AtlasRegion getAtlasRegion(String regionName) {
		AtlasRegion region = null;

		for (TextureAtlas atlas : manager.getAll(TextureAtlas.class, new Array<TextureAtlas>())) {
			region = atlas.findRegion(regionName);

			if (region != null) {
				break;
			}
		}
		if(region == null){
			throw new ResourcesManagerException("Couldn't find region: " + regionName);
		}
		return region;
	}

	public Array<AtlasRegion> getRegions(String pattern) {
		Array<AtlasRegion> regions = new Array<AtlasRegion>();

		//TODO handle pattern with same beginning sorting without numbers
		for (TextureAtlas atlas : manager.getAll(TextureAtlas.class, new Array<TextureAtlas>())) {
			for (AtlasRegion region : atlas.getRegions())
				if (region.name.startsWith(pattern))
					regions.add(region);
		}

		AtlasRegionComparator comparator = new AtlasRegionComparator();

		regions.sort(comparator);
		
		return regions;
	}

	public Sound getSound(String name) {
		return manager.get(name + ".ogg");
	}
	
	public <T> T get(String name) {
		return manager.get(name);
	}
}
