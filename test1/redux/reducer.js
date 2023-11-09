const { createSlice } = require("@reduxjs/toolkit");

const initialState = {
    page: 1,
    page_size: 8,
    products: []
};

const productSlice = createSlice({
    name: "Product",
    initialState,
    reducers: {
        setProduct: (state, action) => {
            state.products = action.payload.data;
            state.page = action.payload.page;
            state.page_size = action.payload.page_size

        },
    }
})

export const { setProduct } = productSlice.actions;
export default productSlice.reducer;


