package com.example.androidebookapp.adapter.quizadapter;

import android.app.Activity;
import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.androidebookapp.Interface.Helper;
import com.example.androidebookapp.R;
import com.example.androidebookapp.databinding.BookTabItemBinding;
import com.example.androidebookapp.https.HttpsRequest;
import com.example.androidebookapp.interfaces.OnClick;
import com.example.androidebookapp.item.BookSubCategoryList;
import com.example.androidebookapp.item.QuizCategoryList;
import com.example.androidebookapp.util.GlobalVariables;
import com.example.androidebookapp.util.Method;
import com.example.androidebookapp.util.RestAPI;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class QuizHomeAdapter extends RecyclerView.Adapter<QuizHomeAdapter.ViewHolder> {

    public BookTabItemBinding binding;
    private Method method;
    private Activity activity;
    private String type;
    private int columnWidth;
    private List<QuizCategoryList> categoryLists;
    public ArrayList<BookSubCategoryList> my_id_dataArrayList1;
    private OnClick onClick;
    private QuizHomeAdapter categoryAdapter;
    public QuizSubCatAdapter categoryAdapterr;
    private Boolean isOver = false;
    private int paginationIndex = 1;
    private String adsParam = "1";
    public String CategoryN;


    private final int VIEW_TYPE_LOADING = 0;
    private final int VIEW_TYPE_ITEM = 1;
    private final int VIEW_TYPE_Ad = 2;
      public  int j;

    public QuizHomeAdapter(Activity activity, List<QuizCategoryList> categoryLists, int i, String type, OnClick onClick) {
        this.activity = activity;
        this.type = type;
        this.categoryLists = categoryLists;
        this.j=i;
        CategoryN=GlobalVariables.quizCategoryLists.get(j).getCategory_name();
        Log.d("kingsn", "BookHomeAdapter: "+j+"\n"+CategoryN);
        method = new Method(activity, onClick);
       // method.preferencess.setIntValue("json",j);
        Resources r = activity.getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, r.getDisplayMetrics());
        columnWidth = (int) ((method.getScreenWidth() - ((5 + 3) * padding)));
        Log.d("KINGSN", "QuizHomeAdapter: "+VIEW_TYPE_ITEM);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.book_tab_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView_title.setText(categoryLists.get(position).getCategory_name());

        //category(viewHolder);
        Log.d("KINGSN item count", "onBindViewHolder: "+j+position);
        getMyId(activity,holder,position);
    }

    @Override
    public int getItemCount() {
        return categoryLists.size() ;
    }





    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private ConstraintLayout constraintLayout;
        private MaterialTextView textView_title, textViewItem;
        private RecyclerView recyclerView_bookTab;

        public ViewHolder(View itemView) {
            super(itemView);

           // imageView = itemView.findViewById(R.id.imageView_cat_adapter);
            textView_title = itemView.findViewById(R.id.textView_title);
           // textViewItem = itemView.findViewById(R.id.textView_item_cat_adapter);
            constraintLayout = itemView.findViewById(R.id.con_BooktabMain);
            recyclerView_bookTab = itemView.findViewById(R.id.recyclerView_bookTab);
        }
    }




    public void getMyId(Activity activity,ViewHolder viewholder,int m) {
        method.params.clear();
        method.params.put("mobile","" );
        // method.showToasty(activity,"1",""+GlobalVariables.adminUserID);
        Log.d(GlobalVariables.TAG, "getHomeData2: called"+activity.toString());
        new HttpsRequest(RestAPI.get_QuizSubcategory, method.params, activity).stringPost2(GlobalVariables.TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, JSONObject abcdapp, String title, String message, JSONObject response) {

                if (flag) {

                    try {
                        //  Log.d(GlobalVariables.TAG, "getIDhk:" + response.getJSONObject(GlobalVariables.AppSid).getJSONObject("Results").toString());


                       my_id_dataArrayList1 = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<BookSubCategoryList>>() {
                        }.getType();

                        Log.d("KINGSN", "backResponse: VALUE OF m "+m);
                        CategoryN=GlobalVariables.quizCategoryLists.get(m).getCategory_name();

                        Log.d("KINGSNCategoryN", "backResponse: "+CategoryN);
                        my_id_dataArrayList1 =new Gson().fromJson(response.getJSONObject(GlobalVariables.AppSid).getJSONObject("category_list").getJSONArray(CategoryN).toString(), getpetDTO);

                        viewholder.recyclerView_bookTab.setLayoutManager(new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL));
                        viewholder.recyclerView_bookTab.setHasFixedSize(true);


                        if (my_id_dataArrayList1.size() > 0) {

                            categoryAdapterr = new QuizSubCatAdapter( my_id_dataArrayList1,activity);
                            viewholder.recyclerView_bookTab.setAdapter(categoryAdapterr);

                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        });
    }

}
