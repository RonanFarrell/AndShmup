package com.ronan.andshmup;

import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

public class Enemy extends Sprite {

	private Scene mScene;
	private float mAngle = 0;
	private float mAngleVel = 0;
	private float mVelocity = 2.0f;
	private AnimatedSprite mExplosion;
	private long[] explosionDuration = new long[] {50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 100, 100};
	public boolean mExploded = false;
	private float mExplosionScale = 2.0f;
	public boolean mDead = false;
	private Sound mExplosionSound;
	
	private int mHealth = 6;
	
	public static enum Type {Mine, Asteroid};
	public Type type;
	
	
	/**
	 * Constructor for mine object
	 * @param pX
	 * @param pY
	 * @param pTextureRegion
	 * @param pExplosionTexture
	 * @param pScene
	 * @param pExplosionSound
	 */
	public Enemy(float pX, float pY, TextureRegion pTextureRegion, TiledTextureRegion pExplosionTexture, final Scene pScene, Sound pExplosionSound) {
		super(pX, pY, pTextureRegion);
		mScene = pScene;
		pScene.attachChild(this);
		mExplosion = new AnimatedSprite(0, 0, pExplosionTexture);
		mExplosion.setScale(4.0f);
		mExplosionSound = pExplosionSound;
		
		mHealth = 1;
		
		type = Type.Mine;
	}
	
	
	/**
	 * Constructor for an asteroid
	 * @param pX
	 * @param pY
	 * @param pScale
	 * @param pTextureRegion
	 * @param pExplosionTexture
	 * @param pScene
	 * @param pExplosionSound
	 */
	public Enemy(float pX, float pY, float pScale, TextureRegion pTextureRegion, TiledTextureRegion pExplosionTexture, final Scene pScene, Sound pExplosionSound) {
		super(pX, pY, pTextureRegion);
		mScene = pScene;
		pScene.attachChild(this);
		
		if (pScale < 0.7f)
		{
			mAngleVel = 3.0f;
			mHealth = 3;
			mVelocity = 3.0f;
		}
		else if(pScale < 1.2f)
		{
			mAngleVel = 2.0f;
			mHealth = 6;
			
		}
		else
		{
			mAngleVel = 1.0f;
			mHealth = 9;
			mVelocity = 1.0f;
		}
		
		
		this.setScale(pScale);
		mExplosion = new AnimatedSprite(0, 0, pExplosionTexture);
		mExplosion.setScale(mExplosionScale);
		mExplosionSound = pExplosionSound;
		
		type = Type.Asteroid;
	}
	
	public void update(){
		this.setPosition(this.getX() - mVelocity, this.getY());
		
		this.setRotation(mAngle);
		if (this.getX() < 0){
			mDead = true;
		}
		mAngle += mAngleVel;
		
		
		cleanUpExplosion();
	}
	
	// Creates the explosion animation
	// Hides the enemy sprite
	private void explode()
	{
		if (!mExploded)
		{
			mExplosion.setPosition(this);
			mExplosion.animate(explosionDuration, 0, 16, false);
			mScene.attachChild(mExplosion);
			mExploded = true;
			this.setVisible(false);
			mExplosionSound.play();
		}
	}
	
	public boolean doDamage(int damage)
	{
		mHealth -= damage;
		
		if (mHealth <= 0)
		{
			explode();
			return true;
		}
		
		return false;
	}
	
	// Removes the explosion from the scene
	// when the animation finishes.
	private void cleanUpExplosion()
	{
		if (mExploded)
		{
			if (!mExplosion.isAnimationRunning())
			{
				mScene.detachChild(mExplosion);
				mDead = true;
			}
		}
	}
	
}