package com.example.androidebookapp.fragment.quizFragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.example.androidebookapp.R;
import com.example.androidebookapp.activity.BookSubjectActivity;
import com.example.androidebookapp.activity.MainActivity;
import com.example.androidebookapp.activity.SearchBook;
import com.example.androidebookapp.adapter.SpinnerCatAdapter;
import com.example.androidebookapp.adapter.SpinnerSubCatAdapter;
import com.example.androidebookapp.adapter.SpinnerqsubCatAdapter;
import com.example.androidebookapp.adapter.booksdapter.BookdailyBoosterAdapter;
import com.example.androidebookapp.adapter.booksdapter.BookhCategoryAdapterr;
import com.example.androidebookapp.adapter.quizadapter.QuizHomeAdapter;
import com.example.androidebookapp.database.DatabaseHandler;
import com.example.androidebookapp.fragment.bookfragments.BookFragment;
import com.example.androidebookapp.interfaces.OnClick;
import com.example.androidebookapp.item.BookSubCategoryList;
import com.example.androidebookapp.item.CatSpinnerList;
import com.example.androidebookapp.item.CategoryList;
import com.example.androidebookapp.item.QuizCategoryList;
import com.example.androidebookapp.item.qCatSpinnerList;
import com.example.androidebookapp.response.CatSpinnerRP;
import com.example.androidebookapp.response.QuizSpinnerRP;
import com.example.androidebookapp.rest.ApiClient;
import com.example.androidebookapp.rest.ApiInterface;
import com.example.androidebookapp.util.API;
import com.example.androidebookapp.util.EnchantedViewPager;
import com.example.androidebookapp.util.GlobalVariables;
import com.example.androidebookapp.util.Method;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rd.PageIndicatorView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizHomeFragment extends Fragment {


    private Method method;
    private OnClick onClick;
    private ProgressBar progressBar;
    private BookhCategoryAdapterr homeCatAdapter;
    private InputMethodManager imm;
    private TextInputEditText editText_search;
    private PageIndicatorView pageIndicatorView;
    private EnchantedViewPager enchantedViewPager;
    private List<CategoryList> categoryLists;
    private QuizHomeAdapter categoryAdapter;
    private BookdailyBoosterAdapter bookdailyBoosterAdapter;
    private Boolean isOver = false;
    private int paginationIndex = 1;
    private String adsParam = "1";
    public DatabaseHandler db;
    private String catType, catId,subcatType,subcatId;


    private ArrayList<BookSubCategoryList> my_id_dataArrayList;
    public ConstraintLayout conMain, conNoData, conSlider, conContinue, conLatest, conPopular, conAuthor;
    private RecyclerView recyclerViewContinue, recyclerViewLatest, dailyboosterre, recyclerViewCat, recyclerViewAuthor;
     private LinearLayout conCategory;
    private Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000;
    private Boolean isTimerStart = false;
    private final Handler handler = new Handler();
    private Runnable Update;
    SeekBar seekBar1,seekBar2,seekBar3,seekBar4;
    TextView tvOption1,tvOption2,tvOption3,tvOption4;
    TextView tvPercent1,tvPercent2,tvPercent3,tvPercent4;
    double count1=1,count2=1,count3=1,count4=1;
    boolean flag1=true,flag2=true,flag3=true,flag4=true;
    private LayoutAnimationController animation;
    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.quiz_home_fragment, container, false);

        if (MainActivity.toolbar != null) {
            MainActivity.toolbar.setTitle(getResources().getString(R.string.Quiz));
        }

        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        int resId = R.anim.layout_animation_fall_down;
        animation = AnimationUtils.loadLayoutAnimation(getActivity(), resId);


        method = new Method(requireActivity(), onClick);
        categoryLists = new ArrayList<>();
        progressBar = view.findViewById(R.id.progressBar_home);
        conMain = view.findViewById(R.id.con_main_home);
        conNoData = view.findViewById(R.id.con_noDataFound);
        editText_search = view.findViewById(R.id.editText_home);
      /*  enchantedViewPager = view.findViewById(R.id.slider_home);
        pageIndicatorView = view.findViewById(R.id.pageIndicatorView_home);*/
        conSlider = view.findViewById(R.id.con_slider_home);
        conContinue = view.findViewById(R.id.con_continue_home);
      //  conCategory = view.findViewById(R.id.con_category_home);


        recyclerViewCat = view.findViewById(R.id.recyclerView_category_home);
        dailyboosterre = view.findViewById(R.id.dailyboosterre);




        conNoData.setVisibility(View.GONE);
       conMain.setVisibility(View.GONE);
       progressBar.setVisibility(View.GONE);

     /*   enchantedViewPager.useScale();
        enchantedViewPager.removeAlpha();*/



        recyclerViewCat.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManagerCat = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewCat.setLayoutManager(layoutManagerCat);
        recyclerViewCat.setFocusable(false);


        dailyboosterre.setLayoutManager(new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL));
        dailyboosterre.setHasFixedSize(true);
        //  categoryAdapter = new AdapterCategory(baseActivity, categoryDTOArrayList);
        dailyboosterre.setNestedScrollingEnabled(false);






        if (method.isNetworkAvailable()) {
            if (method.isLogin()) {
                //home(method.userId());
                getMyId(requireActivity());
            } else {
               // home("0");
               //category();
                getMyId(requireActivity());
            }
        } else {
            method.alertBox(getResources().getString(R.string.internet_connection));
            progressBar.setVisibility(View.GONE);
        }

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.search_more, menu);
        MenuItem searchItem = menu.findItem(R.id.ic_search);
        searchItem.setOnMenuItemClickListener(item -> {
            categoryName();
           // CatSpinnerRP catSpinnerRP = GlobalVariables.quizCategoryLists;
          //  showSearch(catSpinnerRP);
            return false;
        });
        super.onCreateOptionsMenu(menu, inflater);
    }


    private void bookFragment(String type, String title, String id, String subId) {
        BookFragment bookFragment = new BookFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        if (type.equals("home_cat")) {
            bundle.putString("title", title);
            bundle.putString("id", id);
            bundle.putString("subId", subId);
        }
        bookFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.frameLayout_main,
                bookFragment, title).addToBackStack(title).commitAllowingStateLoss();
    }

    private void hideKeyBord() {
        if (requireActivity().getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(requireActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void search() {
        String search = editText_search.getText().toString();
        //do something
        if (!search.isEmpty() || !search.equals("")) {
            editText_search.clearFocus();
            imm.hideSoftInputFromWindow(editText_search.getWindowToken(), 0);
            startActivity(new Intent(getActivity(), SearchBook.class)
                    .putExtra("id", "0")
                    .putExtra("search", search)
                    .putExtra("type", "normal"));
        } else {
            if (getActivity().getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            }
            method.alertBox(getResources().getString(R.string.please_enter_book));
        }

    }

    private void categoryName() {

        if (getActivity() != null) {

            ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.show();
            progressDialog.setMessage(getResources().getString(R.string.loading));
            progressDialog.setCancelable(false);

            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(getActivity()));
            jsObj.addProperty("method_name", "get_quizcategory_name");
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<QuizSpinnerRP> call = apiService.getQuizcatSpinner(API.toBase64(jsObj.toString()));
            call.enqueue(new Callback<QuizSpinnerRP>() {
                @Override
                public void onResponse(@NotNull Call<QuizSpinnerRP> call, @NotNull Response<QuizSpinnerRP> response) {

                    if (getActivity() != null) {

                        try {

                            QuizSpinnerRP catSpinnerRP = response.body();
                          // Log.d("KINGSNDB", "onResponse: "+catSpinnerRP.getCatSpinnerLists().get(0).getCategory_name());
                            assert catSpinnerRP != null;
                            if (catSpinnerRP.getStatus().equals("1")) {
                                showSearch(catSpinnerRP);
                            } else {
                                method.alertBox(catSpinnerRP.getMessage());
                            }
                        } catch (Exception e) {
                            Log.d("exception_error", e.toString());
                            method.alertBox(getResources().getString(R.string.failed_try_again));
                        }

                    }

                    progressDialog.dismiss();

                }

                @Override
                public void onFailure(@NotNull Call<QuizSpinnerRP> call, @NotNull Throwable t) {
                    // Log error here since request failed
                    Log.e("fail", t.toString());
                    progressDialog.dismiss();
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            });
        }
    }

    private void showSearch(QuizSpinnerRP catSpinnerRP) {

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
        AppCompatSpinner spinnerAuthor2 = dialog.findViewById(R.id.spinner_search_dialog2);
        TextInputEditText editText = dialog.findViewById(R.id.editText_search_dialog);
        editText.setVisibility(View.GONE);
        MaterialButton button = dialog.findViewById(R.id.button_search_dialog);

        textViewTitle.setText(getResources().getString(R.string.search_quizcat));
       // editText.setHint(getResources().getString(R.string.search_book_name));

        catSpinnerRP.getCatSpinnerLists().add(0, new CatSpinnerList("", getResources().getString(R.string.select_category_type)));
        catSpinnerRP.getQcatSpinnerLists().add(0, new qCatSpinnerList("", getResources().getString(R.string.please_select_sub_category)));

        SpinnerCatAdapter typeAdapter = new SpinnerCatAdapter(getActivity(),  catSpinnerRP.getCatSpinnerLists());
        spinnerAuthor.setAdapter(typeAdapter);

        SpinnerqsubCatAdapter typeAdapter2 = new SpinnerqsubCatAdapter(getActivity(),  catSpinnerRP.getQcatSpinnerLists());
        spinnerAuthor2.setAdapter(typeAdapter2);
        spinnerAuthor2.setVisibility(View.VISIBLE);
        dialog.findViewById(R.id.downar2).setVisibility(View.VISIBLE);

        spinnerAuthor2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.textView_spinner));
                } else {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.textView_app_color));
                }
                catType =  catSpinnerRP.getCatSpinnerLists().get(position).getCategory_name();
                catId =  catSpinnerRP.getCatSpinnerLists().get(position).getId();
                subcatType =  catSpinnerRP.getQcatSpinnerLists().get(position).getCategory_name();
                subcatId =  catSpinnerRP.getQcatSpinnerLists().get(position).getId();

                Log.d("KINGSN", "filter onItemSelected: "+subcatId+"\n"+subcatType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        imageViewClose.setOnClickListener(v -> dialog.dismiss());

        button.setOnClickListener(v -> {

            editText.setError(null);

            String search = editText.getText().toString();
            if (catType.equals(getResources().getString(R.string.select_category_type)) || catType.equals("") || catType.isEmpty()) {
                method.alertBox(getResources().getString(R.string.please_select_category));
            } else  if (subcatType.equals(getResources().getString(R.string.select_category_type)) || subcatType.equals("") || subcatType.isEmpty()) {
                method.alertBox(getResources().getString(R.string.please_select_sub_category));

            } else {

                Intent intent = new Intent(requireActivity(), BookSubjectActivity.class);
                intent.putExtra("catid", catId);
                intent.putExtra("Sid", subcatId);
                intent.putExtra("search", search);
                intent.putExtra("type", "category_search");
                intent.putExtra("screenType", "category_search");
                intent.putExtra("subjectType", "QuizSubjectActivity");
                startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.show();
    }






    public void getMyId(FragmentActivity activity) {

        progressBar.setVisibility(View.VISIBLE);

                        for(int i=0;i<GlobalVariables.quizCategoryLists.size();i++){
                            int j=i;


                            bookdailyBoosterAdapter = new BookdailyBoosterAdapter(getActivity(),  GlobalVariables.categoryLists,j, "category", onClick);
                            dailyboosterre.setAdapter(bookdailyBoosterAdapter);
                            dailyboosterre.setLayoutAnimation(animation);


                            Log.d("KINGSN", "getMyId: Called on Quiz home 21");
                            categoryAdapter = new QuizHomeAdapter(getActivity(),  GlobalVariables.quizCategoryLists,i, "category", onClick);
                            recyclerViewCat.setAdapter(categoryAdapter);
                            recyclerViewCat.setLayoutAnimation(animation);


                           conMain.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.VISIBLE);

                            Log.d("KINGSN", "getMyId: Called on Quiz home 2");

                          //  BookdailyBoosterAdapter.hideHeader();


                        }
                    }
    }







