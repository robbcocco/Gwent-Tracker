package com.robbcocco.gwenttracker;

import android.app.ActivityOptions;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.robbcocco.gwenttracker.database.CardDatabase;
import com.robbcocco.gwenttracker.database.entity.CardModel;
import com.robbcocco.gwenttracker.database.helper.CardHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.bumptech.glide.request.target.Target.SIZE_ORIGINAL;

/**
 * Created by rober on 3/3/2018.
 */

public class CollectionFragment extends Fragment {
    private SharedPreferences sharedPreferences;
    private static String LANGUAGE="en-US";

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        LANGUAGE = sharedPreferences.getString("lang_list", "en-US");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_collection, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.collection_list);
        recyclerViewAdapter = new CollectionAdapter(new ArrayList<CardModel>());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        recyclerView.setAdapter(recyclerViewAdapter);

        new GetCardListTask(recyclerViewAdapter).execute(getActivity().getApplicationContext());

        return rootView;
    }


    public class CollectionAdapter extends RecyclerView.Adapter<CollectionFragment.RecyclerViewHolder> {

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

            if (cardModel.getVariationModelList() != null &&
                    !cardModel.getVariationModelList().isEmpty() &&
                    cardModel.getVariationModelList().get(0).getArt_low() != null) {
                GlideApp.with(holder.itemView)
                        .load(cardModel.getVariationModelList().get(0).getArt_low().toString())
                        .override(SIZE_ORIGINAL, SIZE_ORIGINAL)
                        .into(holder.cardArt);
            }

            holder.cardName.setText(cardModel.getName().get(LANGUAGE));
            holder.cardName.setSelected(true);

            holder.itemView.setTag(cardModel);
        }

        @Override
        public int getItemCount() {
            return cardModelList.size();
        }

        public void updateCardModelList(List<CardModel> cardModelList) {
            this.cardModelList = cardModelList;
            notifyDataSetChanged();

            for (CardModel cardModel : cardModelList) {
                new GetCardDetailTask(recyclerViewAdapter, cardModel.id, cardModelList.indexOf(cardModel))
                        .execute(getActivity().getApplicationContext());
            }
        }

        public void updateCardModelAtPosition(CardModel cardModel, int position) {
            this.cardModelList.set(position, cardModel);
            notifyItemChanged(position);
        }
    }


    private class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private int cardId;
        private List<CardModel> cardModelList;
        private ImageView cardArt;
        private TextView cardName;

        RecyclerViewHolder(View view, List<CardModel> cardModelList) {
            super(view);
            itemView.setOnClickListener(this);

            this.cardModelList = cardModelList;
            cardArt = (ImageView) view.findViewById(R.id.collection_card_art);
            cardName = (TextView) view.findViewById(R.id.collection_card_name);
        }

        @Override
        public void onClick(View view) {
            cardId = cardModelList.get(getAdapterPosition()).id;
            Intent intent = CardDetailActivity.newIntent(getActivity(), cardId);
            startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
        }
    }


    public static class GetCardListTask extends AsyncTask<Context, Void, List<CardModel>> {

        private final CollectionAdapter collectionAdapter;

        public GetCardListTask(CollectionAdapter collectionAdapter) {
            this.collectionAdapter = collectionAdapter;
        }

        @Override
        protected List<CardModel> doInBackground(Context... context) {
            CardDatabase mDb = CardDatabase.getDatabase(context[0]);
            List<CardModel> cardModelList = mDb.cardDao().loadAllCards();

            Collections.sort(cardModelList, new Comparator<CardModel>() {
                @Override
                public int compare(CardModel c1, CardModel c2) {
                    return c1.getName().get(LANGUAGE)
                            .compareToIgnoreCase(c2.getName().get(LANGUAGE));
                }
            });

            return cardModelList;
        }

        @Override
        protected void onPostExecute(List<CardModel> result) {
            collectionAdapter.updateCardModelList(result);
        }
    }


    public class GetCardDetailTask extends AsyncTask<Context, Void, CardModel> {

        private final CollectionAdapter collectionAdapter;

        private final int cardId;
        private final int position;

        public GetCardDetailTask(CollectionAdapter collectionAdapter, int cardId, int position) {
            this.collectionAdapter = collectionAdapter;
            this.cardId = cardId;
            this.position = position;
        }

        @Override
        protected CardModel doInBackground(Context... context) {
            CardDatabase mDb = CardDatabase.getDatabase(context[0]);
            CardHelper cardHelper = new CardHelper(mDb);

            return cardHelper.findCardById(cardId);
        }

        @Override
        protected void onPostExecute(CardModel result) {
            collectionAdapter.updateCardModelAtPosition(result, position);
        }
    }
}
