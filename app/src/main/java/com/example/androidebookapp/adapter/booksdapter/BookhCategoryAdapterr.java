package com.example.androidebookapp.adapter.booksdapter;

import android.app.Activity;
import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidebookapp.Interface.Helper;
import com.example.androidebookapp.R;
import com.example.androidebookapp.adapter.CategoryAdapterr;
import com.example.androidebookapp.databinding.BookTabItemBinding;
import com.example.androidebookapp.https.HttpsRequest;
import com.example.androidebookapp.interfaces.OnClick;
import com.example.androidebookapp.item.BookSubCategoryList;
import com.example.androidebookapp.item.CategoryList;
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

public class BookhCategoryAdapterr extends RecyclerView.Adapter {
public Integer m=0;
    private Method method;
    private Activity activity;
    private String type;
    private int columnWidth;
    private List<CategoryList> categoryLists;
   public BookTabItemBinding binding;
    public ArrayList<BookSubCategoryList> my_id_dataArrayList1;
    private final int VIEW_TYPE_LOADING = 0;
    private final int VIEW_TYPE_ITEM = 1;
    private final int VIEW_TYPE_Ad = 2;
    public CategoryAdapterr categoryAdapterr;
    private String adsParam = "1";
    public  ViewHolder viewHolder;
    public int i=0;

    public BookhCategoryAdapterr(Activity activity, List<CategoryList> categoryLists,int i, String type, OnClick onClick) {
        this.activity = activity;
        this.type = type;
        this.categoryLists = categoryLists;
        this.i=i;
        method = new Method(activity, onClick);
        Resources r = activity.getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, r.getDisplayMetrics());
        columnWidth = (int) ((method.getScreenWidth() - ((5 + 3) * padding)));
    }

    @NotNull
    @Override
    public BookhCategoryAdapterr.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(activity).inflate(R.layout.book_tab_item, parent, false);
            return new ViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {

            final ViewHolder viewHolder = (ViewHolder) holder;

         //   viewHolder.constraintLayout.setLayoutParams(new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, columnWidth / 3));


        }
    }

    @Override
    public int getItemCount() {
        return categoryLists.size() + 1;
    }



    @Override
    public int getItemViewType(int position) {
        if (categoryLists.size() == position) {
            return VIEW_TYPE_LOADING;
        } else if (categoryLists.get(position).isIs_ads()) {
            return VIEW_TYPE_Ad;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private ConstraintLayout constraintLayout;
        private MaterialTextView textView, textViewItem;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView_cat_adapter);
            textView = itemView.findViewById(R.id.textViewName_cat_adapter);
            textViewItem = itemView.findViewById(R.id.textView_item_cat_adapter);
            constraintLayout = itemView.findViewById(R.id.con_BooktabMain);
        }
    }



    public void getMyId(Activity activity) {
        // method.loadingDialogg(activity);
        method.params.clear();
        method.params.put("mobile","" );
        // method.showToasty(activity,"1",""+GlobalVariables.adminUserID);
        Log.d(GlobalVariables.TAG, "getHomeData2: called"+activity.toString());
        new HttpsRequest(RestAPI.get_bookcategory, method.params, activity).stringPost2(GlobalVariables.TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, JSONObject abcdapp, String title, String message, JSONObject response) {
                //  ProjectUtils.pauseProgressDialog();
                //  binding.swipeRefreshLayout.setRefreshing(false);
                if (flag) {
                    // binding.tvNo.setVisibility(View.GONE);

                   // conMain.setVisibility(View.VISIBLE);

                    try {
                        //  Log.d(GlobalVariables.TAG, "getIDhk:" + response.getJSONObject(GlobalVariables.AppSid).getJSONObject("Results").toString());
                        my_id_dataArrayList1 = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<BookSubCategoryList>>() {
                        }.getType();
                        my_id_dataArrayList1 = (ArrayList<BookSubCategoryList>) new Gson().fromJson(response.getJSONObject(GlobalVariables.AppSid).getJSONObject("category_list").getJSONArray(String.valueOf(GlobalVariables.categoryLists.get(i))).toString(), getpetDTO);

                        Log.d("KINGSNSIZE", "backResponse: "+ my_id_dataArrayList1);
                        if (my_id_dataArrayList1.size() > 0) {


                          /*  viewHolder. recyclerView_category_home.setHasFixedSize(true);
                            RecyclerView.LayoutManager layoutManagerCat = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
                            viewHolder. recyclerView_category_home.setLayoutManager(layoutManagerCat);
                            viewHolder. recyclerView_category_home.setFocusable(false);

                            categoryAdapterr = new CategoryAdapterr(activity, my_id_dataArrayList1, "category");
                            viewHolder. recyclerView_category_home.setAdapter(categoryAdapterr);*/
                          //  categoryAdapterr.notifyDataSetChanged();
                           // recyclerView_category_home.setLayoutAnimation(animation);


                            /*    recyclerView_category_home.setHasFixedSize(true);
                                GridLayoutManager layoutManager = new GridLayoutManager(activity, 3);
                                layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                                    @Override
                                    public int getSpanSize(int position) {
                                        if (categoryAdapterr.getItemViewType(position) == 1) {
                                            return 1;
                                        } else {
                                            return 3;
                                        }
                                    }
                                });
                                recyclerView_category_home.setLayoutManager(layoutManager);
                                recyclerView_category_home.setFocusable(false);*/

                            // autoScroll();

                        }

                        // setData();

                        for(int i=0;i<my_id_dataArrayList1.size();i++){
                            int j=i;
                            // Get current json object
                           /* my_id_dataArrayList1 = new ArrayList<>();
                            Type getpetDTO = new TypeToken<List<BookhSubCategoryList>>() {
                            }.getType();
                            my_id_dataArrayList1 = (ArrayList<BookhSubCategoryList>) new Gson().fromJson(response.getJSONObject(GlobalVariables.AppSid).getJSONObject("category_list").getJSONArray("").toString(), getpetDTO);

*/
                            //JSONObject ob=array.getJSONObject(i);




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
