import Config from "../config/Config.js";

export default class UserService {

    static getUserInfo() {
        return new Promise((done, err) => {
            // done({
            //     email: "a@a.com",
            //     isAdmin: false,
            //     link: "http://link"
            // });
            const xhr = new XMLHttpRequest();
            xhr.open('GET', `${Config.API_URL}/me`, false);
            xhr.setRequestHeader('Access-Control-Allow-Origin', '*')
            xhr.send();
            if (xhr.status === 200) {
                done(JSON.parse(xhr.response));
            } else {
                err({"message": xhr.response});
            }
        });
    }

}
