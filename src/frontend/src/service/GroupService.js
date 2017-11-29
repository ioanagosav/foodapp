import Config from "../config/Config.js";

export default class GroupService {

    static saveNewGroup(group) {
        return new Promise((done, err) => {

            const xhr = new XMLHttpRequest();
            xhr.open('POST', `${Config.API_URL}/groups/saveNewGroup`, false);
            xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
            xhr.setRequestHeader('Content-Type', 'application/json');

            xhr.send(JSON.stringify(group));
            if (xhr.status >= 300) {
                err({"message": xhr.response});
            } else {
                done();
            }
        });
    }

    static saveGroup(group) {
        return new Promise((done, err) => {

            const xhr = new XMLHttpRequest();
            xhr.open('POST', `${Config.API_URL}/groups/save`, false);
            xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
            xhr.setRequestHeader('Content-Type', 'application/json');

            xhr.send(JSON.stringify(group));
            if (xhr.status >= 300) {
                err({"message": xhr.response});
            } else {
                done();
            }
        });
    }

    static getGroupsForAdmin() {
        return new Promise((done, err) => {

            const xhr = new XMLHttpRequest();
            xhr.open('GET', `${Config.API_URL}/groups/getGroupsForAdmin`, false);
            xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
            xhr.setRequestHeader('Content-Type', 'application/json');
            xhr.send();

            if (xhr.status >= 300) {
                err({"message": xhr.response});
            } else {
                done(JSON.parse(xhr.response));
            }
        });
    }

    static getGroupsForUser() {
        return new Promise((done, err) => {

            const xhr = new XMLHttpRequest();
            xhr.open('GET', `${Config.API_URL}/groups/getGroupsForUser`, false);
            xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
            xhr.setRequestHeader('Content-Type', 'application/json');
            xhr.send();

            if (xhr.status >= 300) {
                err({"message": xhr.response});
            } else {
                done(JSON.parse(xhr.response));
            }
        });
    }

    static deactivateGroup(groupId) {
        return new Promise((done, err) => {

            const xhr = new XMLHttpRequest();
            xhr.open('POST', `${Config.API_URL}/groups/deactivateGroup`, false);
            xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
            xhr.setRequestHeader('Content-Type', 'application/json');

            xhr.send(JSON.stringify(groupId));
            if (xhr.status >= 300) {
                err({"message": xhr.response});
            } else {
                done();
            }
        });
    }

    static getGroupById(groupId) {
        return new Promise((done, err) => {

            const xhr = new XMLHttpRequest();
            xhr.open('POST', `${Config.API_URL}/groups/getGroupById`, false);
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

    static isUserAdminForGroup(groupId) {
        return new Promise((done, err) => {

            const xhr = new XMLHttpRequest();
            xhr.open('POST', `${Config.API_URL}/groups/isUserAdminForGroup`, false);
            xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
            xhr.setRequestHeader('Content-Type', 'application/json');

            xhr.send(JSON.stringify(groupId));

            if (xhr.response != null) {
                done(JSON.parse(xhr.responseText));
            } else {
                done(JSON.parse(xhr.response));
            }
        });
    }

    static inviteMembers(members) {
        return new Promise((done, err) => {

            const xhr = new XMLHttpRequest();
            xhr.open('POST', `${Config.API_URL}/groups/inviteMembers`, false);
            xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
            xhr.setRequestHeader('Content-Type', 'application/json');

            xhr.send(JSON.stringify(members));
            if (xhr.status >= 300) {
                err({"message": xhr.response});
            } else {
                done();
            }
        });
    }

    static inviteAdmins(admins) {
        return new Promise((done, err) => {

            const xhr = new XMLHttpRequest();
            xhr.open('POST', `${Config.API_URL}/groups/inviteAdmins`, false);
            xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
            xhr.setRequestHeader('Content-Type', 'application/json');

            xhr.send(JSON.stringify(admins));
            if (xhr.status >= 300) {
                err({"message": xhr.response});
            } else {
                done();
            }
        });
    }

    //this method is not called yet and not implemented in DAO, will use saveGroup for now
    static startOrderForGroup(groupId) {
        return new Promise((done, err) => {

            const xhr = new XMLHttpRequest();
            xhr.open('POST', `${Config.API_URL}/groups/startOrderForGroup`, false);
            xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
            xhr.setRequestHeader('Content-Type', 'application/json');

            xhr.send(JSON.stringify(groupId));
            if (xhr.status >= 300) {
                err({"message": xhr.response});
            } else {
                done();
            }
        });
    }
}