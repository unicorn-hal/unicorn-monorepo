'use strict';

var utils = require('../utils/writer.js');
var HealthCheckup = require('../service/HealthCheckupService');

module.exports.health_checkupsHealthCheckupIDDELETE = function health_checkupsHealthCheckupIDDELETE (req, res, next, xUID, healthCheckupID) {
  HealthCheckup.health_checkupsHealthCheckupIDDELETE(xUID, healthCheckupID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.health_checkupsPOST = function health_checkupsPOST (req, res, next, body, xUID) {
  HealthCheckup.health_checkupsPOST(body, xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.usersUserIDHealth_checkupsGET = function usersUserIDHealth_checkupsGET (req, res, next, xUID, userID) {
  HealthCheckup.usersUserIDHealth_checkupsGET(xUID, userID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.usersUserIDHealth_checkupsHealthCheckupIDGET = function usersUserIDHealth_checkupsHealthCheckupIDGET (req, res, next, xUID, healthCheckupID, userID) {
  HealthCheckup.usersUserIDHealth_checkupsHealthCheckupIDGET(xUID, healthCheckupID, userID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.usersUserIDHealth_checkupsHealthCheckupIDPUT = function usersUserIDHealth_checkupsHealthCheckupIDPUT (req, res, next, body, healthCheckupID, userID, xUID) {
  HealthCheckup.usersUserIDHealth_checkupsHealthCheckupIDPUT(body, healthCheckupID, userID, xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};
