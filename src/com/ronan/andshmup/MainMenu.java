package com.ronan.andshmup;

import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;

public class MainMenu
{
	private Text mStart;
	private Text mLeaderboard;
	private Text mTitle;
	
	public boolean startGame = false;
	public boolean showLeaderboard = false;
	
	private Scene mScene;
	
	public boolean hidden;
	
	public MainMenu(Font pFont, Scene pScene)
	{
		hidden = false;
		int x = AndShmupActivity.CAMERA_WIDTH / 2;
		
		mTitle = new Text(x, 20, pFont, "ANDSHMUP");
		
		mStart = new Text(x, 140, pFont, "START")
		{
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY)
			{
				startGame = true;
				return true;
			}
		};
		mLeaderboard = new Text(x, 210, pFont, "LEADERBOARD")
		{
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY)
			{
				showLeaderboard = true;
				return true;
			}
		};
		
		
		centreText(mTitle);
		centreText(mStart);
		centreText(mLeaderboard);
		
		
		mScene = pScene;
		
		mScene.registerTouchArea(mStart);
		mScene.registerTouchArea(mLeaderboard);
		
		mScene.attachChild(mTitle);
		mScene.attachChild(mStart);
		mScene.attachChild(mLeaderboard);
	}
	
	private void centreText(Text pText)
	{
		pText.setPosition(pText.getX() - pText.getWidth() / 2, pText.getY());
	}
	
	public void delete()
	{
		mScene.detachChild(mTitle);
		mScene.detachChild(mStart);
		mScene.detachChild(mLeaderboard);
		
		mScene.unregisterTouchArea(mStart);
		mScene.unregisterTouchArea(mLeaderboard);
		
		mTitle = null;
		mStart = null;
		mLeaderboard = null;
	}
	
	public void hide()
	{
		hidden = true;
		mTitle.setVisible(false);
		mStart.setVisible(false);
		mLeaderboard.setVisible(false);
		
		mScene.unregisterTouchArea(mStart);
		mScene.unregisterTouchArea(mLeaderboard);
	}
	
	public void show()
	{
		hidden = false;
		mTitle.setVisible(true);
		mStart.setVisible(true);
		mLeaderboard.setVisible(true);
		
		mScene.registerTouchArea(mStart);
		mScene.registerTouchArea(mLeaderboard);
	}
}