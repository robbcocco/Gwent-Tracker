package com.robbcocco.gwenttracker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.robbcocco.gwenttracker.database.pojo.CardInfo;

import java.util.List;

import static com.bumptech.glide.request.target.Target.SIZE_ORIGINAL;

/**
 * Created by rober on 3/3/2018.
 */

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.RecyclerViewHolder> {

    private List<CardInfo> cardModelList;

    public CollectionAdapter(List<CardInfo> cardModelList) {
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
        final CardInfo cardModel = cardModelList.get(position);

        holder.cardArt.setImageResource(R.drawable.placeholder_card_low);
        if (!cardModel.getVariationModelList().isEmpty()) {
            if (cardModel.getVariationModelList().get(0).getArt_low() != null) {
                GlideApp
                        .with(holder.itemView)
                        .load(cardModel.getVariationModelList().get(0).getArt_low().toString())
                        .override(SIZE_ORIGINAL, SIZE_ORIGINAL)
                        .placeholder(R.drawable.placeholder_card_low)
                        .into(holder.cardArt);
            }
        }

        holder.cardName.setText(cardModel.getCardModel().getName().get("en-US"));
        holder.itemView.setTag(cardModel);
    }

    @Override
    public int getItemCount() {
        return cardModelList.size();
    }

    public void updateCardModelList(List<CardInfo> cardModelList) {
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

        RecyclerViewHolder(View view) {
            super(view);
            cardArt = (ImageView) view.findViewById(R.id.collection_card_art);
            cardName = (TextView) view.findViewById(R.id.collection_card_name);
        }
    }
}
