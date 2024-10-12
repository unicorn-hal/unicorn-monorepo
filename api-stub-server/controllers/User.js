'use strict';

var utils = require('../utils/writer.js');
var User = require('../service/UserService');

module.exports.userDELETE = function userDELETE (req, res, next, xUID) {
  User.userDELETE(xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.userGET = function userGET (req, res, next, xUID) {
  User.userGET(xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.userPOST = function userPOST (req, res, next, body, xUID) {
  User.userPOST(body, xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.usersUserUidProfilesDELETE = function usersUserUidProfilesDELETE (req, res, next, xUID, userUid) {
  User.usersUserUidProfilesDELETE(xUID, userUid)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.usersUserUidProfilesGET = function usersUserUidProfilesGET (req, res, next, xUID, userUid) {
  User.usersUserUidProfilesGET(xUID, userUid)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.usersUserUidProfilesPOST = function usersUserUidProfilesPOST (req, res, next, body, userUid, xUID) {
  User.usersUserUidProfilesPOST(body, userUid, xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.usersUserUidProfilesPUT = function usersUserUidProfilesPUT (req, res, next, body, userUid, xUID) {
  User.usersUserUidProfilesPUT(body, userUid, xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};
