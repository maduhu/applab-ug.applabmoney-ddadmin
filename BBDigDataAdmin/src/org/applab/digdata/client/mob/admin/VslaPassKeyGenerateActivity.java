package org.applab.digdata.client.mob.admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.applab.digdata.client.mob.admin.database.DatabaseHandler;
import org.applab.digdata.client.mob.admin.model.Vsla;

/**
 *
 */
public class VslaPassKeyGenerateActivity extends DashboardActivity {

    DatabaseHandler dbHandler;
    private Button acceptPassKeyButton;
    private Vsla vsla;
    private TextView passKey;

    /**
     * Called when the activity is first created
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vsla_pass_key);
        setHeader(true, false);


        try {
            dbHandler = new DatabaseHandler(getApplicationContext());
            vsla = (Vsla) getIntent().getExtras().get("vsla");

            // Importing all assets like buttons, text fields
            this.passKey = (TextView) findViewById(R.id.passKeyTxt);
            this.acceptPassKeyButton = (Button) findViewById(R.id.accept_passkey_button);

            if (null != generateVslaPasskey().toString().trim()) {
                passKey.setText(generateVslaPasskey().toString().trim());

                // add a click listener to the "Accept Passkey" button
                this.acceptPassKeyButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (null != vsla.getId()) {
                            vsla.setPasskey(String.valueOf(passKey.getText()).trim());
                            updateVslaPassKey(vsla);
                        }
                    }
                });
            }
        } catch (Exception ex) {
            Log.e("FindVsla vsla count", "" + ex.toString());
        }
    }

    private String generateVslaPasskey() {
        String newPassKey = PassKeyGenerator.randomAlphaNumeric(5);
        return newPassKey;
    }

    private void updateVslaPassKey(Vsla vsla) {
        boolean updateSuccess;
        try {
            updateSuccess = dbHandler.updateVsla(vsla);

            if (updateSuccess) {
                alertMessage();
                //showToast(VslaPassKeyGenerateActivity.this, "Passkey Accepted!!");
                /**Intent openFindVsla = new Intent(VslaPassKeyGenerateActivity.this,
                 FindVslaActivity.class);
                 startActivity(openFindVsla); */
            } else {
                showToast(VslaPassKeyGenerateActivity.this, "Passkey Not accepted!!");
            }
        } catch (Exception ex) {
            Log.e("UPDATEPASSKEY", ex.toString());
        }
    }

    public void alertMessage() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // Set title
        alertDialogBuilder.setTitle("Confirm");

        // Set dialog message
        alertDialogBuilder
                .setMessage("Passkey Updated!")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        // If this button is clicked, close current activity
                        Intent openDashboard = new Intent(VslaPassKeyGenerateActivity.this,
                                FindVslaActivity.class);
                        startActivity(openDashboard);
                    }
                });
        /** .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
         public void onClick(DialogInterface dialog, int id) {

         // If this button is clicked, just close the dialog box and do nothing
         dialog.cancel();
         }
         });  */

        // Create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // Show it
        alertDialog.show();
    }
}
