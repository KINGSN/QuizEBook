package com.example.androidebookapp.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.example.androidebookapp.R;
import com.example.androidebookapp.databinding.ActivitySettingsBinding;
import com.example.androidebookapp.interfaces.OnClick;
import com.example.androidebookapp.preferences.SharedPrefrence;
import com.example.androidebookapp.util.Method;

public class SettingActivity extends AppCompatActivity {
    ActivitySettingsBinding binding;
    public Method method;
    private Context mContext;
    private Dialog mCustomDialog;
    private SwitchCompat mSoundCheckBox, mVibrationCheckBox, mMusicCheckBox;
    private TextView ok_btn;
    private boolean isSoundOn;
    private boolean isVibrationOn;
    private boolean isMusicOn;
    RelativeLayout fontLayout;
    public OnClick onClick;
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    public static AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        binding = DataBindingUtil.setContentView(SettingActivity.this, R.layout.activity_settings);
        method = new Method(SettingActivity.this, onClick);

        mContext = SettingActivity.this;
        initViews();
        fontLayout = (RelativeLayout) findViewById(R.id.font_layout);
        fontLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                   // fontSizeDialog();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    private void initViews() {
        mSoundCheckBox = (SwitchCompat) findViewById(R.id.sound_checkbox);
        mVibrationCheckBox = (SwitchCompat) findViewById(R.id.vibration_checkbox);
        mMusicCheckBox = (SwitchCompat) findViewById(R.id.show_music_checkbox);
        ok_btn = (TextView) findViewById(R.id.ok);
        populateSoundContents();
        populateVibrationContents();
        populateMusicEnableContents();
        ok_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                overridePendingTransition(R.anim.open_next, R.anim.close_next);
                finish();
            }
        });
    }

    private void moreAppClicked() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("")));
        } catch (ActivityNotFoundException anfe) {
            startActivity(new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(String.format("https://play.google.com/store/apps/developer?id=%s&hl=en", ""))));
        }
    }

    private void switchSoundCheckbox() {
        isSoundOn = !isSoundOn;
        SharedPrefrence.setSoundEnableDisable(mContext, isSoundOn);
        populateSoundContents();
    }

    private void switchVibrationCheckbox() {
        isVibrationOn = !isVibrationOn;
        SharedPrefrence.setVibration(mContext, isVibrationOn);
        populateVibrationContents();
    }

    private void switchMusicEnableCheckbox() {
        isMusicOn = !isMusicOn;
        if (isMusicOn) {
            SharedPrefrence.setMusicEnableDisable(mContext, true);
            method.playSound();

        } else {
            SharedPrefrence.setMusicEnableDisable(mContext, false);
            method.StopSound();
        }
        populateMusicEnableContents();
    }

    protected void populateSoundContents() {
        if (SharedPrefrence.getSoundEnableDisable(mContext)) {
            mSoundCheckBox.setChecked(true);
        } else {
            mSoundCheckBox.setChecked(false);
        }
        isSoundOn = SharedPrefrence.getSoundEnableDisable(mContext);
    }

    protected void populateVibrationContents() {
        if (SharedPrefrence.getVibration(mContext)) {
            mVibrationCheckBox.setChecked(true);
        } else {
            mVibrationCheckBox.setChecked(false);
        }
        isVibrationOn = SharedPrefrence.getVibration(mContext);
    }

    protected void populateMusicEnableContents() {
        if (SharedPrefrence.getMusicEnableDisable(mContext)) {
            Method.playSound();
            mMusicCheckBox.setChecked(true);
        } else {
            Method.StopSound();
            mMusicCheckBox.setChecked(false);
        }
        isMusicOn = SharedPrefrence.getMusicEnableDisable(mContext);
    }

    private void updateClicked() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id="
                            + mContext.getPackageName())));
        } catch (ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id="
                            + mContext.getPackageName())));
        }
    }

    private void shareClicked(String subject, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + getPackageName());
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);

        startActivity(Intent.createChooser(intent,
                getString(R.string.share_via)));
    }

    public void viewClickHandler(View view) {
        switch (view.getId()) {
            case R.id.share_layout:
                shareClicked(getString(R.string.Let_me_recommend_you_this_application), method.mAppUrl);
                break;
            case R.id.sound_layout:
                switchSoundCheckbox();
                break;
            case R.id.sound_checkbox:
                switchSoundCheckbox();
                break;


            case R.id.vibration_layout:
                switchVibrationCheckbox();
                break;
            case R.id.vibration_checkbox:
                switchVibrationCheckbox();
                break;
            case R.id.show_hint_layout:
                switchMusicEnableCheckbox();
                break;
            case R.id.show_music_checkbox:
                String[] LOCATION_PERMS = {android.Manifest.permission.READ_PHONE_STATE
                };

                if (ContextCompat.checkSelfPermission(SettingActivity.this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SettingActivity.this, LOCATION_PERMS, 0);
                } else {

                    setTelephoneListener();

                }

                switchMusicEnableCheckbox();
                break;
            case R.id.moreapp_layout:
                moreAppClicked();
                overridePendingTransition(R.anim.open_next, R.anim.close_next);
                break;
            case R.id.rate_layout:
                updateClicked();
                overridePendingTransition(R.anim.open_next, R.anim.close_next);
                break;
            case R.id.ok:
                onBackPressed();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:

                setTelephoneListener();

                break;
        }
    }

    private void setTelephoneListener() {
        PhoneStateListener phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                if (state == TelephonyManager.CALL_STATE_RINGING) {
                    method.StopSound();
                } else if (state == TelephonyManager.CALL_STATE_IDLE) {
                } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                    method.StopSound();
                }
                super.onCallStateChanged(state, incomingNumber);
            }
        };

        TelephonyManager telephoneManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (telephoneManager != null) {
            telephoneManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        overridePendingTransition(R.anim.close_next, R.anim.open_next);
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {

        if (mContext != null) {
            if (mCustomDialog != null) {
                mCustomDialog.dismiss();
                mCustomDialog = null;
            }
            mVibrationCheckBox = null;
            mMusicCheckBox = null;
            mSoundCheckBox = null;
            mContext = null;
            super.onDestroy();
        }
    }

  /*  public void fontSizeDialog() {


        String changedFontSize;

        changedFontSize = SharedPrefrence.getSavedTextSize(getApplicationContext());

        final AlertDialog.Builder dialog = new AlertDialog.Builder(SettingActivity.this);
        dialog.setTitle("Change font Size");

        LayoutInflater inflater1 = (LayoutInflater) SettingActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater1.inflate(R.layout.dialog_font_size, null);
        dialog.setView(dialogView);

        alertDialog = dialog.create();
        Button btnOk = (Button) dialogView.findViewById(R.id.btnYes);
        final EditText edt_font_size_value = (EditText) dialogView.findViewById(R.id.edt_font_size_value);
        final SeekBar skBar_value = (SeekBar) dialogView.findViewById(R.id.skBar_value);

        skBar_value.setMax(14);
        skBar_value.setProgress(Integer.parseInt(changedFontSize) - 16);
        edt_font_size_value.setText(changedFontSize);
        edt_font_size_value.setSelection(edt_font_size_value.getText().toString().length());

        skBar_value.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                edt_font_size_value.setText(Integer.toString(progress + 16));
                edt_font_size_value.setSelection(edt_font_size_value.getText().toString().length());
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {

                System.out.println("----edit size  " + edt_font_size_value.getText().toString().trim());
                if (Integer.parseInt(edt_font_size_value.getText().toString().trim()) >= 30) {
                    edt_font_size_value.setText(Constant.TEXTSIZE_MAX);
                    SettingsPreferences.saveTextSize(getApplicationContext(), Constant.TEXTSIZE_MAX);

                } else if (Integer.parseInt(edt_font_size_value.getText().toString().trim()) < 16) {
                    edt_font_size_value.setText(Constant.TEXTSIZE_MIN);
                    SettingsPreferences.saveTextSize(getApplicationContext(), Constant.TEXTSIZE_MIN);

                } else {
                    SettingsPreferences.saveTextSize(getApplicationContext(), edt_font_size_value.getText().toString().trim());

                }

                FragmentPlay.ChangeTextSize(Integer.parseInt(SettingsPreferences.getSavedTextSize(getApplicationContext())));
            }

        });

        edt_font_size_value.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String currentProgress = edt_font_size_value.getText().toString().trim();
                if (!currentProgress.equals("")) {
                    skBar_value.setProgress(Integer.parseInt(currentProgress) - 16);
                    edt_font_size_value.setSelection(edt_font_size_value.getText().toString().length());
                }
            }
        });
        btnOk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }*/
}
