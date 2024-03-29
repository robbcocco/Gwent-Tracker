package com.robbcocco.gwenttracker;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robbcocco.gwenttracker.database.entity.CardModel;
import com.robbcocco.gwenttracker.tasks.GetCardDetailTask;
import com.robbcocco.gwenttracker.tasks.GetCardDetailCallback;

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

    private SharedPreferences sharedPreferences;
    private static String LANGUAGE="en-US";

    private GetCardDetailTask getCardDetailTask;

    private CardModel cardModel;
//    private CollapsingToolbarLayout collapsingToolbar;
//    private Toolbar toolbar;
    private TextView titleView;
    private ImageView factionArtView;
    private ImageView artView;
    private TextView strView;
    private TextView factionView;
    private LinearLayout rarityViewParent;
    private TextView rarityView;
    private TextView flavorView;
    private LinearLayout categoriesViewParent;
    private TextView categoriesView;
    private LinearLayout infoViewParent;
    private TextView infoView;

    //TODO: should probably switch to databinding
    private CardView fac1;
    private CardView fac2;
    private CardView fac3;
    private CardView fac4;
    private CardView fac5;
    private CardView fac6;

    private LinearLayout relatedTitleView;
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

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        LANGUAGE = sharedPreferences.getString("lang_list", "en-US");

        if (getArguments() != null) {
            int cardId = getArguments().getInt(CARD_ID);

            GetCardDetailCallback getCardDetailCallback = new GetCardDetailCallback() {
                @Override
                public void updateAdapter(CardModel result) {
                    cardModel = result;
                    updateView();
                }
            };
            getCardDetailTask = new GetCardDetailTask(getCardDetailCallback, cardId);
            getCardDetailTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, getActivity());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_card_detail, container, false);

//        toolbar = view.findViewById(R.id.card_detail_toolbar);
//        collapsingToolbar = view.findViewById(R.id.card_detail_collapsingtoolbar);

        titleView = view.findViewById(R.id.card_detail_title);
        factionArtView = view.findViewById(R.id.card_detail_faction_art);
        artView = view.findViewById(R.id.card_detail_art);
        factionView = view.findViewById(R.id.card_detail_faction);
        rarityViewParent = view.findViewById(R.id.card_detail_rarity_parent);
        rarityView = view.findViewById(R.id.card_detail_rarity);
        strView = view.findViewById(R.id.card_detail_set);
        categoriesViewParent = view.findViewById(R.id.card_detail_categories_parent);
        categoriesView = view.findViewById(R.id.card_detail_categories);
        flavorView = view.findViewById(R.id.card_detail_flavor);
        infoViewParent = view.findViewById(R.id.card_detail_info_parent);
        infoView = view.findViewById(R.id.card_detail_info);

        //TODO: should probably switch to databinding
        fac1 = view.findViewById(R.id.card_detail_faction1);
        fac2 = view.findViewById(R.id.card_detail_faction2);
        fac3 = view.findViewById(R.id.card_detail_faction3);
        fac4 = view.findViewById(R.id.card_detail_faction4);
        fac5 = view.findViewById(R.id.card_detail_faction5);
        fac6 = view.findViewById(R.id.card_detail_faction6);

        relatedTitleView = view.findViewById(R.id.card_detail_related_parent);
        recyclerView = view.findViewById(R.id.card_detail_related_list);
        recyclerViewAdapter = new RelatedAdapter(new ArrayList<CardModel>());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recyclerViewAdapter);

