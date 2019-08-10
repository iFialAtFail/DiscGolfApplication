package com.manleysoftware.michael.discgolfapp.data.filerepository;

import android.content.Context;
import android.support.annotation.NonNull;

import com.manleysoftware.michael.discgolfapp.Application.AlreadyExistsException;
import com.manleysoftware.michael.discgolfapp.data.Model.Scorecard;
import com.manleysoftware.michael.discgolfapp.data.ScorecardRepository;

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
public class ScorecardFileRepository implements ScorecardRepository, Serializable {

	//region Private Fields

	private static final long serialVersionUID = 3L;
	private static final String SCORECARD_STORAGE_FILE = "scorecards.data";
	private final List<Scorecard> scorecards;

	//endregion

	//region Constructors

	public ScorecardFileRepository(Context context){
		ScorecardFileRepository repo = loadFinishedCardStorage(context);
		if (repo != null){
			scorecards = repo.getAllScorecards();
		} else{
			scorecards = new ArrayList<Scorecard>();
		}
	}


	//endregion


	//region Public Methods


	private boolean save(Context context){
		if (this.scorecards.isEmpty()) return false;
		try {
			FileOutputStream fos = context.openFileOutput(SCORECARD_STORAGE_FILE, Context.MODE_PRIVATE);
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

	private static ScorecardFileRepository loadFinishedCardStorage(Context context){
		ScorecardFileRepository cardStorage;
		try{
			FileInputStream fis = context.openFileInput(SCORECARD_STORAGE_FILE);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object obj = ois.readObject();

			if (obj instanceof ScorecardFileRepository){
				cardStorage = (ScorecardFileRepository) obj;
				return cardStorage;
			}

		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}


	@Override
	public List<Scorecard> findAllFinishedScorecards() {
		List<Scorecard> finishedScorecards = getFinishedScorecards();
		return finishedScorecards;
	}

	@NonNull
	private List<Scorecard> getFinishedScorecards() {
		List<Scorecard> finishedScorecards = getUnfinishedScorecards();
		return finishedScorecards;
	}

	@NonNull
	private List<Scorecard> getUnfinishedScorecards() {
		List<Scorecard> finishedScorecards = new ArrayList<>();
		for (Scorecard scorecard : scorecards) {
			if (scorecard.getArchived()) {
				finishedScorecards.add(scorecard);
			}
		}
		return finishedScorecards;
	}

	@Override
	public List<Scorecard> findAllUnfinishedScorecards() {
		List<Scorecard> unfinishedScorecards = new ArrayList<>();
		for (Scorecard scorecard : scorecards){
			if (!scorecard.getArchived()){
				unfinishedScorecards.add(scorecard);
			}
		}
		return unfinishedScorecards;
	}

	@Override
	public void add(Scorecard entity, Context context) throws AlreadyExistsException {
		scorecards.add(entity);
		save(context);
	}

	@Override
	public void update(Scorecard entity, Context context) {
		return;
	}

	@Override
	public void delete(Scorecard entity, Context context) {
		for (int i = 0; i < scorecards.size(); i++) {
			if (scorecards.get(i).getDate().equals(entity.getDate()) &&
					scorecards.get(i).getCourseName().equals(entity.getCourseName())){
				scorecards.remove(i);
				save(context);
				break;
			}
		}
	}

	@Override
	public Scorecard findByPrimaryKey(Scorecard template) {
		Scorecard retval = null;
		for(Scorecard scorecard: scorecards){
			if (retval.getCourseName().equals(template.getCourseName()) &&
			retval.getDate().equals(template.getDate())){
				retval = scorecard;
				break;
			}
		}
		return retval;
	}

	@Override
	public List<Scorecard> getAllScorecards() {
		return scorecards;
	}

	@Override
	public void deleteAllFinishedScorecards(Context context) {
		List<Scorecard> finishedScorecards = getFinishedScorecards();
		for (Scorecard finishedScorecard : finishedScorecards) {
			delete(finishedScorecard, context);
		}
	}

	@Override
	public void deleteAllUnfinishedScorecards(Context context) {
		List<Scorecard> unfinishedScorecards = getUnfinishedScorecards();
		for (Scorecard unfinishedScorecard : unfinishedScorecards) {
			delete(unfinishedScorecard, context);
		}
	}
}
