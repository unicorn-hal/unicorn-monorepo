'use strict';

var utils = require('../utils/writer.js');
var Account = require('../service/AccountService');

module.exports.accountsDELETE = function accountsDELETE (req, res, next, xUID) {
  Account.accountsDELETE(xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.accountsGET = function accountsGET (req, res, next, xUID) {
  Account.accountsGET(xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.accountsPOST = function accountsPOST (req, res, next, body, xUID) {
  Account.accountsPOST(body, xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.accountsPUT = function accountsPUT (req, res, next, body, xUID) {
  Account.accountsPUT(body, xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};
