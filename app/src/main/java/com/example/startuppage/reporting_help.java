package com.example.startuppage;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class reporting_help extends DialogFragment {
    public reporting_help() {
        // Empty constructor required for DialogFragment
    }

    public static reporting_help newInstance(String helpMessage) {
        reporting_help frag = new reporting_help();
        Bundle args = new Bundle();
        args.putString("helpMessage", helpMessage);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String helpMessage = getArguments().getString("helpMessage");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Reporting Help:");
        alertDialogBuilder.setMessage(helpMessage);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
//        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
////                if (dialog != null && dialog.isShowing()) {
////                    dialog.dismiss();
////                }
//            }
//
//        });

        return alertDialogBuilder.create();
    }
}



