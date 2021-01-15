package de.aeins.capacitor;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;

import org.json.JSONException;

@NativePlugin(requestCodes = {CapacitorGooglePay.LOAD_PAYMENT_DATA_REQUEST_CODE})
public class CapacitorGooglePay extends Plugin {

    static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 42;

    private PaymentsClient paymentsClient = null;

    @Override
    protected void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
        super.handleOnActivityResult(requestCode, resultCode, data);

        PluginCall call = this.getSavedCall();
        switch (resultCode) {
            case Activity.RESULT_OK:
                PaymentData paymentData = PaymentData.getFromIntent(data);
                try {
                    call.success(new JSObject(paymentData.toJson()));
                } catch (JSONException e) {
                    call.error(e.getMessage(), e);
                }
                break;
            case Activity.RESULT_CANCELED:
                call.error("Payment cancelled");
                break;
            case AutoResolveHelper.RESULT_ERROR:
                Status status = AutoResolveHelper.getStatusFromIntent(data);

                switch (status.getStatusCode()) {
                    case WalletConstants.ERROR_CODE_DEVELOPER_ERROR: {
                        call.error(status.getStatusCode() + " ERROR_CODE_DEVELOPER_ERROR");
                    }
                    case WalletConstants.ERROR_CODE_BUYER_ACCOUNT_ERROR: {
                        // https://developers.google.com/android/reference/com/google/android/gms/wallet/WalletConstants
                        call.error(status.getStatusCode() + " ERROR_CODE_BUYER_ACCOUNT_ERROR");
                    }
                    break;
                    default: {
                        call.error(Integer.toString(status.getStatusCode()));
                    }
                }
                break;
        }
    }

    @PluginMethod
    public void configure(PluginCall call) {
        Boolean useProdEnvironment = call.getBoolean("useProdEnvironment");

        this.paymentsClient =
                Wallet.getPaymentsClient(
                        getActivity(),
                        new Wallet.WalletOptions.Builder()
                                .setEnvironment(useProdEnvironment ? WalletConstants.ENVIRONMENT_PRODUCTION : WalletConstants.ENVIRONMENT_TEST)
                                .build()
                );

        call.success();
    }

    @PluginMethod
    public void isReadyToPay(PluginCall call) {
        if (this.paymentsClient == null) {
            call.error("Not initialised. Please run configure() first.");
            return;
        }

        IsReadyToPayRequest request = IsReadyToPayRequest.fromJson(call.getData().toString());

        final PluginCall callContext = call;

        Task<Boolean> task = this.paymentsClient.isReadyToPay(request);
        task.addOnCompleteListener(
                getActivity(),
                new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            callContext.success();
                        } else {
                            Exception e = task.getException();
                            callContext.error(e.getMessage(), e);
                        }
                    }
                }
        );
    }

    @PluginMethod
    public void loadPaymentData(PluginCall call) {
        if (this.paymentsClient == null) {
            call.error("Not initialised. Please run configure() first.");
            return;
        }

        PaymentDataRequest request = PaymentDataRequest.fromJson(call.getData().toString());

        if (request != null) {
            this.saveCall(call);
            AutoResolveHelper.resolveTask(this.paymentsClient.loadPaymentData(request), getActivity(), LOAD_PAYMENT_DATA_REQUEST_CODE);
        }
    }
}
