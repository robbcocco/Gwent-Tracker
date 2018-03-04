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

import com.robbcocco.gwenttracker.database.entity.CardInfoModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
        recyclerViewAdapter = new CollectionAdapter(new ArrayList<CardInfoModel>());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        recyclerView.setAdapter(recyclerViewAdapter);

        viewModel = ViewModelProviders.of(this).get(CollectionViewModel.class);

        viewModel.getCardModelList().observe(getActivity(), new Observer<List<CardInfoModel>>() {
            @Override
            public void onChanged(@Nullable List<CardInfoModel> cardModelList) {
                if (cardModelList != null) {
                    Collections.sort(cardModelList, new Comparator<CardInfoModel>() {
                        @Override
                        public int compare(CardInfoModel c1, CardInfoModel c2) {
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
