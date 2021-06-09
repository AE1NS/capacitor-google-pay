import { registerPlugin } from '@capacitor/core';
import type { GooglePayPlugin } from './definitions';

const GooglePay = registerPlugin<GooglePayPlugin>('GooglePay', {
  web: () => import('./web').then(m => new m.GoogleMapsWeb()),
});

export * from './definitions';
export { GooglePay };
