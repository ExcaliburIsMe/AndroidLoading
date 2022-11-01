package cn.zcst.edu.loading;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private EditText et_password;
    private EditText et_phoneNumber;
    private TextView tv_change;
    private Button bt_sent;
    private CheckBox cb;
    private RadioButton rb_yanzhengma;
    private RadioButton rb_password;
    private ActivityResultLauncher<Intent> register;
    private String mPassword="111111";
    private String code;
    private SharedPreferences sharedPreferences;
    private Button bt_newRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //注册控件
        rb_password = findViewById(R.id.bt_mima);
        rb_yanzhengma = findViewById(R.id.bt_yanzhengma);
        tv_change = findViewById(R.id.tv_change);
        RadioGroup rg_state=findViewById(R.id.rg_state);
        bt_sent = findViewById(R.id.bt_sent);
        Button bt_login=findViewById(R.id.bt_login);
        cb = findViewById(R.id.cb_rememberPassword);
        et_phoneNumber = findViewById(R.id.et_phoneNumber);
        et_password = findViewById(R.id.et_password);
        bt_newRegister=findViewById(R.id.bt_register);

        //注册共享参数
        sharedPreferences =getSharedPreferences("remenberPassword", Context.MODE_PRIVATE);
        reload();

        //设置监听器
        rg_state.setOnCheckedChangeListener(this);
        bt_login.setOnClickListener(this);
        bt_sent.setOnClickListener(this);
        //新用户注册
        bt_newRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,LoginForgetActivity.class);
                startActivity(intent);
            }
        });

        //Activity返回值
        register = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent intent=result.getData();
                if(intent!=null && result.getResultCode()== Activity.RESULT_OK){
                    Bundle bundle=intent.getExtras();
                    mPassword=bundle.getString("newPassword");
                }

            }
        });


    }

    //记住密码实现功能
    private void reload() {
        String phone=sharedPreferences.getString("phone","");
        String password=sharedPreferences.getString("password","");

        et_phoneNumber.setText(phone);
        et_password.setText(password);

    }

    //功能：切换不同种登录方式 checkbox的功能
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.bt_mima:
                tv_change.setText(R.string.password);
                bt_sent.setText(R.string.forgetPassword);
                et_password.setHint(R.string.hintPassword);
                cb.setVisibility(View.VISIBLE);
                break;
            case  R.id.bt_yanzhengma:
                tv_change.setText(R.string.yanzhengma);
                bt_sent.setText(R.string.yanzhengmafasong);
                et_password.setHint(R.string.hintYanzhengma);
                cb.setVisibility(View.GONE);
                break;
        }
        
    }

    //按钮功能：忘记密码和发送验证吗按钮和登录按钮
    @Override
    public void onClick(View v) {
        //读取用户输入的手机号和密码
        String phone=et_phoneNumber.getText().toString();
        String password=et_password.getText().toString();
        //判断是不是手机号
        if(phone.length()<11)
        {
            Toast.makeText(this,"请输入正确的手机号",Toast.LENGTH_SHORT).show();
            return;
        }
        switch (v.getId()){
            //发送验证码和忘记密码按钮
            case R.id.bt_sent:
                if (rb_password.isChecked()){
                    Intent intent1=new Intent(this,LoginForgetActivity.class);
                    intent1.putExtra("phone",et_phoneNumber.getText().toString());
                    register.launch(intent1);


                }else if (rb_yanzhengma.isChecked()){
                    int i = new Random().nextInt(999999);
                    String verfideCode=String.format("%06d", i);
                    code=verfideCode;
                    AlertDialog.Builder builder=new AlertDialog.Builder(this);
                    builder.setTitle("验证码");
                    builder.setMessage("验证码："+verfideCode);
                    builder.setPositiveButton("好的",null);
                    AlertDialog alertDialog=builder.create();
                    alertDialog.show();
                }
                break;
            case R.id.bt_login:
                //登录按钮
                if(rb_password.isChecked()){
                    if(!mPassword.equals(et_password.getText().toString())){
                        Toast.makeText(this,R.string.ToastPassword,Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        //先判断是否勾选了记住密码
                        if (cb.isChecked()) {
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("password",password);
                            editor.putString("phone",phone);
                            editor.commit();
                        }
                        //页面跳转
                        Intent intent=new Intent(this,MainActivity2.class);
                        Bundle bundle=new Bundle();
                        bundle.putString("phoneNumber",phone);
                        bundle.putString("password",password);
                        intent.putExtras(bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                }
                if(rb_yanzhengma.isChecked()){
                    if(!code.equals(et_password.getText().toString())){
                        Toast.makeText(this,R.string.ToastVerifiCode,Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        Intent intent=new Intent(this,MainActivity2.class);
                        Bundle bundle=new Bundle();
                        bundle.putString("phoneNumber",et_phoneNumber.getText().toString());
                        bundle.putString("password",et_password.getText().toString());
                        intent.putExtras(bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
                break;

        }

    }
}