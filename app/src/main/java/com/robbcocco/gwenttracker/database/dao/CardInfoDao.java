package com.robbcocco.gwenttracker.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.robbcocco.gwenttracker.database.entity.CardInfoModel;

import java.util.List;

/**
 * Created by rober on 3/3/2018.
 */

@Dao
public interface CardInfoDao {
    @Query("SELECT * FROM cards JOIN variations ON cards.cardid = variations.card_id WHERE variations.collectible = 1")
    LiveData<List<CardInfoModel>> findAllCardInfo();
}
