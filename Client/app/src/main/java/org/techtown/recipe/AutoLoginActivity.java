package org.techtown.recipe;

import android.app.AppComponentFactory;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AutoLoginActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //단말에 저장된 token 받아오기
        preferences = getSharedPreferences("UserToken", MODE_PRIVATE);
        String accessToken = preferences.getString("accessToken", "");
        String refreshToken = preferences.getString("refreshToken", "");

        //헤더에 토큰 넣기
        HashMap<String, String> headers = new HashMap<>();
        headers.put("accessToken", accessToken);

        if (accessToken == "" && refreshToken == "") {
            Intent intent1 = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent1);
            finish();
        } else {

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //자동 로그인 됐을 때
                    Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent1);
                    finish();
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse.statusCode == 419) {
                        //1. access token과 일치하는 사용자가 있지만 만료기간이 지난 경우
                        Log.d("statuscode", "" + networkResponse.statusCode);

                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);

                                    //access token 재발급 성공
                                    String newAccessToken = jsonObject.getString("newAccessToken");
                                    preferences = getSharedPreferences("UserToken", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("accessToken", newAccessToken);
                                    editor.commit();

                                    //자동 로그인 됐을 때
                                    Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent1);
                                    finish();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        Response.ErrorListener errorListener = new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (networkResponse.statusCode == 411||networkResponse.statusCode == 412) {
                                    //1. refresh token, access token과 일치하는 사용자가 없는 경우 411
                                    //2. refresh token이 올바른 토큰 형태가 아닌 경우 412
                                    Log.d("statuscode", "" + networkResponse.statusCode);
                                    Toast.makeText( getApplicationContext(), "저장된 회원정보에 오류가 발생하였습니다. 다시 로그인 해주세요.", Toast.LENGTH_SHORT ).show();

                                }
                                else{
                                    //서버 잘못되었을 때
                                    Log.d("statuscode", "" + networkResponse.statusCode);
                                    Toast.makeText( getApplicationContext(), "서버에 오류가 발생하였습니다.", Toast.LENGTH_SHORT ).show();
                                }
                                Intent intent2 = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent2);
                                finish();
                            }
                        };
                        //헤더에 토큰 넣기
                        HashMap<String, String> headers = new HashMap<>();
                        headers.put("accessToken", accessToken);
                        headers.put("refreshToken", refreshToken);

                        //서버로 Volley를 이용해서 요청
                        TokenReissueRequest tokenReissueRequest = new TokenReissueRequest(headers, responseListener, errorListener);
                        RequestQueue queue1 = Volley.newRequestQueue(AutoLoginActivity.this);
                        queue1.add(tokenReissueRequest);
                    }
                    else if (networkResponse.statusCode == 401||networkResponse.statusCode == 402) {
                        //2. access token과 일치하는 사용자가 없는 경우 401
                        //3. 유효하지 않은 토큰 형태인 경우 402
                        Log.d("statuscode", "" + networkResponse.statusCode);
                        Toast.makeText( getApplicationContext(), "저장된 회원정보에 오류가 발생하였습니다. 다시 로그인 해주세요.", Toast.LENGTH_SHORT ).show();

                        Intent intent2 = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent2);
                        finish();
                    }
                    else{
                        //서버 잘못되었을 때
                        Log.d("statuscode", "" + networkResponse.statusCode);
                        Toast.makeText( getApplicationContext(), "서버에 오류가 발생하였습니다.", Toast.LENGTH_SHORT ).show();

                        Intent intent2 = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent2);
                        finish();
                    }

                }
            };
            TokenValidateRequest tokenValidateRequest = new TokenValidateRequest(headers, responseListener, errorListener);
            RequestQueue queue = Volley.newRequestQueue(AutoLoginActivity.this);
            queue.add(tokenValidateRequest);
        }
    }
}