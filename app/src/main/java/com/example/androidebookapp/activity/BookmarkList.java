package com.example.androidebookapp.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.androidebookapp.R;
import com.example.androidebookapp.interfaces.OnClick;
import com.example.androidebookapp.item.Bookmark;
import com.example.androidebookapp.preferences.SharedPrefrence;
import com.example.androidebookapp.util.Method;
import com.example.androidebookapp.util.StaticUtils;


import java.util.ArrayList;

public class BookmarkList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ImageView back, setting;
    TextView title, tvNoBookmarked;
    public static ArrayList<Bookmark> bookmarks;
    Button btnPlay;
    public Method method;
    public OnClick onClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark_list);
        method = new Method(BookmarkList.this, onClick);
        setting = (ImageView) findViewById(R.id.setting);
        btnPlay = (Button) findViewById(R.id.btnPlay);
        tvNoBookmarked = (TextView) findViewById(R.id.emptyMsg);
     //   title.setText("Bookmark List");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        bookmarks = MainActivity.bookmarkDBHelper.getAllBookmarkedList();

        //when bookmark note available show message
        if (bookmarks.size() == 0) {
            tvNoBookmarked.setVisibility(View.VISIBLE);
            btnPlay.setVisibility(View.GONE);
        }
        BookMarkAdapter adapter = new BookMarkAdapter(getApplicationContext(), bookmarks);
        recyclerView.setAdapter(adapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SharedPrefrence.getSoundEnableDisable(getApplicationContext())) {
                    StaticUtils.backSoundonclick(getApplicationContext());
                }
                if (SharedPrefrence.getVibration(getApplicationContext())) {
                    StaticUtils.vibrate(getApplicationContext(), StaticUtils.VIBRATION_DURATION);
                }
                Intent playQuiz = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(playQuiz);
                overridePendingTransition(R.anim.open_next, R.anim.close_next);
            }
        });

       /* btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent playIntent = new Intent(BookmarkList.this, BookmarkPlay.class);
                startActivity(playIntent);
            }
        });*/
    }

    public class BookMarkAdapter extends RecyclerView.Adapter<BookMarkAdapter.ItemRowHolder> {
        private ArrayList<Bookmark> bookmarks;
        private Context mContext;
        private ImageLoader imageLoader = method.getImageLoader();

        public BookMarkAdapter(Context context, ArrayList<Bookmark> bookmarks) {
            this.bookmarks = bookmarks;
            this.mContext = context;
        }

        @Override
        public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_layout, parent, false);
            return new ItemRowHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemRowHolder holder, @SuppressLint("RecyclerView") final int position) {

            ItemRowHolder itemRowHolder = (ItemRowHolder) holder;
            final Bookmark bookmark = bookmarks.get(position);
            itemRowHolder.tvNo.setText("" + (position + 1) + ".");
            itemRowHolder.tvQue.setText(Html.fromHtml(bookmark.getQuestion()));
            itemRowHolder.tvAns.setText(" Ans : " + Html.fromHtml(bookmark.getAnswer()));
            itemRowHolder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity.bookmarkDBHelper.delete_id(bookmark.getQue_id());
                    bookmarks.remove(position);
                    notifyDataSetChanged();
                }
            });
            itemRowHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!bookmark.getSolution().isEmpty()) {
                        SolutionDialog(bookmark.getQuestion(), bookmark.getSolution());
                    }
                }
            });
            System.out.println("=====image url  " + bookmark.getImageUrl());
            if (!bookmark.getImageUrl().isEmpty()) {
                itemRowHolder.imgQuestion.setVisibility(View.VISIBLE);
                itemRowHolder.imgQuestion.setImageUrl(bookmark.getImageUrl(), imageLoader);
            }

        }

        @Override
        public int getItemCount() {
            return (null != bookmarks ? bookmarks.size() : 0);
        }

        public class ItemRowHolder extends RecyclerView.ViewHolder {
            TextView tvNo, tvQue, tvAns;
            ImageView remove;
            CardView cardView;
            NetworkImageView imgQuestion;

            public ItemRowHolder(View itemView) {
                super(itemView);
                tvNo = (TextView) itemView.findViewById(R.id.tvNo);
                tvQue = (TextView) itemView.findViewById(R.id.tvQue);
                tvAns = (TextView) itemView.findViewById(R.id.tvAns);
                remove = (ImageView) itemView.findViewById(R.id.remove);
                cardView = (CardView) itemView.findViewById(R.id.cardView);
                imgQuestion = (NetworkImageView) itemView.findViewById(R.id.imgQuestion);


            }
        }
    }

    public void SolutionDialog(String question, String solution) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(BookmarkList.this);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.note_dialog_layout, null);
        dialog.setView(dialogView);
        TextView tvQuestion = (TextView) dialogView.findViewById(R.id.question);
        TextView tvSolution = (TextView) dialogView.findViewById(R.id.solution);
        tvQuestion.setText(Html.fromHtml(question));
        tvSolution.setText(Html.fromHtml(solution));
        AlertDialog alertDialog = dialog.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.setCancelable(true);
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
