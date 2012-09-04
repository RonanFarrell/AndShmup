package com.ronan.andshmup;

import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;

public class GameOverScreen
{
	private Text mGameOver;
	private Text mMainMenu;
	
	private Scene mScene;
	
	public boolean gotoMainMenu;
	
	public GameOverScreen(Font pFont, Scene pScene)
	{
		mScene = pScene;
		
		int x = AndShmupActivity.CAMERA_WIDTH/2;
		mGameOver = new Text(x, 200, pFont, "GAME OVER");
		mMainMenu = new Text(x, 300, pFont, "Main Menu")
		{
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY)
			{
				gotoMainMenu = true;
				return true;
			}
		};
		
		centreText(mGameOver);
		centreText(mMainMenu);
		
		mScene.registerTouchArea(mMainMenu);
		
		mScene.attachChild(mGameOver);
		mScene.attachChild(mMainMenu);
		
	}
	
	private void centreText(Text pText)
	{
		pText.setPosition(pText.getX() - pText.getWidth() / 2, pText.getY());
	}
	
	public void delete()
	{
		mScene.unregisterTouchArea(mMainMenu);
		
		mScene.detachChild(mGameOver);
		mScene.detachChild(mMainMenu);
	}
}