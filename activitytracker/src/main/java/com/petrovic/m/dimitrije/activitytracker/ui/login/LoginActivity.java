package com.petrovic.m.dimitrije.activitytracker.ui.login;

import android.app.Activity;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptionsExtension;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.petrovic.m.dimitrije.activitytracker.MainActivity;
import com.petrovic.m.dimitrije.activitytracker.R;
import com.petrovic.m.dimitrije.activitytracker.data.model.LoggedInUser;
import com.petrovic.m.dimitrije.activitytracker.data.pojo.User;
import com.petrovic.m.dimitrije.activitytracker.databinding.ActivityLoginBinding;
import com.petrovic.m.dimitrije.activitytracker.rest.APIService;
import com.petrovic.m.dimitrije.activitytracker.rest.APIUtils;
import com.petrovic.m.dimitrije.activitytracker.rest.SessionManager;
import com.petrovic.m.dimitrije.activitytracker.ui.register.RegisterActivity;
import com.petrovic.m.dimitrije.activitytracker.utils.Utils;

import java.util.Set;

public class LoginActivity extends AppCompatActivity {

    public static final String LOG_TAG = Utils.getLogTag(LoginActivity.class);

    private ActivityLoginBinding binding;

    private LoginViewModel loginViewModel;

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    private SessionManager sessionManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(LOG_TAG, "onCreate");

        // View binding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.appBar.toolbar.setTitle(R.string.title_activity_login);

        // TODO remove temp
        binding.gotToMain.setOnClickListener(v -> {
            User tempUser = new User();
            LoggedInUser tempLoggedInUser = new LoggedInUser();
            tempLoggedInUser.setUser(tempUser);
            sessionManager.setUser(tempLoggedInUser);
            Intent mainActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(mainActivityIntent);
        });

        SpannableString createUserString = createSpannableCreateUserString();
        binding.createAccount.setText(createUserString);
        binding.createAccount.setMovementMethod(LinkMovementMethod.getInstance());

        // REST communication init
        sessionManager = SessionManager.getInstance(this);

        // Get Google client
        mGoogleSignInClient = APIUtils.getGoogleClient(this);

        binding.googleLoginButton.setSize(SignInButton.SIZE_STANDARD);
        binding.googleLoginButton.setOnClickListener(v -> googleLogin());

        // Set the login view model
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory(getApplication()))
                .get(LoginViewModel.class);

        loginViewModel.getLoginFormState().observe(this, loginFormState -> {
            if (loginFormState == null) {
                return;
            }
            binding.loginButton.setEnabled(loginFormState.isDataValid());
            if (loginFormState.getUsernameError() != null) {
                binding.email.setError(getString(loginFormState.getUsernameError()));
            }
            if (loginFormState.getPasswordError() != null) {
                binding.password.setError(getString(loginFormState.getPasswordError()));
            }
        });

        loginViewModel.getLoginResult().observe(this, loginResult -> {
            if (loginResult == null) {
                return;
            }

            if (loginResult.getError() != null) {
                binding.progressOverlay.getRoot().setVisibility(View.GONE);
                showLoginFailed(loginResult.getError());
            } else if (loginResult.getSuccess() != null) {
                updateUiWithUser(loginResult.getSuccess());
            } else {
                binding.progressOverlay.getRoot().setVisibility(View.GONE);
                Log.d(LOG_TAG, "Silent login failed");
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(binding.email.getText().toString(),
                        binding.password.getText().toString());
            }
        };
        binding.email.addTextChangedListener(afterTextChangedListener);
        binding.password.addTextChangedListener(afterTextChangedListener);
        binding.password.setOnEditorActionListener((v, actionId, event) -> {
            Log.d(LOG_TAG, "onEditorActionListener");
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.progressOverlay.getRoot().setVisibility(View.VISIBLE);
                Log.d(LOG_TAG, "progressBar visible");
                loginViewModel.login(binding.email.getText().toString(),
                        binding.password.getText().toString());
            }
            return false;
        });

        binding.loginButton.setOnClickListener(v -> {
            Log.d(LOG_TAG, "onClickListener");
            binding.progressOverlay.getRoot().setVisibility(View.VISIBLE);
            Log.d(LOG_TAG, "progressBar visible");
            loginViewModel.login(binding.email.getText().toString(),
                    binding.password.getText().toString());
        });

        binding.progressOverlay.getRoot().bringToFront();
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.d(LOG_TAG, "onStart");

        attemptSilentLogin();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(LOG_TAG, "onActivityResult");

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            loginViewModel.loginGoogle(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(LOG_TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(getApplicationContext(), "Error signing in to Google!", Toast.LENGTH_LONG).show();
        }
    }

    public void attemptSilentLogin() {
        Log.d(LOG_TAG, "Attempting silent log in");
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        binding.progressOverlay.getRoot().setVisibility(View.VISIBLE);

        if (account != null) {
            // TODO
            //  issue with calling silentLoginGoogle with authCode authentication
            Log.d(LOG_TAG, "Already signed in to Google, name: " + account.getDisplayName() + ", email: " + account.getEmail());
            loginViewModel.silentLogin(account);
        } else {
            Log.d(LOG_TAG, "Not signed in to Google");
            loginViewModel.silentLogin(null);
        }
    }

    public void googleLogin() {
        Log.d(LOG_TAG, "googleLogin");

        binding.progressOverlay.getRoot().setVisibility(View.VISIBLE);

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private SpannableString createSpannableCreateUserString() {
        SpannableString createUserString = new SpannableString(getResources().getString(R.string.action_create_user));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent loginIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(loginIntent);
            }
        };
        createUserString.setSpan(clickableSpan, createUserString.length() - 10, createUserString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        createUserString.setSpan(new StyleSpan(Typeface.BOLD), createUserString.length() - 10, createUserString.length(),0);

        return createUserString;
    }

    private void updateUiWithUser(LoggedInUser user) {
        Log.d(LOG_TAG, "Login successful");
        String welcome = getString(R.string.welcome) + " " +  user.getDisplayName();

        if (user.getGoogleAccount() != null) {
            welcome += "\n             Google Sign-In";
        }

        setResult(Activity.RESULT_OK);

        //Complete and destroy login activity once successful
        Intent mainActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mainActivityIntent);
        finish();

        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(Throwable error) {
        Log.d(LOG_TAG, "Login failed");
        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
    }
}