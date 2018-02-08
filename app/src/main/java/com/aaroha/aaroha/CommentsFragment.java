package com.aaroha.aaroha;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.util.Date;

import classes.Comment;

public class CommentsFragment extends Fragment {

    String myCourse,postId,uid;
    private Firebase rootRef = new Firebase("https://aaroha-50e98.firebaseio.com/");
    RecyclerView recyclerView;
    LinearLayoutManager mLayoutManager;
    FirebaseAuth firebaseAuth;

    public CommentsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_comments, container, false);

        Bundle b = getArguments();
        postId = b.getString("cmtId");

        uid = firebaseAuth.getInstance().getCurrentUser().getUid();

        recyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerViewForComments);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);

        final ImageView sendButton = (ImageView)rootView.findViewById(R.id.sendComment);
        final EditText editTextCmt = (EditText)rootView.findViewById(R.id.commentText);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cmtText = editTextCmt.getText().toString().trim();
                if(TextUtils.isEmpty(cmtText))
                {
                    Toast.makeText(getActivity(), "Nothing to send", Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

                        Firebase tempRefForKey = rootRef.child("comments").child(postId).push();
                        Comment newComment = new Comment();
                        newComment.setCmtId(tempRefForKey.getKey());
                        newComment.setCommentBody(cmtText);
                        newComment.setDate(currentDateTimeString.substring(0, 11));
                        newComment.setTime(currentDateTimeString.substring(12, currentDateTimeString.length()));
                        newComment.setSenderId(uid);
                        tempRefForKey.setValue(newComment, new Firebase.CompletionListener() {
                            @Override
                            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                if (firebaseError == null) {
                                    editTextCmt.setText("");
                                } else {
                                    Toast.makeText(getActivity(), "Unable to send", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }catch(Exception e){
                        Toast.makeText(getActivity(),"Unknown error. Report to dev", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        final FirebaseRecyclerAdapter<Comment, CommentViewHolder> adapter = new FirebaseRecyclerAdapter<Comment,CommentViewHolder>(
                Comment.class,
                R.layout.chat_sent_layout,
                CommentViewHolder.class,
                rootRef.child("comments").child(postId)
        ) {
            @Override
            protected void populateViewHolder(CommentViewHolder commentViewHolder, Comment c,int i) {
                commentViewHolder.commentBody.setText(c.getCommentBody()+ Html.fromHtml(" &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;"));
                commentViewHolder.sentTime.setText(c.getTime());
            }

            @Override
            public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                switch (viewType) {
                    case 1:
                        View userType1 = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.chat_sent_layout, parent, false);
                        return new CommentViewHolder(userType1);
                    case 2:
                        View userType2 = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.chat_receive_layout, parent, false);
                        return new CommentViewHolder(userType2);
                }
                return super.onCreateViewHolder(parent, viewType);
            }

            @Override
            public int getItemViewType(int position) {
                Comment c = getItem(position);
                if(TextUtils.equals(c.getSenderId(),uid)){
                    return 1;
                }
                else
                    return 2;
            }
        };

        //scroll down when new comment posted
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                recyclerView.scrollToPosition(positionStart);
            }
        });

        recyclerView.setAdapter(adapter);

        editTextCmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                    }
                }, 200);
            }
        });

        return rootView;
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder{

        TextView senderName,commentBody,sentTime;
        View mView;
        ImageView triangle;
        RelativeLayout chatBox;

        public CommentViewHolder(View v){
            super(v);
            this.mView = v;
            commentBody = (TextView)v.findViewById(R.id.commentBody);
            sentTime = (TextView)v.findViewById(R.id.sentTime);
        }
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