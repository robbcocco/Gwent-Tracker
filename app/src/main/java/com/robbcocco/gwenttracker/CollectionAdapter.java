package com.robbcocco.gwenttracker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.robbcocco.gwenttracker.database.entity.CardModel;
import com.robbcocco.gwenttracker.database.entity.CategoryModel;

import java.util.List;

import static com.bumptech.glide.request.target.Target.SIZE_ORIGINAL;

/**
 * Created by rober on 3/3/2018.
 */

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.RecyclerViewHolder> {

    private List<CardModel> cardModelList;

    public CollectionAdapter(List<CardModel> cardModelList) {
        setHasStableIds(true);
        this.cardModelList = cardModelList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.collection_card, parent, false));
    }

    @Override
    public void onBindViewHolder(final CollectionAdapter.RecyclerViewHolder holder, final int position) {
        final CardModel cardModel = cardModelList.get(position);

        holder.cardArt.setImageResource(R.drawable.placeholder_card_low);
        if (cardModel.getVariationModelList() != null) {
            if (!cardModel.getVariationModelList().isEmpty()) {
                if (cardModel.getVariationModelList().get(0).getArt_low() != null) {
                    GlideApp.with(holder.itemView)
                            .load(cardModel.getVariationModelList().get(0).getArt_low().toString())
                            .override(SIZE_ORIGINAL, SIZE_ORIGINAL)
                            .placeholder(R.drawable.placeholder_card_low)
                            .into(holder.cardArt);
                }
            }
        }

        holder.cardName.setText(cardModel.getName().get("en-US"));
        holder.cardName.setSelected(true);
        if (cardModel.getCategoryModelList() != null) {
            String categories = null;
            for (CategoryModel categoryModel : cardModel.getCategoryModelList()) {
                if (categories != null) {
                    categories = categories + ", " + categoryModel.getName().get("en-US");
                } else {
                    categories = categoryModel.getName().get("en-US");
                }
            }
            holder.cardCategories.setText(categories);
            holder.cardCategories.setSelected(true);
        }
        holder.itemView.setTag(cardModel);
    }

    @Override
    public int getItemCount() {
        return cardModelList.size();
    }

    public void updateCardModelList(List<CardModel> cardModelList) {
        this.cardModelList = cardModelList;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private ImageView cardArt;
        private TextView cardName;
        private TextView cardCategories;

        RecyclerViewHolder(View view) {
            super(view);
            cardArt = (ImageView) view.findViewById(R.id.collection_card_art);
            cardName = (TextView) view.findViewById(R.id.collection_card_name);
            cardCategories = (TextView) view.findViewById(R.id.collection_card_categories);
        }
    }
}
