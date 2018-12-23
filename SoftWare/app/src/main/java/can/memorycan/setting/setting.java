package can.memorycan.setting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import can.memorycan.R;

public class setting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageButton ig8;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ig8=findViewById(R.id.imageButton8);
        ig8.setOnClickListener(new tomain());
        Button b1=findViewById(R.id.button);
        b1.setOnClickListener(new clickToast());
    }
    private class tomain implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(setting.this,can.main_delete.MainActivity.class);
            startActivity(intent);
        }
    }
    private class clickToast implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Toast.makeText(setting.this,"提交成功", Toast.LENGTH_SHORT).show();
        }
    }
}
