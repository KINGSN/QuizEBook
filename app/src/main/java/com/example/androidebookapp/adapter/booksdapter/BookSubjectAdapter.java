package com.example.androidebookapp.adapter.booksdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidebookapp.R;
import com.example.androidebookapp.activity.BookViewActivity;
import com.example.androidebookapp.activity.QuizChapterActivity;
import com.example.androidebookapp.interfaces.OnClick;
import com.example.androidebookapp.item.BookSubjectList;
import com.example.androidebookapp.util.Method;
import com.google.android.material.textview.MaterialTextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BookSubjectAdapter extends RecyclerView.Adapter {

    private Method method;
    private Activity activity;
    private String type,ScreenType;
    private int columnWidth;
    private List<BookSubjectList> bookSubjectLists;
    //private List<BookSubCategoryList> bookSubjectListss;
     public  Intent intent=new Intent();
    public ArrayList<BookSubjectList> my_id_dataArrayList1;
    private OnClick onClick;
    private BookSubjectAdapter categoryAdapter;
    public BookSubCatAdapter categoryAdapterr;
    private Boolean isOver = false;
    private int paginationIndex = 1;
    private String adsParam = "1";
    public String CategoryN;

    private final int VIEW_TYPE_LOADING = 0;
    private final int VIEW_TYPE_ITEM = 1;
    private final int VIEW_TYPE_Ad = 2;
      public  int j;
    public BookSubjectAdapter(Activity activity, List<BookSubjectList> bookSubjectLists, String type,String subjecttype) {
        this.activity = activity;
        this.type = type;
        this.ScreenType = subjecttype;
        this.bookSubjectLists = bookSubjectLists;
        method = new Method(activity, onClick);
       // method.preferencess.setIntValue("json",j);
        Resources r = activity.getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, r.getDisplayMetrics());
        columnWidth = (int) ((method.getScreenWidth() - ((5 + 3) * padding)));
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(activity).inflate(R.layout.booksubject_item, parent, false);
            return new ViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View v = LayoutInflater.from(activity).inflate(R.layout.layout_loading_item, parent, false);
            return new ProgressViewHolder(v);
        } else if (viewType == VIEW_TYPE_Ad) {
            View view = LayoutInflater.from(activity).inflate(R.layout.adview_adapter, parent, false);
            return new AdOption(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {


        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {

            method = new Method(activity, onClick);

            final ViewHolder viewHolder = (ViewHolder) holder;


            viewHolder.title.setText(bookSubjectLists.get(position).getSubjTitle());

            viewHolder.Subtitle.setText(bookSubjectLists.get(position).getSubjSubTitle());
            Glide.with(activity).load(bookSubjectLists.get(position).getSubjImage())
                    .placeholder(R.drawable.round_bg)
                    .into(viewHolder.subjImage);

            //hideHeader();

            viewHolder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  /*  ((FragmentActivity)activity).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frameLayout_main, new bookschapterFragment(bookSubjectLists.get(position).getSubjectsId(),bookSubjectLists.get(position).getSubjTitle(),bookSubjectLists.get(position).getSubjLangType()))
                            .commit();
*/
                if(!Objects.equals(ScreenType, "QuizSubjectActivity")){
                     intent = new Intent((FragmentActivity)activity, BookViewActivity.class);
                }else{
                     intent = new Intent((FragmentActivity)activity, QuizChapterActivity.class);
                }


                    intent.putExtra("screenType","BookViewActivity");
                    intent.putExtra("cat_id",bookSubjectLists.get(position).getCid());
                    intent.putExtra("sub_cat_id", bookSubjectLists.get(position).getSubjSid());
                    intent.putExtra("book_subjId", bookSubjectLists.get(position).getSubjectsId());
                    intent.putExtra("Language", bookSubjectLists.get(position).getSubjLangType());
                    intent.putExtra("Language", bookSubjectLists.get(position).getSubjLangType());
                    intent.putExtra("topic", bookSubjectLists.get(position).getSubjTitle());
                    activity.startActivity(intent);
                }


            });

            //    viewHolder.constraintLayout.setLayoutParams(new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, columnWidth / 3));

       //     viewHolder.textView_title.setText(bookSubjectLists.get(position).getCategory_name());

            //category(viewHolder);
        //    Log.d("KINGSN item count", "onBindViewHolder: "+j+position);
        //    getMyId(activity,viewHolder,position);



          /*  String itemCount = activity.getResources().getString(R.string.items) + " " + "(" + bookSubjectLists.get(position).getTotal_books() + ")";
            viewHolder.textViewItem.setText(itemCount);

            Glide.with(activity).load(bookSubjectLists.get(position).getCat_image_thumb())
                    .placeholder(R.drawable.placeholder_portable)
                    .into(viewHolder.imageView);

            viewHolder.constraintLayout.setOnClickListener(v -> method.onClickAd(position, type, bookSubjectLists.get(position).getCid(), "", bookSubjectLists.get(position).getCategory_name(), "", "", bookSubjectLists.get(position).getSub_cat_status()));
*/
        } /*else if (holder.getItemViewType() == VIEW_TYPE_Ad) {
            AdOption adOption = (AdOption) holder;
            if (adOption.conAdView.getChildCount() == 0) {
                if (bookSubjectLists.get(position).getNative_ad_type().equals("admob")) {

                    @SuppressLint("InflateParams") View view = activity.getLayoutInflater().inflate(R.layout.admob_ad, null, true);

                    TemplateView templateView = view.findViewById(R.id.my_template);
                    if (templateView.getParent() != null) {
                        ((ViewGroup) templateView.getParent()).removeView(templateView); // <- fix
                    }
                    adOption.conAdView.addView(templateView);
                    AdLoader adLoader = new AdLoader.Builder(activity, bookSubjectLists.get(position).getNative_ad_id())
                            .forNativeAd(nativeAd -> {
                                NativeTemplateStyle styles = new
                                        NativeTemplateStyle.Builder()
                                        .build();

                                templateView.setStyles(styles);
                                templateView.setNativeAd(nativeAd);

                            })
                            .build();

                    AdRequest adRequest;
                    if (Method.personalizationAd) {
                        adRequest = new AdRequest.Builder()
                                .build();
                    } else {
                        Bundle extras = new Bundle();
                        extras.putString("npa", "1");
                        adRequest = new AdRequest.Builder()
                                .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                                .build();
                    }
                    adLoader.loadAd(adRequest);
                } else if (bookSubjectLists.get(position).getNative_ad_type().equals("facebook")) {
                    LayoutInflater inflater = LayoutInflater.from(activity);
                    LinearLayout adView = (LinearLayout) inflater.inflate(R.layout.native_ad_layout, adOption.conAdView, false);

                    NativeAd nativeAd = new NativeAd(activity, bookSubjectLists.get(position).getNative_ad_id());

                    // Add the AdOptionsView
                    LinearLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);

                    // Create native UI using the ad metadata.
                    MediaView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
                    TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
                    MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
                    TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
                    TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
                    TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
                    Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

                    adOption.conAdView.addView(adView);

                    NativeAdListener nativeAdListener = new NativeAdListener() {
                        @Override
                        public void onMediaDownloaded(Ad ad) {
                            // Native ad finished downloading all assets
                            Log.e("status_data", "Native ad finished downloading all assets.");
                        }

                        @Override
                        public void onError(Ad ad, AdError adError) {
                            // Native ad failed to load
                            Log.e("status_data", "Native ad failed to load: " + adError.getErrorMessage());
                        }

                        @Override
                        public void onAdLoaded(Ad ad) {
                            // Native ad is loaded and ready to be displayed
                            Log.d("status_data", "Native ad is loaded and ready to be displayed!");
                            // Race condition, load() called again before last ad was displayed
                            if (nativeAd != ad) {
                                return;
                            }
                            // Inflate Native Ad into Container
                            Log.d("status_data", "on load" + " " + ad.toString());

                            NativeAdLayout nativeAdLayout = new NativeAdLayout(activity);
                            AdOptionsView adOptionsView = new AdOptionsView(activity, nativeAd, nativeAdLayout);
                            adChoicesContainer.removeAllViews();
                            adChoicesContainer.addView(adOptionsView, 0);

                            // Set the Text.
                            nativeAdTitle.setText(nativeAd.getAdvertiserName());
                            nativeAdBody.setText(nativeAd.getAdBodyText());
                            nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
                            nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
                            nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
                            sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

                            // Create a list of clickable views
                            List<View> clickableViews = new ArrayList<>();
                            clickableViews.add(nativeAdTitle);
                            clickableViews.add(nativeAdCallToAction);

                            // Register the Title and CTA button to listen for clicks.
                            nativeAd.registerViewForInteraction(
                                    adOption.conAdView,
                                    nativeAdMedia,
                                    nativeAdIcon,
                                    clickableViews);
                        }

                        @Override
                        public void onAdClicked(Ad ad) {
                            // Native ad clicked
                            Log.d("status_data", "Native ad clicked!");
                        }

                        @Override
                        public void onLoggingImpression(Ad ad) {
                            // Native ad impression
                            Log.d("status_data", "Native ad impression logged!");
                        }
                    };

                    // Request an ad
                    nativeAd.loadAd(nativeAd.buildLoadAdConfig().withAdListener(nativeAdListener).build());
                } else if (bookSubjectLists.get(position).getNative_ad_type().equals("startapp")) {

                    LayoutInflater inflater = LayoutInflater.from(activity);
                    CardView adView = (CardView) inflater.inflate(R.layout.native_start_item, adOption.conAdView, false);

                    adOption.conAdView.addView(adView);

                    ImageView icon = adView.findViewById(R.id.icon);
                    TextView title = adView.findViewById(R.id.title);
                    TextView description = adView.findViewById(R.id.description);
                    Button button = adView.findViewById(R.id.button);

                    final StartAppNativeAd nativeAd = new StartAppNativeAd(activity);

                    nativeAd.loadAd(new NativeAdPreferences()
                            .setAdsNumber(1)
                            .setAutoBitmapDownload(true)
                            .setPrimaryImageSize(1), new AdEventListener() {
                        @Override
                        public void onReceiveAd(@NonNull com.startapp.sdk.adsbase.Ad ad) {
                            ArrayList<NativeAdDetails> ads = nativeAd.getNativeAds();    // get NativeAds list
                            NativeAdDetails nativeAdDetails = ads.get(0);
                            if (nativeAdDetails != null) {
                                icon.setImageBitmap(nativeAdDetails.getImageBitmap());
                                title.setText(nativeAdDetails.getTitle());
                                description.setText(nativeAdDetails.getDescription());
                                button.setText(nativeAdDetails.isApp() ? "Install" : "Open");

                                nativeAdDetails.registerViewForInteraction(adView);

                            }
                        }

                        @Override
                        public void onFailedToReceiveAd(@Nullable com.startapp.sdk.adsbase.Ad ad) {
                            if (BuildConfig.DEBUG) {
                                Log.e("onFailedToReceiveAd: ", "" + ad.getErrorMessage());
                            }
                        }
                    });
                } else if (bookSubjectLists.get(position).getNative_ad_type().equals("applovins")) {
                    LayoutInflater inflater = LayoutInflater.from(activity);
                    FrameLayout nativeAdLayout = (FrameLayout) inflater.inflate(R.layout.activity_native_max_template, adOption.conAdView, false);
                    MaxNativeAdLoader nativeAdLoader = new MaxNativeAdLoader(bookSubjectLists.get(position).getNative_ad_id(), activity);
                    nativeAdLoader.loadAd();
                    nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
                        @Override
                        public void onNativeAdLoaded(@Nullable MaxNativeAdView maxNativeAdView, MaxAd maxAd) {
                            super.onNativeAdLoaded(maxNativeAdView, maxAd);
                            // Add ad view to view.
                            nativeAdLayout.removeAllViews();
                            nativeAdLayout.addView(maxNativeAdView);
                            adOption.conAdView.addView(nativeAdLayout);
                        }

                        @Override
                        public void onNativeAdLoadFailed(String s, MaxError maxError) {
                            super.onNativeAdLoadFailed(s, maxError);
                        }

                        @Override
                        public void onNativeAdClicked(MaxAd maxAd) {
                            super.onNativeAdClicked(maxAd);
                        }
                    });
                }
            }
        }*/
    }

    @Override
    public int getItemCount() {
        return bookSubjectLists.size() + 1;
    }

    public static void hideHeader() {
        ProgressViewHolder.progressBar.setVisibility(View.GONE);
    }

    @Override
    public int getItemViewType(int position) {
        if (bookSubjectLists.size() == position) {
            return VIEW_TYPE_LOADING;
        } /*else if (bookSubjectLists.get(position).isIs_ads()) {
            return VIEW_TYPE_Ad;
        }*/ else {
            return VIEW_TYPE_ITEM;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView subjImage;
        private ConstraintLayout constraintLayout;
        private MaterialTextView title, Subtitle;
        private RecyclerView recyclerView_bookTab;
        private CardView cardview;

        public ViewHolder(View itemView) {
            super(itemView);

            subjImage = itemView.findViewById(R.id.subjImage);
            title = itemView.findViewById(R.id.Title);
            Subtitle = itemView.findViewById(R.id.Subtitle);
            cardview = itemView.findViewById(R.id.cardview);
           // recyclerView_bookTab = itemView.findViewById(R.id.recyclerView_bookTab);
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public static ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar_loading);
            progressBar.setVisibility(View.GONE);
        }
    }



    public class AdOption extends RecyclerView.ViewHolder {

        private ConstraintLayout conAdView;

        public AdOption(View itemView) {
            super(itemView);
            conAdView = itemView.findViewById(R.id.con_adView);
        }
    }






}
