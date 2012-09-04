package com.ronan.andshmup;

import java.util.ArrayList;

import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

public class EnemyManager
{
	private ArrayList<Enemy> mEnemies;
	
	private Scene mScene;
	
	private TextureRegion mAsteroidTexture;
	private TextureRegion mMineTexture;
	private TiledTextureRegion mExplosionTexture;
	
	private Sound mExplosionSound;
	
	private int mNumEnemies = 2;
	private int mEnemiesSpawned = 0;
	
	public boolean allEnemiesDead = false;
	
	private TimerHandler mSpawnTimer;
	private Engine mEngine;
	
	/**
	 * @param pScene
	 * @param pAsteroidTexture
	 * @param pMineTexture
	 * @param pExplosionTexture
	 * @param pNumEnemies
	 */
	public EnemyManager(Scene pScene, TextureRegion pAsteroidTexture, TextureRegion pMineTexture,
			TiledTextureRegion pExplosionTexture, int pNumEnemies, Sound pExplosionSound, Engine pEngine)
	{
		mEnemies = new ArrayList<Enemy>();
		
		mScene = pScene;
		
		mAsteroidTexture = pAsteroidTexture;
		mMineTexture = pMineTexture;
		mExplosionTexture = pExplosionTexture;
		mNumEnemies = pNumEnemies;
		mExplosionSound = pExplosionSound;
		createEnemy();
		
		mEngine = pEngine;
		
		mSpawnTimer = new TimerHandler(3.0f, new ITimerCallback() {

			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				pTimerHandler.reset();
				createEnemy();
			}//end onTimePassed

		});
		
		mEngine.registerUpdateHandler(mSpawnTimer);
	}
	
	public void update()
	{
		for (int i = 0; i < mEnemies.size(); i++)
		{
			mEnemies.get(i).update();
			cleanUpEnemy(mEnemies.get(i));
		}
		
		if (mEnemies.size() == 0 && mEnemiesSpawned == mNumEnemies)
		{
			allEnemiesDead = true;
		}
	}
	
	private void createAsteroid()
	{
		float scale = (float)(Math.random() * 1.0) + 0.5f;
		float x = AndShmupActivity.CAMERA_WIDTH + 70, y = (float)(Math.random() * (AndShmupActivity.CAMERA_HEIGHT - 64));
		Enemy tmp = new Enemy(x, y, scale, mAsteroidTexture, mExplosionTexture, mScene, mExplosionSound);
		mEnemies.add(tmp);
	}
	
	private void createMine()
	{
		float x = AndShmupActivity.CAMERA_WIDTH + 70, y = (float)(Math.random() * (AndShmupActivity.CAMERA_HEIGHT - 64));
		Enemy tmp = new Enemy(x, y, mMineTexture, mExplosionTexture, mScene, mExplosionSound);
		mEnemies.add(tmp);
	}
	
	// Factory to create enemies
	public void createEnemy()
	{
		if (mEnemiesSpawned < mNumEnemies)
		{
			int random = (int)(Math.random() * 5);

			if (random <= 3)
				createAsteroid();
			else if (random > 3)
				createMine();
			
			mEnemiesSpawned++;
		}
	}
	
	// If the enemy is dead remove it from the scene
	private void cleanUpEnemy(Enemy pEnemy)
	{
		if (pEnemy.mDead)
		{
			mScene.detachChild(pEnemy);
			mEnemies.remove(pEnemy);
		}
	}
	
	public ArrayList<Enemy> getEnemies()
	{
		return mEnemies;
	}
	
	public int superExplode(Enemy pEnemy)
	{
		int count = 0;
		
		for (int i = 0; i < mEnemies.size(); i++)
		{
			float dist = length(pEnemy.getX(), pEnemy.getY(), mEnemies.get(i).getX(), mEnemies.get(i).getY());
			
			if (dist < 250)
			{
				mEnemies.get(i).doDamage(30);
				count++;
			}
		}
		
		return count;
	}
	
	private float length(float x1, float y1, float x2, float y2)
	{
		float x = x2 - x1;
		float y = y2 - y1;
		return (float)Math.sqrt((x*x) + (y*y));
	}
	
	public void delete()
	{
		for (int i = 0; i < mEnemies.size(); i++)
		{
			mScene.detachChild(mEnemies.get(i));
		}
		mEnemies = null;
		this.mEngine.unregisterUpdateHandler(mSpawnTimer);
	}
}