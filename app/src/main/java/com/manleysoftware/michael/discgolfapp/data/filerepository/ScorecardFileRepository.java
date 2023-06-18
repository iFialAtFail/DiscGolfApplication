package com.manleysoftware.michael.discgolfapp.data.filerepository;

import android.content.Context;

import androidx.annotation.NonNull;

import com.manleysoftware.michael.discgolfapp.application.AlreadyExistsException;
import com.manleysoftware.michael.discgolfapp.domain.Scorecard;
import com.manleysoftware.michael.discgolfapp.data.ScorecardRepository;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Michael on 8/16/2016.
 */
public class ScorecardFileRepository implements ScorecardRepository, Serializable {

    private long scorecardSequence = 0;

    private static final long serialVersionUID = 3L;
    private static final String SCORECARD_STORAGE_FILE = "scorecards.data";
    private final List<Scorecard> scorecards;

    public ScorecardFileRepository(Context context) {
        ScorecardFileRepository repo = loadAllScorecardsFromFile(context);
        if (repo != null) {
            scorecards = repo.getAllScorecards();
            scorecardSequence = repo.scorecardSequence;
        } else {
            scorecards = new ArrayList<>();
        }
    }

    public ScorecardFileRepository(List<Scorecard> scorecards, long sequence) {
        this.scorecards = scorecards;
        this.scorecardSequence = sequence;
    }

    private boolean save(Context context) {
        if (this.scorecards.isEmpty() && this.scorecardSequence == 0L) return false;
        return saveToFile(context, this);
    }

    protected boolean saveToFile(Context context, ScorecardFileRepository objectToWriteToFile) {
        try {
            FileOutputStream fos = context.openFileOutput(SCORECARD_STORAGE_FILE, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(objectToWriteToFile);

            oos.close();
            fos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    private static ScorecardFileRepository loadAllScorecardsFromFile(Context context) {
        ScorecardFileRepository cardStorage;
        try {
            FileInputStream fis = context.openFileInput(SCORECARD_STORAGE_FILE);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object obj = ois.readObject();

            if (obj instanceof ScorecardFileRepository) {
                cardStorage = (ScorecardFileRepository) obj;
                return cardStorage;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @NonNull
    public List<Scorecard> findAllFinishedScorecards() {
        return scorecards.stream().filter(Scorecard::isArchived).collect(Collectors.toList());
    }

    @Override
    public List<Scorecard> findAllUnfinishedScorecards() {
        return scorecards.stream().filter(s -> !s.isArchived()).collect(Collectors.toList());
    }

    @Override
    public void add(Scorecard entity, Context context) throws AlreadyExistsException {
        scorecards.add(entity);
        save(context);
    }

    @Override
    public void update(Scorecard entity, Context context) {

    }

    @Override
    public void delete(Scorecard entity, Context context) {
        for (int i = 0; i < scorecards.size(); i++) {
            if (scorecards.get(i).displayDate().equals(entity.displayDate()) &&
                    scorecards.get(i).courseName().equals(entity.courseName())) {
                scorecards.remove(i);
                save(context);
                break;
            }
        }
    }

    @Override
    public Scorecard findByPrimaryKey(Scorecard template) {
        Scorecard retval = null;
        for (Scorecard scorecard : scorecards) {
            if (scorecard.courseName().equals(template.courseName()) &&
                    scorecard.displayDate().equals(template.displayDate())) {
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
        List<Scorecard> finishedScorecards = findAllFinishedScorecards();
        for (Scorecard finishedScorecard : finishedScorecards) {
            delete(finishedScorecard, context);
        }
    }

    @Override
    public void deleteAllUnfinishedScorecards(Context context) {
        List<Scorecard> unfinishedScorecards = findAllUnfinishedScorecards();
        for (Scorecard unfinishedScorecard : unfinishedScorecards) {
            delete(unfinishedScorecard, context);
        }
    }
}
