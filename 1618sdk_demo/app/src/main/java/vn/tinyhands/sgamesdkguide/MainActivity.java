package vn.tinyhands.sgamesdkguide;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import vn.tinyhands.sdk.SgameSDK;
import vn.tinyhands.sdk.data.local.PreferenceUtils;
import vn.tinyhands.sdk.data.model.UserInfo;
import vn.tinyhands.sdk.listener.LogoutListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.root_view).requestFocus();

        SgameSDK.init(this);

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SgameSDK.login(MainActivity.this);
            }
        });

        findViewById(R.id.btn_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SgameSDK.logout(new LogoutListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(MainActivity.this, "Logout Successful", Toast.LENGTH_SHORT).show();
                        displayUserConfig();
                    }
                });
            }
        });;

        findViewById(R.id.btn_dashboard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SgameSDK.openDashboard(MainActivity.this);
            }
        });

        findViewById(R.id.btn_payment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SgameSDK.openPayment(MainActivity.this);
            }
        });

        findViewById(R.id.btn_notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SgameSDK.notify(MainActivity.this,
                        "Title",
                        "Neque porro quisquam est qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit");
            }
        });

        findViewById(R.id.btn_fab_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SgameSDK.showFloatingButton(MainActivity.this);
            }
        });

        findViewById(R.id.btn_fab_hide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SgameSDK.hideFloatingButton();
            }
        });

        findViewById(R.id.btn_save_user_config).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserConfig();
                Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btn_share_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.youtube.com/watch?v=1saWEINsBgo";
                SgameSDK.shareLink(MainActivity.this, url);
            }
        });

        findViewById(R.id.btn_share_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.test);
                SgameSDK.sharePhoto(MainActivity.this, image);
            }
        });

        displayUserConfig();
    }

    private void displayUserConfig() {
        PreferenceUtils pref = SgameSDK.getInstance().getPref();
        ((TextView)findViewById(R.id.tv_access_token)).setText("Access Token : " + pref.getString(PreferenceUtils.KEY_ACCESS_TOKEN, null));
        ((EditText) findViewById(R.id.et_server_id)).setText(pref.getString(PreferenceUtils.SERVER_ID));
        ((EditText) findViewById(R.id.et_character_id)).setText(pref.getString(PreferenceUtils.CHARACTER_ID));
        ((EditText) findViewById(R.id.et_character_name)).setText(pref.getString(PreferenceUtils.CHARACTER_NAME));
        ((EditText) findViewById(R.id.et_character_level)).setText(pref.getString(PreferenceUtils.CHARACTER_LEVEL));
    }

    private void saveUserConfig() {
        String serverId = ((EditText) findViewById(R.id.et_server_id)).getText().toString().trim();
        String characterId = ((EditText) findViewById(R.id.et_character_id)).getText().toString().trim();
        String characterName = ((EditText) findViewById(R.id.et_character_name)).getText().toString().trim();
        String characterLevel = ((EditText) findViewById(R.id.et_character_level)).getText().toString().trim();
        SgameSDK.setUserConfig(serverId, characterId, characterName, characterLevel);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SgameSDK.REQUEST_CODE_LOGIN && resultCode == RESULT_OK) {
            String userInfoData = data.getStringExtra("user_info");
            UserInfo userInfo = new Gson().fromJson(userInfoData, UserInfo.class);
            Toast.makeText(this, "Login Successful:"
                    + ", userId=" + userInfo.getUserId()
                    + ", accessToken=" + userInfo.getAccessToken(), Toast.LENGTH_SHORT).show();
            displayUserConfig();
        }

        if (requestCode == SgameSDK.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK) {
            Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
        }
    }
}
