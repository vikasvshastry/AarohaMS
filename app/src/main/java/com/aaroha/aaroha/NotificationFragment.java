package com.aaroha.aaroha;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.firebase.ui.FirebaseRecyclerAdapter;

import com.firebase.client.Firebase;

import classes.Notification;

public class NotificationFragment extends Fragment {

    private Firebase rootRef = new Firebase("https://aaroha-50e98.firebaseio.com/");
    private RecyclerView recyclerView;
    private View rootView;
    LinearLayoutManager mLayoutManager;
    FirebaseRecyclerAdapter<Notification, PostViewHolder> adapter;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_notification, container, false);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        recyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);

        FloatingActionButton fab = (FloatingActionButton)rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NewNotificationActivity.class));
            }
        });

        adapterFunc();

        return rootView;
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder{

        TextView headingText,bodyText,timeText,dateText;
        View mView;
        ImageView bar;

        public PostViewHolder(View v){
            super(v);
            this.mView = v;
            headingText = (TextView) v.findViewById(R.id.headingText);
            bodyText = (TextView) v.findViewById(R.id.bodyText);
            timeText = (TextView) v.findViewById(R.id.timeText);
            dateText = (TextView) v.findViewById(R.id.dateText);
            bar = (ImageView) v.findViewById(R.id.sideBar);
        }
    }

    public void adapterFunc(){
        adapter = new FirebaseRecyclerAdapter<Notification, PostViewHolder>(
                Notification.class,
                R.layout.notification_layout,
                PostViewHolder.class,
                rootRef.child("Notifications")
        ) {
            @Override
            protected void populateViewHolder(final PostViewHolder postViewHolder, final Notification p, final int i) {

                postViewHolder.headingText.setText(p.getHead());
                postViewHolder.bodyText.setText(p.getBody());
                postViewHolder.timeText.setText(p.getTime());
                postViewHolder.dateText.setText(p.getDate());
                if(p.getPriority().equals("blue"))
                    postViewHolder.bar.setBackgroundColor(Color.parseColor("#0093EA"));
                if(p.getPriority().equals("yellow"))
                    postViewHolder.bar.setBackgroundColor(Color.parseColor("#FFEB3B"));
                if(p.getPriority().equals("red"))
                    postViewHolder.bar.setBackgroundColor(Color.parseColor("#4CAF50"));

                postViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("cmtId",p.getNotifId());

                        Fragment fragment = new CommentsFragment();
                        fragment.setArguments(bundle);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.add(R.id.frameLayout, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });

            }
        };
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}