package com.robbcocco.gwenttracker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.util.SortedList;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robbcocco.gwenttracker.database.CardDatabase;
import com.robbcocco.gwenttracker.database.entity.CardModel;
import com.robbcocco.gwenttracker.database.entity.FactionModel;
import com.robbcocco.gwenttracker.database.helper.CardHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.bumptech.glide.request.target.Target.SIZE_ORIGINAL;

/**
 * Created by rober on 3/3/2018.
 */

public class CollectionFragment extends Fragment implements SearchView.OnQueryTextListener {
    private SharedPreferences sharedPreferences;
    private static String LANGUAGE="en-US";

    private LinearLayout filters;
    private FloatingActionButton fab;
    private FactionListAdapter factionListAdapter;
    private RecyclerView factionListRecyclerView;
    private List<FactionModel> factionModelList;
    private List<Integer> factionsId=new ArrayList<>();

    private CollectionAdapter collectionViewAdapter;
    private RecyclerView collectionRecyclerView;

    private final Comparator<CardModel> ALPHABETICAL_COMPARATOR = new Comparator<CardModel>() {
        @Override
        public int compare(CardModel a, CardModel b) {
            return a.getName().get(LANGUAGE)
                    .compareToIgnoreCase(b.getName().get(LANGUAGE));
        }
    };
    private final SortedList.Callback<CardModel> mCallback = new SortedList.Callback<CardModel>() {
        @Override
        public void onInserted(int position, int count) {
            collectionViewAdapter.notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            collectionViewAdapter.notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            collectionViewAdapter.notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onChanged(int position, int count) {
            collectionViewAdapter.notifyItemRangeChanged(position, count);
        }

        @Override
        public int compare(CardModel a, CardModel b) {
            return ALPHABETICAL_COMPARATOR.compare(a, b);
        }

        @Override
        public boolean areContentsTheSame(CardModel oldItem, CardModel newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areItemsTheSame(CardModel item1, CardModel item2) {
            return item1.id == item2.id;
        }
    };
    private List<CardModel> cardModelList;
    private final SortedList<CardModel> mSortedList = new SortedList<>(CardModel.class, mCallback);

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

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_collection, container, false);

//        TabItem fragmentTabItem = getActivity().findViewById(R.id.tabItem);
//        fragmentTabItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                collectionRecyclerView.scrollToPosition(0);
//            }
//        });

        collectionRecyclerView = (RecyclerView) rootView.findViewById(R.id.collection_list);
        collectionViewAdapter = new CollectionAdapter(new ArrayList<CardModel>());
        collectionRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        collectionRecyclerView.setAdapter(collectionViewAdapter);

        setupFiltersView();

        executeAsyncTasks();

        return rootView;
    }

    public void executeAsyncTasks() {
        new GetCardListTask(collectionViewAdapter).execute(getActivity().getApplicationContext());
        new GetFactionListTask(factionListAdapter).execute(getActivity().getApplicationContext());
    }

