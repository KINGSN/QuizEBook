package com.example.androidebookapp.fragment.bookfragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.androidebookapp.R;
import com.example.androidebookapp.activity.MainActivity;
import com.example.androidebookapp.activity.SearchBook;
import com.example.androidebookapp.adapter.AuthorAdapter;
import com.example.androidebookapp.adapter.SpinnerAuthorAdapter;
import com.example.androidebookapp.adapter.booksdapter.SubjectTabAdapter;
import com.example.androidebookapp.databinding.SubjectFragmentBinding;
import com.example.androidebookapp.interfaces.OnClick;
import com.example.androidebookapp.item.AuthorList;
import com.example.androidebookapp.item.AuthorSpinnerList;
import com.example.androidebookapp.response.AuthorSpinnerRP;
import com.example.androidebookapp.util.Method;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SubjectFragment extends Fragment {
    public SubjectFragmentBinding binding;
    private Method method;
    private OnClick onClick;
    private String authorType, authorId;
    private List<AuthorList> authorLists;
    private ConstraintLayout conNoData;
    private RecyclerView recyclerView;
    private AuthorAdapter authorAdapter;
    private LayoutAnimationController animation;
    private Boolean isOver = false;
    private String adsParam = "1";
    private int paginationIndex = 1;
    private InputMethodManager imm;
    private SubjectTabAdapter subjectTabAdapter;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    String sid;

    public SubjectFragment(String sid) {
        this.sid=sid;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.subject_fragment, container, false);

        binding = SubjectFragmentBinding.inflate(inflater, container, false);
         view = binding.getRoot();
         

        if (MainActivity.toolbar != null) {
            MainActivity.toolbar.setTitle(getResources().getString(R.string.subject));
        }

        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);



        authorLists = new ArrayList<>();

        int resId = R.anim.layout_animation_fall_down;
        animation = AnimationUtils.loadLayoutAnimation(getActivity(), resId);
/*
        onClick = (position, type, id, subId, title, fileType, fileUrl, otherData) -> {
            AuthorBookFragment authorBookFragment = new AuthorBookFragment();
            Bundle bundle = new Bundle();
            bundle.putString("title", title);
            bundle.putString("id", id);
            bundle.putString("type", type);
            authorBookFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.frameLayout_main, authorBookFragment,
                    title).addToBackStack(title).commitAllowingStateLoss();
        };*/

        method = new Method(getActivity(), onClick);

    // subjectTabAdapter=new SubjectTabAdapter(requireActivity(),getActivity().getSupportFragmentManager(),getLifecycle(),sid);

        viewPager= binding.viewPager;


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

       /* viewPager= findViewById(R.id.viewPagerwallet);
        viewPager.setAdapter(walletAdapter);

        tabLayout= findViewById(R.id.tabLayoutwallet);
        tabLayout.setupWithViewPager(viewPager);

        TabAdapter walletAdapter=new TabAdapter(this,getSupportFragmentManager());
        walletAdapter.getItemPosition(1);*/


       /* subjectTabAdapter=new SubjectTabAdapter(getContext(), getActivity().getSupportFragmentManager(),sid);
        subjectTabAdapter.getItemPosition(0);
        binding.viewPager.setAdapter(subjectTabAdapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);*/


        conNoData = view.findViewById(R.id.con_noDataFound);

        //recyclerView = view.findViewById(R.id.//recyclerView_author_fragment);

       // conNoData.setVisibility(View.GONE);
       // progressBar.setVisibility(View.GONE);

        //recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (authorAdapter.getItemViewType(position) == 1) {
                    return 1;
                } else {
                    return 3;
                }
            }
        });
        //recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setFocusable(false);



        callData();

        setHasOptionsMenu(true);
        return view;
    }

    private void callData() {
        if (method.isNetworkAvailable()) {
          //  author();
        } else {
            method.alertBox(getResources().getString(R.string.internet_connection));
        }
    }

    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.search_more, menu);
        MenuItem searchItem = menu.findItem(R.id.ic_search);
        searchItem.setOnMenuItemClickListener(item -> {
        //    authorName();
            return false;
        });
        super.onCreateOptionsMenu(menu, inflater);
    }


    private void showSearch(AuthorSpinnerRP authorSpinnerRP) {

        Dialog dialog = new Dialog(requireActivity());
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

        SpinnerAuthorAdapter typeAdapter = new SpinnerAuthorAdapter(getActivity(), authorSpinnerRP.getAuthorSpinnerLists());
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

                Intent intent = new Intent(requireActivity(), SearchBook.class);
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
