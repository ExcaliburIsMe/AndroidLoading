package cn.zcst.edu.loading;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Random;

public class LoginForgetActivity extends AppCompatActivity implements View.OnClickListener {
    //定义变量
    private   EditText et_phoneNumber2,et_newPassword,et_confirmPassword,et_yanzhengma;
    private Button bt_confirm,bt_sent2;
    private String code2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_forget);

        //定义控件
        bt_confirm = findViewById(R.id.bt_confirm);
        et_phoneNumber2=findViewById(R.id.et_phoneNumber2);
        et_newPassword=findViewById(R.id.et_newPassword);
        et_confirmPassword=findViewById(R.id.et_confirmPassword);
        et_yanzhengma=findViewById(R.id.et_yanzhengma2);
        bt_sent2=findViewById(R.id.bt_sent2);

        //设置监听器
        bt_confirm.setOnClickListener(this);
        bt_sent2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(!(et_newPassword.getText().toString() ==et_confirmPassword.getText().toString())){
            Toast.makeText(this,R.string.equalPassword,Toast.LENGTH_SHORT).show();
           return;
        }

        switch (v.getId()){
            case R.id.bt_sent2:
                int i = new Random().nextInt(999999);
                String verfideCode=String.format("%06d", i);
                code2=verfideCode;
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("验证码");
                builder.setMessage("验证码："+verfideCode);
                builder.setPositiveButton("好的",null);
                AlertDialog alertDialog=builder.create();
                alertDialog.show();
                break;
            case R.id.bt_confirm:
                if(!(et_yanzhengma.getText().toString()==code2)){
                    Toast.makeText(this,R.string.equalYanzhengma,Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent=new Intent(this,MainActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("phoneNumber",et_phoneNumber2.getText().toString());
                bundle.putString("newPassword",et_newPassword.getText().toString());
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);
                finish();
                break;
        }
    }
}