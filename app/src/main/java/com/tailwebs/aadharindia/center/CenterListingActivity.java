package com.tailwebs.aadharindia.center;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.center.searchinmap.models.CenterModel;
import com.tailwebs.aadharindia.utils.GlobalValue;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CenterListingActivity extends BaseActivity implements View.OnClickListener {

    private List<CenterModel> centerModelList = null;

    @BindView(R.id.center_listing_recycler_view)
    RecyclerView centerListingRecyclerView;

    static CenterListingActivity instance;

    //Action Bar

    @BindView(R.id.back_button)
    ImageView backButton;

    @BindView(R.id.heading_tv)
    TextView headingTV;

    @BindView(R.id.right_action_bar_button)
    ImageView rightActionBarButton;

    String placeName=null;

    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_listing);
        ButterKnife.bind(this);
        instance=this;

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "All Centers", null);

        centerModelList = GlobalValue.centerModelList;
        //action bar
        backButton.setOnClickListener(this);
        placeName=getIntent().getExtras().getString("place_name");
        headingTV.setText("Center in "+(getIntent().getExtras().getString("place_name")));
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);
        
        loadListingCenters();







    }

    private void loadListingCenters() {


        // Add Adapter
        CenterListingRecyclerAdapter adapter = new CenterListingRecyclerAdapter(placeName);
        adapter.setData(centerModelList, false);
        centerListingRecyclerView.setItemAnimator(new DefaultItemAnimator());
        centerListingRecyclerView.addItemDecoration(new MyCenterDividerItemDecoration(CenterListingActivity.this, DividerItemDecoration.VERTICAL, 16));
        centerListingRecyclerView.setAdapter(adapter);

    }

    public static CenterListingActivity getInstance() {
        return instance;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.back_button:
                onBackPressed();
                break;
        }

    }
}
