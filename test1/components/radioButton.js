import React, { useState } from "react"
import { Text, View, TouchableOpacity, StyleSheet } from 'react-native'

function radioButton(props) {

    const [selected, setSelected] = useState(1)
    const { list } = props
    return (
        <View style={styles.main}>
            {list.map((item, index) => <TouchableOpacity key={index}
                onPress={() => setSelected(item.id)}>
                <View style={styles.wapper}>
                    <View style={styles.radio}>
                        {selected == item.id ? <View style={styles.radioBg}></View> : null}
                    </View>
                    <Text style={styles.radioText}>{item.name}</Text>
                </View>
            </TouchableOpacity>)}
        </View>
    )
}
const styles = StyleSheet.create({
    main: {
        flex: 1,
        alignItems: "center",
        justifyContent: "center"
    },
    radioText: {
        fontSize: 20,
        color: 'black'
    },
    radio: {
        width: 40,
        height: 40,
        borderColor: 'black',
        borderRadius: 20,
        borderWidth: 3,
        margin: 10
    },
    wapper: {
        flexDirection: 'row',
        alignItems: "center"
    },
    radioBg: {
        backgroundColor: 'green',
        height: 28,
        width: 28,
        margin: 3,
        borderRadius: 20

    }

})
export default radioButton