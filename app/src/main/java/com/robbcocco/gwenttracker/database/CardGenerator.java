package com.robbcocco.gwenttracker.database;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by rober on 2/27/2018.
 */

public class CardGenerator {
    private static final String TAG = "CardGenerator";

    public static void GenerateCollection(CardDatabase mDb, Context ctxt) {
        ArrayList<CardCategoryModel> cardCategoryModels = new ArrayList<>();
        ArrayList<CardKeywordModel> cardKeywordModels = new ArrayList<>();
        ArrayList<CardLoyaltyModel> cardLoyaltyModels = new ArrayList<>();
        ArrayList<CardModel> cardModels = new ArrayList<>();
        ArrayList<CategoryModel> categoryModels = new ArrayList<>();
        ArrayList<FactionModel> factionModels = new ArrayList<>();
        ArrayList<KeywordModel> keywordModels = new ArrayList<>();
        ArrayList<LoyaltyModel> loyaltyModels = new ArrayList<>();
        ArrayList<RarityModel> rarityModels = new ArrayList<>();
        ArrayList<RelatedCardModel> relatedCardModels = new ArrayList<>();
        ArrayList<SetModel> setModels = new ArrayList<>();
        ArrayList<VariationModel> variationModels = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(loadJSONFromAsset(ctxt, "categories.json"));
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                // Categories
                Map<String, String> value;

                value = makeMap(jsonObject.getString(key));

                CategoryModel categoryModel = new CategoryModel(key, value);
                categoryModels.add(categoryModel);
            }
            mDb.categoryDao().insertAll(categoryModels);

            jsonObject = new JSONObject(loadJSONFromAsset(ctxt, "keywords.json"));
            keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                // Keywords
                Map<String, String> name = new HashMap<>();
                Map<String, String> text = new HashMap<>();
                Map<String, String> raw = new HashMap<>();

                JSONObject keyword = jsonObject.getJSONObject(key);
                Iterator<String> keywordLangs = keyword.keys();
                while (keywordLangs.hasNext()) {
                    String keyLang = keywordLangs.next();
                    name.put(keyLang, keyword.getString(keyLang).split(":")[0]);
                    text.put(keyLang, keyword.getString(keyLang));
                    raw.put(keyLang, keyword.getString(keyLang));
                }

