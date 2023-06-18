package com.manleysoftware.michael.discgolfapp.data;

import android.content.Context;

import com.manleysoftware.michael.discgolfapp.data.filerepository.ScorecardFileRepository;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@InstallIn(SingletonComponent.class)
@Module
public class DatastoreHiltModule {

    @Provides
    public ScorecardRepository filescorecardRepository(@ApplicationContext Context context) {
        return new ScorecardFileRepository(context);
    }

}
