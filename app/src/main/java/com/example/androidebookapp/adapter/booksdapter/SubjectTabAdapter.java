package com.example.androidebookapp.adapter.booksdapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.androidebookapp.fragment.subjectTabFragment.subjectTabFragment;
import com.example.androidebookapp.fragment.subjectTabFragment.subjectTabFragmentHn;


public class SubjectTabAdapter extends FragmentStateAdapter {

    private Context mContext;
    String subcategory,subjecttype,Subject_name;

    public SubjectTabAdapter(Context context, @NonNull FragmentManager fragmentManager,@NonNull Lifecycle lifecycle,
                             String sid,String subjectType,String SubjectName) {
        //super(fm);
        super(fragmentManager, lifecycle);
        mContext = context;
        subcategory=sid;
        subjecttype=subjectType;
        Subject_name=SubjectName;
    }









    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position==0){
            return new subjectTabFragment("en",subcategory,subjecttype,Subject_name);
        }
        else
        if(position==1){
            return new subjectTabFragmentHn("hn",subcategory,subjecttype,Subject_name);

        } /*if(position==2){
            return new WithdrawalHistoryFragment();

        }*/
        else
        {

            return new subjectTabFragment("en", subcategory,subjecttype,Subject_name);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
