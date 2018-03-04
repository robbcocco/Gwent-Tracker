package com.robbcocco.gwenttracker.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by rober on 2/27/2018.
 */

@Entity(tableName = "loyalties",
        indices = {@Index("loyaltyid"), @Index("loyalty_name")})
public class LoyaltyModel {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "loyaltyid")
    public int id;
    @ColumnInfo(name = "loyalty_name")
    private String name;

    public LoyaltyModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
