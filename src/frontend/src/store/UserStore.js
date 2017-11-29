class UserStore {
    userInfo = null;

    setUserInfo(userInfo) {
        this.userInfo = userInfo;
    }

    getUserInfo() {
        return this.userInfo;
    }

    isAdmin() {
        return this.userInfo.isAdmin;
    }

    setAdmin(isAdmin) {
        this.userInfo.isAdmin = isAdmin;
    }

    getUserEmail() {
        return this.userInfo.email;
    }
}

export const userStore = new UserStore();
