'use strict';

var utils = require('../utils/writer.js');
var User = require('../service/UserService');

module.exports.usersPOST = function usersPOST (req, res, next, body, xUID) {
  User.usersPOST(body, xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.usersUserIDDELETE = function usersUserIDDELETE (req, res, next, xUID, userID) {
  User.usersUserIDDELETE(xUID, userID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.usersUserIDGET = function usersUserIDGET (req, res, next, xUID, userID) {
  User.usersUserIDGET(xUID, userID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.usersUserIDPUT = function usersUserIDPUT (req, res, next, body, userID, xUID) {
  User.usersUserIDPUT(body, userID, xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};
