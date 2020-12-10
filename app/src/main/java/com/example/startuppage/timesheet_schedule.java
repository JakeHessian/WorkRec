package com.example.startuppage;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class timesheet_schedule extends DialogFragment {
    public timesheet_schedule() {
        // Empty constructor required for DialogFragment
    }

    public static timesheet_schedule newInstance(String sch) {
        timesheet_schedule frag = new timesheet_schedule();
        Bundle args = new Bundle();
        args.putString("SCH", sch);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String schedule = getArguments().getString("SCH");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Employee Schedule:");
        alertDialogBuilder.setMessage(schedule);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return alertDialogBuilder.create();
    }
}
