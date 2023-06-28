package com.manleysoftware.michael.discgolfapp.data;

import android.content.Context;

import com.manleysoftware.michael.discgolfapp.data.filerepository.CourseFileRepository;
import com.manleysoftware.michael.discgolfapp.data.filerepository.PlayerFileRepository;
import com.manleysoftware.michael.discgolfapp.data.filerepository.ScorecardFileRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@InstallIn(SingletonComponent.class)
@Module
public class DatastoreHiltModule {

    @Provides
    @Singleton
    public ScorecardRepository filescorecardRepository(@ApplicationContext Context context) {
        return new ScorecardFileRepository(context);
    }

    @Provides
    @Singleton
    public PlayerRepository filePlayerRepository(@ApplicationContext Context context) {
        return new PlayerFileRepository(context);
    }

    @Provides
    @Singleton
    public CourseRepository fileCourseRepository(@ApplicationContext Context context) {
        return new CourseFileRepository(context);
    }

}
