package info.kingpes.verifyotp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

//Email:chau72811@gmail.com

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText edtSend, edtOtp;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks otpCallback;
    private String otpCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtSend = findViewById(R.id.edtSend);
        edtOtp = findViewById(R.id.edtOtp);

        firebaseAuth = FirebaseAuth.getInstance();

        otpCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(MainActivity.this, "Đã gửi thành công - onComplete", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(MainActivity.this, "Đã gửi thành công - onException", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                otpCode = s;
                Toast.makeText(MainActivity.this, "Đã gửi thành công - " + s, Toast.LENGTH_SHORT).show();
            }
        };
    }

    public void sendSms(View view) {
        String number = edtSend.getText().toString();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number, 60, TimeUnit.SECONDS, this, otpCallback
        );
    }

    private void signInWithPhone(PhoneAuthCredential credential){
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                    Toast.makeText(MainActivity.this, "Login Thành Công", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendOtp(View view) {
        String otp = edtOtp.getText().toString();
        if (!TextUtils.isEmpty(otpCode)){
            verifyPhoneNumber(otpCode, otp);
        }
    }

    private void verifyPhoneNumber(String otpCode, String otp) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpCode, otp);
        signInWithPhone(credential);
    }
}
