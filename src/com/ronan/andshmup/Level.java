package com.ronan.andshmup;

import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

class Level
{
	public int numEnemies;
	public int levelDuration;
	public int difficulty;
	
	public boolean levelOver;
	public boolean gameOver;
	
	private Scene mScene;
	private Engine mEngine;
	
	public Player mPlayer;
	private EnemyManager mEnemyManager;
	private HUD mHUD;
	

	/**
	 * @param pNumEnemies
	 * @param pScene
	 * @param pEngine
	 */
	public Level (int pNumEnemies, Scene pScene, Engine pEngine)
	{
		numEnemies = pNumEnemies;
		mScene = pScene;
		mEngine = pEngine;
	}
	
	/**
	 * @param pTextureRegion
	 * @param pThruster
	 * @param pLaserRegion
	 * @param pHeart
	 * @param pLaserSound
	 */
	public void loadPlayer(TextureRegion pTextureRegion, TextureRegion pThruster,
			TextureRegion pLaserRegion, TextureRegion pHeart, Sound pLaserSound, int pScore, int pLives)
	{
		int x = pThruster.getHeight();
		int y = AndShmupActivity.CAMERA_HEIGHT/2 - (pTextureRegion.getHeight()/2);
		mPlayer = new Player(x, y, pTextureRegion, pThruster, pLaserRegion, pHeart, pLaserSound, mScene, mEngine, pScore, pLives);
	}
	
	/**
	 * @param pAsteroidTexture
	 * @param pMineTexture
	 * @param pExplosionTexture
	 */
	public void loadEnemies(TextureRegion pAsteroidTexture, TextureRegion pMineTexture, TiledTextureRegion pExplosionTexture, Sound pExplosionSound)
	{
		mEnemyManager = new EnemyManager(mScene, pAsteroidTexture, pMineTexture, pExplosionTexture, numEnemies, pExplosionSound, mEngine);
	}
	
	public void loadHUD(Font pFont, int pLevel, int pScore)
	{
		mHUD = new HUD(pFont, mScene, pLevel, pScore);
	}
	
	public void update()
	{
		mEnemyManager.update();
		mPlayer.update();
		
		laserEnemyCollisions();
		playerEnemyCollisions();
		
		if (mEnemyManager.allEnemiesDead)
		{
			levelOver = true;
		}
		if (mPlayer.lives == 0)
		{
			gameOver = true;
		}
		
	}
	
	public void delete()
	{
		mPlayer.delete();
		mEnemyManager.delete();
		mHUD.delete();
	}
	
	public void laserEnemyCollisions()
	{
		for (int i = 0; i < mPlayer.getLasers().size(); i++)
		{
			if (mPlayer.getLasers().get(i).mAlive)
			{
				for (int j = 0; j < mEnemyManager.getEnemies().size(); j++) {
					if (!mEnemyManager.getEnemies().get(j).mExploded)
					{
						if (mPlayer.getLasers().get(i).collidesWith(mEnemyManager.getEnemies().get(j)))
						{	
							if (mEnemyManager.getEnemies().get(j).doDamage(mPlayer.getLasers().get(i).mDamage))
							{
								if (mEnemyManager.getEnemies().get(j).type == Enemy.Type.Mine)
								{
									int count = mEnemyManager.superExplode(mEnemyManager.getEnemies().get(j));
									for (int k = 0; k < count; k++)
									{
										mPlayer.increaseScore(10 * count);
									}
								}
								else
								{
									mPlayer.increaseScore(10);
								}
								
								mHUD.score.setText("Score: " + mPlayer.score);
							}
							
							mPlayer.getLasers().get(i).removeFromScene();
							mPlayer.getLasers().remove(i);
							mPlayer.lasersHit++;
						}
					}
				}
			}
		}
	}
	
	public void playerEnemyCollisions()
	{
		for (int i = 0; i < mEnemyManager.getEnemies().size(); i++)
		{
			if (!mEnemyManager.getEnemies().get(i).mExploded)
			{
				if (mPlayer.collidesWith(mEnemyManager.getEnemies().get(i)))
				{
					//AndShmupActivity.this.mExplosionSound.play();
					mEnemyManager.getEnemies().get(i).doDamage(5000);
					mPlayer.decreaseLife();
				}
			}
		}
	}
}