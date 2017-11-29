import Config from "../config/Config.js";
import {hashHistory} from "react-router";
import React from "react";
import {Dropdown} from "semantic-ui-react";
import {userStore} from "../store/UserStore.js";
import RestaurantService from "../service/RestaurantService";
import GroupService from "../service/GroupService.js";
import OrderService from "../service/OrderService.js";

export default class OrderSettings extends React.Component {

    state = {
        loggedInUser: null,
        groupId: this.props.params.groupId,
        restaurantNameInput: "",
        restaurantLinkInput: "",
        restaurantList: [],
        restaurantIDOrder: 0,
        errorMessage: "",
    };

    componentWillMount() {
        // OrderService.getOrderForGroup(this.state.groupId).then((newOrder) => {
        //     this.setState({
        //         order: newOrder
        //     });
        // }).catch((err) => {
        //     errorMessage: err.message
        // });

        this.getPopularRestaurants();
        this.setState({
            loggedInUser: userStore.getUserInfo().email
        });
    }

    componentDidMount() {
        $('.content')
    }

    // -------------------------------------
    // ---------BE OPERATIONS---------------
    // -------------------------------------

    getPopularRestaurants() {
        RestaurantService.getExistentRestaurantsForGroup(this.state.groupId).then((restaurants) => {
            let newRestaurantList = []
            for (let restaurant of restaurants) {
                newRestaurantList.push({
                    text: restaurant.name,
                    value: restaurant.id
                });
            }
            this.setState({
                restaurantList: newRestaurantList
            });
        }).catch((err) => {
            this.setState({
                errorMessage: err.message
            });
        });
    }

    saveRestaurant() {
        let restaurant = {
            id: null,
            name: this.state.restaurantNameInput,
            link: this.state.restaurantLinkInput
        };

        RestaurantService.saveRestaurant(restaurant).then((restaurantId) => {
            this.setState({
                restaurantIDOrder: restaurantId
            });
        }).catch((err) => {
            this.setState({
                loaded: true,
                errorMessage: err.message
            });
        });
    }

    startNewOrder(ev) {
        this.saveNewOrder();
    }

    saveNewOrder() {
        let order = {
            id: null,
            restaurantId: this.state.restaurantIDOrder,
            groupId: this.state.groupId,
            productList: null,
            sendOrderDate: null,
            active: true
        };
        OrderService.saveOrder(order).then((orderId) => {
            GroupService.getGroupById(this.state.groupId).then((oldGroup) => {
                if (typeof oldGroup != 'undefined') {
                    oldGroup.hasActiveOrder = true;
                    GroupService.saveGroup(oldGroup).then(() => {
                    });

                }
            });

            this.goToNewOrder(orderId);

        }).catch((err) => {
            this.setState({
                errorMessage: err.message
            });
        });
    }

    // -------------------------------------
    // ---------SET STATE METHODS-----------
    // -------------------------------------

    updateRestaurantName(event) {
        this.setState({
            restaurantNameInput: event.target.value
        });
    }

    updateRestaurantLink(event) {
        this.setState({
            restaurantLinkInput: event.target.value
        });
    }

    // -------------------------------------
    // ---------NAVIGATION------------------
    // -------------------------------------

    goToNewOrder(newOrderId) {
        let path = `${Config.ACTIVE_ORDER_PATH}/${ newOrderId }`;
        hashHistory.push(path);
    }

    // -------------------------------------
    // ---------RENDER METHODS--------------
    // -------------------------------------

    renderRestaurantSelectionForm() {
        return <div>
            <div className="field">
                <label>Restaurant Name</label>
                <input type="text" name="restaurant-name" placeholder="Restaurant Name"
                       onChange={this.updateRestaurantName.bind(this)}/>
            </div>
            <div className="field">
                <label>Restaurant Link</label>
                <input type="text" name="restaurant-link" placeholder="Restaurant Link"
                       onChange={this.updateRestaurantLink.bind(this)}/>
            </div>
            <button className="ui green button" onClick={this.saveRestaurant.bind(this)}>SAVE</button>
        </div>;
    }

    renderNextPhase() {
        if (this.state.restaurantIDOrder != 0) {
            return <div>
                <p>Selected Restaurant</p>
                <p>{ this.state.restaurantNameInput }</p>
                <button className="ui blue button"
                        onClick={ this.startNewOrder.bind(this) }>
                    START ORDER
                </button>
            </div>;
        }
    }


    render() {
        return <div className="ui raised very padded text container segment">
            <h1>OrderSettings</h1>

            <div className="ui form">

                <label>Previously used Restaurants</label>
                <Dropdown placeholder='Feature disabled' fluid multiple selection
                          disabled={true} options={this.state.restaurantList}/>

                <div className="ui divider"/>

                <div className="ui two column grid">
                    <div className="row">
                        <div className="ten wide column">
                            <div className="ui segment">
                                { this.renderRestaurantSelectionForm() }
                            </div>
                        </div>
                        <div className="six wide column">
                            {this.renderNextPhase()}
                        </div>
                    </div>
                </div>
            </div>
            <div>{this.props.children}</div>
        </div>;
    }


}