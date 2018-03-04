package com.robbcocco.gwenttracker.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by rober on 2/27/2018.
 */

@Entity(tableName = "sets",
        indices = {@Index("setid"), @Index("set_name")})
public class SetModel {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "setid")
    public int id;
    @ColumnInfo(name = "set_name")
    private String name;

    public SetModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
