package com.robbcocco.gwenttracker;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.robbcocco.gwenttracker.database.CardDatabase;
import com.robbcocco.gwenttracker.database.CardGenerator;
import com.robbcocco.gwenttracker.database.entity.CardModel;
import com.robbcocco.gwenttracker.database.entity.CategoryModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Created by robb on 12/12/17.
 */

@RunWith(AndroidJUnit4.class)
public class CardDatabaseTest {
    private static final String TAG = "CardDatabaseTest";

    private CardDatabase mDb;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        mDb = Room.inMemoryDatabaseBuilder(context, CardDatabase.class).build();
        CardGenerator.GenerateCollection(mDb, context);
    }

    @After
    public void closeDb() throws IOException {
        mDb.close();
    }

    @Test
    public void testCardGenerator() throws Exception {
        List<CategoryModel> categories = mDb.categoryDao().loadAllCategories().getValue();
        List<CardModel> cards = mDb.cardDao().loadAllCards().getValue();

        assertNotNull(categories);

        assertNotNull(cards);
        assertThat(cards.get(0).getName().get("en-US"), equalTo("Ciri"));
    }
}
