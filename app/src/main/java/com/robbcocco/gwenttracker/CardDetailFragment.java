package com.robbcocco.gwenttracker;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * CardDetailFragment.OnFragmentInteractionListener interface
 * to handle interaction events.
 * Use the {@link CardDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardDetailFragment extends Fragment {
    private static final String CARD_ID = "card_id";

    private CardDetailViewModelFactory.CardDetailViewModel viewModel;

    private CardModel cardModel;
    private CollapsingToolbarLayout collapsingToolbar;
    private Toolbar toolbar;
    private ImageView artView;
    private TextView factionView;
    private TextView rarityView;
    private TextView setView;
    private TextView categoriesView;
    private TextView flavorView;
    private TextView infoView;

    private TextView relatedTitleView;
    private RelatedAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;

//    private OnFragmentInteractionListener mListener;

    public CardDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param cardId Parameter 1.
     * @return A new instance of fragment CardDetailFragment.
     */
    public static CardDetailFragment newInstance(Integer cardId) {
        CardDetailFragment fragment = new CardDetailFragment();
        Bundle args = new Bundle();
        args.putInt(CARD_ID, cardId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            int cardId = getArguments().getInt(CARD_ID);
            viewModel = ViewModelProviders
                    .of(this,
                            new CardDetailViewModelFactory(getActivity().getApplication(), cardId))
                    .get(CardDetailViewModelFactory.CardDetailViewModel.class);

            viewModel.getCardModel().observe(getActivity(), new Observer<CardModel>() {
                @Override
                public void onChanged(@Nullable CardModel model) {
                    cardModel = model;
                    updateView();
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_card_detail, container, false);

        toolbar = (Toolbar) view.findViewById(R.id.card_detail_toolbar);
        collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.card_detail_collapsingtoolbar);

        artView = (ImageView) view.findViewById(R.id.card_detail_art);
        factionView = (TextView) view.findViewById(R.id.card_detail_faction);
        rarityView = (TextView) view.findViewById(R.id.card_detail_rarity);
        setView = (TextView) view.findViewById(R.id.card_detail_set);
        categoriesView = (TextView) view.findViewById(R.id.card_detail_categories);
        flavorView = (TextView) view.findViewById(R.id.card_detail_flavor);
        infoView = (TextView) view.findViewById(R.id.card_detail_info);

        relatedTitleView = (TextView) view.findViewById(R.id.card_detail_related_title);
        recyclerView = (RecyclerView) view.findViewById(R.id.card_detail_related);
        recyclerViewAdapter = new RelatedAdapter(new ArrayList<CardModel>());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(recyclerViewAdapter);

        if (toolbar != null) {
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        updateView();

        return view;
    }

    private void updateView() {
        if (cardModel != null) {
            collapsingToolbar.setTitle(cardModel.getName().get("en-US"));

            if (cardModel.getVariationModelList() != null &&
                    !cardModel.getVariationModelList().isEmpty() &&
                    cardModel.getVariationModelList().get(0).getArt_medium() != null) {
                GlideApp.with(getActivity())
                        .load(cardModel.getVariationModelList().get(0).getArt_medium().toString())
                        .override(SIZE_ORIGINAL, SIZE_ORIGINAL)
                        .placeholder(R.drawable.placeholder_card_low)
                        .into(artView);
            }

            if (cardModel.getFactionModel() != null && cardModel.getVariationModelList() != null) {
                factionView.setText(cardModel.getFactionModel().getName().get("en-US"));
                rarityView.setText(cardModel.getVariationModelList().get(0).getRarityModel().getName());
                setView.setText(cardModel.getVariationModelList().get(0).getSetModel().getName());
            }

            categoriesView.setText(cardModel.getCategories("en-US"));

            flavorView.setText(cardModel.getFlavor().get("en-US"));
            infoView.setText(cardModel.getInfo().get("en-US"));

            if (cardModel.getRelatedCardModelList() != null && !cardModel.getRelatedCardModelList().isEmpty()) {
                relatedTitleView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerViewAdapter.updateRelatedList(cardModel.getRelatedCardModelList());

            }
        }
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }

    private class RelatedAdapter extends RecyclerView.Adapter<CardDetailFragment.RecyclerViewHolder> {

        private List<CardModel> cardModelList;

        public RelatedAdapter(List<CardModel> cardModelList) {
            this.cardModelList = cardModelList;
        }

        @Override
        public CardDetailFragment.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CardDetailFragment.RecyclerViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.collection_card, parent, false), cardModelList);
        }

        @Override
        public void onBindViewHolder(final CardDetailFragment.RecyclerViewHolder holder, final int position) {
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

        public void updateRelatedList(List<CardModel> cardModelList) {
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
