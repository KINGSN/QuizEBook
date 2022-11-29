package com.example.androidebookapp.activity;


import static com.example.androidebookapp.activity.MainActivity.bookmarkDBHelper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.androidebookapp.R;
import com.example.androidebookapp.databinding.ActivityReviewBinding;
import com.example.androidebookapp.interfaces.OnClick;
import com.example.androidebookapp.item.Review;
import com.example.androidebookapp.util.Method;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReviewActivity extends AppCompatActivity {

    public ActivityReviewBinding binding;
    public   Menu toolbarMenu;
    public Method method;
    public TextView txtQuestion, txtQuestion1, btnOpt1, btnOpt2, btnOpt3, btnOpt4, tvLevel, tvQuestionNo;
    public ImageView prev, next, back, setting, F, report;
    NetworkImageView imgQuestion;
    public LinearLayout layout_A, layout_B, layout_C, layout_D;
    public int questionIndex = 0;

    private int NO_OF_QUESTION;
    public Button btnSolution;
    public TextView tvSolution, tvTitle;
    public CardView cardView;
    public ArrayList<Review> reviews = new ArrayList<>();
    AlertDialog alertDialog;
   public ImageLoader imageLoader = Method.getImageLoader();
    ProgressBar imgProgress;
    RelativeLayout fabLayout;
    public OnClick onClick;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(ReviewActivity.this, R.layout.activity_review);
        method = new Method(ReviewActivity.this, onClick);
        setSupportActionBar(binding.toolbar);
        btnOpt1 = (TextView) findViewById(R.id.btnOpt1);
        btnOpt2 = (TextView) findViewById(R.id.btnOpt2);
        btnOpt3 = (TextView) findViewById(R.id.btnOpt3);
        btnOpt4 = (TextView) findViewById(R.id.btnOpt4);
        txtQuestion = (TextView) findViewById(R.id.question);
        txtQuestion1 = (TextView) findViewById(R.id.question1);
      //  tvLevel = (TextView) findViewById(R.id.tvLevel);
        tvSolution = (TextView) findViewById(R.id.tvSolution);
        tvQuestionNo = (TextView) findViewById(R.id.questionNo);

        imgQuestion = (NetworkImageView) findViewById(R.id.imgQuestion);
        imgProgress = (ProgressBar) findViewById(R.id.imgProgress);

        fabLayout = (RelativeLayout) findViewById(R.id.fabLayout);
        layout_A = (LinearLayout) findViewById(R.id.a_layout);
        layout_B = (LinearLayout) findViewById(R.id.b_layout);
        layout_C = (LinearLayout) findViewById(R.id.c_layout);
        layout_D = (LinearLayout) findViewById(R.id.d_layout);
        prev = (ImageView) findViewById(R.id.prev);
        next = (ImageView) findViewById(R.id.next);
      //  back = (ImageView) findViewById(R.id.back);
        setting = (ImageView) findViewById(R.id.setting);
      //  report = (ImageView) findViewById(R.id.report);
      //  bookmark = (ImageView) findViewById(R.id.bookmark);
        btnSolution = (Button) findViewById(R.id.btnSolution);

        cardView = (CardView) findViewById(R.id.cardView1);

     /*   setting.setVisibility(View.GONE);
        bookmark.setVisibility(View.VISIBLE);
        report.setVisibility(View.VISIBLE);
        setting.setVisibility(View.INVISIBLE);
        tvLevel.setText(getString(R.string.review_answer));*/
        reviews = QuizPlayActivity.reviews;
      //  Utils.displayInterstitial();
        ReviewQuestion();
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (questionIndex > 0) {
                    questionIndex--;
                    ReviewQuestion();
                }

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (questionIndex < (reviews.size() - 1)) {
                    questionIndex++;
                    ReviewQuestion();
                }
            }
        });

       /* report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReportDialog();
            }
        });*/
      /*  back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });*/

        fabLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ReviewActivity.this, BookmarkList.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu, menu);

       /* int isfav = bookmarkDBHelper.getBookmarks(reviews.get(questionIndex).getQueId());

        if (isfav == reviews.get(questionIndex).getQueId()) {
            menu.findItem(R.id.bookmark).setIcon(R.drawable.ic_mark);
         //   menu.findItem(R.id.bookmark).setTag("mark");
        } else {
          //  bookmark.setImageResource(R.drawable.ic_unmark);
            menu.findItem(R.id.bookmark).setIcon(R.drawable.ic_unmark);

        }*/

        toolbarMenu = menu;
        toolbarMenu.findItem(R.id.bookmark).setVisible(false);
        toolbarMenu.findItem(R.id.report).setVisible(false);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_setting:
                Log.d("KINGSN", "onOptionsItemSelected: kin");
                SettingButtonMethod();
                break;
            case R.id.report:
                ReportDialog();
                break;
            case R.id.bookmark:
                ReportDialog();
                break;
        }
