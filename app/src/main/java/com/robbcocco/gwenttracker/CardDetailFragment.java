package com.robbcocco.gwenttracker;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CardDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CardDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardDetailFragment extends Fragment {
    private static final String CARD_ID = "card_id";

    private CollectionViewModel viewModel;

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
    public static CardDetailFragment newInstance(int cardId) {
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
//            viewModel = ViewModelProviders.of(this).get(CollectionViewModel.class);
            viewModel = CollectionViewModel.getInstance(this);

            viewModel.getCardModelList().observe(getActivity(), new Observer<List<CardModel>>() {
                @Override
                public void onChanged(@Nullable List<CardModel> cardModelList) {
                    int cardId = getArguments().getInt(CARD_ID);
                    if (cardModelList != null && !cardModelList.isEmpty() && cardModelList.get(cardId) != null) {
                        cardModel = cardModelList.get(cardId);
                        updateView();
                    }
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

        if (toolbar != null) {
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        }

        updateView();

        return view;
    }

    private void updateView() {
        if (cardModel != null) {
            collapsingToolbar.setTitle(cardModel.getName().get("en-US"));

            GlideApp.with(getActivity())
                    .load(cardModel.getVariationModelList().get(0).getArt_medium().toString())
                    .override(SIZE_ORIGINAL, SIZE_ORIGINAL)
                    .placeholder(R.drawable.placeholder_card_low)
                    .into(artView);
            factionView.setText(cardModel.getFactionModel().getName().get("en-US"));
            rarityView.setText(cardModel.getVariationModelList().get(0).getRarityModel().getName());
            setView.setText(cardModel.getVariationModelList().get(0).getSetModel().getName());

            categoriesView.setText(cardModel.getCategories("en-US"));

            flavorView.setText(cardModel.getFlavor().get("en-US"));
            infoView.setText(cardModel.getInfo().get("en-US"));
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
}
