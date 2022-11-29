package com.example.androidebookapp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.example.androidebookapp.R;
import com.example.androidebookapp.databinding.ActivityQuizPlayBinding;
import com.example.androidebookapp.interfaces.OnClick;
import com.example.androidebookapp.item.QuestionList;
import com.example.androidebookapp.item.Review;
import com.example.androidebookapp.preferences.SharedPrefrence;
import com.example.androidebookapp.util.CircularProgressIndicatorr;
import com.example.androidebookapp.util.Constant;
import com.example.androidebookapp.util.GlobalVariables;
import com.example.androidebookapp.util.Method;
import com.example.androidebookapp.util.StaticUtils;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;
import java.util.Collections;

public class QuizPlayActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityQuizPlayBinding binding;
    Menu toolbarMenu;
    public Method method;
    private OnClick onClick;
    public ArrayList<QuestionList> my_id_dataArrayList=new ArrayList<>();
    public static ArrayList<Review> reviews = new ArrayList<>();
    public QuestionList question;
    public static CircularProgressIndicatorr progressTimer;
    public MyCountDownTimer myCountDownTimer;
    public MyCountDownTimer myCountDownTimer1;
    public static long leftTime = 0;
    public boolean isDialogOpen = false;
    public ImageView imgBookmark;
    public Animation RightSwipe_A, RightSwipe_B, RightSwipe_C, RightSwipe_D, Fade_in, fifty_fifty_anim;
    public int questionIndex = 0,
            btnPosition = 0,
            totalScore = 0,
            count_question_completed = 0,
            score = 0,
            coin = 6,
            level_coin = 6,
            correctQuestion = 0,
            inCorrectQuestion = 0,
            rightAns;
    int click = 0;
    int textSize;
    private Animation animation;
    public static Callback mCallback = null;
    private static int levelNo = 1;
    public void setCallback(Callback callback) {
        mCallback = callback;
    }


    public interface Callback {
        void onEnteredScore(int score);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(QuizPlayActivity.this, R.layout.activity_quiz_play);
        method = new Method(QuizPlayActivity.this);
        setSupportActionBar(binding.toolbar);
        Fade_in = AnimationUtils.loadAnimation(method.activity, R.anim.fade_out);
        RightSwipe_A = AnimationUtils.loadAnimation(method.activity, R.anim.anim_right_a);
        RightSwipe_B = AnimationUtils.loadAnimation(method.activity, R.anim.anim_right_b);
        RightSwipe_C = AnimationUtils.loadAnimation(method.activity, R.anim.anim_right_c);
        RightSwipe_D = AnimationUtils.loadAnimation(method.activity, R.anim.anim_right_d);

        animation = AnimationUtils.loadAnimation(method.activity, R.anim.right_ans_anim); // Change alpha from fully visible
        animation.setDuration(500); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the


        binding.progressBarTwo.setMaxProgress(GlobalVariables.CIRCULAR_MAX_PROGRESS);
        binding.progressBarTwo.setCurrentProgress(GlobalVariables.CIRCULAR_MAX_PROGRESS);

        binding.rightProgress.setMax(my_id_dataArrayList.size());
        binding.wrongProgress.setMax(my_id_dataArrayList.size());
        binding.progressBarTwo.clearAnimation();
        my_id_dataArrayList=GlobalVariables.questionlist;

        resetAllValue();

        binding.toolbar.setNavigationOnClickListener(view -> {
            PlayAreaLeaveDialog(method.activity);
        });

    }

    public void resetAllValue() {

        count_question_completed = 0;
       // binding.imgBookmark.setTag("unmark");
        binding.coinCount.setText(String.valueOf(coin));
        binding.txtTrueQuestion.setText("0");
        binding.txtFalseQuestion.setText("0");
        binding.aLayout.setOnClickListener(this);
        binding.bLayout.setOnClickListener(this);
        binding.cLayout.setOnClickListener(this);
        binding.dLayout.setOnClickListener(this);

        binding.rightProgress.setMax(my_id_dataArrayList.size());
        binding.wrongProgress.setMax(my_id_dataArrayList.size());


        nextQuizQuestion();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu, menu);
        toolbarMenu = menu;
        toolbarMenu.findItem(R.id.bookmark).setVisible(false);
        toolbarMenu.findItem(R.id.report).setVisible(false);
        return true;



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_setting:
                Log.d("KINGSN", "onOptionsItemSelected: kin");
                SettingButtonMethod();
                break;
        }
