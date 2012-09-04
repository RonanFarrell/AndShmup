package com.ronan.andshmup;

import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;

public class EndOfLevelStats
{
	private Text mTitle;
	private Text mScore;
	private Text mHighestCombo;
	private Text mLives;
	private Text mEnemiesDestroyed;
	private Text mLasersFired;
	private Text mAccuracy;
	private Text mContinue;
	
	public boolean m_continue = false;
	
	private Scene mScene;
	
	public EndOfLevelStats(int pScore, int pLivesRemaining, int pLasersFired, int pLasersHit, int pHighestCombo, int pEnemiesDestroyed, Font pFont, Scene pScene)
	{
		int x = AndShmupActivity.CAMERA_WIDTH/2;
		mTitle = new Text(x, 20, pFont, "Level Stats");
		mScore = new Text(x, 80, pFont, "Score: " + pScore);
		mHighestCombo = new Text(x, 120, pFont, "Highest Combo: x" + pHighestCombo);
		mLives = new Text(x, 160, pFont, "Lives Remaining: " + pLivesRemaining);
		mEnemiesDestroyed = new Text(x, 200, pFont, "Enemies Destroyed: " + pEnemiesDestroyed);
		mLasersFired = new Text(x, 240, pFont, "Lasers Fired: " + pLasersFired);
		float acc = ((float)pLasersHit/(float)pLasersFired)*100;
		mAccuracy = new Text(x, 280, pFont, "Accuracy: " + (int)acc +"%");
		
		mContinue = new Text(x, 350, pFont, "Continue")
		{
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY)
			{
				m_continue = true;
				return true;
			}
		};
		
		centreText(mTitle);
		centreText(mScore);
		centreText(mHighestCombo);
		centreText(mLives);
		centreText(mEnemiesDestroyed);
		centreText(mLasersFired);
		centreText(mAccuracy);
		centreText(mContinue);
		
		mScene = pScene;
		
		mScene.registerTouchArea(mContinue);
		
		mScene.attachChild(mScore);
		mScene.attachChild(mEnemiesDestroyed);
		mScene.attachChild(mLasersFired);
		mScene.attachChild(mHighestCombo);
		mScene.attachChild(mLives);
		mScene.attachChild(mAccuracy);
		mScene.attachChild(mContinue);
		mScene.attachChild(mTitle);
	}
	
	private void centreText(Text pText)
	{
		pText.setPosition(pText.getX() - pText.getWidth() / 2, pText.getY());
	}
	
	public void delete()
	{
		mScene.detachChild(mEnemiesDestroyed);
		mScene.detachChild(mLasersFired);
		mScene.detachChild(mScore);
		mScene.detachChild(mContinue);
		mScene.detachChild(mAccuracy);
		mScene.detachChild(mHighestCombo);
		mScene.detachChild(mLives);
		mScene.detachChild(mTitle);
		
		mScene.unregisterTouchArea(mContinue);
		
		mContinue = null;
		mScore = null;
		mLasersFired = null;
		mEnemiesDestroyed = null;
	}
}