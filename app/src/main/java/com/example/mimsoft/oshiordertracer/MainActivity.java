package com.example.mimsoft.oshiordertracer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

    }

  public void submit(View v) {
      Intent i = new Intent(this, webservice.class);
      EditText order = (EditText) findViewById(R.id.order);
      EditText phone = (EditText) findViewById(R.id.phone);
      if (order.getText().toString().trim().equals("")) {

          order.setError("Order Number is required!");

      }  if (phone.getText().toString().trim().equals("")) {

          /**
           *   You can Toast a message here that the Username is Empty
           **/
          phone.setError("Phone Number is required!");
      }else{
          String theText = order.getText().toString();
          String theText1 = phone.getText().toString();
          i.putExtra("text_label", theText);
          i.putExtra("text_labe2", theText1);
          startActivity(i);
      }

  }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();

            //moveTaskToBack(false);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey() {

        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to exit application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("EXIT", true);
                        startActivity(intent);
                        finish();
//close();


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();

    }

}
