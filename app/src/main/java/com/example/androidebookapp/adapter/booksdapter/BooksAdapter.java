package com.example.androidebookapp.adapter.booksdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidebookapp.R;
import com.example.androidebookapp.activity.Login;
import com.example.androidebookapp.activity.PDFShow;
import com.example.androidebookapp.databinding.BookViewItemBinding;
import com.example.androidebookapp.interfaces.FavouriteIF;
import com.example.androidebookapp.interfaces.OnClick;
import com.example.androidebookapp.item.BookitemList;
import com.example.androidebookapp.response.DataRP;
import com.example.androidebookapp.response.GetReportRP;
import com.example.androidebookapp.rest.ApiClient;
import com.example.androidebookapp.rest.ApiInterface;
import com.example.androidebookapp.util.API;
import com.example.androidebookapp.util.DownloadEpub;
import com.example.androidebookapp.util.Method;
import com.github.ornolfr.ratingview.RatingView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BooksAdapter extends RecyclerView.Adapter {

    public BookViewItemBinding binding;
    LayoutInflater layoutInflater;
    private Method method;
    private Activity activity;
    private String type;
    private int columnWidth;
    public List<BookitemList> bookSubjectLists;
    //private List<BookSubCategoryList> bookSubjectListss;
    private Animation myAnim;
    public BookitemList bookSubjectLists1;
    private OnClick onClick;
    private BooksAdapter categoryAdapter;
    public BookSubCatAdapter categoryAdapterr;
    private Boolean isOver = false;
    private int paginationIndex = 1;
    private String adsParam = "1";
    public String CategoryN;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private InputMethodManager imm;
    private final int VIEW_TYPE_LOADING = 0;
    private final int VIEW_TYPE_ITEM = 1;
    private final int VIEW_TYPE_Ad = 2;
      public  int j;
    public BooksAdapter(Activity activity, List<BookitemList> bookSubjectLists, String type) {
        this.activity = activity;
        this.type = type;
        this.bookSubjectLists = bookSubjectLists;
        method = new Method(activity, onClick);
       // method.preferencess.setIntValue("json",j);
        Resources r = activity.getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, r.getDisplayMetrics());
        //GlobalBus.getBus().register(this);
        imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        columnWidth = (int) ((method.getScreenWidth() - ((5 + 3) * padding)));
        myAnim = AnimationUtils.loadAnimation(activity, R.anim.bounce);
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            if (layoutInflater == null) {
                layoutInflater = LayoutInflater.from(parent.getContext());
            }
            View view = LayoutInflater.from(activity).inflate(R.layout.book_view_item, parent, false);
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
     /*   my_id_dataArrayList1  = bookSubjectLists.get(position);
        Log.d("KINGSNIN", "onBindViewHolder: "+ my_id_dataArrayList1.toString());
*/
        progressDialog = new ProgressDialog(activity);

        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {

            method = new Method(activity, onClick);
            bookSubjectLists1=bookSubjectLists.get(position);

            final ViewHolder viewHolder = (ViewHolder) holder;


            viewHolder.textView_title.setText(bookSubjectLists.get(position).getBookTitle());
            viewHolder.textView_view_bookListView_adapter.setText(bookSubjectLists.get(position).getBookViews());

         //   viewHolder.Subtitle.setText(bookSubjectLists.get(position).getSubjSubTitle());
            if(!bookSubjectLists.get(position).getBookCoverImg().equals("")){
                Glide.with(activity).load(bookSubjectLists.get(position).getBookCoverImg())
                        .placeholder(R.drawable.round_bg)
                        .into((ImageView) viewHolder.imageView);
            }else{
                viewHolder.imageView.setVisibility(View.GONE);
               // viewHolder.constraintw.setMargins(mImageView, 50, 50, 50, 50);
                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 0, 0);
            }


            viewHolder.textViewRatingCount.setText(bookSubjectLists.get(position).getTotalRate());
            viewHolder.ratingBar.setRating(Float.parseFloat(bookSubjectLists.get(position).getRateAvg()));

            if (bookSubjectLists.get(position).getIs_fav().equals("true")) {
                viewHolder.image_favorite_bookDetail.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_fav_hov));
            } else {
                if (method.isDarkMode()) {
                    viewHolder.image_favorite_bookDetail.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_unfav_white));
                } else {
                    viewHolder.image_favorite_bookDetail.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_unfav));
                }
            }

            viewHolder.con_read_bookDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewHolder.itemView.findViewById(R.id.imageView_read_bookDetail).startAnimation(myAnim);
                    if (method.isNetworkAvailable()) {
                        bookSubjectLists1=bookSubjectLists.get(position);
                       continueBook(bookSubjectLists.get(position).getId());
                    } else {
                        method.alertBox(activity.getResources().getString(R.string.internet_connection));
                    }
                }
            });

            viewHolder.con_report_bookDetail.setOnClickListener(v -> {
                bookSubjectLists1=bookSubjectLists.get(position);
                viewHolder.imageView_report_bookDetail.startAnimation(myAnim);
                if (method.isLogin()) {
                    getReport(method.userId(), bookSubjectLists1.getId());
                } else {
                    Method.loginBack = true;
                    activity.startActivity(new Intent(activity, Login.class));
                }
            });

            viewHolder.con_download_bookDetail.setOnClickListener(view -> {
                bookSubjectLists1=bookSubjectLists.get(position);
                viewHolder.imageViewDownload.startAnimation(myAnim);
                if (method.isNetworkAvailable()) {
                    if (bookSubjectLists1.getBookFileUrl().contains(".epub")) {
                        method.download(bookSubjectLists1.getId(),
                                bookSubjectLists1.getBookTitle(),
                                bookSubjectLists1.getBookCoverImg(),
                                bookSubjectLists1.getBookSubjId(),
                                bookSubjectLists1.getBookFileUrl(), "epub");
                    } else {
                        method.download(bookSubjectLists1.getId(),
                                bookSubjectLists1.getBookTitle(),
                                bookSubjectLists1.getBookCoverImg(),
                                bookSubjectLists1.getBookSubjId(),
                                bookSubjectLists1.getBookFileUrl(), "pdf");
                    }
                } else {
                    method.alertBox(activity.getResources().getString(R.string.internet_connection));
                }
            });

            viewHolder.con_favorite_bookDetail.setOnClickListener(v -> {
                bookSubjectLists1=bookSubjectLists.get(position);
                viewHolder.image_favorite_bookDetail.startAnimation(myAnim);
                if (method.isLogin()) {
                    FavouriteIF favouriteIF = (isFavourite, message) -> {
                        if (!isFavourite.equals("")) {
                            if (isFavourite.equals("true")) {
                                viewHolder.image_favorite_bookDetail.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_fav_hov));
                            } else {
                                if (method.isDarkMode()) {
                                    viewHolder.image_favorite_bookDetail.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_unfav_white));
                                } else {
                                    viewHolder.image_favorite_bookDetail.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_unfav));
                                }
                            }
                        }
                    };
                    method.addToFav(bookSubjectLists1.getId(), method.userId(), favouriteIF);
                } else {
                    Method.loginBack = true;
                    activity.startActivity(new Intent(activity, Login.class));
                }
            });


         /*
            viewHolder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((FragmentActivity)activity).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frameLayout_main, new bookschapterFragment(bookSubjectLists.get(position).getSubjectsId(),bookSubjectLists.get(position).getSubjTitle(),bookSubjectLists.get(position).getSubjLangType()))
                            .commit();
                }
            });*/

            //    viewHolder.constraintLayout.setLayoutParams(new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, columnWidth / 3));

       //     viewHolder.textView_title.setText(bookSubjectLists.get(position).getCategory_name());

            //category(viewHolder);
        //    Log.d("KINGSN item count", "onBindViewHolder: "+j+position);
        //    getMyId(activity,viewHolder,position);



          /*  String itemCount = activity.activity.getResources().getString(R.string.items) + " " + "(" + bookSubjectLists.get(position).getTotal_books() + ")";
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

    public void hideHeader() {
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
      public   ConstraintLayout constraintw,con_favorite_bookDetail,con_read_bookDetail,con_download_bookDetail,con_report_bookDetail;
        private ImageView imageView,imageView_report_bookDetail,imageViewDownload,image_favorite_bookDetail;
        private RatingView ratingBar;
        private ConstraintLayout constraintLayout1;
        private MaterialTextView textViewRating,textView_view_bookListView_adapter,textView_title, textView_desc, textViewRatingCount, textViewViewCount, textViewDescription;
        private CardView cardView_imageView;
        public ViewHolder(View itemView) {
            super(itemView);

            constraintLayout1 = itemView.findViewById(R.id.con_bookListView_adapter);
            textView_view_bookListView_adapter = itemView.findViewById(R.id.textView_view_bookListView_adapter);
            con_favorite_bookDetail = itemView.findViewById(R.id.con_favorite_bookDetail);
            con_download_bookDetail = itemView.findViewById(R.id.con_download_bookDetail);
            con_read_bookDetail = itemView.findViewById(R.id.con_read_bookDetail);
            con_report_bookDetail = itemView.findViewById(R.id.con_report_bookDetail);
            con_download_bookDetail = itemView.findViewById(R.id.con_download_bookDetail);
            imageViewDownload= itemView.findViewById(R.id.imageView_download_bookDetail);
            con_favorite_bookDetail= itemView.findViewById(R.id.con_favorite_bookDetail);
            image_favorite_bookDetail= itemView.findViewById(R.id.image_favorite_bookDetail);
            imageView = itemView.findViewById(R.id.imageViewBookCover);
            textView_title = itemView.findViewById(R.id.textView_title);
            imageView_report_bookDetail = itemView.findViewById(R.id.imageView_report_bookDetail);
            textView_desc = itemView.findViewById(R.id.textView_desc);
            ratingBar = itemView.findViewById(R.id.ratingBar_bookListView_adapter);
            textViewRatingCount = itemView.findViewById(R.id.textView_ratingCount_bookListView_adapter);
            textViewViewCount = itemView.findViewById(R.id.textView_view_bookListView_adapter);
            textViewDescription = itemView.findViewById(R.id.textView_description_bookListView_adapter);
            cardView_imageView = itemView.findViewById(R.id.cardView_imageView);
            constraintw = itemView.findViewById(R.id.constraintw);

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



    private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    public void continueBook(String bookId) {

        if (method.isLogin()) {

            progressDialog.show();
            progressDialog.setMessage(activity.getResources().getString(R.string.loading));
            progressDialog.setCancelable(false);

            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(activity));
            jsObj.addProperty("user_id", method.userId());
            jsObj.addProperty("book_id", bookSubjectLists1.getId());
            jsObj.addProperty("method_name", "user_continue_book");
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<DataRP> call = apiService.submitContinueReading(API.toBase64(jsObj.toString()));
            call.enqueue(new Callback<DataRP>() {
                @Override
                public void onResponse(@NotNull Call<DataRP> call, @NotNull Response<DataRP> response) {

                    try {
                        DataRP dataRP = response.body();
                        assert dataRP != null;

                        if (dataRP.getStatus().equals("1")) {

                            if (dataRP.getSuccess().equals("1")) {
                                openBook();
                            } else {
                                method.alertBox(dataRP.getMsg());
                            }

                        } else if (dataRP.getStatus().equals("2")) {
                            method.suspend(dataRP.getMessage());
                        } else {
                            method.alertBox(dataRP.getMessage());
                        }

                    } catch (Exception e) {
                        Log.d("exception_error", e.toString());
                        method.alertBox(activity.getResources().getString(R.string.failed_try_again));
                    }

                    progressDialog.dismiss();

                }

                @Override
                public void onFailure(@NotNull Call<DataRP> call, @NotNull Throwable t) {
                    // Log error here since request failed
                    Log.e("fail", t.toString());
                    progressDialog.dismiss();
                    method.alertBox(activity.getResources().getString(R.string.failed_try_again));
                }
            });

        } else {
            openBook();
        }

    }



    //send book report
    public void submitReport(String userId, String bookId, String report) {

        progressDialog.show();
        progressDialog.setMessage(activity.getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(activity));
        jsObj.addProperty("user_id", userId);
        jsObj.addProperty("book_id", bookId);
        jsObj.addProperty("report", report);
        jsObj.addProperty("method_name", "book_report");
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<DataRP> call = apiService.submitBookReport(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<DataRP>() {
            @Override
            public void onResponse(@NotNull Call<DataRP> call, @NotNull Response<DataRP> response) {

                try {
                    DataRP dataRP = response.body();
                    assert dataRP != null;

                    if (dataRP.getStatus().equals("1")) {
                        method.alertBox(dataRP.getMsg());
                    } else if (dataRP.getStatus().equals("2")) {
                        method.suspend(dataRP.getMessage());
                    } else {
                        method.alertBox(dataRP.getMessage());
                    }

                } catch (Exception e) {
                    method.alertBox(activity.getResources().getString(R.string.failed_try_again));
                    Log.d("exception_error", e.toString());
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(@NotNull Call<DataRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e("fail", t.toString());
                progressDialog.dismiss();
                method.alertBox(activity.getResources().getString(R.string.failed_try_again));
            }
        });
    }

    private void openBook() {
        Log.d("KINGSN OP", "openBook: "+bookSubjectLists1.getBookCoverImg());
        if (bookSubjectLists1.getBookFileUrl().contains(".epub")) {
            DownloadEpub downloadEpub = new DownloadEpub(activity);
            downloadEpub.pathEpub(bookSubjectLists1.getBookFileUrl(), bookSubjectLists1.getId());
        } else {
            activity.startActivity(new Intent(activity, PDFShow.class)
                    .putExtra("id", bookSubjectLists1.getId())
                    .putExtra("link", bookSubjectLists1.getBookFileUrl())
                    .putExtra("toolbarTitle", bookSubjectLists1.getBookTitle())
                    .putExtra("type", "link"));
        }
    }

    //get book report
    public void getReport(String userId, String bookId) {

        progressDialog.show();
        progressDialog.setMessage(activity.getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(activity));
        jsObj.addProperty("user_id", userId);
        jsObj.addProperty("book_id", bookId);
        jsObj.addProperty("method_name", "get_report");
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<GetReportRP> call = apiService.getBookReport(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<GetReportRP>() {
            @Override
            public void onResponse(@NotNull Call<GetReportRP> call, @NotNull Response<GetReportRP> response) {

                try {
                    GetReportRP getReportRP = response.body();
                    assert getReportRP != null;
                    if (getReportRP.getStatus().equals("1")) {

                        final Dialog dialog = new Dialog(activity);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.report_dialog);
                        if (method.isRtl()) {
                            dialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                        }
                        dialog.setCancelable(false);
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        dialog.getWindow().setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

                        TextInputEditText editText = dialog.findViewById(R.id.editText_report_dialog);
                        MaterialButton buttonSubmit = dialog.findViewById(R.id.button_submit_review_dialog);
                        MaterialButton buttonClose = dialog.findViewById(R.id.button_close_review_dialog);

                        editText.setText(getReportRP.getReport());

                        buttonSubmit.setOnClickListener(v -> {

                            editText.setError(null);
                            String stringReport = editText.getText().toString();

                            if (stringReport.equals("") || stringReport.isEmpty()) {
                                editText.requestFocus();
                                editText.setError(activity.getResources().getString(R.string.please_enter_comment));
                            } else {
                                editText.clearFocus();
                                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                                submitReport(userId, bookId, stringReport);
                                dialog.dismiss();
                            }

                        });

                        buttonClose.setOnClickListener(v -> {
                            dialog.dismiss();
                        });

                        dialog.show();

                    } else if (getReportRP.getStatus().equals("2")) {
                        method.suspend(getReportRP.getMessage());
                    } else {
                        method.alertBox(getReportRP.getMessage());
                    }

                } catch (Exception e) {
                    method.alertBox(activity.getResources().getString(R.string.failed_try_again));
                    Log.d("exception_error", e.toString());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NotNull Call<GetReportRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e("fail", t.toString());
                progressDialog.dismiss();
                method.alertBox(activity.getResources().getString(R.string.failed_try_again));
            }
        });
    }

}
