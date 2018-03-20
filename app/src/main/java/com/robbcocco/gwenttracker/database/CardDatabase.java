package com.robbcocco.gwenttracker.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;

import com.robbcocco.gwenttracker.CollectionFragment;
import com.robbcocco.gwenttracker.MainActivity;
import com.robbcocco.gwenttracker.database.converter.DictConverter;
import com.robbcocco.gwenttracker.database.converter.URLConverter;
import com.robbcocco.gwenttracker.database.dao.CardCategoryDao;
import com.robbcocco.gwenttracker.database.dao.CardDao;
import com.robbcocco.gwenttracker.database.dao.CardKeywordDao;
import com.robbcocco.gwenttracker.database.dao.CardLoyaltyDao;
import com.robbcocco.gwenttracker.database.dao.CategoryDao;
import com.robbcocco.gwenttracker.database.dao.FactionDao;
import com.robbcocco.gwenttracker.database.dao.KeywordDao;
import com.robbcocco.gwenttracker.database.dao.LoyaltyDao;
import com.robbcocco.gwenttracker.database.dao.RarityDao;
import com.robbcocco.gwenttracker.database.dao.RelatedCardDao;
import com.robbcocco.gwenttracker.database.dao.SetDao;
import com.robbcocco.gwenttracker.database.dao.VariationDao;
import com.robbcocco.gwenttracker.database.entity.CardCategoryModel;
import com.robbcocco.gwenttracker.database.entity.CardKeywordModel;
import com.robbcocco.gwenttracker.database.entity.CardLoyaltyModel;
import com.robbcocco.gwenttracker.database.entity.CardModel;
import com.robbcocco.gwenttracker.database.entity.CategoryModel;
import com.robbcocco.gwenttracker.database.entity.FactionModel;
import com.robbcocco.gwenttracker.database.entity.KeywordModel;
import com.robbcocco.gwenttracker.database.entity.LoyaltyModel;
import com.robbcocco.gwenttracker.database.entity.RarityModel;
import com.robbcocco.gwenttracker.database.entity.RelatedCardModel;
import com.robbcocco.gwenttracker.database.entity.SetModel;
import com.robbcocco.gwenttracker.database.entity.VariationModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by rober on 2/23/2018.
 */

@Database(version = 5, entities = {
    CardCategoryModel.class, CardKeywordModel.class, CardLoyaltyModel.class, CardModel.class,
    CategoryModel.class, FactionModel.class, KeywordModel.class, LoyaltyModel.class,
    RarityModel.class, RelatedCardModel.class, SetModel.class, VariationModel.class
    })
@TypeConverters({DictConverter.class, URLConverter.class})
public abstract class CardDatabase extends RoomDatabase {

    public static final ExecutorService DB_THREAD = Executors.newSingleThreadExecutor();
    private static final String DB_NAME="collection.db";
    private static volatile CardDatabase INSTANCE=null;

    public abstract CardCategoryDao cardCategoryDao();
    public abstract CardDao cardDao();
    public abstract CardKeywordDao cardKeywordDao();
    public abstract CardLoyaltyDao cardLoyaltyDao();
    public abstract CategoryDao categoryDao();
    public abstract FactionDao factionDao();
    public abstract KeywordDao keywordDao();
    public abstract LoyaltyDao loyaltyDao();
    public abstract RarityDao rarityDao();
    public abstract RelatedCardDao relatedCardDao();
    public abstract SetDao setDao();
    public abstract VariationDao variationDao();

    public synchronized static CardDatabase getDatabase(Context context) {
        if (INSTANCE==null) {
            INSTANCE=create(context);
        }

        return(INSTANCE);
    }

    @NonNull
    private static CardDatabase create(final Context context) {
        return Room.databaseBuilder(context, CardDatabase.class, DB_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        DB_THREAD.execute(new Runnable() {
                            @Override
                            public void run() {
                                CardGenerator.GenerateCollection(getDatabase(context), context);
                            }
                        });
                    }
                })
                .addMigrations()
                .fallbackToDestructiveMigration()
                .build();
    }
}
