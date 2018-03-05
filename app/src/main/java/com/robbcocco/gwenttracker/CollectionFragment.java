package com.robbcocco.gwenttracker;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.robbcocco.gwenttracker.database.pojo.CardInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Created by rober on 3/3/2018.
 */

public class CollectionFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

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
//        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_collection, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.collection_list);
        recyclerViewAdapter = new CollectionAdapter(new ArrayList<CardInfo>());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        recyclerView.setAdapter(recyclerViewAdapter);

        viewModel = ViewModelProviders.of(this).get(CollectionViewModel.class);

        viewModel.getCardModelList().observe(getActivity(), new Observer<List<CardInfo>>() {
            @Override
            public void onChanged(@Nullable List<CardInfo> cardModelList) {
                if (cardModelList != null) {
                    for(Iterator<CardInfo> iterator = cardModelList.iterator(); iterator.hasNext(); ) {
                        if(!iterator.next().getVariationModelList().get(0).getCollectible())
                            iterator.remove();
                    }
                    Collections.sort(cardModelList, new Comparator<CardInfo>() {
                        @Override
                        public int compare(CardInfo c1, CardInfo c2) {
                            return c1.getCardModel().getName().get("en-US")
                                    .compareToIgnoreCase(c2.getCardModel().getName().get("en-US"));
                        }
                    });
                }
                recyclerViewAdapter.updateCardModelList(cardModelList);
            }
        });

        return rootView;
    }
}
