import Config from "../config/Config.js";

export default class OrderService {

    static saveOrder(order) {
        return new Promise((done, err) => {
            const xhr = new XMLHttpRequest();
            xhr.open('POST', `${Config.API_URL}/orders/saveOrder`, false);
            xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
            xhr.setRequestHeader('Content-Type', 'application/json');

            xhr.send(JSON.stringify(order));
            if (xhr.status >= 300) {
                err({"message": xhr.response.errorMessage});
            } else {
                done(JSON.parse(xhr.response));
            }
        });
    }

    static getOrderById(orderId) {
        return new Promise((done, err) => {
            const xhr = new XMLHttpRequest();
            xhr.open('POST', `${Config.API_URL}/orders/getOrderById`, false);
            xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
            xhr.setRequestHeader('Content-Type', 'application/json');

            xhr.send(JSON.stringify(orderId));
            if (xhr.status >= 300) {
                err({"message": xhr.response});
            } else {
                done(JSON.parse(xhr.response));
            }
        });
    }

    static getOrderForGroup(groupId) {
        return new Promise((done, err) => {
            const xhr = new XMLHttpRequest();
            xhr.open('POST', `${Config.API_URL}/orders/getOrderForGroup`, false);
            xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
            xhr.setRequestHeader('Content-Type', 'application/json');

            xhr.send(JSON.stringify(groupId));
            if (xhr.status >= 300) {
                err({"message": xhr.response});
            } else {
                done(JSON.parse(xhr.response));
            }
        });
    }

    static sendOrder(orderId) {
        return new Promise((done, err) => {
            const xhr = new XMLHttpRequest();
            xhr.open('POST', `${Config.API_URL}/orders/sendOrder`, false);
            xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
            xhr.setRequestHeader('Content-Type', 'application/json');

            xhr.send(JSON.stringify(orderId));
            if (xhr.status >= 300) {
                err({"message": xhr.response});
            } else {
                done();
            }
        });
    }
}