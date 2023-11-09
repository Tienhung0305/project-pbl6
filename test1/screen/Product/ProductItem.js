import React, { useEffect, useState } from "react"
import {
    Image,
    Text,
    View,
    TouchableOpacity,
    TextInput,
    KeyboardAvoidingView,
    Platform, Keyboard
} from 'react-native'
import { images, fontSize, colors } from '../../constants/index'
import getColorFromStatus from '../../utilies/CheckShoesStatus'
//import Icon from 'react-native-vector-icons/FontAwesome'

function ProductItem(props) {
    let { name,
        price,
        imageSet
    } = props.products //destructuring an object

    // Kiểm tra xem có tồn tại url và nó không phải là null
    const imageUrl = (imageSet.length > 0 ? imageSet[0].url : images.GiayMD);
    //console.log(imageSet.length > 0 ? imageSet[0].url : images.GiayMD);

    const { onPress } = props
    return (<TouchableOpacity
        onPress={onPress}
        style={{
            height: 150,
            paddingTop: 20,
            paddingStart: 10,
            flexDirection: 'row',
        }}>
        <Image
            style={{
                marginTop: 8,
                width: 100, height: 100,
                resizeMode: 'cover',
                borderRadius: 10,
                marginRight: 15
            }}
            source={{ uri: `${imageUrl}` }}
        >

        </Image>
        <View style={{
            flex: 1,
            marginRight: 10
        }}>
            <Text style={{
                color: 'black', fontSize: fontSize.h3,
                fontWeight: 'bold'
            }}>{name}
            </Text>

            <View style={{
                height: 1,
                backgroundColor: 'black',
            }} />

            {/* <View style={{
                flexDirection: "row"
            }}>
                <Text style={{
                    color: colors.placeholder,
                    fontSize: fontSize.h3,
                }}>Status:
                </Text>
                <Text style={{
                    color: getColorFromStatus(status),
                    fontSize: fontSize.h3,
                    fontWeight: 'bold'
                }}> {status.toUpperCase()}
                </Text>
            </View> */}

            <Text style={{
                color: colors.placeholder,
                fontSize: fontSize.h3,
            }}>Price: ${price}
            </Text>
            <Text style={{
                color: colors.placeholder,
                fontSize: fontSize.h3,
            }}>Brand: Nike
            </Text>

            {/* <View style={{ flexDirection: "row" }}>
                {socialNetworks['facebook'] != undefined &&
                    <Icon
                        style={{ paddingEnd: 10 }}
                        name='facebook'
                        size={20}
                        color={colors.placeholder}></Icon>
                }
                {socialNetworks['twitter'] != undefined &&
                    <Icon
                        style={{ paddingEnd: 10 }}
                        name='twitter'
                        size={20}
                        color={colors.placeholder}></Icon>}
                {socialNetworks['instagram'] != undefined &&
                    <Icon
                        style={{ paddingEnd: 10 }}
                        name='instagram'
                        size={20}
                        color={colors.placeholder}></Icon>}
            </View> */}

        </View>
    </TouchableOpacity >
    )
}
export default ProductItem