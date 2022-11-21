package com.example.androidebookapp.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.example.androidebookapp.R;
import com.example.androidebookapp.databinding.ActivityQuizPlayBinding;
import com.example.androidebookapp.interfaces.OnClick;
import com.example.androidebookapp.item.QuestionList;
import com.example.androidebookapp.util.CircularProgressIndicatorr;
import com.example.androidebookapp.util.Constant;
import com.example.androidebookapp.util.GlobalVariables;
import com.example.androidebookapp.util.Method;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;
import java.util.Collections;

public class QuizPlayActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityQuizPlayBinding binding;
    Method method;
    private OnClick onClick;
    public ArrayList<QuestionList> my_id_dataArrayList;
    public QuestionList question;
    public static CircularProgressIndicatorr progressTimer;
    public static MyCountDownTimer myCountDownTimer;
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
    public TextView txtQuestionIndex,
            tvLevel,
            option_a, option_b, option_c, option_d,
            txtScore, txtTrueQuestion, txtFalseQuestion, coin_count;
    int click = 0;
    int textSize;
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(QuizPlayActivity.this, R.layout.activity_quiz_play);
        method = new Method(QuizPlayActivity.this, onClick);
        Fade_in = AnimationUtils.loadAnimation(method.activity, R.anim.fade_out);
        RightSwipe_A = AnimationUtils.loadAnimation(method.activity, R.anim.anim_right_a);
        RightSwipe_B = AnimationUtils.loadAnimation(method.activity, R.anim.anim_right_b);
        RightSwipe_C = AnimationUtils.loadAnimation(method.activity, R.anim.anim_right_c);
        RightSwipe_D = AnimationUtils.loadAnimation(method.activity, R.anim.anim_right_d);

        my_id_dataArrayList=GlobalVariables.questionlist;

        animation = AnimationUtils.loadAnimation(method.activity, R.anim.right_ans_anim); // Change alpha from fully visible
        animation.setDuration(500); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the
    /*    totalScore = SettingsPreferences.getScore(mContext);
        count_question_completed = SettingsPreferences.getCountQuestionCompleted(mContext);
        coin = SettingsPreferences.getPoint(mContext);
        txtScore = (TextView) v.findViewById(R.id.txtScore);
        txtScore.setText(String.valueOf(score));
        coin_count.setText(String.valueOf(coin));

        rightProgress.setMax(Constant.MAX_QUESTION_PER_LEVEL);
        wrongProgress.setMax(Constant.MAX_QUESTION_PER_LEVEL);*/

        nextQuizQuestion();
    }

    private void nextQuizQuestion() {
        myCountDownTimer = new MyCountDownTimer(GlobalVariables.TIME_PER_QUESTION, GlobalVariables.COUNT_DOWN_TIMER);
        if (myCountDownTimer != null) {
            myCountDownTimer.cancel();
            myCountDownTimer.start();
        } else {
            myCountDownTimer.start();
        }
        if (myCountDownTimer1 != null) {
            myCountDownTimer1.cancel();
        }
        GlobalVariables.LeftTime = 0;
        leftTime = 0;
        //setAgain();
        if (questionIndex >= GlobalVariables.MAX_QUESTION_PER_LEVEL) {
            //levelCompleted();
        }
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
            binding.txtQuestionIndex.setText(++temp + "/" + GlobalVariables.MAX_QUESTION_PER_LEVEL);
            if (!question.getImage().isEmpty()) {
                binding.imgZoom.setVisibility(View.VISIBLE);
                binding.txtQuestion1.setVisibility(View.VISIBLE);
                binding. txtQuestion.setVisibility(View.GONE);
               // binding. imgQuestion.setImageUrl(question.getImage());
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
          /*  options = new ArrayList<String>();
            options.addAll(question.getOptions());
            Collections.shuffle(options);
*/
            binding. btnOpt1.setText(Html.fromHtml(question.getOptiona().trim()));
            binding.btnOpt2.setText(Html.fromHtml(question.getOptionb().trim()));
            binding.btnOpt3.setText(Html.fromHtml(question.getOptionc().trim()));
            binding.btnOpt4.setText(Html.fromHtml(question.getOptiond().trim()));

           /* int isfav = bookmarkDBHelper.getBookmarks(question.getId());

            if (isfav == question.getId()) {
                imgBookmark.setImageResource(R.drawable.ic_mark);
                imgBookmark.setTag("mark");
            } else {
                imgBookmark.setImageResource(R.drawable.ic_unmark);
                imgBookmark.setTag("unmark");
            }*/
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
            if (questionIndex >= GlobalVariables.MAX_QUESTION_PER_LEVEL) {
              //  levelCompleted();

            } else {

                //WrongQuestion();
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


    @Override
    public void onClick(View v) {
        if (questionIndex < my_id_dataArrayList.size()) {
            question = my_id_dataArrayList.get(questionIndex);
            binding.aLayout.setClickable(false);
            binding.bLayout.setClickable(false);
            binding.cLayout.setClickable(false);
            binding.dLayout.setClickable(false);

            GlobalVariables.LeftTime = 0;
            switch (v.getId()) {
                case R.id.a_layout:
                 //   AddReview(question, btnOpt1);
                    if (binding.btnOpt1.getText().toString().trim().equalsIgnoreCase(question.getAnswer().trim())) {
                        questionIndex++;
                        addScore();
                        binding.aLayout.setBackgroundResource(R.drawable.right_gradient);
                        binding.aLayout.startAnimation(animation);
                        binding.bLayout.setBackgroundResource(R.drawable.answer_bg);
                        binding.cLayout.setBackgroundResource(R.drawable.answer_bg);
                        binding.dLayout.setBackgroundResource(R.drawable.answer_bg);

                    } else if (!binding.btnOpt1.getText().toString().trim().equalsIgnoreCase(question.getAnswer().trim())) {

                        binding.aLayout.setBackgroundResource(R.drawable.wrong_gradient);
                     //   WrongQuestion();
                        String trueAns = question.getAnswer().trim();
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
                  //  AddReview(question, binding.btnOpt2);
                    if (binding.btnOpt2.getText().toString().trim().equalsIgnoreCase(question.getAnswer().trim())) {
                        questionIndex++;
                        addScore();
                        binding.bLayout.setBackgroundResource(R.drawable.right_gradient);
                        binding.bLayout.startAnimation(animation);
                        binding.aLayout.setBackgroundResource(R.drawable.answer_bg);
                        binding.cLayout.setBackgroundResource(R.drawable.answer_bg);
                        binding.dLayout.setBackgroundResource(R.drawable.answer_bg);

                    } else if (!binding.btnOpt2.getText().toString().trim().equalsIgnoreCase(question.getAnswer().trim())) {

                        String trueAns = question.getAnswer().trim();
                        binding.bLayout.setBackgroundResource(R.drawable.wrong_gradient);
                     //   WrongQuestion();

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
                 //   AddReview(question, btnOpt3);
                    if (binding.btnOpt3.getText().toString().trim().equalsIgnoreCase(question.getAnswer().trim())) {
                        questionIndex++;
                        addScore();
                        binding.cLayout.setBackgroundResource(R.drawable.right_gradient);
                        binding.cLayout.startAnimation(animation);
                        binding.aLayout.setBackgroundResource(R.drawable.answer_bg);
                        binding.bLayout.setBackgroundResource(R.drawable.answer_bg);
                        binding.dLayout.setBackgroundResource(R.drawable.answer_bg);


                    } else if (!binding.btnOpt3.getText().toString().trim().equalsIgnoreCase(question.getAnswer().trim())) {
                        binding.cLayout.setBackgroundResource(R.drawable.wrong_gradient);
                        String trueAns = question.getAnswer().trim();
                    //    WrongQuestion();

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
                   // AddReview(question, btnOpt4);
                    // AnswerButtonClickMethod(binding.dLayout, btnOpt4);
                    if (binding.btnOpt4.getText().toString().trim().equalsIgnoreCase(question.getAnswer().trim())) {
                        binding.dLayout.setBackgroundResource(R.drawable.right_gradient);
                        binding.dLayout.startAnimation(animation);
                        questionIndex++;
                        binding.aLayout.setBackgroundResource(R.drawable.answer_bg);
                        binding.bLayout.setBackgroundResource(R.drawable.answer_bg);
                        binding.cLayout.setBackgroundResource(R.drawable.answer_bg);

                        addScore();
                    } else if (!binding.btnOpt4.getText().toString().trim().equalsIgnoreCase(question.getAnswer().trim())) {
                        binding.dLayout.setBackgroundResource(R.drawable.wrong_gradient);
                      //  WrongQuestion();
                        String trueAns = question.getAnswer().trim();

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

      //  rightSound();
        correctQuestion++;
        txtTrueQuestion.setText(String.valueOf(correctQuestion));
        binding.rightProgress.setProgress(correctQuestion);
        totalScore = totalScore + 5;
        count_question_completed = count_question_completed + 5;
        score = score + 5;
        txtScore.setText(String.valueOf(score));
        rightAns = method.preferencess.getRightAns(method.activity);
        rightAns++;
        method.preferencess.setRightAns(method.activity, rightAns);
        method.preferencess.setScore(method.activity, totalScore);
        method.preferencess.setCountQuestionCompleted(method.activity, count_question_completed);
    }

    //play sound when answer is correct
  /*  public void rightSound() {
        if (SettingsPreferences.getSoundEnableDisable(getActivity())) {
            StaticUtils.setrightAnssound(getActivity());
        }
        if (SettingsPreferences.getVibration(getActivity())) {
            StaticUtils.vibrate(getActivity(), StaticUtils.VIBRATION_DURATION);
        }
    }*/


   /* public void levelCompleted() {
        StaticUtils.TotalQuestion = Constant.MAX_QUESTION_PER_LEVEL;
        StaticUtils.CoreectQuetion = correctQuestion;
        StaticUtils.WrongQuation = inCorrectQuestion;
        myCountDownTimer.cancel();
        editor = settings.edit();
        if (correctQuestion >= Constant.correctAnswer && levelNo == StaticUtils.RequestlevelNo) {
            levelNo = levelNo + 1;
            if (MainActivity.dbHelper.isExist(Constant.CATE_ID, Constant.SUB_CAT_ID)) {
                MainActivity.dbHelper.UpdateLevel(Constant.CATE_ID, Constant.SUB_CAT_ID, levelNo);
            } else {
                MainActivity.dbHelper.insertIntoDB(Constant.CATE_ID, Constant.SUB_CAT_ID, levelNo);
            }
            SettingsPreferences.setNoCompletedLevel(mContext, levelNo);
        }
        if (correctQuestion >= Constant.correctAnswer) {
            editor.putBoolean(SettingsPreferences.IS_LAST_LEVEL_COMPLETED, true);
        } else {
            editor.putBoolean(SettingsPreferences.IS_LAST_LEVEL_COMPLETED, false);
        }
        if (correctQuestion >= 3 && correctQuestion <= 4) {
            coin = coin + Constant.giveOneCoin;
            level_coin = Constant.giveOneCoin;
            StaticUtils.level_coin = level_coin;
        } else if (correctQuestion >= 4 && correctQuestion <= 5) {
            coin = coin + Constant.giveTwoCoins;
            level_coin = Constant.giveTwoCoins;
            StaticUtils.level_coin = level_coin;
        } else if (correctQuestion >= 6 && correctQuestion < 8) {
            coin = coin + Constant.giveThreeCoins;
            level_coin = Constant.giveThreeCoins;
            StaticUtils.level_coin = level_coin;
        } else if (correctQuestion >= 8) {
            coin = coin + Constant.giveFourCoins;
            level_coin = Constant.giveFourCoins;
            StaticUtils.level_coin = level_coin;
        }
        SettingsPreferences.setPoint(mContext, coin);
        coin_count.setText(String.valueOf(coin));
        editor.apply();

        mContext = getActivity().getBaseContext();
        saveScore();
        getActivity().getSupportFragmentManager().popBackStack();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentComplete).addToBackStack("tag").commitAllowingStateLoss();
        blankAllValue();
        SettingsPreferences.removeSharedPreferencesData(getActivity());

    }*/

}