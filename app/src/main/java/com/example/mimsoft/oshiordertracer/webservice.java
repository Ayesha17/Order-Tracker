package com.example.mimsoft.oshiordertracer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;



public class webservice extends Activity {
   // public static final String LOG_TAG = MainActivity.class.getSimpleName();


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webservice);


        Intent i = getIntent();
        String   order = i.getStringExtra("text_label");
        String   phone = i.getStringExtra("text_labe2");
        TextView ins = (TextView) findViewById(R.id.ins);
      //  ins.setText(uriString);
                // WebServer Request URL
                String serverURL = "http://www.oshi.pk/app_services/track_my_order/"+order+"/"+phone;

                // Use AsyncTask execute Method To Prevent ANR Problem
                new LongOperation().execute(serverURL);

    }



    // Class with extends AsyncTask class
    private class LongOperation  extends AsyncTask<String, Void, Void> {

        // Required initialization
        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(webservice.this);
        String data = "";
        TextView address = (TextView) findViewById(R.id.address);
        TextView name = (TextView) findViewById(R.id.name);
        TextView email = (TextView) findViewById(R.id.email);
        TextView phone = (TextView) findViewById(R.id.phone);
        TextView orderdate = (TextView) findViewById(R.id.order_date);
        TextView orderstatus = (TextView) findViewById(R.id.order_status);
        TextView shipaddr = (TextView) findViewById(R.id.shipadd);
        TextView summary = (TextView) findViewById(R.id.summary);

        //  TextView jsonParsed = (TextView) findViewById(R.id.jsonParsed);
        int sizeData = 0;
        // EditText serverText = (EditText) findViewById(R.id.serverText);


        protected void onPreExecute() {
            // NOTE: You can call UI Element here.

            //Start Progress Dialog (Message)

            Dialog.setMessage("Please wait..");
            Dialog.show();

//            try{
//                // Set Request parameter
//         //       data +="&" + URLEncoder.encode("data", "UTF-8") + "="+serverText.getText();
//
//            } catch (UnsupportedEncodingException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }

        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {

            /************ Make Post Call To Web Server ***********/
            BufferedReader reader = null;

            // Send data
            try {

                // Defined URL  where to send data
                URL url = new URL(urls[0]);

                // Send POST data request

                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();

                // Get the server response

                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    // Append server response in string
                    sb.append(line + "\n");
                }

                // Append Server Response To Content String
                Content = sb.toString();
            } catch (Exception ex) {
                Error = ex.getMessage();
            } finally {
                try {

                    reader.close();
                } catch (Exception ex) {
                }
            }

            /*****************************************************/
            return null;
        }

        protected void onPostExecute(Void unused) {
            // NOTE: You can call UI Element here.

            // Close progress dialog
            Dialog.dismiss();

//            if (Error != null) {
//
//                uiUpdate.setText("Output : "+Error);
//
//            } else {
//
//                // Show Response Json On Screen (activity)
//                uiUpdate.setText( Content );
//
//                /****************** Start Parse Response JSON Data *************/

            String OutputData = "";
            JSONObject jsonResponse;

            try {

                /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
                jsonResponse = new JSONObject(Content);

                /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
                /*******  Returns null otherwise.  *******/
                JSONArray jsonMainNode = jsonResponse.optJSONArray("result");
if (jsonMainNode.length() > 0) {

                    /*********** Process each JSON Node ************/

                int lengthJsonArr = jsonMainNode.length();
                String name1 = null;
                String number = null;
                String date_added = null;
                String contact = null;
                String email1 = null;
                String status = null;
                String shipad = null;
                String summ = null;
                for (int i = 0; i < lengthJsonArr; i++) {
                    /****** Get Object for each JSON node.***********/
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                        /******* Fetch node values **********/
                        name1 = jsonChildNode.optString("user_name").toString();
                        number = jsonChildNode.optString("city_name").toString();
                        email1 = jsonChildNode.optString("email").toString();
                        contact = jsonChildNode.optString("phone").toString();
                        date_added = jsonChildNode.optString("created_at").toString();
                        shipad = jsonChildNode.optString("ship_address").toString();
                        summ = jsonChildNode.optString("item_summary").toString();
                        status = jsonChildNode.optString("order_status").toString();
                        //   status=jsonChildNode.optString("").toString();
                        //Log.i("JSON parse", song_name);
                    }
                    /****************** End Parse Response JSON Data *************/
//                    OutputData += " Name 		    : "+ name1 +" \n "
//                            + "Number 		: "+ number +" \n "
//                            + "Time 				: "+ date_added +" \n "
//                            +"--------------------------------------------------\n";

                    //Show Parsed Output on screen (activity)
                    name.setText(name1);
                    address.setText(number);
                    email.setText(email1);
                    phone.setText(contact);
                    orderdate.setText(date_added);
                    shipaddr.setText(shipad);
                    summary.setText(summ);
                    switch (status) {
                        case "1":
                            orderstatus.setText("On Hold");
                            break;
                        case "5":
                            orderstatus.setText("Delivered");
                            break;
                        case "2":
                            orderstatus.setText("Pending");
                            break;
                        case "3":
                            orderstatus.setText("Confirmed");
                            break;
                        case "13":
                            orderstatus.setText("Khi in Process");
                            break;
                        case "6":
                            orderstatus.setText("Not answering calls");
                            break;
                        case "7":
                            orderstatus.setText("Reviewed");
                            break;
                        case "8":
                            orderstatus.setText("Cancelled");
                            break;
                        case "9":
                            orderstatus.setText("Refused/Returned");
                            break;
                        case "10":
                            orderstatus.setText("Shipped");
                            break;
                        case "11":
                            orderstatus.setText("Merged Order");
                            break;
                        case "12":
                            orderstatus.setText("Duplicate Order");
                            break;
                        default:
                            orderstatus.setText("no");
                    }



            }
        } catch (JSONException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (!isFinishing()) {
                            new AlertDialog.Builder(webservice.this)
                                    .setTitle("Error")
                                    .setMessage("You enter wrong order and phone number")
                                    .setCancelable(false)
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {

                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);// Whatever...
                                            }
                                    }).create().show();
                        }
                    }
                });

                           // Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
            }  }
        }
    public void back(View v){
        Intent i=new Intent(this, MainActivity.class);
        startActivity(i);
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