// ab kr lol default valu return kr raha tha
        return true;
    }

    public void SettingButtonMethod() {

        Intent intent = new Intent(method.activity, SettingActivity.class);
        startActivity(intent);
        method.activity.overridePendingTransition(R.anim.open_next, R.anim.close_next);
    }

    public void ReportDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(ReviewActivity.this);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.report_dialog, null);
        dialog.setView(dialogView);
   /*     TextView tvReport = (TextView) dialogView.findViewById(R.id.tvReport);
        TextView cancel = (TextView) dialogView.findViewById(R.id.cancel);
        final EditText edtReport = (EditText) dialogView.findViewById(R.id.edtReport);
        final TextInputLayout txtInputReport = (TextInputLayout) dialogView.findViewById(R.id.txtInputReport);
        TextView tvQuestion = (TextView) dialogView.findViewById(R.id.tvQuestion);
        tvQuestion.setText("Que : " + Html.fromHtml(reviews.get(questionIndex).getQuestion()));*/

        alertDialog = dialog.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.setCancelable(true);

      /*  tvReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edtReport.getText().toString().isEmpty()) {
                    ReportQuestion(edtReport.getText().toString());
                    txtInputReport.setError(null);
                } else {
                    txtInputReport.setError("Please fill all the data and submit!");
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();*/
    }




    public void ReviewQuestion() {
        if (questionIndex < reviews.size()) {
            txtQuestion.setText(Html.fromHtml(reviews.get(questionIndex).getQuestion()));
            txtQuestion1.setText(Html.fromHtml(reviews.get(questionIndex).getQuestion()));
            final ArrayList<String> options = new ArrayList<String>();
            options.addAll(reviews.get(questionIndex).getOptionList());
            //  Collections.shuffle(options);
            btnOpt1.setText(Html.fromHtml(options.get(0).trim()));
            btnOpt2.setText(Html.fromHtml(options.get(1).trim()));
            btnOpt3.setText(Html.fromHtml(options.get(2).trim()));
            btnOpt4.setText(Html.fromHtml(options.get(3).trim()));

            tvQuestionNo.setText(" " + (questionIndex + 1) + "/" + reviews.size());

            if (reviews.get(questionIndex).getExtraNote().isEmpty()) {
                btnSolution.setVisibility(View.GONE);
            } else {
                btnSolution.setVisibility(View.VISIBLE);
            }
      /*      bookmark.setTag("unmark");
            bookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Review review = reviews.get(questionIndex);
                    if (bookmark.getTag().equals("unmark")) {
                        String solution = reviews.get(questionIndex).getExtraNote();
                        bookmarkDBHelper.insertIntoDB(review.getQueId(),
                                review.getQuestion(),
                                review.getRightAns(),
                                solution,
                                review.getImgUrl(),
                                options.get(0).trim(),
                                options.get(1).trim(),
                                options.get(2).trim(),
                                options.get(3).trim());
                        bookmark.setImageResource(R.drawable.ic_mark);
                        bookmark.setTag("mark");
                    } else {
                        bookmarkDBHelper.delete_id(reviews.get(questionIndex).getQueId());
                        bookmark.setImageResource(R.drawable.ic_unmark);
                        bookmark.setTag("unmark");
                    }

                }
            });*/

          /*  int isfav = bookmarkDBHelper.getBookmarks(reviews.get(questionIndex).getQueId());

            if (isfav == reviews.get(questionIndex).getQueId()) {
                binding.toolbar.getMenu().getItem(2).setImageResource(R.drawable.ic_mark);
                bookmark.setTag("mark");
            } else {
                bookmark.setImageResource(R.drawable.ic_unmark);
                bookmark.setTag("unmark");
            }*/
            tvSolution.setVisibility(View.INVISIBLE);
            cardView.setVisibility(View.INVISIBLE);
            btnSolution.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String solution = reviews.get(questionIndex).getExtraNote();
                    cardView.setVisibility(View.VISIBLE);
                    tvSolution.setVisibility(View.VISIBLE);
                    tvSolution.setText(solution);

                }
            });


            if (!reviews.get(questionIndex).getImgUrl().isEmpty()) {
                txtQuestion1.setVisibility(View.GONE);
                txtQuestion.setVisibility(View.VISIBLE);
                imgQuestion.setImageUrl(reviews.get(questionIndex).getImgUrl(), imageLoader);
                imgQuestion.setVisibility(View.VISIBLE);
                imgProgress.setVisibility(View.VISIBLE);

             /*   Glide.with(method.activity)
                        .load(reviews.get(questionIndex).getImgUrl())
                        .dontAnimate()
                        .thumbnail(0.5f)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .encodeFormat(Bitmap.CompressFormat.PNG)
                        .into(imgQuestion);*/
                Picasso.with(method.activity)
                        .load(reviews.get(questionIndex).getImgUrl())
                        .into(imgQuestion, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                //do something when picture is loaded successfully
                                imgProgress.setVisibility(View.GONE);

                            }

                            @Override
                            public void onError() {
                                //do something when there is picture loading error
                                imgProgress.setVisibility(View.GONE);
                            }
                        });
            } else {
                imgQuestion.setVisibility(View.GONE);
                txtQuestion1.setVisibility(View.VISIBLE);
                txtQuestion.setVisibility(View.GONE);
            }


            if (btnOpt1.getText().toString().trim().equalsIgnoreCase(reviews.get(questionIndex).getRightAns().trim())) {

                layout_A.setBackgroundResource(R.drawable.right_gradient);
                layout_B.setBackgroundResource(R.drawable.answer_bg);
                layout_C.setBackgroundResource(R.drawable.answer_bg);
                layout_D.setBackgroundResource(R.drawable.answer_bg);
                if (btnOpt2.getText().toString().trim().equalsIgnoreCase(reviews.get(questionIndex).getWrongAns())) {
                    layout_B.setBackgroundResource(R.drawable.wrong_gradient);
                    layout_C.setBackgroundResource(R.drawable.answer_bg);
                    layout_D.setBackgroundResource(R.drawable.answer_bg);
                } else if (btnOpt3.getText().toString().trim().equalsIgnoreCase(reviews.get(questionIndex).getWrongAns())) {
                    layout_C.setBackgroundResource(R.drawable.wrong_gradient);
                    layout_B.setBackgroundResource(R.drawable.answer_bg);
                    layout_D.setBackgroundResource(R.drawable.answer_bg);
                } else if (btnOpt4.getText().toString().trim().equalsIgnoreCase(reviews.get(questionIndex).getWrongAns())) {
                    layout_D.setBackgroundResource(R.drawable.wrong_gradient);
                    layout_B.setBackgroundResource(R.drawable.answer_bg);
                    layout_C.setBackgroundResource(R.drawable.answer_bg);
                }

            } else if (btnOpt2.getText().toString().trim().equalsIgnoreCase(reviews.get(questionIndex).getRightAns().trim())) {

                layout_B.setBackgroundResource(R.drawable.right_gradient);
                layout_A.setBackgroundResource(R.drawable.answer_bg);
                layout_C.setBackgroundResource(R.drawable.answer_bg);
                layout_D.setBackgroundResource(R.drawable.answer_bg);
                if (btnOpt1.getText().toString().trim().equalsIgnoreCase(reviews.get(questionIndex).getWrongAns())) {
                    layout_A.setBackgroundResource(R.drawable.wrong_gradient);
                    layout_C.setBackgroundResource(R.drawable.answer_bg);
                    layout_D.setBackgroundResource(R.drawable.answer_bg);
                } else if (btnOpt3.getText().toString().trim().equalsIgnoreCase(reviews.get(questionIndex).getWrongAns())) {
                    layout_C.setBackgroundResource(R.drawable.wrong_gradient);
                    layout_A.setBackgroundResource(R.drawable.answer_bg);
                    layout_D.setBackgroundResource(R.drawable.answer_bg);
                } else if (btnOpt4.getText().toString().trim().equalsIgnoreCase(reviews.get(questionIndex).getWrongAns())) {
                    layout_D.setBackgroundResource(R.drawable.wrong_gradient);
                    layout_A.setBackgroundResource(R.drawable.answer_bg);
                    layout_C.setBackgroundResource(R.drawable.answer_bg);
                }

            } else if (btnOpt3.getText().toString().trim().equalsIgnoreCase(reviews.get(questionIndex).getRightAns().trim())) {

                layout_C.setBackgroundResource(R.drawable.right_gradient);
                layout_A.setBackgroundResource(R.drawable.answer_bg);
                layout_B.setBackgroundResource(R.drawable.answer_bg);
                layout_D.setBackgroundResource(R.drawable.answer_bg);
                if (btnOpt1.getText().toString().trim().equalsIgnoreCase(reviews.get(questionIndex).getWrongAns())) {
                    layout_A.setBackgroundResource(R.drawable.wrong_gradient);
                    layout_B.setBackgroundResource(R.drawable.answer_bg);
                    layout_D.setBackgroundResource(R.drawable.answer_bg);
                } else if (btnOpt2.getText().toString().trim().equalsIgnoreCase(reviews.get(questionIndex).getWrongAns())) {
                    layout_B.setBackgroundResource(R.drawable.wrong_gradient);
                    layout_A.setBackgroundResource(R.drawable.answer_bg);
                    layout_D.setBackgroundResource(R.drawable.answer_bg);
                } else if (btnOpt4.getText().toString().trim().equalsIgnoreCase(reviews.get(questionIndex).getWrongAns())) {
                    layout_D.setBackgroundResource(R.drawable.wrong_gradient);
                    layout_A.setBackgroundResource(R.drawable.answer_bg);
                    layout_B.setBackgroundResource(R.drawable.answer_bg);
                }

            } else if (btnOpt4.getText().toString().trim().equalsIgnoreCase(reviews.get(questionIndex).getRightAns().trim())) {
                layout_D.setBackgroundResource(R.drawable.right_gradient);
                layout_A.setBackgroundResource(R.drawable.answer_bg);
                layout_C.setBackgroundResource(R.drawable.answer_bg);
                layout_B.setBackgroundResource(R.drawable.answer_bg);
                if (btnOpt1.getText().toString().trim().equalsIgnoreCase(reviews.get(questionIndex).getWrongAns())) {
                    layout_A.setBackgroundResource(R.drawable.wrong_gradient);
                    layout_B.setBackgroundResource(R.drawable.answer_bg);
                    layout_C.setBackgroundResource(R.drawable.answer_bg);
                } else if (btnOpt2.getText().toString().trim().equalsIgnoreCase(reviews.get(questionIndex).getWrongAns())) {
                    layout_B.setBackgroundResource(R.drawable.wrong_gradient);
                    layout_A.setBackgroundResource(R.drawable.answer_bg);
                    layout_C.setBackgroundResource(R.drawable.answer_bg);
                } else if (btnOpt3.getText().toString().trim().equalsIgnoreCase(reviews.get(questionIndex).getWrongAns())) {
                    layout_C.setBackgroundResource(R.drawable.wrong_gradient);
                    layout_A.setBackgroundResource(R.drawable.answer_bg);
                    layout_B.setBackgroundResource(R.drawable.answer_bg);

                }
            }
        }

    }

    /* public void ReportQuestion(final String message) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.QUIZ_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            boolean error = obj.getBoolean("error");
                            String message = obj.getString("message");
                            if (!error) {
                                alertDialog.dismiss();
                                Toast.makeText(ReviewActivity.this, message, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(ReviewActivity.this, message, Toast.LENGTH_LONG).show();
                                System.out.println(" empty msg " + message);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.accessKey, Constant.accessKeyValue);
                params.put(Constant.reportQuestion, "1");
                params.put(Constant.questionId, "" + reviews.get(questionIndex).getQueId());
                params.put(Constant.messageReport, message);
                return params;
            }
        };
        method.getRequestQueue().getCache().clear();
      //  AppController.getInstance().addToRequestQueue(stringRequest);
    }*/
}
