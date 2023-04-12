package com.example.csci571_assignment9.Reservation;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.csci571_assignment9.R;
import com.example.csci571_assignment9.Search.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ReservationFormFragment extends DialogFragment {

    TextView tvName;
    EditText email;
    EditText date;
    EditText time;
    Button submit;
    Button cancel;

    String name;
    public ReservationFormFragment(String name) {
        this.name = name;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_reservation_form, container, false);

        tvName = v.findViewById(R.id.text_reservationForm_title);
        email = v.findViewById(R.id.input_reservationForm_email);
        date = v.findViewById(R.id.input_reservationForm_date);
        time = v.findViewById(R.id.input_reservationForm_time);
        submit = v.findViewById(R.id.button_reservationForm_submit);
        cancel = v.findViewById(R.id.button_reservationForm_cancel);

        tvName.setText(name);
        setDateInput();
        setTimeInput();
        setButtons();

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        assert dialog != null;

        int dialogWidth = 1000;
        int dialogHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(dialogWidth, dialogHeight);
        dialog.getWindow().setGravity(Gravity.CENTER);
    }

    private void setDateInput() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, day) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-d-yyyy", Locale.US);
            date.setText(dateFormat.format(calendar.getTime()));
        };
        date.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(requireActivity(),
                    dateSetListener,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            datePickerDialog.show();
        });
    }

    private void setTimeInput() {
        time.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();

            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            @SuppressLint("SetTextI18n")
            TimePickerDialog timePickerDialog = new TimePickerDialog(requireActivity(),
                    (view, h, min) -> time.setText(String.format(Locale.US, "%02d:%02d", h, min)), hour, minute, false);
            timePickerDialog.show();
        });
    }

    private void setButtons() {
        submit.setOnClickListener(view -> {
            if (checkEmail() && checkDate() && checkTime()) {
                MainActivity.createReservation(
                        name,
                        date.getText().toString(),
                        time.getText().toString(),
                        email.getText().toString());
                Toast toast = Toast.makeText(requireActivity(),
                        getResources().getString(R.string.toast_reservationForm_validInput),
                        Toast.LENGTH_SHORT);
                toast.show();
            }
            this.dismiss();
        });
        cancel.setOnClickListener(view -> this.dismiss());
    }

    private boolean checkEmail() {
        if (email.getText().toString().equals("")) {
            Toast toast = Toast.makeText(requireActivity(),
                    getResources().getString(R.string.toast_reservationForm_emptyEmail),
                    Toast.LENGTH_SHORT);
            toast.show();
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()) {
            Toast toast = Toast.makeText(requireActivity(),
                    getResources().getString(R.string.toast_reservationForm_invalidEmail),
                    Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }

    private boolean checkDate() {
        if (date.getText().toString().equals("")) {
            Toast toast = Toast.makeText(requireActivity(),
                    getResources().getString(R.string.toast_reservationForm_emptyDate),
                    Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }

    private boolean checkTime() {
        if (time.getText().toString().equals("")) {
            Toast toast = Toast.makeText(requireActivity(),
                    getResources().getString(R.string.toast_reservationForm_emptyTime),
                    Toast.LENGTH_SHORT);
            toast.show();
            return false;
        } else {
            int hour = Integer.parseInt(time.getText().toString().split(":")[0]);
            if (hour < 10 || hour > 16) {
                Toast toast = Toast.makeText(requireActivity(),
                        getResources().getString(R.string.toast_reservationForm_invalidTime),
                        Toast.LENGTH_SHORT);
                toast.show();
                return false;
            }
            return true;
        }
    }
}