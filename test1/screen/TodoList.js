import React, { useState, useEffect } from "react"
import { View, Text, TouchableOpacity, AsyncStorage, TextInput, StyleSheet, FlatList } from "react-native"
function TodoList(props) {
    const [job, setJob] = useState('')
    const [jobs, setJobs] = useState([])

    useEffect(() => {
        //lay danh sach cong viec tu AsyncStorage
        retrieveJobs();
    }, []);

    useEffect(() => {
        //Luu danh sach cong viec vao AsyncStorage
        storeJobs();
    }, [jobs]);

    //luu vao 
    const storeJobs = async () => {
        try {
            await AsyncStorage.setItem('jobs', JSON.stringify(jobs));
        } catch (error) {
            console.log(error);
        }
    }

    //lay ra 
    const retrieveJobs = async () => {
        try {
            const storedJobs = await AsyncStorage.getItem('jobs');

            if (storedJobs !== null) {
                setJobs(JSON.parse(storeJobs));
                console.log(storeJobs);
            }
        } catch (error) {

            console.log(error);
        }
    }

    const handleSubmit = () => {
        setJobs(prev => {
            const newJobs = [...prev, { key: Date.now().toString(), text: job }]

            // //luu vao localStogare
            // const jsonJobs = JSON.stringify(newJobs)
            // localStorage.setItem('jobs', jsonJobs)

            return newJobs
        })
        setJob('')
    }

    return (<View style={styles.main}>
        <TextInput
            value={job}
            autoCorrect={false}
            onChangeText={(text) => {
                setJob(text)
            }}
            style={styles.input} >
        </TextInput>

        <TouchableOpacity
            style={styles.button}
            onPress={handleSubmit}>
            <Text
                style={{ color: 'green' }}>Add
            </Text>
        </TouchableOpacity>
        <View>
            {jobs.map((item) => (
                <View key={item.key} style={styles.item}>
                    <Text style={{ color: 'black' }}>{item.text}</Text>
                </View>
            ))}
        </View>


    </View >)
}

const styles = StyleSheet.create({
    main: { backgroundColor: 'white', flex: 1 },
    button: {
        height: 40,
        paddingTop: 10,
        paddingStart: 10,
        flexDirection: 'row',
        borderWidth: 1,
        width: 100,
        marginTop: 10
    },
    input: {
        height: 40,
        width: '80%',
        backgroundColor: '#262526',
        borderRadius: 6,
        marginEnd: 20,
        marginBottom: 10,
        opacity: 0.8,
        paddingStart: 20,
    },
    container: {
        flex: 1,
        backgroundColor: '#fff',
        alignItems: 'center',
        justifyContent: 'center',
    },
    item: {
        backgroundColor: '#FCA17A',
        padding: 20,
        marginVertical: 8,
        marginHorizontal: 16,
    },
})

export default TodoList