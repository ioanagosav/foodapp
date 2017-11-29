import Config from "../config/Config.js";
import React from 'react';
import OrderService from "../service/OrderService.js";
import GroupService from '../service/GroupService.js';
import {userStore} from '../store/UserStore.js';
import {hashHistory} from 'react-router';

export default class User extends React.Component {

    state = {
        loggedInUser: "",
        errorMessage: null,
        loaded: true,
        groupsForAdmin: [],
        groupsForMembers: []
    };

    componentWillMount() {
        this.setState({
            loggedInUser: userStore.getUserInfo().email
        });
    }

    componentDidMount() {
        this.getGroupsForAdmin();
        this.getGroupsForMembers();
    }

    // -------------------------------------
    // ---------BE OPERATIONS---------------
    // -------------------------------------

    getGroupsForAdmin() {
        GroupService.getGroupsForAdmin().then((groups) => {
            this.setState({
                groupsForAdmin: groups
            });
        });
    }

    getGroupsForMembers() {
        GroupService.getGroupsForUser().then((groups) => {
            this.setState({
                groupsForMembers: groups
            });
        });
    }



    // -------------------------------------
    // ---------NAVIGATION------------------
    // -------------------------------------

    goToGroupSettings(group, ev) {
        const path = `${Config.ADD_EDIT_GROUP_PATH}/${ group.id }`;
        hashHistory.push(path);
    }

    goToOrderSettings(group, ev) {
        const path = `${Config.ORDER_SETTINGS_PATH}/${ group.id }`;
        hashHistory.push(path);
    }

    goToOrder(group, ev) {
        OrderService.getOrderForGroup(group.id).then((newOrder) => {

            hashHistory.push(`${Config.ACTIVE_ORDER_PATH}/${ newOrder.id }`);

        }).catch((err) => {
            this.setState({
                errorMessage: err.message
            });
        });
    }

    goToEditGroup(group, ev) {
        if (group == null) {
            hashHistory.push(`${Config.ADD_EDIT_GROUP_PATH}/0`);
        } else {
            hashHistory.push(`${Config.ADD_EDIT_GROUP_PATH}/${group.id}`);
        }
    }

    // OrderService.getOrderForGroup(this.state.groupId).then((newOrder) => {
    //     this.setState({
    //         order: newOrder
    //     });
    // }).catch((err) => {
    //     errorMessage: err.message
    // });

    // -------------------------------------
    // ---------RENDER METHODS--------------
    // -------------------------------------

    renderGroupsForMembers() {
        return this.state.groupsForMembers.map((group, i) => {
            let hasActiveOrder = group.hasActiveOrder;
            return (
                <div key={i} className="content">
                    <div className="header">
                        <div className="ui two column grid">
                            <div className="row">
                                <div className="six wide column">
                                    { this.renderIconForGroups(hasActiveOrder) }
                                    { group.name } Group
                                </div>
                                <div className="eight wide column">
                                    { this.renderOrderButtonForMember(hasActiveOrder, group) }
                                </div>
                                <div className="two wide column"/>
                            </div>
                        </div>
                    </div>
                </div>
            );
        });
    }

    renderGroupsForAdmins() {
        return this.state.groupsForAdmin.map((group, i) => {
            let hasActiveOrder;
            if (group.hasActiveOrder) {
                hasActiveOrder = true;
            } else {
                hasActiveOrder = false;
            }
            return (
                <div key={i} className="content">
                    <div className="header">
                        <div className="ui two column grid">
                            <div className="row">
                                <div className="six wide column">
                                    { this.renderIconForGroups(hasActiveOrder) }
                                    { group.name } Group
                                </div>
                                <div className="eight wide column">
                                    { this.renderOrderButtonsForAdmin(hasActiveOrder, group) }
                                </div>
                                <div className="two wide column"/>
                            </div>
                        </div>
                    </div>
                </div>
            );
        });

    }

