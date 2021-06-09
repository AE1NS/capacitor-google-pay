import { WebPlugin } from '@capacitor/core';
import { GooglePayPlugin } from './definitions';

export class GoogleMapsWeb extends WebPlugin implements GooglePayPlugin {
  constructor() {
    super({
      name: 'GooglePay',
      platforms: ['web'],
    });
  }

  configure(): Promise<void> {
    throw new Error('Method not implemented.');
  }

  isReadyToPay(): Promise<void> {
    throw new Error('Method not implemented.');
  }

  loadPaymentData(): Promise<string> {
    throw new Error('Method not implemented.');
  }
}
