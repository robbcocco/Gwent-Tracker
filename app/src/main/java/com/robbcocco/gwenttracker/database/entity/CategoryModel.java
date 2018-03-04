package com.robbcocco.gwenttracker.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Map;

/**
 * Created by rober on 2/26/2018.
 */

@Entity(tableName = "categories",
        indices = {@Index("categoryid"), @Index("category_name")})
public class CategoryModel {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "categoryid")
    public int id;
    @ColumnInfo(name = "category_tag")
    private String tag;
    @ColumnInfo(name = "category_name")
    private Map<String, String> name;

    public CategoryModel(String tag, Map<String, String> name) {
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
