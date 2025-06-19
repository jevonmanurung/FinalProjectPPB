package com.example.finalprojectppb.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ScanHistory.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ScanHistoryDao scanHistoryDao();
}
