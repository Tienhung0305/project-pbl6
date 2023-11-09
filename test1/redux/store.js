import { configureStore } from '@reduxjs/toolkit';
import productSlice from './reducer';

export const store = configureStore({
    reducer: {
        product: productSlice,
    },
});