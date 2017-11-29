import React from 'react';
import UserService from '../service/UserService.js';
import {userStore} from '../store/UserStore.js';
import {hashHistory} from 'react-router';

export default class Layout extends React.Component {

    constructor() {
        super();
        this.state = {
            loaded: false,
            errorMessage: null
        };
    }

    componentWillMount() {
        //ajax call la /me
        UserService.getUserInfo().then((rsp) => {
            userStore.setUserInfo(rsp);
            this.setState({
                loaded: true
            });

            if (rsp.isAdmin === true) {
                hashHistory.getCurrentLocation().pathname !== "/admin" && hashHistory.push('/admin');
            } else {
                hashHistory.getCurrentLocation().pathname !== "/user" && hashHistory.push('/user');
            }
        })
            .catch((err) => {
                this.setState({
                    loaded: true,
                    errorMessage: err.message
                });

            });
    }

    render() {

        if (this.state.loaded === false) {
            return <div className="ui segment">
                <p></p>
                <div className="ui active dimmer">
                    <div className="ui loader"></div>
                </div>
            </div>;
        }

        if (this.state.errorMessage) {
            return <div className="ui message red">{this.state.errorMessage}</div>;
        }

        return <div>{this.props.children}</div>;
    }

}
