package com.example.androidebookapp.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidebookapp.Interface.Helper;
import com.example.androidebookapp.R;
import com.example.androidebookapp.adapter.booksdapter.BooksAdapter;
import com.example.androidebookapp.databinding.FragmentBookchapterBinding;
import com.example.androidebookapp.https.HttpsRequest;
import com.example.androidebookapp.item.BookitemList;
import com.example.androidebookapp.util.GlobalVariables;
import com.example.androidebookapp.util.Method;
import com.example.androidebookapp.util.RestAPI;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class booksViewFragment extends Fragment {

    public FragmentBookchapterBinding binding;
    public Method method;
    public BooksAdapter bookViewAdapter;
    public ArrayList<BookitemList> my_id_dataArrayList;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public String Language;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String SubCat,subjects_id,Subject_Name,Subject_lang;


    public booksViewFragment(String bookchapter_id, String SubjectName, String Language) {
        this.subjects_id=bookchapter_id;
        this.Subject_Name=SubjectName;
        this.Subject_lang=Language;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        method = new Method(getActivity());
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_subject_tab, container, false);

        binding = FragmentBookchapterBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        binding.cptitle.setVisibility(View.GONE);

        getMyId(requireActivity());

        return view;
    }

    public void getMyId(Activity activity) {
        Log.d("KINGSHIN", "getMyId:subjecttabfrag ");
       method.params.clear();
       method.params.put("languageType",Subject_lang);
       method.params.put("bookchapter_id",subjects_id );
        // method.showToasty(activity,"1",""+GlobalVariables.adminUserID);
        Log.d(GlobalVariables.TAG, "getHomeData2: called"+activity.toString());
        new HttpsRequest(RestAPI.get_books, method.params, activity).stringPost2(GlobalVariables.TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, JSONObject abcdapp, String title, String message, JSONObject response) {
                //  ProjectUtils.pauseProgressDialog();
                //  binding.swipeRefreshLayout.setRefreshing(false);
                if (flag) {
                    // binding.tvNo.setVisibility(View.GONE);



                    try {
                        //  Log.d(GlobalVariables.TAG, "getIDhk:" + response.getJSONObject(GlobalVariables.AppSid).getJSONObject("Results").toString());


                        my_id_dataArrayList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<BookitemList>>() {
                        }.getType();


                       // Log.d("KINGSNCategoryN", "backResponse: "+CategoryN);
                        my_id_dataArrayList =new Gson().fromJson(response.getJSONObject(GlobalVariables.AppSid).getJSONArray("subjects").toString(), getpetDTO);

                        //  Log.d("KINGSH", "backResponse: "+my_id_dataArrayList1.get(0).getSubCategoryName());
                        // setData();
                        if (my_id_dataArrayList.size() > 0) {


                            bookViewAdapter = new BooksAdapter(activity,my_id_dataArrayList,"eng");
                            RecyclerView.LayoutManager layoutManagerAuthor = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
                            binding.recycleview.setLayoutManager(layoutManagerAuthor);
                            binding.recycleview.setFocusable(false);
                            binding.recycleview.setAdapter(bookViewAdapter);

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