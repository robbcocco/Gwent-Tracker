package com.robbcocco.gwenttracker.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.List;
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

    @Ignore
    private FactionModel factionModel;
    @Ignore
    private List<VariationModel> variationModelList;
    @Ignore
    private List<CategoryModel> categoryModelList;
    @Ignore
    private List<KeywordModel> keywordModelList;
    @Ignore
    private List<LoyaltyModel> loyaltyModelList;
    @Ignore
    private List<CardModel> relatedCardModelList;
    @Ignore
    public CardModel(int id) {
        this.id = id;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CardModel cardModel = (CardModel) o;

        if (id != cardModel.id) return false;
        if (strength != cardModel.strength) return false;
        if (faction_id != cardModel.faction_id) return false;
        if (tag != null ? !tag.equals(cardModel.tag) : cardModel.tag != null) return false;
        if (name != null ? !name.equals(cardModel.name) : cardModel.name != null) return false;
        if (flavor != null ? !flavor.equals(cardModel.flavor) : cardModel.flavor != null)
            return false;
        if (info != null ? !info.equals(cardModel.info) : cardModel.info != null) return false;
        if (info_raw != null ? !info_raw.equals(cardModel.info_raw) : cardModel.info_raw != null)
            return false;
        if (factionModel != null ? !factionModel.equals(cardModel.factionModel) : cardModel.factionModel != null)
            return false;
        if (variationModelList != null ? !variationModelList.equals(cardModel.variationModelList) : cardModel.variationModelList != null)
            return false;
        if (categoryModelList != null ? !categoryModelList.equals(cardModel.categoryModelList) : cardModel.categoryModelList != null)
            return false;
        if (keywordModelList != null ? !keywordModelList.equals(cardModel.keywordModelList) : cardModel.keywordModelList != null)
            return false;
        if (loyaltyModelList != null ? !loyaltyModelList.equals(cardModel.loyaltyModelList) : cardModel.loyaltyModelList != null)
            return false;
        return relatedCardModelList != null ? relatedCardModelList.equals(cardModel.relatedCardModelList) : cardModel.relatedCardModelList == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (tag != null ? tag.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (flavor != null ? flavor.hashCode() : 0);
        result = 31 * result + (info != null ? info.hashCode() : 0);
        result = 31 * result + (info_raw != null ? info_raw.hashCode() : 0);
        result = 31 * result + strength;
        result = 31 * result + faction_id;
        result = 31 * result + (factionModel != null ? factionModel.hashCode() : 0);
        result = 31 * result + (variationModelList != null ? variationModelList.hashCode() : 0);
        result = 31 * result + (categoryModelList != null ? categoryModelList.hashCode() : 0);
        result = 31 * result + (keywordModelList != null ? keywordModelList.hashCode() : 0);
        result = 31 * result + (loyaltyModelList != null ? loyaltyModelList.hashCode() : 0);
        result = 31 * result + (relatedCardModelList != null ? relatedCardModelList.hashCode() : 0);
        return result;
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

    public FactionModel getFactionModel() {
        return factionModel;
    }

    public void setFactionModel(FactionModel factionModel) {
        this.factionModel = factionModel;
    }

    public List<VariationModel> getVariationModelList() {
        return variationModelList;
    }

    public String getRarity(String lang) {
        if (this.getVariationModelList() != null) {
            String values;
            values = "Craft";
            values = values + "\nStandard: " + String.valueOf(this.getVariationModelList().get(0).getRarityModel().getStandard());
            values = values + "\nPremium: " + String.valueOf(this.getVariationModelList().get(0).getRarityModel().getPremium());
            values = values + "\nUpgrade: " + String.valueOf(this.getVariationModelList().get(0).getRarityModel().getUpgrade());
            values = values + "\nMill";
            values = values + "\nStandard: " + String.valueOf(this.getVariationModelList().get(0).getRarityModel().getMill_standard());
            values = values + "\nPremium: " + String.valueOf(this.getVariationModelList().get(0).getRarityModel().getMill_premium());
            return values;
        }
        return "";
    }

    public void setVariationModelList(List<VariationModel> variationModelList) {
        this.variationModelList = variationModelList;
    }

    public List<CategoryModel> getCategoryModelList() {
        return categoryModelList;
    }

    public String getCategories(String lang) {
        if (this.getCategoryModelList() != null) {
            String categories = null;
            for (CategoryModel categoryModel : this.getCategoryModelList()) {
                if (categories != null) {
                    categories = categories + ", " + categoryModel.getName().get(lang);
                } else {
                    categories = categoryModel.getName().get(lang);
                }
            }
            return categories;
        }
        return "";
    }

    public void setCategoryModelList(List<CategoryModel> categoryModelList) {
        this.categoryModelList = categoryModelList;
    }

    public List<KeywordModel> getKeywordModelList() {
        return keywordModelList;
    }

    public String getKeywords(String lang) {
        if (this.getKeywordModelList() != null) {
            String keywords = null;
            String keyword = null;
            for (KeywordModel keywordModel : this.getKeywordModelList()) {
                keyword = keywordModel.getInfo().get(lang);
                if (keywords != null) {
                    keywords = keywords + "\n\n" + keyword;
                } else {
                    keywords = keyword;
                }
            }
            return keywords;
        }
        return "";
    }

    public void setKeywordModelList(List<KeywordModel> keywordModelList) {
        this.keywordModelList = keywordModelList;
    }

    public List<LoyaltyModel> getLoyaltyModelList() {
        return loyaltyModelList;
    }

    public void setLoyaltyModelList(List<LoyaltyModel> loyaltyModelList) {
        this.loyaltyModelList = loyaltyModelList;
    }

    public List<CardModel> getRelatedCardModelList() {
        return relatedCardModelList;
    }

    public void setRelatedCardModelList(List<CardModel> relatedCardModelList) {
        this.relatedCardModelList = relatedCardModelList;
    }
}
