package com.ronan.andshmup;

import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

public class Laser extends Sprite 
{	
	private int mVelocity = 20;
	public int mDamage = 3;
	public boolean mAlive = false;
	private Scene mScene;

	public Laser(float pX, float pY, final TextureRegion pTextureRegion, Sound pLaserSound, final Scene pScene)
	{
		super(pX, pY, pTextureRegion);
		this.setScale(0.5f);
		this.setPosition(this.getX(), this.getY() - (this.getHeight()/2));
		mScene = pScene;
		pScene.attachChild(this);
		pLaserSound.play();
		mAlive = true;
	}
	
	public void update()
	{
		this.setPosition(this.getX() + mVelocity, this.getY());
		
		if (this.getX() > AndShmupActivity.CAMERA_WIDTH)
		{
			removeFromScene();
		}
	}//end update
	
	public void removeFromScene()
	{
		mScene.detachChild(this);
		mAlive = false;
	}
}