import React, { useState } from "react"
import { View, Text, TouchableOpacity, TextInput, StyleSheet } from "react-native"
import { radioButton } from "../components/index"

// const gifts = [
//     'CPU-9',
//     'RAM 32GB',
//     'RGB Keyboard'
// ]s



//Response from API 
const courses = [
    {
        id: 1,
        name: 'HTML'
    },
    {
        id: 2,
        name: 'JavaScript'
    },
    {
        id: 3,
        name: 'ReactJS'
    }
]

function Home(props) {

    const [checked, setChecked] = useState([])


    const handCheckBox = (id) => {
        setChecked(prev => {
            const isCheked = checked.includes(id)
            if (isCheked) {
                return checked.filter(item => item !== id)
            } else {
                return [...prev, id]
            }
        })
        console.log(checked)
    }

    const handleSubmit = () => {
        //CALL API
        // console.log({ name, email })
        console.log({ ids: checked })
    }

    return <View style={{
        backgroundColor: 'white', flex: 1
    }}>
        {courses.map((item, index) => <TouchableOpacity key={index}
            onPress={() => handCheckBox(item.id)}>
            <View style={styles.wapper}>
                <View style={styles.radio}>
                    {checked.includes(item.id) ? <View style={styles.radioBg}></View> : null}
                </View>
                <Text style={styles.radioText}>{item.name}</Text>
            </View>
        </TouchableOpacity>)}

        {/* <radioButton list={courses} /> */}


        <TouchableOpacity style={{
            height: 40,
            paddingTop: 10,
            paddingStart: 10,
            flexDirection: 'row',
            borderWidth: 1,
            width: 100,
            marginTop: 10
        }}
            onPress={handleSubmit}>
            <Text
                style={{ color: 'red' }}>Register
            </Text>
        </TouchableOpacity>

    </View >
}
export default Home
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



// #1 USESTATE

// const [gift, setGift] = useState()

// const ramdonGift = () => {
//     const idx = Math.floor(Math.random() * gifts.length)
//     setGift(gifts[idx])
// }

// return <View style={{
//     backgroundColor: 'white', flex: 1
// }}>
//     <Text
//         style={{ color: 'red', marginTop: 20 }}> {gift || 'Chua co phan thuong'}
//     </Text>
//     <View style={{
//         height: 1,
//         backgroundColor: 'black',
//     }} />
//     <TouchableOpacity style={{
//         height: 150,
//         paddingTop: 20,
//         paddingStart: 10,
//         flexDirection: 'row',
//     }}
//         onPress={ramdonGift}>
//         <Text
//             style={{ color: 'red' }}> Nhan thuong
//         </Text>
//     </TouchableOpacity>

// </View>
//----------------------------------------------------------------------
// #2 2 binding

// const [name, setName] = useState('')
//     const [email, setEmail] = useState('')


//     const handleSubmit = () => {
//         //CALL API
//         console.log({ name, email })
//     }

//     return <View style={{
//         backgroundColor: 'white', flex: 1
//     }}>
//         <TextInput
//             value={name}
//             autoCorrect={false}
//             onChangeText={(text) => {
//                 setName(text)
//             }}
//             style={{
//                 height: 40,
//                 width: '80%',
//                 backgroundColor: '#262526',
//                 borderRadius: 6,
//                 marginEnd: 20,
//                 marginBottom: 10,
//                 opacity: 0.8,
//                 paddingStart: 20,
//             }} >
//         </TextInput>
//         <TextInput
//             value={email}
//             autoCorrect={false}
//             onChangeText={(text) => {
//                 setEmail(text)
//             }}
//             style={{
//                 height: 40,
//                 width: '80%',
//                 backgroundColor: '#262526',
//                 borderRadius: 6,
//                 marginEnd: 20,
//                 opacity: 0.8,
//                 paddingStart: 20,
//             }} >
//         </TextInput>

//         <TouchableOpacity style={{
//             height: 40,
//             paddingTop: 10,
//             paddingStart: 10,
//             flexDirection: 'row',
//             borderWidth: 1,
//             width: 100,
//             marginTop: 10
//         }}
//             onPress={handleSubmit}>
//             <Text
//                 style={{ color: 'red' }}>Register
//             </Text>
//         </TouchableOpacity>

//     </View >
//-----------------------------------------
// const [selected, setSelected] = useState(1)
// console.log(selected)

// const handleSubmit = () => {
//     //CALL API
//     console.log({ name, email })
// }

// return <View style={{
//     backgroundColor: 'white', flex: 1
// }}>
//     {courses.map((item, index) => <TouchableOpacity key={index}
//         onPress={() => setSelected(item.id)}>
//         <View style={styles.wapper}>
//             <View style={styles.radio}>
//                 {selected == item.id ? <View style={styles.radioBg}></View> : null}
//             </View>
//             <Text style={styles.radioText}>{item.name}</Text>
//         </View>
//     </TouchableOpacity>)}

//     {/* <radioButton list={courses} /> */}


//     <TouchableOpacity style={{
//         height: 40,
//         paddingTop: 10,
//         paddingStart: 10,
//         flexDirection: 'row',
//         borderWidth: 1,
//         width: 100,
//         marginTop: 10
//     }}
//         onPress={handleSubmit}>
//         <Text
//             style={{ color: 'red' }}>Register
//         </Text>
//     </TouchableOpacity>

// </View >