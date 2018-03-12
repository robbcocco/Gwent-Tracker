package com.robbcocco.gwenttracker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.robbcocco.gwenttracker.database.entity.CategoryModel;
import com.robbcocco.gwenttracker.database.entity.FactionModel;
import com.robbcocco.gwenttracker.database.helper.CardHelper;

import java.util.ArrayList;
import java.util.Collections;
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
    private List<Integer> factionIds = new ArrayList<>();
    private CategoryListAdapter categoryListAdapter;
    private RecyclerView categoryListRecyclerView;
    private List<CategoryModel> categoryModelList;
    private List<Integer> categoryIds = new ArrayList<>();
    private String searchQuery="";

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
        new GetCategoryListTask(categoryListAdapter).execute(getActivity().getApplicationContext());
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

        categoryListRecyclerView = (RecyclerView) getActivity().findViewById(R.id.filter_categories);
        categoryListAdapter = new CategoryListAdapter(new ArrayList<CategoryModel>());
        categoryListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryListRecyclerView.setAdapter(categoryListAdapter);

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
                factionIds = new ArrayList<>();
                factionListAdapter.notifyDataSetChanged();
                categoryIds = new ArrayList<>();
                categoryListAdapter.notifyDataSetChanged();
                filter();

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
        searchQuery=query;
        filter();
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private void filter() {
        collectionViewAdapter.replaceAll(filter(filter(cardModelList, factionIds, categoryIds), searchQuery));
        collectionRecyclerView.scrollToPosition(0);
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

    private static List<CardModel> filter(List<CardModel> models, List<Integer> factionsId, List<Integer> categoryIds) {
        final List<CardModel> cardsByFaction = new ArrayList<>();
        if (!factionsId.isEmpty()) {
            for (CardModel model : models) {
                if (factionsId.contains(model.getFaction_id())) {
                    cardsByFaction.add(model);
                }
            }
        }
        else {
            cardsByFaction.addAll(models);
        }

        final List<CardModel> cardsByCategory = new ArrayList<>();
        if (!categoryIds.isEmpty()) {
            for (CardModel model : cardsByFaction) {
                Boolean hasCategory = false;
                if (model.getCategoryModelList() != null) {
                    for (CategoryModel categoryModel : model.getCategoryModelList()) {
                        if (categoryIds.contains(categoryModel.id)) {
                            hasCategory = true;
                        }
                    }
                }
                if (hasCategory) {
                    cardsByCategory.add(model);
                }
            }
        }
        else {
            cardsByCategory.addAll(cardsByFaction);
        }

        return cardsByCategory;
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
    public class FactionListAdapter extends RecyclerView.Adapter<FactionListViewHolder> {

        public FactionListAdapter(List<FactionModel> models) {
            factionModelList = models;
        }

        @Override
        public FactionListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new FactionListViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.filter_button, parent, false));
        }

        @Override
        public void onBindViewHolder(FactionListViewHolder holder, int position) {
            FactionModel model = factionModelList.get(position);

            holder.button.setText(model.getName().get(LANGUAGE));
            if (factionIds.isEmpty() || !factionIds.contains(model.id)) {
                holder.button.setSelected(false);
            }
            else {
                holder.button.setSelected(true);
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

    private class FactionListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private int id;
        private Button button;

        FactionListViewHolder(View view) {
            super(view);

            button = (Button) view.findViewById(R.id.filter_button);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            id = factionModelList.get(getAdapterPosition()).id;
            if (factionIds.contains(id)) {
                factionIds.remove((Object) id);
                button.setSelected(false);
            }
            else {
                factionIds.add(id);
                button.setSelected(true);
            }
            filter();
        }
    }

    public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListViewHolder> {

        public CategoryListAdapter(List<CategoryModel> models) {
            categoryModelList = models;
        }

        @Override
        public CategoryListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CategoryListViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.filter_button, parent, false));
        }

        @Override
        public void onBindViewHolder(CategoryListViewHolder holder, int position) {
            CategoryModel model = categoryModelList.get(position);

            holder.button.setText(model.getName().get(LANGUAGE));
            if (categoryIds.isEmpty() || !categoryIds.contains(model.id)) {
                holder.button.setSelected(false);
            }
            else {
                holder.button.setSelected(true);
            }

            holder.itemView.setTag(model);
        }

        @Override
        public int getItemCount() {
            return categoryModelList.size();
        }

        public void updateModelList(List<CategoryModel> models) {
            categoryModelList = models;
            notifyDataSetChanged();
        }
    }

    private class CategoryListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private int id;
        private Button button;

        CategoryListViewHolder(View view) {
            super(view);

            button = (Button) view.findViewById(R.id.filter_button);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            id = categoryModelList.get(getAdapterPosition()).id;
            if (categoryIds.contains(id)) {
                categoryIds.remove((Object) id);
                button.setSelected(false);
            }
            else {
                categoryIds.add(id);
                button.setSelected(true);
            }
            filter();
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

    public static class GetCategoryListTask extends AsyncTask<Context, Void, List<CategoryModel>> {

        private final CategoryListAdapter adapter;

        GetCategoryListTask(CategoryListAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        protected List<CategoryModel> doInBackground(Context... context) {
            CardDatabase mDb = CardDatabase.getDatabase(context[0]);
            List<CategoryModel> modelList = mDb.categoryDao().loadAllCategories();

            Collections.sort(modelList, new Comparator<CategoryModel>() {
                @Override
                public int compare(CategoryModel o1, CategoryModel o2) {
                    return o1.getName().get(LANGUAGE).compareToIgnoreCase(o2.getName().get(LANGUAGE));
                }
            });

            return modelList;
        }

        @Override
        protected void onPostExecute(List<CategoryModel> result) {
            adapter.updateModelList(result);
        }
    }
}
