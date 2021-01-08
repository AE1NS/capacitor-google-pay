import { WebPlugin } from '@capacitor/core';

export class GooglePayWeb extends WebPlugin {} // Not implemented

declare module '@capacitor/core' {
  interface PluginRegistry {
    GooglePay: GooglePayPlugin;
  }
}

export interface GooglePayPlugin {
  configure(options: { useProdEnvironment: boolean }): Promise<void>;
  isReadyToPay(isReadyToPayRequest: any): Promise<void>;
  loadPaymentData(paymentDataRequest: any): Promise<string>;
}
