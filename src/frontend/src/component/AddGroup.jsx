import Config from "../config/Config.js";
import React from 'react';
import GroupService from '../service/GroupService.js';
import {userStore} from '../store/UserStore.js';
import {hashHistory} from 'react-router';

export default class AddGroup extends React.Component {

    state = {
        loaded: true,
        errorMessage: null,
        showErrorMessage: false,
        groupName: "",
        adminEmail: "",
        memberList: [],
        adminList: [],
        newMemberInput: "",
        newAdminInput: "",
        notifyMembers: false,
        groupId: null,
        recentlyAddedAdminList: [],
        recentlyAddedMemberList: [],
        activeGroup: false
    };

    componentWillMount() {
        this.getGroupFromID();
    }

    // -------------------------------------
    // ---------BE OPERATIONS---------------
    // -------------------------------------

    getGroupFromID() {
        let groupIdParam = this.props.params.groupId;

        if (groupIdParam != 0) {
            GroupService.getGroupById(groupIdParam).then((group) => {

                this.setState({
                    groupId: groupIdParam,
                    adminEmail: userStore.getUserInfo().email,
                    groupName: group.name,
                    memberList: group.members,
                    adminList: group.admins,
                    notifyMembers: group.notifyMembers,
                    activeGroup: group.hasActiveOrder
                });

            });
        } else {
            this.setState({
                adminEmail: userStore.getUserInfo().email,
                adminList: [userStore.getUserInfo().email]
            });
        }
    }

    saveGroup() {
        this.setState({
            loaded: false
        });
        let groupId;
        if (this.state.groupId != 0) {
            groupId = this.state.groupId;
        } else {
            groupId = null;
        }

        let group = {
            id: groupId,
            name: this.state.groupName,
            admins: this.state.adminList,
            members: this.state.memberList,
            notifyMembers: this.state.notifyMembers,
            hasActiveOrder: this.state.activeGroup
        };

        GroupService.saveNewGroup(group).then(() => {
            this.sendInvitationsToNewUsers();

            this.setState({
                loaded: true
            });
            userStore.setAdmin(true);
            hashHistory.push(Config.MAIN_PAGE_PATH);
        }).catch((err) => {
            this.setState({
                loaded: true,
                errorMessage: err.message
            });
        });
    }

    sendInvitationsToNewUsers() {
        if (this.state.recentlyAddedAdminList != null
            && this.state.recentlyAddedAdminList.length > 0) {
            GroupService.inviteAdmins(this.state.recentlyAddedAdminList);
        }
        if (this.state.recentlyAddedMemberList != null
            && this.state.recentlyAddedMemberList.length > 0) {
            GroupService.inviteMembers(this.state.recentlyAddedMemberList);
        }
    }

    // -------------------------------------
    // ---------SET STATE METHODS-----------
    // -------------------------------------

    updateMemberInput(ev) {
        this.setState({
            newMemberInput: ev.target.value
        });
    }

    updateAdminInput(ev) {
        this.setState({
            newAdminInput: ev.target.value
        });
    }

    updateGroupName(ev) {
        this.setState({
            groupName: ev.target.value
        });
    }

    addMember(ev) {
        ev.stopPropagation();
        ev.preventDefault();

        let list = this.state.memberList;
        let newMembers = this.state.recentlyAddedMemberList;

        if ((list == null) || (typeof list == 'undefined')) {
            list = [];
        }
        if ((newMembers == null) || (typeof newMembers == 'undefined')) {
            newMembers = [];
        }

        list.push(this.state.newMemberInput);
        newMembers.push(this.state.newMemberInput);

        this.setState({
            memberList: list,
            newMemberInput: "",
            recentlyAddedMemberList: newMembers
        });
    }

    addAdmin(ev) {
        ev.stopPropagation();
        ev.preventDefault();

        let list = this.state.adminList;
        let newAdmins = this.state.recentlyAddedAdminList;

        newAdmins.push(this.state.newAdminInput);
        list.push(this.state.newAdminInput);

        this.setState({
            adminList: list,
            newAdminInput: "",
            recentlyAddedAdminList: newAdmins
        });
    }

    updateNotificationToggle(ev) {
        this.setState({
            notifyMembers: ev.target.checked
        })
    }

    removeMemberFromGroup(email, ev) {
        let members = this.state.memberList;
        let recentMembers = this.state.recentlyAddedMemberList;

        let memberIndex = members.indexOf(email);
        let recentIndex = recentMembers.indexOf(email);

        members.splice(memberIndex, 1);
        recentMembers.splice(recentIndex, 1);

        this.setState({
            memberList: members,
            recentMembers: recentMembers
        });
    }