// ab kr lol default valu return kr raha tha
        return true;
    }
    @SuppressLint("NonConstantResourceId")

    public void SettingButtonMethod() {
        CheckSound();
        if (myCountDownTimer1 != null) {
            myCountDownTimer1.cancel();
        }
        // TODO Auto-generated method stub
        if (myCountDownTimer != null) {
            myCountDownTimer.cancel();

        }
        Intent intent = new Intent(method.activity, SettingActivity.class);
        startActivity(intent);
        method.activity.overridePendingTransition(R.anim.open_next, R.anim.close_next);
    }


    private void nextQuizQuestion() {

        binding.aLayout.setBackgroundResource(R.drawable.answer_bg);
        binding.bLayout.setBackgroundResource(R.drawable.answer_bg);
        binding.cLayout.setBackgroundResource(R.drawable.answer_bg);
        binding.dLayout.setBackgroundResource(R.drawable.answer_bg);
        binding.aLayout.clearAnimation();
        binding.bLayout.clearAnimation();
        binding.cLayout.clearAnimation();
        binding.dLayout.clearAnimation();


        binding.aLayout.setClickable(true);
        binding.bLayout.setClickable(true);
        binding.cLayout.setClickable(true);
        binding.dLayout.setClickable(true);

        binding.btnOpt1.startAnimation(RightSwipe_A);
        binding.btnOpt2.startAnimation(RightSwipe_B);
        binding.btnOpt3.startAnimation(RightSwipe_C);
        binding.btnOpt4.startAnimation(RightSwipe_D);

        binding.txtQuestion1.startAnimation(Fade_in);

        if (questionIndex < my_id_dataArrayList.size()) {
            question = my_id_dataArrayList.get(questionIndex);
            int temp = questionIndex;
            binding.imgQuestion.resetZoom();
            binding.txtQuestionIndex.setText(++temp + "/" + my_id_dataArrayList.size());
            if (!question.getImage().isEmpty()) {
                binding.imgZoom.setVisibility(View.VISIBLE);
                binding.txtQuestion1.setVisibility(View.VISIBLE);
                binding. txtQuestion.setVisibility(View.GONE);
              //  binding. imgQuestion.setImageUrl(question.getImage());
                Glide.with(method.activity).load(question.getImage())
                        .placeholder(null)
                        .into(binding.imgQuestion);
                binding. imgQuestion.setVisibility(View.VISIBLE);
                binding. imgProgress.setVisibility(View.GONE);
                binding. imgZoom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        click++;
                        if (click == 1)
                            binding.imgQuestion.setZoom(1.25f);
                        else if (click == 2)
                            binding.imgQuestion.setZoom(1.50f);
                        else if (click == 3)
                            binding.imgQuestion.setZoom(1.75f);
                        else if (click == 4) {
                            binding.imgQuestion.setZoom(2.00f);
                            click = 0;
                        }
                    }
                });
            } else {
                binding.imgZoom.setVisibility(View.GONE);
                binding.imgQuestion.setVisibility(View.GONE);
                binding.txtQuestion1.setVisibility(View.GONE);
                binding.txtQuestion.setVisibility(View.VISIBLE);
            }


            System.out.println("img Question " + question.getImage());
            binding.txtQuestion.setText(Html.fromHtml(question.getQuestion()));
            binding.txtQuestion1.setText(Html.fromHtml(question.getQuestion()));

            binding. btnOpt1.setText(Html.fromHtml(question.getOptiona().trim()));
            binding.btnOpt2.setText(Html.fromHtml(question.getOptionb().trim()));
            binding.btnOpt3.setText(Html.fromHtml(question.getOptionc().trim()));
            binding.btnOpt4.setText(Html.fromHtml(question.getOptiond().trim()));


            myCountDownTimer = new MyCountDownTimer(GlobalVariables.TIME_PER_QUESTION, GlobalVariables.COUNT_DOWN_TIMER);
            myCountDownTimer.start();
           /* if (myCountDownTimer1 != null) {
                myCountDownTimer1.cancel();
            }*/
            GlobalVariables.LeftTime = 0;
            leftTime = 0;
            //setAgain();


            }else{

            levelCompleted();
        }
    }

    public class MyCountDownTimer extends CountDownTimer {

        private MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            leftTime = millisUntilFinished;

            int progress = (int) (millisUntilFinished / 1000);

            if (progressTimer == null) {
                progressTimer = binding.progressBarTwo;
            } else {
                progressTimer.setCurrentProgress(progress);
            }
            //when left last 5 second we show progress color red
            if (millisUntilFinished <= 6000) {
                progressTimer.SetTimerAttributes(Color.RED, Color.parseColor(GlobalVariables.PROGRESS_BG_COLOR), Color.RED, GlobalVariables.PROGRESS_TEXT_SIZE);
            } else {
                progressTimer.SetTimerAttributes(Color.parseColor(GlobalVariables.PROGRESS_COLOR), Color.parseColor(GlobalVariables.PROGRESS_BG_COLOR), Color.WHITE, GlobalVariables.PROGRESS_TEXT_SIZE);
            }
        }

        @Override
        public void onFinish() {
            if (questionIndex >= my_id_dataArrayList.size()) {
                levelCompleted();

            } else {

                WrongQuestion();
                new Handler().postDelayed(mUpdateUITimerTask, 100);


                questionIndex++;
            }

        }
    }

    private final Runnable mUpdateUITimerTask = new Runnable() {
        public void run() {
            if (method.activity != null) {
                nextQuizQuestion();
            }
        }
    };



    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Log.d("KINGSN", "onClick: hello");
        if (questionIndex < my_id_dataArrayList.size()) {
            question = my_id_dataArrayList.get(questionIndex);
            binding.aLayout.setClickable(false);
            binding.bLayout.setClickable(false);
            binding.cLayout.setClickable(false);
            binding.dLayout.setClickable(false);

            GlobalVariables.LeftTime = 0;
            switch (v.getId()) {
                case R.id.a_layout:
                    AddReview(question, binding.btnOpt1);
                    if (binding.btnOpt1.getText().toString().trim().equalsIgnoreCase(question.getTrueAns().trim())) {
                        questionIndex++;
                        addScore();
                        binding.aLayout.setBackgroundResource(R.drawable.right_gradient);
                        binding.aLayout.startAnimation(animation);
                        binding.bLayout.setBackgroundResource(R.drawable.answer_bg);
                        binding.cLayout.setBackgroundResource(R.drawable.answer_bg);
                        binding.dLayout.setBackgroundResource(R.drawable.answer_bg);

                    } else if (!binding.btnOpt1.getText().toString().trim().equalsIgnoreCase(question.getTrueAns().trim())) {

                        binding.aLayout.setBackgroundResource(R.drawable.wrong_gradient);
                        WrongQuestion();
                        String trueAns = question.getTrueAns().trim();
                        if (binding.btnOpt2.getText().toString().trim().equals(trueAns)) {
                            binding.bLayout.startAnimation(animation);
                            binding.bLayout.setBackgroundResource(R.drawable.right_gradient);
                            binding.cLayout.setBackgroundResource(R.drawable.answer_bg);
                            binding.dLayout.setBackgroundResource(R.drawable.answer_bg);


                        } else if (binding.btnOpt3.getText().toString().trim().equals(trueAns)) {
                            binding.cLayout.setBackgroundResource(R.drawable.right_gradient);
                            binding.cLayout.startAnimation(animation);
                            binding.bLayout.setBackgroundResource(R.drawable.answer_bg);
                            binding.dLayout.setBackgroundResource(R.drawable.answer_bg);

                        } else if (binding.btnOpt4.getText().toString().trim().equals(trueAns)) {
                            binding.dLayout.setBackgroundResource(R.drawable.right_gradient);
                            binding.dLayout.startAnimation(animation);
                            binding.bLayout.setBackgroundResource(R.drawable.answer_bg);
                            binding.cLayout.setBackgroundResource(R.drawable.answer_bg);
                        }


                        questionIndex++;
                    }

                    if (myCountDownTimer != null) {
                        myCountDownTimer.cancel();
                    }
                    if (myCountDownTimer1 != null) {
                        myCountDownTimer1.cancel();
                        leftTime = 0;
                    }
                    break;

                case R.id.b_layout:
                   AddReview(question, binding.btnOpt2);
                    if (binding.btnOpt2.getText().toString().trim().equalsIgnoreCase(question.getTrueAns().trim())) {
                        questionIndex++;
                        addScore();
                        binding.bLayout.setBackgroundResource(R.drawable.right_gradient);
                        binding.bLayout.startAnimation(animation);
                        binding.aLayout.setBackgroundResource(R.drawable.answer_bg);
                        binding.cLayout.setBackgroundResource(R.drawable.answer_bg);
                        binding.dLayout.setBackgroundResource(R.drawable.answer_bg);

                    } else if (!binding.btnOpt2.getText().toString().trim().equalsIgnoreCase(question.getTrueAns().trim())) {

                        String trueAns = question.getTrueAns().trim();
                        binding.bLayout.setBackgroundResource(R.drawable.wrong_gradient);
                        WrongQuestion();

                        if (binding.btnOpt1.getText().toString().trim().equals(trueAns)) {
                            binding.aLayout.startAnimation(animation);
                            binding.aLayout.setBackgroundResource(R.drawable.right_gradient);
                            binding.cLayout.setBackgroundResource(R.drawable.answer_bg);
                            binding.dLayout.setBackgroundResource(R.drawable.answer_bg);

                        } else if (binding.btnOpt3.getText().toString().trim().equals(trueAns)) {
                            binding.cLayout.setBackgroundResource(R.drawable.right_gradient);
                            binding.cLayout.startAnimation(animation);
                            binding.aLayout.setBackgroundResource(R.drawable.answer_bg);
                            binding.dLayout.setBackgroundResource(R.drawable.answer_bg);

                        } else if (binding.btnOpt4.getText().toString().trim().equals(trueAns)) {
                            binding.dLayout.setBackgroundResource(R.drawable.right_gradient);
                            binding.dLayout.startAnimation(animation);
                            binding.aLayout.setBackgroundResource(R.drawable.answer_bg);
                            binding.cLayout.setBackgroundResource(R.drawable.answer_bg);
                        }

                        questionIndex++;
                    }

                    if (myCountDownTimer != null) {
                        myCountDownTimer.cancel();
                    }
                    if (myCountDownTimer1 != null) {
                        myCountDownTimer1.cancel();
                        leftTime = 0;
                    }
                    break;
                case R.id.c_layout:
                    AddReview(question,binding. btnOpt3);
                    if (binding.btnOpt3.getText().toString().trim().equalsIgnoreCase(question.getTrueAns().trim())) {
                        questionIndex++;
                        addScore();
                        binding.cLayout.setBackgroundResource(R.drawable.right_gradient);
                        binding.cLayout.startAnimation(animation);
                        binding.aLayout.setBackgroundResource(R.drawable.answer_bg);
                        binding.bLayout.setBackgroundResource(R.drawable.answer_bg);
                        binding.dLayout.setBackgroundResource(R.drawable.answer_bg);


                    } else if (!binding.btnOpt3.getText().toString().trim().equalsIgnoreCase(question.getTrueAns().trim())) {
                        binding.cLayout.setBackgroundResource(R.drawable.wrong_gradient);
                        String trueAns = question.getTrueAns().trim();
                        WrongQuestion();

                        if (binding.btnOpt1.getText().toString().trim().equals(trueAns)) {
                            binding.aLayout.startAnimation(animation);
                            binding.aLayout.setBackgroundResource(R.drawable.right_gradient);
                            binding.bLayout.setBackgroundResource(R.drawable.answer_bg);
                            binding.dLayout.setBackgroundResource(R.drawable.answer_bg);

                        } else if (binding.btnOpt2.getText().toString().trim().equals(trueAns)) {
                            binding.bLayout.startAnimation(animation);
                            binding.bLayout.setBackgroundResource(R.drawable.right_gradient);
                            binding.aLayout.setBackgroundResource(R.drawable.answer_bg);
                            binding.dLayout.setBackgroundResource(R.drawable.answer_bg);

                        } else if (binding.btnOpt4.getText().toString().trim().equals(trueAns)) {
                            binding.dLayout.setBackgroundResource(R.drawable.right_gradient);
                            binding.dLayout.startAnimation(animation);
                            binding.aLayout.setBackgroundResource(R.drawable.answer_bg);
                            binding.bLayout.setBackgroundResource(R.drawable.answer_bg);
                        }
                        questionIndex++;
                    }
                    if (myCountDownTimer != null) {
                        myCountDownTimer.cancel();
                    }
                    if (myCountDownTimer1 != null) {
                        myCountDownTimer1.cancel();
                        leftTime = 0;
                    }

                    break;
                case R.id.d_layout:
                    AddReview(question, binding.btnOpt4);
                    // AnswerButtonClickMethod(binding.dLayout, btnOpt4);
                    if (binding.btnOpt4.getText().toString().trim().equalsIgnoreCase(question.getTrueAns().trim())) {
                        binding.dLayout.setBackgroundResource(R.drawable.right_gradient);
                        binding.dLayout.startAnimation(animation);
                        questionIndex++;
                        binding.aLayout.setBackgroundResource(R.drawable.answer_bg);
                        binding.bLayout.setBackgroundResource(R.drawable.answer_bg);
                        binding.cLayout.setBackgroundResource(R.drawable.answer_bg);

                        addScore();
                    } else if (!binding.btnOpt4.getText().toString().trim().equalsIgnoreCase(question.getTrueAns().trim())) {
                        binding.dLayout.setBackgroundResource(R.drawable.wrong_gradient);
                        WrongQuestion();
                        String trueAns = question.getTrueAns().trim();

                        if (binding.btnOpt1.getText().toString().trim().equals(trueAns)) {
                            binding.aLayout.startAnimation(animation);
                            binding.aLayout.setBackgroundResource(R.drawable.right_gradient);
                            binding.bLayout.setBackgroundResource(R.drawable.answer_bg);
                            binding.cLayout.setBackgroundResource(R.drawable.answer_bg);

                        } else if (binding.btnOpt2.getText().toString().trim().equals(trueAns)) {
                            binding.bLayout.startAnimation(animation);
                            binding.bLayout.setBackgroundResource(R.drawable.right_gradient);
                            binding.aLayout.setBackgroundResource(R.drawable.answer_bg);
                            binding.cLayout.setBackgroundResource(R.drawable.answer_bg);

                        } else if (binding.btnOpt3.getText().toString().trim().equals(trueAns)) {
                            binding.cLayout.setBackgroundResource(R.drawable.right_gradient);
                            binding.cLayout.startAnimation(animation);
                            binding.aLayout.setBackgroundResource(R.drawable.answer_bg);
                            binding.bLayout.setBackgroundResource(R.drawable.answer_bg);

                        }
                        questionIndex++;
                    }
                    if (myCountDownTimer != null) {
                        myCountDownTimer.cancel();
                    }
                    if (myCountDownTimer1 != null) {
                        myCountDownTimer1.cancel();

                    }
                    break;
            }

        }
    }

    private void addScore() {

        rightSound();
        correctQuestion++;
        binding.txtTrueQuestion.setText(String.valueOf(correctQuestion));
        binding.rightProgress.setProgress(correctQuestion);
        totalScore = totalScore + 5;
        count_question_completed = count_question_completed + 5;
        score = score + 5;
        binding.txtScore.setText(String.valueOf(score));
        rightAns = SharedPrefrence.getRightAns(method.activity);
        rightAns++;
        SharedPrefrence.setRightAns(method.activity, rightAns);
        SharedPrefrence.setScore(method.activity, totalScore);
        SharedPrefrence.setCountQuestionCompleted(method.activity, count_question_completed);
    }

    private void WrongQuestion() {
        //setAgain();
        playWrongSound();
      //  saveScore();
        inCorrectQuestion++;
        totalScore = totalScore - 2;
        count_question_completed = count_question_completed - 2;
        score = score - 2;
        binding.txtFalseQuestion.setText(String.valueOf(inCorrectQuestion));
        binding.wrongProgress.setProgress(inCorrectQuestion);
        binding.txtScore.setText(String.valueOf(score));
    }

    //play sound when answer is incorrect
    private void playWrongSound() {
        if (SharedPrefrence.getSoundEnableDisable(method.activity)) {
            StaticUtils.setwronAnssound(method.activity);
        }
        if (SharedPrefrence.getVibration(method.activity)) {
            StaticUtils.vibrate(method.activity, StaticUtils.VIBRATION_DURATION);
        }
    }

    /*
     * Save score in Preferences
     */
    private void saveScore() {
   //    method.preferencess.setValue(GlobalVariables.COUNT_QUESTION_COMPLETED, String.valueOf(count_question_completed));
      /*  editor.putInt(SettingsPreferences.TOTAL_SCORE, totalScore);
        editor.putInt(SettingsPreferences.LAST_LEVEL_SCORE, score);
        editor.putInt(SettingsPreferences.COUNT_QUESTION_COMPLETED, count_question_completed);
        editor.apply();*/

        try {
            mCallback.onEnteredScore(totalScore);
        } catch (Exception e) {
            System.out.println("" + e.getMessage());
        }

    }
    //set progress again after next question
  /*  private void setAgain() {
        if (progressBarTwo_A.getVisibility() == (View.VISIBLE)) {
            progressBarTwo_A.setVisibility(View.GONE);
            progressBarTwo_B.setVisibility(View.GONE);
            progressBarTwo_C.setVisibility(View.GONE);
            progressBarTwo_D.setVisibility(View.GONE);
        }

    }*/

    //add attended question in ReviewList
    public void AddReview(QuestionList question, TextView tvBtnOpt) {
        reviews.add(new Review(question.getId(),
                question.getQuestion(),
                question.getImage(),
                question.getTrueAns(),
                tvBtnOpt.getText().toString(),
                question.getOptions(),
                question.getNote()));

        leftTime = 0;
        GlobalVariables.LeftTime = 0;
        new Handler().postDelayed(mUpdateUITimerTask, 1000);
        binding.txtScore.setText(String.valueOf(score));
    }

    //play sound when answer is correct
    public void rightSound() {
        if (SharedPrefrence.getSoundEnableDisable(method.activity)) {
            StaticUtils.setrightAnssound(method.activity);
        }
        if (SharedPrefrence.getVibration(method.activity)) {
            StaticUtils.vibrate(method.activity, StaticUtils.VIBRATION_DURATION);
        }
    }


    public void CheckSound() {
        if (SharedPrefrence.getSoundEnableDisable(method.activity)) {
            StaticUtils.backSoundonclick(method.activity);
        }
        if (SharedPrefrence.getVibration(method.activity)) {
            StaticUtils.vibrate(method.activity, StaticUtils.VIBRATION_DURATION);
        }
    }


    public void levelCompleted() {

      //  saveScore();
      /*  method.activity.getSupportFragmentManager().popBackStack();
        method.activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentComplete).addToBackStack("tag").commitAllowingStateLoss();
        */
        blankAllValue();
        /*method.preferencess.removeSharedPreferencesData(method.activity);*/
       /* if (myCountDownTimer1 != null) {
            myCountDownTimer1.cancel();
        }
        // TODO Auto-generated method stub
        if (myCountDownTimer != null) {
            myCountDownTimer.cancel();

        }*/

        String topicId="topicid"+my_id_dataArrayList.get(1).getQtopicId();


         method.preferencess.setValue(""+topicId,"played");
        Log.d("KINGSN", "levelCompleted456: "+method.preferencess.getValue(topicId));
        startActivity(new Intent(QuizPlayActivity.this, ActivityComplete.class));

        method.activity.isDestroyed();
        finish();

    }
    public void blankAllValue() {
        questionIndex = 0;
        totalScore = 0;
        count_question_completed = 0;
        score = 0;
        correctQuestion = 0;
        inCorrectQuestion = 0;
        if (myCountDownTimer1 != null) {
            myCountDownTimer1.cancel();
        }
        // TODO Auto-generated method stub
        if (myCountDownTimer != null) {
            myCountDownTimer.cancel();

        }


    }

    public void PlayAreaLeaveDialog(final Activity context) {
        if (myCountDownTimer != null) {
            myCountDownTimer.cancel();
        }
        if (myCountDownTimer1 != null) {
            myCountDownTimer1.cancel();

        }
       // GlobalVariables.LeftTime = leftTime;

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Message
        alertDialog.setMessage(context.getResources().getString(R.string.exit_msg));
        alertDialog.setCancelable(false);
        final AlertDialog alertDialog1 = alertDialog.create();
        // Setting OK Button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed.
                blankAllValue();
                QuizPlayActivity.this.finishAndRemoveTask();
                finish();
                leftTime = 0;
                GlobalVariables.LeftTime = 0;
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog1.dismiss();
                if (GlobalVariables.LeftTime != 0) {
                    myCountDownTimer = new MyCountDownTimer(GlobalVariables.LeftTime, 1000);
                    myCountDownTimer.start();

                }
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        PlayAreaLeaveDialog(method.activity);
    }


    @Override
    public void onStart() {
        super.onStart();
        updateUi();

    }


    @Override
    public void onResume() {

        if (leftTime != 0) {
            myCountDownTimer1 = new MyCountDownTimer(leftTime, GlobalVariables.COUNT_DOWN_TIMER);
            myCountDownTimer1.start();

        }
        super.onResume();
        updateUi();


        //GlobalVariables.setText(String.valueOf(coin));
    }

    @Override
    public void onPause() {
        if (myCountDownTimer != null) {
            myCountDownTimer.cancel();
        }
        if (myCountDownTimer1 != null) {
            myCountDownTimer1.cancel();
        }
        super.onPause();
    }

    void updateUi() {
        if (method.activity == null) ;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        if (myCountDownTimer != null) {
            myCountDownTimer.cancel();
            leftTime = 0;
        }
        leftTime = 0;
        if (myCountDownTimer1 != null) {
            myCountDownTimer1.cancel();
        }

        super.onDestroy();
    }


}