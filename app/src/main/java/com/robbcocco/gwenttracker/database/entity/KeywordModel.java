package com.robbcocco.gwenttracker.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Map;

/**
 * Created by rober on 2/26/2018.
 */

@Entity(tableName = "keywords",
        indices = {@Index("keywordid"), @Index("keyword_name")})
public class KeywordModel {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "keywordid")
    public int id;
    @ColumnInfo(name = "keyword_tag")
    private String tag;
    @ColumnInfo(name = "keyword_name")
    private Map<String, String> name;
    @ColumnInfo(name = "keyword_info")
    private Map<String, String> info;
    @ColumnInfo(name = "keyword_info_raw")
    private Map<String, String> info_raw;

    public KeywordModel(String tag, Map<String, String> name, Map<String, String> info, Map<String,
            String> info_raw) {
        this.tag = tag;
        this.name = name;
        this.info = info;
        this.info_raw = info_raw;
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
}
