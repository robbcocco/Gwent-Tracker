package com.robbcocco.gwenttracker.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.net.URL;

/**
 * Created by rober on 2/27/2018.
 */

@Entity(tableName = "variations",
        indices = {@Index("variationid"), @Index("card_id"), @Index("rarity_id"), @Index("set_id")},
        foreignKeys = {
                @ForeignKey(entity = CardModel.class,
                        parentColumns = "cardid",
                        childColumns = "card_id"),
                @ForeignKey(entity = RarityModel.class,
                        parentColumns = "rarityid",
                        childColumns = "rarity_id"),
                @ForeignKey(entity = SetModel.class,
                        parentColumns = "setid",
                        childColumns = "set_id")})
public class VariationModel {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "variationid")
    public int id;
    @ColumnInfo(name = "variation_tag")
    private String tag;
    private int card_id;
    private URL art_high;
    private URL art_low;
    private URL art_medium;
    private URL art_original;
    private URL art_thumbnail;
    private Boolean collectible;
    private int rarity_id;
    private int set_id;

    public VariationModel(String tag, int card_id, URL art_high, URL art_low,
                          URL art_medium, URL art_original, URL art_thumbnail,
                          Boolean collectible, int rarity_id, int set_id) {
        this.tag = tag;
        this.card_id = card_id;
        this.art_high = art_high;
        this.art_low = art_low;
        this.art_medium = art_medium;
        this.art_original = art_original;
        this.art_thumbnail = art_thumbnail;
        this.collectible = collectible;
        this.rarity_id = rarity_id;
        this.set_id = set_id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getCard_id() {
        return card_id;
    }

    public void setCard_id(int card_id) {
        this.card_id = card_id;
    }

    public URL getArt_high() {
        return art_high;
    }

    public void setArt_high(URL art_high) {
        this.art_high = art_high;
    }

    public URL getArt_low() {
        return art_low;
    }

    public void setArt_low(URL art_low) {
        this.art_low = art_low;
    }

    public URL getArt_medium() {
        return art_medium;
    }

    public void setArt_medium(URL art_medium) {
        this.art_medium = art_medium;
    }

    public URL getArt_original() {
        return art_original;
    }

    public void setArt_original(URL art_original) {
        this.art_original = art_original;
    }

    public URL getArt_thumbnail() {
        return art_thumbnail;
    }

    public void setArt_thumbnail(URL art_thumbnail) {
        this.art_thumbnail = art_thumbnail;
    }

    public Boolean getCollectible() {
        return collectible;
    }

    public void setCollectible(Boolean collectible) {
        this.collectible = collectible;
    }

    public int getRarity_id() {
        return rarity_id;
    }

    public void setRarity_id(int rarity_id) {
        this.rarity_id = rarity_id;
    }

    public int getSet_id() {
        return set_id;
    }

    public void setSet_id(int set_id) {
        this.set_id = set_id;
    }
}
