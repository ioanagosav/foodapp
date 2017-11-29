import Config from "../config/Config.js";

export default class RestaurantService {


    static getExistentRestaurantsForGroup(groupId) {
        return new Promise((done, err) => {

            const xhr = new XMLHttpRequest();
            xhr.open('POST', `${Config.API_URL}/restaurant/getPreferredRestaurants`, false);
            xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
            xhr.setRequestHeader('Content-Type', 'application/json');

            xhr.send(JSON.stringify(groupId));
            if (xhr.status >= 300) {
                err({"message": xhr.response.errorMessage});
            } else {
                done(JSON.parse(xhr.response));
            }
        });
    }

    static getRestaurantById(restaurantId) {
        return new Promise((done, err) => {

            const xhr = new XMLHttpRequest();
            xhr.open('POST', `${Config.API_URL}/restaurant/getRestaurantById`, false);
            xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
            xhr.setRequestHeader('Content-Type', 'application/json');

            xhr.send(JSON.stringify(restaurantId));
            if (xhr.status >= 300) {
                err({"message": xhr.response});
            } else {
                done(JSON.parse(xhr.response));
            }
        });
    }

    static saveRestaurant(restaurant) {
        return new Promise((done, err) => {

            const xhr = new XMLHttpRequest();
            xhr.open('POST', `${Config.API_URL}/restaurant/save`, false);
            xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
            xhr.setRequestHeader('Content-Type', 'application/json');

            xhr.send(JSON.stringify(restaurant));
            if (xhr.status >= 300) {
                err({"message": xhr.response});
            } else {
                done(JSON.parse(xhr.response));
            }
        });
    }

}