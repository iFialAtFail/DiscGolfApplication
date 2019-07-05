package com.manleysoftware.michael.discgolfapp.Model;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 8/16/2016.
 */
public class ScorecardRepository implements Serializable {

	//region Private Fields

	private static final long serialVersionUID = 3L;
	private static final String SCORECARD_STORAGE_FILE_FINISHED = "finishedScorecards.data";
	private static final String SCORECARD_STORAGE_FILE_UNFINISHED = "UnfinishedScorecards.data";
	private final List<ScoreCard> scoreCardStorage;

	//endregion

	//region Constructors

	public ScorecardRepository(){
		scoreCardStorage = new ArrayList<ScoreCard>();
	}

	//endregion

	//region Getters and Setters

	public List<ScoreCard> getScoreCardStorage(){
		return scoreCardStorage;
	}

	public int getCount(){
		if (!scoreCardStorage.isEmpty()){
			return scoreCardStorage.size();
		}
		return 0;
	}



	//endregion

	//region Public Methods

	public void AddScoreCardsToStorage(ScoreCard card){
		scoreCardStorage.add(card);
	}

	public void DeleteScoreCardFromStorage(ScoreCard card){
		scoreCardStorage.remove(card);
	}

	public void DeleteScoreCardFromStorage(int position){
		scoreCardStorage.remove(position);
	}

	public void DeleteAll(){
		this.scoreCardStorage.clear();
	}

	public boolean SaveFinishedCardsToFile(Context context){
		try {
			FileOutputStream fos = context.openFileOutput(SCORECARD_STORAGE_FILE_FINISHED, Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(this);

			oos.close();
			fos.close();
		} catch (Exception ex){
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	public static ScorecardRepository LoadFinishedCardStorage(Context context){
		ScorecardRepository cardStorage;
		try{
			FileInputStream fis = context.openFileInput(SCORECARD_STORAGE_FILE_FINISHED);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object obj = ois.readObject();

			if (obj instanceof ScorecardRepository){
				cardStorage = (ScorecardRepository) obj;
				return cardStorage;
			}

		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public boolean SaveUnFinishedCardListToFile(Context context){
		try {
			FileOutputStream fos = context.openFileOutput(SCORECARD_STORAGE_FILE_UNFINISHED, Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(this);

			oos.close();
			fos.close();
		} catch (Exception ex){
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	public static ScorecardRepository LoadUnFinishedCardStorage(Context context){
		ScorecardRepository cardStorage;
		try{
			FileInputStream fis = context.openFileInput(SCORECARD_STORAGE_FILE_UNFINISHED);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object obj = ois.readObject();

			if (obj instanceof ScorecardRepository){
				cardStorage = (ScorecardRepository) obj;
				return cardStorage;
			}

		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

}