    private void setupFiltersView() {
        filters = (LinearLayout) getActivity().findViewById(R.id.filters);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        FloatingActionButton close = getActivity().findViewById(R.id.filters_close);
        Button reset = getActivity().findViewById(R.id.filters_reset);

        factionListRecyclerView = (RecyclerView) getActivity().findViewById(R.id.filter_factions);
        factionListAdapter = new FactionListAdapter(new ArrayList<FactionModel>());
        factionListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        factionListRecyclerView.setAdapter(factionListAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                revealFilters();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideFilters();
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideFilters();
                factionsId=new ArrayList<>();
                factionListAdapter.notifyDataSetChanged();
                final List<CardModel> filteredModelList = filter(cardModelList, factionsId);

                collectionViewAdapter.replaceAll(filteredModelList);
                collectionRecyclerView.scrollToPosition(0);

            }
        });
    }

    private void revealFilters() {
        int fabMargin = (int) getResources().getDimension(R.dimen.fab_margin);
        int filterscx = filters.getWidth() - (fab.getWidth()/2 + fabMargin);
        int filterscy = filters.getHeight() - (fab.getHeight()/2 + fabMargin);
        float filtersRadius = (float) Math.hypot(filterscx, filterscy);

        int fabcx = fab.getWidth()/2;
        int fabcy = fab.getHeight()/2;
        float fabRadius = (float) Math.hypot(fabcx, fabcy);

        Animator filtersAnim =
                ViewAnimationUtils.createCircularReveal(filters, filterscx, filterscy, fabRadius, filtersRadius);

        fab.setVisibility(View.INVISIBLE);

        filters.setVisibility(View.VISIBLE);
        filtersAnim.start();
    }

    private void hideFilters() {
        int fabMargin = (int) getResources().getDimension(R.dimen.fab_margin);
        int filterscx = filters.getWidth() - (fab.getWidth()/2 + fabMargin);
        int filterscy = filters.getHeight() - (fab.getHeight()/2 + fabMargin);
        float filtersRadius = (float) Math.hypot(filterscx, filterscy);

        int fabcx = fab.getWidth()/2;
        int fabcy = fab.getHeight()/2;
        float fabRadius = (float) Math.hypot(fabcx, fabcy);

        Animator filtersAnim =
                ViewAnimationUtils.createCircularReveal(filters, filterscx, filterscy, filtersRadius, fabRadius);

        filtersAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                filters.setVisibility(View.INVISIBLE);

                fab.setVisibility(View.VISIBLE);
            }
        });

        filtersAnim.start();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextChange(String query) {
        final List<CardModel> filteredModelList = filter(filter(cardModelList, query), factionsId);
        collectionViewAdapter.replaceAll(filteredModelList);
        collectionRecyclerView.scrollToPosition(0);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private static List<CardModel> filter(List<CardModel> models, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<CardModel> filteredModelList = new ArrayList<>();
        for (CardModel model : models) {
            final String text = model.getName().get(LANGUAGE).toLowerCase() + model.getInfo().get(LANGUAGE).toLowerCase();
            if (text.contains(lowerCaseQuery)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    private static List<CardModel> filter(List<CardModel> models, List<Integer> factionsId) {
        final List<CardModel> filteredModelList = new ArrayList<>();
        if (factionsId.isEmpty()) {
            return models;
        }
        for (CardModel model : models) {
            if (factionsId.contains(model.getFaction_id())) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }


    /**
     * Cards list classes
     */
    public class CollectionAdapter extends RecyclerView.Adapter<CollectionViewHolder> {

        public CollectionAdapter(List<CardModel> cardModels) {
            cardModelList = cardModels;
        }

        @Override
        public CollectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CollectionViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.collection_card, parent, false));
        }

        @Override
        public void onBindViewHolder(final CollectionViewHolder holder, final int position) {
            final CardModel cardModel = mSortedList.get(position);

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
            return mSortedList.size();
        }

        public void updateCardModelList(List<CardModel> cardModels) {
            cardModelList = cardModels;
            add(cardModels);

            for (CardModel cardModel : cardModelList) {
                new GetCardDetailTask(collectionViewAdapter, cardModel.id, cardModelList.indexOf(cardModel))
                        .execute(getActivity().getApplicationContext());
            }
        }

        public void updateCardModelAtPosition(CardModel cardModel, int position) {
            cardModelList.set(position, cardModel);
            add(cardModel);
        }

        public void add(CardModel model) {
            mSortedList.add(model);
        }

        public void remove(CardModel model) {
            mSortedList.remove(model);
        }

        public void add(List<CardModel> models) {
            mSortedList.addAll(models);
        }

        public void remove(List<CardModel> models) {
            mSortedList.beginBatchedUpdates();
            for (CardModel model : models) {
                mSortedList.remove(model);
            }
            mSortedList.endBatchedUpdates();
        }

        public void replaceAll(List<CardModel> models) {
            mSortedList.beginBatchedUpdates();
            for (int i = mSortedList.size() - 1; i >= 0; i--) {
                final CardModel model = mSortedList.get(i);
                if (!models.contains(model)) {
                    mSortedList.remove(model);
                }
            }
            mSortedList.addAll(models);
            mSortedList.endBatchedUpdates();
        }
    }

    private class CollectionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private int cardId;
        private ImageView cardArt;
        private TextView cardName;

        CollectionViewHolder(View view) {
            super(view);
            itemView.setOnClickListener(this);

            cardArt = (ImageView) view.findViewById(R.id.collection_card_art);
            cardName = (TextView) view.findViewById(R.id.collection_card_name);
        }

        @Override
        public void onClick(View view) {
            cardId = mSortedList.get(getAdapterPosition()).id;
            Intent intent = CardDetailActivity.newIntent(getActivity(), cardId);
            startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
        }
    }

    /**
     * Filter lists classes
     */
    public class FactionListAdapter extends RecyclerView.Adapter<FilterListViewHolder> {

        public FactionListAdapter(List<FactionModel> models) {
            factionModelList = models;
        }

        @Override
        public FilterListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new FilterListViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.filter_button, parent, false));
        }

        @Override
        public void onBindViewHolder(FilterListViewHolder holder, int position) {
            FactionModel model = factionModelList.get(position);

            holder.button.setText(model.getName().get(LANGUAGE));
            if (factionsId.isEmpty() || !factionsId.contains(model.id)) {
                holder.button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
            }
            else {
                holder.button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccentDark)));
            }

            holder.itemView.setTag(model);
        }

        @Override
        public int getItemCount() {
            return factionModelList.size();
        }

        public void updateModelList(List<FactionModel> models) {
            factionModelList = models;
            notifyDataSetChanged();
        }
    }

    private class FilterListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private int id;
        private Button button;

        FilterListViewHolder(View view) {
            super(view);

            button = (Button) view.findViewById(R.id.filter_button);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            id = factionModelList.get(getAdapterPosition()).id;
            if (factionsId.contains(id)) {
                button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                factionsId.remove((Object) id);
            }
            else {
                button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccentDark)));
                factionsId.add(id);
            }
            final List<CardModel> filteredModelList = filter(cardModelList, factionsId);

            collectionViewAdapter.replaceAll(filteredModelList);
            collectionRecyclerView.scrollToPosition(0);
        }
    }

    /**
     * Async tasks
     */
    public static class GetCardListTask extends AsyncTask<Context, Void, List<CardModel>> {

        private final CollectionAdapter collectionAdapter;

        GetCardListTask(CollectionAdapter collectionAdapter) {
            this.collectionAdapter = collectionAdapter;
        }

        @Override
        protected List<CardModel> doInBackground(Context... context) {
            CardDatabase mDb = CardDatabase.getDatabase(context[0]);
            List<CardModel> cardModelList = mDb.cardDao().loadAllCards();

            return cardModelList;
        }

        @Override
        protected void onPostExecute(List<CardModel> result) {
            collectionAdapter.updateCardModelList(result);
        }
    }

    public static class GetCardDetailTask extends AsyncTask<Context, Void, CardModel> {

        private final CollectionAdapter collectionAdapter;

        private final int cardId;
        private final int position;

        GetCardDetailTask(CollectionAdapter collectionAdapter, int cardId, int position) {
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

    public static class GetFactionListTask extends AsyncTask<Context, Void, List<FactionModel>> {

        private final FactionListAdapter adapter;

        GetFactionListTask(FactionListAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        protected List<FactionModel> doInBackground(Context... context) {
            CardDatabase mDb = CardDatabase.getDatabase(context[0]);
            List<FactionModel> modelList = mDb.factionDao().loadAllFactions();

            return modelList;
        }

        @Override
        protected void onPostExecute(List<FactionModel> result) {
            adapter.updateModelList(result);
        }
    }
}
