import React, { useEffect, useState } from "react"
import { getProduct } from "../../APIs/Product";
import {
    Image,
    Text,
    View,
    TouchableOpacity,
    StyleSheet,
    TextInput,
    FlatList
} from 'react-native'

import { useSelector, useDispatch } from 'react-redux';
import { setProduct } from "../../redux/reducer";

import fontSizes from "../../constants/fontSizes";
import ProductItem from "./ProductItem";
function ListProduct(props) {
    // const [page, setPage] = useState(1);
    // const [page_size, setPageSize] = useState(2);
    // const [products, setProducts] = useState([]);
    const { page, page_size, products } = useSelector(state => state.product);

    const dispatch = useDispatch();

    useEffect(() => {
        const getData = async () => {
            try {
                const data = await getProduct(page, page_size);
                dispatch(setProduct({ data, page, page_size }));
            } catch (error) {
                // Handle error if needed
            }
        };
        getData();

    }, [page, page_size]);
    // useEffect(() => {
    // }, [products])


    const renderItem = ({ item }) => {
        //console.log(item.imageSet[0]);
        return (
            <ProductItem products={item}
                onPress={() => {
                    alert(`press ${item.price}`)
                }}>
            </ProductItem>
        );
    }
    return (<View style={style.main}>
        <FlatList
            data={products}
            keyExtractor={(item) => item.id.toString()}
            renderItem={renderItem}
        />
    </View>)
}
const style = StyleSheet.create({
    main: { backgroundColor: 'white', flex: 1 },
    text: { color: 'black' }
})
export default ListProduct