    removeAdminFromGroup(email, ev) {
        let list = this.state.adminList;
        let recentAdmins = this.state.recentlyAddedAdminList;

        let index = list.indexOf(email);
        let recentIndex = recentAdmins.indexOf(email);

        list.splice(index, 1);
        recentAdmins.splice(recentIndex, 1);

        this.setState({
            adminList: list,
            recentlyAddedAdminList: recentAdmins
        });

    }

    // -------------------------------------
    // ---------RENDER METHODS--------------
    // -------------------------------------

    showMembers() {
        if (this.state.memberList != null) {
            return this.state.memberList.map((member, i) => {
                return (
                    <div key={i} className="content">
                        <div className="ui three column grid">
                            <div className="row">

                                <div className="eight wide column">
                                    <i className="smile icon"/>
                                    {member}

                                </div>
                                <div className="two wide column">
                                    <button className="ui mini basic circular icon button"
                                            onClick={ this.removeMemberFromGroup.bind(this, member) }>
                                        <i className="minus red icon"/>
                                    </button>
                                </div>

                                <div className="six wide column"/>

                            </div>
                        </div>
                    </div>
                );
            });
        }
    }

    showAdmins() {
        if (this.state.adminList != null) {
            return this.state.adminList.map((admin, i) => {
                return (
                    <div key={i} className="content">
                        <div className="ui three column grid">
                            <div className="row">

                                <div className="eight wide column">
                                    <i className="settings icon"/>
                                    {admin}
                                </div>

                                <div className="two wide column">
                                    { this.renderRemoveAdminButton(i, admin) }
                                </div>

                                <div className="six wide column"/>

                            </div>
                        </div>
                    </div>
                );
            });
        }
    }

    renderRemoveAdminButton(i, admin) {
        if (i > 0) {
            return (
                <button className="ui mini basic circular icon button"
                        onClick={ this.removeAdminFromGroup.bind(this, admin) }>
                    <i className="minus red icon"/>
                </button>
            );
        }
    }

    renderErrorMessage() {
        if (this.state.errorMessage != null) {
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
            <h2>Add a new group</h2>
            <div className="ui form">

                <div className={this.renderErrorMessage()}>
                    <div className="header">
                        An error occurred
                    </div>
                    <p>{this.state.errorMessage}</p>
                </div>

                <div className="field">
                    <label>Group Name</label>
                    <input type="text" name="group-name" value={this.state.groupName} placeholder="Group Name"
                           onChange={this.updateGroupName.bind(this)} maxLength="10"/>
                </div>
                <div className="field">
                    <label>Group administrators</label>
                    <div className="ui two column grid">
                        <div className="row">
                            <div className="fourteen wide column">
                                <input type="email" name="admin-email" placeholder="Admin email"
                                       value={this.state.newAdminInput} onChange={this.updateAdminInput.bind(this)}/>
                            </div>
                            <div className="two wide column">
                                <button className="ui small basic icon button" onClick={this.addAdmin.bind(this)}
                                        onKeyPress="">
                                    <i className="checkmark icon green"/>
                                </button>
                            </div>
                        </div>
                    </div>
                    <div className="ui list">
                        { this.showAdmins() }
                    </div>
                </div>
                <div className="ui divider"/>
                <div className="field">
                    <label>Members</label>
                    <div className="ui two column grid">
                        <div className="row">
                            <div className="fourteen wide column">
                                <input type="email" name="member-email" placeholder="Group member email"
                                       value={this.state.newMemberInput} onChange={this.updateMemberInput.bind(this)}/>
                            </div>
                            <div className="two wide column">
                                <button className="ui small basic icon button" onClick={this.addMember.bind(this)}
                                        onKeyPress="">
                                    <i className="checkmark icon green"/>
                                </button>
                            </div>
                        </div>
                    </div>
                    <div className="ui list">
                        { this.showMembers() }
                    </div>
                </div>
                <h4 className="ui horizontal divider header">
                    <i className="alarm icon"/> Notifications
                </h4>
                <div className="field">
                    <div className="ui toggle checkbox">
                        <input type="checkbox" checked={this.state.notifyMembers}
                               onChange={this.updateNotificationToggle.bind(this)}/>
                        <label>I would like to notify the members of this group by email when an administrator sends the
                            food order. ( doesn't work yet :) )</label>
                    </div>
                </div>
                <div className="ui divider"/>
                <button className="ui green button" onClick={this.saveGroup.bind(this)}>Submit</button>
            </div>
            <div>{this.props.children}</div>
        </div>;
    }
}