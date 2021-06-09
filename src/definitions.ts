export interface GooglePayPlugin {
  configure(options: { useProdEnvironment: boolean }): Promise<void>;
  isReadyToPay(isReadyToPayRequest: any): Promise<void>;
  loadPaymentData(paymentDataRequest: any): Promise<any>;
}
