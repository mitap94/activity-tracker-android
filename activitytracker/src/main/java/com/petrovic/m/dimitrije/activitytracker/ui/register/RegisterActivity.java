package com.petrovic.m.dimitrije.activitytracker.ui.register;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import com.petrovic.m.dimitrije.activitytracker.R;
import com.petrovic.m.dimitrije.activitytracker.databinding.ActivityRegisterBinding;
import com.petrovic.m.dimitrije.activitytracker.ui.login.LoginActivity;
import com.petrovic.m.dimitrije.activitytracker.utils.Utils;

public class RegisterActivity extends AppCompatActivity {

    public static final String LOG_TAG = Utils.getLogTag(RegisterActivity.class);

    private ActivityRegisterBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(LOG_TAG, "onCreate");

        // View binding
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.appBar.toolbar.setTitle(R.string.title_activity_register);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_values_array,R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        binding.genderSpinner.setAdapter(adapter);

        SpannableString haveAccountString = createSpannableHaveAccountString();
        binding.haveAccount.setText(haveAccountString);
        binding.haveAccount.setMovementMethod(LinkMovementMethod.getInstance());

    }

    private SpannableString createSpannableHaveAccountString() {
        SpannableString createUserString = new SpannableString(getResources().getString(R.string.action_have_account));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginIntent);
            }
        };
        createUserString.setSpan(clickableSpan, createUserString.length() - 7, createUserString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        createUserString.setSpan(new StyleSpan(Typeface.BOLD), createUserString.length() - 7, createUserString.length(),0);

        return createUserString;
    }
}