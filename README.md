<p align="center"><br><img src="https://avatars3.githubusercontent.com/u/47378799?s=460&u=f086e8ca43aa0794dc61a453aae751b26f937d95&v=4" width="128" height="128" /></p>
<h3 align="center">Google Pay</h3>
<p align="center">
  Capacitor plugin for native Google Pay usage (Android).
</p>

## Installation

```bash
npm i capacitor-google-pay@git://github.com/AE1NS/capacitor-google-pay.git
npx cap sync android
```

Register the plugin in your main activity:
```java
import de.aeins.capacitor.CapacitorGooglePay;

public class MainActivity extends BridgeActivity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Initializes the Bridge
    this.init(savedInstanceState, new ArrayList<Class<? extends Plugin>>() {{
      // Additional plugins you've installed go here
      // Ex: add(TotallyAwesomePlugin.class);
      add(CapacitorGooglePay.class);
    }});
  }
}
```

## Usage

Always call configure() once, before using the isReadyToPay/requestPayment method.

```typescript
async configure() {
    await GooglePay.configure({ useProdEnvironment: false });
    GooglePay.isReadyToPay({ ... })
      .then(() => {
          // Show Google Pay button
      });
}

async pay() {
    GooglePay.requestPayment({ ... }).then((token) => {
        console.log('Google pay token', token);
    });
}
```

## Methods

| Method          | Parameter type     | Response type |
| -               | -                  | -             |
| configure       | {<br/>&nbsp;&nbsp;&nbsp;&nbsp;useProdEnvironment: <span style="color:blue">boolean</span><br/>} | <span style="color:blue">void</span> |
| isReadyToPay    | [IsReadyToPayRequest](https://developers.google.com/pay/api/android/reference/request-objects#IsReadyToPayRequest) | <span style="color:blue">void</span> |
| loadPaymentData | [PaymentDataRequest](https://developers.google.com/pay/api/android/reference/request-objects#PaymentDataRequest) | <span style="color:blue">string</span> (response token) |