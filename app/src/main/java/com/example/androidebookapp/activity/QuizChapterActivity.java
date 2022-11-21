package com.example.androidebookapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.androidebookapp.Interface.Helper;
import com.example.androidebookapp.R;
import com.example.androidebookapp.adapter.booksdapter.BookChapterAdapter;
import com.example.androidebookapp.adapter.booksdapter.SubjectTabAdapter;
import com.example.androidebookapp.databinding.ActivityQuizchapterBinding;
import com.example.androidebookapp.https.HttpsRequest;
import com.example.androidebookapp.interfaces.OnClick;
import com.example.androidebookapp.item.AuthorList;
import com.example.androidebookapp.item.BookChapterList;
import com.example.androidebookapp.util.GlobalVariables;
import com.example.androidebookapp.util.Method;
import com.example.androidebookapp.util.RestAPI;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class QuizChapterActivity extends AppCompatActivity {
    ActivityQuizchapterBinding binding;
    public Method method;
    public BookChapterAdapter bookSubjectAdapter;
    public ArrayList<BookChapterList> my_id_dataArrayList;
    private OnClick onClick;
    private List<AuthorList> authorLists;
    private LayoutAnimationController animation;
    public  String  cat_id,sub_cat_id,book_subjId,SubCat,subjects_id,Topic_Name,Subject_lang;


    /*public QuizChapterActivity(String subjectsId,String SubjectName,String Language) {
        this.subjects_id=subjectsId;
        this.Subject_Name=SubjectName;
        this.Subject_lang=Language;
    }
*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(QuizChapterActivity.this, R.layout.activity_quizchapter);
        method = new Method(QuizChapterActivity.this, onClick);

        if (getIntent().hasExtra("screenType")) {
            if (Objects.equals(getIntent().getStringExtra("screenType"), "BookViewActivity")) {
                // subjects_id=getIntent().getStringExtra("subjects_id");
                cat_id = getIntent().getStringExtra("cat_id");
                sub_cat_id = getIntent().getStringExtra("sub_cat_id");
                book_subjId = getIntent().getStringExtra("book_subjId");
                Subject_lang = getIntent().getStringExtra("Language");
                Topic_Name = getIntent().getStringExtra("topic");
                binding.toolbar.setTitle(Topic_Name);
            }
        }

        authorLists = new ArrayList<>();

        int resId = R.anim.layout_animation_fall_down;
        animation = AnimationUtils.loadLayoutAnimation(method.activity, resId);

        binding.toolbar.setNavigationOnClickListener(view -> {
            super.onBackPressed();
        });

        getMyId(method.activity);



    }

    public void getMyId(Activity activity) {
        Log.d("KINGSHIN", "getMyId:subjecttabfrag ");
        method.params.clear();
        method.params.put("languageType",Subject_lang);
        method.params.put("chap_subj_id",book_subjId );
        // method.showToasty(activity,"1",""+GlobalVariables.adminUserID);
        Log.d(GlobalVariables.TAG, "getHomeData2: called"+activity.toString());
        new HttpsRequest(RestAPI.get_quiz_subjects_chapter, method.params, activity).stringPost2(GlobalVariables.TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, JSONObject abcdapp, String title, String message, JSONObject response) {
                //  ProjectUtils.pauseProgressDialog();
                //  binding.swipeRefreshLayout.setRefreshing(false);
                if (flag) {
                    // binding.tvNo.setVisibility(View.GONE);



                    try {
                        //  Log.d(GlobalVariables.TAG, "getIDhk:" + response.getJSONObject(GlobalVariables.AppSid).getJSONObject("Results").toString());


                        my_id_dataArrayList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<BookChapterList>>() {
                        }.getType();


                        // Log.d("KINGSNCategoryN", "backResponse: "+CategoryN);
                        my_id_dataArrayList =new Gson().fromJson(response.getJSONObject(GlobalVariables.AppSid).getJSONArray("subjects").toString(), getpetDTO);

                        //  Log.d("KINGSH", "backResponse: "+my_id_dataArrayList1.get(0).getSubCategoryName());
                        // setData();
                        if (my_id_dataArrayList.size() > 0) {


                            bookSubjectAdapter = new BookChapterAdapter(activity,my_id_dataArrayList,"eng");
                            RecyclerView.LayoutManager layoutManagerAuthor = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
                            binding.recycleview.setLayoutManager(layoutManagerAuthor);
                            binding.recycleview.setFocusable(false);
                            binding.recycleview.setAdapter(bookSubjectAdapter);

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {

                   /* setData();

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireActivity());
                    alertDialogBuilder.setTitle(title);
                    alertDialogBuilder.setMessage(message);
                    alertDialogBuilder.setIcon(R.mipmap.ic_launcher);
                    alertDialogBuilder.setPositiveButton(requireActivity().getResources().getString(R.string.ok_message),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    method.loadingDialog.dismiss();
                                    requireActivity().finish();
                                    // startActivity(new Intent(requireActivity(), LoginPageActivity.class));
                                    //Log.d("Response",msg);
                                    // finishAffinity();

                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    // alertDialog.show();
         */       }
            }
        });
    }



}