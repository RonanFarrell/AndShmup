package com.ronan.andshmup;

import java.util.ArrayList;

import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

public class Player extends Sprite {
	
	public Sprite mThrusterLeft;
	public Sprite mThrusterRight;
	
	private ArrayList<Laser> mLasers;
	
	public int score = 0;
	public int combo = 1;
	private int consecutiveEnemiesKilled = 0;
	public int lives; 
	
	private Scene mScene;
	private Engine mEngine;
	
	private ArrayList<Sprite> mLives;
	
	private Sound mLaserSound;
	private TextureRegion mLaserRegion;
	
	public int enemiesDestroyed = 0;
	public int lasersFired = 0;
	public int highestCombo = 1;
	public int lasersHit = 0;
	
	private TimerHandler mShootTimer;

	/**
	 * @param pX
	 * @param pY
	 * @param pTextureRegion
	 * @param pThruster
	 * @param pLaserRegion
	 * @param pHeart
	 * @param pLaserSound
	 * @param pScene
	 */
	public Player(float pX, float pY, TextureRegion pTextureRegion, TextureRegion pThruster, TextureRegion pLaserRegion,
			TextureRegion pHeart, Sound pLaserSound, final Scene pScene, Engine pEngine, int pScore, int pLives)
	{
		super(pX, pY, pTextureRegion);
		
		mThrusterLeft = new Sprite(this.getX() - pThruster.getWidth(), this.getY(), pThruster);
		
		mThrusterRight = new Sprite(this.getX() - pThruster.getWidth(), this.getY() + this.getHeight() - pThruster.getHeight(), pThruster);
		
		mThrusterLeft.setAlpha(0.4f);
		mThrusterRight.setAlpha(0.4f);
		
		pScene.registerTouchArea(this);
		
		pScene.attachChild(this);
		pScene.attachChild(mThrusterLeft);
		pScene.attachChild(mThrusterRight);
		
		mLasers = new ArrayList<Laser>();
		
		mLives = new ArrayList<Sprite>();
		
		for (int i = 0; i < pLives; i++)
		{
			Sprite tmp = new Sprite(90 + (18*i), 5, pHeart);
			pScene.attachChild(tmp);
			mLives.add(tmp);
		}
		
		lives = mLives.size();
		
		mScene = pScene;
		mLaserSound = pLaserSound;
		mLaserRegion = pLaserRegion;
		score = pScore;
		
		mEngine = pEngine;
		
		mShootTimer = new TimerHandler(1.7f, new ITimerCallback() {

			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				pTimerHandler.reset();
				shoot();
			}//end onTimePassed

		});
		
		mEngine.registerUpdateHandler(mShootTimer);
		
	}//end player constructor
	
	@Override		
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {			
		
		
		
		this.setPosition(this.getX(), pSceneTouchEvent.getY() - this.getHeight() / 2);
		
		if (this.getY() < 0 ) {
			this.setPosition(this.getX(), 0);
		}
		else if (this.getY() > AndShmupActivity.CAMERA_HEIGHT - this.getHeight()) {				
			this.setPosition(this.getX(), AndShmupActivity.CAMERA_HEIGHT - this.getHeight());
		}
		
		mThrusterLeft.setPosition(this.getX() - mThrusterLeft.getWidth(), this.getY());
		mThrusterRight.setPosition(this.getX() - mThrusterRight.getWidth(), this.getY() + this.getHeight() - mThrusterRight.getHeight());
		
		mThrusterLeft.setAlpha(1.0f);
		mThrusterRight.setAlpha(1.0f);
		
		if (pSceneTouchEvent.isActionUp()) {
			mThrusterLeft.setAlpha(0.4f);
			mThrusterRight.setAlpha(0.4f);
		}
		
		return true;    			
	}
	
	public void update() 
	{
		for (int i = 0; i < mLasers.size(); i++)
		{
			mLasers.get(i).update();
		}
		
		cleanUpLasers();
	}
	
	public void shoot()
	{
		float x = this.getX() + this.getWidth();
		float y = this.getY() + (this.getHeight()/2);
		mLasers.add(new Laser(x, y, mLaserRegion, mLaserSound, mScene));
		lasersFired++;
	}
	
	public void increaseScore (int points)
	{
		consecutiveEnemiesKilled++;
		if (consecutiveEnemiesKilled == 3)
		{
			combo++;
			if (combo > highestCombo)
			{
				highestCombo = combo;
			}
			consecutiveEnemiesKilled = 0;
		}
		score += points * combo;
		enemiesDestroyed++;
	}
	
	
	public ArrayList<Laser> getLasers()
	{
		return mLasers;
	}
	
	private void cleanUpLasers()
	{
		for (int i = 0; i < mLasers.size(); i++)
		{
			if (!mLasers.get(i).mAlive)
				mLasers.remove(i);
		}
	}
	
	public void decreaseLife()
	{
		int numLives = mLives.size(); 
		if (numLives > 0)
		{
			mScene.detachChild(mLives.get(numLives - 1));
			mLives.remove(numLives - 1);
		}
		lives = mLives.size();
	}
	
	public void resetCombo()
	{
		combo = 1;
		consecutiveEnemiesKilled = 0;
	}
	
	public void delete()
	{
		mEngine.unregisterUpdateHandler(mShootTimer);
		mScene.unregisterTouchArea(this);
		
		for (int i = 0; i < mLasers.size(); i++)
		{
			mLasers.get(i).removeFromScene();
		}
		mLasers.clear();
		mLasers = null;
		
		for (int i = 0; i < mLives.size(); i++)
		{
			mScene.detachChild(mLives.get(i));
		}
		mLives.clear();
		mLives = null;
		
		mScene.detachChild(mThrusterLeft);
		mScene.detachChild(mThrusterRight);
		mScene.detachChild(this);
	}
}