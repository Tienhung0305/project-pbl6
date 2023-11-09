import axios from "axios";

const getProduct = async (page, pageSize) => {
    try {
        const response = await axios.get('https://decent-slope-production.up.railway.app/api/v1/product', {
            params: {
                page: page,
                page_size: pageSize,
            },
        });
        return response.data.content;
    } catch (error) {
        console.error('Error fetching data: ', error);
        throw error;
    }
};

export default getProduct;