    renderIconForGroups(hasActiveOrder) {
        if (hasActiveOrder) {
            return <i className="unmute green icon"/>;
        } else {
            return <i className="mute red icon"/>;
        }
    }

    // -------------------------------------
    // ---------RENDER BUTTONS--------------
    // -------------------------------------

    renderOrderButtonsForAdmin(hasActiveOrder, group) {
        //
        // THIS IS OLD IMPLEMENTATION

        // if (hasActiveOrder) {
        //     return <div className="ui two column grid">
        //         <div className="row">
        //             <div className="eight wide column">
        //                 <button className="ui mini red basic left floated button"
        //                         onClick={ this.setGroupStatus.bind(this, group, false) }>
        //                     STOP Order
        //                 </button>
        //             </div>
        //             <div className="eight wide column">
        //                 <button className="ui mini purple basic left floated button"
        //                         onClick={this.goToOrder.bind(this, group)}>Go to Order
        //                 </button>
        //             </div>
        //         </div>
        //     </div>;
        // } else {
        //     return <button className="ui mini green basic button"
        //                    onClick={ this.setGroupStatus.bind(this, group, true) }>
        //         START Order
        //     </button>;
        // }


        return <div>
            <button className="ui mini grey basic button"
                    onClick={ this.goToGroupSettings.bind(this, group) }>
                Group Settings
            </button>

            { this.renderGoToOrderButton(hasActiveOrder, group) }
        </div>;
    }

    renderGoToOrderButton(hasActiveOrder, group) {
        if (hasActiveOrder) {
            return <button className="ui mini teal basic button"
                           onClick={ this.goToOrder.bind(this, group)}>
                Go To Order
            </button>;
        } else {
            return <button className="ui mini green basic button"
                           onClick={ this.goToOrderSettings.bind(this, group)}>
                Start Order
            </button>;
        }
    }

    renderOrderButtonForMember(hasActiveOrder, group) {
        if (hasActiveOrder) {
            return <div className="eight wide column">
                <button className="ui mini purple basic left floated button"
                        onClick={ this.goToOrder.bind(this, group) }>Go to Order
                </button>
            </div>;
        } else {
            return <p>Ask your admin to start the order</p>;
        }
    }

    // -------------------------------------
    // ---------RENDER MESSAGES-------------
    // -------------------------------------

    renderMessageForAdmins() {
        if (this.state.groupsForAdmin.length > 0) {
            return <div><h4>You currently are managing the following groups : </h4></div>
        }
        if (this.state.groupsForAdmin.length == 0) {
            return <div><h4>You currently are not managing any groups.</h4></div>
        }
    }

    renderMessageForMembers() {
        if (this.state.groupsForMembers.length > 0) {
            return <div><h4>You currently are a member of the following groups : </h4></div>
        }
        if (this.state.groupsForMembers.length == 0) {
            return <div><h4>You currently are not a member of any groups.</h4></div>
        }
    }

    renderErrorMessage() {
        if (this.state.errorMessage != null && this.state.errorMessage != "") {
            return "ui negative message";
        } else {
            return "ui hidden message";
        }
    }

    render() {
        if (this.state.loaded === false) {
            return <div className="ui segment">
                <div className="ui active dimmer">
                    <div className="ui loader"></div>
                </div>
            </div>;
        }
        return <div className="ui raised very padded text container segment">

            <div className={this.renderErrorMessage()}>
                <div className="header">
                    An error occurred
                </div>
                <p>{this.state.errorMessage}</p>
            </div>

            <h4>Welcome user, { this.state.loggedInUser }</h4>
            <button className="ui blue basic button"
                    onClick={ this.goToEditGroup.bind(this, null)}>
                Create a new Group
            </button>

            <div className="ui divider"/>

            { this.renderMessageForAdmins() }
            <div className="ui list">
                { this.renderGroupsForAdmins() }
            </div>
            <div className="ui divider"/>

            { this.renderMessageForMembers() }
            <div className="ui list">
                { this.renderGroupsForMembers() }
            </div>

            <div>{this.props.children}</div>
        </div>;
    }
}