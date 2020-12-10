package com.example.startuppage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class timesheet_sick_frag extends DialogFragment {
    //Button add;
    String sickDayString;

    //EditText textInput;
    public timesheet_sick_frag() {
    }

    public static timesheet_sick_frag newInstance() {
        timesheet_sick_frag frag = new timesheet_sick_frag();
        return frag;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button addButton = (Button) view.findViewById(R.id.button);
        final EditText textInput = (EditText) view.findViewById(R.id.editTextDate);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sickDayString = textInput.getText().toString();
                if (sickDayString != null) {
                    Toast t = Toast.makeText(getContext(), "Adding sick day.." + sickDayString, Toast.LENGTH_SHORT);
                    t.show();
                    ((Timesheet) getActivity()).addSickDay(sickDayString);
                    getDialog().dismiss();
                } else {
                    Toast t = Toast.makeText(getContext(), "Empty. Canceling." + sickDayString, Toast.LENGTH_SHORT);
                    t.show();
                    getDialog().dismiss();
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.timesheet_sickday, container);
        return v;
    }
}




