'use strict';

var utils = require('../utils/writer.js');
var Firebase = require('../service/FirebaseService');

module.exports.firebaseAccountsPOST = function firebaseAccountsPOST (req, res, next, body, xUID) {
  Firebase.firebaseAccountsPOST(body, xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.firebaseAccountsRobotIDDELETE = function firebaseAccountsRobotIDDELETE (req, res, next, xUID, robotID) {
  Firebase.firebaseAccountsRobotIDDELETE(xUID, robotID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};
