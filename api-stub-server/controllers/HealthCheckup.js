'use strict';

var utils = require('../utils/writer.js');
var HealthCheckup = require('../service/HealthCheckupService');

module.exports.health_checkupsGET = function health_checkupsGET (req, res, next, xUID) {
  HealthCheckup.health_checkupsGET(xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.health_checkupsHealthCheckupIDDELETE = function health_checkupsHealthCheckupIDDELETE (req, res, next, xUID, healthCheckupID) {
  HealthCheckup.health_checkupsHealthCheckupIDDELETE(xUID, healthCheckupID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.health_checkupsHealthCheckupIDGET = function health_checkupsHealthCheckupIDGET (req, res, next, xUID, healthCheckupID) {
  HealthCheckup.health_checkupsHealthCheckupIDGET(xUID, healthCheckupID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.health_checkupsHealthCheckupIDPUT = function health_checkupsHealthCheckupIDPUT (req, res, next, body, healthCheckupID, xUID) {
  HealthCheckup.health_checkupsHealthCheckupIDPUT(body, healthCheckupID, xUID)
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
