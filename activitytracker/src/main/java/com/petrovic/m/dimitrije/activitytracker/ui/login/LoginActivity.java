package com.petrovic.m.dimitrije.activitytracker.ui.login;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.Nullable;
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
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.petrovic.m.dimitrije.activitytracker.MainActivity;
import com.petrovic.m.dimitrije.activitytracker.R;
import com.petrovic.m.dimitrije.activitytracker.data.model.LoggedInUser;
import com.petrovic.m.dimitrije.activitytracker.data.pojo.Token;
import com.petrovic.m.dimitrije.activitytracker.databinding.ActivityLoginBinding;
import com.petrovic.m.dimitrije.activitytracker.rest.APIService;
import com.petrovic.m.dimitrije.activitytracker.rest.APIUtils;
import com.petrovic.m.dimitrije.activitytracker.rest.SessionManager;
import com.petrovic.m.dimitrije.activitytracker.ui.register.RegisterActivity;
import com.petrovic.m.dimitrije.activitytracker.utils.Utils;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    public static final String LOG_TAG = Utils.getLogTag(LoginActivity.class);

    private ActivityLoginBinding binding;

    private LoginViewModel loginViewModel;

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    private APIService apiService;
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

        // TODO
        //  remove temp
        binding.gotToMain.setOnClickListener(v -> {
            Intent mainActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(mainActivityIntent);
        });

        SpannableString createUserString = createSpannableCreateUserString();
        binding.createAccount.setText(createUserString);
        binding.createAccount.setMovementMethod(LinkMovementMethod.getInstance());

        // REST communication init
        apiService = APIUtils.getApiService(this.getApplicationContext());
        sessionManager = SessionManager.getInstance(this);

        // Configure Google sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        //mGoogleSignInClient.silentSignIn();

        // TODO
        // temp
        mGoogleSignInClient.signOut();

        binding.googleLoginButton.setSize(SignInButton.SIZE_STANDARD);
        binding.googleLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleLogin();
            }
        });

        // Set the login view model
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory(getApplication()))
                .get(LoginViewModel.class);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
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
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }

                if (loginResult.getError() != null) {
                    binding.progressOverlay.getRoot().setVisibility(View.GONE);
                    showLoginFailed(loginResult.getError());
                } else if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
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

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        Toast.makeText(getApplicationContext(), "GoogleSignInAccount " + (account != null ? account.getDisplayName() : "not signed in to Google"), Toast.LENGTH_LONG).show();
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

            String authCode = account.getServerAuthCode();
            // TODO remove
            //  used for debugging
            Log.d(LOG_TAG, "authCode = " + authCode);

            // TODO(developer): send code to server and exchange for access/refresh/ID tokens

            Single<Token> token = apiService.googleToken(authCode);
            token.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Token>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(LOG_TAG, "onSubscribe");
                    }

                    @Override
                    public void onSuccess(Token token) {
                        Log.d(LOG_TAG, "onSuccess token = " + token);

                        Intent mainActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(mainActivityIntent);
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(LOG_TAG, "onError");
                    }
                });


            // Signed in successfully, show authenticated UI.
            Toast.makeText(getApplicationContext(), "GoogleSignInAccount " + (account != null ? account.getDisplayName() : "not signed in to Google"), Toast.LENGTH_LONG).show();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(LOG_TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(getApplicationContext(), "Not signed in to Google!", Toast.LENGTH_LONG).show();
        }
    }

    public void googleLogin() {
        Log.d(LOG_TAG, "googleLogin");

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void googleLogout() {
        Log.d(LOG_TAG, "googleLogout");
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //updateUI(null);
            }
        });
    }

    private void googleRevokeAccess() {
        Log.d(LOG_TAG, "googleRevokeAccess");
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //updateUI(null);
                    }
                });
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