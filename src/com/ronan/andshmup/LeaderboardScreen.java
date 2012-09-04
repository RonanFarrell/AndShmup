package com.ronan.andshmup;

import java.util.ArrayList;

import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;

public class LeaderboardScreen
{
	private ArrayList<Text> names;
	private ArrayList<Text> rank;
	private ArrayList<Text> score;
	
	private Text mainMenu;
	public boolean gotoMainMenu = false;
	
	private Scene mScene;
	
	public LeaderboardScreen(ArrayList<String> pValues, Scene pScene, Font pFont)
	{
		names = new ArrayList<Text>();
		rank = new ArrayList<Text>();
		score = new ArrayList<Text>();
		
		int rankX = 100;
		int namesX = 300;
		int scoreX = 700;
		
		rank.add(new Text(rankX, 30, pFont, "Rank"));
		names.add(new Text(namesX, 30, pFont, "Name"));
		score.add(new Text(scoreX, 30, pFont, "Score"));
		
		int offsetY = 50;
		
		for (int i = 0; i < pValues.size(); i++)
		{
			String[] tmp = pValues.get(i).split("~");
			rank.add(new Text(rankX, 80+(offsetY*i), pFont, (i+1) + ". "));
			names.add(new Text(namesX, 80+(offsetY*i), pFont, tmp[0]));
			score.add(new Text(scoreX, 80+(offsetY*i), pFont, tmp[1]));
		}
		
		for (int i = 0; i < rank.size(); i++)
		{
			pScene.attachChild(rank.get(i));
			pScene.attachChild(names.get(i));
			pScene.attachChild(score.get(i));
		}
		
		mainMenu = new Text(500, 700, pFont, "Main Menu")
		{
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY)
			{
				gotoMainMenu = true;
				return true;
			}
		};
		
		pScene.registerTouchArea(mainMenu);
		pScene.attachChild(mainMenu);
		
		mScene = pScene;
	}
	
	public void delete()
	{
		for (int i = 0; i < rank.size(); i++)
		{
			mScene.detachChild(rank.get(i));
			mScene.detachChild(names.get(i));
			mScene.detachChild(score.get(i));
		}
		
		rank.clear();
		rank = null;
		names.clear();
		names = null;
		score.clear();
		score = null;
		
		mScene.unregisterTouchArea(mainMenu);
		mScene.detachChild(mainMenu);
		
	}
}