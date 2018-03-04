package com.robbcocco.gwenttracker.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by rober on 2/27/2018.
 */

@Entity(tableName = "rarities",
        indices = {@Index("rarityid"), @Index("rarity_name")})
public class RarityModel {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rarityid")
    public int id;
    @ColumnInfo(name = "rarity_name")
    private String name;
    private int premium;
    private int standard;
    private int upgrade;
    private int mill_premium;
    private int mill_standard;
    private int mill_upgrade;

    public RarityModel(String name, int premium, int standard, int upgrade, int mill_premium,
                       int mill_standard, int mill_upgrade) {
        this.name = name;
        this.premium = premium;
        this.standard = standard;
        this.upgrade = upgrade;
        this.mill_premium = mill_premium;
        this.mill_standard = mill_standard;
        this.mill_upgrade = mill_upgrade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPremium() {
        return premium;
    }

    public void setPremium(int premium) {
        this.premium = premium;
    }

    public int getStandard() {
        return standard;
    }

    public void setStandard(int standard) {
        this.standard = standard;
    }

    public int getUpgrade() {
        return upgrade;
    }

    public void setUpgrade(int upgrade) {
        this.upgrade = upgrade;
    }

    public int getMill_premium() {
        return mill_premium;
    }

    public void setMill_premium(int mill_premium) {
        this.mill_premium = mill_premium;
    }

    public int getMill_standard() {
        return mill_standard;
    }

    public void setMill_standard(int mill_standard) {
        this.mill_standard = mill_standard;
    }

    public int getMill_upgrade() {
        return mill_upgrade;
    }

    public void setMill_upgrade(int mill_upgrade) {
        this.mill_upgrade = mill_upgrade;
    }
}
