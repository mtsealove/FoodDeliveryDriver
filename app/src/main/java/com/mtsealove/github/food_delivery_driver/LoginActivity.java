package com.mtsealove.github.food_delivery_driver;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mtsealove.github.food_delivery_driver.Deisgn.SystemUiTuner;
import com.mtsealove.github.food_delivery_driver.Entity.Login;
import com.mtsealove.github.food_delivery_driver.Restful.RestAPI;
import com.mtsealove.github.food_delivery_driver.SQLite.AccountDbHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText idEt, pwEt;
    Button loginBtn;
    TextView titleTv;
    String tag = getClass().getSimpleName();
    public static Login login;

    CheckBox keepCb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SystemUiTuner tuner = new SystemUiTuner(this);
        tuner.setStatusBarWhite();

        idEt = findViewById(R.id.idEt);
        pwEt = findViewById(R.id.pwEt);
        loginBtn = findViewById(R.id.loginBtn);
        keepCb = findViewById(R.id.keepCb);
        titleTv = findViewById(R.id.titleTv);
        titleTv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(LoginActivity.this, IpActivity.class);
                startActivity(intent);
                return false;
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckInput();
            }
        });

        ReadDb();
    }

    private void CheckInput() {
        if (idEt.getText().toString().length() == 0) {
            Toast.makeText(this, "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
        } else if (pwEt.getText().toString().length() == 0) {
            Toast.makeText(this, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
        } else {
            Login();
        }
    }

    private void Login() {
        String id = idEt.getText().toString();
        String pw = pwEt.getText().toString();
        String token = FirebaseInstanceId.getInstance().getToken();

        RestAPI restAPI = new RestAPI(this);
        Call<Login> call = restAPI.getRetrofitService().PostLogin(id, pw, token);
        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if (response.isSuccessful()) {
                    if (response.body().getID() == null) {
                        Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 확인하세요", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        UpdateDb(keepCb.isChecked());
                        LoginActivity.login = response.body();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Log.e(tag, t.toString());
            }
        });
    }

    private void UpdateDb(boolean keep) {
        String ID = idEt.getText().toString();
        String pw = pwEt.getText().toString();
        AccountDbHelper helper = new AccountDbHelper(this, AccountDbHelper.table, null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        if (keep) {
            helper.InsertAccount(db, ID, pw);
        } else {
            helper.RemoveAccount(db);
        }
        db.close();
        helper.close();
    }

    private void ReadDb() {
        AccountDbHelper helper = new AccountDbHelper(this, AccountDbHelper.table, null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        String query = "select * from " + AccountDbHelper.table;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() != 0) {
            cursor.moveToNext();
            idEt.setText(cursor.getString(0));
            pwEt.setText(cursor.getString(1));
            keepCb.setChecked(true);
            loginBtn.performClick();
        }
        cursor.close();
        db.close();
        helper.close();
    }
}
