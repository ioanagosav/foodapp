import Config from "./config/Config.js";
import React from 'react';
import ReactDOM from 'react-dom';
import {Router, Route, Link, hashHistory} from 'react-router';

import Layout from './component/Layout.jsx';
import User from './component/User.jsx';
import AddGroup from './component/AddGroup.jsx';
import OrderSettings from './component/OrderSettings.jsx';
import AddProductToOrder from './component/AddProductToOrder.jsx';
import {userStore} from './store/UserStore.js';


hashHistory.listen((location, action) => {
    const adminRoutesOnly = ['/admin', '/groups'];

    if (!userStore.isAdmin()) {
        for (let p of adminRoutesOnly) {
            if (location.pathname.indexOf(p) !== -1) {
                hashHistory.push('/unauthorized');
            }
        }
    }
});

ReactDOM.render(
    <Router history={hashHistory}>
        <Route path="/" component={Layout}>
            <Route path={`${Config.MAIN_PAGE_PATH}`} component={User}/>
            <Route path={`${Config.ACTIVE_ORDER_PATH}/:orderId`} component={AddProductToOrder}/>
            <Route path={`${Config.ADD_EDIT_GROUP_PATH}/:groupId`} component={AddGroup}/>
            <Route path={`${Config.ORDER_SETTINGS_PATH}/:groupId`} component={OrderSettings}/>
        </Route>
    </Router>
    ,
    document.querySelector('#app')
);

