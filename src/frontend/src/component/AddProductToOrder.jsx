import Config from "../config/Config.js";
import React from "react";
import RestaurantService from "../service/RestaurantService.js";
import GroupService from "../service/GroupService.js";
import OrderService from "../service/OrderService.js";
import {userStore} from "../store/UserStore.js";
import {Accordion, Icon} from "semantic-ui-react";
import {hashHistory} from 'react-router';

export default class AddProductToOrder extends React.Component {

    state = {
        order: {},
        orderId: this.props.params.orderId,
        restaurant: {},
        productNameInput: "",
        productDescInput: "",
        productPriceInput: 0,
        loggedInUser: null,
        isUserAdmin: false,
        activeIndex: 0,
        errorMessage: ""
    };

    componentWillMount() {
        this.getOrderById();
        this.setState({
            loggedInUser: userStore.getUserInfo().email
        });

    }

    componentDidMount() {
        $('.content');
    }

    goToMainPage() {
        hashHistory.push(`${Config.MAIN_PAGE_PATH}`);
    }

    // -------------------------------------
    // ---------BE OPERATIONS---------------
    // -------------------------------------

    getOrderById() {
        OrderService.getOrderById(this.state.orderId).then((returnedOrder) => {
            this.setState({
                order: returnedOrder
            });

            this.getRestaurantById();

            this.checkIsUserAdmin();

        }).catch((err) => {
            this.setState({
                errorMessage: err.message
            });
        });
    }

    getRestaurantById() {
        RestaurantService.getRestaurantById(this.state.order.restaurantId).then((restaurantObject) => {
            this.setState({
                restaurant: restaurantObject
            });
        }).catch((err) => {
            this.setState({
                errorMessage: err.message
            });
        });
    }

    saveOrder(newOrder) {
        OrderService.saveOrder(newOrder).then((id) => {
            this.setState({
                order: newOrder,
                activeIndex: 1
            });
        }).catch((err) => {

            this.setState({
                errorMessage: err.message
            });
        });
    }

    checkIsUserAdmin() {
        GroupService.isUserAdminForGroup(this.state.order.groupId).then((response) => {
            if (response) {
                this.setState({
                    isUserAdmin: response
                });
            }
        });
    }

    stopOrder() {
        //group active = false
        //order active = false
        GroupService.deactivateGroup(this.state.order.groupId).then(() => {
            OrderService.sendOrder(this.state.orderId).then(() => {

                this.goToMainPage();

            }).catch((err) => {
                this.setState({
                    errorMessage: err.message
                });
            });
        }).catch((err) => {
            this.setState({
                errorMessage: err.message
            });
        });

    }

    // -------------------------------------
    // ---------SET STATE METHODS-----------
    // -------------------------------------

    updateProductNameInput(ev) {
        this.setState({
            productNameInput: ev.target.value
        });
    }

    updateProductDescInput(ev) {
        this.setState({
            productDescInput: ev.target.value
        });
    }

    updateProductPriceInput(ev) {
        this.setState({
            productPriceInput: ev.target.value
        });
    }

    saveProductToOrder(ev) {
        let product = {
            name: this.state.productNameInput,
            price: this.state.productPriceInput,
            description: this.state.productDescInput,
            saveDate: null,
            userEmail: this.state.loggedInUser
        };

        let newOrder = this.state.order;
        let products = newOrder.productList;
        if ((typeof products == 'undefined') || products == null) {
            products = [];
        }
        products.push(product);
        newOrder.productList = products;
        this.saveOrder(newOrder);

        this.refreshProductInput();
    }

    refreshProductInput() {
        this.setState({
            productNameInput: "",
            productPriceInput: 0,
            productDescInput: ""
        });
    }

    // -------------------------------------
    // ---------RENDER METHODS--------------
    // -------------------------------------

    renderErrorMessage() {
        if (this.state.errorMessage != null
            && this.state.errorMessage != "") {
            return "ui negative message";
        } else {
            return "ui hidden message";
        }
    }

    renderStopOrderButton() {
        if (this.state.isUserAdmin) {
            return (
                <button className="ui red basic button"
                        onClick={ this.stopOrder.bind(this) }>
                    STOP order & email me details
                </button>
            );
        }
    }

    renderAddProduct() {
        return <div>
            <p>Restaurant { this.state.restaurant.name } - <a
                target="_blank" href={ this.state.restaurant.link }>Link</a>
            </p>
            <div className="ui form">
                <div className="field">
                    <label>Product name</label>
                    <input type="text" name="product-name" placeholder="Product Name"
                           value={this.state.productNameInput}
                           onChange={ this.updateProductNameInput.bind(this) }/>
                </div>
                <div className="field">
                    <label>Product price</label>
                    <input type="number" name="product-price" placeholder="Product Price"
                           value={this.state.productPriceInput}
                           onChange={ this.updateProductPriceInput.bind(this) }/>
                </div>
                <div className="field">
                    <label>Product Description</label>
                    <input type="text" name="product-desc" placeholder="Product Description"
                           value={this.state.productDescInput}
                           onChange={ this.updateProductDescInput.bind(this) }/>
                </div>
                <button className="ui green button"
                        onClick={ this.saveProductToOrder.bind(this) }>SAVE
                </button>
            </div>
        </div>;
    }

    renderOrderTable() {
        return <table className="ui fixed table">
            <thead>
            <tr>
                <th>Name</th>
                <th>Product</th>
                <th>Price</th>
                <th>Description</th>
            </tr>
            </thead>
            <tbody>
            { this.renderProductRow() }
            </tbody>
        </table>;
    }

    renderProductRow() {
        if (this.state.order.productList != null) {
            return this.state.order.productList.map((productObject, i) => {
                return (
                    <tr key={i}>
                        <td>{ productObject.userEmail }</td>
                        <td>{ productObject.name }</td>
                        <td>{ productObject.price }</td>
                        <td>{ productObject.description }</td>
                    </tr>
                );

            });
        }
    }

    generateAccordionPanel() {
        return <Accordion defaultActiveIndex={this.state.activeIndex} fluid styled>

            <Accordion.Title><Icon name='dropdown'/>Add Product</Accordion.Title>
            <Accordion.Content>{this.renderAddProduct()}</Accordion.Content>

            <Accordion.Title><Icon name='dropdown'/>All Orders</Accordion.Title>
            <Accordion.Content>{this.renderOrderTable()}</Accordion.Content>
        </Accordion>;
    }

    render() {
        return <div className="ui raised very padded text container segment">
            <h1>Order Food <i className="food icon"/></h1>

            <div className={this.renderErrorMessage()}>
                <div className="header">
                    An error occurred
                </div>
                <p>{this.state.errorMessage}</p>
            </div>

            { this.generateAccordionPanel() }

            <div className="ui divider"/>

            { this.renderStopOrderButton() }

            <div>{this.props.children}</div>
        </div>;
    }
}