//        if (toolbar != null) {
//            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//        }

        updateView();

        return view;
    }

    private void updateView() {
        if (cardModel != null) {
//            collapsingToolbar.setTitle(cardModel.getName().get(LANGUAGE));
            titleView.setText(cardModel.getName().get(LANGUAGE));

            if (cardModel.getVariationModelList() != null &&
                    !cardModel.getVariationModelList().isEmpty() &&
                    cardModel.getVariationModelList().get(0).getArt_medium() != null) {
                GlideApp.with(getActivity())
                        .load(cardModel.getVariationModelList().get(0).getArt_medium().toString())
                        .override(SIZE_ORIGINAL, SIZE_ORIGINAL)
                        .into(artView);
            }

            strView.setText(String.valueOf(cardModel.getStrength()));

            if (cardModel.getFactionModel() != null) {
                factionView.setText(cardModel.getFactionModel().getName().get(LANGUAGE));
                factionArtView.setImageResource(cardModel.getFactionBanner());

                //TODO: should probably switch to databinding
                fac1.setCardBackgroundColor(getResources().getColor(cardModel.getFactionColor()));
                fac2.setCardBackgroundColor(getResources().getColor(cardModel.getFactionColor()));
                fac3.setCardBackgroundColor(getResources().getColor(cardModel.getFactionColor()));
                fac4.setCardBackgroundColor(getResources().getColor(cardModel.getFactionColor()));
                fac5.setCardBackgroundColor(getResources().getColor(cardModel.getFactionColor()));
                fac6.setCardBackgroundColor(getResources().getColor(cardModel.getFactionColor()));
            }

            if (cardModel.getVariationModelList().get(0).getRarityModel() != null) {
                rarityView.setText(cardModel.getVariationModelList().get(0).getRarityModel().getName());
                rarityViewParent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog
                                .Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme_AlertDialog));
                        builder.setMessage(cardModel.getRarity(LANGUAGE))
                                .setTitle("Scraps");
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
            }

            flavorView.setText(cardModel.getFlavor().get(LANGUAGE));

            if (!cardModel.getCategoryModelList().isEmpty()) {
                categoriesView.setText(cardModel.getCategories(LANGUAGE));
            }
            else {
                categoriesViewParent.setVisibility(View.GONE);
            }

            infoView.setText(cardModel.getInfo().get(LANGUAGE));
            if (!cardModel.getKeywordModelList().isEmpty()) {
                infoViewParent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog
                                .Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme_AlertDialog));
                        builder.setMessage(cardModel.getKeywords(LANGUAGE))
                                .setTitle("Keywords");
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
            }

            if (cardModel.getRelatedCardModelList() != null && !cardModel.getRelatedCardModelList().isEmpty()) {
                recyclerViewAdapter.updateRelatedList(cardModel.getRelatedCardModelList());
            }
            else {
                relatedTitleView.setVisibility(View.GONE);
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

    private class RelatedAdapter extends RecyclerView.Adapter<RelatedViewHolder> {

        private List<CardModel> cardModelList;

        public RelatedAdapter(List<CardModel> cardModelList) {
            this.cardModelList = cardModelList;
        }

        @Override
        public RelatedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new RelatedViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_detail_related, parent, false), cardModelList);
        }

        @Override
        public void onBindViewHolder(final RelatedViewHolder holder, final int position) {
            final CardModel cardModel = cardModelList.get(position);

            String strengthDotName = String.valueOf(cardModel.getStrength()) + " • " + cardModel.getName().get(LANGUAGE);
            holder.cardName.setText(strengthDotName);
            holder.cardName.setSelected(true);

            holder.cardInfo.setText(cardModel.getInfo().get(LANGUAGE));
            holder.cardInfo.setSelected(true);

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

    private class RelatedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private int cardId;
        private List<CardModel> cardModelList;
        private TextView cardName;
        private TextView cardInfo;

        RelatedViewHolder(View view, List<CardModel> cardModelList) {
            super(view);
            itemView.setOnClickListener(this);

            this.cardModelList = cardModelList;
            cardName = view.findViewById(R.id.card_detail_related_card_name);
            cardInfo = view.findViewById(R.id.card_detail_related_card_info);
        }

        @Override
        public void onClick(View view) {
            cardId = cardModelList.get(getAdapterPosition()).id;
            Intent intent = CardDetailActivity.newIntent(getActivity(), cardId);
            startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
        }
    }
}
