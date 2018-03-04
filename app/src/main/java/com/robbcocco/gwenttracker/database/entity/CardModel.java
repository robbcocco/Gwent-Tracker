package com.robbcocco.gwenttracker.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Map;

/**
 * Created by rober on 2/23/2018.
 */

@Entity (tableName = "cards",
        indices = {@Index("cardid"), @Index("card_name"), @Index("faction_id")},
        foreignKeys = {
                @ForeignKey(entity = FactionModel.class,
                        parentColumns = "factionid",
                        childColumns = "faction_id")})
public class CardModel {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "cardid")
    public int id;
    @ColumnInfo(name = "card_tag")
    private String tag;
    @ColumnInfo(name = "card_name")
    private Map<String, String> name;
    @ColumnInfo(name = "card_flavor")
    private Map<String, String> flavor;
    @ColumnInfo(name = "card_info")
    private Map<String, String> info;
    @ColumnInfo(name = "card_info_raw")
    private Map<String, String> info_raw;
    private int strength;
    private int faction_id;

    public CardModel(String tag, Map<String, String> name, Map<String, String> flavor,
                     Map<String, String> info, Map<String, String> info_raw, int strength,
                     int faction_id) {
        this.tag = tag;
        this.name = name;
        this.flavor = flavor;
        this.info = info;
        this.info_raw = info_raw;
        this.strength = strength;
        this.faction_id = faction_id;
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

    public Map<String, String> getFlavor() {
        return flavor;
    }

    public void setFlavor(Map<String, String> flavor) {
        this.flavor = flavor;
    }

    public Map<String, String> getInfo() {
        return info;
    }

    public void setInfo(Map<String, String> info) {
        this.info = info;
    }

    public Map<String, String> getInfo_raw() {
        return info_raw;
    }

    public void setInfo_raw(Map<String, String> info_raw) {
        this.info_raw = info_raw;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getFaction_id() {
        return faction_id;
    }

    public void setFaction_id(int faction_id) {
        this.faction_id = faction_id;
    }
}
