package com.robbcocco.gwenttracker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityOptions;
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

import com.facebook.shimmer.ShimmerFrameLayout;
import com.robbcocco.gwenttracker.database.entity.CardModel;
import com.robbcocco.gwenttracker.database.entity.CategoryModel;
import com.robbcocco.gwenttracker.database.entity.FactionModel;
import com.robbcocco.gwenttracker.database.entity.RarityModel;
import com.robbcocco.gwenttracker.tasks.GetCardListCallback;
import com.robbcocco.gwenttracker.tasks.GetCardListTask;
import com.robbcocco.gwenttracker.tasks.GetDBCategoryListTask;
import com.robbcocco.gwenttracker.tasks.GetDBFactionListTask;
import com.robbcocco.gwenttracker.tasks.GetDBListCallback;
import com.robbcocco.gwenttracker.tasks.GetDBRarityListTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.bumptech.glide.request.target.Target.SIZE_ORIGINAL;

/**
 * Created by rober on 3/3/2018.
 */

public class CollectionFragment extends Fragment implements SearchView.OnQueryTextListener {
    private String TAG = "CollectionFragment";

    private SharedPreferences sharedPreferences;
    private static String LANGUAGE="en-US";

    private GetCardListTask getCardListTask;
//    private GetCardDetailTask getCardDetailTask;
    private GetDBFactionListTask getDBFactionListTask;
    private GetDBCategoryListTask getDBCategoryListTask;
    private GetDBRarityListTask getDBRarityListTask;

    private View rootView;
    private ShimmerFrameLayout mShimmerViewContainer;
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
    private RarityListAdapter rarityListAdapter;
    private RecyclerView rarityListRecyclerView;
    private List<RarityModel> rarityModelList;
    private List<Integer> rarityIds = new ArrayList<>();
    private String searchQuery="";
    private Boolean filtersVisible=false;

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
        if (rootView != null) {
            return rootView;
        }
        rootView = inflater.inflate(R.layout.fragment_collection, container, false);
        setRetainInstance(true);

        setupFiltersView();
        setupCollectionView(rootView);
        executeAsyncTasks();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (!LANGUAGE.equals(sharedPreferences.getString("lang_list", "en-US"))) {
            LANGUAGE = sharedPreferences.getString("lang_list", "en-US");
            collectionViewAdapter.replaceAll(cardModelList);
            factionListAdapter.notifyDataSetChanged();
            categoryListAdapter.updateModelList(categoryModelList);
            rarityListAdapter.notifyDataSetChanged();
        }

        if (cardModelList.isEmpty()) {
            mShimmerViewContainer.startShimmerAnimation();
            executeAsyncTasks();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mShimmerViewContainer.stopShimmerAnimation();

        getCardListTask.cancel(true);
//        getCardDetailTask.cancel(true);
        getDBFactionListTask.cancel(true);
        getDBCategoryListTask.cancel(true);
        getDBRarityListTask.cancel(true);
    }

    public void setupCollectionView(View view) {
        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_collection_list);
        mShimmerViewContainer.startShimmerAnimation();

        collectionRecyclerView = view.findViewById(R.id.collection_list);
        collectionViewAdapter = new CollectionAdapter(new ArrayList<CardModel>());
        collectionRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        collectionRecyclerView.setAdapter(collectionViewAdapter);

        collectionRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy == 0) {
                    if (!filtersVisible) {
                        fab.show();
                    }
                }
                else if(dy < 0) {
                    if (filtersVisible) {
                        hideFilters();
                    }
                    fab.show();
                }
                else {
                    if (filtersVisible) {
                        hideFilters();
                    }
                    fab.hide();
                }

                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    public void executeAsyncTasks() {
        GetCardListCallback getCardListInterface = new GetCardListCallback() {
            @Override
            public void updateAdapter(CardModel result) {
                collectionViewAdapter.updateCardModel(result);
            }

            @Override
            public void updateAdapter(List result) {
                collectionViewAdapter.updateCardModelList(result);
            }
        };
        getCardListTask = new GetCardListTask(getCardListInterface);
        getCardListTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, getActivity());

        GetDBListCallback getDBListCallback = new GetDBListCallback() {
            @Override
            public void updateAdapter(List result) {
                factionListAdapter.updateModelList(result);
            }
        };
        getDBFactionListTask = new GetDBFactionListTask(getDBListCallback);
        getDBFactionListTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, getActivity());

        getDBListCallback = new GetDBListCallback() {
            @Override
            public void updateAdapter(List result) {
                categoryListAdapter.updateModelList(result);
            }
        };
        getDBCategoryListTask = new GetDBCategoryListTask(getDBListCallback);
        getDBCategoryListTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, getActivity());

        getDBListCallback = new GetDBListCallback() {
            @Override
            public void updateAdapter(List result) {
                Collections.sort(result, new Comparator<RarityModel>() {
                    @Override
                    public int compare(RarityModel o1, RarityModel o2) {
                        return ((Integer) o1.getStandard()).compareTo((Integer) o2.getStandard());
                    }
                });
                rarityListAdapter.updateModelList(result);
            }
        };
        getDBRarityListTask = new GetDBRarityListTask(getDBListCallback);
        getDBRarityListTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, getActivity());
    }

    private void setupFiltersView() {
        filters = getActivity().findViewById(R.id.filters);
        fab = getActivity().findViewById(R.id.fab);
        FloatingActionButton close = getActivity().findViewById(R.id.filters_close);
        Button reset = getActivity().findViewById(R.id.filters_reset);

        factionListRecyclerView = getActivity().findViewById(R.id.filter_factions);
        factionListAdapter = new FactionListAdapter(new ArrayList<FactionModel>());
        factionListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        factionListRecyclerView.setAdapter(factionListAdapter);

        categoryListRecyclerView = getActivity().findViewById(R.id.filter_categories);
        categoryListAdapter = new CategoryListAdapter(new ArrayList<CategoryModel>());
        categoryListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryListRecyclerView.setAdapter(categoryListAdapter);

        rarityListRecyclerView = getActivity().findViewById(R.id.filter_rarities);
        rarityListAdapter = new RarityListAdapter(new ArrayList<RarityModel>());
        rarityListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rarityListRecyclerView.setAdapter(rarityListAdapter);

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
                rarityIds = new ArrayList<>();
                rarityListAdapter.notifyDataSetChanged();
                filter();

            }
        });
    }

    private void revealFilters() {
        filtersVisible = true;

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
        filtersVisible = false;

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
        collectionViewAdapter.replaceAll(filter(filter(cardModelList, factionIds, categoryIds, rarityIds), searchQuery));
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

    private static List<CardModel> filter(List<CardModel> models, List<Integer> factionsId, List<Integer> categoryIds, List<Integer> rarityIds) {
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

        final List<CardModel> cardsByRarity = new ArrayList<>();
        if (!rarityIds.isEmpty()) {
            for (CardModel model : cardsByCategory) {
                if (model.getVariationModelList() != null && !model.getVariationModelList().isEmpty()) {
                    if (rarityIds.contains(model.getVariationModelList().get(0).getRarity_id())) {
                        cardsByRarity.add(model);
                    }
                }
            }
        }
        else {
            cardsByRarity.addAll(cardsByCategory);
        }

        return cardsByRarity;
    }


    /**
     * Cards list classes
     */
    public class CollectionAdapter extends RecyclerView.Adapter<CollectionViewHolder> {

        public CollectionAdapter(List<CardModel> cardModels) {
            setHasStableIds(true);
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

        @Override
        public long getItemId(int position) {
            return mSortedList.get(position).id;
        }

        public void updateCardModelList(List<CardModel> cardModels) {
            cardModelList = cardModels;
            mSortedList.addAll(cardModels);
            mShimmerViewContainer.stopShimmerAnimation();
            mShimmerViewContainer.setVisibility(View.GONE);
        }

        public void updateCardModel(CardModel cardModel) {
            for (CardModel card : cardModelList) {
                if (card.id == cardModel.id) {
                    cardModelList.set(cardModelList.indexOf(card), cardModel);
                }
            }
            mSortedList.add(cardModel);
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

            cardArt = view.findViewById(R.id.collection_card_art);
            cardName = view.findViewById(R.id.collection_card_name);
        }

        @Override
        public void onClick(View view) {
            cardId = mSortedList.get(getAdapterPosition()).id;

            if (mSortedList.get(getAdapterPosition()).getFactionModel() != null) {
                Intent intent = CardDetailActivity.newIntent(getActivity(), cardId);
                startActivity(intent,
                        ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
            }
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

            button = view.findViewById(R.id.filter_button);

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
            Collections.sort(models, new Comparator<CategoryModel>() {
                @Override
                public int compare(CategoryModel o1, CategoryModel o2) {
                    return o1.getName().get(LANGUAGE).compareToIgnoreCase(o2.getName().get(LANGUAGE));
                }
            });
            categoryModelList = models;
            notifyDataSetChanged();
        }
    }

    private class CategoryListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private int id;
        private Button button;

        CategoryListViewHolder(View view) {
            super(view);

            button = view.findViewById(R.id.filter_button);

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

    public class RarityListAdapter extends RecyclerView.Adapter<RarityListViewHolder> {

        public RarityListAdapter(List<RarityModel> models) {
            rarityModelList = models;
        }

        @Override
        public RarityListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new RarityListViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.filter_button, parent, false));
        }

        @Override
        public void onBindViewHolder(RarityListViewHolder holder, int position) {
            RarityModel model = rarityModelList.get(position);

            holder.button.setText(model.getName());
            if (rarityIds.isEmpty() || !rarityIds.contains(model.id)) {
                holder.button.setSelected(false);
            }
            else {
                holder.button.setSelected(true);
            }

            holder.itemView.setTag(model);
        }

        @Override
        public int getItemCount() {
            return rarityModelList.size();
        }

        public void updateModelList(List<RarityModel> models) {
            rarityModelList = models;
            notifyDataSetChanged();
        }
    }

    private class RarityListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private int id;
        private Button button;

        RarityListViewHolder(View view) {
            super(view);

            button = view.findViewById(R.id.filter_button);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            id = rarityModelList.get(getAdapterPosition()).id;
            if (rarityIds.contains(id)) {
                rarityIds.remove((Object) id);
                button.setSelected(false);
            }
            else {
                rarityIds.add(id);
                button.setSelected(true);
            }
            filter();
        }
    }
}
