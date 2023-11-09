/**
 * @format
 */

import { AppRegistry } from 'react-native';
import React from 'react';
import { name as appName } from './app.json';
import { Home, TodoList, ListProduct } from './screen/index'
import { Provider } from 'react-redux';
import { store } from './redux/store';

AppRegistry.registerComponent(appName,
    () => () =>
        <Provider store={store}>
            <ListProduct />
        </Provider>
);
