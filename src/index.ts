export * from './definitions';

import { registerPlugin } from '@capacitor/core';
import type { GooglePayPlugin } from './definitions';

const GooglePay = registerPlugin<GooglePayPlugin>('GooglePay', {});

export * from './definitions';
export { GooglePay };
