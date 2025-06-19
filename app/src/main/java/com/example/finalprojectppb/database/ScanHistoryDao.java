package com.example.finalprojectppb.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ScanHistoryDao {
    @Insert
    void insert(ScanHistory history);

    @Query("SELECT * FROM scan_history ORDER BY id DESC")
    List<ScanHistory> getAllHistory();
}