                KeywordModel keywordModel = new KeywordModel(key, name, text, raw);
                keywordModels.add(keywordModel);
            }
            mDb.keywordDao().insertAll(keywordModels);

            jsonObject = new JSONObject(loadJSONFromAsset(ctxt, "cards.json"));
            keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                if (Integer.parseInt(key) < 1000) {
                    // Factions
                    String tag;
                    Map<String, String> name;

                    tag = jsonObject.getJSONObject(key).getString("faction");
                    name = makeMap(jsonObject.getJSONObject(key).getString("name"));

                    FactionModel factionModel = new FactionModel(tag, name);
                    factionModels.add(factionModel);
                } else if (jsonObject.getJSONObject(key).getBoolean("released")) {
                    JSONArray loyalties = jsonObject.getJSONObject(key).getJSONArray("loyalties");
                    for (int i = 0; i < loyalties.length(); i++) {
                        // Loyalties
                        String name;

                        name = loyalties.getString(i);

                        LoyaltyModel loyaltyModel = new LoyaltyModel(name);
                        if (!loyaltyModels.contains(loyaltyModel)) {
                            loyaltyModels.add(loyaltyModel);
                        }
                    }

                    JSONObject variations = jsonObject.getJSONObject(key).getJSONObject("variations");
                    Iterator<String> varKeys = variations.keys();
                    while (varKeys.hasNext()) {
                        String varKey = varKeys.next();
                        // Sets
                        String set;

                        set = variations.getJSONObject(varKey).getString("availability");

                        SetModel setModel = new SetModel(set);
                        if (!setModels.contains(setModel)) {
                            setModels.add(setModel);
                        }

                        // Rarities
                        String name;
                        int premium;
                        int standard;
                        int upgrade;
                        int mill_premium;
                        int mill_standard;
                        int mill_upgrade;

                        name = variations.getJSONObject(varKey).getString("rarity");
                        premium = variations.getJSONObject(varKey).getJSONObject("craft")
                                .getInt("premium");
                        standard = variations.getJSONObject(varKey).getJSONObject("craft")
                                .getInt("standard");
                        upgrade = variations.getJSONObject(varKey).getJSONObject("craft")
                                .getInt("upgrade");
                        mill_premium = variations.getJSONObject(varKey).getJSONObject("mill")
                                .getInt("premium");
                        mill_standard = variations.getJSONObject(varKey).getJSONObject("mill")
                                .getInt("standard");
                        mill_upgrade = variations.getJSONObject(varKey).getJSONObject("mill")
                                .getInt("upgrade");

                        RarityModel rarityModel = new RarityModel(name, premium, standard, upgrade,
                                mill_premium, mill_standard, mill_upgrade);
                        if (!rarityModels.contains(rarityModel)) {
                            rarityModels.add(rarityModel);
                        }
                    }
                }
            }
            mDb.factionDao().insertAll(factionModels);
            mDb.loyaltyDao().insertAll(loyaltyModels);
            mDb.setDao().insertAll(setModels);
            mDb.rarityDao().insertAll(rarityModels);

            keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                if (Integer.parseInt(key) > 1000 & jsonObject.getJSONObject(key)
                        .getBoolean("released")) {
                    // Cards
                    Map<String, String> name = new HashMap<>();
                    Map<String, String> flavor = new HashMap<>();
                    Map<String, String> info = new HashMap<>();
                    Map<String, String> info_raw = new HashMap<>();
                    int strength;
                    String faction_tag;
                    int faction_id;

                    name = makeMap(jsonObject.getJSONObject(key).getString("name"));
                    flavor = makeMap(jsonObject.getJSONObject(key).getString("flavor"));
                    info = makeMap(jsonObject.getJSONObject(key).getString("info"));
                    info_raw = makeMap(jsonObject.getJSONObject(key).getString("infoRaw"));
                    strength = jsonObject.getJSONObject(key).getInt("strength");
                    faction_tag = jsonObject.getJSONObject(key).getString("faction");
                    faction_id = mDb.factionDao().getIdByTag(faction_tag);

                    CardModel cardModel = new CardModel(key, name, flavor, info, info_raw, strength,
                            faction_id);
                    cardModels.add(cardModel);
                }
            }
            mDb.cardDao().insertAll(cardModels);

            keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                if (Integer.parseInt(key) > 1000 & jsonObject.getJSONObject(key)
                        .getBoolean("released")) {
                    int cardId = mDb.cardDao().getIdByTag(key);

                    JSONObject variations = jsonObject.getJSONObject(key).getJSONObject("variations");
                    Iterator<String> varKeys = variations.keys();
                    while (varKeys.hasNext()) {
                        String varKey = varKeys.next();
                        // Variations
                        URL art_high = null;
                        URL art_low = null;
                        URL art_medium = null;
                        URL art_original = null;
                        URL art_thumbnail = null;
                        Boolean collectible;
                        String rarity_name;
                        int rarity_id;
                        String set_name;
                        int set_id;

                        collectible = variations.getJSONObject(varKey).getBoolean("collectible");
                        if (collectible) {
                            try {
                                art_high = new URL(variations.getJSONObject(varKey).getJSONObject("art")
                                        .getString("high"));
                                art_low = new URL(variations.getJSONObject(varKey).getJSONObject("art")
                                        .getString("low"));
                                art_medium = new URL(variations.getJSONObject(varKey).getJSONObject("art")
                                        .getString("medium"));
                                art_original = new URL(variations.getJSONObject(varKey).getJSONObject("art")
                                        .getString("original"));
                                art_thumbnail = new URL(variations.getJSONObject(varKey).getJSONObject("art")
                                        .getString("thumbnail"));
                            } catch (MalformedURLException e) {
                                Log.e(TAG, varKey);
                                e.printStackTrace();
                            }
                        }
                        rarity_name = variations.getJSONObject(varKey).getString("rarity");
                        rarity_id = mDb.rarityDao().getIdByName(rarity_name);
                        set_name = variations.getJSONObject(varKey).getString("availability");
                        set_id = mDb.setDao().getIdByName(set_name);

                        VariationModel variationModel = new VariationModel(varKey, cardId, art_high,
                                art_low, art_medium, art_original, art_thumbnail, collectible,
                                rarity_id, set_id);
                        variationModels.add(variationModel);
                    }

                    JSONArray categoryIds = jsonObject.getJSONObject(key).getJSONArray("categoryIds");
                    for (int i = 0; i < categoryIds.length(); i++) {
                        // Card - Categories
                        String category_tag;
                        int category_id;

                        category_tag = categoryIds.getString(i);
                        category_id = mDb.categoryDao().getIdByTag(category_tag);

                        if (category_id > 0) {
                            CardCategoryModel cardCategoryModel = new CardCategoryModel(cardId, category_id);
                            cardCategoryModels.add(cardCategoryModel);
                        }
                    }

                    JSONArray keywords = jsonObject.getJSONObject(key).getJSONArray("keywords");
                    for (int i = 0; i < keywords.length(); i++) {
                        // Card - Keywords
                        String keyword_tag;
                        int keyword_id;

                        keyword_tag = keywords.getString(i);
                        keyword_id = mDb.keywordDao().getIdByTag(keyword_tag);

                        if (keyword_id > 0) {
                            CardKeywordModel cardKeywordModel = new CardKeywordModel(cardId, keyword_id);
                            cardKeywordModels.add(cardKeywordModel);
                        }
                    }

                    JSONArray loyalties = jsonObject.getJSONObject(key).getJSONArray("loyalties");
                    for (int i = 0; i < loyalties.length(); i++) {
                        // Card - Loyalties
                        String loyalty_name;
                        int loyalty_id;

                        loyalty_name = loyalties.getString(i);
                        loyalty_id = mDb.loyaltyDao().getIdByName(loyalty_name);

                        if (loyalty_id > 0) {
                            CardLoyaltyModel cardLoyaltyModel = new CardLoyaltyModel(cardId, loyalty_id);
                            cardLoyaltyModels.add(cardLoyaltyModel);
                        }
                    }

                    JSONArray related = jsonObject.getJSONObject(key).getJSONArray("related");
                    for (int i = 0; i < related.length(); i++) {
                        // Related Cards
                        String related_tag;
                        int related_id;

                        related_tag = related.getString(i);
                        related_id = mDb.cardDao().getIdByTag(related_tag);

                        if (related_id > 0) {
                            RelatedCardModel relatedCardModel = new RelatedCardModel(cardId, related_id);
                            relatedCardModels.add(relatedCardModel);
                        }
                    }
                }
            }
            mDb.variationDao().insertAll(variationModels);
            mDb.cardCategoryDao().insertAll(cardCategoryModels);
            mDb.cardKeywordDao().insertAll(cardKeywordModels);
            mDb.cardLoyaltyDao().insertAll(cardLoyaltyModels);
            mDb.relatedCardDao().insertAll(relatedCardModels);

        } catch (JSONException e) {
            Log.e(TAG, "GenerateCollection: ", e);
        }
    }

    private static String loadJSONFromAsset(Context ctxt, String fileName) {
        String json;
        try {
            InputStream is = ctxt.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private static Map<String, String> makeMap(String string) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        return gson.fromJson(string, type);
    }
}
