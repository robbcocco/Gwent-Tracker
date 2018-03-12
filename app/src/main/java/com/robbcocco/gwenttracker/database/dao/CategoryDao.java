package com.robbcocco.gwenttracker.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.robbcocco.gwenttracker.database.entity.CategoryModel;

import java.util.List;

/**
 * Created by rober on 2/27/2018.
 */

@Dao
public interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CategoryModel> categories);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(CategoryModel... categories);

    @Query("SELECT categoryid FROM categories WHERE category_tag IS :tag LIMIT 1")
    int getIdByTag(String tag);

    @Query("SELECT DISTINCT categories.* FROM categories INNER JOIN card_categories ON card_categories.category_id = categories.categoryid")
    List<CategoryModel> loadAllCategories();

    @Query("SELECT * FROM categories INNER JOIN card_categories ON categories.categoryid = card_categories.category_id WHERE card_categories.card_id = :cardId")
    LiveData<List<CategoryModel>> getCategoriesByCardId(int cardId);
}
