package com.example.androidebookapp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.androidebookapp.R;
import com.example.androidebookapp.adapter.SpinnerAuthorAdapter;
import com.example.androidebookapp.adapter.booksdapter.SubjectTabAdapter;
import com.example.androidebookapp.databinding.ActivityBookSubjectBinding;
import com.example.androidebookapp.interfaces.OnClick;
import com.example.androidebookapp.item.AuthorList;
import com.example.androidebookapp.item.AuthorSpinnerList;
import com.example.androidebookapp.response.AuthorSpinnerRP;
import com.example.androidebookapp.util.Method;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;


public class BookSubjectActivity extends AppCompatActivity {
    ActivityBookSubjectBinding binding;
    private Method method;
    private OnClick onClick;
    private String authorType, authorId,subjecttype,SubjectName;
    private List<AuthorList> authorLists;
    private InputMethodManager imm;
    private SubjectTabAdapter subjectTabAdapter;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    public String sid;
    private LayoutAnimationController animation;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        binding = ActivityBookSubjectBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
        binding = DataBindingUtil.setContentView(BookSubjectActivity.this, R.layout.activity_book_subject);
        method = new Method(BookSubjectActivity.this, onClick);


        authorLists = new ArrayList<>();

        int resId = R.anim.layout_animation_fall_down;
        animation = AnimationUtils.loadLayoutAnimation(method.activity, resId);

        if (getIntent().hasExtra("screenType")) {
            sid=getIntent().getStringExtra("Sid");
            subjecttype=getIntent().getStringExtra("screenType");
            SubjectName=getIntent().getStringExtra("SubjectName");
        }


        subjectTabAdapter=new SubjectTabAdapter(BookSubjectActivity.this,getSupportFragmentManager(),getLifecycle(),
                sid,subjecttype,SubjectName);

        viewPager= binding.viewPager;
        binding.toolbar.setTitle(SubjectName);
        binding.toolbar.setNavigationOnClickListener(view -> {
            super.onBackPressed();
        });

        tabLayout= binding.tabLayout;
        tabLayout.addTab(tabLayout.newTab().setText("English"));
        tabLayout.addTab(tabLayout.newTab().setText("Hindi"));


        binding.viewPager.setAdapter(subjectTabAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });



        callData();



    }

    private void callData() {
        if (method.isNetworkAvailable()) {
            //  author();
        } else {
            method.alertBox(getResources().getString(R.string.internet_connection));
        }
    }



    private void showSearch(AuthorSpinnerRP authorSpinnerRP) {

        Dialog dialog = new Dialog(method.activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.search_dialog);
        if (method.isRtl()) {
            dialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);

        MaterialTextView textViewTitle = dialog.findViewById(R.id.textView_title_search_dialog);
        ImageView imageViewClose = dialog.findViewById(R.id.imageView_close_search_dialog);
        AppCompatSpinner spinnerAuthor = dialog.findViewById(R.id.spinner_search_dialog);
        TextInputEditText editText = dialog.findViewById(R.id.editText_search_dialog);
        MaterialButton button = dialog.findViewById(R.id.button_search_dialog);

        textViewTitle.setText(getResources().getString(R.string.search_author));
        editText.setHint(getResources().getString(R.string.search_book_name));

        authorSpinnerRP.getAuthorSpinnerLists().add(0, new AuthorSpinnerList("", getResources().getString(R.string.select_author_type)));

        SpinnerAuthorAdapter typeAdapter = new SpinnerAuthorAdapter(BookSubjectActivity.this, authorSpinnerRP.getAuthorSpinnerLists());
        spinnerAuthor.setAdapter(typeAdapter);

        spinnerAuthor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.textView_spinner));
                } else {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.textView_app_color));
                }
                authorType = authorSpinnerRP.getAuthorSpinnerLists().get(position).getAuthor_name();
                authorId = authorSpinnerRP.getAuthorSpinnerLists().get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        imageViewClose.setOnClickListener(v -> dialog.dismiss());

        button.setOnClickListener(v -> {

            editText.setError(null);

            String search = editText.getText().toString();
            if (authorType.equals(getResources().getString(R.string.select_author_type)) || authorType.equals("") || authorType.isEmpty()) {
                method.alertBox(getResources().getString(R.string.please_select_author));
            } else if (search.isEmpty()) {
                editText.requestFocus();
                editText.setError(getResources().getString(R.string.please_enter_book));
            } else {

                editText.clearFocus();
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

                Intent intent = new Intent(BookSubjectActivity.this, SearchBook.class);
                intent.putExtra("id", authorId);
                intent.putExtra("search", search);
                intent.putExtra("type", "author_search");
                startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.show();
    }




}