package com.example.androidebookapp.activity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import com.example.androidebookapp.R;
import com.example.androidebookapp.databinding.ActivityCompleteBinding;
import com.example.androidebookapp.interfaces.OnClick;
import com.example.androidebookapp.item.Review;
import com.example.androidebookapp.preferences.SharedPrefrence;
import com.example.androidebookapp.util.CircularProgressIndicator2;
import com.example.androidebookapp.util.GlobalVariables;
import com.example.androidebookapp.util.Method;
import com.example.androidebookapp.util.StaticUtils;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class ActivityComplete extends AppCompatActivity implements View.OnClickListener  {
    public CircularProgressIndicator2 result_prog;
    Menu toolbarMenu;
    ActivityCompleteBinding binding;
    public Method method;
    public SharedPreferences settings;
    public OnClick onClick;
    public static Context mContex;
    int levelNo = 1,
            lastLevelScore = 0,
            coin = 0,
            totalScore = 0;

    public View v;
    boolean isLevelCompleted;


    ///for pdf generate
    AlertDialog alertDialog;
    Button btnView, btnShare1, btnCancel;
    ProgressBar progressBar;
    public static final String[] WRITE_EXTERNAL_STORAGE_PERMS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(ActivityComplete.this, R.layout.activity_complete);
        method = new Method(ActivityComplete.this, onClick);

     
        binding.btnPlayagain.setOnClickListener(this);
        binding.btnRate.setOnClickListener(this);
        binding.btnQuite.setOnClickListener(this);
        binding.btnReview.setOnClickListener(this);
        binding.btnPdf.setOnClickListener(this);
        if (QuizPlayActivity.reviews.size() == 0) {
            binding.btnReview.setVisibility(View.GONE);
        }
        binding.btnShare.setOnClickListener(this);
        isLevelCompleted = SharedPrefrence.getBoolean(SharedPrefrence.IS_LAST_LEVEL_COMPLETED,this);

       // levelNo = MainActivity.dbHelper.GetLevelById(GlobalVariables.CATE_ID, GlobalVariables.SUB_CAT_ID);

        binding.toolbar.setTitle("Result");
        binding.toolbar.setNavigationOnClickListener(view -> {
            super.onBackPressed();
        });
        
    /*    binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                method.activity.getSupportFragmentManager().popBackStack();
                if (method.activity.getSupportFragmentManager().getBackStackEntryCount() == 0) {

                    try {
                        AppController.StopSound();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }


                }
            }
        });*/
        
        if (isLevelCompleted) {
            // levelNo--;
            if (GlobalVariables.TotalLevel == StaticUtils.RequestlevelNo) {
                binding.btnPlayagain.setText("Play Again");

            } else {
                binding.txtResultTitle.setText(method.activity.getString(R.string.completed));
                 binding.btnPlayagain.setText(getResources().getString(R.string.next_play));

            //    binding.tvLevel.setText(getResources().getString(R.string.next_play));
            }
        } else {
             binding.txtResultTitle.setText(method.activity.getString(R.string.not_completed));
             binding.btnPlayagain.setText(getResources().getString(R.string.play_next));
         //   binding.tvLevel.setText(getResources().getString(R.string.play_next));
        }
        binding.resultProgress.setCurrentProgress((double) getPercentageCorrect(StaticUtils.TotalQuestion, StaticUtils.CoreectQuetion));

        binding.right.setText("" + StaticUtils.CoreectQuetion);
        binding.wrong.setText("" + StaticUtils.WrongQuation);
        /*adRequest = new AdRequest.Builder().build();
        FragmentPlay.interstitial.loadAd(adRequest);*/



       
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
        int id = item.getItemId();
        if(id == R.id.action_setting){
            Log.d("KINGSN", "onOptionsItemSelected: Kin");
            Toast.makeText(this, "setting clicked by shubhcode", Toast.LENGTH_SHORT).show();

            if (method.preferencess.getSoundEnableDisable(method.activity)) {
                StaticUtils.backSoundonclick(method.activity);
            }
            if (method.preferencess.getVibration(method.activity)) {
                StaticUtils.vibrate(method.activity, StaticUtils.VIBRATION_DURATION);
            }
            Intent playQuiz = new Intent(method.activity, SettingActivity.class);
            startActivity(playQuiz);
            method.activity.overridePendingTransition(R.anim.open_next, R.anim.close_next);
        }
        return super.onOptionsItemSelected(item);
    }

    public static float getPercentageCorrect(int questions, int correct) {
        float proportionCorrect = ((float) correct) / ((float) questions);
        return proportionCorrect * 100;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_playagain:
                if (isLevelCompleted) {
                    if (GlobalVariables.TotalLevel == StaticUtils.RequestlevelNo) {
                        StaticUtils.RequestlevelNo = 1;
                    } else {
                        StaticUtils.RequestlevelNo = StaticUtils.RequestlevelNo + 1;
                    }

                 /*   ArrayList<QuestionList> question = new ArrayList<>();
                    question.clear();
                    question = filter(FragmentLock.questionList, "" + StaticUtils.RequestlevelNo);
                    if (question.size() < GlobalVariables.MAX_QUESTION_PER_LEVEL) {
                        Toast.makeText(mContex, getString(R.string.no_enough_question), Toast.LENGTH_SHORT).show();
                    } else {
                        FragmentPlay.displayInterstitial();
                        FragmentTransaction ft = method.activity.getSupportFragmentManager().beginTransaction();
                        method.activity.getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        ft.replace(R.id.fragment_container, fragmentPlay, "fragment");
                        ft.addToBackStack("tag");
                        ft.commit();

                    }
                } else {
                    FragmentPlay.displayInterstitial();
                    FragmentTransaction ft = method.activity.getSupportFragmentManager().beginTransaction();
                    method.activity.getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    ft.replace(R.id.fragment_container, fragmentPlay, "fragment");
                    ft.addToBackStack("tag");
                    ft.commit();
*/
                }

                break;
            case R.id.btn_share:
            //    Utils.displayInterstitial();
                shareClicked();


                break;

            case R.id.btnPdf:
               // Utils.displayInterstitial();
              //  PdfClicked();
                break;
            case R.id.btn_rate:

              //  Utils.displayInterstitial();
                rateClicked();

                break;
            case R.id.btn_quite:

              finish();

                break;
            case R.id.btn_review:

                Intent intent = new Intent(method.activity, ReviewActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    public void shareClicked() {
        final String sharetext = "I have finished Level No : " + StaticUtils.RequestlevelNo + " with " + lastLevelScore + " Score in " + getString(R.string.app_name);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        share.putExtra(Intent.EXTRA_TEXT, "" + sharetext + "  https://play.google.com/store/apps/details?id=" + method.activity.getPackageName());
        startActivity(Intent.createChooser(share, "Share " + getString(R.string.app_name) + "!"));
    }

    private void rateClicked() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id="
                            + method.activity.getPackageName())));
        } catch (ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id="
                            + method.activity.getPackageName())));
        }
    }

    public void PdfClicked() {
        if (ContextCompat.checkSelfPermission(method.activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(method.activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(method.activity, WRITE_EXTERNAL_STORAGE_PERMS, 0);
        } else {
            final AlertDialog.Builder dialog = new AlertDialog.Builder(method.activity);
            LayoutInflater inflater1 = (LayoutInflater) (method.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
            assert inflater1 != null;
            @SuppressLint("InflateParams") final View dialogView = inflater1.inflate(R.layout.progress_dialogpdf, null);
            dialog.setView(dialogView);
            progressBar = (ProgressBar) dialogView.findViewById(R.id.progressBar);
            btnView = (Button) dialogView.findViewById(R.id.btnView);
            btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);
            btnShare1 = (Button) dialogView.findViewById(R.id.btnShare);
            btnShare1.setVisibility(View.INVISIBLE);
            btnView.setVisibility(View.INVISIBLE);

            progressBar.setMax(100);
            alertDialog = dialog.create();
            alertDialog.setCancelable(false);
            alertDialog.show();
            btnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "quiz/report.pdf");
                    Uri uri = FileProvider.getUriForFile(method.activity, method.activity.getPackageName() + ".provider", file);
                    Intent target = new Intent(Intent.ACTION_VIEW);
                    target.setDataAndType(uri, "application/pdf");
                    target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    target.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                    try {
                        startActivity(Intent.createChooser(target, "Open File"));
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                        // Instruct the user to install a PDF reader here, or something
                    }
                    alertDialog.dismiss();
                }
            });
            btnShare1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "quiz/report.pdf");
                    // Uri uri = Uri.fromFile(file);
                    Uri uri = FileProvider.getUriForFile(method.activity, method.activity.getPackageName() + ".provider", file);
                    String sharetext = "I have finished Level No : " + StaticUtils.RequestlevelNo + " with " + lastLevelScore + " Score in " + getString(R.string.app_name);
                    Intent share = new Intent();
                    share.setAction(Intent.ACTION_SEND);
                    share.setType("application/pdf");
                    share.putExtra(Intent.EXTRA_TEXT, "" + sharetext + "  https://play.google.com/store/apps/details?id=" + method.activity.getPackageName());
                    share.putExtra(Intent.EXTRA_STREAM, uri);
                    method.activity.startActivity(Intent.createChooser(share, "Share"));
                    alertDialog.dismiss();
                }
            });
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            if (method.activity != null)
                new GeneratePdf().execute();
        }
    }

    /*
     * Generate pdf for current level question
     */
    public void GeneratePdf(String dest) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        if (new File(dest).exists()) {
            new File(dest).delete();
        }

        try {
            /*
             * Creating Document
             */
            final Document document = new Document();

            // Location to save
            PdfWriter.getInstance(document, new FileOutputStream(dest));

            // Open to write
            document.open();

            // Document binding.settings
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("WRTEAM");
            document.addCreator("WRTEAM");

            /*
             * Variables for further use....
             */
            BaseColor mColorAccent = new BaseColor(0, 153, 204, 255);
            float mHeadingFontSize = 26.0f;
            final float mValueFontSize = 24.0f;

            /*
             * How to USE FONT....
             */
            final BaseFont urName = BaseFont.createFont("assets/fonts/centarell.ttf", "UTF-8", BaseFont.EMBEDDED);

            // LINE SEPARATOR
            final LineSeparator lineSeparator = new LineSeparator();
            lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));

            // Title Order Details...
            // Adding Title....
            Font mOrderDetailsTitleFont = new Font(urName, 36.0f, Font.NORMAL, BaseColor.BLACK);
            Chunk mOrderDetailsTitleChunk = new Chunk("Online Quiz", mOrderDetailsTitleFont);
            Paragraph mOrderDetailsTitleParagraph = new Paragraph(mOrderDetailsTitleChunk);
            mOrderDetailsTitleParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(mOrderDetailsTitleParagraph);


            //here use loop for add questionList in page
            for (int i = 0; i < QuizPlayActivity.reviews.size(); i++) {
                final Review review = QuizPlayActivity.reviews.get(i);
                Font mOrderAcNameFont1 = new Font(urName, mHeadingFontSize, Font.NORMAL, mColorAccent);
                Chunk mOrderAcNameChunk1 = new Chunk(("" + (i + 1) + ". " + Html.fromHtml(review.getQuestion())), mOrderAcNameFont1);
                Paragraph mOrderAcNameParagraph1 = new Paragraph(mOrderAcNameChunk1);
                document.add(mOrderAcNameParagraph1);
                try {
                    if (!review.getImgUrl().isEmpty() || review.getImgUrl() != null) {
                        String imageUrl = review.getImgUrl();

                        Image image2 = Image.getInstance(new URL(imageUrl));
                        image2.scaleAbsolute(200f, 200f);
                        document.add(image2);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                QuestionOption(document, urName, mValueFontSize, review, lineSeparator);
            }

            document.close();


            //FileUtils.openFile(method.activity, new File(dest));

        } catch (IOException | DocumentException ie) {
            Log.e("createPdf: Error", "" + ie.getLocalizedMessage());
        } catch (ActivityNotFoundException ae) {
            Toast.makeText(method.activity, "No application found to open this file.", Toast.LENGTH_SHORT).show();
        }
    }



    public void QuestionOption(Document document, BaseFont urName, float mValueFontSize, Review review, LineSeparator lineSeparator) {
        try {
            Font mOrderDateValueFont = new Font(urName, mValueFontSize, Font.NORMAL, BaseColor.BLACK);
            Chunk mOrderDateValueChunk = new Chunk("(a) " + Html.fromHtml(review.getOptionList().get(0)), mOrderDateValueFont);
            Paragraph mOrderDateValueParagraph = new Paragraph(mOrderDateValueChunk);
            document.add(mOrderDateValueParagraph);

            Font mOrderDateValueFont1 = new Font(urName, mValueFontSize, Font.NORMAL, BaseColor.BLACK);
            Chunk mOrderDateValueChunk1 = new Chunk("(b) " + Html.fromHtml(review.getOptionList().get(1)), mOrderDateValueFont1);
            Paragraph mOrderDateValueParagraph1 = new Paragraph(mOrderDateValueChunk1);
            document.add(mOrderDateValueParagraph1);

            Font mOrderDateValueFont2 = new Font(urName, mValueFontSize, Font.NORMAL, BaseColor.BLACK);
            Chunk mOrderDateValueChunk2 = new Chunk("(c) " + Html.fromHtml(review.getOptionList().get(2)), mOrderDateValueFont2);
            Paragraph mOrderDateValueParagraph2 = new Paragraph(mOrderDateValueChunk2);
            document.add(mOrderDateValueParagraph2);

            Font mOrderDateValueFont3 = new Font(urName, mValueFontSize, Font.NORMAL, BaseColor.BLACK);
            Chunk mOrderDateValueChunk3 = new Chunk("(d) " + Html.fromHtml(review.getOptionList().get(3)), mOrderDateValueFont3);
            Paragraph mOrderDateValueParagraph3 = new Paragraph(mOrderDateValueChunk3);
            document.add(mOrderDateValueParagraph3);

            Font mOrderDateValueFont4 = new Font(urName, mValueFontSize, Font.NORMAL, new BaseColor(139, 0, 0));
            Chunk mOrderDateValueChunk4 = new Chunk("True Answer  : " + Html.fromHtml(review.getRightAns()), mOrderDateValueFont4);
            mOrderDateValueChunk4.setUnderline(0.1f, -2f);
            Paragraph mOrderDateValueParagraph4 = new Paragraph(mOrderDateValueChunk4);
            document.add(mOrderDateValueParagraph4);

            document.add(new Paragraph(""));
            document.add(new Chunk(lineSeparator));
            document.add(new Paragraph(""));
        } catch (DocumentException ie) {
            Log.e("createPdf: Error", "" + ie.getLocalizedMessage());
        } catch (ActivityNotFoundException ae) {
            Toast.makeText(method.activity, "No application found to open this file.", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GeneratePdf extends AsyncTask<String, Void, String> {

        @Override
        public void onPreExecute() {
            progressBar.setIndeterminate(true);
        }

        @Override
        protected String doInBackground(String... params) {

            GeneratePdf(getAppPath(Objects.requireNonNull(method.activity)) + "report.pdf");
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            btnShare1.setVisibility(View.VISIBLE);
            btnView.setVisibility(View.VISIBLE);
            progressBar.setIndeterminate(false);
            progressBar.setProgress(100);

        }


        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    /**
     * Get Path of App which contains Files
     *
     * @return path of root dir
     */
    public static String getAppPath(Context context) {

        File dir = new File(Environment.getExternalStorageDirectory()
                + File.separator
                + context.getResources().getString(R.string.app_name)
                + File.separator);
        // Uri uri= FileProvider.getUriForFile(context, context.getPackageName()+".provider",dir);
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir.getPath() + File.separator;
    }

    @Override
    public void onResume() {
        super.onResume();
       // Utils.loadAd(method.activity);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}