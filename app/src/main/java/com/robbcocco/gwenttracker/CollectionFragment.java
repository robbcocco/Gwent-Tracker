package com.robbcocco.gwenttracker;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.robbcocco.gwenttracker.database.entity.CardModel;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.request.target.Target.SIZE_ORIGINAL;

/**
 * Created by rober on 3/3/2018.
 */

public class CollectionFragment extends Fragment {
    private CollectionViewModel viewModel;

    private CollectionAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;

    public CollectionFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static CollectionFragment newInstance() {
        CollectionFragment fragment = new CollectionFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_collection, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.collection_list);
        recyclerViewAdapter = new CollectionAdapter(new ArrayList<CardModel>());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        recyclerView.setAdapter(recyclerViewAdapter);

//        viewModel = ViewModelProviders.of(this).get(CollectionViewModel.class);
        viewModel = CollectionViewModel.getInstance(this);

        viewModel.getCardModelList().observe(getActivity(), new Observer<List<CardModel>>() {
            @Override
            public void onChanged(@Nullable List<CardModel> cardModelList) {
                recyclerViewAdapter.updateCardModelList(cardModelList);
            }
        });

        return rootView;
    }


    private class CollectionAdapter extends RecyclerView.Adapter<CollectionFragment.RecyclerViewHolder> {

        private List<CardModel> cardModelList;

        public CollectionAdapter(List<CardModel> cardModelList) {
            this.cardModelList = cardModelList;
        }

        @Override
        public CollectionFragment.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CollectionFragment.RecyclerViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.collection_card, parent, false), cardModelList);
        }

        @Override
        public void onBindViewHolder(final CollectionFragment.RecyclerViewHolder holder, final int position) {
            final CardModel cardModel = cardModelList.get(position);

            holder.cardArt.setImageResource(R.drawable.placeholder_card_low);
            if (cardModel.getVariationModelList() != null &&
                    !cardModel.getVariationModelList().isEmpty() &&
                    cardModel.getVariationModelList().get(0).getArt_low() != null) {
                GlideApp.with(holder.itemView)
                        .load(cardModel.getVariationModelList().get(0).getArt_low().toString())
                        .override(SIZE_ORIGINAL, SIZE_ORIGINAL)
                        .placeholder(R.drawable.placeholder_card_low)
                        .into(holder.cardArt);
            }

            holder.cardName.setText(cardModel.getName().get("en-US"));
            holder.cardName.setSelected(true);

            holder.cardCategories.setText(cardModel.getCategories("en-US"));
            holder.cardCategories.setSelected(true);

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
    }


    private class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private int cardId;
        private List<CardModel> cardModelList;
        private ImageView cardArt;
        private TextView cardName;
        private TextView cardCategories;

        RecyclerViewHolder(View view, List<CardModel> cardModelList) {
            super(view);
            itemView.setOnClickListener(this);

            this.cardModelList = cardModelList;
            cardArt = (ImageView) view.findViewById(R.id.collection_card_art);
            cardName = (TextView) view.findViewById(R.id.collection_card_name);
            cardCategories = (TextView) view.findViewById(R.id.collection_card_categories);
        }

        @Override
        public void onClick(View view) {
            cardId = cardModelList.get(getAdapterPosition()).id;
            Intent intent = CardDetailActivity.newIntent(getActivity(), cardId);
            startActivity(intent);
        }
    }
}
