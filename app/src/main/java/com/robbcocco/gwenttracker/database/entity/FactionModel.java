package com.robbcocco.gwenttracker.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Map;

/**
 * Created by rober on 2/26/2018.
 */

@Entity(tableName = "factions",
        indices = {@Index("factionid"), @Index("faction_name")})
public class FactionModel {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "factionid")
    public int id;
    @ColumnInfo(name = "faction_tag")
    private String tag;
    @ColumnInfo(name = "faction_name")
    private Map<String, String> name;

    public FactionModel(String tag, Map<String, String> name) {
        this.tag = tag;
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Map<String, String> getName() {
        return name;
    }

    public void setName(Map<String, String> name) {
        this.name = name;
    }
}
