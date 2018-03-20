package com.robbcocco.gwenttracker.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.robbcocco.gwenttracker.database.CardDatabase;
import com.robbcocco.gwenttracker.database.entity.CardModel;
import com.robbcocco.gwenttracker.database.entity.VariationModel;

import java.util.List;

/**
 * Created by rober on 3/14/2018.
 */

public class GetCardDetailRawTask extends AsyncTask<Context, Void, CardModel> {
    private GetCardDetailCallback getCardDetailCallback;
    private final int cardId;

    public GetCardDetailRawTask(GetCardDetailCallback getCardDetailCallback, int cardId) {
        this.getCardDetailCallback = getCardDetailCallback;
        this.cardId = cardId;
    }

    @Override
    protected CardModel doInBackground(Context... context) {
        CardDatabase mDb = CardDatabase.getDatabase(context[0]);
        CardModel cardModel = mDb.cardDao().findCardById(cardId);

        List<VariationModel> variationModelList = mDb.cardDao().findVariationsByCardId(cardId);

        for (VariationModel variationModel : variationModelList) {
            variationModel.setRarityModel(mDb.cardDao().findRarityByVarId(variationModel.getRarity_id()));
        }
        cardModel.setVariationModelList(variationModelList);

        cardModel.setCategoryModelList(mDb.cardDao().getCategoriesByCardId(cardModel.id));

        return cardModel;
    }

    @Override
    protected void onPostExecute(CardModel result) {
        getCardDetailCallback.updateAdapter(result);
    }
}